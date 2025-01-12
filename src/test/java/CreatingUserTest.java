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

    CreatingUser creatingUser = GeneratorUser.getRandomUser();

    String token;

    @Step("Запуск Stellar Burgers")
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.URL_BURGER;
    }

    CreatingUser creatingUserWithoutPassword = new CreatingUser(GeneratorUser.getRandomEmail(), null, GeneratorUser.getRandomName());
    CreatingUser creatingUserWithoutEmail = new CreatingUser(null, GeneratorUser.getRandomPassword(), GeneratorUser.getRandomName());

    @Test
    @DisplayName("Check creating user")
    @Description("POST api/auth/register")
    public void creatingUniqueUserTest() {
        Response response = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(response);
         creatingUser.creatingUsersSuccessfully(response);
    }

    @Test
    @DisplayName("Check creating two identical users")
    @Description("POST api/auth/register")
    public void createdEqualUsersTest() {
        Response firstUser = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(firstUser);
        Response secondUser = creatingUser.creatingUser(creatingUser);
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


