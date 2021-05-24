package com.example.tasksolution;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText Name,Dob,Email,Password;
    String name,dob,age,email,password;
    FirebaseDatabase db= FirebaseDatabase.getInstance();
    DatabaseReference userRef= db.getReference().child("User");
    Calendar myCalendar;
    ImageView showpassword;
    String ageS;
    private boolean isHide = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
          myCalendar = Calendar.getInstance();
        mAuth= FirebaseAuth.getInstance();
        Name=findViewById(R.id.name);
        Email=findViewById(R.id.editemail);
        Password=findViewById(R.id.editpassword);
        showpassword=findViewById(R.id.ivEnterShow);
        Dob=findViewById(R.id.editdob);


        showpassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isHide) {
                    showpassword.setImageResource(R.drawable.ic_pass_invisible);
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Password.setSelection(Password.getText().length());
                    isHide = false;
                } else {
                    showpassword.setImageResource(R.drawable.ic_pass_visible);
                    Password.setTransformationMethod(null);
                    Password.setSelection(Password.getText().length());
                    isHide = true;
                }

            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                getAge( year, monthOfYear,dayOfMonth);
            }

        };

        Dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Signup.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void signUp(View view) {
         name = Name.getText().toString().trim();
         dob = Dob.getText().toString();
        email = Email.getText().toString();
        password = Password.getText().toString();

        if(name.isEmpty()|| name.length() < 3) {
            Name.setError("Enter name");
            Name.requestFocus();
        }
        if(dob.isEmpty() ) {
            Dob.setError("Enter date of birth");
            Dob.requestFocus();
        }
        if (!isValidEmail(email)) {
            Email.setError("Enter valid Email Id");
            Email.requestFocus();
        }

        if (!(email.isEmpty() || password.isEmpty())) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Signup.this, "Sucessfully Signed Up", Toast.LENGTH_SHORT).show();
                                addUserDatatoDB();
                                //startActivity(new Intent(Signup.this, Dashboard.class));
                            } else
                                Toast.makeText(Signup.this, "Error Signing Up" + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    });

        } else
            Toast.makeText(this, "Invalid Fields", Toast.LENGTH_SHORT).show();


    }



    private void addUserDatatoDB() {
        final HashMap<String, Object> userData=new HashMap<>();
        userData.put("name",name);
        userData.put("dob",dob);
        userData.put("age",ageS);
        userData.put("idLogin",email);
        userData.put("Password",password);
        userData.put("uid",mAuth.getUid());

        userRef.child(mAuth.getUid()).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(Signup.this,Dashboard.class));

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signup.this, e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("why",e.toString());
                    }
                });

    }


    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Dob.setText(sdf.format(myCalendar.getTime()));
    }


    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
         ageS = ageInt.toString();

        return ageS;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


}
