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

    private static final int HEIGHT_ZOOM_UP_RATE = 5;
    private static final int WEIGHT_ZOOM_UP_RATE = 5;
    private int DEFAULT_PLAYER_RADIUS = 40;
    private PlayGround playground;
    private int Screen_height, Screen_width;
    private PointF center;
    private SurfaceHolder holder;
    private String TAG = GameView.class.getSimpleName();

    //we need to get the relative location of points to the playerCircle
    //then check if these circle could appear on map or not
    //Draw the circle with location of screen
    /* Testing Data */
    public PlayerCircle player;//User information
    //private float player_on_screen_radius=30;
    private int map_height, map_width;
    List<StaticCircle> SCircle;
    List<MovableCircle> MCircle;

    /**
     * -----------
     **/
    public int getScreen_height() {
        return Screen_height;
    }

    public int getScreen_width() {
        return Screen_width;
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

        playground = new PlayGround(this, difficulty);
        holder = getHolder();
        holder.addCallback(this);
        player = new PlayerCircle(0, 0, DEFAULT_PLAYER_RADIUS, 0);
    }

    public float getScore() {
        return player.getMass();
    }

    public void pause() {
        playground.setPauseState(true);
    }

    public void resume() {
        playground.setPauseState(false);
    }

    public void drawSCircleList(List<StaticCircle> list, Canvas canvas) {
        Paint p = new Paint();
        for (int i = 0; i < list.size(); i++) {
            StaticCircle sc = list.get(i);
            PointF circle_center = RelativeCenterLocation(sc);
            p.setColor(sc.getColor());
            canvas.drawCircle(circle_center.x, circle_center.y, RelativeScreenSize(sc), p);
        }
    }

    public void drawMCircleList(List<MovableCircle> list, Canvas canvas) {
        Paint p = new Paint();
        for (int i = 0; i < list.size(); i++) {
            MovableCircle sc = list.get(i);
            PointF circle_center = RelativeCenterLocation(sc);
            p.setColor(sc.getColor());
            canvas.drawCircle(circle_center.x, circle_center.y, RelativeScreenSize(sc), p);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (playground.isGameOver()) {
            gameOverScene(canvas);
            return;
        }

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(center.x, center.y, player.player_on_screen_radius, p);
        player.moveToDirection(getMap_width(), getMap_height());
        SCircle = playground.getManger().ProvideStatic();
        MCircle = playground.getManger().ProvideMovable();
        String displayText = "Score:" + getScore();
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        p.setTextSize(40);
        playground.getManger().controlPopulation();
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
        Log.d(TAG, "Surfaceview created");
        Canvas canvas = surfaceHolder.lockCanvas();
        Screen_width = canvas.getWidth();
        Screen_height = canvas.getHeight();
        center = new PointF(Screen_width / 2, Screen_height / 2);//Get center of the canvas


        Log.d(TAG, "height:width=" + Integer.toString(Screen_width) + ":" + Integer.toString(Screen_height));
        //define map size
        map_height = HEIGHT_ZOOM_UP_RATE * Screen_height;
        map_width = WEIGHT_ZOOM_UP_RATE * Screen_width;

        int newX = Utility.generateRandomInt(0, map_width);
        int newY = Utility.generateRandomInt(0, map_height);
        player.setCenter(newX, newY);

        playground.getManger().setSize(map_width, map_height);
        playground.getManger().ProvideMovable().add(player);
        surfaceHolder.unlockCanvasAndPost(canvas);
        playground.setRunState(true);
        playground.start();
    }

    public boolean inScreen(AbstractCircle sc) {
        if (Math.abs(sc.getCenter().x - player.getCenter().x) <= sc.getRadius() + Screen_width / 2 + 10 & Math.abs(sc.getCenter().y - player.getCenter().y) <= sc.getRadius() + Screen_height / 2 + 10) {
            return true;
        }
        return false;

    }

    public float RelativeScreenSize(AbstractCircle circle) {
        return circle.getRadius() * player.zoom_rate;
    }

    public PointF RelativeCenterLocation(AbstractCircle sc) {
        float zoom = player.zoom_rate;
        float x = (sc.getCenter().x - player.getCenter().x) * zoom;
        float y = (sc.getCenter().y - player.getCenter().y) * zoom;
        return new PointF(x + Screen_width / 2, y + Screen_height / 2);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Log.d(TAG, "Surfaceview changed");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "Surfaceview destroyed");
        boolean retry = true;
        while (retry) {
            try {
                playground.setRunState(false);
                playground.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            player.setNewDirection(new PointF(event.getX(), event.getY()), center);
        }

        return super.onTouchEvent(event);
    }
}
