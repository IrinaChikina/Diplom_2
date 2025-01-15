import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthorizationUserTest extends BaseTest {
    String token;

    CreatingUser creatingUser = new CreatingUser();
    UserLombok createdUser = GeneratorUser.getRandomUser();

    UserLombok existingUser = UserLombok.builder().email(createdUser.getEmail()).password(createdUser.getPassword()).build();
    UserLombok userWithWrongPassword = UserLombok.builder().email(createdUser.getEmail()).password(GeneratorUser.getRandomPassword()).build();
    UserLombok userWithWrongEmail = UserLombok.builder().email(GeneratorUser.getRandomEmail()).password(createdUser.getPassword()).build();

    @Step("Создание пользователя")
    @Before
    public void creatingNewUser() {
        Response creatingResponse = creatingUser.creatingUser(createdUser);
        token = creatingUser.checkCreatedOK(creatingResponse);
    }

    @Test
    @DisplayName("Check authorization existing user")
    @Description("POST api/auth/login")
    public void authorizationExistingUserTest() {
        Response authorizationResponse = existingUser.authorizationUser(existingUser);
        existingUser.authorizationUserOK(authorizationResponse);
    }

    @Test
    @DisplayName("Check authorization existing user with an incorrect password")
    @Description("POST api/auth/login")
    public void authorizationUserWithWrongPasswordTest() {
        Response authorizationResponse = userWithWrongPassword.authorizationUser(userWithWrongPassword);
        userWithWrongPassword.authorizationUserOff(authorizationResponse);
    }

    @Test
    @DisplayName("Check authorization existing user with an incorrect email")
    @Description("POST api/auth/login")
    public void authorizationUserWithWrongEmailTest() {
        Response authorizationResponse = userWithWrongEmail.authorizationUser(userWithWrongEmail);
        userWithWrongEmail.authorizationUserOff(authorizationResponse);
    }

    @After
    public void deleteUser() {
        if (token != null)
            creatingUser.deleteUser(token);
    }
}