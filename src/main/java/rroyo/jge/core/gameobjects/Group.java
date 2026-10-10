package rroyo.jge.core.gameobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Group} class represents a collection of {@link GameElement} objects.
 * It extends {@link GameObject} and is used to manage multiple elements as a single unit,
 * allowing bulk operations like checking collisions or overlaps between an entity and an entire group.
 */
public class Group extends GameObject {

    /**
     * The internal list maintaining the members of this group.
     */
    private List<GameElement> members = new ArrayList<>();

    /**
     * Constructs a new {@code Group} initialized with the provided members.
     * It ensures no duplicate members are added.
     *
     * @param member A variable number of {@link GameElement} objects to add to the group.
     */
    public Group(GameElement... member) {
        for (GameElement m : member) {
            if (!members.contains(m)) {
                members.add(m);
            }
        }
    }

    /**
     * Performs a physical collision check between a given game element and all members of this group.
     *
     * @param ge The {@link GameElement} to check against the group.
     * @return {@code true} if a collision happened with at least one member; {@code false} otherwise.
     */
    public boolean collide(GameElement ge) {
        if (isDeleted() || ge.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.collide(ge)) return true;
        }
        return false;
    }

    /**
     * Performs a physical collision check between all members of this group and all members of another group.
     *
     * @param group The other {@link Group} to check against.
     * @return {@code true} if any collision happened; {@code false} otherwise.
     */
    public boolean collide(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.collide(group)) return true;
        }
        return false;
    }

    /**
     * Performs an overlap check between a given game element and all members of this group.
     *
     * @param ge The {@link GameElement} to check.
     * @return {@code true} if an overlap occurred with at least one member; {@code false} otherwise.
     */
    public boolean overlap(GameElement ge) {
        if (isDeleted() || ge.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.overlap(ge)) return true;
        }
        return false;
    }

    /**
     * Performs an overlap check between all members of this group and all members of another group.
     *
     * @param group The other {@link Group} to check against.
     * @return {@code true} if any overlap occurred; {@code false} otherwise.
     */
    public final boolean overlap(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.overlap(group)) return true;
        }
        return false;
    }

    /**
     * Performs a proximity (in-range) check between a given game element and all members of this group.
     *
     * @param ge The {@link GameElement} to check.
     * @return {@code true} if the element is in range of at least one member; {@code false} otherwise.
     */
    public final boolean inRange(GameElement ge) {
        if (isDeleted() || ge.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.inRange(ge)) return true;
        }
        return false;
    }

    /**
     * Performs a proximity (in-range) check between all members of this group and all members of another group.
     *
     * @param group The other {@link Group} to check against.
     * @return {@code true} if any member is in range of a member from the other group; {@code false} otherwise.
     */
    public final boolean inRange(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement m : members) {
            if (m.inRange(group)) return true;
        }
        return false;
    }

    /**
     * Retrieves an immutable copy of the list of game elements belonging to this group.
     *
     * @return A {@link List} containing the {@link GameElement} members.
     */
    public final List<GameElement> getMembers() {
        return List.copyOf(members);
    }

    /**
     * Flags this group for deletion and clears all its members.
     * Overrides the parent method to ensure the member list is properly released.
     */
    @Override
    public void delete() {
        super.delete();
        members.clear();
    }
}
