package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.api.action.ItemRepeatableUseAction;
import io.socol.betterthirdperson.api.action.MouseAction;
import io.socol.betterthirdperson.impl.ClientAdapter;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {
    @Shadow
    private int rightClickDelay;
    @Shadow
    @Final
    public Options options;

    @Shadow
    protected abstract boolean startAttack();

    @Shadow
    protected abstract void startUseItem();

    @Shadow
    protected abstract void continueAttack(boolean var1);

    @Inject(method={"handleKeybinds"}, at=@At(value="HEAD"))
    public void onHandleInputEvents(CallbackInfo ci) {
        PlayerAdapter player = new PlayerAdapter((LocalPlayer)Minecraft.getInstance().player);
        BetterThirdPerson.getCameraManager().onInputEvents(player);
    }

    @Inject(method={"runTick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GameRenderer;render(Lnet/minecraft/client/DeltaTracker;Z)V", shift=At.Shift.BEFORE))
    public void preRenderHook(boolean tick, CallbackInfo ci) {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        PlayerAdapter playerAdapter = new PlayerAdapter((LocalPlayer)player);
        float tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        BetterThirdPerson.getCameraManager().onRenderTickStart(playerAdapter, tickDelta);
    }

    @Inject(method={"startAttack"}, at=@At(value="HEAD"), cancellable=true)
    public void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        PlayerAdapter playerAdapter = new PlayerAdapter((LocalPlayer)player);
        MouseAction action = new MouseAction(this::startAttack);
        if (BetterThirdPerson.getCameraManager().onMouseAction(playerAdapter, action)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method={"continueAttack"}, at=@At(value="HEAD"), cancellable=true)
    public void onBlockBreaking(boolean pressed, CallbackInfo ci) {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        PlayerAdapter playerAdapter = new PlayerAdapter((LocalPlayer)player);
        MouseAction action = new MouseAction(() -> this.continueAttack(true));
        if (pressed && BetterThirdPerson.getCameraManager().onMouseAction(playerAdapter, action)) {
            ci.cancel();
        }
    }

    @Redirect(method={"handleKeybinds"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;startUseItem()V", ordinal=0))
    public void onDoItemUse(Minecraft client) {
        AbstractClientPlayer player = client.player;
        if (player == null) {
            return;
        }
        PlayerAdapter playerAdapter = new PlayerAdapter((LocalPlayer)player);
        MouseAction action = new MouseAction(this::startUseItem);
        if (!BetterThirdPerson.getCameraManager().onMouseAction(playerAdapter, action)) {
            this.startUseItem();
        }
    }

    @Redirect(method={"handleKeybinds"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;startUseItem()V", ordinal=1))
    public void onItemUseRepeatable(Minecraft client) {
        AbstractClientPlayer player = client.player;
        if (player == null) {
            return;
        }
        PlayerAdapter playerAdapter = new PlayerAdapter((LocalPlayer)player);
        ItemRepeatableUseAction action = new ItemRepeatableUseAction(ClientAdapter.INSTANCE, () -> this.rightClickDelay, this::startUseItem);
        if (!BetterThirdPerson.getCameraManager().onMouseAction(playerAdapter, action)) {
            this.startUseItem();
        }
    }

    @Inject(method={"handleKeybinds"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/Options;setCameraType(Lnet/minecraft/client/CameraType;)V", shift=At.Shift.AFTER))
    public void onSetPerspective(CallbackInfo ci) {
        if (!BetterThirdPerson.getCameraManager().getConfig().skipThirdPersonFrontView()) {
            return;
        }
        if (this.options.getCameraType() == CameraType.THIRD_PERSON_FRONT) {
            this.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
    }
}
