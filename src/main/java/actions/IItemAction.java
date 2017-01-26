package actions;

import objects.Item;

public interface IItemAction {

    void add(Item item);
    void del(Item item);
    void edit(Item item);
}
