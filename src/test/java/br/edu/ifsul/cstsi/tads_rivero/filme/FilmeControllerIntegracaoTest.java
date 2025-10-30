package br.edu.ifsul.cstsi.tads_rivero.filme;

import br.edu.ifsul.cstsi.tads_rivero.BaseAPIIntegracaoTest;
import br.edu.ifsul.cstsi.tads_rivero.TadsRiveroApplication;
import br.edu.ifsul.cstsi.tads_rivero.usuario.AutenticacaoDTO;
import br.edu.ifsul.cstsi.tads_rivero.usuario.TokenJwtDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TadsRiveroApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FilmeControllerIntegracaoTest extends BaseAPIIntegracaoTest {

    private String tokenAdmin;
    private String tokenGerente;
    private String tokenUser;

    @BeforeEach
    public void setup() {
        // Obter tokens para os diferentes perfis
        var loginAdmin = new AutenticacaoDTO("admin@cinema.com", "123456");
        var responseAdmin = post("/login", loginAdmin, TokenJwtDTO.class);
        tokenAdmin = responseAdmin.getBody().token();

        var loginGerente = new AutenticacaoDTO("gerente@cinema.com", "123456");
        var responseGerente = post("/login", loginGerente, TokenJwtDTO.class);
        tokenGerente = responseGerente.getBody().token();

        var loginUser = new AutenticacaoDTO("user@cinema.com", "123456");
        var responseUser = post("/login", loginUser, TokenJwtDTO.class);
        tokenUser = responseUser.getBody().token();
    }

    // Métodos utilitários
    private ResponseEntity<FilmeDTOResponse> getFilme(String url) {
        return get(url, FilmeDTOResponse.class);
    }

    private ResponseEntity<FilmeDTOResponse> getFilme(String url, String token) {
        return get(url, FilmeDTOResponse.class, token);
    }

    private ResponseEntity<List<FilmeDTOResponse>> getFilmesList(String url) {
        var headers = getHeaders();
        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    @Test
    @DisplayName("Espera uma lista com 5 filmes")
    public void findAllEsperaUmaListaCom5Filmes() {
        // ACT
        var data = getFilmesList("/api/v1/filmes").getBody();

        // ASSERT
        assertNotNull(data);
        assertEquals(5, data.size());
    }

    @Test
    @DisplayName("Espera um filme por ID pesquisado e NotFound para ID inexistente")
    public void findByIdEsperaUmFilmePorIdPesquisadoENotFoundParaIdInexistente() {
        // ACT + ASSERT
        assertNotNull(getFilme("/api/v1/filmes/1").getBody());
        assertNotNull(getFilme("/api/v1/filmes/2").getBody());
        assertNotNull(getFilme("/api/v1/filmes/3").getBody());
        assertNotNull(getFilme("/api/v1/filmes/4").getBody());
        assertNotNull(getFilme("/api/v1/filmes/5").getBody());
        assertEquals(HttpStatus.NOT_FOUND, getFilme("/api/v1/filmes/100000").getStatusCode());
    }

    @Test
    @DisplayName("Espera filmes por título pesquisado")
    public void findByTituloEsperaFilmesPorTituloPesquisado() {
        // ACT + ASSERT
        assertEquals(1, getFilmesList("/api/v1/filmes/titulo/Matrix").getBody().size());
        assertEquals(1, getFilmesList("/api/v1/filmes/titulo/Senhor").getBody().size());
        assertEquals(HttpStatus.NO_CONTENT, getFilmesList("/api/v1/filmes/titulo/NaoExiste").getStatusCode());
    }

    @Test
    @DisplayName("Espera 201 Created ao inserir filme como ADMIN")
    public void testInsertEspera201CreatedComoAdmin() {
        // ARRANGE
        var filmeDTO = new FilmeDTOPost("Teste Filme", LocalTime.of(2, 30));

        // ACT
        var response = post("/api/v1/filmes", filmeDTO, FilmeDTOResponse.class, tokenAdmin);

        // ASSERT
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        var location = response.getHeaders().get("location").get(0);
        var filme = getFilme(location).getBody();
        assertNotNull(filme);
        assertEquals("Teste Filme", filme.titulo());
        assertEquals(LocalTime.of(2, 30), filme.duracao());

        // ACT - Deletar o filme criado
        delete(location, Void.class, tokenAdmin);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, getFilme(location).getStatusCode());
    }

    @Test
    @DisplayName("Espera 201 Created ao inserir filme como GERENTE")
    public void testInsertEspera201CreatedComoGerente() {
        // ARRANGE
        var filmeDTO = new FilmeDTOPost("Teste Gerente", LocalTime.of(1, 45));

        // ACT
        var response = post("/api/v1/filmes", filmeDTO, FilmeDTOResponse.class, tokenGerente);

        // ASSERT
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Teste Gerente", response.getBody().titulo());

        // Limpar
        var location = response.getHeaders().get("location").get(0);
        delete(location, Void.class, tokenAdmin);
    }

    @Test
    @DisplayName("Espera 403 Forbidden ao inserir filme como USER")
    public void testInsertEspera403ForbiddenComoUser() {
        // ARRANGE
        var filmeDTO = new FilmeDTOPost("Teste User", LocalTime.of(2, 0));

        // ACT
        var response = post("/api/v1/filmes", filmeDTO, FilmeDTOResponse.class, tokenUser);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @DisplayName("Espera 401 Unauthorized ao inserir filme sem token")
    public void testInsertEspera401UnauthorizedSemToken() {
        // ARRANGE
        var filmeDTO = new FilmeDTOPost("Teste Sem Auth", LocalTime.of(2, 0));

        // ACT
        var response = post("/api/v1/filmes", filmeDTO, FilmeDTOResponse.class);

        // ASSERT
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("Espera 200 OK ao atualizar filme como ADMIN")
    public void testUpdateEspera200OkComoAdmin() {
        // ARRANGE - Criar filme primeiro
        var filmeDTOPost = new FilmeDTOPost("Filme Original", LocalTime.of(2, 0));
        var responsePost = post("/api/v1/filmes", filmeDTOPost, FilmeDTOResponse.class, tokenAdmin);
        assertEquals(HttpStatus.CREATED, responsePost.getStatusCode());
        var location = responsePost.getHeaders().get("location").get(0);

        // Preparar DTO para PUT
        var filmeDTOPut = new FilmeDTOPut("Filme Modificado", LocalTime.of(3, 0));

        // ACT
        var responsePut = put(location, filmeDTOPut, FilmeDTOResponse.class, tokenAdmin);

        // ASSERT
        assertEquals(HttpStatus.OK, responsePut.getStatusCode());
        assertEquals("Filme Modificado", responsePut.getBody().titulo());
        assertEquals(LocalTime.of(3, 0), responsePut.getBody().duracao());

        // Limpar
        delete(location, Void.class, tokenAdmin);
    }

    @Test
    @DisplayName("Espera 403 Forbidden ao atualizar filme como USER")
    public void testUpdateEspera403ForbiddenComoUser() {
        // ARRANGE
        var filmeDTOPut = new FilmeDTOPut("Tentativa Update", LocalTime.of(2, 0));

        // ACT
        var response = put("/api/v1/filmes/1", filmeDTOPut, FilmeDTOResponse.class, tokenUser);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @DisplayName("Espera 200 OK ao deletar filme como ADMIN")
    public void testDeleteEspera200OkComoAdmin() {
        // ARRANGE - Criar filme primeiro
        var filmeDTO = new FilmeDTOPost("Filme Para Deletar", LocalTime.of(2, 0));
        var responsePost = post("/api/v1/filmes", filmeDTO, FilmeDTOResponse.class, tokenAdmin);
        var location = responsePost.getHeaders().get("location").get(0);

        // ACT
        var responseDelete = delete(location, Void.class, tokenAdmin);

        // ASSERT
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, getFilme(location).getStatusCode());
    }

    @Test
    @DisplayName("Espera 403 Forbidden ao deletar filme como GERENTE")
    public void testDeleteEspera403ForbiddenComoGerente() {
        // ACT
        var response = delete("/api/v1/filmes/1", Void.class, tokenGerente);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @DisplayName("Espera 403 Forbidden ao deletar filme como USER")
    public void testDeleteEspera403ForbiddenComoUser() {
        // ACT
        var response = delete("/api/v1/filmes/1", Void.class, tokenUser);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @DisplayName("Espera 404 NotFound ao buscar filme inexistente")
    public void testGetNotFoundEspera404NotFound() {
        // ARRANGE + ACT
        var response = getFilme("/api/v1/filmes/1100");

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Espera 400 BadRequest ao inserir filme com dados inválidos")
    public void testInsertEspera400BadRequestComDadosInvalidos() {
        // ARRANGE - Título vazio
        var filmeDTOInvalido = new FilmeDTOPost("", LocalTime.of(2, 0));

        // ACT
        var response = post("/api/v1/filmes", filmeDTOInvalido, String.class, tokenAdmin);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
