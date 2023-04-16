package user;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    private UserManagement userManagement;
    private UserInformation userInformation;
    private Tokens token;
    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured());
    }
    @Before
    public void setUp() {
        userManagement = new UserManagement();
    }
    @After
    @Description("Удаление пользователя")
    public void removeUser() {
        token = userManagement.loginUser(LoginUser.from(userInformation))
                .extract().body()
                .as(Tokens.class);
        if (!(token.getAccessToken() == null)) {
            userManagement.removeUser(token.getAccessToken());
        }
    }
    @Test
    @Description("Создание пользователя")
    public void createUserTest() {
        userInformation = UserCreator.getRandom();
        userManagement.createUser(userInformation)
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("user", notNullValue())
                .body("user.email", equalTo(userInformation.getEmail().toLowerCase()))
                .body("user.name", equalTo(userInformation.getName()));
    }
    @Test
    @Description("Создание пользователя без Email")
    public void createUserWithoutEmailTest() {
        userInformation = UserCreator.getRandomWithoutEmail();
        userManagement.createUser(userInformation)
                .assertThat()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @Description("Создание пользователя без имени")
    public void createUserWithoutNameTest() {
        userInformation = UserCreator.getRandomWithoutName();
        userManagement.createUser(userInformation)
                .assertThat()
                .statusCode(403)
                .and()
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @Description("Создание пользователя без пароля")
    public void createUserWithoutPassword() {
        userInformation = UserCreator.getRandomWithoutPassword();
        userManagement.createUser(userInformation)
                .assertThat()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @Description("Создание пользователя с уже зарегистрированными данными")
    public void createUserAlreadyExist() {
        userInformation = UserCreator.getRandom();
        userManagement.createUser(userInformation);
        userManagement.createUser(userInformation)
                .assertThat()
                .statusCode(403)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("User already exists"));
    }
    @Test
    @Description("Создание пользователя без данных")
    public void createNullUser() {
        userInformation = new UserInformation();
        userManagement.createUser(userInformation)
                .assertThat()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
