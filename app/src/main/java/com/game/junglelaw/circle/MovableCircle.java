package com.game.junglelaw.circle;

import android.graphics.PointF;
import android.util.Log;

import com.game.junglelaw.Utility;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by apple on 10/15/15.
 */
public class MovableCircle extends AbstractCircle {

    private static final String LOG_TAG = MovableCircle.class.getSimpleName();

    private String difficulty;
    private PointF direction; // direction vector size should be 1 (i.e. has a unit distance)
    private float speed;
    private boolean isAttackPlayer;

    public MovableCircle(float x, float y, float radius, int color, String difficulty) {
        super(x, y, radius, color);
        direction = new PointF(0, 0);
        speed = 10;
        isAttackPlayer = false;
        this.difficulty = difficulty;
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
        speed = 10 / (radius / 40);
        float new_x = center.x + speed * direction.x;
        float new_y = center.y + speed * direction.y;
        if ((new_x > 0 && new_x < width) && (new_y > 0 && new_y < height)) {
            setCenter(new_x, new_y);
        }
    }

    public void setNewDirection(PointF userClickPoint, PointF center) {
        float newX = userClickPoint.x - center.x;
        float newY = userClickPoint.y - center.y;
        float len = (float) Math.sqrt(Math.pow(newX, 2) + Math.pow(newY, 2)); // for 归一化处理
        setDirection(newX / len, newY / len);
    }

    public void aiMove(int width, int height, MovableCircle playerCircle, List<StaticCircle> staticCircleList) {
        if (difficulty.equals("easy") && Math.random() < 0.1) {
            PointF p = new PointF(Utility.generateRandomFloat(0, width), Utility.generateRandomFloat(0, height));
            setNewDirection(p, center);
            moveToDirection(width, height);
            return;
        }

        if (isAttackPlayer == true) {

            if (radius > 1.1 * playerCircle.getRadius()) {
                setNewDirection(playerCircle.getCenter(), center);
                moveToDirection(width, height);
                return;

            } else {
                isAttackPlayer = false;
            }
        }

        // When goes to here: isAttackPlayer == false
        double randNumb = Math.random();

        if (randNumb < 0.1) {
            PointF staticCircleCenter = findNearest(staticCircleList);
            setNewDirection(staticCircleCenter, center);

        } else if (radius > 1.1 * playerCircle.getRadius() && randNumb < 0.6) {
            isAttackPlayer = true;
            setNewDirection(playerCircle.getCenter(), center);
        }
        // else, keep the predefined direction

        moveToDirection(width, height);
    }

    private PointF findNearest(List<StaticCircle> staticCircleList) {
        final PointF currentCenter = center;
        PriorityQueue<PointF> minPQ = new PriorityQueue<>(11, new Comparator<PointF>() {
            @Override
            public int compare(PointF lhs, PointF rhs) {
                return (int) (distance(currentCenter, lhs) - distance(currentCenter, rhs));
            }
        });

        for (StaticCircle sc : staticCircleList) {
            minPQ.add(sc.center);
        }

        return minPQ.poll();
    }

    private float distance(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}