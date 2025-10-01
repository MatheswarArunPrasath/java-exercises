package com.smartoffice.patterns.creational.abstractfactory;

import com.smartoffice.patterns.shared.*;

public class PerformanceFactory implements ControllerFactory {
    @Override
    public ACControllerLite createAC(RoomLite room) {
        // Aggressive cooling: 16..24°C
        return new ACControllerLite(room, new LinearTemperaturePolicyLite(16, 24));
    }

    @Override
    public LightControllerLite createLight(RoomLite room) {
        return new LightControllerLite(room);
    }
}
