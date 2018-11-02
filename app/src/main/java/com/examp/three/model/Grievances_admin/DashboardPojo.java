package com.examp.three.model.Grievances_admin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardPojo {

    @SerializedName("Received")
    @Expose
    private Integer received;
    @SerializedName("Process")
    @Expose
    private Integer process;
    @SerializedName("Completed")
    @Expose
    private Integer completed;
    @SerializedName("Total")
    @Expose
    private Integer total;

    public Integer getReceived() {
        return received;
    }

    public void setReceived(Integer received) {
        this.received = received;
    }

    public Integer getProcess() {
        return process;
    }

    public void setProcess(Integer process) {
        this.process = process;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
