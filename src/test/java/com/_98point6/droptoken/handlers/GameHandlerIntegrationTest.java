package com._98point6.droptoken.handlers;

import com._98point6.droptoken.exceptions.NotFoundException;
import com._98point6.droptoken.handlers.dto.GameRequestDTO;
import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.model.factories.GameFactory;
import com._98point6.droptoken.services.GameService;
import com._98point6.droptoken.tests.HttpIntegrationBase;
import io.reactivex.Single;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class GameHandlerIntegrationTest extends HttpIntegrationBase {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private GameFactory gameFactory;
    
    @Autowired
    private GameHandler gameHandler;
    
    @MockBean
    private GameService gameService;
    
    @Test
    public void when_getting_all_games_in_progress_should_return_all_ids_in_the_response_body() throws JSONException {
        List<Game> games = 
                Arrays.asList(
                        new Game(null, null, null), 
                        new Game(null, null, null));
        
        when(gameService.getGamesInProgress()).thenReturn(Single.just(games));

        String url = baseUrl + "/drop_token";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String actual = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONObject expected =
                new JSONObject()
                        .put("games", new JSONArray(Arrays.asList(games.get(0).getId(), games.get(1).getId())));

        JSONAssert.assertEquals(expected.toString(), actual, true);
    }
    
    @Test
    public void when_getting_in_progress_game_state_should_return_game_with_no_winner_field() throws JSONException {
        Game game = gameFactory.createGame(Arrays.asList("player1", "player2"), 4);
        
        when(gameService.getGameState(game.getId())).thenReturn(Single.just(game));

        String url = baseUrl + "/drop_token/" + game.getId();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String actual = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONObject expected =
                new JSONObject()
                        .put("players", new JSONArray(Arrays.asList("player1", "player2")))
                        .put("state", "IN_PROGRESS");

        JSONAssert.assertEquals(expected.toString(), actual, true);
    }

    @Test
    public void when_getting_finished_game_state_should_return_game_with_winner() throws JSONException {
        Game game = gameFactory.createGame(Arrays.asList("player1", "player2"), 4);
        
        game.dropToken("player1", 0);
        game.dropToken("player2", 1);
        
        game.dropToken("player1", 0);
        game.dropToken("player2", 1);

        game.dropToken("player1", 0);
        game.dropToken("player2", 1);

        game.dropToken("player1", 0);

        when(gameService.getGameState(game.getId())).thenReturn(Single.just(game));

        String url = baseUrl + "/drop_token/" + game.getId();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String actual = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONObject expected =
                new JSONObject()
                        .put("players", new JSONArray(Arrays.asList("player1", "player2")))
                        .put("state", "DONE")
                        .put("winner", "player1");

        JSONAssert.assertEquals(expected.toString(), actual, true);
    }

    @Test
    public void when_getting_draw_game_state_should_return_game_with_winner_null() throws JSONException {
        Game game = gameFactory.createGame(Arrays.asList("player1", "player2"), 4);

        game.dropToken("player1", 0);
        game.dropToken("player2", 2);
        game.dropToken("player1", 1);
        game.dropToken("player2", 3);

        game.dropToken("player1", 2);
        game.dropToken("player2", 0);
        game.dropToken("player1", 3);
        game.dropToken("player2", 1);

        game.dropToken("player1", 0);
        game.dropToken("player2", 2);
        game.dropToken("player1", 1);
        game.dropToken("player2", 3);

        game.dropToken("player1", 2);
        game.dropToken("player2", 0);
        game.dropToken("player1", 3);
        game.dropToken("player2", 1);

        when(gameService.getGameState(game.getId())).thenReturn(Single.just(game));

        String url = baseUrl + "/drop_token/" + game.getId();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String actual = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONObject expected =
                new JSONObject()
                        .put("players", new JSONArray(Arrays.asList("player1", "player2")))
                        .put("state", "DONE")
                        .put("winner", JSONObject.NULL);

        JSONAssert.assertEquals(expected.toString(), actual, true);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void when_game_does_not_exist_should_return_not_found() throws JSONException {
        Game game = new Game(null, null, null);
        
        when(gameService.getGameState(game.getId())).thenReturn(Single.error(new NotFoundException()));

        String url = baseUrl + "/drop_token/" + game.getId();
        restTemplate.getForEntity(url, String.class);

        fail();
    }

    @Test
    public void when_posting_a_valid_game_should_create_it_and_return_the_game_id_in_the_response_body() throws JSONException {
        Game game = new Game(null, null, null);
        GameRequestDTO gameRequest = new GameRequestDTO(Arrays.asList("player1", "player2"), 4, 4);
        
        when(gameService.createGame(gameRequest.getPlayers(), gameRequest.getColumns())).thenReturn(Single.just(game.getId()));

        String url = baseUrl + "/drop_token";
        ResponseEntity<String> response = restTemplate.postForEntity(url, gameRequest, String.class);
        String actual = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONObject expected =
                new JSONObject()
                        .put("gameId", game.getId());

        JSONAssert.assertEquals(expected.toString(), actual, true);
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void when_posting_a_invalid_game_should_return_error() throws JSONException {
        Game game = new Game(null, null, null);
        GameRequestDTO gameRequest = new GameRequestDTO();

        String url = baseUrl + "/drop_token";
        restTemplate.postForEntity(url, gameRequest, String.class);
        
        fail();
    }
    
}
