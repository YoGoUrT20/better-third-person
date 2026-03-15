/*
 * Decompiled with CFR 0.152.
 */
package io.socol.betterthirdperson;

import io.socol.betterthirdperson.api.CustomCameraManager;
import io.socol.betterthirdperson.impl.ClientAdapter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class BetterThirdPerson implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "betterthirdperson";
    private static CustomCameraManager manager = new CustomCameraManager(ClientAdapter.INSTANCE);

    public static CustomCameraManager getCameraManager() {
        return manager;
    }

    @Override
    public void onInitialize() {
    }

    @Override
    public void onInitializeClient() {
    }
}

