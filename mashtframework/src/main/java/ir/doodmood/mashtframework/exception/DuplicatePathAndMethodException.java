package ir.doodmood.mashtframework.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicatePathAndMethodException extends Exception {
    public DuplicatePathAndMethodException(String message) {
        super(message);
    }
}