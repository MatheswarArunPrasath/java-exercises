package com.smartoffice.patterns.creational.abstractfactory;

import com.smartoffice.patterns.shared.*;

public class EcoFactory implements ControllerFactory {
    @Override
    public ACControllerLite createAC(RoomLite room) {
        // Energy-saving: warmer targets (20..28°C)
        return new ACControllerLite(room, new LinearTemperaturePolicyLite(20, 28));
    }

    @Override
    public LightControllerLite createLight(RoomLite room) {
        return new LightControllerLite(room);
    }
}
