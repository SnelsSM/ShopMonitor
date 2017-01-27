package threads;

import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.*;


public class BuildTable extends Thread {

    private Collections collections = MainController.collections;
    private ObservableList<Item> itemList = FXCollections.observableArrayList();
    private StringBuilder htmlPage = new StringBuilder();
    private Group group;
    private int perCount = 0;

    public BuildTable() {
        itemList.addAll(collections.getItemList());
        this.start();
    }

    public void run() {
        perCount = 80/collections.getShopList().size();

        htmlPage.append("<table id=\"tableSearchResults\" class=\"table table-hover\">\n" +
                "<thead class=\"bg-primary\">\n" +
                "<th>Номенклатура</th>\n");


        for (Shop shop : collections.getShopList())
        {
            htmlPage.append("<th class=\"text-center\" width=\"" + perCount + "%\">" + shop.getShopName() + "</th>\n");
        }

        htmlPage.append("</thead>" +
                "<tbody>");

        int i = 0;
        if(collections.getGroupList().size() != 0) {
            for(Group group : collections.getGroupList()) {
                i += 1;
                this.group = group;
                if(group.getItemsInGroup().size() != 0)
                    addGroup(group.getGroupName(), i);
            }
        }

        if(itemList.size() != 0) {
            group = null;
            addGroup("Без группы", 0);
        }

        htmlPage.append("</tbody>\n" +
                "</table>\n");

        collections.setHtmlPage("");
        collections.setHtmlPage(htmlPage.toString());
    }

    private void addGroup(String name, int i) {
        htmlPage.append("<tr id=\"package1\" data-toggle=\"collapse\" data-parent=\"#OrderPackages\" data-target=\"#accordion" + i + "\">\n" +
                "<td colspan=\"" + (collections.getShopList().size() + 1) + "\"  class=\"bg-success\">" +name+ "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td colspan=\"" + (collections.getShopList().size() + 1) + "\" class=\"nopadding\">\n" +
                "<div class=\"collapse in\" id=\"accordion" + i + "\">\n" +
                "<table width=\"100%\" class=\"table table-hover table-striped nopadding text-muted\">\n");


        if (group != null) {
            itemList.removeAll(group.getItemsInGroup());
            for (Item item : group.getItemsInGroup()) {
                htmlPage.append("<tr>\n" +
                        "<td>" + item.getItemName() + "</td>\n");

                for (Shop shop : collections.getShopList()) {
                    boolean foo = false;
                    for (Change change : collections.getChangeList()) {
                        if ((item == change.getItem()) && (shop == change.getShop())) {
                            foo = true;
                            htmlPage.append("<td class=\"text-center\" width=\"" + perCount + "%\">" + change.getValue() + "</td>\n");
                        }
                    }
                    if (!foo)
                        htmlPage.append("<td class=\"text-center\" width=\"" + perCount + "%\">-</td>\n");
                }
                htmlPage.append("</tr>\n");

            }
        } else {
            for (Item item : itemList) {
                htmlPage.append("<tr>\n" +
                        "<td>" + item.getItemName() + "</td>\n");

                for (Shop shop : collections.getShopList()) {
                    boolean foo = false;
                    for (Change change : collections.getChangeList()) {
                        if ((item == change.getItem()) && (shop == change.getShop())) {
                            foo = true;
                            htmlPage.append("<td class=\"text-center\" width=\"" + perCount + "%\">" + change.getValue() + "</td>\n");
                        }
                    }
                    if (!foo)
                        htmlPage.append("<td class=\"text-center\" width=\"" + perCount + "%\">-</td>\n");
                }
                htmlPage.append("</tr>\n");

            }
        }

        htmlPage.append("</table>\n" +
                "</div>\n" +
                "</td>\n" +
                "</tr>\n");

    }
}
