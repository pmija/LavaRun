package com.example.modz.lavarun;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Modz on 3/13/2018.
 */

public class ReturnMainMenu extends GameObject{
    private Bitmap returnMain ;

    public ReturnMainMenu(Bitmap res, int x, int y, int w, int h){
        super.x=x;
        super.y=y;
        width = w;
        height = h;
        returnMain = res;
    }
    public  void draw(Canvas canvas){
        canvas.drawBitmap(returnMain,x,y,null);

    }
}
