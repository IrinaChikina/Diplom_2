
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;

public class ListOrder {

    @Step("Проверка выполнения запроса на полуение списка заказов пользователя без авторизации")
    public void listOrderWithoutAuthorizationForUser() {
        given().log().all()
                .contentType(ContentType.JSON)
                .filter((new AllureRestAssured()))
                .get(Constant.API_ORDER)
                .then().log().all().statusCode(HTTP_UNAUTHORIZED)
                .and().assertThat().body("message", equalTo("You should be authorised"));
    }

    @Step("(Проверка выполнения запроса на полуение списка заказов пользователя с авторизацией")
    public void listOrderWithAuthorizationForUser(String token) {
        given().log().all()
                .contentType(ContentType.JSON)
                .filter((new AllureRestAssured()))
                .header("Authorization", token)
                .get(Constant.API_ORDER)
                .then().log().all().statusCode(HTTP_OK)
                .and().assertThat().body("success", equalTo(true));
    }
}
