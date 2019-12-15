package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.restUtil.HelperMethods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class End2EndFlow {

    String token = null;
    List<Map> product_attributes;
    Map<String,String> headers;
    String lineItemId;
    String productId;
    HelperMethods helperMethods = new HelperMethods();

    @BeforeClass(alwaysRun = true)
    public void getAuthorizationToken()
    {


            Response response = given()
                    .formParam("grant_type","password")
                    .formParam("username","ruchikaahuja.asm@gmail.com")
                    .formParam("password","abcd1234")
                    .post(helperMethods.getauthorizationURL());

            token = "Bearer "+response.path("access_token");
            headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/vnd.api+json");
            headers.put("Authorization",token);
            lineItemId = null;
            productId = null;
    }


    @Test
    public void getProducts()
    {

        Response response = given()
                .get(helperMethods.getProductsURL());
        JsonPath jsonPathEvaluator = response.jsonPath();
        product_attributes = jsonPathEvaluator.get("data");
        productId = jsonPathEvaluator.get("data[0].id");
        Assert.assertEquals(product_attributes.size(),16);
    }

    @Test(dependsOnMethods = "getProducts")
    public void testProductDetails()
    {
        Response response = given()
                            .get(helperMethods.getProductsURL()+"/"+productId);
        Assert.assertEquals(response.getStatusCode(),200);
        JsonPath jsonPathEvaluator = response.jsonPath();
        Assert.assertEquals(jsonPathEvaluator.get("data.attributes.name"),"Ruby on Rails Baseball Jersey");
    }
    @Test
    public void testCreateCart()
    {

        Response response = given()
                            .headers(headers)
                            .post(helperMethods.getCartURL());
        Assert.assertEquals(response.getStatusCode(), 201);
        //check type value should be cart in data
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.get("data.type"),"cart");

        //check cart created for loggedin user
        Assert.assertEquals(jsonPath.get("data.attributes.email"),"ruchikaahuja.asm@gmail.com");
    }

    @Test
    public void addToCartTest()
    {


        String createBody = "{\n" +
                "  \"variant_id\": \"17\",\n" +
                "  \"quantity\": 2\n" +
                "}";
        Response response = given()
                .headers(headers)
                .body(createBody)
                .post(helperMethods.getaddItemURL());
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(response.getStatusCode(),200);
        //check item count
        Assert.assertTrue(jsonPath.get("data.attributes.item_count").equals(2));


    }

    @Test()
    public void testViewCart()
    {


        Response response = given()
                            .headers(headers)
                            .get(helperMethods.getCartURL());
        JsonPath jsonPath = response.jsonPath();
        lineItemId = jsonPath.get("data.relationships.line_items.data[0].id").toString();
        //context.setAttribute("lineItem", lineitemId);

        Assert.assertEquals(response.getStatusCode(), 200);
        //Check total price
        Assert.assertEquals(jsonPath.get("data.attributes.display_item_total"),"$39.98");
        //check Item count
        Assert.assertTrue(jsonPath.get("data.attributes.item_count").equals(2));

    }

    @Test(dependsOnMethods = "testViewCart")
    public void testDeleteItem()
    {

        Response response = given()
                            .headers(headers)
                            .delete(helperMethods.getdeleteItemURL()+lineItemId);
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(jsonPath.get("data.attributes.item_count").equals(0));

    }

}
