package com.epam.esm.service;

public class SearchCriteria {

    public enum Operation {TAGS, PART}
    private Operation operation;
    private Object value;

    public SearchCriteria() {
    }

    public SearchCriteria(Operation operation, Object value) {
        this.operation = operation;
        this.value = value;
    }


    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
