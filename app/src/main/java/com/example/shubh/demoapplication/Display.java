package com.example.shubh.demoapplication;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Display extends AppCompatActivity {

    ListView lstEmp;
    StringBuilder fields;
    ProgressDialog prd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_display);
            lstEmp = (ListView) findViewById(R.id.lstEmp);
            prd = new ProgressDialog(this);
            prd.setMessage("Loading Data !!!");
            prd.setCancelable(false);
            prd.show();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("emp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<Employee> emp = new ArrayList<Employee>();
                        for (DocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                Log.d("Tags : ",document.getData() + "");
                                emp.add(document.toObject(Employee.class));
                            }
                        }
                        CustomList adapter = new CustomList(Display.this,emp.toArray());
                        lstEmp.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), "Successfully Loaded Data", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Loading Failed", Toast.LENGTH_LONG).show();
                    }
                    prd.dismiss();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            if(prd != null)
                prd.dismiss();
        }
    }
}
