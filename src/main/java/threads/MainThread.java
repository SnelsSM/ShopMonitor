package threads;

import controllers.MainController;
import objects.Collections;
import objects.Item;
import objects.Shop;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MainThread extends Thread {

    private Collections collections = MainController.collections;
    private MainController mainController;
    private boolean fail = false;

    public MainThread(MainController mainController) {

        this.mainController = mainController;
        this.start();
    }

    public void run() {

        collections.setComplete(0);
        collections.setCountAll(0);

        int count = 0;
        for (Item item : collections.getItemList()) {
            for (Map.Entry entry : item.getShopMap().entrySet()) {
                if (!entry.getValue().equals(""))
                    count += 1;
            }
        }

        collections.setCountAll(count);
        if (collections.getCountAll() == 0) {
            mainController.setErrorLabel("Нечего сканировать");
            fail = true;
        }


        if (!fail)
        if (checkInternetConnection())
            for (Shop shop : collections.getShopList())
                new StartParsing(shop);
        else mainController.setErrorLabel("Проверьте соединение");
        }


    private boolean checkInternetConnection() {
        Boolean result = false;
        HttpURLConnection con = null;
        while (!result) {
            try {
                con = (HttpURLConnection) new URL("http://google.com").openConnection();
                con.setRequestMethod("HEAD");
                result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    try {
                        con.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!result) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
