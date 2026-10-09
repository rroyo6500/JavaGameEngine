package rroyo.jge.core.gameobjects;

import java.util.ArrayList;
import java.util.List;

public class Group extends GameObject {

    private List<GameElement> members = new ArrayList<>();

    public Group(GameElement... member) {
        for (GameElement m : member) {
            if (!members.contains(m)) {
                members.add(m);
            }
        }
    }

    public boolean overlap(GameElement go) {
        if (isDeleted() || go.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.overlap(go)) {
                return true;
            }
        }
        return false;
    }

    public final boolean overlap(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.overlap(group)) {
                return true;
            }
        }
        return false;
    }

    public final boolean inRange(GameElement go) {
        if (isDeleted() || go.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.inRange(go)) {
                return true;
            }
        }
        return false;
    }

    public final boolean inRange(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.inRange(group)) {
                return true;
            }
        }
        return false;
    }

    public final List<GameElement> getMembers() {
        return List.copyOf(members);
    }

    @Override
    public void delete() {
        super.delete();
        members.clear();
    }
}
