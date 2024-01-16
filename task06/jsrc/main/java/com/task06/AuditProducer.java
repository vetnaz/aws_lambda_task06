package com.task06;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syndicate.deployment.annotations.events.DynamoDbTriggerEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.resources.DependsOn;
import com.syndicate.deployment.model.ResourceType;
import com.task06.constant.LambdaConstant;
import com.task06.dto.LambdaResponse;
import com.task06.model.AuditCreatedValue;
import com.task06.model.AuditUpdatedValue;
import com.task06.model.NewValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@LambdaHandler(lambdaName = "audit_producer",
        roleName = "audit_producer-role"
)
@DynamoDbTriggerEventSource(targetTable = "Configuration", batchSize = 1)
@DependsOn(name = "Configuration", resourceType = ResourceType.DYNAMODB_TABLE)
public class AuditProducer implements RequestHandler<DynamodbEvent, LambdaResponse> {
    private AmazonDynamoDB amazonDynamoDB;
    private static final String REGION = "eu-central-1";

    public LambdaResponse handleRequest(DynamodbEvent dynamodbEvent, Context context) {
        this.initDynamoDbClient();
        Gson gson = new GsonBuilder().create();
        LambdaLogger logger = context.getLogger();

        logger.log("EVENT: " + gson.toJson(dynamodbEvent));
        DynamodbEvent.DynamodbStreamRecord record = null;
        try {
            record = dynamodbEvent.getRecords().stream().findFirst().orElseThrow(Exception::new);
        } catch (Exception e) {
            LambdaResponse lambdaResponse = new LambdaResponse();
            lambdaResponse.setStatusCode(400);
            lambdaResponse.setMessage("Failed request");
            logger.log(Arrays.toString(e.getStackTrace()));
        }

        if (record.getEventName().equals("INSERT")) {
            putInsertEvent(record);
        } else if (record.getEventName().equals("MODIFY")) {
            putUpdateEvent(record);
        }

        LambdaResponse lambdaResponse = new LambdaResponse();
        lambdaResponse.setStatusCode(201);
        lambdaResponse.setMessage("Processed successfully");
        return lambdaResponse;
    }

    private void putUpdateEvent(DynamodbEvent.DynamodbStreamRecord record) {
        Map<String, AttributeValue> oldImage = record.getDynamodb().getNewImage();
        Map<String, AttributeValue> newImage = record.getDynamodb().getOldImage();

        AuditUpdatedValue auditUpdatedValue = new AuditUpdatedValue();
        auditUpdatedValue.setId(UUID.randomUUID().toString());
        auditUpdatedValue.setItemKey(newImage.get(LambdaConstant.KEY).getS());
        auditUpdatedValue.setModificationTime(getCurrentTime());
        auditUpdatedValue.setNewValue(newImage.get(LambdaConstant.VALUE).getS());
        auditUpdatedValue.setOldValue(oldImage.get(LambdaConstant.VALUE).getS());
        auditUpdatedValue.setUpdatedAttribute(LambdaConstant.VALUE);

        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        mapper.save(auditUpdatedValue);
    }

    private void putInsertEvent(DynamodbEvent.DynamodbStreamRecord record) {
        Map<String, AttributeValue> image = record.getDynamodb().getNewImage();
        NewValue newValue = new NewValue(image.get(LambdaConstant.KEY).getS(), image.get(LambdaConstant.VALUE).getS());

        AuditCreatedValue auditCreatedValue = new AuditCreatedValue();
        auditCreatedValue.setId(UUID.randomUUID().toString());
        auditCreatedValue.setItemKey(image.get(LambdaConstant.KEY).getS());
        auditCreatedValue.setModificationTime(getCurrentTime());
        auditCreatedValue.setNewValue(newValue);

        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        mapper.save(auditCreatedValue);
    }

    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

        return now.format(formatter);
    }


    private void initDynamoDbClient() {
        this.amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion(REGION)
                .build();
    }
}
