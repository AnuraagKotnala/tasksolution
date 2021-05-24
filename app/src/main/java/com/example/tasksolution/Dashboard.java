package com.example.tasksolution;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("User");
    RecyclerView mRv;
    private AllRegisteredEmployeesAdapter mAdapter;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    TextView Name, Age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mRv = findViewById(R.id.rv);
        Name = findViewById(R.id.showname);
        Age = findViewById(R.id.showage);
        getRegisteredEmployees();
        getRegisteredEmployees1();
        getRegisteredEmployees2();

        setAdapter();

        ImageButton btn = (ImageButton) findViewById(R.id.addimage);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, NewTask.class));

            }
        });
        }

    private void setAdapter() {
        mRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Dashboard.this);
        mRv.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRv.getContext(),
                layoutManager.getOrientation());
        mRv.addItemDecoration(dividerItemDecoration);
        mAdapter=new AllRegisteredEmployeesAdapter(Dashboard.this, new ArrayList<>(),
                new AllRegisteredEmployeesAdapter.OnItemClickListener() {
                    @Override
                    public void onEdit(UserClass personModel, int position) {
                        String value=personModel.getUid();
                        Intent i = new Intent(Dashboard.this, NewTask.class);
                        i.putExtra("key",value);
                        startActivity(i);


                    }
        });
        mRv.setAdapter(mAdapter);

    }

    private void getRegisteredEmployees() {

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<UserClass> personModelArrayList = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        UserClass personModel = ds.getValue(UserClass.class);
                        assert personModel != null;
                        Log.e("getkey",ds.getKey());

                        personModelArrayList.add(personModel);

                    }

                    if(mAdapter!=null) {
                        mAdapter.addList(personModelArrayList);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getRegisteredEmployees1() {

        userRef.child(mAuth.getUid()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Name.setText(String.valueOf(snapshot.getValue()));  //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getRegisteredEmployees2() {

        userRef.child(mAuth.getUid()).child("age").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Age.setText(String.valueOf(snapshot.getValue()));  //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

