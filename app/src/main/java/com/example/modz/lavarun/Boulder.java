package com.example.modz.lavarun;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Modz on 3/12/2018.
 */

public class Boulder extends GameObject{

    private int score;
    private int speed;
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private ArrayList<Integer> pos = new ArrayList<>();

    public Boulder(Bitmap res, int x, int y, int w, int h, int s, int numFrames)
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

    @Override
    public int getWidth() {
        //offset slightly for more realistic collision detection
        return width-10;
    }

    public int getY() {
        return y;
    }
}
