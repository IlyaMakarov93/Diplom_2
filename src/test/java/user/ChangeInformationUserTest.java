package user;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import jdk.jfr.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
public class ChangeInformationUserTest {
    UserManagement userManagement;
    UserInformation userInformation;
    Tokens token;
    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured());
    }
    @Before
    @Description("Создание пользователя")
    public void setUp() {
        userManagement = new UserManagement();
        userInformation = UserCreator.getRandom();
        userManagement.createUser(userInformation);
        token = userManagement.loginUser(LoginUser.from(userInformation))
                .extract()
                .body()
                .as(Tokens.class);
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
    @Description("Изменение всех данных авторизованного пользователя")
    public void changeAllDataUserTest() {
        userInformation = UserCreator.getRandom();
        userManagement.changeDataUser(token.getAccessToken(), userInformation)
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("user", notNullValue());
    }
    @Test
    @Description("Изменение имени авторизованного пользователя")
    public void changeNameUserTest() {
        userInformation = new UserInformation(userInformation.getEmail(), userInformation.getPassword(),
                RandomStringUtils.randomAlphabetic(10));
        userManagement.changeDataUser(token.getAccessToken(), userInformation)
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("user", notNullValue())
                .body("user.name", equalTo(userInformation.getName()));
    }
    @Test
    @Description("Изменение email авторизованного пользователя")
    public void changeEmailUserTest() {
        userInformation = new UserInformation(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru",
                userInformation.getPassword(),
                userInformation.getName());
        userManagement.changeDataUser(token.getAccessToken(), userInformation)
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("user", notNullValue())
                .body("user.email", equalTo(userInformation.getEmail().toLowerCase()));
    }
    @Test
    @Description("Изменение email авторизованного пользователя существующим email-ом")
    public void changeEmailAlreadyExistUserTest() {
        UserInformation newUser = UserCreator.getRandom();
        userManagement.createUser(newUser);
        userManagement.changeDataUser(token.getAccessToken(), newUser)
                .assertThat()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("User with such email already exists"));
        token = userManagement.loginUser(LoginUser.from(newUser))
                .extract().body()
                .as(Tokens.class);
        if (!(token.getAccessToken() == null)) {
           userManagement.removeUser(token.getAccessToken());
        }
    }
    @Test
    @Description("Изменение пароля авторизованного пользователя")
    public void changePasswordUserTest() {
        userInformation = new UserInformation(userInformation.getEmail(),
                RandomStringUtils.randomAlphabetic(10),
                userInformation.getName());
        userManagement.changeDataUser(token.getAccessToken(), userInformation)
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("user", notNullValue());
    }
    @Test
    @Description("Измененние все данных неавторизованного пользователя")
    public void changeDataUserWithoutTokenTest() {
        userManagement.changeDataUserWithoutToken(userInformation)
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }
}
