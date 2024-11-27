import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

public class ChangeUser {

    private String email;
    private String name;

    public ChangeUser(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Step("Запрос на изменение данных пользователя без авторизации")
    public Response changeUserWithoutAuthorization(ChangeUser changeUser) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(changeUser)
                .patch(Constant.API_USER);
    }

    @Step("Данные пользователя не изменены, необходима авторизация")
    public String errorAuthorization (Response response) {
        return response.then().log().all()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract().path("message");
    }

    @Step("Запрос на изменение данных пользователя c авторизацией")
    public Response changeUserWithAuthorization(ChangeUser changeUser,String token) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(changeUser)
                .patch(Constant.API_USER);
    }

    @Step("Данные пользователя изменены")
    public boolean changeUserOk (Response response) {
        return response.then().log().all()
                .statusCode(HTTP_OK)
                .extract().path("success");
    }

    @Step("Данные пользователя не изменены,  электронный адрес уже используется")
    public String changeUserForbidden (Response response) {
        return response.then().log().all()
                .statusCode(HTTP_FORBIDDEN)
                .extract().path("message");
    }

}
