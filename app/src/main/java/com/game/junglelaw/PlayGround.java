package com.game.junglelaw;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.game.junglelaw.circle.MovableCircle;
import com.game.junglelaw.circle.StaticCircle;

import java.util.List;

/**
 * PlayGround is the main class for controlling how the game is going.
 * It should implement a thread interface
 */
public class PlayGround extends Thread{
    private GameView view;
    private SurfaceHolder viewholder;
    private boolean RunState;
    private String TAG="PlayGround";
    static final long FPS=60;
    private CircleManager manger;
    public CircleManager getManger(){
        return manger;
    }


    //public float player_x,player_y;//wrap this into method and seal it,now is just draft
    public PlayGround(GameView view){
        this.view=view;
        viewholder=view.getHolder();
        RunState=false;
        manger =new CircleManager();
        Log.d(TAG,"PlayGround object created");
    }

    public void setRunState(boolean state){
        RunState=state;
    }
    public void render(){
        Canvas c=null;
        try{
            c=viewholder.lockCanvas();
            synchronized (viewholder){
                if(c!=null){
                    manger.ControlPopulation(); // manage the points in the map
                    manger.EliminateConfliction();
                    view.player.updateZoom(view);
                    //let movable move in manger
                    view.onDraw(c);
                }
            }
        }finally {
            if (c != null)
                viewholder.unlockCanvasAndPost(c);
            else
                Log.d(TAG, "Empty canvas");
        }
    }

    @Override
    public void run() {
        Log.d(TAG,"Start main loop...");
        long startTime;
        long sleepTime;
        long ticksPS=1000/FPS;
        while(RunState){
            startTime = System.currentTimeMillis();
            //Log.d(TAG,Float.toString(view.player.x)+" "+Float.toString(view.player.y));
            render();
            sleepTime=(System.currentTimeMillis()-startTime);
            //Log.d(TAG,"SleepTime:"+Long.toString(sleepTime));
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(5);
            } catch (Exception e) {}
        }
        Log.d(TAG,"Ending main loop...");
    }
}
