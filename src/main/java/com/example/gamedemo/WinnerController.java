package com.example.gamedemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class WinnerController {

    @FXML
    private Button continueBTN;

    @FXML
    private Label winnerLBL;
    @FXML
    void showResults(ActionEvent event) {
        GameMain.showWindow("table.fxml");
    }

}
