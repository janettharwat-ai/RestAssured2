package Maps;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static PayLoads.MapsPayloads.addPlaceRequestBody;
import static PayLoads.MapsPayloads.updatePlaceRequestBody;
import static PayLoads.MapsPayloads.deletePlaceRequestBody;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

public class specBuilders {

    String newAddress = "70 summer walk, USA";
    RequestSpecification reqSpec;

    @BeforeClass
    public void setup() {
        // Build reusable Request Specification
        reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key", "qaclick123")
                .addHeader("Content-Type", "application/json")
                .build();
    }

    @Test
    public void testMapApiUsingSpec() {

        // Add Place
        String addPlaceResponse = given()
                .spec(reqSpec)
                .body(addPlaceRequestBody())
                .when().post("maps/api/place/add/json")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("scope", equalTo("APP"))
                .extract().response().asString();

        System.out.println("Add Place Response: " + addPlaceResponse);

        JsonPath js = new JsonPath(addPlaceResponse);
        String placeId = js.getString("place_id");
        System.out.println("Place ID: " + placeId);

        //  Update Place
        given()
                .spec(reqSpec)
                .body(updatePlaceRequestBody(placeId, newAddress))
                .when().put("maps/api/place/update/json")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("msg", equalTo("Address successfully updated"));

        // Get Place
        String getPlaceResponse = given()
                .spec(reqSpec)
                .queryParam("place_id", placeId)
                .when().get("maps/api/place/get/json")
                .then()
                .assertThat().statusCode(200)
                .body("address", equalTo(newAddress))
                .extract().response().asString();

        System.out.println("Get Place Response: " + getPlaceResponse);

        js = new JsonPath(getPlaceResponse);
        String actualAddress = js.getString("address");
        assertEquals(actualAddress, newAddress, "Address does not match");

        //  Delete Place
        String deleteResponse = given()
                .spec(reqSpec)
                .body(deletePlaceRequestBody(placeId))
                .when().post("maps/api/place/delete/json")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("status", equalTo("OK"))
                .extract().response().asString();

        System.out.println("Delete Place Response: " + deleteResponse);
    }
}