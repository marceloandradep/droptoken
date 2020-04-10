package com._98point6.droptoken.repositories.rowmappers;

import com._98point6.droptoken.model.Move;
import com._98point6.droptoken.model.constants.MoveType;
import com._98point6.droptoken.vertx.mysql.api.RxRowMapper;
import io.vertx.reactivex.sqlclient.Row;
import org.springframework.stereotype.Component;

@Component
public class MoveRowMapper implements RxRowMapper<Move> {
    
    @Override
    public Move mapRow(Row row) {
        return new Move(
                row.getLong("Move_ID"),
                MoveType.valueOf(row.getString("MoveType")),
                row.getString("Player"),
                row.getInteger("MoveColumn"));
    }
}
