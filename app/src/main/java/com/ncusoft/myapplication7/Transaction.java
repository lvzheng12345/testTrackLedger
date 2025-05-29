package com.ncusoft.myapplication7;

import java.math.BigDecimal;

public class Transaction {
    public static final int TYPE_INCOME = 1;
    public static final int TYPE_EXPENSE = 2;

    private int id;
    private int type;
    private BigDecimal amount;
    private String note;
    private long timestamp;

    public Transaction(int type, BigDecimal amount, String note, long timestamp) {
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isIncome() {
        return type == TYPE_INCOME;
    }
}