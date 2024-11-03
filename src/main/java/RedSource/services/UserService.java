package RedSource.services;

import RedSource.Entities.User;
import RedSource.Entities.DTO.UserDTO;
import RedSource.repositories.UserRepository;
import RedSource.Entities.utils.MessageUtils;
import RedSource.UserServiceException;
import RedSource.Entities.RO.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String DONOR = "Donor";
    private static final String DONORS = "Donors";
    private static final String ID = "ID";

    @Autowired
    private UserRepository userRepository;

    // Get all users (donors)
    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            logger.warn("No users found.");
            throw new UserServiceException(MessageUtils.retrieveEmpty(DONORS));
        }
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get a user (donor) by ID
    public UserDTO getById(Integer id) {
        validateId(id);
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new UserServiceException(MessageUtils.notFound(DONOR + " with ID " + id)));
    }

    // Create a new user (donor)
    public UserDTO create(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        UserValidator.validate(user);
        checkIfUserExists(user);
        user.setCreatedAt(Date.from(Instant.now()));
        user.setUpdatedAt(Date.from(Instant.now()));
        logger.info("Creating user: {}", user);
        User createdUser = userRepository.save(user);
        return convertToDTO(createdUser);
    }

    // Update an existing user (donor)
    public UserDTO update(Integer id, UserDTO userDTO) {
        validateId(id);
        User user = convertToEntity(userDTO);
        UserValidator.validate(user);
        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserDetails(existingUser, user);
                    existingUser.setUpdatedAt(Date.from(Instant.now()));
                    return userRepository.save(existingUser);
                })
                .map(this::convertToDTO)
                .orElseThrow(() -> new UserServiceException(MessageUtils.notFound(DONOR + " with ID " + id)));
    }

    // Delete a user (donor) by ID
    public void delete(Integer id) {
        validateId(id);
        if (!userRepository.existsById(id)) {
            throw new UserServiceException(MessageUtils.notFound(DONOR + " with ID " + id));
        }
        userRepository.deleteById(id);
    }

    private void checkIfUserExists(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw new UserServiceException(MessageUtils.alreadyExists("Email " + user.getEmail()));
                });
    }

    private void updateUserDetails(User existingUser, User newUser) {
        existingUser.setName(newUser.getName());
        existingUser.setEmail(newUser.getEmail());
        existingUser.setPassword(newUser.getPassword());
        existingUser.setDateOfBirth(newUser.getDateOfBirth());
        existingUser.setContact_information(newUser.getContact_information());
        if (newUser.getProfile_picture() != null) {
            existingUser.setProfile_picture(newUser.getProfile_picture());
        }
    }

    private void validateId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(ID));
        }
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .bloodTypeId(user.getBloodTypeId())
                .role(user.getRole())
                .name(user.getName())
                .email(user.getEmail())
                .profilePicture(user.getProfile_picture())
                .dateOfBirth(user.getDateOfBirth())
                .contactInformation(user.getContact_information())
                .build();
    }

    private User convertToEntity(UserDTO userDTO) {
        return User.builder()
                .userId(userDTO.getUserId())
                .bloodTypeId(userDTO.getBloodTypeId())
                .role(userDTO.getRole())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .profile_picture(userDTO.getProfilePicture())
                .dateOfBirth(userDTO.getDateOfBirth())
                .contact_information(userDTO.getContactInformation())
                .build();
    }
}