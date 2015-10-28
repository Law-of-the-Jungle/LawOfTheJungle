package com.game.junglelaw;

import com.game.junglelaw.Circle.*;

import java.util.List;
import java.util.Random;

/**
 * Created by apple on 10/15/15.
 * Control circle recourse and provide circle data
 */
public class CircleManager {


    private Random rand;
    private CircleFactory circleFactory;
    private List<StaticCircle> staticCircleList;
    private List<MovableCircle> movableCircleList;
    private int width_max,height_max;


    public CircleManager(int width_max,int height_max) {
        rand = new Random();
        this.width_max=width_max;
        this.height_max=height_max;
        circleFactory = new CircleFactory();
    }

    public List<StaticCircle> ProvideStatic(){
        return staticCircleList;
    }
    public List<MovableCircle> ProvideMovable(){
        return movableCircleList;
    }


    // TODO 监控是否需要添加新的static circle
    public boolean PopulationAlert(){
        int SCircle_size=staticCircleList.size();
        int MCircle_size=movableCircleList.size();
        if(SCircle_size<MCircle_size || SCircle_size<100){

        }
        return true;

    }
    // TODO (如有必要)生成新的static circle
    public void ControlPopulation(){

    }
    // TODO 判断，circle的相互吃情况
    public void EliminateConfliction(){
    }
}
