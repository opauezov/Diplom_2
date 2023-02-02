import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.User;
import steps.UserClient;
import steps.UserData;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class LoginUserTest {
    private UserClient userClient;
    private User user;
    String accessToken;
    private Response responseLogin;
    private final String messageInvalidLogin = "email or password are incorrect";

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.createUser(user);
    }

    @After
    public void tearDown() {
        accessToken = userClient.createUser(user).path("accessToken");
        if (accessToken != null) {
            userClient.deleteUser(user);
            System.out.println("User deleted");
        } else {
            System.out.println("No user to delete");
        }
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void userLoginSuccess() {
        responseLogin = userClient.loginUser(UserData.from(user));
        int expectedCodResponse = 200;
        assertEquals("Код ответа не соответствует 200", SC_OK, responseLogin.statusCode());
    }

    @Test
    @DisplayName("Логин пользователя с неверной почтой")
    public void userInvalidLogin() {
        user = new User(user.getEmail() + "1", user.getPassword());
        responseLogin = userClient.loginUser(UserData.from(user));

        assertEquals("Ошибка. Авторизация с неверным логином", messageInvalidLogin,
                responseLogin.then().extract().path("message"));
        assertEquals("Ошибка. неверный код ответа", SC_UNAUTHORIZED, responseLogin.statusCode());
    }

    @Test
    @DisplayName("Логин пользователя с неверным паролем")
    public void userInvalidPassword() {
        user = new User(user.getEmail(), user.getPassword() + 1);
        responseLogin = userClient.loginUser(UserData.from(user));
        assertEquals("Ошибка. Авторизация с неверным паролем", messageInvalidLogin,
                responseLogin.then().extract().path("message"));
        assertEquals("Ошибка. неверный код ответа", SC_UNAUTHORIZED, responseLogin.statusCode());
    }
}
