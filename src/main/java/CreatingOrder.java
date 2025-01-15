import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.io.File;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;

public class CreatingOrder {

    @Step("Запрос на создание заказа с указанием ингредиентов для авторизированного пользователя")
    public Response creatingOrderWithIngredientsAndAuthorization(String token, File json) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .filter((new AllureRestAssured()))
                .header("Authorization", token)
                .body(json)
                .when().post(Constant.API_ORDER);
    }

    @Step("Оформление заказа для авторизированного пользователя без указанием ингредиентов")
    public Response orderWithoutIngredientsWithAuthorization(String token) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .filter((new AllureRestAssured()))
                .header("Authorization", token)
                .when().post(Constant.API_ORDER);
    }

    @Step("Запрос на создание заказа с указанием ингредиентов без авторизации")
    public Response orderWithIngredientsWithoutAuthorization(File json) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .filter((new AllureRestAssured()))
                .body(json)
                .when().post(Constant.API_ORDER);
    }

    @Step("Проверка статуса запроса при создание заказа с указанием ингредиентов для авторизированного пользователя")
    public void checkStatusCodeOrderWithAuthorization(Response response) {
        response.then().statusCode(HTTP_OK);
    }

    @Step("Проверка статуса запроса при создание заказа при неверном запросе")
    public void checkStatusCodeOrderBadRequest(Response response) {
        response.then().statusCode(HTTP_BAD_REQUEST);
    }

    @Step("Проверка статуса запроса на создание заказа без авторизации")
    public void checkStatusOrderWithoutAuthorization(Response response) {
        response.then().log().all().assertThat().statusCode(HTTP_UNAUTHORIZED);
    }


    @Step("Проверка статуса запроса на создание заказа с ошибочным списком ингредиентов")
    public ValidatableResponse checkStatusCodeOrderInternalError(Response response) {
        return response.then().log().all().assertThat().statusCode(HTTP_INTERNAL_ERROR);
    }

    @Step("Проверка сообщения при создании запроса на создание заказа с указанием ингредиентов для авторизированного пользователя")
    public void booleanMessageForOrder(Response response) {
        response.then().log().all().assertThat().body("success", equalTo(true));
    }

    @Step("Проверка текста сообщения ответа при создании запроса на создание заказа без авторизации")
    public String checkTextMessageForOrder(Response response) {
        return response.then().log().all().extract().path("message");
    }

}


