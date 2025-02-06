import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;

public class ChangeUser {

    @Step("Запрос на изменение данных пользователя без авторизации")
    public Response changeUserWithoutAuthorization(UserLombok existingUser) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .filter((new AllureRestAssured()))
                .body(existingUser)
                .patch(Constant.API_USER);
    }

    @Step("Данные пользователя не изменены, необходима авторизация")
    public void errorAuthorization(Response response) {
        response.then().log().all()
                .statusCode(HTTP_UNAUTHORIZED)
                .and().assertThat().body("message", equalTo("You should be authorised"));
    }

    @Step("Запрос на изменение данных пользователя c авторизацией")
    public Response changeUserWithAuthorization(UserLombok existingUser, String token) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .filter((new AllureRestAssured()))
                .header("Authorization", token)
                .body(existingUser)
                .patch(Constant.API_USER);
    }

    @Step("Данные пользователя изменены")
    public void changeUserOk(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_OK);
    }

    @Step("Проверка изменения имени пользоватлея")
    public String changeNameUser(Response response) {
        return response.then()
                .extract().path("user.name");
    }

    @Step("Проверка изменения email пользователя")
    public String changeEmailUser(Response response) {
        return response.then()
                .extract().path("user.email");
    }

    @Step("Данные пользователя не изменены,  электронный адрес уже используется")
    public void changeUserForbidden(Response response) {
        response.then().log().all()
                .statusCode(HTTP_FORBIDDEN)
                .and().assertThat().body("message", equalTo("User with such email already exists"));
    }
}
