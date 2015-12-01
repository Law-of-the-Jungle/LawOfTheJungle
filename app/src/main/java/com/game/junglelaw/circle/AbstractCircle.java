package com.game.junglelaw.circle;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 *
 * Declares the common properties of all circles.
 */
public abstract class AbstractCircle {

    protected static final double DIGEST_RATE = 0.5;

    protected PointF center;
    protected int color;
    protected float radius;
    protected float mass;

    protected AbstractCircle(float x, float y, float radius, int color) {
        center = new PointF(x, y);
        this.radius = radius;
        this.color = color;
        mass = radius * radius;
    }

    public void setCenter(float x, float y) {
        center = new PointF(x, y);
    }

    public PointF getCenter() {
        return center;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void addMass(float num) {
        mass += num * DIGEST_RATE;
        radius = (float) Math.sqrt(mass);
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
        return "radius = " + radius + "; center = " + center;
    }

    public void draw(Canvas canvas) {
    } // TODO

}