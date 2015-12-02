package com.game.junglelaw.circle;

import android.graphics.Color;
import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 */
public class PlayerCircle extends MovableCircle {

    private static final String LOG_TAG = PlayerCircle.class.getSimpleName();
    private static final float ON_SCREEN_RADIUS_SHRINK_RATE = (float) 0.8;

    public static final float PLAYER_CIRCLE_DEFAULT_RADIUS = 40;
    public static final int PLAYER_CIRCLE_DEFAULT_COLOR = Color.BLACK;

    private float mZoomRate; // the whole game view's zoom rate
    private float mOnScreenRadius; // player circle's on screen radius
    private boolean mIsAbsorbed;

    public PlayerCircle(float x, float y) {
        super(x, y, PLAYER_CIRCLE_DEFAULT_RADIUS, PLAYER_CIRCLE_DEFAULT_COLOR);
        mOnScreenRadius = mRadius;
        mZoomRate = mOnScreenRadius / mRadius;
    }

    public float getmZoomRate() {
        return mZoomRate;
    }

    public boolean getmIsAbsorbed() {
        return mIsAbsorbed;
    }

    public void setmIsAbsorbed(boolean isAbsorbed) {
        mIsAbsorbed = isAbsorbed;
    }

    public float getmOnScreenRadius() {
        return mOnScreenRadius;
    }

    public void newDirection(PointF userClickPosition, PointF screenCenter) {
        setmMovingDirection(new PointF(userClickPosition.x - screenCenter.x, userClickPosition.y - screenCenter.y));
    }

    public void updateZoomRate() {
        if (mOnScreenRadius >= 80) {
            mOnScreenRadius *= ON_SCREEN_RADIUS_SHRINK_RATE;
            mZoomRate = mOnScreenRadius / mRadius;

        } else {
            mOnScreenRadius = getmRadius() * mZoomRate;
        }
    }
}