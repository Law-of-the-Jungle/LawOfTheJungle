package com.game.junglelaw.circle;

import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 *
 * Declares the common properties of all circles.
 */
public abstract class AbstractCircle {

    private static final String LOG_TAG = AbstractCircle.class.getSimpleName();

    protected PointF mCenter;
    protected int mColor;
    protected float mRadius;

    protected AbstractCircle(float x, float y, float radius, int ccolor) {
        mCenter = new PointF(x, y);
        mRadius = radius;
        mColor = ccolor;
    }

    public void setCenter(float x, float y) {
        mCenter = new PointF(x, y);
    }

    public PointF getmCenter() {
        return mCenter;
    }

    public float getmRadius() {
        return mRadius;
    }

    public int getmColor() {
        return mColor;
    }

    @Override
    public String toString() {
        return "mCenter = " + mCenter + "; mRadius = " + mRadius;
    }
}