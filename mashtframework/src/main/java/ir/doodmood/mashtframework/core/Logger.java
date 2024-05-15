package ir.doodmood.mashtframework.core;

import ir.doodmood.mashtframework.annotation.Component;
import ir.doodmood.mashtframework.exception.CriticalError;

@Component(singleton = true)
public class Logger {
    public void debug(Object... message);
    public void info(Object... message);
    public void warning(Object... message);
    public void error(Object... message);
    public void critical(Object... message) throws CriticalError;
}
