package RedSource.Entities.RO.validators;

import RedSource.Entities.User;
import RedSource.Entities.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class UserValidator {

    private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);

    public static void validate(User user) {
        if (user == null) {
            logger.error("User object is null.");
            throw new IllegalArgumentException(MessageUtils.invalidRequest(MessageUtils.MISSING_NAME));
        }

        if (!StringUtils.hasText(user.getName())) {
            logger.error("User name is invalid or empty: {}", user.getName());
            throw new IllegalArgumentException(MessageUtils.invalidRequest(MessageUtils.MISSING_NAME));
        }

        if (!StringUtils.hasText(user.getEmail())) {
            logger.error("User email is invalid or empty: {}", user.getEmail());
            throw new IllegalArgumentException(MessageUtils.invalidRequest(MessageUtils.MISSING_EMAIL));
        }

        if (user.getPassword() == null || !StringUtils.hasText(user.getPassword()) || user.getPassword().length() < 6) {
            logger.error("User password is either null, empty, or too short (less than 6 characters).");
            throw new IllegalArgumentException(MessageUtils.invalidRequest(MessageUtils.MISSING_PASSWORD));
        }

        if (!StringUtils.hasText(user.getContact_information())) {
            logger.error("User contact information is invalid or empty: {}", user.getContact_information());
            throw new IllegalArgumentException(MessageUtils.invalidRequest(MessageUtils.MISSING_CONTACT_INFORMATION));
        }

        if (user.getDateOfBirth() == null) {
            logger.error("User birthdate is null.");
            throw new IllegalArgumentException(MessageUtils.invalidRequest(MessageUtils.MISSING_BIRTHDATE));
        }

        logger.debug("User data validated successfully for user: {}", user);
    }
}
