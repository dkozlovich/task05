package by.epam.task05.state.impl;

import by.epam.task05.entity.Port;
import by.epam.task05.entity.Ship;
import by.epam.task05.exception.CustomException;
import by.epam.task05.state.ShipState;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadingUnloadingState implements ShipState {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void nextAction(Ship ship) throws CustomException {

        Port port = Port.getInstance();
        if (ship.isUnloaded()) {
            LOGGER.log(Level.INFO, "Ship " + ship.getShipId() + " is unloading.");
            port.unload(ship);
        }
        if (ship.isLoaded()) {
            LOGGER.log(Level.INFO, "Ship " + ship.getShipId() + " is loading.");
            port.load(ship);

        }
    }
}
