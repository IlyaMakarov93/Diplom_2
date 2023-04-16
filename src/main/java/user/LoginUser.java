package user;

public class LoginUser {
    private final String email;
    private final String password;

    public static LoginUser from(UserInformation userInformation) {
        return new LoginUser(userInformation.getEmail(), userInformation.getPassword());
    }

    public LoginUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
