import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class AuthorizationUser {

    @Step("Запрос на авторизацию пользователя")
    public Response authorizationUser(UserLambok existingUser) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .filter((new AllureRestAssured()))
                .body(existingUser)
                .when().post(Constant.API_LOGIN);
    }

    @Step("Авторизация успешна")
    public void authorizationUserOK(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .and().assertThat().body("success", equalTo(true));
    }

    @Step("Авторизация не выполнена")
    public void authorizationUserOff(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and().assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
