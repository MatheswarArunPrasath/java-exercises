package com.smartoffice.patterns.behavioral.state;

public interface EnergyState {
    void peopleEnter(EnergyContext ctx, int count);

    void peopleExit(EnergyContext ctx, int count);

    void idleTimeout(EnergyContext ctx);

    String name();
}
