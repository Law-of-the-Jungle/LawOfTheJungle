package com.game.junglelaw.Circle;

import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 */
public class MovableCircle extends AbstractCircle {

    private PointF direction; // direction vector size should be 1 (i.e. has a unit distance)
    private float speed;

    protected MovableCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color);
        direction=new PointF(0,0);
        speed=10;
    }
    public void setSpeed(float new_speed){
        speed=new_speed;
    }
    public float getSpeed(){
        return speed;
    }
    public void setDirection(float newX, float newY) {
        direction.set(newX, newY);
    }

    public PointF getDirection() {
        return direction;
    }

    /** Moves forward to the direction by one unit distance */
    public void moveToDirection() {
        x = x+speed*direction.x;
        y = y+speed*direction.y;
    }
    public void setNewDirection(PointF userClickPoint,PointF center) {
        float newX = userClickPoint.x - center.x;
        float newY = userClickPoint.y - center.y;
        float len = (float) Math.sqrt(Math.pow(newX, 2) + Math.pow(newY, 2)); // for 归一化处理
        setDirection(newX / len, newY / len);
    }
    public void randomMove(){

    }

    /**
     * Absorbs the targetCircle into current player circle.
     *
     * Pre-assumption: current player circle's radius is larger than targetCircle's.
     */
    public void absorb(AbstractCircle targetCircle) {
        // TODO stay tuned
    }
}