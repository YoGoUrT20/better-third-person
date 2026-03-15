/*
 * Decompiled with CFR 0.152.
 */
package io.socol.betterthirdperson.api.config;

import io.socol.betterthirdperson.api.config.CustomCameraConfig;

public class DefaultCustomCameraConfig
implements CustomCameraConfig {
    public static final CustomCameraConfig INSTANCE = new DefaultCustomCameraConfig();

    private DefaultCustomCameraConfig() {
    }

    @Override
    public boolean shouldAimPlayerOnInteract() {
        return true;
    }

    @Override
    public int getAimDuration() {
        return 40;
    }

    @Override
    public int getFollowYaw() {
        return 45;
    }

    @Override
    public boolean hasFreeCameraDuringElytraFlight() {
        return false;
    }

    @Override
    public boolean skipThirdPersonFrontView() {
        return false;
    }

    @Override
    public int getPlayerRotationSpeed() {
        return 50;
    }

    @Override
    public int getPitchChangeSpeed() {
        return 65;
    }
}

