package objects;

import javafx.beans.property.SimpleBooleanProperty;
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
    private SimpleBooleanProperty loaded = new SimpleBooleanProperty();
    private SimpleIntegerProperty complete = new SimpleIntegerProperty();

    private SimpleBooleanProperty autoStart = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty autoSave = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty repeat = new SimpleBooleanProperty(false);
    private SimpleIntegerProperty repeatInterval = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty loadPause = new SimpleIntegerProperty(5);
    private SimpleIntegerProperty saveInterval = new SimpleIntegerProperty(0);

    private SimpleBooleanProperty completeTasks = new SimpleBooleanProperty(true);
    private SimpleIntegerProperty completeCount = new SimpleIntegerProperty(0);

    public synchronized void updateCompleteCount() {
        setCompleteCount(completeCount.getValue() + 1);
    }

    public int getCompleteCount() {
        return completeCount.get();
    }

    public SimpleIntegerProperty completeCountProperty() {
        return completeCount;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount.set(completeCount);
    }

    public boolean isCompleteTasks() {
        return completeTasks.get();
    }

    public SimpleBooleanProperty completeTasksProperty() {
        return completeTasks;
    }

    public void setCompleteTasks(boolean completeTasks) {
        this.completeTasks.set(completeTasks);
    }

    public boolean isAutoStart() {
        return autoStart.get();
    }

    public SimpleBooleanProperty autoStartProperty() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart.set(autoStart);
    }

    public boolean isAutoSave() {
        return autoSave.get();
    }

    public SimpleBooleanProperty autoSaveProperty() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave.set(autoSave);
    }

    public boolean isRepeat() {
        return repeat.get();
    }

    public SimpleBooleanProperty repeatProperty() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat.set(repeat);
    }

    public int getRepeatInterval() {
        return repeatInterval.get();
    }

    public SimpleIntegerProperty repeatIntervalProperty() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval.set(repeatInterval);
    }

    public int getLoadPause() {
        return loadPause.get();
    }

    public SimpleIntegerProperty loadPauseProperty() {
        return loadPause;
    }

    public void setLoadPause(int loadPause) {
        this.loadPause.set(loadPause);
    }

    public int getSaveInterval() {
        return saveInterval.get();
    }

    public SimpleIntegerProperty saveIntervalProperty() {
        return saveInterval;
    }

    public void setSaveInterval(int saveInterval) {
        this.saveInterval.set(saveInterval);
    }

    private int countAll = 0;

    public boolean isLoaded() {
        return loaded.get();
    }

    public SimpleBooleanProperty loadedProperty() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded.set(loaded);
    }

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
