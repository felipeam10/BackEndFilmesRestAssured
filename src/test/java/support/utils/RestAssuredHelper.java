package support.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class RestAssuredHelper {

    public static void setup(String propertiesFile, String baseUrlKey) {
        RestAssuredConfig.setup(propertiesFile, baseUrlKey);
    }

    // Method for GET requests without path parameters
    public static Response sendGetRequest(String endpoint) {
        return given()
                .when()
                .get(endpoint);
    }

    // Overloaded method to handle single path parameter
    public static Response sendGetRequest(String endpoint, String paramKey, Object paramValue) {
        // Create a map for path parameters
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put(paramKey, paramValue);

        return sendGetRequest(endpoint, pathParams);
    }

    // Existing method for multiple path parameters
    public static Response sendGetRequest(String endpoint, Map<String, Object> pathParams) {
        return given()
                .pathParams(pathParams)
                .when()
                .get(endpoint);
    }

    public static Response sendPostRequest(String endpoint, String body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(endpoint);
    }

    public static Response sendPutRequest(String endpoint, Object pathParam, String body) {
        return given()
                .pathParam("codigo", pathParam)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(endpoint);
    }

    public static Response sendPatchRequest(String endpoint, Object pathParam, String body) {
        return given()
                .pathParam("codigo", pathParam)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch(endpoint);
    }

    public static Response sendDeleteRequest(String endpoint, Object pathParam) {
        return given()
                .pathParam("codigo", pathParam)
                .when()
                .delete(endpoint);
    }

    public static Response sendGetRequestWithHeader(String endpoint, String headerKey, String headerValue) {
        return given()
                .header(headerKey, headerValue)
                .when()
                .get(endpoint);
    }
    public static void assertResponse(Response response, int expectedStatusCode, String expectedContentType) {
        // Assert the status code
        assertEquals(expectedStatusCode, response.getStatusCode(), "Unexpected status code");

        // Handle cases where Content-Type is not present
        if (expectedContentType != null) {
            String actualContentType = response.getContentType();

            // If Content-Type is missing
            if (actualContentType == null || actualContentType.isEmpty()) {
                if (expectedStatusCode == 404) {
                    // Allow missing Content-Type for 404 responses
                    System.out.println("Content-Type is not asserted for 404 responses with no Content-Type.");
                } else {
                    // For other status codes, throw an error
                    throw new AssertionError("Expected Content-Type: " + expectedContentType + " but was: " + actualContentType);
                }
            } else {
                // Normalize Content-Type by removing charset (e.g., "application/json; charset=UTF-8")
                String actualContentTypeWithoutCharset = actualContentType.split(";")[0].trim();
                String expectedContentTypeWithoutCharset = expectedContentType.split(";")[0].trim();

                // Assert the normalized Content-Type
                assertEquals(expectedContentTypeWithoutCharset, actualContentTypeWithoutCharset, "Unexpected Content-Type");
            }
        }
    }
}
