package threads;

import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.Change;
import objects.Collections;
import objects.Item;
import objects.Shop;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartParsing extends Thread{
    private Collections collections = MainController.collections;
    private Shop shop;
    private Map<Item, String> itemLinksMap = new HashMap<>();
    private ObservableList<Change> items = FXCollections.observableArrayList();


    public StartParsing(Shop shop) {
        this.shop = shop;

        this.start();
    }

    public void run() {

        ArrayList<Item> items = new ArrayList<>();
        items.addAll(collections.getItemList());

        for (Item item : items)
        {

            for (Map.Entry entry : item.getShopMap().entrySet()) {
                if ((Shop)entry.getKey() == this.shop) {
                    itemLinksMap.put(item, (String) entry.getValue());
                }
            }
        }


            for (Map.Entry entry : itemLinksMap.entrySet()) {

                Item item = (Item) entry.getKey();

                Element body = getElement((String) entry.getValue());

                if (body != null) {

                    String stringBody = replaces(body.toString());
                    String from = replaces(shop.getCodeFrom());
                    String to = replaces(shop.getCodeTo());

                    Pattern pattern = Pattern.compile(from + "([^&]*)" + to);
                    Matcher m = pattern.matcher(stringBody);
                    String result = "";


                    while (m.find()) {
                        result += m.group();
                        result = result.replace(from, "");
                        result = result.replace(to, "");

                        ObservableList<Change> temp = FXCollections.observableArrayList();
                        temp.addAll(collections.getChangeList());

                        if (collections.getChangeList().size() != 0) {
                            boolean boo = false;
                            for (Change change : temp) {

                                if ((shop == change.getShop()) && (item == change.getItem())) {
                                    boo = true;
                                    if (!change.getValue().equals(result))
                                        change.setValue(result);
                                }
                            }
                            if (!boo) collections.addChange(new Change(shop, item, result));

                        } else collections.addChange(new Change(shop, item, result));

                        break;
                    }

                    try {
                        Thread.sleep(collections.getLoadPause()*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                collections.updateComplete();
                collections.updateCompleteCount();
            }

    }

    private String replaces(String string) {
        string = string.replace(" ", "");
        string = string.replace("\n", "");
        string = string.replace("/", "");
        string = string.replace("\"", "");
        string = string.replace("'", "");

        return string;
    }

    private Element getElement(String url) {
        Document document = null;
        Element body;
        try {
            document = Jsoup.connect(url).userAgent("Mozilla").get();
        } catch (IOException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }

        try {
            body = document.body();
        } catch (NullPointerException e) {
            return null;
        }
        return body;
    }

}
