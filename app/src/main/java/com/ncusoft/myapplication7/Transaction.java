package com.ncusoft.myapplication7;


public class Transaction {
    public static final int TYPE_INCOME = 1;
    public static final int TYPE_EXPENSE = 2;

    private int id;
    private int type;
    private double amount;
    private String note;
    private long timestamp;

    public Transaction(int type, double amount, String note, long timestamp) {
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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