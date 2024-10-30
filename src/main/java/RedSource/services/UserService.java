package RedSource.services;

import RedSource.entities.User;
import RedSource.repositories.UserRepository;
import RedSource.Utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final String DONOR = "Donor";
    private static final String DONORS = "Donors";
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String CONTACT_INFORMATION = "Contact Information";
    private static final String BIRTHDATE = "Date of Birth";
    private static final String ID = "ID";

    @Autowired
    private UserRepository userRepository;

    // Get all users (donors)
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new RuntimeException(MessageUtils.retrieveEmpty(DONORS));
        }
        return users;
    }

    // Get a user (donor) by ID
    public User getById(Integer id) {
        validateId(id);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageUtils.notFound(DONOR + " with ID " + id)));
    }

    // Create a new user (donor)
    public User create(User user) {
        validateUser(user);
        checkIfUserExists(user);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        System.out.println("Creating user: " + user);
        return userRepository.save(user);
    }

    // Update an existing user (donor)
    public User update(Integer id, User user) {
        validateId(id);
        validateUser(user);
        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserDetails(existingUser, user);
                    existingUser.setUpdatedAt(new Date()); // Update updatedAt to current date
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException(MessageUtils.notFound(DONOR + " with ID " + id)));
    }

    // Delete a user (donor) by ID
    public void delete(Integer id) {
        validateId(id);
        if (!userRepository.existsById(id)) {
            throw new RuntimeException(MessageUtils.notFound(DONOR + " with ID " + id));
        }
        userRepository.deleteById(id);
    }

    // Utility to validate user data
    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(DONOR));
        }
        if (!StringUtils.hasText(user.getName())) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(NAME));
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(EMAIL));
        }
        if (!StringUtils.hasText(user.getUsername())) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(USERNAME));
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(PASSWORD));
        }
        if (!StringUtils.hasText(user.getContact_information())) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(CONTACT_INFORMATION));
        }
        if (user.getDateOfBirth() == null) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(BIRTHDATE));
        }
    }

    // Utility to check for existing user with same email or username
    private void checkIfUserExists(User user) {
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        Optional<User> existingUserByUsername = userRepository.findByUsername(user.getUsername());

        if (existingUserByEmail.isPresent()) {
            throw new RuntimeException(MessageUtils.alreadyExists("Email " + user.getEmail()));
        }
        if (existingUserByUsername.isPresent()) {
            throw new RuntimeException(MessageUtils.alreadyExists("Username " + user.getUsername()));
        }
    }

    // Utility to update user details
    private void updateUserDetails(User existingUser, User newUser) {
        existingUser.setName(newUser.getName());
        existingUser.setEmail(newUser.getEmail());
        existingUser.setUsername(newUser.getUsername());
        existingUser.setPassword(newUser.getPassword());
        existingUser.setDateOfBirth(newUser.getDateOfBirth());
        existingUser.setContact_information(newUser.getContact_information());

        if (newUser.getProfile_picture() != null) {
            existingUser.setProfile_picture(newUser.getProfile_picture());
        }
    }

    // Utility to validate ID
    private void validateId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(ID));
        }
    }
}
