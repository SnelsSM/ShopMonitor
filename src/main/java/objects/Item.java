package objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Item implements Serializable{
    private String itemName;
    private Map<Shop, String> shopMap = new HashMap<>();

    public Item(String itemName, Map<Shop, String> shopMap) {
        this.itemName = itemName;
        this.shopMap = shopMap;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Map<Shop, String> getShopMap() {
        return shopMap;
    }

    public void setShopMap(Map<Shop, String> shopMap) {
        this.shopMap = shopMap;
    }

}
