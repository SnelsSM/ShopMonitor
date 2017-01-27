package controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import objects.Collections;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemsTableController implements Initializable {
    @FXML
    private WebView webView;
    private boolean loaded = false;
    private boolean start = false;


    private Collections collections = MainController.collections;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadPage();



        collections.htmlPageProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (loaded && collections.isLoaded()) {
                        webView.getEngine().executeScript("remove()");
                        webView.getEngine().executeScript("add('" + collections.getHtmlPage().replace("\n", "") + "')");
                    }
                }
            });
        });
    }

    private void loadPage() {
        URL url = getClass().getResource("/Table.html");
        webView.getEngine().load(url.toExternalForm());

        loaded = false;
        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != Worker.State.SUCCEEDED) {return;}
            loaded = true;
            webView.getEngine().executeScript("add('" + collections.getHtmlPage().replace("\n", "") + "')");
        });
    }

}
