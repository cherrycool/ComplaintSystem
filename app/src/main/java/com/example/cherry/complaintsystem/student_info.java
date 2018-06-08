package com.example.cherry.complaintsystem;

public class student_info {

    private String name;
    private String entry_no;
    private String branch;
    private String phone_no;
    private String year_join;
    private String hostel;
    private String room_no;
    private String floor;
    private String wing;


    public student_info(String name, String entry_no, String branch, String phone_no, String year_join, String hostel, String room_no, String floor, String wing){
        this.name = name;
        this.entry_no = entry_no;
        this.branch = branch;
        this.phone_no = phone_no;
        this.year_join = year_join;
        this.hostel = hostel;
        this.room_no = room_no;
        this.floor = floor;
        this.wing = wing;
    }

    public String getBranch() {
        return branch;
    }

    public String getEntry_no() {
        return entry_no;
    }

    public String getName() {
        return name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getHostel() {
        return hostel;
    }

    public String getRoom_no() {
        return room_no;
    }

    public String getYear_join(){
        return year_join;
    }

    public String getFloor() {
        return floor;
    }

    public String getWing() {
        return wing;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public void setEntry_no(String entry_no) {
        this.entry_no = entry_no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

}
