package com.game.junglelaw.circle;

import android.graphics.PointF;

import java.util.Random;

/**
 * Created by apple on 10/15/15.
 */
public class MovableCircle extends AbstractCircle {

    private PointF direction; // direction vector size should be 1 (i.e. has a unit distance)
    private float speed;

    public MovableCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color);
        direction = new PointF(0, 0);
        speed = 10;
    }

    public void setSpeed(float new_speed) {
        speed = new_speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setDirection(float newX, float newY) {
        direction.set(newX, newY);
    }

    public PointF getDirection() {
        return direction;
    }

    /**
     * Moves forward to the direction by one unit distance
     */
    public void moveToDirection(int width, int height) {
        float new_x = x + speed * direction.x;
        float new_y = y + speed * direction.y;
        if (new_x > 0 && new_x < width)
            x = new_x;
        if (new_y > 0 && new_y < height)
            y = new_y;
    }

    public void setNewDirection(PointF userClickPoint, PointF center) {
        float newX = userClickPoint.x - center.x;
        float newY = userClickPoint.y - center.y;
        float len = (float) Math.sqrt(Math.pow(newX, 2) + Math.pow(newY, 2)); // for 归一化处理
        setDirection(newX / len, newY / len);
    }

    public void randomMove(int width, int height) {
        double randNumb = Math.random();

        if (randNumb < 0.1) {
            PointF fakeClick = new PointF((float) (Math.random() * width), (float) (Math.random() * height));
            setNewDirection(fakeClick, this);

        } else if (randNumb < 0.5) {

        }

        moveToDirection(width, height);
    }
}