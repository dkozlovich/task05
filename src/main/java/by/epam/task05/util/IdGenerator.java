package by.epam.task05.util;

public class IdGenerator {
    private static int counter;

    private IdGenerator() {

    }

    public static int generateId() {
        return counter++;
    }
}
