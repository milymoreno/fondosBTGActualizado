#!/bin/sh

# Crear tabla Clientes en DynamoDB con _id como UUID generado automáticamente
aws dynamodb create-table \
    --table-name Clientes \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
        AttributeName=numeroIdentificacion,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --global-secondary-indexes \
        "[
            {
                \"IndexName\": \"NumeroIdentificacionIndex\",
                \"KeySchema\": [
                    {\"AttributeName\": \"numeroIdentificacion\", \"KeyType\": \"HASH\"}
                ],
                \"Projection\": {
                    \"ProjectionType\": \"ALL\"
                },
                \"ProvisionedThroughput\": {
                    \"ReadCapacityUnits\": 5,
                    \"WriteCapacityUnits\": 5
                }
            }
        ]" \
    --endpoint-url http://localhost:4566

	
sleep 2
	
aws dynamodb create-table \
    --table-name Fondos \
    --attribute-definitions \
        AttributeName=fondoId,AttributeType=S \
    --key-schema AttributeName=fondoId,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://localhost:4566 \
	
sleep 2
	
	
