package com.example.gamedemo.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wall {

    private Canvas canvas;
    private GraphicsContext gc;
    public int x,y;

    public int sizeX, sizeY;

    public Wall(Canvas canvas, int x, int y, int sizeX, int sizeY) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void draw(){
        gc.setFill(Color.GREEN);
        gc.fillRect(x-50,y-5,sizeX,sizeY);
    }
}
