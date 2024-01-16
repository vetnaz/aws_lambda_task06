package com.task06.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.task06.constant.LambdaConstant;

@DynamoDBTable(tableName = LambdaConstant.AUDIT_TABLE)
public class AuditUpdatedValue {
    private String id;

    private String itemKey;

    private String modificationTime;

    private String updatedAttribute;

    private int oldValue;

    private int newValue;

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

    @DynamoDBAttribute(attributeName = "updatedAttribute")
    public String getUpdatedAttribute() {
        return updatedAttribute;
    }

    public void setUpdatedAttribute(String updatedAttribute) {
        this.updatedAttribute = updatedAttribute;
    }

    @DynamoDBAttribute(attributeName = "oldValue")
    public int getOldValue() {
        return oldValue;
    }

    public void setOldValue(int oldValue) {
        this.oldValue = oldValue;
    }

    @DynamoDBAttribute(attributeName = "newValue")
    public int getNewValue() {
        return newValue;
    }

    public void setNewValue(int newValue) {
        this.newValue = newValue;
    }
}
