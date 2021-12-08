package by.epam.task05.main;

import by.epam.task05.entity.Port;
import by.epam.task05.entity.Ship;
import by.epam.task05.exception.CustomException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        Port port;
        try {
            port = Port.getInstance();
            List<Ship> ships = port.getShips();
            ExecutorService service = Executors.newFixedThreadPool(port.getShips().size());
            ships.forEach(service::submit);
            service.shutdown();
        } catch (CustomException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
    }
}
