## About database integration

There is a branch called db-persistence which is a version of the application that persists the game state into a database. In that branch you can find the instructions to create the docker image of the database and to run it locally.

## How to run the application

This version of the application stores game states in memory. No database needed.

    ./gradlew bootRun

The server runs on port 8080.