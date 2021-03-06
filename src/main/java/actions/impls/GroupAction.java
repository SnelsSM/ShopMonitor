package actions.impls;

import actions.IGroupAction;
import controllers.MainController;
import objects.Collections;
import objects.Group;

public class GroupAction implements IGroupAction {

    private Collections collections;

    public  GroupAction() {
        this.collections = MainController.collections;
    }

    public GroupAction(Collections collections) {
        this.collections = collections;
    }

    public void add(Group group) {
        collections.getGroupList().add(group);
    }

    public void del(Group group) {
        collections.getGroupList().remove(group);
    }
}
