package by.epam.task05.state.impl;

import by.epam.task05.entity.Ship;
import by.epam.task05.state.ShipState;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaitingState implements ShipState {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void nextAction(Ship ship) {
        StringBuilder builder = new StringBuilder();
        builder.append("Ship " + ship.getShipId() + " with " + ship.getContainerNumber() + " containers is in queue.");
        if (ship.isLoaded()) {
            if (ship.isUnloaded()) {
                builder.append(" Ship status is - unloading and loading required.");
            } else {
                builder.append(" Ship status - loading required.");
            }
        } else if (ship.isUnloaded()) {
            builder.append(" Ship status - unloading required.");
        }
        LOGGER.log(Level.INFO, builder);
        ship.setShipState(new LoadingUnloadingState());
    }
}
