package com.game.junglelaw;

import android.util.Log;

import com.game.junglelaw.Circle.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by apple on 10/15/15.
 * Control circle recourse and provide circle data
 */
public class CircleManager {

    private String TAG="CircleManger";
    private Random rand;
    private CircleFactory circleFactory;
    private List<StaticCircle> staticCircleList;
    private List<MovableCircle> movableCircleList;
    private int width_max,height_max;


    public CircleManager() {
        rand = new Random();
        staticCircleList=new ArrayList<StaticCircle>();
        movableCircleList= new ArrayList<MovableCircle>();
        circleFactory = new CircleFactory();
    }

    public void setSize(int width_max,int height_max){
        this.width_max=width_max;
        this.height_max=height_max;
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
            return true;
        }
        return false;
    }
    // TODO (如有必要)生成新的static circle
    public void ControlPopulation(){
        if(PopulationAlert()){
            Log.d(TAG, "Expending population...");
            staticCircleList.addAll(circleFactory.BatchWorkForScircle(width_max,height_max,500));
            //Log.d(TAG,Float.toString(staticCircleList.get(0).x)+" "+Float.toString(staticCircleList.get(0).y));
        }
    }
    // TODO 判断，circle的相互吃情况
    public void EliminateConfliction(){
    }
}
