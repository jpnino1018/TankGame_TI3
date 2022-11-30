package com.example.gamedemo;


import com.example.gamedemo.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    //Variables globales de la ventana
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean isRunning = true;

    private MenuController mc;


    //Elementos gráficos
    private Avatar avatar1;
    private Avatar avatar2;
    private Avatar avatar3;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private ArrayList<Wall> walls;

    private ArrayList<Avatar> players;


    //Estados de las teclas
    boolean Wpressed = false;
    boolean Apressed = false;
    boolean Spressed = false;
    boolean Dpressed = false;
    boolean UPpressed = false;
    boolean LEFTpressed = false;
    boolean DOWNpressed = false;
    boolean RIGHTpressed = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        mc = new MenuController();
        canvas.setFocusTraversable(true);

        enemies = new ArrayList<>();
        enemies.add(new Enemy(canvas, 300, 100));
        enemies.add(new Enemy(canvas, 300, 300));

        walls = new ArrayList<>();
        walls.add(new Wall(canvas, 300, 200));
        walls.add(new Wall(canvas, 220, 200));
        walls.add(new Wall(canvas, 380, 200));
        walls.add(new Wall(canvas, 400, 100));
        walls.add(new Wall(canvas, 400, 300));
        walls.add(new Wall(canvas, 200, 100));
        walls.add(new Wall(canvas, 200, 300));

        bullets = new ArrayList<>();

        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);

        avatar1 = new Avatar(canvas, 100, 100, mc.name1);
        avatar2 = new Avatar(canvas, 500, 300, mc.name2);
        avatar3 = new Avatar(canvas, 100, 300, mc.name3);

        players = new ArrayList<>();

        players.add(avatar1);
        players.add(avatar2);
        players.add(avatar3);

        draw();
    }



    public void draw() {
        new Thread(
                () -> {
                    while (isRunning) {
                        Platform.runLater(() -> {
                            gc.setFill(Color.DIMGRAY);
                            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                            avatar1.draw();
                            avatar2.draw();
                            avatar3.draw();
                            //Pintar enemigos
                            for (int i = 0; i < enemies.size(); i++) {
                                enemies.get(i).draw();
                            }
                            for (int i = 0; i < walls.size(); i++) {
                                walls.get(i).draw();
                            }
                            for (int i = 0; i < bullets.size(); i++) {
                                bullets.get(i).draw();
                                if (bullets.get(i).pos.x > canvas.getWidth() + 20 ||
                                        bullets.get(i).pos.y > canvas.getHeight() + 20 ||
                                        bullets.get(i).pos.y < -20 ||
                                        bullets.get(i).pos.x < -20) {
                                    bullets.remove(i);
                                }
                            }

                            //Colisiones
                            detectBulletCollision();
                            detectBorderCollision();
                            detectWallCollision();
                            deadDetection();

                            doKeyboardActions();

                        });
                        //Sleep
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        ).start();
    }

   private void deadDetection(){
       for (int i = 0; i < players.size(); i++) {
           for (int j = 0; j < bullets.size(); j++) {
               Bullet b = bullets.get(j);
               Avatar a = players.get(i);

               double c1 = b.pos.x - a.pos.x;
               double c2 = b.pos.y - a.pos.y;
               double distance = Math.sqrt(Math.pow(c1, 2) + Math.pow(c2, 2));
               if (  players.get(j).lifes == 0) {
                   bullets.remove(j);
                   players.remove(i);
                   return;
               }else {
                  players.get(j).lifes = players.get(j).lifes - 1;
               }
           }
       }
   }
    private void detectBulletCollision() {
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {
                Bullet b = bullets.get(j);
                Enemy e = enemies.get(i);

                double c1 = b.pos.x - e.x;
                double c2 = b.pos.y - e.y;
                double distance = Math.sqrt(Math.pow(c1, 2) + Math.pow(c2, 2));
                if (distance < 12.5) {
                    bullets.remove(j);
                    enemies.remove(i);
                    return;
                }
            }
        }
        for (int i = 0; i < walls.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {
                Bullet b = bullets.get(j);
                Wall w = walls.get(i);

                if (b.pos.x <= w.x+w.sizeX/2 && b.pos.x>=w.x-w.sizeX/2){
                    if (b.pos.y <= w.y+w.sizeY/2 && b.pos.y>=w.y-w.sizeY/2){
                        bullets.remove(j);
                        return;
                    }
                }
            }
        }
    }

    private void detectBorderCollision(){
        if (avatar1.pos.x<=0){
            avatar1.pos.x=0;
        }
        if (avatar1.pos.x>=canvas.getWidth()){
            avatar1.pos.x=canvas.getWidth();
        }
        if (avatar1.pos.y<=0){
            avatar1.pos.y=0;
        }
        if (avatar1.pos.y>=canvas.getHeight()){
            avatar1.pos.y=canvas.getHeight();
        }
        if (avatar2.pos.x<=0){
            avatar2.pos.x=0;
        }
        if (avatar2.pos.x>=canvas.getWidth()){
            avatar2.pos.x=canvas.getWidth();
        }
        if (avatar2.pos.y<=0){
            avatar2.pos.y=0;
        }
        if (avatar2.pos.y>=canvas.getHeight()){
            avatar2.pos.y=canvas.getHeight();
        }
    }

    public void detectWallCollision(){
        for (int i = 0; i < walls.size(); i++) {
            for (int j = 0; j < players.size(); j++) {
                Avatar a = players.get(j);
                Wall w = walls.get(i);

                if (a.pos.x <= w.x+w.sizeX/2 && a.pos.x>=w.x-w.sizeX/2){
                    if (a.pos.y <= w.y+w.sizeY/2 && a.pos.y>=w.y-w.sizeY/2){
                        if (a.lifes>0) {
                            a.pos.x = a.initX;
                            a.pos.y = a.initY;
                            playerSufferedDamage(a);
                            if (a.lifes<=0){
                                players.remove(j);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
    private void doKeyboardActions() {
        if (Wpressed) {
            avatar1.moveForward();
        }
        if (Apressed) {
            avatar1.changeAngle(-6);
        }
        if (Spressed) {
            avatar1.moveBackward();
        }
        if (Dpressed) {
            avatar1.changeAngle(6);
        }
        if (UPpressed) {
            avatar2.moveForward();
        }
        if (LEFTpressed) {
            avatar2.changeAngle(-6);
        }
        if (DOWNpressed) {
            avatar2.moveBackward();
        }
        if (RIGHTpressed) {
            avatar2.changeAngle(6);
        }
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.W) {
            Wpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.A) {
            Apressed = false;
        }
        if (keyEvent.getCode() == KeyCode.S) {
            Spressed = false;
        }
        if (keyEvent.getCode() == KeyCode.D) {
            Dpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.UP) {
            UPpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.LEFT) {
            LEFTpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.DOWN) {
            DOWNpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            RIGHTpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.SPACE) {
            if (avatar1.bullets > 0) {
                Bullet bullet = new Bullet(canvas,
                        new Vector(avatar1.pos.x, avatar1.pos.y),
                        new Vector(2 * avatar1.direction.x, 2 * avatar1.direction.y));
                bullets.add(bullet);
                avatar1.bullets = avatar1.bullets-1;
            }
        }
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (avatar2.bullets > 0) {
                Bullet bullet = new Bullet(canvas,
                        new Vector(avatar2.pos.x, avatar2.pos.y),
                        new Vector(2 * avatar2.direction.x, 2 * avatar2.direction.y));
                bullets.add(bullet);
                avatar2.bullets = avatar2.bullets-1;
            }
        }
        if (keyEvent.getCode() == KeyCode.R) {
            avatar1.bullets = avatar1.magazineSize;
        }
        if (keyEvent.getCode() == KeyCode.P) {
            avatar2.bullets = avatar2.magazineSize;
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        if (keyEvent.getCode() == KeyCode.W) {
            Wpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.A) {
            Apressed = true;
        }
        if (keyEvent.getCode() == KeyCode.S) {
            Spressed = true;
        }
        if (keyEvent.getCode() == KeyCode.D) {
            Dpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.UP) {
            UPpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.LEFT) {
            LEFTpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.DOWN) {
            DOWNpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            RIGHTpressed = true;
        }
    }

    public void playerSufferedDamage(Avatar p){
        if (p.lifes > 0) {
            p.lifes = p.lifes - 1;
        }
    }

    public void checkWin(){
        if(players.size()==1){
            String winner = players.get(0).name;
        }
    }
}