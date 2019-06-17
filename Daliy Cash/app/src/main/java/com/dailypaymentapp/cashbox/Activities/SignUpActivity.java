package com.dailypaymentapp.cashbox.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.dailypaymentapp.cashbox.Constants.Constant;
import com.dailypaymentapp.cashbox.R;
import com.dailypaymentapp.cashbox.Models.Time;
import com.dailypaymentapp.cashbox.Models.Total_Account;
import com.dailypaymentapp.cashbox.Models.User;
import com.dailypaymentapp.cashbox.Utilities;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;



public class SignUpActivity extends AppCompatActivity {

    EditText name,phnNumber, refer_code;

    Button submit;
    ProgressDialog progressDialog;

    Date currentDate;
    String presentDate;

    public String UNIQUE_ID;

    DatabaseReference myRef;

    DatabaseReference timeReference;

    DatabaseReference total_Account_Reference;
    DatabaseReference databaseReference;
    DatabaseReference maxdatabaseReference;
    DatabaseReference currentdatabaseReference;


    String who_reffer_me;
    User user;


    int MAX_USER;
    int CURRENT_USER;
    int canSignUpFlag=1;
    private InterstitialAd mInterstitialAd;

   TextView maxTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);

        Constant constant=new Constant();

        MobileAds.initialize(this, constant.getADMOB_APP_ID());


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(constant.getInterstitial_id1());

        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }


        CURRENT_USER=0;
        MAX_USER=0;

        myRef = FirebaseDatabase.getInstance().getReference("User");
        myRef.keepSynced(true);


        timeReference = FirebaseDatabase.getInstance().getReference("Time");
        timeReference.keepSynced(true);

        maxTextView=findViewById(R.id.maxTextViewId);


        maxdatabaseReference = FirebaseDatabase.getInstance().getReference("MAX_USER");

        maxdatabaseReference.keepSynced(true);
        currentdatabaseReference = FirebaseDatabase.getInstance().getReference("User");
        currentdatabaseReference.keepSynced(true);


        total_Account_Reference = FirebaseDatabase.getInstance().getReference("Total_Account");
        total_Account_Reference.keepSynced(true);


        databaseReference = FirebaseDatabase.getInstance().getReference();


        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Loading Info...");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

//

        name = findViewById(R.id.name);
        phnNumber = findViewById(R.id.phnNumber);
        refer_code = findViewById(R.id.referCode);





        submit = findViewById(R.id.submit);


        UNIQUE_ID = getDeviceUniqueID(this);

        currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        presentDate = dateFormat.format(currentDate);

        if (Utilities.isNetworkAvailable(this)) {



            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(UNIQUE_ID).exists()) {
                        user = dataSnapshot.child(UNIQUE_ID).getValue(User.class);

                        who_reffer_me = user.getRefer_code();
                        if (user.getIs_active() == 1) {

                            progressDialog.dismiss();

                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
                            dialogBuilder.setTitle("Suspended");
                            dialogBuilder.setIcon(R.drawable.ic_warning_black_24dp);
                            dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            LayoutInflater inflater = SignUpActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.layout_for_suspend_alert, null);
                            dialogBuilder.setView(dialogView);

                            final TextView accountId = dialogView.findViewById(R.id.myAccountId);
                            accountId.setText(UNIQUE_ID);
                            TextView copy = dialogView.findViewById(R.id.copy);
                            copy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData cData = ClipData.newPlainText("text", accountId.getText());
                                    cm.setPrimaryClip(cData);
                                    Toast.makeText(SignUpActivity.this, "Copied to clipboard", Toast.LENGTH_LONG).show();
                                }
                            });

                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();

                        }
                    } else {
                        progressDialog.dismiss();


                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference locationRef = rootRef.child("User");
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                    CURRENT_USER++;
                                    // String key = ds.getKey();
                                    //Toast.makeText(getApplicationContext(),key+"   "+CURRENT_USER,Toast.LENGTH_SHORT).show();
                                }

                                DatabaseReference maxdb=FirebaseDatabase.getInstance().getReference("MAX_USER");

                                maxdb.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child("user_max").exists()){
                                            MAX_USER=dataSnapshot.child("user_max").getValue(Integer.class);
                                            Toast.makeText(getApplicationContext(),"called "+MAX_USER,Toast.LENGTH_LONG).show();

                                            if(MAX_USER>CURRENT_USER){
                                                canSignUpFlag=1;
                                            }
                                            else {
                                                canSignUpFlag=0;
                                            }
                                            maxTextView.setText(" Max Member "+MAX_USER+" Total Signed Up "+CURRENT_USER+"");
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        };
                        locationRef.addListenerForSingleValueEvent(eventListener);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    progressDialog.dismiss();
                }
            });



            submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utilities.isNetworkAvailable(SignUpActivity.this)) {

                    if(canSignUpFlag==1) {


                        if (checkInvo() == 0) {


                            databaseReference = FirebaseDatabase.getInstance().getReference();

                            databaseReference.child("User").orderByChild("referral_code").equalTo(refer_code.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        //    sendReferBonus();

                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (!dataSnapshot.child(UNIQUE_ID).exists()) {


                                                    phnNumber = findViewById(R.id.phnNumber);
                                                    refer_code = findViewById(R.id.referCode);

                                                    User u = new User(name.getText().toString(),phnNumber.getText().toString(), UNIQUE_ID, 1, 1, refer_code.getText().toString(),0);
                                                    myRef.child(UNIQUE_ID).setValue(u);


                                                    Time time = new Time(Constant.TIME_IN_MILISECONDS, 0, 0, 0, 0, 0);
                                                    timeReference.child(UNIQUE_ID).child(presentDate).setValue(time);


                                                    Total_Account total_account = new Total_Account(0, 0, 0);
                                                    total_Account_Reference.child(UNIQUE_ID).setValue(total_account);

                                                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                                    databaseReference.child("Payment_order").child(UNIQUE_ID);

                                                   final DatabaseReference df=FirebaseDatabase.getInstance().getReference("Total_Account");

                                                    df.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.child(refer_code.getText().toString()).exists()){

                                                                int tr=    dataSnapshot.child(refer_code.getText().toString()).child("total_referral").getValue(Integer.class);
                                                                tr=tr+1;
                                                                df.child(refer_code.getText().toString()).child("total_referral").setValue(tr);
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });


                                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                                    finish();

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid Refer Code. please check and try again", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            Toast.makeText(SignUpActivity.this, "Please Check Your Info", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
                        dialogBuilder.setTitle("Limit Excided");
                        dialogBuilder.setIcon(R.drawable.ic_warning_black_24dp);
                        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                        LayoutInflater inflater = SignUpActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.layout_for_user_lmi, null);
                        dialogBuilder.setView(dialogView);

                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();
                    }

                } else {
                    new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle("No Internet Connection")
                            .setMessage("Please connect with internet and try again")
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .setNeutralButton("Okay!!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }
        });
        } else {
            progressDialog.dismiss();
            new AlertDialog.Builder(this)
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




    }




    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id;
        device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        return device_unique_id;
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    int checkInvo() {

        int wronginfo = 0;


        if(TextUtils.isEmpty(name.getText().toString()))
        {
            name.setError("Please Input Your Name");
            wronginfo=1;
        }

        if (TextUtils.isEmpty(phnNumber.getText().toString())) {
            phnNumber.setError("Please input valid phone number");
            wronginfo = 1;

        }
        if (TextUtils.isEmpty(refer_code.getText().toString())) {
            refer_code.setError("Please input your Referral Code");
            wronginfo = 1;
        }

        return wronginfo;
    }
}
