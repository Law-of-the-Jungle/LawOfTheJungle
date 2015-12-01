package com.game.junglelaw.circle;

import android.graphics.PointF;

import com.game.junglelaw.Utility;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by apple on 11/30/15.
 */
public class AiCircle extends MovableCircle {

    private static final String LOG_TAG = AiCircle.class.getSimpleName();

    private String mGameDifficulty;
    private boolean mIsAttackingPlayer;

    public AiCircle(float x, float y, float radius, int color, String gameDifficulty) {
        super(x, y, radius, color);
        mSpeed = DEAFULT_SPEED;
        mIsAttackingPlayer = false;
        mGameDifficulty = gameDifficulty;
    }

    public void aiMove(int width, int height, MovableCircle playerCircle, List<StaticCircle> staticCircleList) {

        if (mGameDifficulty.equals("easy") && Math.random() < 0.1) {
            PointF p = new PointF(Utility.generateRandomFloat(0, width), Utility.generateRandomFloat(0, height));
            setDirectTowardPoint(p);
            moveToDirection(width, height);
            return;
        }

        if (mIsAttackingPlayer) {

            if (Utility.isAbsorbableLarger(this, playerCircle)) {
                setDirectTowardPoint(playerCircle.getmCenter());
                moveToDirection(width, height);
                return;

            } else {
                mIsAttackingPlayer = false;
            }
        }

        // When goes to here: mIsAttackingPlayer == false
        if (Math.random() < 0.1) {
            PointF staticCircleCenter = findNearestStaticCircle(staticCircleList);
            setDirectTowardPoint(staticCircleCenter);

        } else if (Utility.isAbsorbableLarger(this, playerCircle) && Math.random() < 0.5) {
            mIsAttackingPlayer = true;
            setDirectTowardPoint(playerCircle.getmCenter());
        }
        // else, keep the predefined mMovingDirection

        moveToDirection(width, height);
    }

    private PointF findNearestStaticCircle(List<StaticCircle> staticCircleList) {

        PriorityQueue<PointF> minPQ = new PriorityQueue<>(11, new Comparator<PointF>() {

            @Override
            public int compare(PointF lhs, PointF rhs) {
                return (int) (Utility.pointsDistance(mCenter, lhs) - Utility.pointsDistance(mCenter, rhs));
            }
        });

        for (StaticCircle sc : staticCircleList) {
            minPQ.add(sc.mCenter);
        }

        return minPQ.poll();
    }
}
