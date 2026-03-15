package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Redirect(method={"setup"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/Entity;getViewYRot(F)F"))
    private float getYawHook(Entity entity, float tickDelta) {
        if (BetterThirdPerson.getCameraManager().hasCustomCamera()) {
            return BetterThirdPerson.getCameraManager().getCustomCamera().getYaw();
        }
        return entity != null ? entity.getViewYRot(tickDelta) : 0.0f;
    }

    @Redirect(method={"setup"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/Entity;getViewXRot(F)F"))
    private float getPitchHook(Entity entity, float tickDelta) {
        if (BetterThirdPerson.getCameraManager().hasCustomCamera()) {
            return BetterThirdPerson.getCameraManager().getCustomCamera().getPitch();
        }
        return entity != null ? entity.getViewXRot(tickDelta) : 0.0f;
    }
}
