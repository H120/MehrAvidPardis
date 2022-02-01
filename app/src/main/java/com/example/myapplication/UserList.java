package com.example.myapplication;

public class UserList implements Comparable{
    private String orderShow;
    private String value;
    private String title;
    private String placeholder;
    private String ModelShow;
    private String name;
    private String description;
    private String shouldScan;


    public String getorderShow() {
        return orderShow;
    }
    public void setorderShow(String orderShow) {
        this.orderShow = orderShow;
    }

    public String getvalue() {
        return value;
    }
    public void setvalue(String value) {
        this.value = value;
    }

    public String gettitle() {
        return title;
    }
    public void settitle(String title) {
        this.title = title;
    }

    public String getplaceholder() {
        return placeholder;
    }
    public void setplaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getModelShow() {
        return ModelShow;
    }
    public void setModelShow(String ModelShow) {
        this.ModelShow = ModelShow;
    }

    public String getname() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }

    public String getdescription() {
        return description;
    }
    public void setdescription(String description) {
        this.description = description;
    }

    public String getshouldScan() {
        return shouldScan;
    }
    public void setshouldScan(String shouldScan) {
        this.shouldScan = shouldScan;
    }

    @Override
    public int compareTo(Object o) {

            int compareordershow = Integer.parseInt(((UserList) o).getorderShow());

            return Integer.parseInt(this.orderShow)-compareordershow;
        }

}
