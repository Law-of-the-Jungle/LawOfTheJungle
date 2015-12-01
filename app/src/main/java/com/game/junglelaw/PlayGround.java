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
    private CircleManager mCircleManager;
    private boolean mIsRun;
    private boolean mIsPause;
    private boolean mIsGameOver;

    public CircleManager getmCircleManager() {
        return mCircleManager;
    }

    public void setmIsPause(boolean new_state) {
        mIsPause = new_state;
    }

    public PlayGround(GameView gameView, String gameDifficulty) {
        mGameView = gameView;
        mSurfaceHolder = gameView.getHolder();
        mIsRun = false;
        mIsGameOver = false;
        mIsPause = false;
        mCircleManager = new CircleManager(gameDifficulty);
    }

    public void setmIsGameOver(boolean isGameOver) {
        mIsGameOver = isGameOver;
    }

    public boolean isGameOver() {
        return mIsGameOver;
    }

    public void setmIsRun(boolean state) {
        mIsRun = state;
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
                        setmIsRun(false);
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
        while (mIsRun) {
            if (mIsPause) {
                continue;
            }
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
