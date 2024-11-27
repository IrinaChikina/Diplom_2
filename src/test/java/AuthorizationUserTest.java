import com.github.javafaker.Faker;
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

public class AuthorizationUserTest {

    Faker faker = new Faker();
    private String email = faker.internet().emailAddress();
    private String password = faker.internet().password(6, 10);
    private String name = faker.name().username();

    String token;

    @Step("Запуск Stellar Burgers")
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.URL_BURGER;
    }

    CreatingUser creatingUser = new CreatingUser (email,password,name);
    AuthorizationUser existingUser = new AuthorizationUser(creatingUser.getEmail(),creatingUser.getPassword());
    AuthorizationUser wrongUser = new AuthorizationUser(creatingUser.getEmail(),password);

    @Test
    @DisplayName("Check authorization existing user")
    @Description("POST api/auth/login")
    public void checkAuthorizationExistingUser() {
        Response creatingResponse = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(creatingResponse);

        Response authorizationResponse = existingUser.authorizationUser(existingUser);
        ValidatableResponse result = existingUser.authorizationUserOK(authorizationResponse);

        Assert.assertTrue(result.extract().path("success"));
    }

    @Test
    @DisplayName("Check authorization existing user")
    @Description("POST api/auth/login")
    public void checkAuthorizationWrongUser() {
        Response authorizationResponse = wrongUser.authorizationUser(wrongUser);
        ValidatableResponse result = wrongUser.authorizationUserOff(authorizationResponse);

        Assert.assertEquals("email or password are incorrect",result.extract().path("message"));
    }

    @After
    public void deleteOrder() {
        if (token != null)
            creatingUser.deleteUser(token);
    }
}
