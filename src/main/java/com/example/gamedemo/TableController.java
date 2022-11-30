package com.example.gamedemo;

import com.example.gamedemo.model.Avatar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TableController implements Initializable {

    private MenuController mc;
    @FXML
    private TableColumn<Avatar, String> Players;

    @FXML
    private TableColumn<Avatar, Integer> Victories;

    @FXML
    private TableView<Avatar> table;

    ObservableList<Avatar> list = FXCollections.observableArrayList(
            new Avatar("mc.name1", 0),
            new Avatar("mc.name2", 0),
            new Avatar("mc.name3", 0)
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Players.setCellValueFactory(new PropertyValueFactory<Avatar, String>("name"));
        Victories.setCellValueFactory(new PropertyValueFactory<Avatar, Integer>("wins"));
            table.setItems(list);
    }
}