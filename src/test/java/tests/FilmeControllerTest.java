package tests;

import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import support.utils.BaseUrlSetup;
import support.utils.RestAssuredConfig;
import support.enums.Endpoint;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import io.restassured.response.*;
import support.utils.RestAssuredHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmeControllerTest {

    @BeforeAll
    public static void setup() {
        String environment = System.getProperty("env");
        if (environment == null || environment.isEmpty()) {
            throw new RuntimeException("Environment not specified. Please provide the 'env' system property (e.g., -Denv=dev or -Denv=homolog).");
        }
        RestAssuredConfig.setup(environment, "base.url");
    }

    @Test
    public void testConsultarFilmePorId_Success() {

        int codigoFilme = Integer.parseInt(BaseUrlSetup.getProperty("codigo.filme"));
        Response response = RestAssuredHelper.sendGetRequest(Endpoint.FILME.getPath(), "codigo", codigoFilme);
        RestAssuredHelper.assertResponse(response, 200, ContentType.JSON.toString());
        assertEquals(codigoFilme, response.jsonPath().getInt("codigo"));
        assertNotNull(response.jsonPath().getString("nome"));
        assertNotNull(response.jsonPath().getString("sinopse"));
        assertNotNull(response.jsonPath().getString("faixaEtaria"));
        assertNotNull(response.jsonPath().getString("genero"));
    }

    @Test
    public void testConsultarFilmePorId_NotFound() {

        int codigoFilmeInvalido = Integer.parseInt(BaseUrlSetup.getProperty("codigo.filmeInvalido"));
        Response response = RestAssuredHelper.sendGetRequest(Endpoint.FILME.getPath(), "codigo", codigoFilmeInvalido);
        RestAssuredHelper.assertResponse(response, 404, ContentType.JSON.toString());
    }

    @Test
    public void testConsultarTodosOsFilmes_Success() {

        Response response = RestAssuredHelper.sendGetRequest(Endpoint.FILMES.getPath());
        RestAssuredHelper.assertResponse(response, 200, ContentType.JSON.toString());
        List<?> filmes = response.jsonPath().getList("$");
        assertNotNull(filmes, "The list of movies should not be null");
        assertTrue(filmes.size() > 0, "The list of movies should not be empty");
        Map<String, Object> primeiroFilme = response.jsonPath().getMap("[0]");
        assertNotNull(primeiroFilme.get("codigo"), "The 'codigo' field should not be null");
        assertNotNull(primeiroFilme.get("nome"), "The 'nome' field should not be null");
        assertNotNull(primeiroFilme.get("sinopse"), "The 'sinopse' field should not be null");
        assertNotNull(primeiroFilme.get("faixaEtaria"), "The 'faixaEtaria' field should not be null");
        assertNotNull(primeiroFilme.get("genero"), "The 'genero' field should not be null");
    }

    @Test
    public void testConsultarTodosOsFilmes_InvalidEndpoint() {

        Response response = RestAssuredHelper.sendGetRequest(Endpoint.FILMES_INVALID.getPath());
        RestAssuredHelper.assertResponse(response, 404, null); // Content-Type is skipped for 404 responses
    }

    @Test
    public void testCriarFilme_Success() {

        String filmeJson = BaseUrlSetup.getProperty("filme.success");
        Response response = RestAssuredHelper.sendPostRequest(Endpoint.SALVAR.getPath(), filmeJson);
        RestAssuredHelper.assertResponse(response, 201, ContentType.JSON.toString());
        assertEquals(10, response.jsonPath().getInt("codigo"), "Expected movie code to be 10");
        assertEquals("Filme de Ação", response.jsonPath().getString("nome"), "Expected movie name to be 'Filme de Ação'");
        assertEquals("Ação", response.jsonPath().getString("genero"), "Expected movie genre to be 'Ação'");
        assertEquals("12", response.jsonPath().getString("faixaEtaria"), "Expected movie age rating to be '12'");
        assertEquals("Um filme emocionante de ação.", response.jsonPath().getString("sinopse"), "Expected movie synopsis to match");
    }

    @Test
    public void testDeletarFilmePorId_Success() {

        Response response = RestAssuredHelper.sendDeleteRequest(Endpoint.FILME.getPath(), 10);
        RestAssuredHelper.assertResponse(response, 200, null); // Content-Type is skipped for DELETE success
    }

    @Test
    public void testCriarFilme_Conflict() {

        String filmeJson = BaseUrlSetup.getProperty("filme.conflict");
        Response response = RestAssuredHelper.sendPostRequest(Endpoint.SALVAR.getPath(), filmeJson);
        RestAssuredHelper.assertResponse(response, 409, ContentType.JSON.toString());
        assertEquals("Filme já cadastrado!", response.jsonPath().getString("message"), "Expected conflict message to match");
    }

    @Test
    public void testCriarFilme_MissingFaixaEtaria() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.faixa.etaria");
        Response response = RestAssuredHelper.sendPostRequest(Endpoint.SALVAR.getPath(), filmeJson);
        RestAssuredHelper.assertResponse(response, 400, ContentType.JSON.toString());
        assertEquals("Faixa etária é obrigatória!", response.jsonPath().getString("message"), "Expected error message to match");
    }

    @Test
    public void testCriarFilme_MissingGenero() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.genero");
        Response response = RestAssuredHelper.sendPostRequest(Endpoint.SALVAR.getPath(), filmeJson);
        RestAssuredHelper.assertResponse(response, 400, ContentType.JSON.toString());
        assertEquals("Genêro é obrigatório!", response.jsonPath().getString("message"), "Expected error message to match");
    }

    @Test
    public void testCriarFilme_MissingNome() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.nome");
        Response response = RestAssuredHelper.sendPostRequest(Endpoint.SALVAR.getPath(), filmeJson);
        RestAssuredHelper.assertResponse(response, 400, ContentType.JSON.toString());
        assertEquals("Nome é obrigatório!", response.jsonPath().getString("message"), "Expected error message to match");
    }

    @Test
    public void testCriarFilme_MissingSinopse() {

        String filmeJson = BaseUrlSetup.getProperty("filme.sem.sinopse");
        Response response = RestAssuredHelper.sendPostRequest(Endpoint.SALVAR.getPath(), filmeJson);
        RestAssuredHelper.assertResponse(response, 400, ContentType.JSON.toString());
        assertEquals("Sinopse é obrigatório!", response.jsonPath().getString("message"), "Expected error message to match");
    }

    @Test
    public void testDeletarFilmePorId_NotFound() {
        // Assuming a movie with codigo 999 does not exist in the database
        Response response = RestAssuredHelper.sendDeleteRequest(Endpoint.FILME.getPath(), 999);
        RestAssuredHelper.assertResponse(response, 404, ContentType.JSON.toString());
        assertEquals("Not Found", response.jsonPath().getString("error"), "Expected error message to match");
    }

    @Test
    public void testEditarFilme_Success() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.success");
        Response response = RestAssuredHelper.sendPutRequest(Endpoint.FILME.getPath(), 1, filmeJson);
        RestAssuredHelper.assertResponse(response, 200, ContentType.JSON.toString());
        assertEquals("Novo Nome do Filme", response.jsonPath().getString("nome"), "Expected movie name to match");
        assertEquals("Nova sinopse do filme", response.jsonPath().getString("sinopse"), "Expected movie synopsis to match");
        assertEquals("Ação", response.jsonPath().getString("genero"), "Expected movie genre to match");
        assertEquals("16+", response.jsonPath().getString("faixaEtaria"), "Expected movie age rating to match");
    }

    @Test
    public void testEditarFilme_NotFound() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.not.found");
        Response response = RestAssuredHelper.sendPutRequest(Endpoint.FILME.getPath(), 999, filmeJson);
        RestAssuredHelper.assertResponse(response, 404, "text/plain;charset=UTF-8");
        String expectedBody = "{\n" +
                "    \"message\": \"Filme não encontrado\",\n" +
                "}";
        assertEquals(expectedBody, response.getBody().asString(), "Response body mismatch");
    }

    @Test
    public void testEditarFilme_MissingFaixaEtaria() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.sem.faixa.etaria");
        Response response = RestAssuredHelper.sendPutRequest(Endpoint.FILME.getPath(), 1, filmeJson);
        RestAssuredHelper.assertResponse(response, 400, "text/plain;charset=UTF-8");
        String expectedBody = "{\n" +
                "    \"message\": \"Faixa etária é obrigatória\",\n" +
                "}";
        assertEquals(expectedBody, response.getBody().asString(), "Response body mismatch");
    }

    @Test
    public void testEditarFilme_MissingGenero() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.sem.genero");
        Response response = RestAssuredHelper.sendPutRequest(Endpoint.FILME.getPath(), 1, filmeJson);
        RestAssuredHelper.assertResponse(response, 400, "text/plain;charset=UTF-8");
        String expectedBody = "{\n" +
                "    \"message\": \"Genêro é obrigatório\",\n" +
                "}";
        assertEquals(expectedBody, response.getBody().asString(), "Response body mismatch");
    }

    @Test
    public void testEditarFilme_MissingNome() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.sem.nome");
        Response response = RestAssuredHelper.sendPutRequest(Endpoint.FILME.getPath(), 1, filmeJson);
        RestAssuredHelper.assertResponse(response, 400, "text/plain;charset=UTF-8");
        String expectedBody = "{\n" +
                "    \"message\": \"Nome é obrigatório\",\n" +
                "}";
        assertEquals(expectedBody, response.getBody().asString(), "Response body mismatch");
    }

    @Test
    public void testEditarFilme_MissingSinopse() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.sem.sinopse");
        Response response = RestAssuredHelper.sendPutRequest(Endpoint.FILME.getPath(), 1, filmeJson);
        RestAssuredHelper.assertResponse(response, 400, "text/plain;charset=UTF-8");
        String expectedBody = "{\n" +
                "    \"message\": \"Sinopse é obrigatório\",\n" +
                "}";
        assertEquals(expectedBody, response.getBody().asString(), "Response body mismatch");
    }

    @Test
    public void testEditarFilmeComPatch_Success() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.success");
        Response response = RestAssuredHelper.sendPatchRequest(Endpoint.PATCH.getPath(), 1, filmeJson);
        RestAssuredHelper.assertResponse(response, 200, ContentType.JSON.toString());
        assertEquals("Nome Atualizado", response.jsonPath().getString("nome"), "Nome mismatch");
        assertEquals("Ação", response.jsonPath().getString("genero"), "Genero mismatch");
        assertEquals("Sinopse Atualizada", response.jsonPath().getString("sinopse"), "Sinopse mismatch");
        assertEquals("16+", response.jsonPath().getString("faixaEtaria"), "Faixa Etaria mismatch");
    }

    @Test
    public void testEditarFilmeComPatch_PartialUpdate() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.partial.update");
        Response response = RestAssuredHelper.sendPatchRequest(Endpoint.PATCH.getPath(), 1, filmeJson);
        RestAssuredHelper.assertResponse(response, 200, ContentType.JSON.toString());
        assertEquals("Nome Parcialmente Atualizado", response.jsonPath().getString("nome"), "Nome mismatch");
    }

    @Test
    public void testEditarFilmeComPatch_NotFound() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.not.found");
        Response response = RestAssuredHelper.sendPatchRequest(Endpoint.PATCH.getPath(), 999, filmeJson);
        RestAssuredHelper.assertResponse(response, 500, null);
    }

    @Test
    public void testEditarFilmeComPatch_NoFieldsToUpdate() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.no.fields.to.update");
        Response response = RestAssuredHelper.sendPatchRequest(Endpoint.PATCH.getPath(), 1, filmeJson);
        RestAssuredHelper.assertResponse(response, 200, ContentType.JSON.toString());
    }

    @Test
    public void testEditarFilmeComPatch_UpdateGeneroField() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.update.genero.field");
        Response response = RestAssuredHelper.sendPatchRequest(Endpoint.PATCH.getPath(), 1, filmeJson);
        RestAssuredHelper.assertResponse(response, 200, ContentType.JSON.toString());
        assertEquals("Comédia", response.jsonPath().getString("genero"), "Genero mismatch");
    }

    @Test
    public void testEditarFilmeComPatch_UpdateSinopseField() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.update.sinopse.field");
        Response response = RestAssuredHelper.sendPatchRequest(Endpoint.PATCH.getPath(), 1, filmeJson);
        RestAssuredHelper.assertResponse(response, 200, ContentType.JSON.toString());
        assertEquals("Uma nova sinopse para o filme.", response.jsonPath().getString("sinopse"), "Sinopse mismatch");
    }

    @Test
    public void testEditarFilmeComPatch_UpdateFaixaEtariaField() {

        String filmeJson = BaseUrlSetup.getProperty("editar.filme.com.path.update.faixa.etaria.field");
        Response response = RestAssuredHelper.sendPatchRequest(Endpoint.PATCH.getPath(), 1, filmeJson);
        RestAssuredHelper.assertResponse(response, 200, ContentType.JSON.toString());
        assertEquals("40+", response.jsonPath().getString("faixaEtaria"), "Faixa Etaria mismatch");
    }

}
