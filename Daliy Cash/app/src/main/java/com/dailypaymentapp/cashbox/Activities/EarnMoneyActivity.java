package com.dailypaymentapp.cashbox.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypaymentapp.cashbox.BottomNavigationBehavior;
import com.dailypaymentapp.cashbox.BuildConfig;
import com.dailypaymentapp.cashbox.Constants.Constant;
import com.dailypaymentapp.cashbox.DarkModePrefManager;
import com.dailypaymentapp.cashbox.ForceUpdateAsync;
import com.dailypaymentapp.cashbox.R;
import com.dailypaymentapp.cashbox.Models.ReferTotalAccount;
import com.dailypaymentapp.cashbox.Models.Time;
import com.dailypaymentapp.cashbox.Models.Total_Account;
import com.dailypaymentapp.cashbox.Models.User;
import com.dailypaymentapp.cashbox.Utilities;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


import static com.dailypaymentapp.cashbox.Constants.Constant.TIME_IN_MILISECONDS;

public class EarnMoneyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Date currentDate;
    String presentDate;

    int impression = 0;

    int isAdClicked = 0;

    private long mEndIime = 0;
    private long timeLeftInMilis = TIME_IN_MILISECONDS;
    int isTimerRunning = 0;

    int todaysRefer = 0;
    double  todaysReferalBalance = 0.0d;

    Handler mHandler;
    Runnable mTicker;

    String UNIQUE_ID;

    DatabaseReference timeReference;
    DatabaseReference total_Account_Reference;

    DatabaseReference userReference;

    TextView impressionTextView, clickleftTextView, timerTextView;
    private RelativeLayout btnHideShadow;
    private TextView timerText;

    InterstitialAd mInterstitialAd, mInterstitialAd1, mInterstitialAd2;
    Button nextButton;

    double totalBalance = 0.0d, totalreferalBalance = 0.0d;
    int total_referral;

    int theif = 0;
    String refer_code;

    int invalid_watch = 0;
    int invalid_activity = 0;

    private AdView mAdView, mAdView1;

    public long timer = getRemainingDays();
    private CountDownTimer countDownTimer;

    Animation blink_animation;

    ProgressDialog progressDialog;

    NavigationView navigationView;
    View headerView;

    final int clickImpression=25;
    final int maxClick=6;
    /*TextView user_name,user_phone_number;
    TextView total_balance,totalreferral,total_referral_balance;*/

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    Intent intent=new Intent(getApplicationContext(), DashBoardActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigationwithdraw:

                    Intent intent1=new Intent(getApplicationContext(), OrderPaymentActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigationHome:
                    Intent intent4=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent4);
                    return true;
                case  R.id.navigatonearn:

                    return true;
                case  R.id.navigationMenu:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnmoney);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Earn Money");

        Constant constant=new Constant();

        blink_animation = AnimationUtils.loadAnimation(EarnMoneyActivity.this, R.anim.blink_anim);


        progressDialog = new ProgressDialog(EarnMoneyActivity.this);
        progressDialog.setTitle("Loading Info...");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();



        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView1 = findViewById(R.id.adView1);
        mAdView1.loadAd(adRequest);

        UNIQUE_ID = getDeviceUniqueID(EarnMoneyActivity.this);

        currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        presentDate = dateFormat.format(currentDate);

        MobileAds.initialize(this, constant.getADMOB_APP_ID());

        diaglog();

        nextButton = findViewById(R.id.nextButtonid);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd1 = new InterstitialAd(this);
        mInterstitialAd2 = new InterstitialAd(this);


        mInterstitialAd.setAdUnitId(constant.getInterstitial_id1());
        mInterstitialAd1.setAdUnitId(constant.getInterstitial_id2());
        mInterstitialAd2.setAdUnitId(constant.getInterstitial_id3());


        loadintersitialadd();



        mHandler = new Handler();
        mTicker = new Runnable() {
            public void run() {
                loadintersitialadd();
                mHandler.postDelayed(mTicker, 5000);
            }
        };
        mTicker.run();

        interstitialAdsListeners();

        if (Utilities.isNetworkAvailable(EarnMoneyActivity.this)) {


            timeReference = FirebaseDatabase.getInstance().getReference("Time");
            timeReference.keepSynced(true);


            userReference = FirebaseDatabase.getInstance().getReference("User");
            userReference.keepSynced(true);

            total_Account_Reference = FirebaseDatabase.getInstance().getReference("Total_Account");
            total_Account_Reference.keepSynced(true);


            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.child(UNIQUE_ID).getValue(User.class);
                    if (user != null) {
                        refer_code = user.getRefer_code();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            total_Account_Reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Total_Account t_acount = dataSnapshot.child(UNIQUE_ID).getValue(Total_Account.class);
                    if (t_acount != null) {

                        totalBalance = t_acount.getTotal_balance();
                        totalreferalBalance = t_acount.getTotal_referral_earning();
                        total_referral = t_acount.getTotal_referral();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            timeReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Time time = dataSnapshot.child(UNIQUE_ID).child(presentDate).getValue(Time.class);
                    if (time != null) {

                        progressDialog.dismiss();

                        impression = time.getImpression();
                        isAdClicked = time.getIsAdClicked();

                        invalid_activity = time.getInvalid_activity();

                        timerTextView.setText("Invalid Activity : " + String.valueOf(invalid_activity) + "  Max - 4");
                        impressionTextView.setText(String.valueOf(impression) + "");
                        clickleftTextView.setText(String.valueOf(maxClick - isAdClicked) + "");

                        timeLeftInMilis = time.getLast_time();
                        isTimerRunning = time.getIsTimerRunning();

                        if (isAdClicked >= maxClick) {

                            btnHideShadow.setVisibility(View.VISIBLE);
                            timerText.setVisibility(View.VISIBLE);
                            UpdateCountdownTextIntervalDay();
                            IntervalDay();

                        }
                        if (isTimerRunning == 1) {
                            mEndIime = time.getEnd_time();
                            timeLeftInMilis = mEndIime - System.currentTimeMillis();

                            if (timeLeftInMilis < 0) {
                                timerText.setVisibility(View.INVISIBLE);
                                btnHideShadow.setVisibility(View.INVISIBLE);
                                isTimerRunning = 0;
                                timeLeftInMilis = TIME_IN_MILISECONDS;
                                mEndIime = 0;
                                invalid_activity = 0;

                                Time time1 = new Time(timeLeftInMilis, 0, 0, isAdClicked, 0, 0);
                                timeReference.child(UNIQUE_ID).child(presentDate).setValue(time1);


                            } else {

                                btnHideShadow.setVisibility(View.VISIBLE);
                                timerText.setVisibility(View.VISIBLE);
                                UpdateCountdownText();
                                startTimer();


                            }
                        } else if (isTimerRunning == 2) {

                            mEndIime = time.getEnd_time();
                            timeLeftInMilis = mEndIime - System.currentTimeMillis();


                            if (timeLeftInMilis < 0) {
                                timerText.setVisibility(View.INVISIBLE);
                                btnHideShadow.setVisibility(View.INVISIBLE);
                                isTimerRunning = 0;
                                timeLeftInMilis = TIME_IN_MILISECONDS;
                                mEndIime = 0;
                                isAdClicked = 0;
                                invalid_activity = 0;


                                Time time1 = new Time(TIME_IN_MILISECONDS, 0, 0, 0,  0, 0);
                                timeReference.child(UNIQUE_ID).child(presentDate).setValue(time1);


                            } else {

                                btnHideShadow.setVisibility(View.VISIBLE);
                                timerText.setVisibility(View.VISIBLE);
                                UpdateCountdownTextIntervalDay();
                                IntervalDay();

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    progressDialog.dismiss();
                }
            });

        }
        else {
            progressDialog.dismiss();
            new android.app.AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("Please connect with internet and try again")
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .setPositiveButton("Okay!!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else if (mInterstitialAd1.isLoaded()) {
                    mInterstitialAd1.show();
                }
                else if (mInterstitialAd2.isLoaded()) {
                    mInterstitialAd2.show();
                } else {
                    loadintersitialadd();
                    Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                }
                loadintersitialadd();

            }
        });


        impressionTextView = (TextView) findViewById(R.id.impressionTextViewId);
        clickleftTextView = (TextView) findViewById(R.id.clickLeftTextViewId);
        timerTextView = (TextView) findViewById(R.id.invalidtextViewId);

        timerText = findViewById(R.id.timerText);
        btnHideShadow = findViewById(R.id.btnHideShadow);

        impressionTextView.setText(String.valueOf(impression) + "");
        clickleftTextView.setText(String.valueOf(maxClick - isAdClicked) + "");


        impressionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadintersitialadd();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

      //  bottomNavigationView.setSelectedItemId(R.id.navigationSearch);
       // setprofile();


    }
    private boolean checkVPN() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnectedOrConnecting();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            Time time = new Time(timeLeftInMilis, mEndIime, isTimerRunning, isAdClicked, impression, invalid_activity);
            timeReference.child(UNIQUE_ID).child(presentDate).setValue(time);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent1 =new Intent(getApplicationContext(),DashBoardActivity.class);
            startActivity(intent1);

        } else if (id == R.id.nav_earn_money) {



        } else if (id == R.id.nav_withdraw) {
            Intent intent =new Intent(getApplicationContext(), OrderPaymentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_withdraw_history) {

        } else if (id == R.id.nav_active_account) {
            //activeAccount();
        }  else if (id == R.id.nav_share) {
            Intent referralIntent=new Intent(getApplicationContext(), ReferralActivity.class);
            startActivity(referralIntent);

        } else if (id == R.id.nav_dark_mode) {

            DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
            darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refferCommision() {

        final DatabaseReference totalRef = FirebaseDatabase.getInstance().getReference("Total_Account");

        totalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                ReferTotalAccount referTotalAccount = dataSnapshot.child(refer_code).getValue(ReferTotalAccount.class);

                if (referTotalAccount != null) {

                    double tb = referTotalAccount.getTotal_balance();
                    int tr = referTotalAccount.getTotal_referral();
                    double tre = referTotalAccount.getTotal_referral_earning();

                    tre = tre + 0.3;

                    ReferTotalAccount referTotalAccount1 = new ReferTotalAccount(tb, tr, tre);
                    totalRef.child(refer_code).setValue(referTotalAccount1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void loadintersitialadd() {

        if (!mInterstitialAd.isLoaded()) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
        if (!mInterstitialAd1.isLoaded()) {
            mInterstitialAd1.loadAd(new AdRequest.Builder().build());
        }
        if (!mInterstitialAd2.isLoaded()) {
            mInterstitialAd2.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        ForceUpdateAsync forceUpdateAsync = new ForceUpdateAsync(BuildConfig.VERSION_NAME, getApplicationContext());

        if (checkVPN()) {
           Intent I=new Intent(EarnMoneyActivity.this,VpnConncetedActivity.class);
           startActivity(I);
        }



        if (theif >= 1 || invalid_activity > 4) {

            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("User").child(UNIQUE_ID);
            databaseReference1.child("is_active").setValue(0);
            databaseReference1.child("theif").setValue(+1);

            finish();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (theif >= 1 || invalid_activity > 4) {

            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("User").child(UNIQUE_ID);
            databaseReference1.child("is_active").setValue(0);
            databaseReference1.child("theif").setValue(+1);

            finish();
        }

    }

    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id;
        device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    private void UpdateCountdownText() {

        int hours = (int) (timeLeftInMilis / (60 * 60 * 1000)) % 24;
        int minutes = (int) (timeLeftInMilis / (60 * 1000) % 60);
        int seconds = (int) (timeLeftInMilis / (1000) % 60);

        StringBuilder sb = new StringBuilder();
        sb.append("Please Try After  :  ").append(String.valueOf(hours) + " : ").append(String.valueOf(minutes) + " : ").append(String.valueOf(seconds));
        timerText.setText(sb.toString());
    }

    public void IntervalDay() {

        timeLeftInMilis = timer;

        mEndIime = System.currentTimeMillis() + timeLeftInMilis;


        CountDownTimer countDownTimer = new CountDownTimer(timeLeftInMilis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeftInMilis = millisUntilFinished;
                UpdateCountdownTextIntervalDay();
            }

            @Override
            public void onFinish() {

                isTimerRunning = 0;
                mEndIime = 0;
                isAdClicked = 0;
                invalid_activity = 0;

                btnHideShadow.setVisibility(View.INVISIBLE);
                timerText.setVisibility(View.INVISIBLE);
                timeLeftInMilis = TIME_IN_MILISECONDS;
                Time time = new Time(timeLeftInMilis, 0, 0, 0, impression,  0);
                timeReference.child(UNIQUE_ID).child(presentDate).setValue(time);

            }
        };

        countDownTimer.start();

    }

    private void UpdateCountdownTextIntervalDay() {

        int hours = (int) (timeLeftInMilis / (60 * 60 * 1000)) % 24;
        int minutes = (int) (timeLeftInMilis / (60 * 1000) % 60);
        int seconds = (int) (timeLeftInMilis / (1000) % 60);
        StringBuilder sb = new StringBuilder();
        sb.append("Your Today's Limit Has Exceed Try Again After\n").append(String.valueOf(hours) + " : ").append(String.valueOf(minutes) + " : ").append(String.valueOf(seconds));
        timerText.setText(sb.toString());

    }

    private long getRemainingDays() {
        Date currentDate = new Date();
        Date futureDate;

        if (currentDate.getMonth() <= 11) {

            futureDate = new Date(currentDate.getYear(), currentDate.getMonth(), currentDate.getDate() + 1);
        } else {
            futureDate = new Date(currentDate.getYear() + 1, currentDate.getMonth(), currentDate.getDate() + 1);
        }

        return futureDate.getTime() - currentDate.getTime();
    }


    private void startTimer() {

        mEndIime = System.currentTimeMillis() + timeLeftInMilis;

        countDownTimer = new CountDownTimer(timeLeftInMilis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeftInMilis = millisUntilFinished;

                UpdateCountdownText();
                isTimerRunning = 1;
                nextButton.setEnabled(false);

            }

            @Override
            public void onFinish() {
                nextButton.setEnabled(true);
                mEndIime = 0;
                isTimerRunning = 0;

                btnHideShadow.setVisibility(View.INVISIBLE);
                timerText.setVisibility(View.INVISIBLE);
                timeLeftInMilis = TIME_IN_MILISECONDS;
                Time time = new Time(timeLeftInMilis, 0, 0, isAdClicked, 0, invalid_activity);
                timeReference.child(UNIQUE_ID).child(presentDate).setValue(time);

            }
        };
        countDownTimer.start();

    }

   /* private boolean checkVPN() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnectedOrConnecting();
    }*/

    private void interstitialAdsListeners() {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                nextButton.setAnimation(blink_animation);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                interstitalquensbonus();
            }

            @Override
            public void onAdLeftApplication() {
                interstitialLeftApplication();
            }
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                loadintersitialadd();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                loadintersitialadd();

                nextButton.setAnimation(null);

                impressionTextView.setAnimation(null);

                if(totalBalance<-10){

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("User").child(UNIQUE_ID);
                    databaseReference1.child("is_active").setValue(0);
                    databaseReference1.child("theif").setValue(+1);
                    startActivity(new Intent(EarnMoneyActivity.this, SignUpActivity.class));
                    finish();
                }

                if (invalid_watch >=1) {

                    invalid_activity = invalid_activity + 1;
                    Time time = new Time(timeLeftInMilis, mEndIime, isTimerRunning, isAdClicked, impression, invalid_activity);
                    timeReference.child(UNIQUE_ID).child(presentDate).setValue(time);
                    diaglogfor_alart();

                }
                if (theif >= 1 || invalid_activity > 4) {

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("User").child(UNIQUE_ID);
                    databaseReference1.child("is_active").setValue(0);
                    databaseReference1.child("theif").setValue(+1);
                    startActivity(new Intent(EarnMoneyActivity.this,SignUpActivity.class));
                    finish();
                }
            }
        });

        mInterstitialAd1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                nextButton.setAnimation(blink_animation);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                interstitalquensbonus();
            }

            @Override
            public void onAdLeftApplication() {
                interstitialLeftApplication();
            }
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                loadintersitialadd();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                loadintersitialadd();

                nextButton.setAnimation(null);

                impressionTextView.setAnimation(null);

                if (invalid_watch >=1) {
                    invalid_activity = invalid_activity + 1;
                    Time time = new Time(timeLeftInMilis, mEndIime, isTimerRunning, isAdClicked, impression, invalid_activity);
                    timeReference.child(UNIQUE_ID).child(presentDate).setValue(time);
                    diaglogfor_alart();

                }
                if (theif >= 1 || invalid_activity > 4) {

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("User").child(UNIQUE_ID);
                    databaseReference1.child("is_active").setValue(0);
                    databaseReference1.child("theif").setValue(+1);
                    startActivity(new Intent(EarnMoneyActivity.this,SignUpActivity.class));
                    finish();
                }
            }

        });
        mInterstitialAd2.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                nextButton.setAnimation(blink_animation);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                interstitalquensbonus();
            }

            @Override
            public void onAdLeftApplication() {
                interstitialLeftApplication();
            }
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                loadintersitialadd();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                loadintersitialadd();

                nextButton.setAnimation(null);

                impressionTextView.setAnimation(null);
                if (invalid_watch >=1) {

                    invalid_activity = invalid_activity + 1;
                    Time time = new Time(timeLeftInMilis, mEndIime, isTimerRunning, isAdClicked, impression, invalid_activity);
                    timeReference.child(UNIQUE_ID).child(presentDate).setValue(time);
                    diaglogfor_alart();

                }
                if (theif >= 1 || invalid_activity > 4) {

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("User").child(UNIQUE_ID);
                    databaseReference1.child("is_active").setValue(0);
                    databaseReference1.child("theif").setValue(+1);
                    startActivity(new Intent(EarnMoneyActivity.this,SignUpActivity.class));
                    finish();
                }
            }
        });
    }

    private void interstitialLeftApplication() {
        isAdClicked = isAdClicked + 1;


        if (impression >= 25) {

            impression = 0;

            Time time = new Time(timeLeftInMilis, 0, isTimerRunning, isAdClicked, impression, invalid_activity);
            timeReference.child(UNIQUE_ID).child(presentDate).setValue(time);

            totalBalance = totalBalance + 2;


            Total_Account total_account = new Total_Account(totalBalance, total_referral, totalreferalBalance);
            total_Account_Reference.child(UNIQUE_ID).setValue(total_account);



            refferCommision();
            startTimer();

        }
       else  {

            impression = 0;

            Time time = new Time(timeLeftInMilis, 0, isTimerRunning, isAdClicked, impression, invalid_activity);
            timeReference.child(UNIQUE_ID).child(presentDate).setValue(time);

            totalBalance = totalBalance - 2;


            Total_Account total_account = new Total_Account(totalBalance, total_referral, totalreferalBalance);
            total_Account_Reference.child(UNIQUE_ID).setValue(total_account);


            Toast to = Toast.makeText(getApplicationContext(), "Invalid Click !!! 2 TK Deducted From Your Account"  , Toast.LENGTH_SHORT);
            to.setGravity(Gravity.CENTER, 0, 0);
            to.show();
          //  refferCommision();
            startTimer();

        }


        new CountDownTimer(1000*1*60, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                int hours = (int) (millisUntilFinished / (60 * 60 * 1000)) % 24;
                int minutes = (int) (millisUntilFinished / (60 * 1000) % 60);
                int seconds = (int) (millisUntilFinished / (1000) % 60);

                StringBuilder sb = new StringBuilder();
                sb.append("Click Different Options Here : ").append(String.valueOf(hours) + " : ").append(String.valueOf(minutes) + " : ").append(String.valueOf(seconds));

                Toast to = Toast.makeText(getApplicationContext(), "" + sb.toString(), Toast.LENGTH_SHORT);
                to.setGravity(Gravity.CENTER, 0, 0);
                to.show();
                theif = 1;

            }

            @Override
            public void onFinish() {

                theif = 0;

            }
        }.start();

    }


    private void interstitalquensbonus() {

        if (impression < clickImpression) {
            Toast.makeText(getApplicationContext(), "Don't Click On this Ad", Toast.LENGTH_LONG).show();
        }
        if (impression >= clickImpression) {
            Toast.makeText(getApplicationContext(), "Click On this Ad Earn 2 TK", Toast.LENGTH_SHORT).show();
        }

        new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int hours = (int) (millisUntilFinished / (60 * 60 * 1000)) % 24;
                int minutes = (int) (millisUntilFinished / (60 * 1000) % 60);
                int seconds = (int) (millisUntilFinished / (1000) % 60);

                StringBuilder sb = new StringBuilder();
                sb.append("").append(String.valueOf(minutes)).append(":").append(String.valueOf(seconds));
                nextButton.setEnabled(false);

                nextButton.setText("WAIT "+sb.toString());

                Toast to = Toast.makeText(getApplicationContext(), "Wait : " + sb.toString(), Toast.LENGTH_SHORT);
                to.setGravity(Gravity.CENTER, 0, 0);
                to.show();

                invalid_watch = 1;
            }

            @Override
            public void onFinish() {
                invalid_watch = 0;


                nextButton.setEnabled(true);

                nextButton.setText("START TASK");
                impression = impression + 1;

                //   Toast.makeText(getApplicationContext(), "Impression : " + impression, Toast.LENGTH_SHORT).show();
                Time time = new Time(timeLeftInMilis, mEndIime, isTimerRunning, isAdClicked, impression, invalid_activity);
                timeReference.child(UNIQUE_ID).child(presentDate).setValue(time);


            }
        }.start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.about1:
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.buythisapp:
                openyash();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void openyash() {
        Uri uri = Uri.parse(getString(R.string.profilelink));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void diaglogfor_alart() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EarnMoneyActivity.this);
        dialogBuilder.setTitle("Invalid Activity ");

        dialogBuilder.setIcon(R.drawable.ic_warning_black_24dp);
        dialogBuilder.setMessage(R.string.notice1);


        dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button bq = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        bq.setBackground(getResources().getDrawable(R.drawable.button_style));

    }

    public void diaglog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EarnMoneyActivity.this);
        dialogBuilder.setTitle("Instruction");

        dialogBuilder.setIcon(R.drawable.ic_warning_black_24dp);
        dialogBuilder.setMessage(R.string.notice);

        dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button bq = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

       // bq.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        bq.setBackground(getResources().getDrawable(R.drawable.button_style));
        //bq.setBackground(getResources().getDrawable(R.drawable.ok32));

    }

}
