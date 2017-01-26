package actions.impls;

import actions.IItemAction;
import controllers.MainController;
import objects.Collections;
import objects.Item;

public class ItemAction implements IItemAction{

    private Collections collections;

    public ItemAction() {
        this.collections = MainController.collections;
    }

    public ItemAction(Collections collections) {
        this.collections = collections;
    }

    @Override
    public void add(Item item) {
        collections.getItemList().add(item);
    }

    @Override
    public void del(Item item) {
        collections.getItemList().remove(item);
    }

    @Override
    public void edit(Item item) {

    }

    public Collections getCollections() {
        return collections;
    }
}
