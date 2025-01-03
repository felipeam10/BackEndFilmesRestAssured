package tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import support.utils.BaseUrlSetup;
import support.utils.RestAssuredConfig;
import support.enums.Endpoint;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FilmeControllerTest {

    @BeforeAll
    public static void setup() {
        RestAssuredConfig.setup("homolog.properties", "base.url");
    }

    @Test
    public void testConsultarFilmePorId_Success() {

        int codigoFilme = Integer.parseInt(BaseUrlSetup.getProperty("codigo.filme"));

        given()
            .pathParam("codigo", codigoFilme)
        .when()
            .get(Endpoint.FILME.getPath())
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("codigo", equalTo(codigoFilme))
            .body("nome", notNullValue())
            .body("sinopse", notNullValue())
            .body("faixaEtaria", notNullValue())
            .body("genero", notNullValue());
    }

    @Test
    public void testConsultarFilmePorId_NotFound() {

        int codigoFilmeInvalido = Integer.parseInt(BaseUrlSetup.getProperty("codigo.filmeInvalido"));

        given()
            .pathParam("codigo", codigoFilmeInvalido)
        .when()
            .get(Endpoint.FILME.getPath())
        .then()
            .statusCode(404);
    }

    @Test
    public void testConsultarTodosOsFilmes_Success() {

        given()
        .when()
            .get(Endpoint.FILMES.getPath())
        .then()
            .statusCode(200) // Expect HTTP 200 OK
            .contentType(ContentType.JSON) // Expect JSON response
            .body("$", not(empty())) // Ensure the response is not empty
            .body("size()", greaterThan(0)) // Ensure at least one movie is returned
            .body("[0].codigo", notNullValue()) // Validate the first movie has an ID
            .body("[0].nome", notNullValue()) // Validate the first movie has a name
            .body("[0].sinopse", notNullValue()) // Validate the first movie has a description
            .body("[0].faixaEtaria", notNullValue()) // Validate the first movie has a description
            .body("[0].genero", notNullValue()) // Validate the first movie has a description
        ;
    }

    @Test
    public void testConsultarTodosOsFilmes_InvalidEndpoint() {

        // Test for an invalid endpoint
        given()
        .when()
            .get(Endpoint.FILMES_INVALID.getPath()) // Invalid endpoint
        .then()
            .statusCode(404); // Expect HTTP 404 Not Found
    }

    @Test
    public void testCriarFilme_Success() {

        String filmeJson = BaseUrlSetup.getProperty("filme.success");

        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath())
        .then()
            .statusCode(201) // Expect HTTP 201 Created
            .contentType(ContentType.JSON) // Expect JSON response
            .body("codigo", equalTo(10)) // Validate the returned movie's code
            .body("nome", equalTo("Filme de Ação")) // Validate the movie's name
            .body("genero", equalTo("Ação")) // Validate the movie's genre
            .body("faixaEtaria", equalTo("12")) // Validate the movie's age rating
            .body("sinopse", equalTo("Um filme emocionante de ação.")); // Validate the movie's synopsis
    }

    @Test
    public void testDeletarFilmePorId_Success() {

        given()
            .pathParam("codigo", 10)
        .when()
            .delete(Endpoint.FILME.getPath())
        .then()
            .statusCode(200); // Expect HTTP 204 No Content
    }

    @Test
    public void testCriarFilme_Conflict() {

        String filmeJson = BaseUrlSetup.getProperty("filme.conflict");

        // Assuming the movie with codigo 1 already exists
        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath())
        .then()
            .statusCode(409) // Expect HTTP 409 Conflict
            .contentType(ContentType.JSON) // Expect JSON response
            .body("message", equalTo("Filme já cadastrado!")); // Validate the conflict message
    }

    @Test
    public void testCriarFilme_MissingFaixaEtaria() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.faixa.etaria");

        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath())
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
            .contentType(ContentType.JSON) // Expect JSON response
            .body("message", equalTo("Faixa etária é obrigatória!")); // Validate the error message
    }

    @Test
    public void testCriarFilme_MissingGenero() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.genero");

        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath())
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
            .contentType(ContentType.JSON) // Expect JSON response
            .body("message", equalTo("Genêro é obrigatório!")); // Validate the error message
    }

    @Test
    public void testCriarFilme_MissingNome() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.nome");

        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath())
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
            .contentType(ContentType.JSON) // Expect JSON response
            .body("message", equalTo("Nome é obrigatório!")); // Validate the error message
    }

    @Test
    public void testCriarFilme_MissingSinopse() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.sinopse");

        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post(Endpoint.SALVAR.getPath())
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
            .contentType(ContentType.JSON) // Expect JSON response
            .body("message", equalTo("Sinopse é obrigatório!")); // Validate the error message
    }

    @Test
    public void testDeletarFilmePorId_NotFound() {
        // Assuming a movie with codigo 999 does not exist in the database
        given()
            .pathParam("codigo", 999)
        .when()
            .delete(Endpoint.FILME.getPath())
        .then()
            .statusCode(404) // Expect HTTP 404 Not Found
            .contentType(ContentType.JSON) // Expect JSON response
            .body("error", equalTo("Not Found")); // Validate the error message
    }

    @Test
    public void testEditarFilme_Success() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.success");

        given()
            .pathParam("codigo", 1)
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .put(Endpoint.FILME.getPath())
        .then()
            .statusCode(200) // Expect HTTP 200 OK
            .contentType(ContentType.JSON)
            .body("nome", equalTo("Novo Nome do Filme"))
            .body("sinopse", equalTo("Nova sinopse do filme"))
            .body("genero", equalTo("Ação"))
            .body("faixaEtaria", equalTo("16+"));
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
