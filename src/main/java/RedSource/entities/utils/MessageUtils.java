package RedSource.entities.utils;

import org.springframework.validation.BindingResult;
import java.util.stream.Collectors;

public class MessageUtils {
    public static String retrieveSuccess(String entity) {
        return String.format("%s retrieved successfully", entity);
    }

    public static String saveSuccess(String entity) {
        return String.format("%s saved successfully", entity);
    }

    public static String updateSuccess(String entity) {
        return String.format("%s updated successfully", entity);
    }

    public static String deleteSuccess(String entity) {
        return String.format("%s deleted successfully", entity);
    }

    public static String invalidRequest(String field) {
        return String.format("Invalid %s provided", field);
    }

    public static String validationErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));
    }
}
