package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value=MouseHandler.class, priority=-1000)
public class MouseMixin {
    @Shadow
    private double accumulatedDX;
    @Shadow
    private double accumulatedDY;

    @Inject(method={"turnPlayer"}, at=@At(value="HEAD"))
    public void preChangeLookDirection(CallbackInfo ci) {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        BetterThirdPerson.getCameraManager().startPlayerTurning(this.accumulatedDX, this.accumulatedDY);
    }

    @Inject(method={"turnPlayer"}, at=@At(value="TAIL"))
    public void postChangeLookDirection(CallbackInfo ci) {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        BetterThirdPerson.getCameraManager().stopPlayerTurning();
    }
}
