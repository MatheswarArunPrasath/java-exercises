package com.smartoffice.patterns.creational.abstractfactory;

import com.smartoffice.patterns.shared.ACControllerLite;
import com.smartoffice.patterns.shared.LightControllerLite;
import com.smartoffice.patterns.shared.RoomLite;

public interface ControllerFactory {
    ACControllerLite createAC(RoomLite room);

    LightControllerLite createLight(RoomLite room);
}
