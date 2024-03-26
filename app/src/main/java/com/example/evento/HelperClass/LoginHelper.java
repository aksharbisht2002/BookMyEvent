package com.example.evento.HelperClass;

public class LoginHelper {

    public String c_id;
    public String name;
    public String phone;
    public String email;
    public String password;

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String profileImg;

    public LoginHelper(){ }
    public LoginHelper(String c_id,String name, String phone, String email, String password,String profileImg) {
        this.c_id = c_id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.profileImg = profileImg;
    }
    public String getC_id() {return c_id;}

    public void setC_id(String c_id)  {
        this.c_id = c_id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
