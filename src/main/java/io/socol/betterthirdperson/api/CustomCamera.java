/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_243
 */
package io.socol.betterthirdperson.api;

import io.socol.betterthirdperson.api.CustomCameraManager;
import io.socol.betterthirdperson.api.TickPhase;
import io.socol.betterthirdperson.api.adapter.IClientAdapter;
import io.socol.betterthirdperson.api.adapter.IMovementInputAdapter;
import io.socol.betterthirdperson.api.adapter.IPlayerAdapter;
import io.socol.betterthirdperson.api.config.CustomCameraConfig;
import io.socol.betterthirdperson.api.util.AngleUtils;
import io.socol.betterthirdperson.api.util.Rotation;
import net.minecraft.world.phys.Vec3;

public class CustomCamera {
    private final CustomCameraManager manager;
    private final IClientAdapter client;
    private float followYaw;
    private Rotation cameraRotation;
    private Rotation playerRotation;
    private Vec3 lastTickPlayerPos;
    private float targetKeyboardInputYaw;
    private float keyboardInputYaw = 0.0f;
    private Rotation mouseInput = Rotation.ZERO;
    private boolean wasMoving = false;
    private boolean isMoving = false;
    private boolean delayMouseActions;
    private long aimTicks = 0L;

    public CustomCamera(CustomCameraManager manager, IClientAdapter client, IPlayerAdapter player, CustomCameraConfig config) {
        this.manager = manager;
        this.client = client;
        this.resetToPlayerView(player);
        this.lastTickPlayerPos = player.getPosition();
    }

    public void handleMovementInputs(IMovementInputAdapter input, TickPhase phase) {
        this.isMoving = input.isMoving();
        if (this.aimTicks > 0L || !this.isMoving) {
            return;
        }
        if (phase == TickPhase.START) {
            this.targetKeyboardInputYaw = input.getInputDirection();
            input.redirect(0.0f);
        } else if (input.getMoveForward() <= 0.0f || input.getMoveStrafe() != 0.0f) {
            this.targetKeyboardInputYaw = input.getRawDirection();
            input.redirect(0.0f);
        }
    }

    private void doAim() {
        this.aimTicks = this.manager.getConfig().getAimDuration();
    }

    public void handlePlayerTurn(float deltaYaw, float deltaPitch) {
        this.mouseInput = this.mouseInput.add(deltaYaw, deltaPitch);
    }

    public void tick(TickPhase phase, IPlayerAdapter player) {
        Vec3 currentPos = player.getPosition();
        boolean mousePressed = this.client.isMousePressed();
        if (phase == TickPhase.START) {
            if (!player.isPassenger() && !this.lastTickPlayerPos.equals((Object)currentPos)) {
                this.resetToPlayerView(player);
            }
            this.wasMoving = this.isMoving;
            if (mousePressed && this.manager.getConfig().shouldAimPlayerOnInteract()) {
                this.doAim();
            } else if (this.aimTicks > 0L) {
                --this.aimTicks;
                if (this.aimTicks == 0L) {
                    this.keyboardInputYaw = 0.0f;
                }
            }
        } else {
            this.lastTickPlayerPos = currentPos;
            if (this.isMoving) {
                if (this.aimTicks <= 0L && !this.wasMoving) {
                    this.keyboardInputYaw = -AngleUtils.normalize(this.cameraRotation.getYaw() - this.playerRotation.getYaw());
                }
                float rotationSpeed = (float)this.manager.getConfig().getPlayerRotationSpeed() / 100.0f;
                this.keyboardInputYaw = AngleUtils.smoothAngle(rotationSpeed, this.keyboardInputYaw, this.targetKeyboardInputYaw);
            }
            if (!this.isMoving && !mousePressed) {
                float pitchChangeSpeed = (float)this.manager.getConfig().getPitchChangeSpeed() / 100.0f;
                this.playerRotation = this.playerRotation.withPitch(AngleUtils.smoothAngle(pitchChangeSpeed, this.playerRotation.getPitch(), this.cameraRotation.getPitch()));
            }
        }
    }

    public void setup(IClientAdapter client, IPlayerAdapter player, float partialTicks) {
        this.cameraRotation = this.cameraRotation.add(this.mouseInput);
        if (this.aimTicks > 0L) {
            this.playerRotation = this.cameraRotation;
        } else if (this.isMoving) {
            this.playerRotation = this.cameraRotation.addYaw(this.keyboardInputYaw);
        } else if (this.manager.getConfig().getFollowYaw() > 0 && !client.isMousePressed()) {
            if (Math.signum(this.mouseInput.getYaw()) != Math.signum(this.followYaw)) {
                this.followYaw = 0.0f;
            }
            this.followYaw += this.mouseInput.getYaw();
            if (Math.abs(this.followYaw) <= (float)this.manager.getConfig().getFollowYaw()) {
                this.playerRotation = this.playerRotation.addYaw((float)((double)this.mouseInput.getYaw() * (1.0 - this.easeInExpo(Math.abs(this.followYaw) / (float)this.manager.getConfig().getFollowYaw()))));
            }
        }
        this.playerRotation.applySafe(player);
        this.mouseInput = Rotation.ZERO;
    }

    public float getYaw() {
        return this.cameraRotation.getYaw();
    }

    public float getPitch() {
        return this.cameraRotation.getPitch();
    }

    public void handleMouseReset() {
        this.delayMouseActions = false;
    }

    public boolean handleMouseAction(IPlayerAdapter player, IClientAdapter client) {
        if (this.delayMouseActions) {
            return true;
        }
        if (this.aimTicks == 0L && this.manager.getConfig().shouldAimPlayerOnInteract()) {
            this.doAim();
            this.cameraRotation.applySafe(player);
            client.updateHitResult();
            this.resetToPlayerView(player);
            this.delayMouseActions = true;
            return true;
        }
        return false;
    }

    private void resetToPlayerView(IPlayerAdapter player) {
        this.cameraRotation = this.playerRotation = player.getRotation();
        this.followYaw = 0.0f;
    }

    private double easeInExpo(double x) {
        return x == 0.0 ? 0.0 : Math.pow(2.0, 10.0 * x - 10.0);
    }

    public void onDisable(IPlayerAdapter player) {
        this.cameraRotation.applySafeFully(player);
    }

    public Rotation getCameraRotation() {
        return this.cameraRotation;
    }

    public Rotation getPlayerRotation() {
        return this.playerRotation;
    }

    public CustomCamera setCameraRotation(Rotation cameraRotation) {
        this.cameraRotation = cameraRotation;
        return this;
    }
}

