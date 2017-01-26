package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import objects.Shop;

import java.io.IOException;

public class ShopsListController extends ListCell<Shop> {
    private FXMLLoader loader;
    private Shop shop;
    private ShopsController shopsController;

    @FXML private AnchorPane anchorPane;
    @FXML private Button label;
    @FXML private Button del;

    public ShopsListController(ShopsController shopsController) {
        this.shopsController = shopsController;
    }

    @Override
    protected void updateItem(Shop shop, boolean empty) {
        this.shop = shop;
        super.updateItem(shop, empty);

        if(empty || shop == null) {

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

            label.setText(shop.getShopName());
            label.setOnAction(event -> shopsController.editShop(shop));
            del.setOnAction(event -> shopsController.delShop(shop));

            setText(null);
            setGraphic(anchorPane);
        }
    }
}
