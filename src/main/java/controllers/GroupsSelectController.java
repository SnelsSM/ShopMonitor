package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import objects.Group;
import objects.Item;

import java.io.IOException;

public class GroupsSelectController extends ListCell<Item> {
    private FXMLLoader loader;

    @FXML private Label label;

    public GroupsSelectController() {

    }

    @Override
    protected void updateItem(Item item, boolean empty) {

        super.updateItem(item, empty);

        if(empty || item == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/views/Select.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            label.setText(item.getItemName());

            setText(null);
            setGraphic(label);
        }
    }
}
