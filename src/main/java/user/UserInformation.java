package user;

public class UserInformation {
    private String email;
    private String password;
    private String name;
    public UserInformation() {
    }
    public UserInformation(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public UserInformation(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
