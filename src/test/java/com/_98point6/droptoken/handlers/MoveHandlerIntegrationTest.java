package com._98point6.droptoken.handlers;

import com._98point6.droptoken.exceptions.NotFoundException;
import com._98point6.droptoken.exceptions.PlayerOutOfTurnException;
import com._98point6.droptoken.handlers.dto.DropTokenDTO;
import com._98point6.droptoken.handlers.dto.GameRequestDTO;
import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.model.Move;
import com._98point6.droptoken.model.Player;
import com._98point6.droptoken.model.factories.GameFactory;
import com._98point6.droptoken.services.MoveService;
import com._98point6.droptoken.tests.HttpIntegrationBase;
import io.reactivex.Single;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

public class MoveHandlerIntegrationTest extends HttpIntegrationBase {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private GameFactory gameFactory;
    
    @Autowired
    private MoveHandler moveHandler;
    
    @MockBean
    private MoveService moveService;
    
    @Test
    public void when_getting_moves_with_no_filter_should_return_all_moves_in_the_response_body() throws JSONException {
        Game game = new Game(null, null, null);
        List<Move> moves = Arrays.asList(Move.dropTokenAt("player1", 1), Move.quit("player2"));
        
        when(moveService.getMoves(game.getId(), null, null)).thenReturn(Single.just(moves));

        String url = baseUrl + "/drop_token/" + game.getId() + "/moves";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String actual = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONArray expectedMoves = new JSONArray()
                .put(
                        new JSONObject()
                                .put("type", "MOVE")
                                .put("player", "player1")
                                .put("column", 1)
                )
                .put(
                        new JSONObject()
                                .put("type", "QUIT")
                                .put("player", "player2")
                );
        
        JSONObject expected = new JSONObject().put("moves", expectedMoves);

        JSONAssert.assertEquals(expected.toString(), actual, true);
    }

    @Test
    public void when_getting_moves_with_filter_should_return_filtered_moves_in_the_response_body() throws JSONException {
        Game game = new Game(null, null, null);
        List<Move> moves = Arrays.asList(Move.dropTokenAt("player1", 1), Move.quit("player2"));

        when(moveService.getMoves(game.getId(), 0, 1)).thenReturn(Single.just(moves));

        String url = baseUrl + "/drop_token/" + game.getId() + "/moves?start=0&until=1";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String actual = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONArray expectedMoves = new JSONArray()
                .put(
                        new JSONObject()
                                .put("type", "MOVE")
                                .put("player", "player1")
                                .put("column", 1)
                )
                .put(
                        new JSONObject()
                                .put("type", "QUIT")
                                .put("player", "player2")
                );

        JSONObject expected = new JSONObject().put("moves", expectedMoves);

        JSONAssert.assertEquals(expected.toString(), actual, true);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void when_getting_moves_and_no_moves_exist_should_return_not_found() throws JSONException {
        Game game = new Game(null, null, null);

        when(moveService.getMoves(game.getId(), 0, 1)).thenReturn(Single.error(NotFoundException::new));

        String url = baseUrl + "/drop_token/" + game.getId() + "/moves?start=0&until=1";
        restTemplate.getForEntity(url, String.class);
        
        fail();
    }

    @Test
    public void when_getting_move_should_return_move_in_the_response_body() throws JSONException {
        Game game = new Game(null, null, null);
        Move move = Move.dropTokenAt("player1", 1);

        when(moveService.getMove(game.getId(), 0)).thenReturn(Single.just(move));

        String url = baseUrl + "/drop_token/" + game.getId() + "/moves/0";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String actual = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONObject expected = 
                new JSONObject()
                        .put("type", "MOVE")
                        .put("player", "player1")
                        .put("column", 1);

        JSONAssert.assertEquals(expected.toString(), actual, true);
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void when_passing_invalid_move_number_should_return_error() throws JSONException {
        Game game = new Game(null, null, null);
        
        String url = baseUrl + "/drop_token/" + game.getId() + "/moves/abc";
        restTemplate.getForEntity(url, String.class);
        
        fail();
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void when_move_does_not_exist_should_return_not_found() throws JSONException {
        Game game = new Game(null, null, null);
        
        when(moveService.getMove(game.getId(), 0)).thenReturn(Single.error(NotFoundException::new));

        String url = baseUrl + "/drop_token/" + game.getId() + "/moves/0";
        restTemplate.getForEntity(url, String.class);
        
        fail();
    }

    @Test
    public void when_posting_a_move_should_create_it_and_return_the_move_url_in_the_response_body() throws JSONException {
        Game game = new Game(null, null, null);
        Player player = new Player("player1", (byte)1, true);
        
        DropTokenDTO dropTokenRequest = new DropTokenDTO(2);

        when(moveService.dropToken(game.getId(), player.getId(), dropTokenRequest.getColumn())).thenReturn(Single.just(0));

        String url = baseUrl + "/drop_token/" + game.getId() + "/" + player.getId();
        ResponseEntity<String> response = restTemplate.postForEntity(url, dropTokenRequest, String.class);
        String actual = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONObject expected =
                new JSONObject()
                        .put("move", game.getId() + "/moves/" + 0);

        JSONAssert.assertEquals(expected.toString(), actual, true);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void when_posting_a_move_and_game_or_player_is_not_found_should_return_not_found() throws JSONException {
        Game game = new Game(null, null, null);
        Player player = new Player("player1", (byte)1, true);

        DropTokenDTO dropTokenRequest = new DropTokenDTO(2);

        when(moveService.dropToken(game.getId(), player.getId(), dropTokenRequest.getColumn())).thenReturn(Single.error(NotFoundException::new));

        String url = baseUrl + "/drop_token/" + game.getId() + "/" + player.getId();
        restTemplate.postForEntity(url, dropTokenRequest, String.class);
        
        fail();
    }

    @Test(expected = HttpClientErrorException.Conflict.class)
    public void when_posting_a_move_and_player_is_out_of_turn_should_return_conflict() throws JSONException {
        Game game = new Game(null, null, null);
        Player player = new Player("player1", (byte)1, true);

        DropTokenDTO dropTokenRequest = new DropTokenDTO(2);

        when(moveService.dropToken(game.getId(), player.getId(), dropTokenRequest.getColumn())).thenReturn(Single.error(PlayerOutOfTurnException::new));

        String url = baseUrl + "/drop_token/" + game.getId() + "/" + player.getId();
        restTemplate.postForEntity(url, dropTokenRequest, String.class);

        fail();
    }
    
}
