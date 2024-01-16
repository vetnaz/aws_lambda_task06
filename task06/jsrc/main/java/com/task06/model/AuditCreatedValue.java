package com.task06.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.task06.constant.LambdaConstant;

@DynamoDBTable(tableName = LambdaConstant.AUDIT_TABLE)
public class AuditCreatedValue {
    private String id;

    private String itemKey;

    private String modificationTime;

    private NewValue newValue;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "itemKey")
    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    @DynamoDBAttribute(attributeName = "modificationTime")
    public String getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }

    @DynamoDBAttribute(attributeName = "newValue")
    public NewValue getNewValue() {
        return newValue;
    }

    public void setNewValue(NewValue newValue) {
        this.newValue = newValue;
    }
}
