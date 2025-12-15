FROM openjdk:8-jdk-alpine

WORKDIR /app

# Install Python and dependencies
RUN apk add --no-cache python3 py3-pip

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy Python ML models and source
COPY src ./src
COPY models ./models

# Install Python packages
RUN pip3 install --no-cache-dir scikit-learn pandas numpy joblib

RUN ./mvnw clean package -DskipTests

EXPOSE 8083
CMD ["java", "-jar", "target/prediction-service-1.0.0.jar"]