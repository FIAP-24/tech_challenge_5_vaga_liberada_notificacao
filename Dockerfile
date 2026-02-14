FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /agenda
COPY . .
RUN mvn clean package -DskipTests

# Criando imagem jdk 21 slim
FROM amazoncorretto:21-alpine-jdk

WORKDIR /agenda

# Copiando o JAR que foi criado no builder stage
COPY --from=builder /agenda/target/*.jar agenda.jar

# Expor a porta
EXPOSE 8081

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "agenda.jar"]