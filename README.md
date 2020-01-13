# Instructions:

* Build docker image for the application using the command: *mvn package docker:build* and then *run docker run -p 8080:8080 challenge*

There are 2 ways to get the list of tracks:
* Calling the api "http://localhost:8080/recommendations" on your browser and pass the parameters as query parametes.
* Calling the api http://localhost:8080/api/recommendations" if you have the spotify token to authenticate, the token must be passed on headers using the header name "Spotify-Token"
* The parameters must be passed as query parametes.
The acceptable parameters are : ?location=city_name or<br/> ?lat=latitude_coordinate&lon=longitude_coordinate. You also can limit the number of tracks that will be returned passing the parameter "limit".
  
* Example: <br/>
   GET http://localhost:8080/recommendation?location=campinas&limit=10 <br/>
   GET http://localhost:8080/recommendation?lat=-22.9&lon=-47.0
