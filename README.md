# open-weather-map

## this project uses open source https://openweathermap.org/api

- Takes geological location from https://openweathermap.org/api/geocoding-api
- Takes pollution values by city from https://openweathermap.org/api/air-pollution#history
- Tech Stack: Java Spring Boot, H2 Database, Redis

### endpoints:
- GET api/city -> makes an external API call to retrieve geological location by city name, creates a City object and saves to database

- GET api/pollution/result -> gets pollution values' daily average for each date from startDate to endDate in given city. Makes external API call if not in the database. 

** Currently implementing Redis for saving the last week's data.