package com.example.gamedemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class MenuController {

    public String name1;
    public String name2;
    public String name3;
    @FXML
    private Button playBTN;

    @FXML
    private TextField player1TF;

    @FXML
    private TextField player2TF;

    @FXML
    private TextField player3TF;

    private final ArrayList<String> usernames = new ArrayList<>();
    @FXML
    void playGame(ActionEvent event) {
        ArrayList<String> localUsernames = new ArrayList<>();
        localUsernames.add(player1TF.getText());
        localUsernames.add(player2TF.getText());
        localUsernames.add(player3TF.getText());

        if (!verifyUsername(localUsernames)){
            name1 = player1TF.getText();
            name2 = player2TF.getText();
            name3 = player3TF.getText();

            usernames.add(player1TF.getText());
            usernames.add(player2TF.getText());
            usernames.add(player3TF.getText());
            GameMain.showWindow("canvasView.fxml");
        }
    }

    boolean verifyUsername(ArrayList<String> localUsernames){
        for (String username : localUsernames) {
            if (username.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Watch out!");
                alert.setContentText("Fill all fields");

                alert.showAndWait();
                return true;
            }
        }
        for (int i = 0; i < localUsernames.size(); i++) {
            for (int j = 0; j < usernames.size(); j++) {
                if (localUsernames.get(i).equals(usernames.get(j))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Watch out!");
                    alert.setContentText("The name: " + localUsernames.get(i) + " has already been added");

                    alert.showAndWait();
                    return true;
                }
            }
        }

        for (int i = 0; i < localUsernames.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (localUsernames.get(i).equals(localUsernames.get(j))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Watch out!");
                    alert.setContentText("The name: " + localUsernames.get(i) + " has already been added");

                    alert.showAndWait();
                    return true;
                }
            }
        }
        return false;
    }


}
