package com.dailypaymentapp.cashbox.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dailypaymentapp.cashbox.BottomNavigationBehavior;
import com.dailypaymentapp.cashbox.Constants.Constant;
import com.dailypaymentapp.cashbox.DarkModePrefManager;
import com.dailypaymentapp.cashbox.Models.Total_Account;
import com.dailypaymentapp.cashbox.Models.User;
import com.dailypaymentapp.cashbox.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.dailypaymentapp.cashbox.Activities.MainActivity.UNIQUE_ID;

public class DashBoardActivity  extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    TextView DashName,DashPhoneNo,DashAccountStatus,
            DashTotalBalance,DashReferrals,DashReferralEarning;

    DatabaseReference userDatabaseReferrence,totalBalanceReferrence;

    private BottomNavigationView bottomNavigationView;

   /* NavigationView navigationView;
    View headerView;*/
    private AdView mAdView,mAdView1;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    /*Intent intent=new Intent(DashBoardActivity.this, DashBoardActivity.class);
                    startActivity(intent);*/
                    return true;
                case R.id.navigationwithdraw:

                    Intent intent1=new Intent(DashBoardActivity.this, OrderPaymentActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigationHome:
                    Intent intent4=new Intent(DashBoardActivity.this, MainActivity.class);
                    startActivity(intent4);
                    return true;


                case  R.id.navigatonearn:
                    Intent intent3=new Intent(DashBoardActivity.this, EarnMoneyActivity.class);
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_activity);

        Constant constant=new Constant();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");


        DashName=findViewById(R.id.dasName);
        DashPhoneNo=findViewById(R.id.dasPhoneNo);
        DashAccountStatus=findViewById(R.id.dasAccountStatus);
        DashTotalBalance=findViewById(R.id.dashTotalBalance);
        DashReferrals=findViewById(R.id.dashTotalReferral);
        DashReferralEarning=findViewById(R.id.dashTotalReferralEarning);

        userDatabaseReferrence= FirebaseDatabase.getInstance().getReference("User");
        totalBalanceReferrence= FirebaseDatabase.getInstance().getReference("Total_Account");

        setuser();

        MobileAds.initialize(this,
                constant.getADMOB_APP_ID());

        mAdView = findViewById(R.id.adView);
        mAdView1 = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView1.loadAd(adRequest);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
*/

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationView.setSelectedItemId(R.id.navigationMyProfile);


    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {


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
            //activeAccount();
        }  else if (id == R.id.nav_share) {
            Intent referralIntent=new Intent(DashBoardActivity.this, ReferralActivity.class);
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

    private void setuser() {

        userDatabaseReferrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(UNIQUE_ID).exists()){
                    User user= dataSnapshot.child(UNIQUE_ID).getValue(User.class);

                    DashName.setText(user.getName());
                    DashPhoneNo.setText(user.getPhone_number());
                    DashAccountStatus.setText(user.getIsAcccountVerifyed()>=1?"Active":"Not Active");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        totalBalanceReferrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(UNIQUE_ID).exists()){
                    Total_Account total_account=dataSnapshot.child(UNIQUE_ID).getValue(Total_Account.class);
                    DashTotalBalance.setText(total_account.getTotal_balance()+" BDT");
                    DashReferrals.setText(total_account.getTotal_referral()+"");
                    DashReferralEarning.setText(total_account.getTotal_referral_earning()+" BDT");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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
                Intent intent = new Intent(DashBoardActivity.this, AboutActivity.class);
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


}
