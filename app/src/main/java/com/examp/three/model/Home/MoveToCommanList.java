package com.examp.three.model.Home;


public class MoveToCommanList {
    String userid;
    String iconNo;
    int count;

    public MoveToCommanList(String userid, String iconNo, int count) {
        this.userid = userid;
        this.iconNo = iconNo;
        this.count = count;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getIconNo() {
        return iconNo;
    }

    public void setIconNo(String iconNo) {
        this.iconNo = iconNo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
