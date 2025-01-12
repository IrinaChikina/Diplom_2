import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListOrderTest {

    CreatingUser creatingUser = GeneratorUser.getRandomUser();

    String token;

    ListOrder listOrder = new ListOrder();

    @Step("Запуск Stellar Burgers")
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.URL_BURGER;
    }

    @Test
    @DisplayName("Check get list order for user without authorization ")
    @Description("POST api/orders")
    public void getListOrderWithoutAuthorizationTest() {
        listOrder.listOrderWithoutAuthorizationForUser();
    }

    @Test
    @DisplayName("Check get list order for user with authorization")
    @Description("POST api/orders")
    public void getListOrderWithAuthorizationTest() {
        Response response = creatingUser.creatingUser(creatingUser);
        token = creatingUser.checkCreatedOK(response);
        listOrder.listOrderWithAuthorizationForUser(token);
    }

    @After
    public void deleteOrder() {
        if (token != null)
            creatingUser.deleteUser(token);
    }
}
