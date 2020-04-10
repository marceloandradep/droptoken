package com._98point6.droptoken.processors;

import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.model.Player;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import javax.sql.DataSource;

import static com.ninja_squad.dbsetup.Operations.insertInto;

public class PlayerProcessor {

    private DataSource dataSource;
    private DbSetupTracker dbSetupTracker;

    public PlayerProcessor(DataSource dataSource, DbSetupTracker dbSetupTracker) {
        this.dataSource = dataSource;
        this.dbSetupTracker = dbSetupTracker;
    }

    public void savePlayer(Game game, Player player) {
        Operation operation = insertInto("Players")
                .columns("Player_ID", "Game_ID")
                .values(player.getId(), game.getId())
                .build();
        
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }
}
