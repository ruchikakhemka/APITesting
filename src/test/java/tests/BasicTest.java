package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;


public class BasicTest {

    String getProductsURL = "https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products";
    String authorizationURL = "https://spree-vapasi-prod.herokuapp.com/spree_oauth/token";
    String token = null;
    String addItemURL = "https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart/add_item";
    String viewCartURL = "https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart";
    String removeItemURL = "https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart/remove_line_item/";
    @Test
    public void testStatusCode()
    {
           given()
                   .get(getProductsURL)
                   .then()
                   .statusCode(200);


    }
    @Test
    public void testLogging()
    {
        given()
                .log()
                .all()
                .get(getProductsURL);
    }
    @Test
    public void printResponse()
    {
        Response response = given().when()
                            .log().all()
                            .get(getProductsURL);

        System.out.println(((Response) response).prettyPrint());

    }

    @Test
    public void testCurrencyAttribute()
    {

        given()

                .get(getProductsURL)
                .then()
                .body("data[0].attributes.currency", equalTo("USD"));
    }
    @Test
    public void testAllCurrencyAttribute()
    {

        Response response = given()
                            .get(getProductsURL);
        JsonPath jsonPathEvaluator = response.jsonPath();
        List<Map> product_attributes = jsonPathEvaluator.get("data");
        for(Map product: product_attributes){

            Map attributes = (Map)product.get("attributes");
            Assert.assertEquals(attributes.get("currency"), "USD");
            }
        }

    @Test
    public void testFilterName()
    {
        Response res =given()
                .queryParam("filter[name]","bag")
                .get(getProductsURL);
        System.out.println(res.prettyPrint());
    }


    @Test
    public void testFilterByPrice()
    {

        Response res =given()
                .log().all()
                .queryParams("filter[price]","")
                .get(getProductsURL);
        System.out.println(res.prettyPeek());
    }

    @BeforeClass
    public void getAuthToken()
    {
        Response response = given()
                            .formParam("grant_type","password")
                            .formParam("username","ruchikaahuja.asm@gmail.com")
                            .formParam("password","abcd1234")
                            .post(authorizationURL);

        token = "Bearer "+response.path("access_token");
        //System.out.println(response.prettyPeek());

    }
    @Test
    public void addItem()
    {
        Map<String,String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization",token);

        String createBody = "{\n" +
                "  \"variant_id\": \"17\",\n" +
                "  \"quantity\": 5\n" +
                "}";
        Response response = given()
                            .headers(headers)
                            .body(createBody)
                            .post(addItemURL);
        Assert.assertEquals(response.getStatusCode(),200);

    }
    @Test
    public void viewCart()
    {
        Map<String,String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization",token);
        Response response = given()
                            .headers(headers)
                            .get(viewCartURL);
        JsonPath jsonPathEvaluator = response.jsonPath();
        List<Map> line_items = jsonPathEvaluator.get("data");
        //List<String> line_ids =
        //System.out.println(cart_items);

    }

    @Test
    public void deleteItem()
    {
        Response response = given()
                            .delete(removeItemURL);
    }

}
