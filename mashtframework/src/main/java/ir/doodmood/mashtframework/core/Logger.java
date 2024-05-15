package ir.doodmood.mashtframework.core;

import ir.doodmood.mashtframework.annotation.Component;

import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component(singleton = true)
public class Logger {

    enum Levels {
        INFO("Info", Colors.LIME_BLUE),
        DEBUG("Debug", Colors.BLUE),
        WARNING("Warning", Colors.YELLOW),
        ERROR("Error", Colors.RED),
        CRITICAL("Critical", Colors.RED);

        private String text;
        private Colors color;

        Levels(String text, Colors color) {
            this.text = text;
            this.color = color;
        }

        public String getText() {
            return text;
        }

        public Colors getColor() {
            return color;
        }
    }

    enum Colors {
        RED("\033[31m"),
        BLUE("\033[32m"),
        GREEN("\033[34m"),
        LIME_BLUE("\033[36m"),
        WHITE("\033[37m"),
        YELLOW("\033[33m"),
        RESET("\033[0m");

        private String ANSI;

        Colors(String ANSI) {
            this.ANSI = ANSI;
        }

        public String getANSI() {
            return ANSI;
        }
    }


    private String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        return setColor(Colors.WHITE, formatter.format(time));
    }

    private String setColor(Colors color, String message) {
        return color.getANSI() + message + Colors.RESET.getANSI();
    }

    private String logFormatter(Levels level, String message) {
        return String.format("%s [%s]: %s", getTime(), setColor(level.getColor(), level.getText()), setColor(level.getColor(), message));
    }

    private String generateMessage(String... messages) {
        String message = "";
        for (int i = 0; i < messages.length; i++) {
            message = message + " " + messages[i];
        }

        return message;
    }

    public void debug(String... messages) {
        System.out.println(logFormatter(Levels.DEBUG, generateMessage(messages)));
    }
    public void info(String... messages){
        System.out.println(logFormatter(Levels.INFO, generateMessage(messages)));
    }
    public void warning(String... messages) {
        System.out.println(logFormatter(Levels.WARNING, generateMessage(messages)));
    }
    public void error(String... messages) {
        System.out.println(logFormatter(Levels.ERROR, generateMessage(messages)));
    }
    public void critical(String... messages) {
        System.out.println(logFormatter(Levels.CRITICAL, generateMessage(messages)));
    }
}
