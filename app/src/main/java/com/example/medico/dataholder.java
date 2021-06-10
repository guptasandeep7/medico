package com.example.medico;

public class dataholder {
    String profession,number,address,date,name,gender,uri,patientId;

    public dataholder(String profession, String number, String address,String date,String name,String gender,String uri,String patientId) {
        this.profession = profession;
        this.number = number;
        this.address = address;
        this.date=date;
        this.name=name;
        this.gender=gender;
        this.uri=uri;
        this.patientId = patientId;

    }

    public dataholder(){

    }
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getDate() {
        return date;
    }

    public String getProfession() {
        return profession;
    }

    public String getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
