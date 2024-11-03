package RedSource.Entities.utils;

import RedSource.Entities.response.ErrorResponse;
import RedSource.Entities.response.SuccessResponse;
import org.springframework.http.HttpStatus;

public class ResponseUtils {

    public static <T> SuccessResponse<T> buildSuccessResponse(HttpStatus status, String message) {
        SuccessResponse<T> response = new SuccessResponse<>();
        response.setStatusCode(status.value());
        response.setMessage(message);

        return response;
    }

    public static <T> SuccessResponse<T> buildSuccessResponse(HttpStatus status, String message, T data) {
        SuccessResponse<T> response = new SuccessResponse<>();
        response.setStatusCode(status.value());
        response.setMessage(message);
        response.setData(data);

        return response;
    }

    public static ErrorResponse buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse response = new ErrorResponse();
        response.setStatusCode(status.value());
        response.setMessage(message);

        return response;
    }
}
