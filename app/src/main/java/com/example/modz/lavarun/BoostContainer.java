package com.example.modz.lavarun;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by Patrick on 4/4/2018.
 */

public class BoostContainer extends GameObject {
    private ArrayList<Bitmap> boostContainer;

    public BoostContainer(ArrayList<Bitmap> res){
        boostContainer = res;
    }
    public  void draw(Canvas canvas){

        canvas.drawBitmap(boostContainer.get(0),GamePanel.WIDTH/4+20, 20,null);

        if (Player.hasPowerup) {

            if (Player.currPowerup.getType() == 0) {
                canvas.drawBitmap(boostContainer.get(1), GamePanel.WIDTH/4+22, 24, null);
            }

            else if (Player.currPowerup.getType() == 1) {
                canvas.drawBitmap(boostContainer.get(2), GamePanel.WIDTH/4+87, 21, null);
            }

            else {
                canvas.drawBitmap(boostContainer.get(3), GamePanel.WIDTH/4+151, 24, null);
            }

        }

    }
}
