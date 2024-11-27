import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class AuthorizationUser {

    private String email;
    private String password;

    public AuthorizationUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Step("Запрос на авторизацию пользователя")
    public Response authorizationUser (AuthorizationUser existingUser) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(existingUser)
                .when().post(Constant.API_LOGIN);
    }

        @Step("Авторизация успешна")
        public ValidatableResponse authorizationUserOK (Response response) {
          return  response.then().log().all()
                    .assertThat()
                    .statusCode(HTTP_OK);
    }

    @Step("Авторизация не выполнена")
    public ValidatableResponse authorizationUserOff (Response response) {
        return  response.then().log().all()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED);
    }
}
