package com.game.junglelaw.circle;

import android.graphics.PointF;

import com.game.junglelaw.Utility;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by apple on 10/15/15.
 */
public class MovableCircle extends AbstractCircle {

    private static final String LOG_TAG = MovableCircle.class.getSimpleName();
    protected static final float DEAFULT_SPEED = 10;

    private PointF mMovingDirection; // mMovingDirection vector size should be 1 (i.e. has a unit distance)
    protected float mSpeed;
    private String mGameDifficulty;
    private boolean isAttackPlayer;

    public MovableCircle(float x, float y, float radius, int color, String gameDifficulty) {
        super(x, y, radius, color);
        mMovingDirection = new PointF((float) Math.random(), (float) Math.random());
        mSpeed = DEAFULT_SPEED;
        isAttackPlayer = false;
        this.mGameDifficulty = gameDifficulty;
    }

    public void setmSpeed(float new_speed) {
        mSpeed = new_speed;
    }

    public float getmSpeed() {
        return mSpeed;
    }

    public void setmMovingDirection(PointF newDirection) {
        float len = newDirection.length(); // normalization
        mMovingDirection = new PointF(newDirection.x / len, newDirection.y / len);
    }

    public PointF getmMovingDirection() {
        return mMovingDirection;
    }

    /**
     * Moves forward to the mMovingDirection with current mSpeed
     */
    public void moveToDirection(int width, int height) {
        mSpeed = DEAFULT_SPEED / (mRadius / 40);
        float newX = mCenter.x + mSpeed * mMovingDirection.x;
        float newY = mCenter.y + mSpeed * mMovingDirection.y;

        if ((newX > 0 && newX < width) && (newY > 0 && newY < height)) {
            setCenter(newX, newY);
        }
    }

    public void setDirectTowardPoint(PointF targetPoint) {
        setmMovingDirection(new PointF(targetPoint.x - mCenter.x, targetPoint.y - mCenter.y));
    }

    public void aiMove(int width, int height, MovableCircle playerCircle, List<StaticCircle> staticCircleList) {

        if (mGameDifficulty.equals("easy") && Math.random() < 0.1) {
            PointF p = new PointF(Utility.generateRandomFloat(0, width), Utility.generateRandomFloat(0, height));
            setDirectTowardPoint(p);
            moveToDirection(width, height);
            return;
        }

        if (isAttackPlayer == true) {

            if (Utility.isAbsorbableLarger(this, playerCircle)) {
                setDirectTowardPoint(playerCircle.getmCenter());
                moveToDirection(width, height);
                return;

            } else {
                isAttackPlayer = false;
            }
        }

        // When goes to here: isAttackPlayer == false
        if (Math.random() < 0.1) {
            PointF staticCircleCenter = findNearest(staticCircleList);
            setDirectTowardPoint(staticCircleCenter);

        } else if (Utility.isAbsorbableLarger(this, playerCircle) && Math.random() < 0.5) {
            isAttackPlayer = true;
            setDirectTowardPoint(playerCircle.getmCenter());
        }
        // else, keep the predefined mMovingDirection

        moveToDirection(width, height);
    }

    private PointF findNearest(List<StaticCircle> staticCircleList) {
        final PointF currentCenter = mCenter;
        PriorityQueue<PointF> minPQ = new PriorityQueue<>(11, new Comparator<PointF>() {
            @Override
            public int compare(PointF lhs, PointF rhs) {
                return (int) (Utility.pointsDistance(currentCenter, lhs) - Utility.pointsDistance(currentCenter, rhs));
            }
        });

        for (StaticCircle sc : staticCircleList) {
            minPQ.add(sc.mCenter);
        }

        return minPQ.poll();
    }
}