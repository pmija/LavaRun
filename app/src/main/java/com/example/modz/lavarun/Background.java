package com.example.modz.lavarun;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Modz on 3/10/2018.
 */

public class Background {
    private Bitmap image;
    private int x,y,dy;

    public Background(Bitmap res){
        image=res;
        dy=GamePanel.MOVESPEED;


    }
    public void update(){
        y+=7 + (int) (Player.score/30);
        if(y>GamePanel.HEIGHT){
            y=0;
        }
    }

    public  void draw(Canvas canvas){
        canvas.drawBitmap(image,x,y,null);
        if(y>0){
            canvas.drawBitmap(image,x,y-GamePanel.HEIGHT,null);
        }

    }


}
