package controllers;

import actions.IGroupAction;
import actions.impls.GroupAction;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import objects.Collections;
import objects.Group;
import objects.Item;
import org.controlsfx.control.ListSelectionView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GroupsController implements Initializable{

    private Collections collections;
    private IGroupAction groupAction;
    private ObservableList<Item> itemsSourceList = FXCollections.observableArrayList();
    private ObservableList<Item> itemsTargetList = FXCollections.observableArrayList();

    @FXML private ListView groupsListView;
    @FXML private Button addActionButton;
    @FXML private Button addButton;
    @FXML private VBox vBox;
    @FXML private TextField groupName;
    @FXML private Label errorLabel;
    @FXML private ListSelectionView listSelectionView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collections = MainController.collections;
        groupAction = new GroupAction(collections);
        groupsListView.setItems(collections.getGroupList());
        groupsListView.setCellFactory(groupsListController -> new GroupsListController(this));
        showEditDialog(false);
        itemsSourceList.addAll(collections.getItemList());

        listSelectionView.setSourceItems(itemsSourceList);
        listSelectionView.setTargetItems(itemsTargetList);


        listSelectionView.setCellFactory(param -> new GroupsSelectController());

        collections.getItemList().addListener((ListChangeListener<Item>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    for (Group group : collections.getGroupList()) group.getItemsInGroup().removeAll(c.getRemoved());
                    itemsSourceList.removeAll(c.getRemoved());
                }
                if (c.wasAdded()) itemsSourceList.addAll(c.getAddedSubList());
            }
        });
    }

    @FXML
    private void addAction() {
        showEditDialog(true);
        addButton.setOnAction(event -> addGroup());
        addButton.setText("Добавить");
        groupName.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setText("");
            if (newValue.length() < 3)
                addButton.setDisable(true);
            else addButton.setDisable(false);

            for (Group group2 : collections.getGroupList())
                if (group2.getGroupName().equals(newValue)) {
                    errorLabel.setText("Это имя уже используется!");
                    addButton.setDisable(true);
                }
        });
        fillSelection();
    }

    private void addGroup() {
        ObservableList<Item> list = FXCollections.observableArrayList();
        list.addAll(listSelectionView.getTargetItems());
        groupAction.add(new Group(groupName.getText(), list));
        showEditDialog(false);
    }


    public void editGroup(Group group) {
        showEditDialog(true);
        groupName.setText(group.getGroupName());
        addButton.setOnAction(event -> saveGroup(group));
        addButton.setText("Сохранить");
        fillSelection(group);
        errorLabel.setText("");

        groupName.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setText("");
            if (newValue.length() < 3)
                addButton.setDisable(true);
            else addButton.setDisable(false);

            for (Group group2 : collections.getGroupList()) {
                if (!group.getGroupName().equals(newValue) && (group2.getGroupName().equals(newValue))) {
                    errorLabel.setText("Это имя уже используется!");
                    addButton.setDisable(true);
                }

            }
        });
    }

    private void saveGroup(Group group) {
        ObservableList<Item> arrayList = FXCollections.observableArrayList();
        arrayList.addAll(listSelectionView.getTargetItems());
        group.setGroupName(groupName.getText());
        group.setItemsInGroup(arrayList);
        showEditDialog(false);
        groupsListView.setItems(null);
        groupsListView.setItems(collections.getGroupList());
    }

    public void delGroup(Group group) {
        groupAction.del(group);
        itemsSourceList.addAll(group.getItemsInGroup());
        showEditDialog(false);
    }

    private void showEditDialog(boolean show) {
        groupName.clear();
        addButton.setDisable(true);

        if (show) {
            vBox.setDisable(false);
            vBox.setVisible(true);
            addActionButton.setDisable(true);
            addActionButton.setVisible(false);
        } else {
            vBox.setDisable(true);
            vBox.setVisible(false);
            addActionButton.setDisable(false);
            addActionButton.setVisible(true);
        }
    }

    @FXML
    private void cancelAction() {
        showEditDialog(false);
    }

    private void fillSelection() {
        itemsTargetList.clear();
    }

    private void fillSelection(Group group) {
        itemsTargetList.clear();
        itemsTargetList.addAll(group.getItemsInGroup());
    }
}