package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.api.TickPhase;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class PlayerEntityMixin {
    @Inject(method={"tick"}, at=@At(value="HEAD"))
    public void onPrePlayerTick(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer)(Object)this;
        BetterThirdPerson.getCameraManager().onPlayerTick(new PlayerAdapter(player), TickPhase.START);
    }

    @Inject(method={"tick"}, at=@At(value="TAIL"))
    public void onPostPlayerTick(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer)(Object)this;
        BetterThirdPerson.getCameraManager().onPlayerTick(new PlayerAdapter(player), TickPhase.END);
    }
}
