import com.github.javafaker.Faker;
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

    Faker faker = new Faker();
    private String email = faker.internet().emailAddress();
    private String password = faker.internet().password(6, 10);
    private String name = faker.name().username();
    private String changeEmail = faker.internet().emailAddress();
    private String changeName = faker.name().username();


    String token;
    String secondToken;

    @Step("Запуск Stellar Burgers")
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.URL_BURGER;
    }

    CreatingUser creatingUser = new CreatingUser (email,password,name);
    CreatingUser creatingEmailUser = new CreatingUser (changeEmail,password,changeName);
    ChangeUser changeNameUser = new ChangeUser(creatingUser.getEmail(),changeName);
    ChangeUser changeEmailUser = new ChangeUser(changeEmail,creatingUser.getName());

    @Test
    @DisplayName("Check change name user without Authorization")
    @Description("PATCH api/auth/user")
    public void checkChangeNameUserWithoutAuthorization() {
        Response creatingResponse = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(creatingResponse);

        Response response = changeNameUser.changeUserWithoutAuthorization(changeNameUser);
        String message = changeNameUser.errorAuthorization(response);

        Assert.assertEquals("You should be authorised", message);
    }

    @Test
    @DisplayName("Check change email user without Authorization")
    @Description("PATCH api/auth/user")
    public void checkChangeEmailUserWithoutAuthorization() {
        Response creatingResponse = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(creatingResponse);

        Response response = changeEmailUser.changeUserWithoutAuthorization(changeEmailUser);
        String message = changeEmailUser.errorAuthorization(response);

        Assert.assertEquals("You should be authorised",message);
    }

    @Test
    @DisplayName("Check change name user with Authorization")
    @Description("PATCH api/auth/user")
    public void checkChangeNameUserWithAuthorization() {
        Response creatingResponse = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(creatingResponse);

        Response response = changeNameUser.changeUserWithAuthorization(changeNameUser,token);
        boolean message = changeNameUser.changeUserOk(response);
        Assert.assertTrue(message);
    }

    @Test
    @DisplayName("Check change email user with Authorization")
    @Description("PATCH api/auth/user")
    public void checkChangeEmailUserWithAuthorization() {
        Response creatingResponse = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(creatingResponse);

        Response response = changeEmailUser.changeUserWithAuthorization(changeEmailUser,token);
        boolean message = changeEmailUser.changeUserOk(response);
        Assert.assertTrue(message);
    }

    @Test
    @DisplayName("Check change email user with Authorization on email other user")
    @Description("PATCH api/auth/user")
    public void checkChangeAlienEmailUserWithAuthorization() {
        Response firstUser = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(firstUser);

        Response creatingEmailResponse = creatingUser.creatingUser(creatingEmailUser);
        secondToken = creatingUser.checkCreatedOK(creatingEmailResponse);

        Response secondResponse = changeEmailUser.changeUserWithAuthorization(changeEmailUser,token);
        String message = changeEmailUser.changeUserForbidden(secondResponse);
        Assert.assertEquals("User with such email already exists", message);
    }


    @After
    public void deleteOrder() {
        if (token != null)
            creatingUser.deleteUser(token);
        if (secondToken != null)
            creatingUser.deleteUser(secondToken);
    }
}
