package nl.kooi.vehicle.api;

import lombok.extern.slf4j.Slf4j;
import nl.kooi.vehicle.exception.NotFoundException;
import nl.kooi.vehicle.exception.VehicleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException exception) {
        log.error("received not found exception");

        var problem = ProblemDetail
                .forStatus(404);

        problem.setStatus(HttpStatus.NOT_FOUND);
        problem.setDetail(exception.getMessage());
        problem.setTitle("NOT FOUND");

        return problem;
    }

    @ExceptionHandler(VehicleException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ProblemDetail handleVehicleException(VehicleException exception) {
        log.error("received vehicle exception");

        var problem = ProblemDetail
                .forStatus(400);

        problem.setStatus(HttpStatus.BAD_REQUEST);
        problem.setDetail(exception.getMessage());
        problem.setTitle("INVALID VEHICLE REQUEST");

        return problem;
    }
}
