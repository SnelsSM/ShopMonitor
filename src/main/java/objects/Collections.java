package objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Collections {
    private ObservableList<Shop> shopList = FXCollections.observableArrayList();
    private ObservableList<Item> itemList = FXCollections.observableArrayList();
    private ObservableList<Group> groupList = FXCollections.observableArrayList();
    private ObservableList<Change> changeList = FXCollections.observableArrayList();
    private SimpleStringProperty htmlPage = new SimpleStringProperty();
    private SimpleIntegerProperty complete = new SimpleIntegerProperty();
    private int countAll = 0;

    public int getCountAll() {
        return countAll;
    }

    public void setCountAll(int countAll) {
        this.countAll = countAll;
    }

    public synchronized int getComplete() {
        return complete.get();
    }

    public SimpleIntegerProperty completeProperty() {
        return complete;
    }

    public synchronized void setComplete(int complete) {
        this.complete.set(complete);
    }

    public synchronized void updateComplete() {
        setComplete(complete.getValue() + 1);
    }

    public ObservableList<Shop> getShopList() {
        return shopList;
    }

    public ObservableList<Item> getItemList() {
        return itemList;
    }

    public ObservableList<Group> getGroupList() {
        return groupList;
    }

    public ObservableList<Change> getChangeList() {
        return changeList;
    }

    public synchronized void addChange(Change change) {
        changeList.add(change);
    }

    public String getHtmlPage() {
        return htmlPage.get();
    }

    public SimpleStringProperty htmlPageProperty() {
        return htmlPage;
    }

    public void setHtmlPage(String htmlPage) {
        this.htmlPage.set(htmlPage);
    }
}
