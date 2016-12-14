package com.example.swapn.bookmyclass.models;

/**
 * Created by swapn on 12/8/2016.
 */

public class Department {
    public Long getDeptid() {
        return deptid;
    }

    public void setDeptid(Long deptid) {
        this.deptid = deptid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getDeptaddress() {
        return deptaddress;
    }

    public void setDeptaddress(String deptaddress) {
        this.deptaddress = deptaddress;
    }

    private Long deptid;
    private String deptname;
    private String deptaddress;
}
