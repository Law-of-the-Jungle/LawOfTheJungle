package com.game.junglelaw.circle;

import android.graphics.PointF;

import com.game.junglelaw.Utility;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by apple on 10/15/15.
 */
public abstract class MovableCircle extends AbstractCircle {

    private static final String LOG_TAG = MovableCircle.class.getSimpleName();

    protected static final float DEAFULT_SPEED = 10;

    protected PointF mMovingDirection; // mMovingDirection vector size should be 1 (i.e. has a unit distance)
    protected float mSpeed;

    public MovableCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color);
        mMovingDirection = new PointF((float) Math.random(), (float) Math.random());
    }

    public void setmSpeed(float new_speed) {
        mSpeed = new_speed;
    }

    public float getmSpeed() {
        return mSpeed;
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
     * Moves forward to the mMovingDirection with current mSpeed
     */
    public void moveToDirection(int width, int height) {
        mSpeed = DEAFULT_SPEED / (mRadius / 40);
        float newX = mCenter.x + mSpeed * mMovingDirection.x;
        float newY = mCenter.y + mSpeed * mMovingDirection.y;

        if ((newX > 0 && newX < width) && (newY > 0 && newY < height)) {
            setCenter(newX, newY);
        }
    }
}