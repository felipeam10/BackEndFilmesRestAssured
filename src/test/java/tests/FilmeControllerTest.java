package tests;

import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import support.utils.BaseUrlSetup;
import support.utils.RestAssuredConfig;
import support.enums.Endpoint;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import io.restassured.response.*;

import java.util.List;
import java.util.Map;

public class FilmeControllerTest {

    @BeforeAll
    public static void setup() {
        RestAssuredConfig.setup("homolog.properties", "base.url");
    }

    @Test
    public void testConsultarFilmePorId_Success() {

        int codigoFilme = Integer.parseInt(BaseUrlSetup.getProperty("codigo.filme"));

        Response response =
        given()
            .pathParam("codigo", codigoFilme)
        .when()
            .get(Endpoint.FILME.getPath());

        assertEquals(200, response.getStatusCode());
        assertEquals(ContentType.JSON.toString(), response.getContentType());
        assertEquals(codigoFilme, response.jsonPath().getInt("codigo"));
        assertNotNull(response.jsonPath().getString("nome"));
        assertNotNull(response.jsonPath().getString("sinopse"));
        assertNotNull(response.jsonPath().getString("faixaEtaria"));
        assertNotNull(response.jsonPath().getString("genero"));
    }

    @Test
    public void testConsultarFilmePorId_NotFound() {

        int codigoFilmeInvalido = Integer.parseInt(BaseUrlSetup.getProperty("codigo.filmeInvalido"));

        Response response =
        given()
            .pathParam("codigo", codigoFilmeInvalido)
        .when()
            .get(Endpoint.FILME.getPath());

        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testConsultarTodosOsFilmes_Success() {

        Response response =
        given()
        .when()
            .get(Endpoint.FILMES.getPath());

        assertEquals(200, response.getStatusCode());
        assertEquals(ContentType.JSON.toString(), response.getContentType());
        List<?> filmes = response.jsonPath().getList("$");
        assertNotNull(filmes);
        assertTrue(filmes.size() > 0);

        Map<String, Object> primeiroFilme = response.jsonPath().getMap("[0]");
        assertNotNull(primeiroFilme.get("codigo"));
        assertNotNull(primeiroFilme.get("nome"));
        assertNotNull(primeiroFilme.get("sinopse"));
        assertNotNull(primeiroFilme.get("faixaEtaria"));
        assertNotNull(primeiroFilme.get("genero"));
    }

    @Test
    public void testConsultarTodosOsFilmes_InvalidEndpoint() {

        Response response =
        given()
        .when()
            .get(Endpoint.FILMES_INVALID.getPath());

        assertEquals(404, response.getStatusCode(), "Expected HTTP 404 Not Found");
    }

    @Test
    public void testCriarFilme_Success() {

        String filmeJson = BaseUrlSetup.getProperty("filme.success");

        Response response =
        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath());

        assertEquals(201, response.getStatusCode(), "Expected HTTP 201 Created");

        assertEquals(ContentType.JSON.toString(), response.getContentType(), "Expected JSON response");

        assertEquals(10, response.jsonPath().getInt("codigo"), "Expected movie code to be 10");
        assertEquals("Filme de Ação", response.jsonPath().getString("nome"), "Expected movie name to be 'Filme de Ação'");
        assertEquals("Ação", response.jsonPath().getString("genero"), "Expected movie genre to be 'Ação'");
        assertEquals("12", response.jsonPath().getString("faixaEtaria"), "Expected movie age rating to be '12'");
        assertEquals("Um filme emocionante de ação.", response.jsonPath().getString("sinopse"), "Expected movie synopsis to match");
    }

    @Test
    public void testDeletarFilmePorId_Success() {

        Response response =
        given()
            .pathParam("codigo", 10)
        .when()
            .delete(Endpoint.FILME.getPath());

        assertEquals(200, response.getStatusCode(), "Expected HTTP 200 OK");
    }

