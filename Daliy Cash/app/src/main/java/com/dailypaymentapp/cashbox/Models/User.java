package com.dailypaymentapp.cashbox.Models;

public class User {


    private String name;
    private String phone_number;
    private String referral_code;

    private int is_active;
    private int isUseReferral;
    private String refer_code;
    private int isAcccountVerifyed;


    public User() {
    }

    public User(String name, String phone_number, String referral_code, int is_active, int isUseReferral, String refer_code, int isAcccountVerifyed) {
        this.name = name;
        this.phone_number = phone_number;
        this.referral_code = referral_code;
        this.is_active = is_active;
        this.isUseReferral = isUseReferral;
        this.refer_code = refer_code;
        this.isAcccountVerifyed = isAcccountVerifyed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getIsUseReferral() {
        return isUseReferral;
    }

    public void setIsUseReferral(int isUseReferral) {
        this.isUseReferral = isUseReferral;
    }

    public String getRefer_code() {
        return refer_code;
    }

    public void setRefer_code(String refer_code) {
        this.refer_code = refer_code;
    }

    public int getIsAcccountVerifyed() {
        return isAcccountVerifyed;
    }

    public void setIsAcccountVerifyed(int isAcccountVerifyed) {
        this.isAcccountVerifyed = isAcccountVerifyed;
    }
}
