package com.example.pethouseowner.Model;

import java.util.List;

public class Request {
    private String phone,adress,total,name;
    private String status;

    private List<Order> pros;

    public Request() {
    }

    public Request(String phone, String adress, String total, String name, List<Order> pros) {
        this.phone = phone;
        this.adress = adress;
        this.total = total;
        this.name = name;
        this.pros = pros;
        this.status="0";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getPros() {
        return pros;
    }

    public void setPros(List<Order> pros) {
        this.pros = pros;
    }
}
