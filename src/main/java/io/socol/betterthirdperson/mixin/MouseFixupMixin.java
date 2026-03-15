package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.api.util.Rotation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value=MouseHandler.class, priority=100000)
public class MouseFixupMixin {
    @Shadow
    @Mutable
    private double accumulatedDX;
    @Shadow
    @Mutable
    private double accumulatedDY;

    @Inject(method={"turnPlayer"}, at=@At(value="HEAD"))
    public void preTurnPlayer(CallbackInfo ci) {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        Rotation turnValues = BetterThirdPerson.getCameraManager().restorePlayerTurnValues();
        if (turnValues != null) {
            this.accumulatedDX = turnValues.getYaw();
            this.accumulatedDY = turnValues.getPitch();
        }
    }
}
