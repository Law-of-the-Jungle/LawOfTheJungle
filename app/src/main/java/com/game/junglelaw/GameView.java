package com.game.junglelaw;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.game.junglelaw.circle.AiCircle;
import com.game.junglelaw.circle.StaticCircle;

import java.util.List;

/**
 * This class is used for rendering the view of game drawing the points in the GameLogic class
 * GameLogic instance is included in this view class, then we need to pass this view class to
 * GameLogic class in its constructor for drawing.
 * <p/>
 * GameView size range:
 * 0 <= x <= mMapWidth;
 * 0 <= y <= mMapHeight.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private String LOG_TAG = GameView.class.getSimpleName();

    private static final int MAP_HEIGHT_ZOOM_UP_RATE = 5;
    private static final int MAP_WEIGHT_ZOOM_UP_RATE = 5;
    private static final int COORDINATE_GRIDS_X_AXIS_UNIT = 100;
    private static final int COORDINATE_GRIDS_Y_AXIS_UNIT = 100;

    private final CircleManager mCircleManager;
    private final GameLogic mGameLogic;
    private final Thread mGameThread;
    private final SurfaceHolder mSurfaceHolder;

    //we need to get the relative location of points to the playerCircle
    //then check if these circle could appear on map or not
    //Draw the circle with location of screen
    private String mGameDifficulty;
    private int mMapWidth;
    private int mMapHeight;

    private int mScreenWidth;
    private int mScreenHeight;
    private PointF mScreenCenter;

    public GameView(Context context, String gameDifficulty) {
        super(context);

        mGameDifficulty = gameDifficulty;
        mGameLogic = new GameLogic(this);
        mGameThread = new Thread(mGameLogic);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mCircleManager = new CircleManager(gameDifficulty);
    }

    public CircleManager getmCircleManager() {
        return mCircleManager;
    }

    public int getScore() {
        return (int) mCircleManager.getmPlayerCircle().getmRadius();
    }

    public void pauseGame() {
        mGameLogic.setmIsPause(true);
    }

    public void resumeGame() {
        mGameLogic.setmIsPause(false);
    }

    public void drawGamePlayView() {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        draw(canvas);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void drawGameOverView() {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        draw(canvas);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(150);
        canvas.drawText("Game Over", 0, canvas.getHeight() / 2, textPaint);

        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);

        drawHintText(canvas);
        drawCoordinateGrids(canvas);
        drawBorder(canvas);

        Paint circlePaint = new Paint();
        drawPlayerCircle(canvas, circlePaint);
        drawAiCircles(canvas, circlePaint, mCircleManager.getmAiCircles());
        drawStaticCircles(canvas, circlePaint, mCircleManager.getmStaticCircles());
    }

    private void drawHintText(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(40);

        canvas.drawText("Score:" + getScore() + "; Game Difficulty: " + mGameDifficulty,
                5, canvas.getHeight() - 5, textPaint);
    }

    private void drawCoordinateGrids(Canvas canvas) {
        Paint coordinateGridsPaint = new Paint();
        coordinateGridsPaint.setColor(Color.GRAY);
        coordinateGridsPaint.setStrokeWidth(5);

        for (int x = 0; x < mMapWidth; x += COORDINATE_GRIDS_X_AXIS_UNIT) {
            PointF startPoint = zoomedOnScreenRelativeDistance(new PointF(x, 0));
            PointF stopPoint = zoomedOnScreenRelativeDistance(new PointF(x, mMapHeight));
            canvas.drawLine(startPoint.x, startPoint.y, stopPoint.x, stopPoint.y, coordinateGridsPaint);
        }

        for (int y = 0; y < mMapHeight; y += COORDINATE_GRIDS_Y_AXIS_UNIT) {
            PointF startPoint = zoomedOnScreenRelativeDistance(new PointF(0, y));
            PointF stopPoint = zoomedOnScreenRelativeDistance(new PointF(mMapWidth, y));
            canvas.drawLine(startPoint.x, startPoint.y, stopPoint.x, stopPoint.y, coordinateGridsPaint);
        }
    }

    /** Returns the length should be shown on screen, after zooming up */
    private float zoomedOnScreenLength(float len) {
        return len * mCircleManager.getmPlayerCircle().getmZoomRate();
    }

    /** Relative distance from a point to player circle center, after zooming up */
    private PointF zoomedOnScreenRelativeDistance(PointF point) {
        float x = (point.x - mCircleManager.getmPlayerCircle().getmCenter().x) * mCircleManager.getmPlayerCircle().getmZoomRate();
        float y = (point.y - mCircleManager.getmPlayerCircle().getmCenter().y) * mCircleManager.getmPlayerCircle().getmZoomRate();
        return new PointF(x + mScreenWidth / 2, y + mScreenHeight / 2);
    }

    private void drawBorder(Canvas canvas) {
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5);
        borderPaint.setColor(Color.BLACK);

        PointF topLeft = zoomedOnScreenRelativeDistance(new PointF(0, 0));
        PointF bottomRight = zoomedOnScreenRelativeDistance(new PointF(mMapWidth, mMapHeight));
        canvas.drawRect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y, borderPaint);
    }

    private void drawPlayerCircle(Canvas canvas, Paint circlePaint) {
        circlePaint.setColor(Color.BLACK);
        canvas.drawCircle(mScreenCenter.x, mScreenCenter.y, mCircleManager.getmPlayerCircle().getmOnScreenRadius(), circlePaint);
    }

    private void drawStaticCircles(Canvas canvas, Paint circlePaint, List<StaticCircle> mStaticCircles) {
        for (int i = 0; i < mStaticCircles.size(); i++) {
            StaticCircle staticCircle = mStaticCircles.get(i);
            PointF circle_center = zoomedOnScreenRelativeDistance(staticCircle.getmCenter());
            circlePaint.setColor(staticCircle.getmColor());
            canvas.drawCircle(circle_center.x, circle_center.y, zoomedOnScreenLength(staticCircle.getmRadius()), circlePaint);
        }
    }

    private void drawAiCircles(Canvas canvas, Paint circlePaint, List<AiCircle> mAiCircles) {
        for (int i = 0; i < mAiCircles.size(); i++) {
            AiCircle aiCircle = mAiCircles.get(i);
            PointF circle_center = zoomedOnScreenRelativeDistance(aiCircle.getmCenter());
            circlePaint.setColor(aiCircle.getmColor());
            canvas.drawCircle(circle_center.x, circle_center.y, zoomedOnScreenLength(aiCircle.getmRadius()), circlePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mCircleManager.getmPlayerCircle().newDirection(new PointF(event.getX(), event.getY()), mScreenCenter);
        }

        return super.onTouchEvent(event);
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

        // Take advantage of integer division to guarantee mMapWidth is an integer-multiple of COORDINATE_GRIDS_X_AXIS_UNIT
        mMapWidth = ((mScreenWidth * MAP_WEIGHT_ZOOM_UP_RATE) / COORDINATE_GRIDS_X_AXIS_UNIT) * COORDINATE_GRIDS_X_AXIS_UNIT;
        mMapHeight = ((mScreenHeight * MAP_HEIGHT_ZOOM_UP_RATE) / COORDINATE_GRIDS_Y_AXIS_UNIT) * COORDINATE_GRIDS_Y_AXIS_UNIT;

        mCircleManager.getmPlayerCircle().setCenter(Utility.generateRandomInt(0, mMapWidth), Utility.generateRandomInt(0, mMapHeight));

        mCircleManager.setMapSize(mMapWidth, mMapHeight);

        mGameLogic.setmIsGamePlaying(true);
        mGameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Log.d(LOG_TAG, "Surfaceview changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(LOG_TAG, "Surfaceview destroyed");
        while (true) {
            try {
                mGameLogic.setmIsGamePlaying(false);
                mGameThread.join();
                break;
            } catch (InterruptedException e) {
            }
        }
    }
}
