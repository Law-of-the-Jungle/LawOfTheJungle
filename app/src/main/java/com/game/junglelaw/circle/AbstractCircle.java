package com.game.junglelaw.circle;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 * <p/>
 * Declares the common properties of all circles.
 */
public abstract class AbstractCircle {

    private static final String LOG_TAG = AbstractCircle.class.getSimpleName();

    protected final double DIGEST_RATE = 0.7;
    protected int color;
    protected float radius;
    protected float mass;
    protected PointF center;

    protected AbstractCircle(float x, float y, float radius, int color) {
        this.center = new PointF(x, y);
        this.radius = radius;
        this.color = color;
        this.mass = radius * radius;
    }

    public void setCenter(float x, float y) {
        center.set(x, y);
    }

    public PointF getCenter() {
        return center;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return (float) Math.sqrt(mass);
    }

    public void addMass(float num) {
        mass += num * DIGEST_RATE;
    }

    public float getMass() {
        return mass;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String toString() {
        return "center: " + center.toString() + "; radius: " + radius;
    }


    // TODO
    public void draw(Canvas canvas) {
    }

}