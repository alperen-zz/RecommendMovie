It is a machine learning tutorial using Apache Spark and Spring Boot. (Rest Services)

It reads the sample data from the cvs file at the project directory and, train the model based on the data. 

Then recommend movies to users based on user ID via Rest API. 

$ curl http://localhost:8080/recommendations/{user_id}

03/01/2025

- Upgraded from Spring-boot 2.0 to 3.0
- Upgraded from Java 11 to 17 (Apache Spark support java 17 for the moment)
- Added OpenAI integration