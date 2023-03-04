package cl.exercise.user.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ErrorList {
    private List<ErrorDetails> errors;
}
