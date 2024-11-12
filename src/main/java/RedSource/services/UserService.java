package RedSource.services;

import RedSource.entities.RO.UserRO;
import RedSource.entities.User;
import RedSource.entities.utils.MessageUtils;
import RedSource.exceptions.ServiceException;
import RedSource.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    public static final String DONOR = "Donor";
    public static final String BLOODBANK = "BloodBank";
    public static final String HOSPITAL = "Hospital";
    public static final String USER = "User";
    public static final String USERS = "Users";
    public static final String ROLE = "Role";


    private final UserRepository userRepository;

    // Retrieve all users without any filter
    public List<User> getAll() {
        try {
            List<User> users = userRepository.findAll();
            log.info(MessageUtils.retrieveSuccess(USERS));
            return users;
        } catch (Exception e) {
            String errorMessage = MessageUtils.retrieveError(USERS);
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Retrieve users by specific filter (e.g., role)
    public List<User> getAllByFilter(String role) {
        try {
            if (role != null && !role.trim().isEmpty()) {
                List<User> users = userRepository.findAllByRole(role);
                log.info(MessageUtils.retrieveSuccess(role));
                return users;
            } else {
                log.warn(MessageUtils.invalidRequest(ROLE));
                return List.of();
            }
        } catch (Exception e) {
            String errorMessage = MessageUtils.retrieveError(role != null ? role : "Filtered Users");
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Retrieve a user by ID
    public User getUserById(Integer id) {
        try {
            if (Objects.isNull(id)) {
                return null;
            }
            return userRepository.findById(id).orElse(null);
        } catch (Exception e) {
            String errorMessage = MessageUtils.retrieveError(USER);
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }


    // Save a new user
    public void save(UserRO userRO) {
        try {
            userRepository.save(userRO.toEntity(null));
            log.info(MessageUtils.saveSuccess(userRO.getRole().toString()));
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveError(userRO.getRole().toString());
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Update an existing user
    public void update(Integer id, UserRO userRO) {
        try {
            User user = getUserById(id);
            userRepository.save(userRO.toEntity(user));
            log.info(MessageUtils.updateSuccess(userRO.getRole().toString()));
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = MessageUtils.updateError(userRO.getRole().toString());
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Delete a user by ID with dynamic role logging
    public void delete(Integer id, String role) {
        try {
            User user = getUserById(id);
            userRepository.delete(user);
            log.info(MessageUtils.deleteSuccess(role != null ? role : USER));
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = MessageUtils.deleteError(role != null ? role : USER);
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }
}
