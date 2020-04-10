package com._98point6.droptoken.repositories;

import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.model.Move;
import com._98point6.droptoken.model.factories.GameFactory;
import com._98point6.droptoken.repositories.rowmappers.GameRowMapper;
import com._98point6.droptoken.repositories.rowmappers.MoveRowMapper;
import com._98point6.droptoken.vertx.mysql.api.RxMySQL;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class GameRepository {

    private static final String SELECT_GAMES_IN_PROGRESS = "SELECT Game_ID, State FROM Games WHERE State = 'IN_PROGRESS'";
    private static final String SELECT_GAME_BY_ID = "SELECT Game_ID, State, Size FROM Games WHERE Game_ID = ?";
    private static final String SELECT_PLAYERS_BY_GAME_ID = "SELECT Name FROM Players WHERE Game_ID = ?";
    private static final String SELECT_MOVES_BY_GAME_ID = "SELECT Move_ID, MoveType, Player, MoveColumn FROM Moves WHERE Game_ID = ? ORDER BY Move_ID";
    private static final String INSERT_INTO_GAMES = "INSERT INTO Games (Game_ID, State, Size) VALUES (?, ?, ?)";
    private static final String INSERT_INTO_PLAYERS = "INSERT INTO Players (Name, Game_ID) VALUES (?, ?)";
    private static final String INSERT_INTO_MOVES = "INSERT INTO Moves (MoveType, Player, MoveColumn, Game_ID) VALUES (?, ?, ?, ?)";

    @Autowired
    private GameRowMapper gameRowMapper;
    
    @Autowired
    private MoveRowMapper moveRowMapper;
    
    @Autowired
    private GameFactory gameFactory;

    @Autowired
    private RxMySQL rxMySQL;
    
    private HashMap<String, Game> games = new HashMap<>();
    
    public Single<Game> save(Game game) {
        return rxMySQL
                .insert(INSERT_INTO_GAMES, game.getId(), game.getState().toString(), game.getBoard().getSize())
                .flatMapCompletable(id -> savePlayers(game))
                .toSingle(() -> game);
    }
    
    private Completable savePlayers(Game game) {
        return Observable
                .fromArray(game.getPlayersIds())
                .flatMapSingle(playerId -> rxMySQL.insert(INSERT_INTO_PLAYERS, playerId, game.getId()))
                .lastOrError()
                .ignoreElement();
    }
    
    public Completable saveMove(Game game, Move move) {
        return rxMySQL
                .insert(INSERT_INTO_MOVES, move.getType().toString(), move.getPlayer(), move.getColumn(), game.getId())
                .ignoreElement();
    }
    
    public Single<List<Game>> getGamesInProgress() {
        return rxMySQL
                .selectMany(SELECT_GAMES_IN_PROGRESS, gameRowMapper)
                .toList();
    }
    
    public Single<Game> getGame(String gameId) {
        return rxMySQL
                .selectMany(SELECT_PLAYERS_BY_GAME_ID, row -> row.getString("Name"), gameId)
                .toList()
                .flatMap(players -> createGame(gameId, players))
                .flatMap(this::addMoves);
    }
    
    private Single<Game> createGame(String gameId, List<String> players) {
        return rxMySQL
                .selectOne(SELECT_GAME_BY_ID, row -> row.getInteger("Size"), gameId)
                .map(size -> gameFactory.createGame(gameId, players, size));
    }
    
    private Single<Game> addMoves(Game game) {
        return rxMySQL
                .selectMany(SELECT_MOVES_BY_GAME_ID, moveRowMapper, game.getId())
                .toList()
                .map(moves -> {
                    moves.forEach(game::applyMove);
                    return game;
                });
    }
    
}
