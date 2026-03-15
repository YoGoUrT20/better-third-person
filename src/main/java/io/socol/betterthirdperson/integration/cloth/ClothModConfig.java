/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  me.shedaniel.autoconfig.AutoConfig
 *  me.shedaniel.autoconfig.ConfigData
 *  me.shedaniel.autoconfig.annotation.Config
 *  me.shedaniel.autoconfig.annotation.ConfigEntry$BoundedDiscrete
 *  me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
 *  me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment
 */
package io.socol.betterthirdperson.integration.cloth;

import io.socol.betterthirdperson.api.config.CustomCameraConfig;
import io.socol.betterthirdperson.api.config.DefaultCustomCameraConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name="betterthirdperson")
public class ClothModConfig
implements ConfigData,
CustomCameraConfig {
    @Comment(value="Align player to camera on left & right clicks")
    public boolean aimOnInteract = DefaultCustomCameraConfig.INSTANCE.shouldAimPlayerOnInteract();
    @Comment(value="How long player will be aligned to camera after left & right clicks")
    @ConfigEntry.BoundedDiscrete(min=10L, max=200L)
    public int aimDuration = DefaultCustomCameraConfig.INSTANCE.getAimDuration();
    @Comment(value="Angle in degrees within the player will slightly follow camera yaw (while standing still)")
    @ConfigEntry.BoundedDiscrete(min=0L, max=90L)
    public int followYaw = DefaultCustomCameraConfig.INSTANCE.getFollowYaw();
    @Comment(value="Does camera should rotate freely during elytra flight")
    public boolean freeCameraDuringElytraFlight = DefaultCustomCameraConfig.INSTANCE.hasFreeCameraDuringElytraFlight();
    @Comment(value="Completely remove third-person front view")
    public boolean skipThirdPersonFrontView = DefaultCustomCameraConfig.INSTANCE.skipThirdPersonFrontView();
    @Comment(value="How fast player changes movement direction in third-person")
    @ConfigEntry.BoundedDiscrete(min=10L, max=100L)
    public int playerRotationSpeed = DefaultCustomCameraConfig.INSTANCE.getPlayerRotationSpeed();
    @Comment(value="How fast player pitch follows camera pitch in third-person")
    @ConfigEntry.BoundedDiscrete(min=10L, max=100L)
    public int pitchChangeSpeed = DefaultCustomCameraConfig.INSTANCE.getPitchChangeSpeed();

    public static CustomCameraConfig create() {
        AutoConfig.register(ClothModConfig.class, JanksonConfigSerializer::new);
        return (CustomCameraConfig)AutoConfig.getConfigHolder(ClothModConfig.class).getConfig();
    }

    @Override
    public boolean shouldAimPlayerOnInteract() {
        return this.aimOnInteract;
    }

    @Override
    public int getAimDuration() {
        return this.aimDuration;
    }

    @Override
    public int getFollowYaw() {
        return this.followYaw;
    }

    @Override
    public boolean hasFreeCameraDuringElytraFlight() {
        return this.freeCameraDuringElytraFlight;
    }

    @Override
    public boolean skipThirdPersonFrontView() {
        return this.skipThirdPersonFrontView;
    }

    @Override
    public int getPlayerRotationSpeed() {
        return this.playerRotationSpeed;
    }

    @Override
    public int getPitchChangeSpeed() {
        return this.pitchChangeSpeed;
    }
}

