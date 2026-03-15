/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_243
 */
package io.socol.betterthirdperson.api.adapter;

import io.socol.betterthirdperson.api.util.AngleUtils;
import io.socol.betterthirdperson.api.util.Rotation;
import net.minecraft.world.phys.Vec3;

public interface IPlayerAdapter {
    public float getRotationYaw();

    public float getRotationPitch();

    public float getPrevRotationYaw();

    public float getPrevRotationPitch();

    public void setRotationYaw(float var1);

    public void setRotationPitch(float var1);

    public void setPrevRotationYaw(float var1);

    public void setPrevRotationPitch(float var1);

    public void setVehicleYaw(float var1);

    public Vec3 getPosition();

    public boolean isPassenger();

    public boolean isUsingItem();

    public boolean hasAllowedVehicle();

    public boolean isElytraFlying();

    default public void setRotationYawSafe(float value) {
        float fixedValue = AngleUtils.wrapAngle(this.getRotationYaw(), value);
        this.setRotationYaw(fixedValue);
        if (this.isPassenger()) {
            this.setVehicleYaw(fixedValue);
        }
    }

    default public Rotation getRotation() {
        return new Rotation(this.getRotationYaw(), this.getRotationPitch());
    }
}

