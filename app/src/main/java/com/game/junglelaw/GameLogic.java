package com.game.junglelaw;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * GameLogic controls the game logic.
 */
public class GameLogic implements Runnable {

    private static final String LOG_TAG = GameLogic.class.getSimpleName();

    private GameView mGameView;
    private SurfaceHolder mSurfaceHolder;
    private boolean mIsRun;
    private boolean mIsPause;
    private boolean mIsGameOver;

    public void setmIsPause(boolean isPause) {
        mIsPause = isPause;
    }

    public GameLogic(GameView gameView) {
        mGameView = gameView;
        mSurfaceHolder = gameView.getHolder();
        mIsRun = false;
        mIsGameOver = false;
        mIsPause = false;
    }

    public void setmIsGameOver(boolean isGameOver) {
        mIsGameOver = isGameOver;
    }

    public boolean isGameOver() {
        return mIsGameOver;
    }

    public void setmIsRun(boolean isRun) {
        mIsRun = isRun;
    }

    @Override
    public void run() {
        Log.d(LOG_TAG, "Start game loop...");

        while (mIsRun) {
            if (mIsPause) {
                continue;
            }
            long startTime = System.currentTimeMillis();
            executeGameLogic();
            long renderTime = System.currentTimeMillis() - startTime;
            try {
                Thread.sleep(Math.max(renderTime, 5));
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }

        Log.d(LOG_TAG, "End game loop...");
    }

    private void executeGameLogic() {
        Canvas canvas = mSurfaceHolder.lockCanvas();

        if (canvas != null) {
            synchronized (mSurfaceHolder) {
                mGameView.getmPlayerCircle().updateZoomRate(mGameView);
                mGameView.getmCircleManager().absorb();
                mGameView.getmCircleManager().controlPopulation();

                if (!mGameView.getmCircleManager().inMovableList(mGameView.getmPlayerCircle())) {
                    setmIsRun(false);
                    setmIsGameOver(true);
                }

                mGameView.getmCircleManager().movePlayerCircle();
                mGameView.getmCircleManager().moveMovableCircles();
                mGameView.onDraw(canvas);

                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
