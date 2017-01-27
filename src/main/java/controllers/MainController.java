package controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objects.Collections;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.StatusBar;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import threads.BuildTable;
import threads.SaveThread;
import threads.ShopsThread;
import threads.SQLThread;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


public class MainController implements Initializable {
    @FXML private AnchorPane anchorPane;
    @FXML private StatusBar statusBar;
    @FXML private Label errorLabel;
    @FXML private SegmentedButton segmentedButton;
    @FXML private Button settingsButton;
    @FXML private Button startButton;
    @FXML private Button saveButton;
    @FXML private Button excelButton;

    private Stage dialog;

    private FXMLLoader fxmlLoader = new FXMLLoader();
    private Parent fxmlEdit;
    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    public static Collections collections;
    private boolean starts = false;
    private MainController mainController = this;

    private Map<String, Parent> parentMap = new HashMap<String, Parent>();
    private ArrayList<String> fxmlList = new ArrayList<String>();

    private double perCount;

    public void initialize(URL location, ResourceBundle resources) {

        collections = new Collections();

        try {
            fxmlLoader.setLocation(getClass().getResource("/views/SettingsWindow.fxml"));
            fxmlEdit = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ToggleButton b1 = new ToggleButton("Таблица");
        ToggleButton b2 = new ToggleButton("Магазины");
        ToggleButton b3 = new ToggleButton("Номенклатура");
        ToggleButton b4 = new ToggleButton("Группы товаров");

        b1.setId("ItemsTable");
        b1.setOnAction(event -> openTab("ItemsTable"));

        b2.setId("Shops");
        b2.setOnAction(event -> openTab("Shops"));

        b3.setId("Items");
        b3.setOnAction(event -> openTab("Items"));

        b4.setId("Groups");
        b4.setOnAction(event -> openTab("Groups"));

        segmentedButton.getButtons().addAll(b1, b2, b3, b4);

        fxmlList.add("ItemsTable");
        fxmlList.add("Shops");
        fxmlList.add("Groups");
        fxmlList.add("Items");
        for (String s : fxmlList) {
            parentMap.put(s, initPages(s));
        }

        openTab("ItemsTable");
        b1.setSelected(true);

        settingsButton.setGraphic(fontAwesome.create("GEAR"));
        startButton.setGraphic(fontAwesome.create("PLAY"));
        saveButton.setGraphic(fontAwesome.create("FLOPPY_ALT"));
        excelButton.setGraphic(fontAwesome.create("FILE_EXCEL_ALT"));

        collections.completeTasksProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                Platform.runLater(() -> {
                    startButton.setGraphic(fontAwesome.create("PLAY"));
                });

            else
                Platform.runLater(() -> {
                    startButton.setGraphic(fontAwesome.create("STOP"));
                });

        });

        collections.completeProperty().addListener((observable, oldValue, newValue) -> {

            int countAll = collections.getCountAll();

            perCount = newValue.doubleValue()/((double) countAll);
            if (perCount >= 1.0) {
                perCount = 0.0;
                Date dNow = new Date();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                Platform.runLater(() -> {
                    statusBar.setText("Обновлено в " + df.format(dNow));
                    new BuildTable();
                });
                collections.setComplete(0);
            }
            Platform.runLater(() -> {
                statusBar.setProgress(perCount);
            });

        });

        new SQLThread("read");


        collections.loadedProperty().addListener((observable, oldValue, newValue) -> {
            if (!starts && newValue) {
                starts = true;
                if (collections.isAutoStart())
                    new ShopsThread(mainController, false);

                if (collections.isAutoSave())
                    new SaveThread();
            }

        });

        collections.autoSaveProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                new SaveThread();
        });
    }

    @FXML
    private void settingsAction () {
        if (dialog == null) {
            dialog = new Stage();
            //dialog.getIcons().add(new Image(getClass().getResourceAsStream("/WCD.png")));
            dialog.setTitle("Настройки");
            dialog.setResizable(false);
            dialog.setScene(new Scene(fxmlEdit));
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(anchorPane.getScene().getWindow());
        }
        dialog.show();
    }

    @FXML
    private void openTab(String page) {

        for (Map.Entry entry : parentMap.entrySet()) {
            if (entry.getKey().equals(page))
                openNewWindow((Parent)entry.getValue());
        }

    }

    private Parent initPages(String FXMLFile) {
        Parent page = null;
        try {
            URL url = getClass().getResource("/views/" + FXMLFile + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(url);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            page = (Parent) fxmlLoader.load(url.openStream());

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }


    public void openNewWindow(Parent page)
    {
        anchorPane.getChildren().clear();///name of pane where you want to put the fxml.
        anchorPane.getChildren().add(page);
        AnchorPane.setLeftAnchor(page, 0.0);
        AnchorPane.setRightAnchor(page, 0.0);
        AnchorPane.setTopAnchor(page, 0.0);
        AnchorPane.setBottomAnchor(page, 0.0);
    }



    @FXML
    private void startAction() {
             new ShopsThread(mainController, true);
    }

    @FXML
    private void saveAction() {
        new SQLThread("write");
    }

    @FXML
    private void startTask() {

    }

    public void setErrorLabel(String error) {

        Platform.runLater(() -> {
            statusBar.setText(error);
            statusBar.setProgress(0.0);
        });
    }
}
