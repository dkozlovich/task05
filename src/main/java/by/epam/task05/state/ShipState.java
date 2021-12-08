package by.epam.task05.state;

import by.epam.task05.entity.Ship;
import by.epam.task05.exception.CustomException;

public interface ShipState {
    void nextAction(Ship ship) throws CustomException;
}
