
Please see the API documentation for available endpoints - http://localhost/swagger-ui.html


Running the project (Development Purposes)

1. Using docker-compose (to run the stack DB+API+Nginx)
The project is setup to run using docker-compose. 
$docker-compose up will bring the stack up and the API will be accessible at http://localhost/{endpoint}

2. Building API container (for API development)
Once you update the API code, inorder to test the changes you can build a new API container 
$docker-compose up --force-recreate --no-deps --build api-server &

Generating JAR artifact (Deployment Purposes)
1. mvn clean package -Plocal <br>
Application will run on port 8080 (http://localhost:8080/{endpoint})
JAR file generate at target\{gis-VERSION.jar}

2. Using maven docker container (not implemented)




CURL Examples

1. Filter request - Filter schools based on school category, type and gender composition
    ```shell script
    curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"type":["1AB"], "category": ["N"], "gender":["Mixed"]}' http://localhost:8080/schools/filter
    ```

2. Radial search (all) - List all schools within circular search area
    ```shell script
    curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"centerLatitude": 7.219584, "centerLongitude": 79.862550, "radius": 20.0}' http://localhost:8080/schools/search/radial
    ```
3. Radial search (with limit) - List all schools within circular search area

    ```shell script
   curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"centerLatitude": 7.219584, "centerLongitude": 79.862550, "radius": 20.0, "limit": 20}' http://localhost:8080/schools/search/radial
   ```
4. Radial search (distance to the center, descending order) 
    
    ```shell script
   curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"centerLatitude": 7.219584, "centerLongitude": 79.862550, "radius": 20.0,"limit": 10, "resultsAscending": false}' http://localhost:8080/schools/search/radial
   ```





