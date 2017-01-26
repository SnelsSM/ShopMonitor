package controllers;

import actions.IShopAction;
import actions.impls.ShopAction;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import objects.Collections;
import objects.Shop;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ShopsController implements Initializable {

    @FXML private HBox hBox;
    @FXML private VBox vBoxEdit;
    @FXML private ListView listShops;
    @FXML private TextField shopName;
    @FXML private TextField codeFrom;
    @FXML private TextField codeTo;
    @FXML private TextField textOrig;
    @FXML private TextField textRep;
    @FXML private TableView repTable;
    @FXML private Button addButton;
    @FXML private Button newShop;
    @FXML private Button repButton;
    @FXML private Label errorLabel;
    @FXML private TableColumn<ObservableMap.Entry<String, String>, String> columnOrig;
    @FXML private TableColumn<ObservableMap.Entry<String, String>, String> columnRep;

    private ObservableMap<String, String> replaces = FXCollections.observableHashMap();
    private Collections collections;
    private IShopAction shopAction;

    private ShopsController shopsController = this;

    public void initialize(URL location, ResourceBundle resources) {
        collections = MainController.collections;
        shopAction = new ShopAction(collections);

        listShops.setItems(collections.getShopList());
        listShops.setCellFactory(shopsListController -> new ShopsListController(shopsController));

        columnOrig.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey()));

        columnRep.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue()));


        for (Shop shop : collections.getShopList()) {
            if (shop.getShopName().equals(shopName.getText())) {
                errorLabel.setText("Это имя уже используется");
                addButton.setDisable(true);
            }
        }
        replaces.addListener((MapChangeListener) (change -> {
            repTable.setItems(FXCollections.observableArrayList(replaces.entrySet()));
        }));

        repTable.getColumns().setAll(columnOrig, columnRep);

        repTable.setRowFactory(param -> {
            final TableRow<Map.Entry> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem editMenuItem = new MenuItem("Изменить");
            final MenuItem removeMenuItem = new MenuItem("Удалить");

            editMenuItem.setOnAction(event -> {
                textOrig.setText((String) row.getItem().getKey());
                textRep.setText((String) row.getItem().getValue());
                repButton.setOnAction((ActionEvent repButtonEvent) -> {
                    repEdit((String)row.getItem().getKey());
                });
            });

            removeMenuItem.setOnAction(event -> replaces.remove(row.getItem().getKey()));

            contextMenu.getItems().addAll(editMenuItem, removeMenuItem);
            // Set context menu on row, but use a binding to make it only show for non-empty rows:
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );
            return row;
        });

    }

    @FXML private void newShopEdit() {
        clearForm();
        newShop.setDisable(true);
        newShop.setVisible(false);
        vBoxEdit.setVisible(true);
        addButton.setText("Добавить");
        addButton.setOnAction(event -> addShop());
        showEditDialog(true);

        shopName.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setText("");
            if (newValue.length() < 3)
                addButton.setDisable(true);
            else {
                addButton.setDisable(false);
            }

            for (Shop shop : collections.getShopList()) {
                if (shop.getShopName().equals(newValue)) {
                    errorLabel.setText("Это имя уже используется!");
                    addButton.setDisable(true);
                }
            }
        });

    }

    @FXML
    private void addReplace() {
        replaces.put(textOrig.getText(), textRep.getText());
        textOrig.clear();
        textRep.clear();
    }

    @FXML
    private void saveShop(Shop shop) {
        Map<String, String> replaceMap = new HashMap<String, String>();
        replaceMap.putAll(replaces);
        shop.setShopName(shopName.getText());
        shop.setCodeFrom(codeFrom.getText());
        shop.setCodeTo(codeTo.getText());
        shop.setReplaceMap(replaceMap);

        listShops.setItems(null);
        listShops.setItems(collections.getShopList());
        showEditDialog(false);
    }

    @FXML
    private void addShop() {
        Map<String, String> replaceMap = new HashMap<String, String>();
        replaceMap.putAll(replaces);
        Shop shop = new Shop(shopName.getText(), codeFrom.getText(), codeTo.getText(), replaceMap);
        shopAction.add(shop);
        clearForm();
        editShop(shop);
        showEditDialog(false);
    }

    public void editShop(Shop shop) {
        clearForm();
        addButton.setText("Изменить");
        addButton.setOnAction(event -> saveShop(shop));
        shopName.setText(shop.getShopName());
        codeFrom.setText(shop.getCodeFrom());
        codeTo.setText(shop.getCodeTo());
        replaces.putAll(shop.getReplaceMap());
        showEditDialog(true);
        errorLabel.setText("");
        shopName.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setText("");
            if (newValue.length() < 3)
                addButton.setDisable(true);
            else {
                addButton.setDisable(false);
            }

            for (Shop shop2 : collections.getShopList()) {
                if (!shop.getShopName().equals(newValue) && shop2.getShopName().equals(newValue)) {
                    errorLabel.setText("Это имя уже используется!");
                    addButton.setDisable(true);
                }
            }
        });

    }

    private void repEdit(String key) {
        replaces.remove(key);
        replaces.put(textOrig.getText(), textRep.getText());
        textOrig.clear();
        textRep.clear();
        repButton.setOnAction(event -> addReplace());
    }

    public void delShop(Shop shop) {
        showEditDialog(false);
        shopAction.del(shop);
        clearForm();
    }

    private void clearForm() {
        shopName.clear();
        errorLabel.setText("");
        codeFrom.clear();
        codeTo.clear();
        textOrig.clear();
        textRep.clear();
        replaces.clear();
    }


    private void showEditDialog(boolean show) {
        if (show) {
            newShop.setDisable(true);
            newShop.setVisible(false);
            vBoxEdit.setDisable(false);
            vBoxEdit.setVisible(true);
        } else {
            newShop.setDisable(false);
            newShop.setVisible(true);
            vBoxEdit.setDisable(true);
            vBoxEdit.setVisible(false);
        }
    }

}
