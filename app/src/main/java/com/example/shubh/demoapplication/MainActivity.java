package com.example.shubh.demoapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    Button btnSubmit, btnFetch;
    EditText txtName, txtDob, txtEmail, txtSal, txtMobile;
    RadioGroup rbgGender;
    Spinner ddlDept;
    ProgressDialog prd;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnFetch = (Button) findViewById(R.id.btnFetch);
        txtName = (EditText) findViewById(R.id.txtName);
        txtDob = (EditText) findViewById(R.id.txtDOB);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSal = (EditText) findViewById(R.id.txtSalary);
        rbgGender = (RadioGroup) findViewById(R.id.rbgGender);
        ddlDept = (Spinner) findViewById(R.id.ddlDept);
        txtMobile = (EditText) findViewById(R.id.txtMobile);
        prd = new ProgressDialog(this);
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent display = new Intent(MainActivity.this, com.example.shubh.demoapplication.Display.class);
                    startActivity(display);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean hasError = false;
                    int id = rbgGender.getCheckedRadioButtonId();
                    String gender = "";
                    if (id != -1) {
                        RadioButton rbGender = (RadioButton) findViewById(id);
                        gender = rbGender.getText().toString();
                    }

                    final String name = txtName.getText().toString();
                    if (name.isEmpty()) {
                        txtName.setError("Name is Compulsory");
                        hasError = true;
                    }

                    String dob = txtDob.getText().toString();
                    if (dob.isEmpty()) {
                        txtDob.setError("DoB is Compulsory");
                        hasError = true;
                    }

                    String email = txtEmail.getText().toString();
                    if (email.isEmpty()) {
                        txtEmail.setError("Email is Compulsory");
                        hasError = true;
                    }

                    long salary = 0;
                    if ((txtSal.getText().toString()).isEmpty()) {
                        txtSal.setError("Salary is Compulsory");
                        hasError = true;
                    } else {
                        salary = Integer.parseInt(txtSal.getText().toString());
                        if (salary <= 0) {
                            txtSal.setError("Salary Cannot be 0 or Less");
                            hasError = true;
                        }
                    }

                    int dept = Integer.parseInt(ddlDept.getSelectedItem().toString());
                    final String mobile = txtMobile.getText().toString();
                    if (mobile.isEmpty()) {
                        txtMobile.setError("Mobile Number is Compulsory");
                        hasError = true;
                    } else if (mobile.length() != 10) {
                        txtMobile.setError("Mobile Number Should Be of 10 Digits");
                        hasError = true;
                    }else if (Character.getNumericValue(mobile.charAt(0)) < 6) {
                        txtMobile.setError("Enter Valid Number");
                        hasError = true;
                    }




                    if (hasError == false) {
                        prd.setCancelable(false);
                        prd.setMessage("Checking Existence Of Mobile Number");
                        prd.show();

                        final Map<String, Object> emp = new HashMap<>();
                        emp.put("emp_dept", dept);
                        emp.put("emp_dob", dob);
                        emp.put("emp_email", email);
                        emp.put("emp_gender", gender);
                        emp.put("emp_mobile", mobile);
                        emp.put("emp_name", name);
                        emp.put("emp_sal", salary);

                        db.collection("emp")
                                .whereEqualTo("emp_mobile", mobile)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            boolean flag = false;
                                            for (DocumentSnapshot doc : task.getResult()) {
                                                if (doc.exists()) {
                                                    Toast.makeText(getApplicationContext(), "An emloyee with this mobile number already exists.", Toast.LENGTH_LONG).show();
                                                    flag = true;
                                                    break;
                                                }
                                            }
                                            if (!flag) {
                                                db.collection("emp")
                                                        .add(emp)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                prd.setMessage("Inserting Employee Record");
                                                                Toast.makeText(getApplicationContext(), "Employee Inserted Successfully", Toast.LENGTH_LONG).show();
                                                                txtName.setText("");
                                                                txtDob.setText("");
                                                                txtEmail.setText("");
                                                                txtSal.setText("");
                                                                txtMobile.setText("");
                                                                ddlDept.setSelection(0);
                                                                rbgGender.check(R.id.rbMale);
                                                                getCurrentFocus().clearFocus();
                                                                prd.dismiss();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getApplicationContext(), "Employee Insertion Failed !!!", Toast.LENGTH_LONG).show();
                                                                prd.dismiss();
                                                            }
                                                        });
                                            }else{
                                                prd.dismiss();
                                            }
                                        }

                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "Incomplete Form", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    prd.dismiss();
                }
            }
        });
    }
}
