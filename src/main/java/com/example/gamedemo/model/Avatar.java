package com.example.gamedemo.model;

import com.example.gamedemo.GameMain;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Avatar {

    private Canvas canvas;
    private GraphicsContext gc;
    private Image tank;
    public Vector pos;
    public Vector direction;

    public int initX=0;

    public int initY=0;

    public Avatar(Canvas canvas, int initX, int initY){
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        String uri = "file:"+ GameMain.class.getResource("tank.png").getPath();
        tank = new Image(uri);
        pos = new Vector(initX,initY);
        direction = new Vector(2,2);
        this.initX=initX;
        this.initY=initY;
    }

    public void draw(){
        gc.save();
        gc.translate(pos.x, pos.y);
        gc.rotate(90+direction.getAngle());
        gc.drawImage(tank, -25,-25, 50,50);
        gc.restore();
    }

    public void changeAngle(double a){
        double amp = direction.getAmplitude();
        double angle = direction.getAngle();
        angle += a;
        direction.x = amp*Math.cos(Math.toRadians(angle));
        direction.y = amp*Math.sin(Math.toRadians(angle));
    }

    public void moveForward(){
        pos.x += direction.x;
        pos.y += direction.y;
    }

    public void moveBackward() {
        pos.x -= direction.x;
        pos.y -= direction.y;
    }
}
