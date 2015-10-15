package com.game.junglelaw;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

/**
 * PlayGround is the main class for controlling how the game is going.
 * It should implement a thread interface
 */
public class PlayGround extends Thread{
    private GameView view;
    private SurfaceHolder viewholder;
    private boolean RunState;
    private String TAG="PlayGround";
    //public float player_x,player_y;//wrap this into method and seal it,now is just draft
    public PlayGround(GameView view){
        this.view=view;
        viewholder=view.getHolder();
        RunState=false;
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
                if(c!=null)
                    view.onDraw(c);
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
        while(RunState){
           render();
        }
        Log.d(TAG,"Ending main loop...");
    }
}
