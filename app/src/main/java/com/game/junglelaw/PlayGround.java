package com.game.junglelaw;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * PlayGround is the main class for controlling how the game is going.
 * It should implement a thread interface
 */
public class PlayGround extends Thread {

    private static final String LOG_TAG = PlayGround.class.getSimpleName();

    private GameView mGameView;
    private SurfaceHolder mSurfaceHolder;
    private boolean runState;
    private boolean pauseState;
    private CircleManager mCircleManager;
    private boolean mIsGameOver;

    public CircleManager getmCircleManager() {
        return mCircleManager;
    }

    public void setPauseState(boolean new_state) {
        pauseState = new_state;
    }

    public PlayGround(GameView gameView, String difficulty) {
        mGameView = gameView;
        mSurfaceHolder = gameView.getHolder();
        runState = false;
        mIsGameOver = false;
        pauseState = false;
        mCircleManager = new CircleManager(difficulty);
    }

    public void setmIsGameOver(boolean state) {
        mIsGameOver = state;
    }

    public boolean isGameOver() {
        return mIsGameOver;
    }

    public void setRunState(boolean state) {
        runState = state;
    }

    public void render() {
        Canvas canvas = null;
        try {
            canvas = mSurfaceHolder.lockCanvas();
            synchronized (mSurfaceHolder) {
                if (canvas != null) {
                    mCircleManager.controlPopulation(); // manage the points in the map
                    mCircleManager.absorb();
                    mGameView.mPlayerCircle.updateZoom(mGameView);
                    if (!mCircleManager.inMovableList(mGameView.mPlayerCircle)) {
                        setRunState(false);
                        setmIsGameOver(true);

                        Log.d(LOG_TAG, "end game");
                    }
                    mCircleManager.moveMovableCircles();
                    mGameView.onDraw(canvas);
                }
            }

        } finally {
            if (canvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            } else {
                Log.d(LOG_TAG, "Empty canvas");
            }
        }
    }

    @Override
    public void run() {
        Log.d(LOG_TAG, "Start main loop...");
        long startTime;
        long sleepTime;
        while (runState) {
            if (pauseState)
                continue;
            startTime = System.currentTimeMillis();
            //Log.d(LOG_TAG,Float.toString(mGameView.mPlayerCircle.x)+" "+Float.toString(mGameView.mPlayerCircle.y));
            render();
            sleepTime = (System.currentTimeMillis() - startTime);
            //Log.d(LOG_TAG,"SleepTime:"+Long.toString(sleepTime));
            try {
                if (sleepTime > 0) {
                    sleep(sleepTime);
                } else {
                    sleep(5);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }
        Log.d(LOG_TAG, "Ending main loop...");
    }
}
