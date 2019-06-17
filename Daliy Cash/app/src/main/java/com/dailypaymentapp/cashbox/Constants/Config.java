/**
 * Configuration File
 * Edit this data according to your Requirement
 *
 * @author DroidOXY
 */

package com.dailypaymentapp.cashbox.Constants;


import com.dailypaymentapp.cashbox.R;

public class Config {


    static String[] titles = {
            "Daily Check-In",
            "Instructions",
            "Watch Videos",
            "Click And Earn ",

            "Redeem",
            "Reffer Earning",
            "AboutActivity"};


    static String[] description = {"Open Daily and Earn .02 $",
            "How to Earn Points",
            "Watch Videos to Earn Points",
            "Click Ad  Earn Dollars Per Hour",

            "Withdraw Your Cash",
            "Reffer and Earn 10% Commision",
            "Advertise with Us"};


   public static int[] payout_images = {

            R.drawable.paypal,
            R.drawable.ic_bkash,
            R.drawable.mobile_recharge
    };

    //Titles for Redeem Activity
    public static String[] payout_titles = {

            "Paypal",
            "bKash",
            "Recharge"
    };

    //Description for Redeem Activity Titles
    public static String[] payout_description = {

            "840 TK = $10 Paypal",
            "105 TK = 105 BDT bKash",
            "12 TK = 12 BDT Recharge"
    };


    // Google Analytics OPTIONAL
    static String analytics_property_id = "UA-76982496-1";

    // Share text and link for Share Button
    static String share_text = "Hello, look what a Wonderful Dollar earning app that I found here:";
    static String share_link = "https://play.google.com/store/apps/details?id=com.ibcbinc.makemoneyonline";

    // APP RATING
    static String rate_later = "Perhaps Later";
    static String rate_never = "No Thanks";
    static String rate_yes = "Rate Now";
    static String rate_message = "We hope you enjoy using %1$s. Would you like to help us by rating us in the Store?";
    static String rate_title = "Enjoying our app?";

}