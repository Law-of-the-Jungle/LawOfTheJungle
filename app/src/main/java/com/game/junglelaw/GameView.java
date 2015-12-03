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

import com.game.junglelaw.circle.AiCircle;
import com.game.junglelaw.circle.StaticCircle;

/**
 * This class is used for rendering the view of game drawing the points in the GameLogic class
 * GameLogic instance is included in this view class, then we need to pass this view class to
 * GameLogic class in its constructor for drawing.
 *
 * GameView size range:
 *     0 <= x <= mMapWidth;
 *     0 <= y <= mMapHeight.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private String LOG_TAG = GameView.class.getSimpleName();

    private static final int MAP_HEIGHT_ZOOM_UP_RATE = 5;
    private static final int MAP_WEIGHT_ZOOM_UP_RATE = 5;
    private static final int COORDINATE_GRIDS_X_AXIS_UNIT = 100;
    private static final int COORDINATE_GRIDS_Y_AXIS_UNIT = 100;

    private final GameLogic mGameLogic;
    private final Thread mGameLogicThread;
    private final SurfaceHolder mSurfaceHolder;
    private final String mGameDifficulty;

    //we need to get the relative location of points to the playerCircle
    //then check if these circle could appear on map or not
    //Draw the circle with location of screen
    private CircleManager mCircleManager;
    private float mMapWidth;
    private float mMapHeight;

    private float mScreenWidth;
    private float mScreenHeight;
    private PointF mScreenCenterPoint;

    public GameView(Context context, String gameDifficulty) {
        super(context);

        mGameLogic = new GameLogic(this);
        mGameLogicThread = new Thread(mGameLogic);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mGameDifficulty = gameDifficulty;
    }

    public CircleManager getmCircleManager() {
        return mCircleManager;
    }

    public int getScore() {
        return (int) Math.pow(mCircleManager.getmPlayerCircle().getmRadius(), 2);
    }

    public void pauseGame() {
        mGameLogic.setmIsPause(true);
    }

    public void resumeGame() {
        mGameLogic.setmIsPause(false);
    }

    public void drawGamePlayView() {
        Canvas canvas = mSurfaceHolder.lockCanvas();

        if (canvas == null) { // This will be executed only when surfaceDestroyed() get called
            return;
        }

        draw(canvas);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void drawGameOverView() {
        Canvas canvas = mSurfaceHolder.lockCanvas();

        if (canvas == null) { // This will be executed only when surfaceDestroyed() get called
            return;
        }

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
        drawCircles(canvas);
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

        for (int x = COORDINATE_GRIDS_X_AXIS_UNIT; x < mMapWidth; x += COORDINATE_GRIDS_X_AXIS_UNIT) {
            PointF startPoint = zoomedOnScreenRelativeDistance(new PointF(x, 0));
            PointF stopPoint = zoomedOnScreenRelativeDistance(new PointF(x, mMapHeight));
            canvas.drawLine(startPoint.x, startPoint.y, stopPoint.x, stopPoint.y, coordinateGridsPaint);
        }

        for (int y = COORDINATE_GRIDS_Y_AXIS_UNIT; y < mMapHeight; y += COORDINATE_GRIDS_Y_AXIS_UNIT) {
            PointF startPoint = zoomedOnScreenRelativeDistance(new PointF(0, y));
            PointF stopPoint = zoomedOnScreenRelativeDistance(new PointF(mMapWidth, y));
            canvas.drawLine(startPoint.x, startPoint.y, stopPoint.x, stopPoint.y, coordinateGridsPaint);
        }
    }

    /**
     * Returns the length should be shown on screen, after zooming up
     */
    private float zoomedOnScreenLength(float len) {
        return len * mCircleManager.getmPlayerCircle().getmZoomRate();
    }

    /**
     * Relative distance from a point to player circle center, after zooming up
     */
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

    private void drawCircles(Canvas canvas) {
        Paint circlePaint = new Paint();

        // Draw player circle
        circlePaint.setColor(Color.BLACK);
        canvas.drawCircle(mScreenCenterPoint.x, mScreenCenterPoint.y, mCircleManager.getmPlayerCircle().getmOnScreenRadius(), circlePaint);

        // Draw ai circles
        for (AiCircle aiCircle : mCircleManager.getmAiCircles()) {
            circlePaint.setColor(aiCircle.getmColor());
            PointF onScreenCenter = zoomedOnScreenRelativeDistance(aiCircle.getmCenter());
            canvas.drawCircle(onScreenCenter.x, onScreenCenter.y, zoomedOnScreenLength(aiCircle.getmRadius()), circlePaint);
        }

        // Draw static circles
        for (StaticCircle staticCircle : mCircleManager.getmStaticCircles()) {
            circlePaint.setColor(staticCircle.getmColor());
            PointF onScreenCenter = zoomedOnScreenRelativeDistance(staticCircle.getmCenter());
            canvas.drawCircle(onScreenCenter.x, onScreenCenter.y, zoomedOnScreenLength(staticCircle.getmRadius()), circlePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mCircleManager.getmPlayerCircle().newDirection(new PointF(event.getX(), event.getY()), mScreenCenterPoint);
        }

        return super.onTouchEvent(event);
    }

    // SurfaceHolder.Callback's method, only called by android system
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(LOG_TAG, "Surfaceview created");
        Canvas canvas = surfaceHolder.lockCanvas();
        mScreenWidth = canvas.getWidth();
        mScreenHeight = canvas.getHeight();
        surfaceHolder.unlockCanvasAndPost(canvas);

        Log.d(LOG_TAG, "screen width, screen height = " + mScreenWidth + ", " + mScreenHeight);

        mScreenCenterPoint = new PointF(mScreenWidth / 2, mScreenHeight / 2);

        // Take advantage of integer division to guarantee mMapWidth is an integer-multiple of COORDINATE_GRIDS_X_AXIS_UNIT
        mMapWidth = (((int) mScreenWidth * MAP_WEIGHT_ZOOM_UP_RATE) / COORDINATE_GRIDS_X_AXIS_UNIT) * COORDINATE_GRIDS_X_AXIS_UNIT;
        mMapHeight = (((int) mScreenHeight * MAP_HEIGHT_ZOOM_UP_RATE) / COORDINATE_GRIDS_Y_AXIS_UNIT) * COORDINATE_GRIDS_Y_AXIS_UNIT;

        mCircleManager = new CircleManager(mGameDifficulty, mMapWidth, mMapHeight);

        // Start the game
        mGameLogic.setmIsGamePlaying(true);
        mGameLogicThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.d(LOG_TAG, "Surfaceview changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(LOG_TAG, "Surfaceview destroyed");
        try {
            mGameLogic.setmIsGamePlaying(false);
            mGameLogicThread.join();
        } catch (InterruptedException e) {
        }
    }
}
