package com.examp.three.model.Grievances_admin;

public class ReceivedUpdateResult implements java.io.Serializable {
    private static final long serialVersionUID = 6986476989819876033L;
    private int code;
    private String message;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
