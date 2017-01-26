package objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable{

    private String groupName;
    private ObservableList<Item> itemsInGroup = FXCollections.observableArrayList();

    public Group(String groupName,ObservableList<Item> itemsInGroup) {
        this.groupName = groupName;
        this.itemsInGroup = itemsInGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ObservableList<Item> getItemsInGroup() {
        return itemsInGroup;
    }

    public void setItemsInGroup(ObservableList<Item> itemsInGroup) {
        this.itemsInGroup = itemsInGroup;
    }
}
