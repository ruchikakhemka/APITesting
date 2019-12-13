import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;


public class BasicTest {

    String getProductsURL = "https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products";
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



}
