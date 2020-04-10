## Creating DB Image

    cd db
    docker build -t droptoken-mysql .
    cd ..
    

## How to run the application using docker

    docker run -p3306:3306 -e MYSQL_ROOT_PASSWORD=appuser \
            -e MYSQL_USER=appuser -e MYSQL_PASSWORD=appuser \
            -e MYSQL_DATABASE=droptoken droptoken-mysql
    ./gradlew bootRun
    
## How to run the application using docker-compose    
    
    docker-compose up -d
    ./gradlew bootRun

The server runs on port 8080.