package rroyo.jgameengine.core.gameobjects;

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

    public boolean overlap(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.overlap(group)) {
                return true;
            }
        }
        return false;
    }

    public boolean inRange(GameElement go) {
        if (isDeleted() || go.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.inRange(go)) {
                return true;
            }
        }
        return false;
    }

    public boolean inRange(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.inRange(group)) {
                return true;
            }
        }
        return false;
    }

    public List<GameElement> getMembers() {
        if (isDeleted()) return null;
        return List.copyOf(members);
    }

    @Override
    public void delete() {
        super.delete();
        members.clear();
        members = null;
    }
}
