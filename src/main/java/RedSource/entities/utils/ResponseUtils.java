package RedSource.entities.utils;

import RedSource.entities.response.SuccessResponse;
import org.springframework.http.HttpStatus;

public class ResponseUtils {
    public static <T> SuccessResponse<T> buildSuccessResponse(HttpStatus status, String message, T data) {
        return SuccessResponse.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> SuccessResponse<T> buildSuccessResponse(HttpStatus status, String message) {
        return buildSuccessResponse(status, message, null);
    }
}
