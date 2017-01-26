package threads;

import actions.IGroupAction;
import actions.IItemAction;
import actions.IShopAction;
import actions.impls.GroupAction;
import actions.impls.ItemAction;
import actions.impls.ShopAction;
import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SQLThread extends Thread {

    private String action;
    private Statement statement;
    private Connection connect;
    private ResultSet resultSet;
    private Collections collections = MainController.collections;
    private IShopAction shopAction = new ShopAction();
    private IItemAction itemAction = new ItemAction();
    private IGroupAction groupAction = new GroupAction();

    public SQLThread(String action) {
        this.action = action;
        this.start();
    }

    @Override
    public void run() {
        connectDB();


        try {


            if (action.equals("write")) {

                statement.execute("drop table `shops`");
                statement.execute("drop table `replaces`");
                statement.execute("drop table `items`");
                statement.execute("drop table `groups`");

                createDB();
                Map<Integer, Shop> shopMap = new HashMap<Integer, Shop>();

                int shopCount = 0;
                for (Shop shop : collections.getShopList()) {
                    shopCount += 1;
                    shopMap.put(shopCount, shop);
                    statement.execute("INSERT INTO 'shops' " +
                            "('id','shopName', 'codeFrom', 'codeTo') VALUES ('" + shopCount + "','" + shop.getShopName() + "','" + shop.getCodeFrom() + "','" + shop.getCodeTo() + "');");

                    for (Map.Entry entry : shop.getReplaceMap().entrySet()) {
                        statement.execute("INSERT INTO 'replaces' " +
                                "('shopId','textOrig', 'textReplace') VALUES ('" + shopCount + "','" + entry.getKey() + "','" + entry.getValue() + "');");
                    }
                }

                Map<Integer, Item> itemMap = new HashMap<>();

                int itemCount = 0;
                for (Item item : collections.getItemList()) {
                    itemCount += 1;
                    itemMap.put(itemCount, item);
                    StringBuilder shopLinks = new StringBuilder();

                    for (Map.Entry entry : item.getShopMap().entrySet()) {
                        for (Map.Entry entry1 : shopMap.entrySet()) {
                            if (entry1.getValue() == entry.getKey())
                                shopLinks.append(entry1.getKey() + ":" + entry.getValue() + ";");
                        }
                    }

                    statement.execute("INSERT INTO 'items' " +
                            "('id','itemName', 'shopLinks') VALUES ('" + itemCount + "','" + item.getItemName() + "','" + shopLinks + "');");
                }

                Map<Integer, Group> groupMap = new HashMap<>();

                int groupCount = 0;
                for (Group group : collections.getGroupList()) {
                    groupCount += 1;
                    groupMap.put(groupCount, group);

                    StringBuilder items = new StringBuilder();

                    for (Item item : group.getItemsInGroup()) {

                        for (Map.Entry entry : itemMap.entrySet())
                            if (entry.getValue() == item)
                                items.append(entry.getKey() + ";");

                        statement.execute("INSERT INTO 'groups' " +
                                "('id','groupName', 'items') VALUES ('" + groupCount + "','" + group.getGroupName() + "','" + items + "');");
                    }
                }

            }

            if (action.equals("read")) {
                createDB();
                Map<Integer, Shop> shopMap = new HashMap<Integer, Shop>();
                Map<Integer, Item> itemMap = new HashMap<Integer, Item>();
                ArrayList<String> replaces = new ArrayList<>();
                Map<Integer, Group> groupMap = new HashMap<>();
                resultSet = statement.executeQuery("SELECT * FROM replaces;");
                while (resultSet.next()) {
                    int shopId = resultSet.getInt("shopId");
                    replaces.add(shopId + ";" + resultSet.getString("textOrig") + ";" + resultSet.getString("textReplace"));
                }

                resultSet = statement.executeQuery("SELECT * FROM shops;");
                while (resultSet.next()) {
                    Map<String, String> replacesMap = new HashMap<>();
                    int id = resultSet.getInt("id");
                    for (String s : replaces) {
                        int shopId = Integer.parseInt(s.split(";")[0]);
                        if (shopId == id)
                            replacesMap.put(s.split(";")[1], s.split(";")[2]);
                    }
                    String shopName = resultSet.getString("shopName");
                    String codeFrom = resultSet.getString("codeFrom");
                    String codeTo = resultSet.getString("codeTo");
                    Shop shop = new Shop(shopName, codeFrom, codeTo, replacesMap);
                    shopMap.put(id, shop);
                    shopAction.add(shop);
                }

                resultSet = statement.executeQuery("SELECT * FROM items;");
                while (resultSet.next()) {
                    Map<Shop, String> linksMap = new HashMap<>();
                    int itemId = resultSet.getInt("id");
                    String itemName = resultSet.getString("itemName");
                    String[] links = resultSet.getString("shopLinks").split(";");
                    for (String s : links) {
                        int shopId = Integer.parseInt(s.split(":")[0]);
                        String link = s.split(":")[1];
                        for (Map.Entry entry : shopMap.entrySet())
                            if ((int)entry.getKey() == shopId)
                                linksMap.put((Shop)entry.getValue(), link);
                    }
                    Item item = new Item(itemName, linksMap);
                    itemMap.put(itemId, item);
                    itemAction.add(item);
                }

                resultSet = statement.executeQuery("SELECT * FROM groups;");
                while (resultSet.next()) {
                    ObservableList<Item> itemsList = FXCollections.observableArrayList();
                    int groupId = resultSet.getInt("id");
                    String groupName = resultSet.getString("groupName");
                    String[] items = resultSet.getString("items").split(";");

                    for (String s : items) {
                        int itemId = Integer.parseInt(s);
                        for (Map.Entry entry : itemMap.entrySet()) {
                            if ((int)entry.getKey() == itemId)
                                itemsList.add((Item)entry.getValue());
                        }
                    }

                    Group group = new Group(groupName, itemsList);
                    groupMap.put(groupId, group);
                    groupAction.add(group);
                }

                collections.setLoaded(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        closeDB();
    }


    public void connectDB(){
        connect = null;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connect = DriverManager.getConnection("jdbc:sqlite:ShopMonitorDB.s3db");
            statement = connect.createStatement();
            System.out.println("База подключена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDB() throws SQLException{
            statement = connect.createStatement();
            statement.execute("CREATE TABLE if not exists 'shops' " +
                    "('id' INTEGER PRIMARY KEY, 'shopName' text, 'codeFrom' text, 'codeTo' text);");

        statement.execute("CREATE TABLE if not exists 'replaces' " +
                "('shopId' INTEGER, 'textOrig' text, 'textReplace' text);");

            statement.execute("CREATE TABLE if not exists 'items' " +
                    "('id' INTEGER PRIMARY KEY, 'itemName' text, 'shopLinks' text);");

            statement.execute("CREATE TABLE if not exists 'groups' " +
                "('id' INTEGER PRIMARY KEY, 'groupName' text, 'items' text);");

            statement.execute("CREATE TABLE if not exists 'other' " +
                    "('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'webPage' text);");

    }

    private void closeDB() {
        try {
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
