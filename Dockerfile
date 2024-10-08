# Use the official OpenJDK image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the target directory
COPY target/receipt-processor-0.0.1-SNAPSHOT.jar /app/receipt-processor.jar

# Expose the port your application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "receipt-processor.jar"]
