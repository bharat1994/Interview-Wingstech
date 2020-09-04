package com.interviewtaskwingstech.base;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.interviewtaskwingstech.MyApp;
import com.interviewtaskwingstech.common.CommonFunction;

public class BaseActivity extends AppCompatActivity {

    protected MyApp myApp = MyApp.getInstance();
    protected AlertDialog progressDialog;

    protected void initViews() {
        progressDialog = CommonFunction.customProgressDialog(BaseActivity.this, "Loading...");
    }

}
