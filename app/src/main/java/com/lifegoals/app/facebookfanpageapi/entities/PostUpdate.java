package com.lifegoals.app.facebookfanpageapi.entities;

/**
 * Created by Paul on 4/12/2015.
 */
public class PostUpdate {

    /* We are sending updates through Bus Event library with this entity, whenever a new Post is added / removed */
    private Type type;
    private int index;


    public PostUpdate(Type type, int index) {
        this.type = type;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static enum Type {
        ADDED, REMOVED
    }
}
