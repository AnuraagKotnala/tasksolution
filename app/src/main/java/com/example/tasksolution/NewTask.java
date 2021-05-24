package com.example.tasksolution;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.GONE;


public class NewTask extends AppCompatActivity {
    DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("User");
    int i=1;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    ArrayList<String> number = new ArrayList<String>();
    ArrayList<String> wrote = new ArrayList<String>();
    RecyclerView mRv;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard1);
         mRv = findViewById(R.id.rv);

        ImageButton btn = (ImageButton) findViewById(R.id.addimage);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            btn.setVisibility(GONE);
             value = extras.getString("key");
            getRegisteredEmployees1();
        }
        else {
            getRegisteredEmployees();
        }





        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewTask.this);
                builder.setTitle("New Task");

                final EditText input = new EditText(NewTask.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final HashMap<String, Object> userData=new HashMap<>();
                        userData.put(String.valueOf(i+1),input.getText().toString());


                        userRef.child(mAuth.getUid()).child("Task").updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {


                                }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NewTask.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        Log.e("why",e.toString());
                                    }
                                });


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }

        });
    }

    private void getRegisteredEmployees() {

        userRef.child(mAuth.getUid()).child("Task").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        number.add(child.getKey());
                        wrote.add(String.valueOf(child.getValue()));
                    }
                    i =  number.size();
                    mRv.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    mRv.setLayoutManager(layoutManager);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRv.getContext(),
                            layoutManager.getOrientation());
                    mRv.addItemDecoration(dividerItemDecoration);

                    PersonLeaveAdapter mAdapter = new PersonLeaveAdapter(NewTask.this, number, wrote);
                    mRv.setAdapter(mAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getRegisteredEmployees1() {

        userRef.child(value).child("Task").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        number.add(child.getKey());
                        wrote.add(String.valueOf(child.getValue()));
                    }
                    i =  number.size();
                    mRv.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    mRv.setLayoutManager(layoutManager);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRv.getContext(),
                            layoutManager.getOrientation());
                    mRv.addItemDecoration(dividerItemDecoration);

                    PersonLeaveAdapter mAdapter = new PersonLeaveAdapter(NewTask.this, number, wrote);
                    mRv.setAdapter(mAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    }

