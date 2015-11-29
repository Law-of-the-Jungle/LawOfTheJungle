package com.game.junglelaw;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * PlayGround is the main class for controlling how the game is going.
 * It should implement a thread interface
 */
public class PlayGround extends Thread {
    private GameView view;
    private SurfaceHolder viewholder;
    private boolean RunState;
    private boolean PauseState;
    private String TAG = "PlayGround";
    static final long FPS = 60;
    private CircleManager manger;
    private boolean GameOverState;

    public CircleManager getManger() {
        return manger;
    }

    public void setPauseState(boolean new_state) {
        PauseState = new_state;
    }

    //public float player_x,player_y;//wrap this into method and seal it,now is just draft
    public PlayGround(GameView view) {
        this.view = view;
        viewholder = view.getHolder();
        RunState = false;
        GameOverState = false;
        PauseState = false;
        manger = new CircleManager();
        Log.d(TAG, "PlayGround object created");
    }

    public void setGameOverState(boolean state) {
        GameOverState = state;
    }

    public boolean IsGameOver() {
        return GameOverState;
    }

    public void setRunState(boolean state) {
        RunState = state;
    }

    public void render() {
        Canvas c = null;
        try {
            c = viewholder.lockCanvas();
            synchronized (viewholder) {
                if (c != null) {
                    manger.controlPopulation(); // manage the points in the map
                    manger.EliminateConfliction();
                    view.player.updateZoom(view);
                    if (!manger.InMovableList(view.player)) {
                        setRunState(false);
                        setGameOverState(true);
                        Log.d("end", "end game");
                    }
                    manger.MoveMovable();
                    view.onDraw(c);
                }
            }
        } finally {
            if (c != null)
                viewholder.unlockCanvasAndPost(c);
            else
                Log.d(TAG, "Empty canvas");
        }
    }

    @Override
    public void run() {
        Log.d(TAG, "Start main loop...");
        long startTime;
        long sleepTime;
        long ticksPS = 1000 / FPS;
        while (RunState) {
            if (PauseState)
                continue;
            startTime = System.currentTimeMillis();
            //Log.d(TAG,Float.toString(view.player.x)+" "+Float.toString(view.player.y));
            render();
            sleepTime = (System.currentTimeMillis() - startTime);
            //Log.d(TAG,"SleepTime:"+Long.toString(sleepTime));
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(5);
            } catch (Exception e) {
            }
        }
        Log.d(TAG, "Ending main loop...");
    }
}
