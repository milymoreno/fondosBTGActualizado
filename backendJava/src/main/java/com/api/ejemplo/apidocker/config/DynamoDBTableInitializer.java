/*package com.api.ejemplo.apidocker.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;


import com.api.ejemplo.apidocker.servicio.NotificationService;


@Component
public class DynamoDBTableInitializer {

    private final AmazonDynamoDB amazonDynamoDB;
    private final String amazonDynamoDBEndpoint;
    private final String awsAccessKeyId;
    private final String awsSecretKey;
    private final String awsRegion;

    @Autowired
    public DynamoDBTableInitializer(
            AmazonDynamoDB amazonDynamoDB,
            @Value("${amazon.dynamodb.endpoint}") 
            String amazonDynamoDBEndpoint,
            @Value("${aws.accessKeyId}") 
            String awsAccessKeyId,
            @Value("${aws.secretKey}") 
            String awsSecretKey,
            @Value("${aws.region}") 
            String awsRegion) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.amazonDynamoDBEndpoint = amazonDynamoDBEndpoint;
        this.awsAccessKeyId = awsAccessKeyId;
        this.awsSecretKey = awsSecretKey;
        this.awsRegion = awsRegion;
    }

    @PostConstruct
    public void init() {
        createClientesTable();
        createFondosTable();       
        verifyEmail();
    }

    private void createClientesTable() {
        String tableName = "Clientes";
        try {
            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withAttributeDefinitions(
                            new AttributeDefinition("Id", ScalarAttributeType.S),
                            new AttributeDefinition("numeroIdentificacion", ScalarAttributeType.S))
                    .withKeySchema(new KeySchemaElement("Id", KeyType.HASH))
                    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L))
                    .withGlobalSecondaryIndexes(new GlobalSecondaryIndex()
                            .withIndexName("NumeroIdentificacionIndex")
                            .withKeySchema(new KeySchemaElement("numeroIdentificacion", KeyType.HASH))
                            .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                            .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L)));

            amazonDynamoDB.createTable(request);
            System.out.println("Tabla " + tableName + " creada correctamente.");
        } catch (ResourceInUseException e) {
            System.out.println("La tabla " + tableName + " ya existe.");
        }
    }

    private void createFondosTable() {
        String tableName = "Fondos";
        try {
            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withAttributeDefinitions(new AttributeDefinition("fondoId", ScalarAttributeType.S))
                    .withKeySchema(new KeySchemaElement("fondoId", KeyType.HASH))
                    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

            amazonDynamoDB.createTable(request);
            System.out.println("Tabla " + tableName + " creada correctamente.");
        } catch (ResourceInUseException e) {
            System.out.println("La tabla " + tableName + " ya existe.");
        }
    }  

    
}*/

package com.api.ejemplo.apidocker.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.api.ejemplo.apidocker.servicio.NotificationService;

@Component
public class DynamoDBTableInitializer {

    private final AmazonDynamoDB amazonDynamoDB;
    private final String amazonDynamoDBEndpoint;
    private final String awsAccessKeyId;
    private final String awsSecretKey;
    private final String awsRegion;
    private final String emailAddress;
    private final NotificationService notificationService;

    @Autowired
    public DynamoDBTableInitializer(
            AmazonDynamoDB amazonDynamoDB,
            @Value("${amazon.dynamodb.endpoint}")             
            String amazonDynamoDBEndpoint,
            @Value("${aws.accessKeyId}") 
            String awsAccessKeyId,
            @Value("${aws.secretKey}") 
            String awsSecretKey,
            @Value("${aws.region}") 
            String awsRegion,
            @Value("${aws.ses.from-email}") 
            String emailAddress,
            NotificationService notificationService) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.amazonDynamoDBEndpoint = amazonDynamoDBEndpoint;
        this.awsAccessKeyId = awsAccessKeyId;
        this.awsSecretKey = awsSecretKey;
        this.awsRegion = awsRegion;
        this.emailAddress = emailAddress;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {
        createClientesTable();
        createFondosTable();       
        verifyEmail();
    }

    private void createClientesTable() {
        String tableName = "Clientes";
        try {
            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withAttributeDefinitions(
                            new AttributeDefinition("id", ScalarAttributeType.S),
                            new AttributeDefinition("numeroIdentificacion", ScalarAttributeType.S))
                    .withKeySchema(new KeySchemaElement("id", KeyType.HASH))
                    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L))
                    .withGlobalSecondaryIndexes(new GlobalSecondaryIndex()
                            .withIndexName("NumeroIdentificacionIndex")
                            .withKeySchema(new KeySchemaElement("numeroIdentificacion", KeyType.HASH))
                            .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                            .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L)));

            amazonDynamoDB.createTable(request);
            System.out.println("Tabla " + tableName + " creada correctamente.");
        } catch (ResourceInUseException e) {
            System.out.println("La tabla " + tableName + " ya existe.");
        }
    }

    private void createFondosTable() {
        String tableName = "Fondos";
        try {
            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withAttributeDefinitions(new AttributeDefinition("fondoId", ScalarAttributeType.S))
                    .withKeySchema(new KeySchemaElement("fondoId", KeyType.HASH))
                    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

            amazonDynamoDB.createTable(request);
            System.out.println("Tabla " + tableName + " creada correctamente.");
        } catch (ResourceInUseException e) {
            System.out.println("La tabla " + tableName + " ya existe.");
        }
    }

    private void verifyEmail() {
        notificationService.verifyEmailIdentity(emailAddress);
    }
}

