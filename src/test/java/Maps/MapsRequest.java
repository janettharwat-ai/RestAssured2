package Maps;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static PayLoads.MapsPayloads.addPlaceRequestBody;
import static PayLoads.MapsPayloads.updatePlaceRequestBody;
import static PayLoads.MapsPayloads.deletePlaceRequestBody;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.testng.Assert.assertEquals;

public class MapsRequest {

    String newAddress = "70 summer walk, USA";
    @Test
    public void testMapAPi()
    {
        RestAssured.baseURI = ("https://rahulshettyacademy.com/");
        String addPlaceResponse = given().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(addPlaceRequestBody())
                .when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200)
                .assertThat().body("scope",  equalTo("APP"))
                .extract().response().asString();
        // .log().body().extract().asString();
        System.out.println(addPlaceResponse);


         // Extract place_id from the response
        JsonPath js = new JsonPath(addPlaceResponse);
        String placeId= js.getString("place_id");
        System.out.println("Place ID: " + placeId);

        String newAddress = "70 summer walk, USA";

        //Update Place put request
        given().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(updatePlaceRequestBody(placeId,newAddress))
                .when().put("maps/api/place/update/json")
                .then().assertThat().statusCode(200)
                .assertThat().body("msg", equalTo("Address successfully updated"))
               // .log().all()
                .extract().response().asString();

        //Get Place to verify the update
        String  getPlaceResponse = given().queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .header("Content-Type", "application/json")
                .when().get("maps/api/place/get/json")
                .then().assertThat().statusCode(200)
                .body("address", equalTo(newAddress))
                .extract().response().asString();

                 System.out.println(getPlaceResponse);
                 js = new JsonPath(getPlaceResponse);
               String actualAddress =  js.getString("address");
             assertEquals(actualAddress, newAddress, "Address does not match");

        // Delete Place Request
        String deleteResponse = given().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(deletePlaceRequestBody(placeId))
                .when().post("maps/api/place/delete/json")
                .then().assertThat().statusCode(200)
                .assertThat().body("status", equalTo("OK"))
                .extract().response().asString();

                 System.out.println(deleteResponse);

                 // Request builder



    }
}
