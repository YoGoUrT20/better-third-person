package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.api.TickPhase;
import io.socol.betterthirdperson.impl.MovementInputAdapter;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Inject(method={"tick"}, at=@At(value="TAIL"))
    public void tickHook(CallbackInfo ci) {
        if (BetterThirdPerson.getCameraManager().hasCustomCamera()) {
            PlayerAdapter player = new PlayerAdapter((LocalPlayer)Minecraft.getInstance().player);
            MovementInputAdapter inputs = new MovementInputAdapter((ClientInput)(Object)this);
            BetterThirdPerson.getCameraManager().handleMovementInputs(player, inputs, TickPhase.START);
        }
    }
}
