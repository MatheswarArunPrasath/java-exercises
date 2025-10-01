package com.smartoffice.patterns.structural.adapter;

import com.smartoffice.patterns.shared.Sensor;

/** Adapts LegacyTurnstile to the demo's Sensor interface. */
public class TurnstileAdapter implements Sensor {
    private final LegacyTurnstile legacy;

    public TurnstileAdapter(LegacyTurnstile legacy) {
        this.legacy = legacy;
    }

    @Override
    public void registerDelta(int delta) {
        if (delta > 0)
            legacy.swipeIn(delta);
        else if (delta < 0)
            legacy.swipeOut(-delta);
    }

    @Override
    public int currentCount() {
        return legacy.getTotalInside();
    }
}
