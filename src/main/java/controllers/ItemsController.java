package controllers;

import actions.IItemAction;
import actions.impls.ItemAction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import objects.Collections;
import objects.Group;
import objects.Item;
import objects.Shop;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ItemsController implements Initializable{

    private IItemAction itemAction;
    private Collections collections;

    private Map<Shop, String> shopStringMap = new HashMap<>();

    @FXML private VBox vBox;
    @FXML private VBox vBoxEdit;
    @FXML private TextField itemName;
    @FXML private ListView itemsListView;
    @FXML private Button addButton;
    @FXML private Button cancelButton;
    @FXML private Button newItemButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        collections = MainController.collections;
        itemAction = new ItemAction(collections);

        itemsListView.setItems(collections.getItemList());
        itemsListView.setCellFactory(itemsListController -> new ItemsListController(this));

        showEditDialog(false);
    }

    @FXML
    private void newItemAdd() {
        shopStringMap.clear();
        showEditDialog(true);
        buildFields();
        addButton.setText("Добавить");
        addButton.setOnAction(event -> addItem());

    }

    @FXML
    private void test() {

    }

    @FXML private void cancelAction() {
        showEditDialog(false);
    }

    private void addItem() {
        Map<Shop, String> map = new HashMap<>();
        map.putAll(shopStringMap);
        Item item = new Item(itemName.getText(), map);
        //collections.getItemList().add(item);
        itemAction.add(item);
        showEditDialog(false);
    }

    private void showEditDialog(boolean show) {
        addButton.setDisable(true);
        if (show) {
            newItemButton.setDisable(true);
            newItemButton.setVisible(false);
            vBoxEdit.setDisable(false);
            vBoxEdit.setVisible(true);
            itemName.textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue.length() > 3)
                    addButton.setDisable(false);
                else
                    addButton.setDisable(true);
            });
        } else {
            newItemButton.setDisable(false);
            newItemButton.setVisible(true);
            vBoxEdit.setDisable(true);
            vBoxEdit.setVisible(false);
        }
        vBox.getChildren().clear();
        itemName.clear();

    }


    public void delItem(Item item) {


        for (Group group : collections.getGroupList()) {
            ArrayList<Item> tempList = new ArrayList<>();
            tempList.addAll(group.getItemsInGroup());
            for (Item item1 : tempList)
                if (item1 == item1)
                    group.getItemsInGroup().remove(item);
        }
        itemAction.del(item);
        showEditDialog(false);
    }

    public void editItem(Item item) {
        showEditDialog(true);
        itemName.setText(item.getItemName());
        buildFields(item);
        addButton.setText("Сохранить");
        addButton.setOnAction(event -> saveItem(item));
    }

    private void saveItem(Item item) {
        item.setItemName(itemName.getText());
        item.getShopMap().clear();
        Map<Shop, String> map = new HashMap<Shop, String>();

        map.putAll(shopStringMap);
        item.setShopMap(map);
        showEditDialog(false);
        itemsListView.setItems(null);
        itemsListView.setItems(collections.getItemList());
    }

    private void buildFields() {

        if (collections.getShopList() != null) {

            for (Shop shop : collections.getShopList()) {

                Label label = new Label(shop.getShopName());
                TextField textField = new TextField();
                textField.setId(shop.getShopName());
                textField.setPromptText("Введите ссылку на товар");
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    shopStringMap.put(shop, textField.getText());
                });
                vBox.getChildren().addAll(label, textField);

            }
        }
        collections.getShopList().addListener((ListChangeListener<Shop>) c -> {
            String s = itemName.getText();
            showEditDialog(true);
            buildFields();
            itemName.setText(s);
        });
    }

    private void buildFields(Item item) {

        if (collections.getShopList() != null) {
            shopStringMap.clear();
            for (Shop shop : collections.getShopList()) {
                Label label = new Label(shop.getShopName());
                TextField textField = new TextField();
                textField.setId(shop.getShopName());
                textField.setPromptText("Введите ссылку на товар");
                for (Map.Entry entry : item.getShopMap().entrySet())
                {
                    if (entry.getKey() == shop)
                        textField.setText((String) entry.getValue());
                }

                shopStringMap.put(shop, textField.getText());

                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    shopStringMap.put(shop, textField.getText());

                });
                vBox.getChildren().addAll(label, textField);

            }
        }
        collections.getShopList().addListener((ListChangeListener<Shop>) c -> {
            showEditDialog(true);
            buildFields(item);
        });
    }

}
