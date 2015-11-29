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

    private String TAG = "CircleManger";

    private CircleFactory circleFactory;
    private List<StaticCircle> staticCircleList;
    private List<MovableCircle> movableCircleList;
    private int width_max, height_max;


    public CircleManager() {
        staticCircleList = new ArrayList<>();
        movableCircleList = new ArrayList<>();
        circleFactory = new CircleFactory();
    }

    public void setSize(int width_max, int height_max) {
        this.width_max = width_max;
        this.height_max = height_max;
    }

    public List<StaticCircle> ProvideStatic() {
        return staticCircleList;
    }

    public List<MovableCircle> ProvideMovable() {
        return movableCircleList;
    }

    public boolean InMovableList(MovableCircle circle) {
        if (movableCircleList.contains(circle))
            return true;
        else
            return false;

    }

    // TODO (如有必要)生成新的static circle
    public void controlPopulation() {
        if (movableCircleList.size() < 30) {
            movableCircleList.addAll(circleFactory.BatchWorkForPCircle(width_max, height_max,
                    30 - staticCircleList.size()));
        }
        if (staticCircleList.size() < 500) {
            staticCircleList.addAll(circleFactory.BatchWorkForScircle(width_max, height_max,
                    500 - staticCircleList.size()));
        }
    }

    public void MoveMovable() {
        for (int i = 1; i < movableCircleList.size(); i++) {
            MovableCircle mc = movableCircleList.get(i);
            mc.aiMove(width_max, height_max, movableCircleList.get(0), staticCircleList);
        }
    }

    // TODO 判断，circle的相互吃情况
    public void EliminateConfliction() {
        //do for player first
        boolean collided = true;
        while (collided) {
            collided = false;
            synchronized (movableCircleList) {
                for (int i = movableCircleList.size() - 2; i >= 0; i--) {
                    for (int j = i + 1; j < movableCircleList.size(); j++) {
                        MovableCircle m1 = movableCircleList.get(i);
                        MovableCircle m2 = movableCircleList.get(j);

                        if (Utility.canAbsorb(m1, m2)) {
                            m1.addMass(m2.getMass());
                            movableCircleList.remove(m2);
                            collided = true;
                        } else if (Utility.canAbsorb(m2, m1)) {
                            m2.addMass(m1.getMass());
                            movableCircleList.remove(m1);
                            collided = true;
                            break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < movableCircleList.size(); i++) {
            for (int j = 0; j < staticCircleList.size(); j++) {
                MovableCircle ms = movableCircleList.get(i);
                StaticCircle sc = staticCircleList.get(j);
                if (Utility.circleCenterDistance(ms, sc) < ms.getRadius()) {
                    synchronized (staticCircleList) {
                        ms.addMass(sc.getMass());
                        staticCircleList.remove(sc);
                    }
                }
            }
        }
    }
}
