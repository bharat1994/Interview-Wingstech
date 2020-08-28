package com.interviewtaskwingstech.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.interviewtaskwingstech.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tilEmail) TextInputLayout tilEmail;
    @BindView(R.id.tilPassword) TextInputLayout tilPassword;

    @BindView(R.id.etEmail) TextInputEditText etEmail;
    @BindView(R.id.etPassword) TextInputEditText etPassword;

    @BindView(R.id.btnLogin) Button btnLogin;

    @BindView(R.id.txtSignup) TextView txtSignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().equalsIgnoreCase("")){
                    tilEmail.setError("Enter Email Address");
                    etEmail.requestFocus();
                }else if (etPassword.getText().toString().equalsIgnoreCase("")){
                    tilPassword.setError("Enter Password");
                    etPassword.requestFocus();
                }else {
                    mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Log.e("FIREBASE_LOGIN", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    }else {
                                        Log.e("FIREBASE_LOGIN", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "Please Enter Valid Email or Password !",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
    }
}