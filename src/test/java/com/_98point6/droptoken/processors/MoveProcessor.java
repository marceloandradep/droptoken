package com._98point6.droptoken.processors;

import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.model.Move;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import javax.sql.DataSource;

import static com.ninja_squad.dbsetup.Operations.insertInto;

public class MoveProcessor {

    private DataSource dataSource;
    private DbSetupTracker dbSetupTracker;

    public MoveProcessor(DataSource dataSource, DbSetupTracker dbSetupTracker) {
        this.dataSource = dataSource;
        this.dbSetupTracker = dbSetupTracker;
    }

    public void saveMove(Game game, Move move) {
        Operation operation = insertInto("Moves")
                .columns("MoveType", "Player", "MoveColumn", "Game_ID")
                .values(move.getType().toString(), move.getPlayer(), move.getColumn(), game.getId())
                .build();
        
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }
}
