package com.examp.three.model.Grievances_admin;

public class FilterPojo {
    String priority,ward,street,catagory;

    public FilterPojo() {
    }

    public FilterPojo(String priority, String ward, String street) {
        this.priority = priority;
        this.ward = ward;
        this.street = street;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
