package RedSource.controllers;

import RedSource.dto.AuthResponse;
import RedSource.dto.LoginRequest;
import RedSource.dto.RegisterRequest;
import RedSource.entities.User;
import RedSource.entities.DTO.UserDTO;
import RedSource.security.jwt.JwtTokenGenerator;
import RedSource.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenGenerator tokenGenerator;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        
        logger.debug("Login endpoint hit with request: {}", request);
        logger.info("Attempting login for user: {}", request.email());
        
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            logger.info("User authenticated successfully: {}", request.email());
            
            // Get UserDetails from Authentication
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Generate tokens
            String accessToken = tokenGenerator.generateToken(userDetails);
            String refreshToken = tokenGenerator.generateRefreshToken(userDetails);

            logger.info("Generated tokens for user: {}", request.email());

            // Create response
            AuthResponse authResponse = new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer"
            );

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}. Error: {}", request.email(), e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response) {
        
        try {
            logger.info("Received registration request for user: {}", request.email());
            
            // Register user
            UserDTO userDTO = userService.registerUser(request);
            logger.info("User registered successfully: {}", userDTO.getEmail());
            
            // Return success response without tokens - user needs to login separately
            return ResponseEntity.ok(new AuthResponse(null, null, "User registered successfully. Please login."));
            
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestHeader("Authorization") String refreshToken,
            HttpServletResponse response) {
        
        logger.debug("Refresh token endpoint hit with token: {}", refreshToken);
        logger.info("Attempting to refresh token");
        
        try {
            // Remove "Bearer " prefix if present
            refreshToken = refreshToken.startsWith("Bearer ") 
                ? refreshToken.substring(7) 
                : refreshToken;

            // Get user from refresh token
            String username = tokenGenerator.extractUsername(refreshToken);
            UserDetails userDetails = userService.loadUserByUsername(username);

            // Validate token
            if (!tokenGenerator.isTokenValid(refreshToken, userDetails)) {
                logger.error("Invalid refresh token");
                return ResponseEntity.badRequest().build();
            }

            logger.info("User loaded from refresh token: {}", username);

            // Generate new tokens
            String newAccessToken = tokenGenerator.generateToken(userDetails);
            String newRefreshToken = tokenGenerator.generateRefreshToken(userDetails);

            logger.info("New tokens generated for user: {}", username);
            
            // Create response
            AuthResponse authResponse = new AuthResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer"
            );

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            logger.error("Refresh token failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}
