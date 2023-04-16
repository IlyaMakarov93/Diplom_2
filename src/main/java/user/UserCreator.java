package user;

import org.apache.commons.lang3.RandomStringUtils;
public class UserCreator {
    public static UserInformation getRandom() {
        String email = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new UserInformation(email + "@yandex.ru", password, name);
    }

    public static UserInformation getRandomWithoutName() {
        String email = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        return new UserInformation(email + "@yandex.ru", password);
    }

    public static UserInformation getRandomWithoutEmail() {
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new UserInformation(password, name);
    }

    public static UserInformation getRandomWithoutPassword() {
        String email = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new UserInformation(email + "@yandex.ru", name);
    }
}
