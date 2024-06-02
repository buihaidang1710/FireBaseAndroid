package com.example.firebase_giandon;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class EditDeleteActivity extends AppCompatActivity {
    EditText txtName, txtDesc, txtDate;
    Button btnEdit, btnDelete,btnSelectDate;
    ImageView img;
    private String key="";
    private int mYear, mMonth, mDay;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        key=getIntent().getStringExtra("key");
        myRef= FirebaseDatabase.getInstance().getReference().
                child("worklist").child(key);
        displayWork();
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c=Calendar.getInstance();
                mYear=c.get(Calendar.YEAR);
                mMonth=c.get(Calendar.MONTH);
                mDay=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(EditDeleteActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                txtDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                            }
                        },mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editWork();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xoa cong viec");
                builder.setMessage("Co chac muon xoa?");
                builder.setIcon(R.drawable.remove);
                builder.setNegativeButton("Bo qua",null);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteWork();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });
    }
    private void deleteWork() {
        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditDeleteActivity.this,
                        "Xoa thanh cong!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(EditDeleteActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void editWork() {
        String name=txtName.getText().toString();
        String desc=txtDesc.getText().toString();
        String date=txtDate.getText().toString();
        if(name.isEmpty()){
            Toast.makeText(this, "khong de trong ten", Toast.LENGTH_SHORT).show();
        }else if(desc.isEmpty()){
            Toast.makeText(this, "khong de trong mieu ta", Toast.LENGTH_SHORT).show();
        }else if(date.isEmpty()){
            Toast.makeText(this, "khong de trong ngay", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String,Object> work=new HashMap<>();
            work.put("key",key);
            work.put("name",name);
            work.put("desc",desc);
            work.put("date",date);
            myRef.updateChildren(work).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(EditDeleteActivity.this,
                                "Sua thanh cong!", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(EditDeleteActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
    private void displayWork() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name=snapshot.child("name").getValue().toString();
                    String desc=snapshot.child("desc").getValue().toString();
                    String date=snapshot.child("date").getValue().toString();
                    String image=snapshot.child("image").getValue().toString();
                    txtName.setText(name);
                    txtDesc.setText(desc);
                    txtDate.setText(date);
                    Picasso.get().load(image).into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDesc);
        txtDate = findViewById(R.id.txtDate);

        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnSelectDate=findViewById(R.id.btnChosseDay);
        img=findViewById(R.id.img);
    }
}