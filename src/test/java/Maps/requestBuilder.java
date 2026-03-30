package Maps;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static PayLoads.MapsPayloads.addPlaceRequestBody;
import static PayLoads.MapsPayloads.updatePlaceRequestBody;
import static PayLoads.MapsPayloads.deletePlaceRequestBody;


    public class requestBuilder {

        String newAddress = "70 summer walk, USA";

        @Test
        public void testMapAPi() {

            //  Request Builder
            RequestSpecification reqSpec = new RequestSpecBuilder()
                    .setBaseUri("https://rahulshettyacademy.com/")
                    .addQueryParam("key", "qaclick123")
                    .addHeader("Content-Type", "application/json")
                    .build();

            // Add Place
            String addPlaceResponse =
                    given().spec(reqSpec)
                            .body(addPlaceRequestBody())
                            .when().post("maps/api/place/add/json")
                            .then().assertThat().statusCode(200)
                            .body("scope", equalTo("APP"))
                            .extract().response().asString();

            System.out.println(addPlaceResponse);

            // Extract place_id
            JsonPath js = new JsonPath(addPlaceResponse);
            String placeId = js.getString("place_id");
            System.out.println("Place ID: " + placeId);

            // Update Place
            given().spec(reqSpec)
                    .body(updatePlaceRequestBody(placeId, newAddress))
                    .when().put("maps/api/place/update/json")
                    .then().assertThat().statusCode(200)
                    .body("msg", equalTo("Address successfully updated"));

            // Get Place
            String getPlaceResponse =
                    given().spec(reqSpec)
                            .queryParam("place_id", placeId) // override / add extra param
                            .when().get("maps/api/place/get/json")
                            .then().assertThat().statusCode(200)
                            .body("address", equalTo(newAddress))
                            .extract().response().asString();

            System.out.println(getPlaceResponse);

            js = new JsonPath(getPlaceResponse);
            String actualAddress = js.getString("address");
            assertEquals(actualAddress, newAddress);

            //  Delete Place
            String deleteResponse =
                    given().spec(reqSpec)
                            .body(deletePlaceRequestBody(placeId))
                            .when().post("maps/api/place/delete/json")
                            .then().assertThat().statusCode(200)
                            .body("status", equalTo("OK"))
                            .extract().response().asString();

            System.out.println(deleteResponse);
        }
    }