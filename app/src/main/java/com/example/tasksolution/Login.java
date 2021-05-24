package com.example.tasksolution;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends Activity {
    EditText Email, Password;
    String email, password;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private boolean isHide = false;
    Button signIn;
    ImageView showpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signIn= findViewById(R.id.CBsignIn);
        Email=findViewById(R.id.CETemailadd);
        Password=findViewById(R.id.CETenterpassword);


    }
    public void signIn(View view)
    {

        email=Email.getText().toString();
        password=Password.getText().toString();

        if(!(email.isEmpty()||password.isEmpty()))
        {
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Login.this, "SignedIn", Toast.LENGTH_SHORT).show();


                            }
                            else
                                Toast.makeText(Login.this, "Error:"+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Invalid Fields", Toast.LENGTH_SHORT).show();
        }
    }
}
