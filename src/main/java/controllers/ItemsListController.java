package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import objects.Item;

import java.io.IOException;

public class ItemsListController extends ListCell<Item> {

    private FXMLLoader loader;
    private Item item;
    private ItemsController itemsController;

    @FXML private AnchorPane anchorPane;
    @FXML private Button label;
    @FXML private Button del;

    public ItemsListController(ItemsController itemsController) {
        this.itemsController = itemsController;
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        this.item = item;
        super.updateItem(item, empty);

        if(empty || item == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/views/List.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            label.setText(item.getItemName());
            label.setOnAction(event -> itemsController.editItem(item));
            del.setOnAction(event -> itemsController.delItem(item));

            setText(null);
            setGraphic(anchorPane);
        }
    }
}
