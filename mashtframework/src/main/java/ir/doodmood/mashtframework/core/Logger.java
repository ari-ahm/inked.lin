package ir.doodmood.mashtframework.core;

import ir.doodmood.mashtframework.annotation.Component;

@Component(singleton = true)
public class Logger {
    public void debug(String... message);
    public void info(String... message);
    public void warning(String... message);
    public void error(String... message);
    public void critical(String... message);
}
