# Receipt Processor

A simple Java application that processes receipts and return points based on certain [rules](https://github.com/fetch-rewards/receipt-processor-challenge?tab=readme-ov-file#rules).

[Fetch Rewards Challenge](https://github.com/fetch-rewards/receipt-processor-challenge)

## Features

- Add and save receipts
- Maintain processing state for each receipt
- Award points for a given receipt based on the rules mentioned above.
- Simple command-line interface for testing
- Added asynchronous processing of receipts

## Technologies Used

- Java 11
- Spring Boot
- JUnit
- Mockito
- Docker

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- Docker (optional, for containerization)

### Running the Application Locally

1. Clone the repository:

   ```bash
   git clone https://github.com/ritvij-saxena/receipt-processor-challenge.git
   cd receipt-processor
   ```

2. Build the project using Maven
    ```bash
    mvn clean install
    ```   
3. Run the application
    ```bash
    mvn spring-boot:run
    ```
### Running tests
To run the tests, use the command
    ```bash
    mvn test
    ```

### Docker
#### Building the Docker Image
To build the Docker image, run the following command from the root of the project directory (where the Dockerfile is located):
```bash
docker build -t receipt-processor .
```

#### Running the Docker Container
After building the image, you can run the application in a Docker container using:
```bash
docker run -p 8080:8080 receipt-processor
```


### Endpoints
You can interact with the application via the following endpoints [(refer challenge for more details)](https://github.com/fetch-rewards/receipt-processor-challenge?tab=readme-ov-file#summary-of-api-specification):

- `POST /receipts: Add a new receipt`
- `GET /receipts/{id}/points: Retrieve points for a given receipt id`
- `GET /receipts/{id}/receipt: Retrieve a receipt for a given receipt id` 
  Note: (out-of-scope) (Added just for the sake of it)