package com.game.junglelaw;

import com.game.junglelaw.circle.MovableCircle;
import com.game.junglelaw.circle.StaticCircle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 10/15/15.
 * Control circle recourse and provide circle data
 */
public class CircleManager {

    private static final String LOG_TAG = CircleManager.class.getSimpleName();

    private static final int MIN_STATIC_CIRCLE_NUMBER = 30;
    private static final int MIN_AI_CIRCLE_NUMBER = 500;

    private CircleFactory circleFactory;

    private List<StaticCircle> mStaticCircles;
    private List<MovableCircle> mMovableCircles;
    private int width_max, height_max;

    public CircleManager(String difficulty) {
        mStaticCircles = new ArrayList<>();
        mMovableCircles = new ArrayList<>();
        circleFactory = new CircleFactory(difficulty);
    }

    public void setSize(int width_max, int height_max) {
        this.width_max = width_max;
        this.height_max = height_max;
    }

    public List<StaticCircle> getmStaticCircles() {
        return mStaticCircles;
    }

    public List<MovableCircle> getmMovableCircles() {
        return mMovableCircles;
    }

    public boolean inMovableList(MovableCircle circle) {
        if (mMovableCircles.contains(circle)) {
            return true;
        } else {
            return false;
        }
    }

    // TODO (如有必要)生成新的static circle
    public void controlPopulation() {
        if (mMovableCircles.size() < MIN_STATIC_CIRCLE_NUMBER) {
            mMovableCircles.addAll(circleFactory.createAiCircles(width_max, height_max,
                    MIN_STATIC_CIRCLE_NUMBER - mStaticCircles.size(), mMovableCircles.get(0)));
        }

        if (mStaticCircles.size() < MIN_AI_CIRCLE_NUMBER) {
            mStaticCircles.addAll(circleFactory.createStaticCircles(width_max, height_max,
                    MIN_AI_CIRCLE_NUMBER - mStaticCircles.size()));
        }
    }

    public void moveMovableCircles() {
        for (int i = 1; i < mMovableCircles.size(); i++) {
            MovableCircle mc = mMovableCircles.get(i);
            mc.aiMove(width_max, height_max, mMovableCircles.get(0), mStaticCircles);
        }
    }

    // TODO 判断，circle的相互吃情况
    public void absorb() {
        //do for player first
        boolean collided = true;
        while (collided) {
            collided = false;
            synchronized (mMovableCircles) {
                for (int i = mMovableCircles.size() - 2; i >= 0; i--) {
                    for (int j = i + 1; j < mMovableCircles.size(); j++) {
                        MovableCircle m1 = mMovableCircles.get(i);
                        MovableCircle m2 = mMovableCircles.get(j);

                        if (Utility.canAbsorb(m1, m2)) {
                            m1.addMass(m2.getMass());
                            mMovableCircles.remove(m2);
                            collided = true;
                        } else if (Utility.canAbsorb(m2, m1)) {
                            m2.addMass(m1.getMass());
                            mMovableCircles.remove(m1);
                            collided = true;
                            break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < mMovableCircles.size(); i++) {
            for (int j = mStaticCircles.size() - 1; j >= 0; j--) {
                MovableCircle ms = mMovableCircles.get(i);
                StaticCircle sc = mStaticCircles.get(j);
                if (Utility.canAbsorb(ms, sc)) {
                    synchronized (mStaticCircles) {
                        ms.addMass(sc.getMass());
                        mStaticCircles.remove(j);
                    }
                }
            }
        }
    }
}
