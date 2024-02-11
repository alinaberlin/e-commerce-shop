FROM eclipse-temurin:17-jdk-focal
RUN mkdir /ecommerce-app
WORKDIR /ecommerce-app
COPY target/e-commerce-shop-0.0.1-SNAPSHOT.jar e-commerce-shop.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "e-commerce-shop.jar"]

