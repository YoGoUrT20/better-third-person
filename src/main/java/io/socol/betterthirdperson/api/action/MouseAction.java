/*
 * Decompiled with CFR 0.152.
 */
package io.socol.betterthirdperson.api.action;

public class MouseAction {
    private final Runnable action;

    public MouseAction(Runnable action) {
        this.action = action;
    }

    public void play() {
        this.action.run();
    }
}

