import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChangeUserTest extends BaseTest {
    String token;
    String secondToken;

    private String changeEmail = GeneratorUser.getRandomEmail();
    private String changeName = GeneratorUser.getRandomName();

    ChangeUser changeUser = new ChangeUser();
    CreatingUser creatingUser = new CreatingUser();

    UserLombok createdUser = GeneratorUser.getRandomUser();
    UserLombok creatingSecondUser = GeneratorUser.getRandomUser();

    UserLombok changeNameUser = UserLombok.builder().email(createdUser.getEmail()).name(changeName).build();
    UserLombok changeEmailUser = UserLombok.builder().email(changeEmail).name(createdUser.getName()).build();
    UserLombok changeEmailUserOnAlien = UserLombok.builder().email(creatingSecondUser.getEmail()).name(createdUser.getName()).build();

    @Step("Создание пользователя")
    @Before
    public void creatingNewUser() {
        Response creatingResponse = creatingUser.creatingUser(createdUser);
        token = creatingUser.checkCreatedOK(creatingResponse);
    }

    @Test
    @DisplayName("Check change name user without Authorization")
    @Description("PATCH api/auth/user")
    public void changeNameUserWithoutAuthorizationTest() {
        Response response = changeUser.changeUserWithoutAuthorization(changeNameUser);
        changeUser.errorAuthorization(response);
    }

    @Test
    @DisplayName("Check change email user without Authorization")
    @Description("PATCH api/auth/user")
    public void changeEmailUserWithoutAuthorizationTest() {
        Response response = changeUser.changeUserWithoutAuthorization(changeEmailUser);
        changeUser.errorAuthorization(response);
    }

    @Test
    @DisplayName("Check change name user with Authorization")
    @Description("PATCH api/auth/user")
    public void changeNameUserWithAuthorizationTest() {
        Response response = changeUser.changeUserWithAuthorization(changeNameUser, token);
        changeUser.changeUserOk(response);
        String message = changeUser.changeNameUser(response);
        Assert.assertEquals(changeName, message);
    }

    @Test
    @DisplayName("Check change email user with Authorization")
    @Description("PATCH api/auth/user")
    public void changeEmailUserWithAuthorization() {
        Response response = changeUser.changeUserWithAuthorization(changeEmailUser, token);
        changeUser.changeUserOk(response);
        String message = changeUser.changeEmailUser(response);
        Assert.assertEquals(changeEmail, message);
    }

    @Test
    @DisplayName("Check change email user with Authorization on email other user")
    @Description("PATCH api/auth/user")
    public void changeAlienEmailUserWithAuthorization() {
        Response creatingEmailResponse = creatingUser.creatingUser(creatingSecondUser);
        secondToken = creatingUser.checkCreatedOK(creatingEmailResponse);
        Response secondResponse = changeUser.changeUserWithAuthorization(changeEmailUserOnAlien, token);
        changeUser.changeUserForbidden(secondResponse);
    }

    @After
    public void deleteUser() {
        if (token != null)
            creatingUser.deleteUser(token);
    }
}