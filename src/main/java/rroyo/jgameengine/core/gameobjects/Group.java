package rroyo.jgameengine.core.gameobjects;

import java.util.ArrayList;
import java.util.List;

public class Group extends GameObject {

    private final List<GameElement> members = new ArrayList<>();

    public Group(GameElement... member) {
        for (GameElement m : member) {
            if (!members.contains(m)) {
                members.add(m);
            }
        }
    }

    public boolean overlap(GameElement go) {
        for (GameElement m : members) {
            if (m.overlap(go)) {
                return true;
            }
        }
        return false;
    }

    public boolean overlap(Group group) {
        for (GameElement m : members) {
            if (m.overlap(group)) {
                return true;
            }
        }
        return false;
    }

    public boolean inRange(GameElement go) {
        for (GameElement m : members) {
            if (m.inRange(go)) {
                return true;
            }
        }
        return false;
    }

    public boolean inRange(Group group) {
        for (GameElement m : members) {
            if (m.inRange(group)) {
                return true;
            }
        }
        return false;
    }

    public List<GameElement> getMembers() {
        return List.copyOf(members);
    }

}
