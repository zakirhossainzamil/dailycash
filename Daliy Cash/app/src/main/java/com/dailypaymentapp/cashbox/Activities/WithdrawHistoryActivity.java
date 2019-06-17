package com.dailypaymentapp.cashbox.Activities;


import android.app.Activity;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dailypaymentapp.cashbox.BottomNavigationBehavior;
import com.dailypaymentapp.cashbox.DarkModePrefManager;
import com.dailypaymentapp.cashbox.Models.Payment;
import com.dailypaymentapp.cashbox.R;
import com.dailypaymentapp.cashbox.WithdrawHistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawHistoryActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{


    private RecyclerView withdrawHistoryRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String unique_id;

    FirebaseRecyclerAdapter<Payment, WithdrawHistoryViewHolder> adapter;
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
                    Intent intent=new Intent(WithdrawHistoryActivity.this, DashBoardActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigationwithdraw:

                    Intent intent1=new Intent(WithdrawHistoryActivity.this, OrderPaymentActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigationHome:
                    Intent intent4=new Intent(WithdrawHistoryActivity.this, MainActivity.class);
                    startActivity(intent4);
                    return true;
                case  R.id.nav_earn_money:
                    Intent intent3=new Intent(WithdrawHistoryActivity.this, EarnMoneyActivity.class);
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
        setContentView(R.layout.activity_history);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Withdraw History");

        unique_id = getDeviceUniqueID(this);
        firebaseDatabase=FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Payment_order");
        databaseReference.keepSynced(true);


        withdrawHistoryRecyclerView = findViewById(R.id.withdrawHistoryRecyclerView);
        withdrawHistoryRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        withdrawHistoryRecyclerView.setLayoutManager(layoutManager);


        Query query = databaseReference.orderByChild("user_id").equalTo(unique_id);
        FirebaseRecyclerOptions<Payment> options =
                new FirebaseRecyclerOptions.Builder<Payment>()
                        .setQuery(query, Payment.class)
                        .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);*/

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        //bottomNavigationView.setSelectedItemId(R.id.navigationSearch);

        adapter= new FirebaseRecyclerAdapter<Payment, WithdrawHistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WithdrawHistoryViewHolder holder, int position, @NonNull Payment model) {
                holder.bind(model);

            }

            @NonNull
            @Override
            public WithdrawHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw_history_show_layout,parent,false);
                return new WithdrawHistoryViewHolder(view);
            }

        };

        withdrawHistoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id;
        device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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
                Intent intent = new Intent(WithdrawHistoryActivity.this, AboutActivity.class);
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

        } else if (id == R.id.nav_active_account) {
            //activeAccount();
        }  else if (id == R.id.nav_share) {
            Intent referralIntent=new Intent(WithdrawHistoryActivity.this, ReferralActivity.class);
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
