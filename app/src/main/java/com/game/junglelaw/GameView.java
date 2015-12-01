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
 * This class is used for rendering the view of game drawing the points in the PlayGround class
 * PlayGround instance is included in this view class, then we need to pass this view class to
 * PlayGround class in its constructor for drawing.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private String LOG_TAG = GameView.class.getSimpleName();

    private static final int MAP_HEIGHT_ZOOM_UP_RATE = 5;
    private static final int MAP_WEIGHT_ZOOM_UP_RATE = 5;
    private static final int DEFAULT_PLAYER_RADIUS = 40;

    private PlayGround mPlayGround;
    private int mScreenWidth;
    private int mScreenHeight;
    private PointF mScreenCenter;
    private SurfaceHolder mSurfaceHolder;

    //we need to get the relative location of points to the playerCircle
    //then check if these circle could appear on map or not
    //Draw the circle with location of screen
    /* Testing Data */
    public PlayerCircle mPlayerCircle;//User information
    //private float mPlayerOnScreenRadius=30;
    private int mMapWidth;
    private int mMapHeight;
    private List<StaticCircle> mStaticCircles;
    private List<MovableCircle> mMovableCircles;

    /**
     * -----------
     **/
    public int getmScreenHeight() {
        return mScreenHeight;
    }

    public int getmScreenWidth() {
        return mScreenWidth;
    }

    public int getmMapHeight() {
        return mMapHeight;
    }

    public int getmMapWidth() {
        return mMapWidth;
    }

    public GameView(Context context, String gameDifficulty) {
        super(context);

        mPlayGround = new PlayGround(this, gameDifficulty);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mPlayerCircle = new PlayerCircle(0, 0, DEFAULT_PLAYER_RADIUS, 0);
    }

    public float getScore() {
        return mPlayerCircle.getMass();
    }

    public void pause() {
        mPlayGround.setmIsPause(true);
    }

    public void resume() {
        mPlayGround.setmIsPause(false);
    }

    public void drawSCircleList(List<StaticCircle> list, Canvas canvas) {
        Paint p = new Paint();
        for (int i = 0; i < list.size(); i++) {
            StaticCircle sc = list.get(i);
            PointF circle_center = RelativeCenterLocation(sc);
            p.setColor(sc.getmCcolor());
            canvas.drawCircle(circle_center.x, circle_center.y, RelativeScreenSize(sc), p);
        }
    }

    public void drawMCircleList(List<MovableCircle> list, Canvas canvas) {
        Paint p = new Paint();
        for (int i = 0; i < list.size(); i++) {
            MovableCircle sc = list.get(i);
            PointF circle_center = RelativeCenterLocation(sc);
            p.setColor(sc.getmCcolor());
            canvas.drawCircle(circle_center.x, circle_center.y, RelativeScreenSize(sc), p);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPlayGround.isGameOver()) {
            gameOverScene(canvas);
            return;
        }

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(mScreenCenter.x, mScreenCenter.y, mPlayerCircle.mPlayerOnScreenRadius, p);
        mPlayerCircle.moveToDirection(getmMapWidth(), getmMapHeight());
        mStaticCircles = mPlayGround.getmCircleManager().getmStaticCircles();
        mMovableCircles = mPlayGround.getmCircleManager().getmMovableCircles();
        String displayText = "Score:" + getScore();
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        p.setTextSize(40);
        mPlayGround.getmCircleManager().controlPopulation();
        canvas.drawText(displayText, 5, canvas.getHeight() - 5, p);
        drawSCircleList(mStaticCircles, canvas);
        drawMCircleList(mMovableCircles, canvas);
    }

    public void gameOverScene(Canvas canvas) {
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
        mScreenCenter = new PointF(mScreenWidth / 2, mScreenHeight / 2);//Get mCenter of the canvas


        Log.d(LOG_TAG, "height:width=" + Integer.toString(mScreenWidth) + ":" + Integer.toString(mScreenHeight));
        //define map size
        mMapHeight = MAP_HEIGHT_ZOOM_UP_RATE * mScreenHeight;
        mMapWidth = MAP_WEIGHT_ZOOM_UP_RATE * mScreenWidth;

        int newX = Utility.generateRandomInt(0, mMapWidth);
        int newY = Utility.generateRandomInt(0, mMapHeight);
        mPlayerCircle.setCenter(newX, newY);

        mPlayGround.getmCircleManager().setMapSize(mMapWidth, mMapHeight);
        mPlayGround.getmCircleManager().getmMovableCircles().add(mPlayerCircle);
        surfaceHolder.unlockCanvasAndPost(canvas);
        mPlayGround.setmIsRun(true);
        mPlayGround.start();
    }

    public boolean inScreen(AbstractCircle sc) {
        if (Math.abs(sc.getmCenter().x - mPlayerCircle.getmCenter().x) <= sc.getmRadius() + mScreenWidth / 2 + 10 & Math.abs(sc.getmCenter().y - mPlayerCircle.getmCenter().y) <= sc.getmRadius() + mScreenHeight / 2 + 10) {
            return true;
        }
        return false;

    }

    public float RelativeScreenSize(AbstractCircle circle) {
        return circle.getmRadius() * mPlayerCircle.mZoomRate;
    }

    public PointF RelativeCenterLocation(AbstractCircle sc) {
        float x = (sc.getmCenter().x - mPlayerCircle.getmCenter().x) * mPlayerCircle.mZoomRate;
        float y = (sc.getmCenter().y - mPlayerCircle.getmCenter().y) * mPlayerCircle.mZoomRate;
        return new PointF(x + mScreenWidth / 2, y + mScreenHeight / 2);
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
                mPlayGround.setmIsRun(false);
                mPlayGround.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mPlayerCircle.newDirection(new PointF(event.getX(), event.getY()), mScreenCenter);
        }

        return super.onTouchEvent(event);
    }
}
