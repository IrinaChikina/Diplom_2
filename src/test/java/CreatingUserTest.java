import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreatingUserTest {
    String token;

    CreatingUser creatingUser = new CreatingUser();
    UserLombok createdUser = GeneratorUser.getRandomUser();

    UserLombok creatingUserWithoutPassword = new UserLombok(GeneratorUser.getRandomEmail(), null, GeneratorUser.getRandomName());
    UserLombok creatingUserWithoutEmail = new UserLombok(null, GeneratorUser.getRandomPassword(), GeneratorUser.getRandomName());

    @Step("Запуск Stellar Burgers")
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.URL_BURGER;
    }

    @Test
    @DisplayName("Check creating user")
    @Description("POST api/auth/register")
    public void creatingUniqueUserTest() {
        Response response = creatingUser.creatingUser(createdUser);
        token = creatingUser.checkCreatedOK(response);
        String result = creatingUser.creatingUsersSuccessfully(response);
        Assert.assertEquals("Проверьте email созданного пользователя", createdUser.getEmail(), result);
    }

    @Test
    @DisplayName("Check creating two identical users")
    @Description("POST api/auth/register")
    public void createdEqualUsersTest() {
        Response firstUser = creatingUser.creatingUser(createdUser);
        token = creatingUser.checkCreatedOK(firstUser);
        Response secondUser = creatingUser.creatingUser(createdUser);
        ValidatableResponse result = creatingUser.creatingEqualUsers(secondUser);
        Assert.assertEquals("User already exists", result.extract().path("message"));
    }

    @Test
    @DisplayName("Check creating user without password")
    @Description("POST api/auth/register")
    public void creatingUserWithoutPasswordTest() {
        Response response = creatingUser.creatingUser(creatingUserWithoutPassword);
        ValidatableResponse result = creatingUser.creatingEqualUsers(response);
        Assert.assertEquals("Email, password and name are required fields", result.extract().path("message"));
    }

    @Test
    @DisplayName("Check creating user without email")
    @Description("POST api/auth/register")
    public void creatingUserWithoutEmailTest() {
        Response response = creatingUser.creatingUser(creatingUserWithoutEmail);
        ValidatableResponse result = creatingUser.creatingEqualUsers(response);
        Assert.assertEquals("Email, password and name are required fields", result.extract().path("message"));
    }

    @After
    public void deleteOrder() {
        if (token != null)
            creatingUser.deleteUser(token);
    }
}


