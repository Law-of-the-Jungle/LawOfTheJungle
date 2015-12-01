package com.game.junglelaw.circle;

import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 *
 * Declares the common properties of all circles.
 */
public abstract class AbstractCircle {

    private static final String LOG_TAG = AbstractCircle.class.getSimpleName();

    protected static final double DIGEST_RATE = 0.5;

    protected PointF mCenter;
    protected int mCcolor;
    protected float mRadius;
    protected float mass;

    protected AbstractCircle(float x, float y, float radius, int ccolor) {
        mCenter = new PointF(x, y);
        mRadius = radius;
        mCcolor = ccolor;
        mass = radius * radius;
    }

    public void setCenter(float x, float y) {
        mCenter = new PointF(x, y);
    }

    public PointF getmCenter() {
        return mCenter;
    }

    public void setmRadius(float mRadius) {
        this.mRadius = mRadius;
    }

    public float getmRadius() {
        return mRadius;
    }

    public void addMass(float num) {
        mass += num * DIGEST_RATE;
        mRadius = (float) Math.sqrt(mass);
    }

    public float getMass() {
        return mass;
    }

    public void setmCcolor(int color) {
        this.mCcolor = color;
    }

    public int getmCcolor() {
        return mCcolor;
    }

    @Override
    public String toString() {
        return "mRadius = " + mRadius + "; mCenter = " + mCenter;
    }
}