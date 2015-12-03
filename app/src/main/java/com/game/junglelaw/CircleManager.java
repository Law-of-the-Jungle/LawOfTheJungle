package com.game.junglelaw;

import com.game.junglelaw.circle.AiCircle;
import com.game.junglelaw.circle.PlayerCircle;
import com.game.junglelaw.circle.StaticCircle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 10/15/15.
 * Control circle recourse and provide circle data
 */
public class CircleManager {

    private static final String LOG_TAG = CircleManager.class.getSimpleName();

    private static final int MIN_STATIC_CIRCLES_NUMBER = 30;
    private static final int MIN_AI_CIRCLES_NUMBER = 500;

    private final CircleFactory mCircleFactory;
    private final String mGameDifficulty;
    private final PlayerCircle mPlayerCircle;
    private final List<AiCircle> mAiCircles;
    private final List<StaticCircle> mStaticCircles;

    public CircleManager(String gameDifficulty, float mapWidth, float mapHeight) {
        mCircleFactory = new CircleFactory(gameDifficulty, mapWidth, mapHeight);
        mGameDifficulty = gameDifficulty;
        mPlayerCircle = mCircleFactory.createRandomPlayerCircle();
        mAiCircles = new ArrayList<>();
        mStaticCircles = new ArrayList<>();
    }

    public PlayerCircle getmPlayerCircle() {
        return mPlayerCircle;
    }

    public List<StaticCircle> getmStaticCircles() {
        return mStaticCircles;
    }

    public List<AiCircle> getmAiCircles() {
        return mAiCircles;
    }

    public void controlCirclesPopulation() {
        // Controls ai circles population
        if (mAiCircles.size() < MIN_STATIC_CIRCLES_NUMBER) {
            mAiCircles.addAll(mCircleFactory.createRandomAiCircles(
                    MIN_STATIC_CIRCLES_NUMBER - mStaticCircles.size(), mPlayerCircle));
        }

        // Controls static circles population
        if (mStaticCircles.size() < MIN_AI_CIRCLES_NUMBER) {
            mStaticCircles.addAll(mCircleFactory.createRandomStaticCircles(
                    MIN_AI_CIRCLES_NUMBER - mStaticCircles.size()));
        }
    }

    public void moveMovableCirclesToTheirDirection() {
        // Moves player circle toward its direction
        mPlayerCircle.moveToDirection();

        // Move ai circles toward its direction
        for (AiCircle aiCircle : mAiCircles) {
            aiCircle.aiMove(mPlayerCircle, mStaticCircles);
        }
    }

    public void movableCirclesAbsorbCircles() {
        playerCircleAbsorbCircles();
        aiCirclesAbsorbCircles();
    }

    /** Player circle absorbs other circles */
    private void playerCircleAbsorbCircles() {
        // Player circle absorbs ai circle(s)
        for (int i = mAiCircles.size() - 1; i >= 0; i--) {
            AiCircle aiCircle = mAiCircles.get(i);
            if (Utility.canAbsorb(mPlayerCircle, aiCircle)) {
                mPlayerCircle.absorbCircle(aiCircle);
                mAiCircles.remove(i);
            }
        }

        // Player circle absorbs static circle(s)
        for (int i = mStaticCircles.size() - 1; i >= 0; i--) {
            StaticCircle staticCircle = mStaticCircles.get(i);
            if (Utility.canAbsorb(mPlayerCircle, staticCircle)) {
                mPlayerCircle.absorbCircle(staticCircle);
                mStaticCircles.remove(i);
            }
        }
    }

    /** Ai circles absorb other circles */
    private void aiCirclesAbsorbCircles() {
        // Ai circles absorb each other
        boolean canAbsorb = true;
        while (canAbsorb) {
            canAbsorb = false;

            for (int i = mAiCircles.size() - 2; i >= 0; i--) {
                AiCircle aiCircle1 = mAiCircles.get(i);
                if (Utility.canAbsorb(aiCircle1, mPlayerCircle)) {
                    aiCircle1.absorbCircle(mPlayerCircle);
                    mPlayerCircle.setmIsAbsorbed(true);
                    return;
                }

                for (int j = mAiCircles.size() - 1; j > i; j--) {
                    AiCircle aiCircle2 = mAiCircles.get(j);

                    if (Utility.canAbsorb(aiCircle1, aiCircle2)) {
                        aiCircle1.absorbCircle(aiCircle2);
                        mAiCircles.remove(j);
                        canAbsorb = true;
                    } else if (Utility.canAbsorb(aiCircle2, aiCircle1)) {
                        aiCircle2.absorbCircle(aiCircle1);
                        mAiCircles.remove(j);
                        canAbsorb = true;
                        break;
                    }
                }
            }
        }

        // Ai circles absorb static circle(s)
        for (AiCircle aiCircle : mAiCircles) {
            for (int j = mStaticCircles.size() - 1; j >= 0; j--) {
                StaticCircle staticCircle = mStaticCircles.get(j);
                if (Utility.canAbsorb(aiCircle, staticCircle)) {
                    aiCircle.absorbCircle(staticCircle);
                    mStaticCircles.remove(j);
                }
            }
        }
    }
}
