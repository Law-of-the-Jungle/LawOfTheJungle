package com.game.junglelaw.Circle;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 *
 * Declares the common properties of all circles.
 */
public abstract class AbstractCircle extends PointF {
    private final float DIGEST_RATE=5;
    private int color;
    private float radius;
    private float mass;
    protected AbstractCircle(float x, float y, float radius, int color) {
        super(x, y);
        this.radius = radius;
        this.color = color;
        mass=radius*radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return (float)Math.sqrt(mass);
    }
    public void addMass(float num){
        mass+=num*DIGEST_RATE;
    }
    public float getMass(){
        return mass;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String toString(){
        return Float.toString(x)+" "+Float.toString(y);
    }
    public void draw(Canvas canvas) {
    } // TODO

}