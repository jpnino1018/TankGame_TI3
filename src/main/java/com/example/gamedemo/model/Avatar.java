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

    public Avatar(Canvas canvas){
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        String uri = "file:"+ GameMain.class.getResource("tank.png").getPath();
        tank = new Image(uri);
        pos = new Vector(100,100);
        direction = new Vector(2,2);
    }

    public void draw(){
        gc.save();
        gc.translate(pos.x, pos.y);
        gc.rotate(90+direction.getAngle());
        gc.drawImage(tank, -25,-25, 50,50);
        gc.restore();
    }
    public void setPosition(double x, double y) {
        pos.x = (int) x - 25;
        pos.y = (int) y - 25;
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
