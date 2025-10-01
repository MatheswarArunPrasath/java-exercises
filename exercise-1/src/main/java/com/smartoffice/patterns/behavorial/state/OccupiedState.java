package com.smartoffice.patterns.behavioral.state;

public class OccupiedState implements EnergyState {
    @Override
    public void peopleEnter(EnergyContext ctx, int count) {
        /* stay occupied */ }

    @Override
    public void peopleExit(EnergyContext ctx, int count) {
        if (ctx.headcount() == 0)
            ctx.setState(new UnoccupiedState());
        else if (ctx.headcount() < 2)
            ctx.setState(new StandbyState());
    }

    @Override
    public void idleTimeout(EnergyContext ctx) {
        ctx.setState(new StandbyState());
    }

    @Override
    public String name() {
        return "OCCUPIED (AC on, lights on)";
    }
}
