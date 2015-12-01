package com.game.junglelaw;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.preference.PreferenceManager;
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

    private static final int HEIGHT_ZOOM_UP_RATE = 5;
    private static final int WEIGHT_ZOOM_UP_RATE = 5;
    private static final int DEFAULT_PLAYER_RADIUS = 40;

    private PlayGround mPlayground;
    private int mScreenWidth;
    private int mScreenHeight;
//    private PointF mScreenCenter;
    private SurfaceHolder mSurfaceHolder;

    //we need to get the relative location of points to the playerCircle
    //then check if these circle could appear on map or not
    //Draw the circle with location of screen
    /* Testing Data */
    public PlayerCircle player;//User information
    //private float mPlayerOnScreenRadius=30;
    private int map_height, map_width;
    List<StaticCircle> SCircle;
    List<MovableCircle> MCircle;

    /**
     * -----------
     **/
    public int getmScreenHeight() {
        return mScreenHeight;
    }

    public int getmScreenWidth() {
        return mScreenWidth;
    }

    public int getMap_height() {
        return map_height;
    }

    public int getMap_width() {
        return map_width;
    }

    public GameView(Context context) {
        super(context);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String difficulty =
            prefs.getString(context.getString(R.string.pref_diffculty_key), context.getString(R.string.pref_diffculty_easy));

        mPlayground = new PlayGround(this, difficulty);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        player = new PlayerCircle(0, 0, DEFAULT_PLAYER_RADIUS, 0);
    }

    public float getScore() {
        return player.getMass();
    }

    public void pause() {
        mPlayground.setPauseState(true);
    }

    public void resume() {
        mPlayground.setPauseState(false);
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
        if (mPlayground.isGameOver()) {
            gameOverScene(canvas);
            return;
        }

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(mScreenWidth / 2, mScreenHeight / 2, player.mPlayerOnScreenRadius, p);
        player.moveToDirection(getMap_width(), getMap_height());
        SCircle = mPlayground.getmCircleManager().getmStaticCircles();
        MCircle = mPlayground.getmCircleManager().getmMovableCircles();
        String displayText = "Score:" + getScore();
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        p.setTextSize(40);
        mPlayground.getmCircleManager().controlPopulation();
        canvas.drawText(displayText, 5, canvas.getHeight() - 5, p);
        drawSCircleList(SCircle, canvas);
        drawMCircleList(MCircle, canvas);
    }

    public void gameOverScene(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(150);
        canvas.drawText("Game Over", 0, canvas.getHeight() / 2, textPaint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(LOG_TAG, "Surfaceview created");
        Canvas canvas = surfaceHolder.lockCanvas();
        mScreenWidth = canvas.getWidth();
        mScreenHeight = canvas.getHeight();


        Log.d(LOG_TAG, "height:width=" + Integer.toString(mScreenWidth) + ":" + Integer.toString(mScreenHeight));
        //define map size
        map_height = HEIGHT_ZOOM_UP_RATE * mScreenHeight;
        map_width = WEIGHT_ZOOM_UP_RATE * mScreenWidth;

        player.setCenter(Utility.generateRandomInt(0, map_width), Utility.generateRandomInt(0, map_height));

        mPlayground.getmCircleManager().setSize(map_width, map_height);
        mPlayground.getmCircleManager().getmMovableCircles().add(player);
        surfaceHolder.unlockCanvasAndPost(canvas);
        mPlayground.setRunState(true);
        mPlayground.start();
    }

    public boolean inScreen(AbstractCircle sc) {
        if (Math.abs(sc.getmCenter().x - player.getmCenter().x) <= sc.getmRadius() + mScreenWidth / 2 + 10 & Math.abs(sc.getmCenter().y - player.getmCenter().y) <= sc.getmRadius() + mScreenHeight / 2 + 10) {
            return true;
        }
        return false;

    }

    public float RelativeScreenSize(AbstractCircle circle) {
        return circle.getmRadius() * player.mZoomRate;
    }

    public PointF RelativeCenterLocation(AbstractCircle sc) {
        float zoom = player.mZoomRate;
        float x = (sc.getmCenter().x - player.getmCenter().x) * zoom;
        float y = (sc.getmCenter().y - player.getmCenter().y) * zoom;
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
                mPlayground.setRunState(false);
                mPlayground.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            player.setDirectTowardPoint(new PointF(event.getX(), event.getY()));
            Log.i(LOG_TAG, event.toString());
        }

        return super.onTouchEvent(event);
    }
}
