package com._98point6.droptoken.repositories.rowmappers;

import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.model.constants.GameState;
import com._98point6.droptoken.vertx.mysql.api.RxRowMapper;
import io.vertx.reactivex.sqlclient.Row;
import org.springframework.stereotype.Component;

@Component
public class GameRowMapper implements RxRowMapper<Game> {
    
    @Override
    public Game mapRow(Row row) {
        return new Game(row.getString("Game_ID"), GameState.valueOf(row.getString("State")));
    }
}
