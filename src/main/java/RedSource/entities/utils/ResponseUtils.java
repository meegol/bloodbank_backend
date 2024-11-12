<<<<<<< HEAD:src/main/java/RedSource/Utils/ResponseUtils.java
package RedSource.Utils;

public class ResponseUtils {
=======
package RedSource.entities.utils;

import RedSource.entities.response.ErrorResponse;
import RedSource.entities.response.SuccessResponse;
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
>>>>>>> dev_arvin:src/main/java/RedSource/entities/utils/ResponseUtils.java
}
