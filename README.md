# Blogging-Platform

## Running the app
### Using Environment Variables

DB_USER=<db username>
DB_PASSWORD=<db password>

$ java -jar target/BlogPlatformAPI-0.0.1-SNAPSHOT.jar

### Alternatively using Java System Properties
$ java -DDB_USER=<db_user> -DDB_PASS=<db_password> -jar target/BlogPlatformAPI.jar
