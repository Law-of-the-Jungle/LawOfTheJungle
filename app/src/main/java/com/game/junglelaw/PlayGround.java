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
    private boolean runState;
    private boolean pauseState;
    private String TAG = "PlayGround";
    static final long FPS = 60;
    private CircleManager manger;
    private boolean gameOverState;

    public CircleManager getManger() {
        return manger;
    }

    public void setPauseState(boolean new_state) {
        pauseState = new_state;
    }

    //public float player_x,player_y;//wrap this into method and seal it,now is just draft
    public PlayGround(GameView view, String difficulty) {
        this.view = view;
        viewholder = view.getHolder();
        runState = false;
        gameOverState = false;
        pauseState = false;
        manger = new CircleManager(difficulty);
    }

    public void setGameOverState(boolean state) {
        gameOverState = state;
    }

    public boolean isGameOver() {
        return gameOverState;
    }

    public void setRunState(boolean state) {
        runState = state;
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

                        Log.d(TAG, "end game");
                    }
                    manger.MoveMovable();
                    view.onDraw(c);
                }
            }
        } finally {
            if (c != null) {
                viewholder.unlockCanvasAndPost(c);
            } else {
                Log.d(TAG, "Empty canvas");
            }
        }
    }

    @Override
    public void run() {
        Log.d(TAG, "Start main loop...");
        long startTime;
        long sleepTime;
        long ticksPS = 1000 / FPS;
        while (runState) {
            if (pauseState)
                continue;
            startTime = System.currentTimeMillis();
            //Log.d(TAG,Float.toString(view.player.x)+" "+Float.toString(view.player.y));
            render();
            sleepTime = (System.currentTimeMillis() - startTime);
            //Log.d(TAG,"SleepTime:"+Long.toString(sleepTime));
            try {
                if (sleepTime > 0) {
                    sleep(sleepTime);
                } else {
                    sleep(5);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        Log.d(TAG, "Ending main loop...");
    }
}
