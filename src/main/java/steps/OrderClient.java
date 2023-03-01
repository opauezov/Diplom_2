package steps;

import client.ClientApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;

import static constants.Url.INGREDIENT;
import static constants.Url.ORDERS;
import static io.restassured.RestAssured.given;

public class OrderClient extends ClientApi {

    @Step("Получение ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENT);
    }

    @Step("Создание заказа")
    public Response createOrder(HashMap ingredients, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .and()
                .body(ingredients)
                .when()
                .post(ORDERS);
    }

    @Step("Получение заказа авторизированного пользователя")
    public Response getOrderWithLogin(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .get(ORDERS);
    }

    @Step("Получение заказа неавторизованного пользователя")
    public Response getOrderWithoutLogin() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS);
    }

}
