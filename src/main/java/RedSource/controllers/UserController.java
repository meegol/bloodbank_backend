package RedSource.controllers;

import RedSource.Entities.DTO.UserDTO;
import RedSource.Entities.utils.MessageUtils;
import RedSource.Entities.utils.ResponseUtils;
import RedSource.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final String DONOR = "Donor";

    @Autowired
    private UserService userService;

    // Get all users (donors)
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> users = userService.getAll(); // Change to UserDTO
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(HttpStatus.OK, "Users retrieved successfully", users));
    }

    // Get a user (donor) by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        UserDTO user = userService.getById(id); // Change to UserDTO
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(HttpStatus.OK, "User retrieved successfully", user));
    }

    // Create a new user (donor)
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) { // Change to UserDTO
        UserDTO createdUser = userService.create(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtils.buildSuccessResponse(HttpStatus.CREATED, MessageUtils.saveSuccess(DONOR), createdUser));
    }

    // Update an existing user (donor) by ID
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) { // Change to UserDTO
        UserDTO updatedUser = userService.update(id, userDTO);
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(HttpStatus.OK, MessageUtils.updateSuccess(DONOR), updatedUser));
    }

    // Delete a user (donor) by ID
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(HttpStatus.NO_CONTENT, MessageUtils.deleteSuccess(DONOR)));
    }
}
