package com.lifegoals.app.feed.other;

/**
 * Created by Paul on 5/1/2015.
 */
public class SelectingPostState {
    private boolean selecting;

    /* we are going to send this object from class to class to update the view */
    public SelectingPostState(boolean selecting) {
        this.selecting = selecting;
    }

    public boolean isSelecting() {
        return selecting;
    }

    public void setSelecting(boolean selecting) {
        this.selecting = selecting;
    }
}
