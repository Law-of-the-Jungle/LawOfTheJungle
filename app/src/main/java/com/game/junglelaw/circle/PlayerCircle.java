package com.game.junglelaw.circle;

import android.graphics.Movie;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;

import com.game.junglelaw.GameView;
import com.game.junglelaw.R;

import java.io.InputStream;

/**
 * Created by apple on 10/15/15.
 * Take charge of lan control
 */
public class PlayerCircle extends MovableCircle {

    private final float DEFAULT_SCREEN_RADIUS = 30;
    private final double SHIFT_THRESHOLD = 0.25;

    public float player_on_screen_radius;
    public float zoom_rate;

    public PlayerCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color, "");
        player_on_screen_radius = DEFAULT_SCREEN_RADIUS;
        zoom_rate = MakeZoomRate();
    }

    /**
     * 根据用户点击位置，计算player circle的新direction
     */
    private float MakeZoomRate() {
        return player_on_screen_radius / getRadius();
    }

    public void updateZoom(GameView view) {
        //switch the lan to a border
        //Log.d("updateZoom",Double.toString(player_on_screen_radius)+" |  "+Double.toString(Math.min(view.getMap_height(),view.getMap_width())*SHIFT_THRESHOLD));
        //if (player_on_screen_radius*2 > Math.min(view.getScreen_height(),view.getScreen_width())*SHIFT_THRESHOLD){
        if (player_on_screen_radius >= 80) {
            //player_on_screen_radius=DEFAULT_SCREEN_RADIUS;
            player_on_screen_radius *= 0.8;
            zoom_rate = MakeZoomRate();
        } else {
            player_on_screen_radius = getRadius() * zoom_rate;
        }
    }
}