package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import support.enums.Endpoint;
import support.utils.RestAssuredConfig;
import support.utils.RestAssuredHelper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WiremockControllerTest {

    @BeforeAll
    public static void setup() {
        String environment = System.getProperty("env");
        if (environment == null || environment.isEmpty()) {
            throw new RuntimeException("Environment not specified. Please provide the 'env' system property (e.g., -Denv=dev or -Denv=homolog).");
        }
        RestAssuredConfig.setup(environment, "wiremock.base.url");
    }

    @Test
    public void testConsultarIdFilmeExterno() {

        Response response = RestAssuredHelper.sendGetRequest(Endpoint.FILME_EXTERNO.getPath(), "codigo", 1);
        RestAssuredHelper.assertResponse(response, 200, null); // Skip Content-Type assertion
        assertEquals("{\"id\":\"12345\"}", response.getBody().asString(), "Response body mismatch");
    }

    @Test
    public void testValidarHeaderWithValidHeader() {

        Response response = RestAssuredHelper.sendGetRequestWithHeader(Endpoint.VALIDAR_HEADER.getPath(),"optional-header","12345678");
        RestAssuredHelper.assertResponse(response, 200, null);
        assertEquals("{\"mensagem\":\"Header válido\"}", response.getBody().asString(), "Response body mismatch");
    }

    @Test
    public void testValidarHeaderWithInvalidHeader() {

        Response response = RestAssuredHelper.sendGetRequestWithHeader(Endpoint.VALIDAR_HEADER.getPath(),"optional-header","invalidHeader");
        RestAssuredHelper.assertResponse(response, 404, null); // Skip Content-Type assertion for 404
    }
}
