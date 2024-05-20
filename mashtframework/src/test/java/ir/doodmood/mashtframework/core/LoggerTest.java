package ir.doodmood.mashtframework.core;

import org.junit.jupiter.api.Test;

public class LoggerTest {
    Logger logger = new Logger();
    @Test
    public void loggerTest1() {
        logger.debug("Debug Test");
        logger.info("Info Test");
        logger.warning("Warning Test");
        logger.critical("Critical Test");
        logger.error("Error Test");
    }
}
