package zogala.tomasz.atipera.middleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import zogala.tomasz.atipera.api.dto.ErrorDto;
import zogala.tomasz.atipera.github.GitHubService;

import java.util.logging.Logger;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(GitHubService.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();


    @ExceptionHandler(RequestException.class)
    protected ResponseEntity<ErrorDto> handleRequestException(final RequestException ex, final WebRequest request) {
        LOGGER.warning(String.format("%s threw exception %s", request.getDescription(true), ex.getMessage()));
        final ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(ex.getRequestStatus().value());
        errorDto.setMessage(ex.getRequestMessage());
        return new ResponseEntity<>(errorDto, ex.getRequestStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        errorDto.setMessage("Requested content type is not supported");
        byte[] responseBytes = null;

        try {
            responseBytes = this.objectMapper.writeValueAsBytes(errorDto);
        } catch (JsonProcessingException e) {
            // Should not happen
            responseBytes = String.format("{\"status\": %d, \"message\": \"%s\"}", errorDto.getStatus(), errorDto.getMessage()).getBytes();
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBytes);
    }
}