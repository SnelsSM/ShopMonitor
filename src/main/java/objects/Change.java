package objects;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Change implements Serializable{
    private Shop shop;
    private Item item;
    private long time;
    private String value = "";
    private Map<Long, String> changesMap = new HashMap<>();

    public Change(Shop shop, Item item, String value) {
        this.shop = shop;
        this.item = item;
        setValue(value);
    }

    public Item getItem() {
        return item;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public long getTime() {
        return time;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        long timeNow = new Date().getTime();
        this.value = value;
        this.time = timeNow;

        changesMap.put(timeNow, value);
    }
}
