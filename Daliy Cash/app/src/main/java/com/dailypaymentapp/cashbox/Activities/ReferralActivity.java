package com.dailypaymentapp.cashbox.Activities;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypaymentapp.cashbox.BottomNavigationBehavior;
import com.dailypaymentapp.cashbox.DarkModePrefManager;
import com.dailypaymentapp.cashbox.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.dailypaymentapp.cashbox.Activities.MainActivity.UNIQUE_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReferralActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    EditText referralCode;
    TextView myReferralCode;
    Button referNowBtn;
    int total_balance;
    TextView copyReferralCode;
    LinearLayout inputReferral;
    RelativeLayout activeReferral;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String name = null;
    int total_referral = 0;
    int referAccountBalance;

    String refer_code;
    Date currentDate;
    String presentDate;

    private BottomNavigationView bottomNavigationView;

   /* NavigationView navigationView;
    View headerView;
*/

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
                    Intent intent3=new Intent(getApplicationContext(),EarnMoneyActivity.class);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_referral_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reffer Friends");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        presentDate = dateFormat.format(currentDate);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String getReferralCode = dataSnapshot.child("User").child(UNIQUE_ID).child("referral_code")
                        .getValue(String.class);

                myReferralCode.setText(getDeviceUniqueID(ReferralActivity.this));

                int isUseReferral = dataSnapshot.child("User").child(UNIQUE_ID).child("isUseReferral").getValue(Integer.class);
                refer_code = dataSnapshot.child("User").child(UNIQUE_ID).child("refer_code").getValue(String.class);
                if (isUseReferral == 0) {
                    inputReferral.setVisibility(View.VISIBLE);
                    activeReferral.setVisibility(View.INVISIBLE);
                } else {
                    activeReferral.setVisibility(View.VISIBLE);
                    inputReferral.setVisibility(View.INVISIBLE);
                }

                total_balance = dataSnapshot.child("Total_Account").child(UNIQUE_ID).child("total_balance").getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        referralCode = findViewById(R.id.referCode);
        myReferralCode = findViewById(R.id.myReferralCode);
        referNowBtn = findViewById(R.id.referNow);
        inputReferral =findViewById(R.id.inputReferral);
        activeReferral = findViewById(R.id.activeReferral);
        copyReferralCode = findViewById(R.id.copyReferralCode);


       /* referNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!referralCode.getText().toString().equals("")) {

                    if (!refer_code.equals(UNIQUE_ID)) {

                        databaseReference.child("User").orderByChild("referral_code").equalTo(refer_code).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        name = data.getKey();
                                        total_referral = data.child("total_referral").getValue(Integer.class);
                                    }

                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            referAccountBalance = dataSnapshot.child("Time").child(name).child("total_balance").getValue(Integer.class);

                                            databaseReference.child("Time").child(name).child("total_balance").setValue(referAccountBalance + 2);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    databaseReference.child("User").child(UNIQUE_ID).child("isUseReferral").setValue(1);
                                    databaseReference.child("Time").child(UNIQUE_ID).child("total_balance").setValue(total_balance + 2);


                                    ReferralActivity referralFragment = new ReferralActivity();

                                  *//*  getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.main_fragment,referralFragment)
                                            .commitAllowingStateLoss();*//*

                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid Refer Code. please check and try again", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    } else {
                        Toast.makeText(getApplicationContext(), "You have used your own referral code. please use a friends referral code", Toast.LENGTH_LONG).show();
                    }


                } else {
                    referralCode.setError("Please input a valid referral code");
                }

            }
        });
*/
        copyReferralCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("text", myReferralCode.getText());
                cm.setPrimaryClip(cData);
                Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_LONG).show();
            }
        });

        /*navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);*/

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
       // bottomNavigationView.setSelectedItemId(R.id.navigationHome);

    }

    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id;
        device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
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
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent1 =new Intent(getApplicationContext(),DashBoardActivity.class);
            startActivity(intent1);

        } else if (id == R.id.nav_earn_money) {

            Intent intent =new Intent(getApplicationContext(), EarnMoneyActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_withdraw) {
            Intent intent =new Intent(getApplicationContext(), OrderPaymentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_withdraw_history) {
            Intent intent2 =new Intent(getApplicationContext(), WithdrawHistoryActivity.class);
            startActivity(intent2);
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
}
