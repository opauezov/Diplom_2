import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.User;
import steps.UserClient;
import steps.UserData;

import static org.junit.Assert.assertEquals;

public class ChangeUserDataTest {
    private UserClient userClient;
    private User user;
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(user);
            System.out.println("User deleted");
        } else {
            System.out.println("No user to delete");
        }
    }

    @Test
    @DisplayName("Изменение данных пользователя: с авторизацией")
    public void editUserDataWithLogin() {
        accessToken = userClient.createUser(user).path("accessToken");
        userClient.loginUser(UserData.from(user));
        User newUser = User.getRandomUser();
        Response changedData = userClient.changeDataUserWithLogin(newUser, accessToken);

        assertEquals("Статус код", 200, changedData.statusCode());
        assertEquals("Почта пользователя не изменилась",
                newUser.getEmail().toLowerCase(), changedData.path("user.email"));
        assertEquals("Имя пользователя не изменилось",
                newUser.getName(), changedData.path("user.name"));
    }

    @Test
    @DisplayName("Изменение данных пользователя: без авторизации")
    public void userChangeWithoutLogin() {
        userClient.loginUser(UserData.from(user));
        User newUser = User.getRandomUser();
        Response changedData = userClient.changeDataUserWithoutLogin(newUser);

        assertEquals("Статус код не соответствует 401", 401, changedData.statusCode());
    }
}
