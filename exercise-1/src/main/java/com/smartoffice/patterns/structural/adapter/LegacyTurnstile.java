package com.smartoffice.patterns.structural.adapter;

/** A legacy class we cannot change. */
public class LegacyTurnstile {
    private int inside = 0;

    public void swipeIn(int count) {
        inside += Math.max(0, count);
    }

    public void swipeOut(int count) {
        inside = Math.max(0, inside - Math.max(0, count));
    }

    public int getTotalInside() {
        return inside;
    }
}
