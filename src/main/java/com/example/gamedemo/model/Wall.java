package com.example.gamedemo.model;

import com.example.gamedemo.GameMain;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Wall {

    private Canvas canvas;
    private GraphicsContext gc;
    private Image wall;
    public int x,y;
    public final int sizeX=80, sizeY=30;

    public Wall(Canvas canvas, int x, int y) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        String uri = "file:"+ GameMain.class.getResource("walls.png").getPath();
        wall = new Image(uri);
        this.x = x;
        this.y = y;
    }

    public void draw(){
        gc.drawImage(wall,x-40,y-15,sizeX,sizeY);
    }
}
