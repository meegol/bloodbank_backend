package RedSource.dto;

public record TokenRefreshResponse(
    String accessToken,
    String refreshToken,
    String tokenType
) {}
