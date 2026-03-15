package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.api.TickPhase;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method={"turn"}, at=@At(value="HEAD"))
    public void preTurnHook(CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof AbstractClientPlayer) {
            BetterThirdPerson.getCameraManager().onPlayerTurn(TickPhase.START, new PlayerAdapter((LocalPlayer)((AbstractClientPlayer)entity)));
        }
    }

    @Inject(method={"turn"}, at=@At(value="TAIL"))
    public void postTurnHook(CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof AbstractClientPlayer) {
            AbstractClientPlayer player = (AbstractClientPlayer)entity;
            if (BetterThirdPerson.getCameraManager().onPlayerTurn(TickPhase.END, new PlayerAdapter((LocalPlayer)player)) && player.getVehicle() != null) {
                player.getVehicle().positionRider(player);
            }
        }
    }
}
