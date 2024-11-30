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

public class ListOrderTest {

    Faker faker = new Faker();
    private String email = faker.internet().emailAddress();
    private String password = faker.internet().password(6, 10);
    private String name = faker.name().username();

    String token;

    ListOrder listOrder = new ListOrder();
    CreatingUser creatingUser = new CreatingUser(email,password,name);

    @Step("Запуск Stellar Burgers")
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.URL_BURGER;
    }

    @Test
    @DisplayName("Check get list order for user without authorization ")
    @Description("POST api/orders")
    public void checkGetListOrderWithoutAuthorization() {
        String message = listOrder.listOrderWithoutAuthorizationForUser();
        Assert.assertEquals("You should be authorised", message);
    }

    @Test
    @DisplayName("Check get list order for user with authorization")
    @Description("POST api/orders")
    public void checkGetListOrderWithAuthorization() {
        Response response = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(response);
        boolean result = listOrder.listOrderWithAuthorizationForUser(token);
        Assert.assertTrue(result);
    }

    @After
    public void deleteOrder() {
        if (token != null)
            creatingUser.deleteUser(token);
    }
}
