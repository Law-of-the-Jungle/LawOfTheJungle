package com.game.junglelaw.circle;

import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 */
public abstract class MovableCircle extends AbstractCircle {

    private static final String LOG_TAG = MovableCircle.class.getSimpleName();

    protected static final float DEAFULT_SPEED = 10;

    protected PointF mMovingDirection; // mMovingDirection vector size should be 1 (i.e. has a unit distance)
    protected float mMovingSpeed;

    public MovableCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color);
        mMovingDirection = new PointF((float) Math.random(), (float) Math.random());
    }

    public void setmMovingSpeed(float new_speed) {
        mMovingSpeed = new_speed;
    }

    public float getmMovingSpeed() {
        return mMovingSpeed;
    }

    public void setmMovingDirection(PointF newDirection) {
        float len = newDirection.length(); // normalization
        mMovingDirection.set(newDirection.x / len, newDirection.y / len);
    }

    public PointF getmMovingDirection() {
        return mMovingDirection;
    }

    public void setDirectTowardPoint(PointF targetPoint) {
        setmMovingDirection(new PointF(targetPoint.x - mCenter.x, targetPoint.y - mCenter.y));
    }

    /**
     * Moves forward to the mMovingDirection with current mMovingSpeed
     */
    public void moveToDirection(int width, int height) {
        mMovingSpeed = DEAFULT_SPEED / (mRadius / 40);
        float newX = mCenter.x + mMovingSpeed * mMovingDirection.x;
        float newY = mCenter.y + mMovingSpeed * mMovingDirection.y;

        if ((newX > 0 && newX < width) && (newY > 0 && newY < height)) {
            setCenter(newX, newY);
        }
    }
}