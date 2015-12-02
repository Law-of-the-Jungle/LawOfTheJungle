package com.game.junglelaw.circle;

import android.graphics.PointF;

import com.game.junglelaw.GameView;

/**
 * Created by apple on 10/15/15.
 */
public class PlayerCircle extends MovableCircle {

    private static final String LOG_TAG = PlayerCircle.class.getSimpleName();

    private float mPlayerOnScreenRadius;
    private float mZoomRate;
    private boolean mIsAbsorbed;

    public PlayerCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color);
        mPlayerOnScreenRadius = radius;
        mZoomRate = calculateZoomRate();
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

    public float getmPlayerOnScreenRadius() {
        return mPlayerOnScreenRadius;
    }

    public void newDirection(PointF userClickPosition, PointF screenCenter) {
        setmMovingDirection(new PointF(userClickPosition.x - screenCenter.x, userClickPosition.y - screenCenter.y));
    }

    public void updateZoomRate(GameView view) {
        //switch the lan to a border
        if (mPlayerOnScreenRadius >= 80) {
            mPlayerOnScreenRadius *= 0.8;
            mZoomRate = calculateZoomRate();

        } else {
            mPlayerOnScreenRadius = getmRadius() * mZoomRate;
        }
    }

    /** 根据player circle 当前大小，计算player zoom rate */
    private float calculateZoomRate() {
        return mPlayerOnScreenRadius / getmRadius();
    }
}