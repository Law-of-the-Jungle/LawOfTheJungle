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
public class PlayGround implements Runnable{
    private GameView view;
    private SurfaceHolder viewholder;
    //public float player_x,player_y;//wrap this into method and seal it,now is just draft
    public PlayGround(GameView view){
        this.view=view;
        viewholder=view.getHolder();
        Log.d("debug","create class");
    }
    @Override
    public void run() {
        Log.d("run","running");
        while(true){
            Canvas c=null;
            try{
                c=viewholder.lockCanvas();
                synchronized (viewholder){
                    Log.d("tag","before draw");
                    view.onDraw(c);
                }
            }finally {
                if (c != null)
                    viewholder.unlockCanvasAndPost(c);
                else
                    Log.d("empty canvas", "empty canvas");
            }
        }
        //Log.d("end","end run()");
    }
}
