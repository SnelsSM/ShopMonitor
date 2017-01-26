package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import objects.Group;

import java.io.IOException;

public class GroupsListController extends ListCell<Group>{

    private FXMLLoader loader;
    private Group group;
    private GroupsController groupsController;

    @FXML private AnchorPane anchorPane;
    @FXML private Button label;
    @FXML private Button del;

    public GroupsListController(GroupsController groupsController) {
        this.groupsController = groupsController;
    }

    @Override
    protected void updateItem(Group group, boolean empty) {
        this.group = group;
        super.updateItem(group, empty);

        if(empty || group == null) {

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

            label.setText(group.getGroupName());
            label.setOnAction(event -> groupsController.editGroup(group));
            del.setOnAction(event -> groupsController.delGroup(group));

            setText(null);
            setGraphic(anchorPane);
        }
    }
}
