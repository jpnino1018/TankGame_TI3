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
    private ArrayList<Bullet> bullets1;
    private ArrayList<Bullet> bullets2;
    private ArrayList<Wall> walls;

    private ArrayList<Avatar> players;

    private ArrayList<Avatar> alivePlayers;

    public String winner;
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

        walls = new ArrayList<>();
        walls.add(new Wall(canvas, 300, 200));
        walls.add(new Wall(canvas, 220, 200));
        walls.add(new Wall(canvas, 380, 200));
        walls.add(new Wall(canvas, 400, 100));
        walls.add(new Wall(canvas, 400, 300));
        walls.add(new Wall(canvas, 200, 100));
        walls.add(new Wall(canvas, 200, 300));

        bullets1 = new ArrayList<>();
        bullets2 = new ArrayList<>();

        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);

        avatar1 = new Avatar(canvas, 100, 100, mc.name1);
        avatar2 = new Avatar(canvas, 500, 300, mc.name2);
        avatar3 = new Avatar(canvas, 100, 300, mc.name3);

        players = new ArrayList<>();

        players.add(avatar1);
        players.add(avatar2);
        players.add(avatar3);

        alivePlayers = new ArrayList<>();

        alivePlayers.add(avatar1);
        alivePlayers.add(avatar2);
        alivePlayers.add(avatar3);

        draw();
    }



    public void draw() {
        new Thread(
                () -> {
                    while (isRunning) {
                        Platform.runLater(() -> {
                            gc.setFill(Color.SANDYBROWN);
                            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                            if (avatar1.lifes>0) {
                                avatar1.draw();
                            }if (avatar2.lifes>0) {
                                avatar2.draw();
                            }if (avatar3.lifes>0) {
                                avatar3.draw();
                            }

                            for (int i = 0; i < walls.size(); i++) {
                                walls.get(i).draw();
                            }
                            for (int i = 0; i < bullets1.size(); i++) {
                                bullets1.get(i).draw();
                                if (bullets1.get(i).pos.x > canvas.getWidth() + 20 ||
                                        bullets1.get(i).pos.y > canvas.getHeight() + 20 ||
                                        bullets1.get(i).pos.y < -20 ||
                                        bullets1.get(i).pos.x < -20) {
                                    bullets1.remove(i);
                                }
                            }
                            for (int i = 0; i < bullets2.size(); i++) {
                                bullets2.get(i).draw();
                                if (bullets2.get(i).pos.x > canvas.getWidth() + 20 ||
                                        bullets2.get(i).pos.y > canvas.getHeight() + 20 ||
                                        bullets2.get(i).pos.y < -20 ||
                                        bullets2.get(i).pos.x < -20) {
                                    bullets2.remove(i);
                                }
                            }

                            //Colisiones
                            detectBulletCollision();
                            detectBorderCollision();
                            detectWallCollision();

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

    private void detectBulletCollision() {
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < bullets1.size(); j++) {
                Bullet b = bullets1.get(j);
                Avatar a = players.get(i);

                double c1 = b.pos.x - a.pos.x;
                double c2 = b.pos.y - a.pos.y;
                double distance = Math.sqrt(Math.pow(c1, 2) + Math.pow(c2, 2));
                if (distance < 12.5 && a!=avatar1) {
                    bullets1.remove(j);
                    playerSufferedDamage(i);
                    return;
                }
            }
            for (int j = 0; j < bullets2.size(); j++) {
                Bullet b = bullets2.get(j);
                Avatar a = players.get(i);

                double c1 = b.pos.x - a.pos.x;
                double c2 = b.pos.y - a.pos.y;
                double distance = Math.sqrt(Math.pow(c1, 2) + Math.pow(c2, 2));
                if (distance < 12.5 && a!=avatar2) {
                    bullets2.remove(j);
                    playerSufferedDamage(i);
                    return;
                }
            }
        }

        for (int i = 0; i < walls.size(); i++) {
            for (int j = 0; j < bullets1.size(); j++) {
                Bullet b = bullets1.get(j);
                Wall w = walls.get(i);

                if (b.pos.x <= w.x+w.sizeX/2 && b.pos.x>=w.x-w.sizeX/2){
                    if (b.pos.y <= w.y+w.sizeY/2 && b.pos.y>=w.y-w.sizeY/2){
                        bullets1.remove(j);
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
                            playerSufferedDamage(j);
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
            if (avatar1.bulletsInMagazine > 0) {
                Bullet bullet = new Bullet(canvas,
                        new Vector(avatar1.pos.x, avatar1.pos.y),
                        new Vector(2 * avatar1.direction.x, 2 * avatar1.direction.y));
                bullets1.add(bullet);
                avatar1.bulletsInMagazine = avatar1.bulletsInMagazine -1;
            }
        }
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (avatar2.bulletsInMagazine > 0) {
                Bullet bullet = new Bullet(canvas,
                        new Vector(avatar2.pos.x, avatar2.pos.y),
                        new Vector(2 * avatar2.direction.x, 2 * avatar2.direction.y));
                bullets2.add(bullet);
                avatar2.bulletsInMagazine = avatar2.bulletsInMagazine -1;
            }
        }
        if (keyEvent.getCode() == KeyCode.R) {
            avatar1.bulletsInMagazine = avatar1.magazineSize;
        }
        if (keyEvent.getCode() == KeyCode.P) {
            avatar2.bulletsInMagazine = avatar2.magazineSize;
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

    public void playerSufferedDamage(int index){
        Avatar p = players.get(index);
        if (p.lifes > 0) {
            p.lifes = p.lifes - 1;
            p.pos.x=p.initX;
            p.pos.y=p.initY;
        }
        if (p.lifes==0){
            alivePlayers.remove(p);
            players.remove(p);
            checkWin();
        }
    }

    public void checkWin(){
        if (alivePlayers.size()==1){
            winner = alivePlayers.get(0).name;
            GameMain.showWindow("winnerSign.fxml");
        }
    }
}