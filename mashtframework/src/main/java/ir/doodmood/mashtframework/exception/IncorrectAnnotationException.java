package ir.doodmood.mashtframework.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IncorrectAnnotationException extends Exception {
    public IncorrectAnnotationException(String message) {
        super(message);
    }
}
