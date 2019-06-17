package com.dailypaymentapp.cashbox.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypaymentapp.cashbox.BottomNavigationBehavior;
import com.dailypaymentapp.cashbox.Constants.Constant;
import com.dailypaymentapp.cashbox.DarkModePrefManager;
import com.dailypaymentapp.cashbox.ForceUpdateAsync;
import com.dailypaymentapp.cashbox.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    DatabaseReference userDatabaseReferance;

    public static String UNIQUE_ID;
    private BottomNavigationView bottomNavigationView;

    ProgressBar progressBar;


    TextView percentageText;
    TextView referTextview;

    String name="";
    String phone_no="";


    NavigationView navigationView;
    View headerView;


    DatabaseReference myRef;
    DatabaseReference TransactionIdReferences;
    DatabaseReference totalReferences;
    DatabaseReference timeReferences;

    Date currentDate;
    String presentDate;


    LinearLayout clickLayout;
    private AdView mAdView,mAdView1,mAdView2;
    private InterstitialAd mInterstitialAd;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    Intent intent=new Intent(MainActivity.this, DashBoardActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigationwithdraw:

                    Intent intent1=new Intent(MainActivity.this, OrderPaymentActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigationHome:

                    return true;
                case  R.id.navigatonearn:
                    Intent intent3=new Intent(MainActivity.this, EarnMoneyActivity.class);
                    startActivity(intent3);

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
        setContentView(R.layout.activity_main);

        if(new DarkModePrefManager(this).isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        Constant constant=new Constant();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        userDatabaseReferance=FirebaseDatabase.getInstance().getReference("User");

        myRef = FirebaseDatabase.getInstance().getReference("User");
        timeReferences = FirebaseDatabase.getInstance().getReference("Time");

        TransactionIdReferences=FirebaseDatabase.getInstance().getReference("Transaction_ID");
        totalReferences=FirebaseDatabase.getInstance().getReference("Total_Account");


        UNIQUE_ID = getDeviceUniqueID(MainActivity.this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        progressBar=(ProgressBar) findViewById(R.id.progressberid);

        percentageText=(TextView)findViewById(R.id.percente);

        referTextview=findViewById(R.id.referTextView);



        clickLayout=(LinearLayout)findViewById(R.id.clickLinearLayout);



        mAdView = findViewById(R.id.adView1);
        mAdView1 = findViewById(R.id.adView2);
        mAdView2 = findViewById(R.id.adView3);

        AdRequest adRequest = new AdRequest.Builder().build();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(constant.getInterstitial_id1());




        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        mAdView.loadAd(adRequest);
        mAdView1.loadAd(adRequest);
        mAdView2.loadAd(adRequest);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationView.setSelectedItemId(R.id.navigationHome);


        currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        presentDate = dateFormat.format(currentDate);


        clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 =new Intent(getApplicationContext(), EarnMoneyActivity.class);
                startActivity(intent1);
            }
        });

        referTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 =new Intent(getApplicationContext(), ReferralActivity.class);
                startActivity(intent1);
                finish();
            }
        });


        getUserInfo();



    }



    private void getUserInfo() {

        View header = navigationView.getHeaderView(0);
     final   TextView navhead_name = (TextView) header.findViewById(R.id.navhead_name_id);
       final TextView navhead_phone= (TextView) header.findViewById(R.id.navhead_phone_id);
      final  TextView navhead_balance= (TextView) header.findViewById(R.id.navhead_balance);
       final TextView navhead_referral= (TextView) header.findViewById(R.id.navhead_total_referral);
        final TextView navhead_referral_balance= (TextView) header.findViewById(R.id.navhead_total_referral_balance);

        timeReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(UNIQUE_ID).child(presentDate).exists()){
                    int impression=dataSnapshot.child(UNIQUE_ID).child(presentDate).child("impression").getValue(Integer.class);


                    try  {
                        progressBar.setProgress(impression);
                        percentageText.setText((impression*4)+" % Completed");
                    }
                    catch (Exception e){

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        totalReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot!=null){

                    Double Total_Balance=dataSnapshot.child(UNIQUE_ID).child("total_balance").getValue(Double.class);
                    int Total_Referral=dataSnapshot.child(UNIQUE_ID).child("total_referral").getValue(Integer.class);
                    Double Total_Referral_Balance=dataSnapshot.child(UNIQUE_ID).child("total_referral_earning").getValue(Double.class);

                    navhead_balance.setText("Balance : "+Total_Balance+" BDT");
                    navhead_referral.setText("Referral : "+Total_Referral);
                    navhead_referral_balance.setText("Referral Balance : "+Total_Referral_Balance+" BDT");


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        userDatabaseReferance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name=dataSnapshot.child(UNIQUE_ID).child("name").getValue(String.class);
                phone_no=dataSnapshot.child(UNIQUE_ID).child("phone_number").getValue(String.class);

//                navnamev=name;
//                navphonenov=phone_no;

                navhead_name.setText("Member : "+name);
                navhead_phone.setText("Contract NO : "+phone_no);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id;
        device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent=new Intent(MainActivity.this,DashBoardActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_earn_money) {

            Intent intent =new Intent(getApplicationContext(), EarnMoneyActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_withdraw) {
                Intent intent =new Intent(getApplicationContext(), OrderPaymentActivity.class);
                startActivity(intent);
        } else if (id == R.id.nav_withdraw_history) {
            Intent intent1 =new Intent(getApplicationContext(),WithdrawHistoryActivity.class);
            startActivity(intent1);

        } else if (id == R.id.nav_active_account) {
                activeAccount();
        }  else if (id == R.id.nav_share) {
            Intent referralIntent=new Intent(MainActivity.this, ReferralActivity.class);
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

    public void activeAccount() {

      final ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading Info...");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(UNIQUE_ID).exists()) {

                    int isAccountVerifyed=dataSnapshot.child(UNIQUE_ID).child("isAcccountVerifyed").getValue(Integer.class);
                    if (isAccountVerifyed == 0) {

                        progressDialog.dismiss();
                        insertTrxid();

                    } else {

                        progressDialog.dismiss();
                        accoutnVerifyedDialog();

                    }
                } else {

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });


    }

    private void accoutnVerifyedDialog() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.congratulation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        Button dialogBtn_okay = (Button) dialog.findViewById(R.id.btn_okay);
        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        //        Toast.makeText(getApplicationContext(),"Okay" ,Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void insertTrxid() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox_otp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        TextView text = (TextView) dialog.findViewById(R.id.txt_file_path);
        text.setText(" Active Your Account ?");

        final EditText trxedittex=(EditText)dialog.findViewById(R.id.trx_editext_id);

        final String trx=trxedittex.getText().toString();


        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Button dialogBtn_okay = (Button) dialog.findViewById(R.id.btn_okay);
        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Okay" ,Toast.LENGTH_SHORT).show();

                if(trxedittex.getText().toString().length()<10){
                    trxedittex.setError("Invalid TrxID");
                }
                else {
                    verify(trxedittex.getText().toString());
                    dialog.cancel();
                }


            }
        });

        dialog.show();
    }


    private void verify(String s) {

        final String TID=s;
        TransactionIdReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.child(TID).exists()) {

                    int isUsedTransactionId= dataSnapshot.child(TID).child("isIDUsed").getValue(Integer.class);

                    if(isUsedTransactionId>=1){

                       transactionIdAlreadyUsed();
                    }
                    else {

                       useTransactionId(TID);
                    }
                } else {
                    noTrxIdFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

    private void noTrxIdFound() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.notrxfound);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        Button dialogBtn_okay = (Button) dialog.findViewById(R.id.btn_okay);
        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Okay" ,Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void useTransactionId(String TID) {

        TransactionIdReferences.child(TID).child("isIDUsed").setValue(1);
        myRef.child(UNIQUE_ID).child("isAcccountVerifyed").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle("Congratulation");
                dialogBuilder.setMessage("Your Account Is Vefified Now");
                dialogBuilder.setIcon(R.drawable.ic_active_account);
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });
        myRef.child(UNIQUE_ID).child("refer_code").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    final String referedBY=dataSnapshot.getValue(String.class);
                    DatabaseReference sendbonus=FirebaseDatabase.getInstance().getReference("Total_Account").child(referedBY);

                    sendbonus.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                double b=dataSnapshot.child("total_balance").getValue(Double.class);

                                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Total_Account").child(referedBY);
                                databaseReference.child("total_balanc").setValue(b+20).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(),"Congratulation !! Your Referral Got 20 BDT",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void transactionIdAlreadyUsed() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.trxalreadyuded);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        Button dialogBtn_okay = (Button) dialog.findViewById(R.id.btn_okay);
        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Okay" ,Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        forceUpdate();
    }
    public void forceUpdate() {


        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = null;
        if (packageInfo != null) {
            currentVersion = packageInfo.versionName;
        }

        new ForceUpdateAsync(currentVersion, MainActivity.this).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

    }
}
