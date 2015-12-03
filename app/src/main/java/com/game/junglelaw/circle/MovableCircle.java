package com.game.junglelaw.circle;

import android.graphics.PointF;

import com.game.junglelaw.Utility;

/**
 * Created by apple on 10/15/15.
 */
public abstract class MovableCircle extends AbstractCircle {

    private static final String LOG_TAG = MovableCircle.class.getSimpleName();

    public static final float ABSORB_THREASHOLD_RATE = (float) 1.1;

    protected static final float DIGEST_RATE = (float) 0.5; // portion of the absorbed circle mass can be added into the absorber circle
    protected static final float DEAFULT_MOVING_SPEED = 10;

    protected final float mMapWidth;
    protected final float mMapHeight;

    protected PointF mMovingDirection; // mMovingDirection vector length should be 1 (i.e. has a unit length)
    protected float mMovingSpeed;

    public MovableCircle(float x, float y, float radius, int color, float mapWidth, float mapHeight) {
        super(x, y, radius, color);
        mMapWidth = mapWidth;
        mMapHeight = mapHeight;

        mMovingDirection = new PointF(Utility.generateRandomFloat(0, mMapWidth), Utility.generateRandomFloat(0, mapHeight));
        mMovingSpeed = DEAFULT_MOVING_SPEED / (mRadius / PlayerCircle.PLAYER_CIRCLE_DEFAULT_RADIUS);
    }

    public void absorbCircle(AbstractCircle circle) {
        mRadius += (float) (Math.sqrt(circle.getmRadius()) * DIGEST_RATE);
        mMovingSpeed = DEAFULT_MOVING_SPEED / (mRadius / PlayerCircle.PLAYER_CIRCLE_DEFAULT_RADIUS);
    }

    public void setDirectTowardPoint(PointF targetPoint) {
        setmMovingDirection(new PointF(targetPoint.x - mCenter.x, targetPoint.y - mCenter.y));
    }

    public void setmMovingDirection(PointF newDirection) {
        float len = newDirection.length(); // normalization
        mMovingDirection.set(newDirection.x / len, newDirection.y / len);
    }

    public void moveToDirection() {
        float newX = mCenter.x + mMovingSpeed * mMovingDirection.x;
        float newY = mCenter.y + mMovingSpeed * mMovingDirection.y;

        // Guarantee never move out of the map border
        if ((newX > 0 && newX < mMapWidth) && (newY > 0 && newY < mMapHeight)) {
            setCenter(newX, newY);
        }
    }
}