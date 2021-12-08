package by.epam.task05.state.impl;

import by.epam.task05.entity.Port;
import by.epam.task05.entity.Ship;
import by.epam.task05.exception.CustomException;
import by.epam.task05.state.ShipState;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EndState implements ShipState {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void nextAction(Ship ship) throws CustomException {
        LOGGER.log(Level.INFO, "Ship " + ship.getShipId() + " has completed work.");
        Port port = Port.getInstance();
        port.removeShip(ship);
    }
}

