package com._98point6.droptoken.repositories;

import com._98point6.droptoken.fixtures.GameFixtures;
import com._98point6.droptoken.fixtures.MoveFixtures;
import com._98point6.droptoken.fixtures.PlayerFixtures;
import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.model.Move;
import com._98point6.droptoken.model.Player;
import com._98point6.droptoken.model.constants.GameState;
import com._98point6.droptoken.model.factories.GameFactory;
import com._98point6.droptoken.processors.GameProcessor;
import com._98point6.droptoken.processors.MoveProcessor;
import com._98point6.droptoken.processors.PlayerProcessor;
import com._98point6.droptoken.tests.MySQLIntegrationBase;
import com.ninja_squad.dbsetup.DbSetupTracker;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.Table;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.db.api.Assertions.assertThat;

public class GameRepositoryIntegrationTest extends MySQLIntegrationBase {

    private static DbSetupTracker dbSetupTracker = new DbSetupTracker();
    
    @Autowired
    GameRepository gameRepository;
    
    @Autowired
    GameFactory gameFactory;
    
    private Table gamesTable;
    private Table playersTable;
    private Table movesTable;
    
    private GameProcessor gameProcessor;
    private PlayerProcessor playerProcessor;
    private MoveProcessor moveProcessor;
    
    @PostConstruct
    public void setup() {
        gamesTable = new Table(dataSource, "Games");
        playersTable = new Table(dataSource, "Players");
        movesTable = new Table(dataSource, "Moves");
        
        gameProcessor = new GameProcessor(dataSource, dbSetupTracker);
        playerProcessor = new PlayerProcessor(dataSource, dbSetupTracker);
        moveProcessor = new MoveProcessor(dataSource, dbSetupTracker);
    }
    
    @Test
    public void should_get_games_in_progress() {
        List<Game> games = GameFixtures.listOfGames();
        games.forEach(game -> gameProcessor.saveGame(game, 4));

        List<Game> gameList = gameRepository.getGamesInProgress().blockingGet();
        
        assertFalse(gameList.isEmpty());
        gameList.forEach(game -> assertFalse(game.isDone()));
    }

    @Test
    public void when_there_is_any_game() {
        List<Game> gameList = gameRepository.getGamesInProgress().blockingGet();

        assertTrue(gameList.isEmpty());
    }
    
    @Test
    public void should_get_game_state() {
        Game game = new Game(null, null, null);
        gameProcessor.saveGame(game, 4);
        
        List<Player> players = PlayerFixtures.listOfPlayers();
        players.forEach(player -> playerProcessor.savePlayer(game, player));
        
        List<Move> moves = MoveFixtures.winningSequence(players, 4);
        moves.forEach(move -> moveProcessor.saveMove(game, move));
        
        Game persisted = gameRepository.getGame(game.getId()).blockingGet();
        
        assertEquals(game.getId(), persisted.getId());
        assertEquals(GameState.DONE, persisted.getState());
    }
    
    @Test
    public void should_save_game() {
        Game game = gameFactory.createGame(Arrays.asList("player1", "player2"), 4);
        
        game.dropToken("player1", 0);
        game.quit("player2");

        game = gameRepository.save(game).blockingGet();

        assertThat(gamesTable)
                .hasNumberOfRows(1)
                .row()
                .value().isEqualTo(game.getId())
                .value().isEqualTo(game.getState().toString())
                .value().isEqualTo(game.getBoard().getSize());
        
        assertThat(playersTable)
                .hasNumberOfRows(2)
                .row()
                .value().isEqualTo("player1")
                .value().isEqualTo(game.getId())
                .row()
                .value().isEqualTo("player2")
                .value().isEqualTo(game.getId());

        assertThat(movesTable)
                .hasNumberOfRows(2)
                .row()
                .value().isEqualTo(1)
                .value().isEqualTo("MOVE")
                .value().isEqualTo("player1")
                .value().isEqualTo(0)
                .value().isEqualTo(game.getId())
                .row()
                .value().isEqualTo(2)
                .value().isEqualTo("QUIT")
                .value().isEqualTo("player2")
                .value().isNull()
                .value().isEqualTo(game.getId());
    }
    
    /*@Test
    public void should_save_a_department() {
        Department department = from(Department.class).gimme(DepartmentFixtures.NEW);
        
        department = gameR.save(department).blockingGet();

        assertThat(gamesTable)
                .hasNumberOfRows(1)
                .row()
                .value().isEqualTo(department.getId())
                .value().isEqualTo(department.getName());
    }
    
    @Test
    public void should_find_a_department_by_id() {
        Department department = 
                from(Department.class)
                        .uses(departmentProcessor)
                        .gimme(DepartmentFixtures.PERSISTED);
        
        Department persistedDepartment = gameR.findByID(department.getId()).blockingGet();

        assertEquals(department.getId(), persistedDepartment.getId());
        assertEquals(department.getName(), persistedDepartment.getName());
    }*/
    
}
