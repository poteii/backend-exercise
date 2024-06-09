# backend-exercise
This is exercise to create GET API

For building and running the application you need:
- JDK 17
- Maven 3

## Running the Application Locally
1. Execute the `main` method in `com.exercise.api.ApiApplication.java`.
2. Access the Swagger-UI at http://localhost:8080/swagger-ui/index.html.

## API Endpoints
1. `/api/job_data`
   - Get job data based on provided criteria. 
   - To filter by salary, use the following suffixes:
      - gte: Greater than or equal 
      - lte: Less than or equal 
      - equal: Equal
2. `/api/job_data/sorted`
   - Get all job data sorted by specified criteria.
3. `/api/job_data/filtered`
   - Get all job data, displaying only the fields specified in the criteria.
4. `/api/job_data`
   - Get all job data based on provided criteria.
