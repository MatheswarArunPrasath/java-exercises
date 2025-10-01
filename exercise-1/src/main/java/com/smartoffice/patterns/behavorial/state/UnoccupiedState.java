package com.smartoffice.patterns.behavioral.state;

public class UnoccupiedState implements EnergyState {
    @Override
    public void peopleEnter(EnergyContext ctx, int count) {
        if (ctx.headcount() >= 2)
            ctx.setState(new OccupiedState());
    }

    @Override
    public void peopleExit(EnergyContext ctx, int count) {
        /* no-op */ }

    @Override
    public void idleTimeout(EnergyContext ctx) {
        /* already minimal */ }

    @Override
    public String name() {
        return "UNOCCUPIED (AC off, lights off)";
    }
}
