/*
 * Decompiled with CFR 0.152.
 */
package io.socol.betterthirdperson.api.adapter;

import io.socol.betterthirdperson.api.util.AngleUtils;
import io.socol.betterthirdperson.api.util.WalkDirection;

public interface IMovementInputAdapter {
    public boolean isLeftKeyDown();

    public boolean isRightKeyDown();

    public boolean isForwardKeyDown();

    public boolean isBackKeyDown();

    public void setLeftKeyDown(boolean var1);

    public void setRightKeyDown(boolean var1);

    public void setForwardKeyDown(boolean var1);

    public void setBackKeyDown(boolean var1);

    public float getMoveForward();

    public float getMoveStrafe();

    public void setMoveForward(float var1);

    public void setMoveStrafe(float var1);

    default public boolean isMoving() {
        return this.isForwardKeyDown() != this.isBackKeyDown() || this.isLeftKeyDown() != this.isRightKeyDown();
    }

    default public float getInputDirection() {
        return WalkDirection.byInput(this).getAngle();
    }

    default public float getRawDirection() {
        return AngleUtils.normalize((float)Math.toDegrees(Math.atan2(-this.getMoveStrafe(), this.getMoveForward())));
    }

    default public void setMoveDirection(float angle) {
        float moveImpulse = Math.max(Math.abs(this.getMoveForward()), Math.abs(this.getMoveStrafe()));
        double angleRad = Math.toRadians(angle);
        this.setMoveForward((float)Math.cos(angleRad) * moveImpulse);
        this.setMoveStrafe((float)(-Math.sin(angleRad)) * moveImpulse);
    }

    default public void redirect(float angle) {
        this.setMoveDirection(angle);
        WalkDirection.approximate(angle).applyKeys(this);
    }
}

