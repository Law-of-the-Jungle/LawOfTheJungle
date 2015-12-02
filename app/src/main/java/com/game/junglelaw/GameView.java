package com.game.junglelaw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.game.junglelaw.circle.AbstractCircle;
import com.game.junglelaw.circle.MovableCircle;
import com.game.junglelaw.circle.PlayerCircle;
import com.game.junglelaw.circle.StaticCircle;

import java.util.List;

/**
 * This class is used for rendering the view of game drawing the points in the GameLogic class
 * GameLogic instance is included in this view class, then we need to pass this view class to
 * GameLogic class in its constructor for drawing.
 *
 * GameView size range:
 *      0 <= x <= mMapWidth;
 *      0 <= y <= mMapHeight.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private String LOG_TAG = GameView.class.getSimpleName();

    private static final int MAP_HEIGHT_ZOOM_UP_RATE = 5;
    private static final int MAP_WEIGHT_ZOOM_UP_RATE = 5;

    private final CircleManager mCircleManager;
    private final GameLogic mGameLogic;
    private final Thread mGameThread;
    private final SurfaceHolder mSurfaceHolder;

    //we need to get the relative location of points to the playerCircle
    //then check if these circle could appear on map or not
    //Draw the circle with location of screen
    private final PlayerCircle mPlayerCircle;
    private List<StaticCircle> mStaticCircles;
    private List<MovableCircle> mMovableCircles;
    private int mMapWidth;
    private int mMapHeight;
    private int mScreenWidth;
    private int mScreenHeight;
    private PointF mScreenCenter;
    private String mGameDifficulty;

    public GameView(Context context, String gameDifficulty) {
        super(context);

        mGameDifficulty = gameDifficulty;
        mGameLogic = new GameLogic(this);
        mGameThread = new Thread(mGameLogic);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mCircleManager = new CircleManager(gameDifficulty);
        mPlayerCircle = mCircleManager.getmPlayerCircle();
    }

    public PlayerCircle getmPlayerCircle() {
        return mPlayerCircle;
    }

    public CircleManager getmCircleManager() {
        return mCircleManager;
    }

    public float getScore() {
        return mPlayerCircle.getMass();
    }

    public void pauseGame() {
        mGameLogic.setmIsPause(true);
    }

    public void resumeGame() {
        mGameLogic.setmIsPause(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mGameLogic.isGameOver()) {
            drawGameOverScene(canvas);
            return;
        }

        mStaticCircles = mCircleManager.getmStaticCircles();
        mMovableCircles = mCircleManager.getmMovableCircles();

        canvas.drawColor(Color.WHITE);

        drawText(canvas);
        drawCoordinateGrids(canvas);
        drawBorder(canvas);

        Paint circlePaint = new Paint();
        drawPlayerCircle(canvas, circlePaint);
        drawStaticCircles(canvas, circlePaint);
        drawAiCircles(canvas, circlePaint);
    }

    private void drawText(Canvas canvas) {
        Paint paint = new Paint();
        String displayText = "Score:" + getScore() + "; Game Difficulty: " + mGameDifficulty;
        String coordinate = "Coordinate: (" + mPlayerCircle.getmCenter().x + ", " + mPlayerCircle.getmCenter().y + ")";
        paint.setTextSize(40);
        canvas.drawText(displayText, 5, canvas.getHeight() - 50, paint);
        canvas.drawText(coordinate, 5, canvas.getHeight() - 5, paint);
    }

    private void drawCoordinateGrids(Canvas canvas) {
        Paint coordinatePaint = new Paint();
        coordinatePaint.setColor(Color.GRAY);
        coordinatePaint.setStrokeWidth(5);

        for (int x = 0; x < mMapWidth; x += 100) {
            PointF startPoint = relativePointDistance(new PointF(x, 0));
            PointF stopPoint = relativePointDistance(new PointF(x, mMapHeight));
            canvas.drawLine(startPoint.x, startPoint.y, stopPoint.x, stopPoint.y, coordinatePaint);
        }

        for (int y = 0; y < mMapHeight; y += 100) {
            PointF startPoint = relativePointDistance(new PointF(0, y));
            PointF stopPoint = relativePointDistance(new PointF(mMapWidth, y));
            canvas.drawLine(startPoint.x, startPoint.y, stopPoint.x, stopPoint.y, coordinatePaint);
        }
    }

    private void drawBorder(Canvas canvas) {
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5);
        borderPaint.setColor(Color.BLACK);
        PointF topLeft = relativePointDistance(new PointF(0, 0));
        PointF bottomRight = relativePointDistance(new PointF(mMapWidth, mMapHeight));
        canvas.drawRect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y, borderPaint);
    }

    private void drawPlayerCircle(Canvas canvas, Paint circlePaint) {
        circlePaint.setColor(Color.BLACK);
        canvas.drawCircle(mScreenCenter.x, mScreenCenter.y, mPlayerCircle.getmPlayerOnScreenRadius(), circlePaint);
    }

    private void drawStaticCircles(Canvas canvas, Paint circlePaint) {
        for (int i = 0; i < mStaticCircles.size(); i++) {
            StaticCircle sc = mStaticCircles.get(i);
            PointF circle_center = relativePointDistance(sc.getmCenter());
            circlePaint.setColor(sc.getmCcolor());
            canvas.drawCircle(circle_center.x, circle_center.y, relativeScreenSize(sc), circlePaint);
        }
    }

    private void drawAiCircles(Canvas canvas, Paint circlePaint) {
        for (int i = 1; i < mMovableCircles.size(); i++) {
            MovableCircle sc = mMovableCircles.get(i);
            PointF circle_center = relativePointDistance(sc.getmCenter());
            circlePaint.setColor(sc.getmCcolor());
            canvas.drawCircle(circle_center.x, circle_center.y, relativeScreenSize(sc), circlePaint);
        }
    }

    private void drawGameOverScene(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(150);
        canvas.drawText("Game Over", 0, canvas.getHeight() / 2, textPaint);
    }

    // SurfaceHolder.Callback's method, only called by system by once
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(LOG_TAG, "Surfaceview created");
        Canvas canvas = surfaceHolder.lockCanvas();
        mScreenWidth = canvas.getWidth();
        mScreenHeight = canvas.getHeight();
        surfaceHolder.unlockCanvasAndPost(canvas);

        mScreenCenter = new PointF(mScreenWidth / 2, mScreenHeight / 2);

        Log.d(LOG_TAG, "screen width, screen height = " +
                Integer.toString(mScreenWidth) + ", " + Integer.toString(mScreenHeight));

        mMapHeight = MAP_HEIGHT_ZOOM_UP_RATE * mScreenHeight;
        mMapWidth = MAP_WEIGHT_ZOOM_UP_RATE * mScreenWidth;

        mPlayerCircle.setCenter(Utility.generateRandomInt(0, mMapWidth), Utility.generateRandomInt(0, mMapHeight));

        mCircleManager.setMapSize(mMapWidth, mMapHeight);
        mCircleManager.getmMovableCircles().add(mPlayerCircle);

        mGameLogic.setmIsRun(true);
        mGameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Log.d(LOG_TAG, "Surfaceview changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(LOG_TAG, "Surfaceview destroyed");
        boolean retry = true;
        while (retry) {
            try {
                mGameLogic.setmIsRun(false);
                mGameThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    private float relativeScreenSize(AbstractCircle circle) {
        return circle.getmRadius() * mPlayerCircle.getmZoomRate();
    }

    private PointF relativePointDistance(PointF point) {
        float x = (point.x - mPlayerCircle.getmCenter().x) * mPlayerCircle.getmZoomRate();
        float y = (point.y - mPlayerCircle.getmCenter().y) * mPlayerCircle.getmZoomRate();
        return new PointF(x + mScreenWidth / 2, y + mScreenHeight / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mPlayerCircle.newDirection(new PointF(event.getX(), event.getY()), mScreenCenter);
        }

        return super.onTouchEvent(event);
    }
}
