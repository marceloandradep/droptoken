DROP TABLE IF EXISTS Moves;
DROP TABLE IF EXISTS Players;
DROP TABLE IF EXISTS Games;

CREATE TABLE Games (
    Game_ID VARCHAR(52) NOT NULL,
    State VARCHAR(15) NOT NULL,
    Size TINYINT(2) NOT NULL,
    PRIMARY KEY (Game_ID)
);

CREATE TABLE Players (
    Player_ID VARCHAR(30) NOT NULL,
    Game_ID VARCHAR(52) NOT NULL,
    PRIMARY KEY (Player_ID),
    CONSTRAINT FK_PlayersGames FOREIGN KEY (Game_ID)
      REFERENCES Games(Game_ID)
);

CREATE TABLE Moves (
    Move_ID BIGINT(20) NOT NULL AUTO_INCREMENT,
    MoveType VARCHAR(15) NOT NULL,
    Player VARCHAR(30) NOT NULL,
    MoveColumn TINYINT(2) NULL,
    Game_ID VARCHAR(52) NOT NULL,
    PRIMARY KEY (Move_ID),
    CONSTRAINT FK_MovesGames FOREIGN KEY (Game_ID)
      REFERENCES Games(Game_ID)
);