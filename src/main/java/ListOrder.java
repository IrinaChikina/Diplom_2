import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

public class ListOrder {

    @Step("Запрос списка заказов пользователя без авторизации")
    public String listOrderWithoutAuthorizationForUser(){
       return given().log().all()
                .contentType(ContentType.JSON)
                .get(Constant.API_ORDER)
                .then().statusCode(HTTP_UNAUTHORIZED)
                .extract().path("message");
    }
    @Step("Запрос списка заказов пользователя с авторизацией")
    public boolean listOrderWithAuthorizationForUser(String token){
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .get(Constant.API_ORDER)
                .then().statusCode(HTTP_OK)
                .extract().path("success");
    }
}
