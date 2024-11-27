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
import com.github.javafaker.Faker;

public class CreatingUserTest {

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
    CreatingUser creatingUserWithoutPassword = new CreatingUser (email,null,name);


    @Test
    @DisplayName("Check creating user")
    @Description("POST api/auth/register")
    public void checkCreatingUniqueUser() {
        Response response =  creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(response);
    }

    @Test
    @DisplayName("Check creating two identical users")
    @Description("POST api/auth/register")
    public void checkCreatedEqualUsers() {
        Response firstUser = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(firstUser);
        Response secondUser = creatingUser.creatingUser(creatingUser);
        ValidatableResponse result = creatingUser.creatingEqualUsers(secondUser);

        Assert.assertEquals("User already exists", result.extract().path("message"));
    }

    @Test
    @DisplayName("Check creating user without password")
    @Description("POST api/auth/register")
    public void checkCreatingUserWithoutPassword() {
        Response response =  creatingUser.creatingUser(creatingUserWithoutPassword);
        ValidatableResponse result = creatingUser.creatingEqualUsers(response);

        Assert.assertEquals("Email, password and name are required fields", result.extract().path("message"));
    }

    @After
    public void deleteOrder() {
        if (token != null)
            creatingUser.deleteUser(token);
    }
}


