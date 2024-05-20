package ir.doodmood.mashtframework.core;

import org.junit.jupiter.api.Test;

public class loggerTest {
    private Logger logger = new Logger();
    @Test
    public void loggerTestDebug() {
        logger.debug("TEST DEBUG");
    }
    @Test
    public void loggerTestInfo() {
        logger.info("TEST INFO");
    }
    @Test
    public void loggerTestWarning() {
        logger.warning("TEST WARNING");
    }

    @Test
    public void loggerTestError() {
        logger.error("TEST ERROR");
    }
}
