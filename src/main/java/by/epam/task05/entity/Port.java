package by.epam.task05.entity;

import by.epam.task05.exception.CustomException;
import by.epam.task05.reader.PortReader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Port {
    private static final Logger LOGGER = LogManager.getLogger();
    private static Port instance;
    private final int TASK_PERIOD = 1000;
    private static final double MAX_LOAD_FACTOR = 75;
    private static final double MIN_LOAD_FACTOR = 25;
    private int containerCapacity;
    private int containerNumber;
    private List<Pier> piers;
    private ArrayList<Ship> ships;
    private Semaphore semaphore;
    private static final AtomicBoolean INSTANCE_INITIALIZED = new AtomicBoolean(false);
    private static final Lock INSTANCE_LOCKER = new ReentrantLock();

    private Port() throws CustomException {
        PortReader portReader = new PortReader();
        int[] value = portReader.readFromFile("target/classes/port.properties");
        int countOfPiers = value[0];
        int countOfShips = value[1];
        containerCapacity = value[2];
        containerNumber = value[3];
        ships = new ArrayList<>();
        for (int i = 0; i < countOfShips; i++) {
            ships.add(new Ship());
        }
        piers = new ArrayList<>();
        for (int i = 0; i < countOfPiers; i++) {
            piers.add(new Pier());
        }
        semaphore = new Semaphore(countOfPiers, true);
        checkSchedule();
        LOGGER.log(Level.INFO, "Create port " + this);
    }

    public static Port getInstance() throws CustomException {
        if (!INSTANCE_INITIALIZED.get()) {
            INSTANCE_LOCKER.lock();
            try {
                if (INSTANCE_INITIALIZED.compareAndSet(false, true)) {
                    instance = new Port();
                }
            } finally {
                INSTANCE_LOCKER.unlock();
            }
        }
        return instance;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public int getContainerCapacity() {
        return containerCapacity;
    }

    public int getContainerNumber() {
        return containerNumber;
    }

    public int getDifference() {
        return containerCapacity - containerNumber;
    }

    public List<Pier> getPiers() {
        return piers;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    public void removeShip(Ship ship) {
        ships.remove(ship);
    }

    public void load(Ship ship) {
        if (ship.getDifference() <= containerNumber) {
            containerNumber -= ship.getDifference();
            ship.setContainerNumber(ship.getContainerCapacity());
        } else {
            ship.setContainerNumber(ship.getContainerNumber() + this.getDifference());
            containerNumber = 0;
        }
        LOGGER.log(Level.INFO, "Number of containers after loading: ship " + ship.getShipId() + " = " +
                ship.getContainerNumber() + "; port = " + containerNumber);
    }

    public void unload(Ship ship) {
        if (this.getDifference() > ship.getContainerNumber()) {
            containerNumber += ship.getContainerNumber();
            ship.setContainerNumber(0);
        } else {
            ship.setContainerNumber(ship.getContainerNumber() - this.getDifference());
            containerNumber = containerCapacity;
        }
        LOGGER.log(Level.INFO, "Number of containers after unloading: ship " + ship.getShipId() + " = " +
                ship.getContainerNumber() + "; port = " + containerNumber);
    }

    private void checkSchedule() {
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                long percent = 100 * containerNumber / containerCapacity;
                if (percent > MAX_LOAD_FACTOR || percent < MIN_LOAD_FACTOR) {
                    containerNumber = containerCapacity / 2;
                }
                LOGGER.log(Level.INFO, "Number of containers in port = " + containerNumber);
            }
        };
        timer.schedule(timerTask, 1, TASK_PERIOD);
    }
}
