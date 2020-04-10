package com._98point6.droptoken.processors;

import com._98point6.droptoken.model.Game;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import javax.sql.DataSource;

import static com.ninja_squad.dbsetup.Operations.insertInto;

public class GameProcessor {

    private DataSource dataSource;
    private DbSetupTracker dbSetupTracker;

    public GameProcessor(DataSource dataSource, DbSetupTracker dbSetupTracker) {
        this.dataSource = dataSource;
        this.dbSetupTracker = dbSetupTracker;
    }

    public void saveGame(Game game, int size) {
        Operation operation = insertInto("Games")
                .columns("Game_ID", "State", "Size")
                .values(game.getId(), game.getState(), size)
                .build();
        
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }
}
