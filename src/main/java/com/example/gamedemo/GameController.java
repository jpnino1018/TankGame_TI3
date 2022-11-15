package com.example.gamedemo;


import com.example.gamedemo.model.Avatar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    //Variables globales de la ventana
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean isRunning = true;


    //Elementos gráficos
    private Avatar avatar;


    //Estados de las teclas
    boolean Wpressed = false;
    boolean Apressed = false;
    boolean Spressed = false;
    boolean Dpressed = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);

        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);

        avatar = new Avatar(canvas);

        draw();
    }

    public void draw() {
        new Thread(
                () -> {
                    while (isRunning) {
                        Platform.runLater(() -> {
                            gc.setFill(Color.BLACK);
                            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                            avatar.draw();
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

    private void doKeyboardActions() {
        if (Wpressed) {
            avatar.moveForward();
        }
        if (Apressed) {
            avatar.changeAngle(-6);
        }
        if (Spressed) {
            avatar.moveBackward();
        }
        if (Dpressed) {
            avatar.changeAngle(6);
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
    }

}