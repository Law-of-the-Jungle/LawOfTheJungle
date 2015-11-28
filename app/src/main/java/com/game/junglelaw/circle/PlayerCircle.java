package com.game.junglelaw.Circle;

import android.graphics.PointF;
import android.util.Log;
import android.view.View;

import com.game.junglelaw.GameView;

/**
 * Created by apple on 10/15/15.
 * Take charge of lan control
 */
public class PlayerCircle extends MovableCircle {

    private final float DEFAULT_SCREEN_RADIUS = 30;
    private final double SHIFT_THRESHOLD=0.3;

    public float player_on_screen_radius;
    public float zoom_rate;
    public PlayerCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color);
        player_on_screen_radius= DEFAULT_SCREEN_RADIUS;
        zoom_rate=MakeZoomRate();
    }
    /** 根据用户点击位置，计算player circle的新direction */
    private float MakeZoomRate(){
        return player_on_screen_radius/getRadius();
    }
    public void updateZoom(GameView view){
        //switch the lan to a border
        //Log.d("updateZoom",Double.toString(player_on_screen_radius)+" |  "+Double.toString(Math.min(view.getMap_height(),view.getMap_width())*SHIFT_THRESHOLD));
        if (player_on_screen_radius*2 > Math.min(view.getScreen_height(),view.getScreen_width())*SHIFT_THRESHOLD){
            player_on_screen_radius=DEFAULT_SCREEN_RADIUS;
            zoom_rate=MakeZoomRate();
        }else{
            player_on_screen_radius=getRadius()*zoom_rate;
        }
    }
    public void setNewDirection(PointF userClickPoint,PointF center) {
        float newX = userClickPoint.x - center.x;
        float newY = userClickPoint.y - center.y;
        float len = (float) Math.sqrt(Math.pow(newX, 2) + Math.pow(newY, 2)); // for 归一化处理
        setDirection(newX / len, newY / len);
    }
}