package com.task06.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.task06.constant.LambdaConstant;

@DynamoDBTable(tableName = LambdaConstant.CONFIGURATION_TABLE)
public class Configuration {
    private String key;
    private int value;

    @DynamoDBHashKey(attributeName = "key")
    public String getKey() {
        return key;
    }

    public void setKey(String id) {
        this.key = id;
    }

    @DynamoDBAttribute(attributeName = "value")
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
