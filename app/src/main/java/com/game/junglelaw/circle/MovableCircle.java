package com.game.junglelaw.circle;

import android.graphics.PointF;

import com.game.junglelaw.Utility;

import java.util.List;
import java.util.Random;

/**
 * Created by apple on 10/15/15.
 */
public class MovableCircle extends AbstractCircle {

    private PointF direction; // direction vector size should be 1 (i.e. has a unit distance)
    private float speed;
    private boolean isAttackPlayer;

    public MovableCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color);
        direction = new PointF(0, 0);
        speed = 10;
        isAttackPlayer = false;
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

    public void aiMove(int width, int height, MovableCircle playerCircle, List<StaticCircle> staticCircleList) {

        if (isAttackPlayer == true) {

            if (radius > 1.1 * playerCircle.getRadius()) {
                setNewDirection(new PointF(playerCircle.x, playerCircle.y), this);
                moveToDirection(width, height);
                return;

            } else {
                isAttackPlayer = false;
            }
        }

        // When goes to here: isAttackPlayer == false
        double randNumb = Math.random();

        if (randNumb < 0.05) {
            StaticCircle staticCircle = staticCircleList.get(Utility.generateRandomInt(0, staticCircleList.size() - 1));
            PointF staticCenter = new PointF(staticCircle.x, staticCircle.y);
            setNewDirection(staticCenter, this);

        } else if (randNumb < 0.55 && radius > 1.1 * playerCircle.getRadius()) {
            isAttackPlayer = true;
            PointF playerCenter = new PointF(playerCircle.x, playerCircle.y);
            setNewDirection(playerCenter, this);
        }
        // else, keep the predefined direction

        moveToDirection(width, height);
    }
}