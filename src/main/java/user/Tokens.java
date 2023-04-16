package user;

public class Tokens {
  private final String accessToken;
   private String refreshToken;

    public Tokens(String accessToken) {
        this.accessToken = accessToken;
    }
    public Tokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
