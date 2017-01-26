package actions.impls;

import actions.IShopAction;
import controllers.MainController;
import objects.Collections;
import objects.Shop;

public class ShopAction implements IShopAction {

    private Collections collections;

    public ShopAction(Collections collections) {
        this.collections = collections;
    }

    public ShopAction() {
        collections = MainController.collections;
    }

    public void add(Shop shop) {
        collections.getShopList().add(shop);
    }

    public void del(Shop shop) {
        collections.getShopList().remove(shop);
    }

    public void edit(Shop shop) {

    }

    public Collections getCollections() {
        return collections;
    }
}
