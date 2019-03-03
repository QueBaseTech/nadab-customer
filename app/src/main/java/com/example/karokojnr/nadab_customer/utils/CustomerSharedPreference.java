package com.example.karokojnr.nadab_customer.utils;

public class CustomerSharedPreference {
    private String id;
    private String username;
    private String email;

    public String getUser_fullname() {
        return user_fullname;
    }

    private String user_fullname;
    private String tv_mobile;
    private String ivImage;

    public String getIvImage() {
        return ivImage;
    }

    public CustomerSharedPreference(String id, String username, String email, String user_fullname, String tv_mobile, String tv_profile) {
        this.id = id;
        this.username = username;
        this.user_fullname = user_fullname;
        this.email = email;
        this.tv_mobile = tv_mobile;
        this.ivImage = tv_profile;

    }



    public String getTv_mobile() {
        return tv_mobile;
    }


    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "HotelSharedPreference{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", user_fullname='" + user_fullname + '\'' +
                ", email='" + email + '\'' +
                ", tv_mobile='" + tv_mobile + '\'' +
                ", ivImage='" + ivImage + '\'' +
                '}';
    }
}
