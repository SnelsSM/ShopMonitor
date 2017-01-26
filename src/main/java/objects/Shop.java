package objects;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Shop implements Serializable{

    private String shopName;
    private String codeFrom;
    private String codeTo;
    private Map<String, String> replaceMap = new HashMap<String, String>();

    public Shop(String shopName, String codeFrom, String codeTo, Map<String, String> replaceMap) {
        this.shopName = shopName;
        this.codeFrom = codeFrom;
        this.codeTo = codeTo;
        this.replaceMap = replaceMap;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCodeFrom() {
        return codeFrom;
    }

    public void setCodeFrom(String codeFrom) {
        this.codeFrom = codeFrom;
    }

    public String getCodeTo() {
        return codeTo;
    }

    public void setCodeTo(String codeTo) {
        this.codeTo = codeTo;
    }

    public Map<String, String> getReplaceMap() {
        return replaceMap;
    }

    public void setReplaceMap(Map<String, String> replaceMap) {
        this.replaceMap = replaceMap;
    }

}
