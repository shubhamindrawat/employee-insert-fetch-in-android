package com.example.shubh.demoapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shubh.demoapplication.Employee;

public class CustomList extends ArrayAdapter<Object> {

    private final Activity context;
    private final Object[] emp;

    public CustomList(Activity context, Object[] emp) {
        super(context, R.layout.activity_list_view,emp);
        this.context = context;
        this.emp = emp;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_list_view, null, true);

        // Getting all Control
        TextView txtName = (TextView) rowView.findViewById(R.id.emp_name);
        TextView txtEmail = (TextView) rowView.findViewById(R.id.emp_email);
        TextView txtMob = (TextView) rowView.findViewById(R.id.emp_mobile);
        TextView txtGen = (TextView) rowView.findViewById(R.id.emp_gender);
        TextView txtDoB = (TextView) rowView.findViewById(R.id.emp_dob);
        TextView txtDept = (TextView) rowView.findViewById(R.id.emp_dept);
        TextView txtSalary = (TextView) rowView.findViewById(R.id.emp_salary);

        // Setting Value to Grid
        Employee emp = (Employee) this.emp[position];

        txtName.setText(emp.getEmp_name());
        txtEmail.setText(emp.getEmp_email());
        txtMob.setText(emp.getEmp_mobile());
        txtGen.setText(emp.getEmp_gender());
        txtDoB.setText(emp.getEmp_dob());
        txtDept.setText(String.valueOf(emp.getEmp_dept()));
        Double salary = emp.getEmp_sal();
        txtSalary.setText(salary.toString());

        return rowView;
    }
}