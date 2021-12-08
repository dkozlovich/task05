package by.epam.task05.reader;


import by.epam.task05.exception.CustomException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PortReader {
    private static final Logger logger = LogManager.getLogger();

    public int[] readFromFile(String filename) throws CustomException {
        Properties properties = new Properties();
        try {
            try (FileInputStream fileInputStream = new FileInputStream(filename)) {
                properties.load(fileInputStream);
            }
        } catch (IOException | NullPointerException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new CustomException("IOException in read from file method with path " + filename);
        }
        int[] value = new int[4];
        value[0] = Integer.parseInt(properties.getProperty("countOfPiers"));
        value[1] = Integer.parseInt(properties.getProperty("countOfShips"));
        value[2] = Integer.parseInt(properties.getProperty("containerCapacity"));
        value[3] = Integer.parseInt(properties.getProperty("containerNumber"));
        return value;
    }

}