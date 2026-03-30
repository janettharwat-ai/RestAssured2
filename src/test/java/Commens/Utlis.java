package Commens;

import io.restassured.path.json.JsonPath;

public class Utlis {

    // This method takes a raw JSON response as a string and coverts it into a JsonPath object for easy querying

    public static JsonPath rawToJson(String response )
    {
        JsonPath js = new JsonPath(response);
        return js;

    }
}
