package RedSource.services;

import RedSource.entities.User;
import RedSource.entities.DTO.UserDTO;
import RedSource.entities.RO.UserRO;
import RedSource.entities.enums.RoleType;
import RedSource.repositories.UserRepository;
import RedSource.utils.MessageUtils;
import RedSource.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private static final String DONOR = "Donor";
    private static final String DONORS = "Donors";
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String CONTACT_INFORMATION = "Contact Information";
    private static final String BIRTHDATE = "Date of Birth";
    private static final String ID = "ID";
    private static final String USER = "User";
    private static final String ROLE = "Role";

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Get all users (donors)
    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new RuntimeException(MessageUtils.retrieveEmpty(DONORS));
        }
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get a user (donor) by ID
    public UserDTO getUserById(Integer id) {
        validateId(id);
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException(MessageUtils.notFound(DONOR + " with ID " + id)));
    }

    // Get current user
    public UserDTO getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException(MessageUtils.notFound(USER)));
    }

    // Save a new user from UserRO
    public UserDTO save(UserRO userRO) {
        try {
            RoleType userRole = userRO.getRole() != null ? 
                RoleType.valueOf(userRO.getRole().toUpperCase()) : 
                RoleType.DONOR; // Default role

            User user = User.builder()
                    .name(userRO.getFirstName() + " " + userRO.getLastName())
                    .email(userRO.getEmail())
                    .password(passwordEncoder.encode(userRO.getPassword()))
                    .roles(userRole)
                    .contact_information(userRO.getPhoneNumber())
                    .build();
            
            User savedUser = create(user);
            return convertToDTO(savedUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(ROLE));
        }
    }

    // Update current user
    public UserDTO updateCurrentUser(UserRO userRO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(MessageUtils.notFound(USER)));
        
        updateUserFromRO(currentUser, userRO);
        User updatedUser = userRepository.save(currentUser);
        return convertToDTO(updatedUser);
    }

    // Update user by ID
    public UserDTO update(Integer id, UserRO userRO) {
        validateId(id);
        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFromRO(existingUser, userRO);
                    User updatedUser = userRepository.save(existingUser);
                    return convertToDTO(updatedUser);
                })
                .orElseThrow(() -> new RuntimeException(MessageUtils.notFound(DONOR + " with ID " + id)));
    }

    // Get all users by filter (role)
    public List<UserDTO> getAllByFilter(String role) {
        if (!StringUtils.hasText(role)) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(ROLE));
        }
        
        try {
            RoleType roleEnum = RoleType.valueOf(role.toUpperCase());
            List<User> users = userRepository.findAll();
            
            if (users.isEmpty()) {
                throw new RuntimeException(MessageUtils.retrieveEmpty(role));
            }
            
            return users.stream()
                    .filter(user -> user.getRoles() == roleEnum)
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(ROLE));
        }
    }

    // Create a new user (donor)
    public User create(User user) {
        validateUser(user);
        checkIfUserExists(user);
        
        // Set blood type ID only for donors
        if (user.getRoles() == RoleType.DONOR && user.getBloodTypeId() == null) {
            user.setBloodTypeId(1); // Default to blood type ID 1 (A+)
        }
        
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }

    // Delete a user (donor) by ID and role
    public void delete(Integer id, String role) {
        validateId(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageUtils.notFound(DONOR + " with ID " + id)));
        
        if (role != null && !role.trim().isEmpty() && !user.getRoles().name().equalsIgnoreCase(role)) {
            throw new RuntimeException(MessageUtils.notFound(role + " with ID " + id));
        }
        
        userRepository.deleteById(id);
    }

    // Delete current user
    public void deleteCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(MessageUtils.notFound(USER)));
        userRepository.delete(currentUser);
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

    // Utility to check for existing user with same email
    private void checkIfUserExists(User user) {
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());

        if (existingUserByEmail.isPresent()) {
            throw new RuntimeException(MessageUtils.alreadyExists("Email " + user.getEmail()));
        }
    }

    // Helper method to update User from UserRO
    private void updateUserFromRO(User user, UserRO userRO) {
        if (userRO == null) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(USER));
        }

        if (StringUtils.hasText(userRO.getFirstName()) || StringUtils.hasText(userRO.getLastName())) {
            user.setName(userRO.getFirstName() + " " + userRO.getLastName());
        }
        if (StringUtils.hasText(userRO.getEmail())) {
            user.setEmail(userRO.getEmail());
        }
        if (StringUtils.hasText(userRO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userRO.getPassword()));
        }
        if (StringUtils.hasText(userRO.getPhoneNumber())) {
            user.setContact_information(userRO.getPhoneNumber());
        }
        if (StringUtils.hasText(userRO.getRole())) {
            try {
                RoleType userRole = RoleType.valueOf(userRO.getRole().toUpperCase());
                user.setRoles(userRole);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(MessageUtils.invalidRequest(ROLE));
            }
        }
        user.setUpdatedAt(new Date());
    }

    // Utility to validate ID
    private void validateId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(ID));
        }
    }

    // Convert User to UserDTO
    private UserDTO convertToDTO(User user) {
        // Split the full name into first and last name
        String[] nameParts = user.getName() != null ? user.getName().split(" ", 2) : new String[]{"", ""};
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        return UserDTO.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(user.getContact_information())
                .role(user.getRoles().name())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find user by email (which is our username)
        return userRepository.findByEmail(username)
            .orElseThrow(() -> 
                new UsernameNotFoundException("User not found with email: " + username));
    }

    public UserDTO registerUser(RegisterRequest request) {
        validateRegistrationRequest(request);

        try {
            RoleType userRole = request.roles() != null ? 
                request.roles() : 
                RoleType.DONOR; // Default role

            User user = User.builder()
                    .name(request.name())
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .roles(userRole)
                    .dateOfBirth(request.dateOfBirth())
                    .contact_information(request.contactInformation())
                    .bloodTypeId(1) // Default to A+ for donors
                    .profile_picture("default.jpg") // Default profile picture
                    .build();

            User savedUser = create(user);
            return convertToDTO(savedUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(ROLE));
        }
    }

    public boolean validateRefreshToken(String token) {
        // Implement refresh token validation logic here
        return true; // Temporary implementation
    }

    private void validateRegistrationRequest(RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Registration request cannot be null");
        }
        if (!StringUtils.hasText(request.name())) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(NAME));
        }
        if (!StringUtils.hasText(request.email())) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(EMAIL));
        }
        if (!StringUtils.hasText(request.password())) {
            throw new IllegalArgumentException(MessageUtils.invalidRequest(PASSWORD));
        }
    }
}
