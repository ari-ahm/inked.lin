package ir.doodmood.mashtframework;

import ir.doodmood.mashtframework.core.Logger;
import org.junit.Test;

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
