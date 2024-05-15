package ir.doodmood.mashtframework.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CriticalError extends Error {
    public CriticalError(String message) {
        super(message);
    }
}
