package com.interviewtaskwingstech.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.tilName) TextInputLayout tilName;
    @BindView(R.id.tilEmail) TextInputLayout tilEmail;
    @BindView(R.id.tilPhone) TextInputLayout tilPhone;
    @BindView(R.id.tilPassword) TextInputLayout tilPassword;

    @BindView(R.id.etName) TextInputEditText etName;
    @BindView(R.id.etEmail) TextInputEditText etEmail;
    @BindView(R.id.etPhone) TextInputEditText etPhone;
    @BindView(R.id.etPassword) TextInputEditText etPassword;

    @BindView(R.id.btnSignup) Button btnSignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().equalsIgnoreCase("")){
                    tilEmail.setError("Enter Email Address");
                    etEmail.requestFocus();
                }else if (etPassword.getText().toString().equalsIgnoreCase("")){
                    tilPassword.setError("Enter Password");
                    etPassword.requestFocus();
                }else if (Integer.parseInt(etPassword.getText().toString()) < 5){
                    tilPassword.setError("Password minimum 6 character");
                    etPassword.requestFocus();
                }else {
                    mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });

    }
}