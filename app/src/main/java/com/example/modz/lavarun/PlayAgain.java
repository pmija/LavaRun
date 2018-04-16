package com.example.modz.lavarun;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Modz on 3/13/2018.
 */

public class PlayAgain extends GameObject{
    private Bitmap playAgainBtn ;

    public PlayAgain(Bitmap res, int x, int y, int w, int h){
        super.x=x;
        super.y=y;
        width = w;
        height = h;
        playAgainBtn = res;
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(playAgainBtn,x,y,null);
    }
}
