package com.example.modz.lavarun;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Patrick on 4/4/2018.
 */

public class BreakableBoulder extends GameObject {

    private int score;
    private int speed;
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private ArrayList<Integer> pos = new ArrayList<>();

    public BreakableBoulder(Bitmap res, int x, int y, int w, int h, int s, int numFrames)
    {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i = 0; i<image.length;i++) {
            image[i] = Bitmap.createBitmap(spritesheet, 0, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(100-speed);

        pos.add(10);
        pos.add(180);
        pos.add(320);

        this.x = pos.get(x);

    }

    public void update() {
        speed = 7 + (int) (Player.score/30);
        y+=speed;
        animation.update();
    }

    public void draw(Canvas canvas) {
        try{
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch(Exception e){}
    }

    public int getY() {
        return y;
    }
    public int getX() { return x; }

}
