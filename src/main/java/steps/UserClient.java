package steps;

import client.ClientApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static constants.Url.*;
import static io.restassured.RestAssured.given;

public class UserClient  extends ClientApi {
    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .log()
                .all()
                .body(user)
                .post(REGISTER_USER);
    }

    @Step("Логин пользователя")
    public Response loginUser(UserData userData) {
        return given()
                .spec(getBaseSpec())
                .log()
                .all()
                .body(userData)
                .post(LOGIN_USER);
    }

    @Step("Удаление пользователя")
    public void deleteUser(User user) {
        given()
                .spec(getBaseSpec())
                .log()
                .all()
                .body(user)
                .delete(USER_INFO);
    }

    @Step("Изменение данных авторизированного пользователя")
    public Response changeDataUserWithLogin(Object changesUser, String accessUserToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessUserToken)
                .log()
                .all()
                .body(changesUser)
                .when()
                .patch(USER_INFO);
    }

    @Step("Изменение данных неавторизованного пользователя")
    public Response changeDataUserWithoutLogin(Object changesUser) {
        return given()
                .spec(getBaseSpec())
                .log()
                .all()
                .body(changesUser)
                .when()
                .patch(USER_INFO);
    }
}
