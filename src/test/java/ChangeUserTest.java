import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChangeUserTest {

    CreatingUser creatingUser = GeneratorUser.getRandomUser();
    private String changeEmail = GeneratorUser.getRandomEmail();
    private String changeName = GeneratorUser.getRandomName();

    String token;
    String secondToken;

    @Step("Запуск Stellar Burgers")
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.URL_BURGER;
    }

    CreatingUser creatingEmailUser = new CreatingUser(changeEmail, creatingUser.getPassword(), changeName);
    ChangeUser changeNameUser = new ChangeUser(creatingUser.getEmail(), changeName);
    ChangeUser changeEmailUser = new ChangeUser(changeEmail, creatingUser.getName());

    @Test
    @DisplayName("Check change name user without Authorization")
    @Description("PATCH api/auth/user")
    public void ChangeNameUserWithoutAuthorizationTest() {
        Response creatingResponse = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(creatingResponse);

        Response response = changeNameUser.changeUserWithoutAuthorization(changeNameUser);
        changeNameUser.errorAuthorization(response);
    }

    @Test
    @DisplayName("Check change email user without Authorization")
    @Description("PATCH api/auth/user")
    public void ChangeEmailUserWithoutAuthorizationTest() {
        Response creatingResponse = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(creatingResponse);

        Response response = changeEmailUser.changeUserWithoutAuthorization(changeEmailUser);
        changeEmailUser.errorAuthorization(response);
    }

    @Test
    @DisplayName("Check change name user with Authorization")
    @Description("PATCH api/auth/user")
    public void ChangeNameUserWithAuthorizationTest() {
        Response creatingResponse = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(creatingResponse);

        Response response = changeNameUser.changeUserWithAuthorization(changeNameUser, token);
        changeNameUser.changeUserOk(response);
        String message = changeNameUser.changeNameUser(response);
        Assert.assertEquals(changeNameUser.getName(), message);
    }

    @Test
    @DisplayName("Check change email user with Authorization")
    @Description("PATCH api/auth/user")
    public void checkChangeEmailUserWithAuthorization() {
        Response creatingResponse = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(creatingResponse);

        Response response = changeEmailUser.changeUserWithAuthorization(changeEmailUser, token);
        changeEmailUser.changeUserOk(response);
        String message = changeEmailUser.changeEmailUser(response);
        Assert.assertEquals(changeEmailUser.getEmail(), message);
    }

    @Test
    @DisplayName("Check change email user with Authorization on email other user")
    @Description("PATCH api/auth/user")
    public void checkChangeAlienEmailUserWithAuthorization() {
        Response firstUser = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(firstUser);

        Response creatingEmailResponse = creatingUser.creatingUser(creatingEmailUser);
        secondToken = creatingUser.checkCreatedOK(creatingEmailResponse);

        Response secondResponse = changeEmailUser.changeUserWithAuthorization(changeEmailUser, token);
        changeEmailUser.changeUserForbidden(secondResponse);
    }

    @After
    public void deleteOrder() {
        if (token != null)
            creatingUser.deleteUser(token);
        if (secondToken != null)
            creatingUser.deleteUser(secondToken);
    }
}
