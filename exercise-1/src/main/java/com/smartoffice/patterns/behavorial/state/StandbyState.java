package com.smartoffice.patterns.behavioral.state;

public class StandbyState implements EnergyState {
    @Override
    public void peopleEnter(EnergyContext ctx, int count) {
        if (ctx.headcount() >= 2)
            ctx.setState(new OccupiedState());
    }

    @Override
    public void peopleExit(EnergyContext ctx, int count) {
        if (ctx.headcount() == 0)
            ctx.setState(new UnoccupiedState());
    }

    @Override
    public void idleTimeout(EnergyContext ctx) {
        /* remain standby */ }

    @Override
    public String name() {
        return "STANDBY (dim lights, relaxed AC)";
    }
}
