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
    private static final float ATTACK_PLAYER_RANGE_RATE = 10;

    public static final float AI_CIRCLE_DEFAULT_RADIUS_RATE = (float) 1.2;

    private String mGameDifficulty;
    private boolean mIsAttackingPlayer;

    public AiCircle(float x, float y, float radius, int color, String gameDifficulty) {
        super(x, y, radius, color);
        mIsAttackingPlayer = false;
        mGameDifficulty = gameDifficulty;
    }

    public void aiMove(int mapWidth, int mapHeight, PlayerCircle playerCircle, List<StaticCircle> staticCircles) {

        if (mGameDifficulty.equals("easy") && Math.random() < 0.1) {
            PointF p = new PointF(Utility.generateRandomFloat(0, mapWidth), Utility.generateRandomFloat(0, mapHeight));
            setDirectTowardPoint(p);
            moveToDirection(mapWidth, mapHeight);
            return;
        }

        if (mIsAttackingPlayer) {
            if (Utility.isAbsorbableLarger(this, playerCircle)) {
                setDirectTowardPoint(playerCircle.getmCenter());
                moveToDirection(mapWidth, mapHeight);
                return;

            } else {
                mIsAttackingPlayer = false;
            }
        }

        // When goes to here: mIsAttackingPlayer == false
        if (Math.random() < 0.1) {
            PointF staticCircleCenter = findNearestStaticCircle(staticCircles);
            setDirectTowardPoint(staticCircleCenter);

        } else if (Utility.isAbsorbableLarger(this, playerCircle) &&
                Utility.pointsDistance(mCenter, playerCircle.getmCenter()) < ATTACK_PLAYER_RANGE_RATE * mRadius && Math.random() < 0.5) {
            mIsAttackingPlayer = true;
            setDirectTowardPoint(playerCircle.getmCenter());
        }
        // else, keep the predefined mMovingDirection

        moveToDirection(mapWidth, mapHeight);
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