    @Test
    public void testCriarFilme_Conflict() {

        String filmeJson = BaseUrlSetup.getProperty("filme.conflict");

        Response response =
        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath());

        assertEquals(409, response.getStatusCode(), "Expected HTTP 409 Conflict");
        assertEquals(ContentType.JSON.toString(), response.getContentType(), "Expected JSON response");
        assertEquals("Filme já cadastrado!", response.jsonPath().getString("message"), "Expected conflict message to match");
    }

    @Test
    public void testCriarFilme_MissingFaixaEtaria() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.faixa.etaria");

        Response response =
        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath());

        assertEquals(400, response.getStatusCode(), "Expected HTTP 400 Bad Request");
        assertEquals(ContentType.JSON.toString(), response.getContentType(), "Expected JSON response");
        assertEquals("Faixa etária é obrigatória!", response.jsonPath().getString("message"), "Expected error message to match");
    }

    @Test
    public void testCriarFilme_MissingGenero() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.genero");

        Response response =
        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath());

        assertEquals(400, response.getStatusCode(), "Expected HTTP 400 Bad Request");
        assertEquals(ContentType.JSON.toString(), response.getContentType(), "Expected JSON response");
        assertEquals("Genêro é obrigatório!", response.jsonPath().getString("message"), "Expected error message to match");
    }

    @Test
    public void testCriarFilme_MissingNome() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.nome");

        Response response =
        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath());

        assertEquals(400, response.getStatusCode(), "Expected HTTP 400 Bad Request");
        assertEquals(ContentType.JSON.toString(), response.getContentType(), "Expected JSON response");
        assertEquals("Nome é obrigatório!", response.jsonPath().getString("message"), "Expected error message to match");
    }

    @Test
    public void testCriarFilme_MissingSinopse() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.sinopse");

        Response response =
        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath());

        assertEquals(400, response.getStatusCode(), "Expected HTTP 400 Bad Request");
        assertEquals(ContentType.JSON.toString(), response.getContentType(), "Expected JSON response");
        assertEquals("Sinopse é obrigatório!", response.jsonPath().getString("message"), "Expected error message to match");
    }

    @Test
    public void testDeletarFilmePorId_NotFound() {
        // Assuming a movie with codigo 999 does not exist in the database
        Response response =
        given()
            .pathParam("codigo", 999)
        .when()
            .delete(Endpoint.FILME.getPath());

        assertEquals(404, response.getStatusCode(), "Expected HTTP 404 Not Found");
        assertEquals(ContentType.JSON.toString(), response.getContentType(), "Expected JSON response");
        assertEquals("Not Found", response.jsonPath().getString("error"), "Expected error message to match");
    }

    @Test
    public void testEditarFilme_Success() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.success");

        Response response =
        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .put(Endpoint.FILME.getPath());

        assertEquals(200, response.getStatusCode(), "Expected HTTP 200 OK");
        assertEquals(ContentType.JSON.toString(), response.getContentType(), "Expected JSON response");
        assertEquals("Novo Nome do Filme", response.jsonPath().getString("nome"), "Expected movie name to match");
        assertEquals("Nova sinopse do filme", response.jsonPath().getString("sinopse"), "Expected movie synopsis to match");
        assertEquals("Ação", response.jsonPath().getString("genero"), "Expected movie genre to match");
        assertEquals("16+", response.jsonPath().getString("faixaEtaria"), "Expected movie age rating to match");
    }

    @Test
    public void testEditarFilme_NotFound() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.not.found");

        given()
            .pathParam("codigo", 999)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .put(Endpoint.FILME.getPath())
        .then()
            .statusCode(404) // Expect HTTP 404 Not Found
            .contentType("text/plain;charset=UTF-8") // Match the actual content type
            .body(equalTo("{\n" +
                    "    \"message\": \"Filme não encontrado\",\n" +
                    "}")); // Match the plain text response
    }

    @Test
    public void testEditarFilme_MissingFaixaEtaria() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.sem.faixa.etaria");

        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .put(Endpoint.FILME.getPath())
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
                .contentType("text/plain;charset=UTF-8") // Match the actual content type
                .body(equalTo("{\n" +
                        "    \"message\": \"Faixa etária é obrigatória\",\n" +
                        "}")); // Match the plain text response
    }

    @Test
    public void testEditarFilme_MissingGenero() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.sem.genero");

        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .put(Endpoint.FILME.getPath())
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
                .contentType("text/plain;charset=UTF-8") // Match the actual content type
                .body(equalTo("{\n" +
                        "    \"message\": \"Genêro é obrigatório\",\n" +
                        "}")); // Match the plain text response
    }

    @Test
    public void testEditarFilme_MissingNome() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.sem.nome");

        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .put(Endpoint.FILME.getPath())
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
                .contentType("text/plain;charset=UTF-8") // Match the actual content type
                .body(equalTo("{\n" +
                        "    \"message\": \"Nome é obrigatório\",\n" +
                        "}")); // Match the plain text response
    }

    @Test
    public void testEditarFilme_MissingSinopse() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.sem.sinopse");

        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .put(Endpoint.FILME.getPath())
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
                .contentType("text/plain;charset=UTF-8") // Match the actual content type
                .body(equalTo("{\n" +
                        "    \"message\": \"Sinopse é obrigatório\",\n" +
                        "}")); // Match the plain text response
    }

    @Test
    public void testEditarFilmeComPatch_Success() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.success");

        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .patch(Endpoint.PATCH.getPath())
        .then()
            .statusCode(200) // Expect HTTP 200 OK
            .contentType(ContentType.JSON)
            .body("nome", equalTo("Nome Atualizado"))
            .body("genero", equalTo("Ação"))
            .body("sinopse", equalTo("Sinopse Atualizada"))
            .body("faixaEtaria", equalTo("16+"));
    }

    @Test
    public void testEditarFilmeComPatch_PartialUpdate() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.partial.update");

        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .patch(Endpoint.PATCH.getPath())
        .then()
            .statusCode(200) // Expect HTTP 200 OK
            .contentType(ContentType.JSON)
            .body("nome", equalTo("Nome Parcialmente Atualizado"));
    }

    @Test
    public void testEditarFilmeComPatch_NotFound() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.not.found");

        given()
            .pathParam("codigo", 999)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .patch(Endpoint.PATCH.getPath())
        .then()
            .statusCode(500); // Expect HTTP 404 Not Found
    }

    @Test
    public void testEditarFilmeComPatch_NoFieldsToUpdate() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.no.fields.to.update");

        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .patch(Endpoint.PATCH.getPath())
        .then()
            .statusCode(200) // Expect HTTP 200 OK
            .contentType(ContentType.JSON);
    }

    @Test
    public void testEditarFilmeComPatch_UpdateGeneroField() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.update.genero.field");

        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .patch(Endpoint.PATCH.getPath())
        .then()
            .statusCode(200) // Expect HTTP 200 OK
            .contentType(ContentType.JSON)
            .body("genero", equalTo("Comédia")); // Validate the updated genero field
    }

    @Test
    public void testEditarFilmeComPatch_UpdateSinopseField() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.update.sinopse.field");

        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .patch(Endpoint.PATCH.getPath())
        .then()
            .statusCode(200) // Expect HTTP 200 OK
            .contentType(ContentType.JSON)
            .body("sinopse", equalTo("Uma nova sinopse para o filme.")); // Validate the updated sinopse field
    }

    @Test
    public void testEditarFilmeComPatch_UpdateFaixaEtariaField() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.update.faixa.etaria.field");

        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .patch(Endpoint.PATCH.getPath())
        .then()
            .statusCode(200) // Expect HTTP 200 OK
            .contentType(ContentType.JSON)
            .body("faixaEtaria", equalTo("40+")); // Validate the updated faixaEtaria field
    }

}
