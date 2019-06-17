package com.dailypaymentapp.cashbox.Constants;

public class Constant {

    public static long TIME_IN_MILISECONDS =60*60*1000;

    private String ADMOB_APP_ID = "ca-app-pub-6176323068072734~7069027652";


    private String Interstitial_id1="ca-app-pub-6176323068072734/4745603299";
    private String Interstitial_id2="ca-app-pub-6176323068072734/4220534271";
    private String Interstitial_id3="ca-app-pub-6176323068072734/4745603299";
    private String rewardad_id="ca-app-pub-3940256099942544/5224354917";

    private String Banner_id="ca-app-pub-6176323068072734/3102893729";


 /*    private String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";
    private String Banner_id="ca-app-pub-3940256099942544/6300978111";
    private String Interstitial_id1="ca-app-pub-3940256099942544/1033173712";
    private String Interstitial_id2="ca-app-pub-3940256099942544/1033173712";
    private String Interstitial_id3="ca-app-pub-3940256099942544/1033173712";
    private String rewardad_id="ca-app-pub-3940256099942544/5224354917";
*/
    public Constant(){}

    public String getADMOB_APP_ID() {
        return ADMOB_APP_ID;
    }

    public String getInterstitial_id1() {
        return Interstitial_id1;
    }

    public String getInterstitial_id2() {
        return Interstitial_id2;
    }

    public String getInterstitial_id3() {
        return Interstitial_id3;
    }

    public String getBanner_id() {
        return Banner_id;
    }

    public String getRewardad_id() {
        return rewardad_id;
    }
}
