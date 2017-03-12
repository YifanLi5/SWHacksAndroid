package com.yifan.swhacksandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity extends Activity {

    private EditText newValueField;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newValueField = (EditText) findViewById(R.id.new_value_field);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent myIntent = new Intent(this, SwipeViewFragmentHolderActivity.class);
        startActivity(myIntent);
    }

    public void onSendPressed(View view) {
        String newValue = newValueField.getText().toString();
        String currentTime = Statics.getTimeStampForCalanderObj(Calendar.getInstance());
        setStatus(newValue, currentTime);
    }

    private void setStatus(String status, String last_feed){
        ToRaspberry data = new ToRaspberry(status, last_feed);
        mDatabase.child("ToRaspberry").setValue(data);
    }
}
