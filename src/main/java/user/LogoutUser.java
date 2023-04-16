package user;

public class LogoutUser {
    private final String token;
    public static LogoutUser from(Tokens token) {
        return new LogoutUser(token.getRefreshToken());
    }
    public String getToken() {
        return token;
    }
    public LogoutUser(String token) {
        this.token = token;
    }
}
