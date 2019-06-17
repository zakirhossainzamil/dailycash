package com.dailypaymentapp.cashbox.Models;

public class ReferTotalAccount {

    double total_balance;
    int total_referral;
    double total_referral_earning;


    public  ReferTotalAccount(){

    }

    public ReferTotalAccount(double total_balance, int total_referral, double total_referral_earning) {
        this.total_balance = total_balance;
        this.total_referral = total_referral;
        this.total_referral_earning = total_referral_earning;
    }

    public double getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(double total_balance) {
        this.total_balance = total_balance;
    }

    public int getTotal_referral() {
        return total_referral;
    }

    public void setTotal_referral(int total_referral) {
        this.total_referral = total_referral;
    }

    public double getTotal_referral_earning() {
        return total_referral_earning;
    }

    public void setTotal_referral_earning(double total_referral_earning) {
        this.total_referral_earning = total_referral_earning;
    }
}
