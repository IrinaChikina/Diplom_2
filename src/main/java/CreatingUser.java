import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

public class CreatingUser {

    @Step("Запрос на создание нового пользователя")
    public Response creatingUser(UserLombok user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .filter((new AllureRestAssured()))
                .body(user)
                .when().post(Constant.API_CREATING);
    }

    @Step("Пользователь успешно создан")
    public String checkCreatedOK(Response response) {
        return response.then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .extract().path("accessToken");
    }

    @Step("Пользователь успешно создан")
    public String creatingUsersSuccessfully(Response response) {
        return response.then()
                .extract().path("user.email");

    }

    @Step("Пользователь не создан")
    public ValidatableResponse creatingEqualUsers(Response response) {
        return response.then().log().all()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN);
    }

    @Step("Созданный пользователь удален")
    public void deleteUser(String token) {
        given().log().all()
                .header("Authorization", token)
                .delete(Constant.API_USER)
                .then().log().all()
                .statusCode(HTTP_ACCEPTED);
    }
}

