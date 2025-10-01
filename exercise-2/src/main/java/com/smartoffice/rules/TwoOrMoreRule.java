package com.smartoffice.rules;

public class TwoOrMoreRule implements OccupancyRule {
    @Override
    public boolean isOccupied(int headcount) {
        return headcount >= 2;
    }
}
