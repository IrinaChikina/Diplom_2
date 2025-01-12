import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;

@AllArgsConstructor
@Data
public class CreatingUser {

    private String email;
    private String password;
    private String name;

    @Step("Запрос на создание нового пользователя")
    public Response creatingUser(CreatingUser user) {
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
    public void creatingUsersSuccessfully(Response response) {
         response.then().log().all()
                .assertThat()
                .body("user.email", equalTo(getEmail()));
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

