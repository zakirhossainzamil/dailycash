package com.dailypaymentapp.cashbox;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dailypaymentapp.cashbox.Activities.WithdrawHistoryActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.dailypaymentapp.cashbox.Activities.MainActivity.UNIQUE_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawFragment extends Fragment {
    TextView warningText;
    LinearLayout withdrawLayout;
    EditText password,withdrawPhoneNumber,conWithdrawPhnNumber;
    Button submitBtn;
    int totalBalance;
    String type;
    RadioGroup paymentType;
    RadioButton selectPaymentType;
    String acccountPassword;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int convertQuinsToTaka;
    double quinsRate;
    String paymentDate,paymentDate1,paymentDate2;
    Date currentDate;
    String presentDate;


    public WithdrawFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_withdraw, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Withdraw");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference();

        warningText = view.findViewById(R.id.warningMsg);
        password = view.findViewById(R.id.userPassword);
        withdrawPhoneNumber = view.findViewById(R.id.withdrawPhoneNumber);
        conWithdrawPhnNumber = view.findViewById(R.id.conWithdrawPhoneNumber);
        submitBtn = view.findViewById(R.id.submit);
        withdrawLayout = view.findViewById(R.id.withdraw_layout);
        paymentType = view.findViewById(R.id.radioGroupPayment);

        currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        presentDate = dateFormat.format(currentDate);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                totalBalance = dataSnapshot.child("Time").child(UNIQUE_ID).child(presentDate).child("total_balance").getValue(Integer.class);

                quinsRate = dataSnapshot.child("Payment_info").child("quins_rate").getValue(Double.class);
                convertQuinsToTaka = (int) (totalBalance*quinsRate);

                paymentDate = dataSnapshot.child("Payment_info").child("payment_date").getValue(String.class);
                paymentDate1 = dataSnapshot.child("Payment_info").child("payment_date1").getValue(String.class);
                paymentDate2 = dataSnapshot.child("Payment_info").child("payment_date2").getValue(String.class);


                acccountPassword = dataSnapshot.child("User").child(UNIQUE_ID).child("password").getValue(String.class);
                if (totalBalance<30)
                {
                    warningText.setVisibility(View.VISIBLE);
                    warningText.setText("You Dont Have Enough Money . Minimum 30 TAKA To Redeem .");
                    withdrawLayout.setVisibility(View.INVISIBLE);

                } else if (!presentDate.equals(paymentDate)||!presentDate.equals(paymentDate1)||!presentDate.equals(paymentDate2))
                {
                    warningText.setVisibility(View.VISIBLE);
                    warningText.setText("Payment Will Be Made On "+ paymentDate+" "+paymentDate1+""+paymentDate2+" "+" Please Redeem On These Dates");
                    withdrawLayout.setVisibility(View.INVISIBLE);
                } else
                {
                    warningText.setVisibility(View.INVISIBLE);
                    withdrawLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phnNumber = withdrawPhoneNumber.getText().toString();
                String userpassword = password.getText().toString();
                if (TextUtils.isEmpty(phnNumber))
                {
                    withdrawPhoneNumber.setError("Please input phone number");
                }
                if (TextUtils.isEmpty(conWithdrawPhnNumber.getText().toString()))
                {
                    conWithdrawPhnNumber.setError("Please input same phone number");
                }

                if (TextUtils.isEmpty(userpassword))
                {
                    password.setError("Please input your password");
                }
                if (paymentType.getCheckedRadioButtonId() == -1) {
                    Snackbar snackbar = Snackbar
                            .make(getView(), "Please select all review", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                } else {
                    int selectedPaymentTypeId = paymentType.getCheckedRadioButtonId();
                    View radioButton = paymentType.findViewById(selectedPaymentTypeId);
                    int radioId = paymentType.indexOfChild(radioButton);
                    selectPaymentType = (RadioButton) paymentType.getChildAt(radioId);
                    type = selectPaymentType.getText().toString();



                    if (conWithdrawPhnNumber.getText().toString().equals(phnNumber))
                    {

                        if (userpassword.equals(acccountPassword))
                        {

                            String key = databaseReference.push().getKey(); // this will create a new unique key
                            Map<String, Object> value = new HashMap<>();
                            value.put("user_id", UNIQUE_ID);
                            value.put("phone_number", phnNumber);
                            value.put("withdraw_amount", convertQuinsToTaka);
                            value.put("payment_type", type);
                            value.put("is_paid", 0);
                            value.put("date", ServerValue.TIMESTAMP);
                            databaseReference.child("Payment_order").child(key).setValue(value);
                            totalBalance = 0;
                            databaseReference.child("Time").child(UNIQUE_ID).child(presentDate).child("total_balance").setValue(totalBalance);

                            WithdrawHistoryActivity withdrawHistoryFragment = new WithdrawHistoryActivity();

                            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,withdrawHistoryFragment).commitAllowingStateLoss();

                        }else
                        {
                            password.setError("Wrong Password");
                        }

                    }else
                    {
                        conWithdrawPhnNumber.setError("Phone number not match");
                    }
                }


            }
        });


        return view;
    }

}
