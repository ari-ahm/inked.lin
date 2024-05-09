package ir.doodmood.mashtframework.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CircularDependencyException extends Exception {
    public CircularDependencyException(String message) {
        super(message);
    }
}
