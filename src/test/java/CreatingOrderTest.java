import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class CreatingOrderTest extends BaseTest {
    String token;

    CreatingOrder creatingOrder = new CreatingOrder();
    CreatingUser creatingUser = new CreatingUser();

    UserLombok createdUser = GeneratorUser.getRandomUser();

    File json = new File(Constant.FILE_INGREDIENTS);
    File falseJson = new File(Constant.FALSE_INGREDIENT);

    @Test
    @DisplayName("Check creating order for authorized user with ingredients")
    @Description("POST api/orders")

    public void creatingOrderWithIngredientAndAuthorizationTest() {
        Response creatingResponse = creatingUser.creatingUser(createdUser);
        token = creatingUser.checkCreatedOK(creatingResponse);
        Response response = creatingOrder.creatingOrderWithIngredientsAndAuthorization(token, json);
        creatingOrder.checkStatusCodeOrderWithIngredients(response);
        creatingOrder.booleanMessageForOrder(response);
    }

    @Test
    @DisplayName("Check creating order for authorized user without ingredients")
    @Description("POST api/orders")
    public void creatingOrderWithoutIngredientAndAuthorizationTest() {
        Response creatingResponse = creatingUser.creatingUser(createdUser);
        token = creatingUser.checkCreatedOK(creatingResponse);
        Response response = creatingOrder.orderWithoutIngredientsWithAuthorization(token);
        creatingOrder.checkStatusCodeOrderBadRequest(response);
        String message = creatingOrder.checkTextMessageForOrder(response);
        Assert.assertEquals("Ingredient ids must be provided", message);
    }

    @Test // баг
    @DisplayName("Check creating order with ingredients without authorization")
    @Description("POST api/orders")
    public void errorCreatingOrderWithoutIngredientTest() {
        Response response = creatingOrder.orderWithIngredientsWithoutAuthorization(json);
        creatingOrder.checkStatusCodeOrderWithIngredients(response);
        creatingOrder.booleanMessageForOrder(response);
    }

    @Test
    @DisplayName("Check creating order for authorized user with false ingredients")
    @Description("POST api/orders")
    public void creatingOrderWithAuthorizationAndFalseIngredientTest() {
        Response creatingResponse = creatingUser.creatingUser(createdUser);
        token = creatingUser.checkCreatedOK(creatingResponse);
        Response response = creatingOrder.creatingOrderWithIngredientsAndAuthorization(token, falseJson);
        creatingOrder.checkStatusCodeOrderInternalError(response);
    }

    @After
    public void deleteUser() {
        if (token != null)
            creatingUser.deleteUser(token);
    }
}