import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.User;
import steps.UserClient;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

public class CreateUserTest {

    private UserClient userClient;
    private User user;
    private Response response;
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
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
    @DisplayName("Создать уникального пользователя")
    public void userCreateSuccess() {
        response = userClient.createUser(user);
        boolean isUserCreated = true;


        assertEquals("", SC_OK, response.statusCode());
        assertEquals("Пользователь не создан", isUserCreated, response.then().extract().path("success"));
    }

    @Test
    @DisplayName("Создать пользователя повторно")
    public void twoIdenticalUsersNotCreate() {
        response = userClient.createUser(user);
        Response responseCreateIdenticalUsers = userClient
                .createUser(new User(user.getEmail(), user.getPassword(), user.getName()));
        String messageIdenticalUsers = "User already exists";

        assertEquals("Созданы два одинаковых курьера", messageIdenticalUsers,
                responseCreateIdenticalUsers.then().extract().path("message"));
    }

    @Test
    @DisplayName("Создание пользователя без заплнения обязательного поля")
    public void userWithoutRequiredField() {
        user = new User("",
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
        response = userClient.createUser(user);
        String userWithoutEmail = "Email, password and name are required fields";
        assertEquals("", SC_FORBIDDEN, response.statusCode());
        if (response.statusCode() == SC_OK) {
            userClient.deleteUser(user);
        }
        assertEquals("Создан курьер без почты",
                userWithoutEmail, response.then().extract().path("message"));
    }
}
