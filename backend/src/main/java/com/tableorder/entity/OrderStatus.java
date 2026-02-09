package com.tableorder.entity;

public enum OrderStatus {
    PENDING, PREPARING, COMPLETED;

    public boolean canTransitionTo(OrderStatus next) {
        switch (this) {
            case PENDING: return next == PREPARING;
            case PREPARING: return next == COMPLETED;
            default: return false;
        }
    }
}
