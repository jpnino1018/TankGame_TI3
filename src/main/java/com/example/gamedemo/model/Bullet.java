package com.example.gamedemo.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet {
    private Canvas canvas;
    private GraphicsContext gc;
    public Vector pos;
    public Vector direction;

    public Bullet(Canvas canvas, Vector pos, Vector direction) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        this.pos = pos;
        this.direction = direction;


    }

    public void draw(){
        gc.setFill(Color.BLUE);
        gc.fillRect(pos.x-5,pos.y-5,10,10);
        pos.x += direction.x;
        pos.y += direction.y;
    }


}
