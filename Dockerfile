FROM eclipse-temurin:17-jdk-focal
WORKDIR /ecommerce-app
COPY target/e-commerce-shop-0.0.1-SNAPSHOT.jar e-commerce-shop.jar
EXPOSE 8080
CMD ["java", "-jar", "/e-commerce-shop.jar"]

