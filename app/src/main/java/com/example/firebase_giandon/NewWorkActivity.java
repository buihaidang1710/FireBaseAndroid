package com.example.firebase_giandon;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class NewWorkActivity extends AppCompatActivity {
    private static final String TAG = "To";
    EditText txtName, txtDesc, txtDate;
    Button btnSaveTask, btnCancel;
    ImageView img;
    private final static int galleryPick=1;
    private Uri imageUri;
    private String saveCurDate,saveCurTime;
    private String downloadImgUrl,randomKey;
    private StorageReference imgRef;
    private DatabaseReference myRef;
    Integer NumWork = new Random().nextInt();
    String keytodo = Integer.toString(NumWork);
    Button btnSelectDate;
    private String name,desc,date;
    private int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_work);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        imgRef= FirebaseStorage.getInstance().getReference().child("work image");
        myRef= FirebaseDatabase.getInstance().getReference().child("worklist");
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c=Calendar.getInstance();
                mYear=c.get(Calendar.YEAR);
                mMonth=c.get(Calendar.MONTH);
                mDay=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(NewWorkActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                txtDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                            }
                        },mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }
    private void validateData(){
        name=txtName.getText().toString();
        desc=txtDesc.getText().toString();
        date=txtDate.getText().toString();
        if(imageUri==null){
            Toast.makeText(this, "Chon anh", Toast.LENGTH_SHORT).show();
        }else if(desc.isEmpty()){
            Toast.makeText(this, "chua mo ta viec", Toast.LENGTH_SHORT).show();
        }else if(name.isEmpty()){
            Toast.makeText(this, "nhap ten viec", Toast.LENGTH_SHORT).show();
        }else{
            storeWork();
        }
    }
    private void storeWork(){
        Calendar c=Calendar.getInstance();
        SimpleDateFormat curDate=new SimpleDateFormat("dd-MM-yyyy");
        saveCurDate=curDate.format(c.getTime());
        SimpleDateFormat curTime=new SimpleDateFormat("HH:mm:ss");
        saveCurTime=curTime.format(c.getTime());
        randomKey=saveCurDate+"-"+saveCurTime;
        StorageReference filePath=imgRef.child(
                imageUri.getLastPathSegment()+randomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(NewWorkActivity.this,
                        "error: "+message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NewWorkActivity.this,
                        "up anh thanh cong!", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImgUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        downloadImgUrl=task.getResult().toString();
                        Toast.makeText(NewWorkActivity.this,
                                "Luu Url anh thanh cong!", Toast.LENGTH_SHORT).show();
                        saveWorktoDatabas();
                    }
                });
            }
        });
    }
    private void saveWorktoDatabas(){
        HashMap<String,Object> work=new HashMap<>();
        work.put("key",TAG+randomKey);
        work.put("name",name);
        work.put("desc",desc);
        work.put("date",date);
        work.put("image",downloadImgUrl);
        myRef.child(TAG+randomKey).updateChildren(work)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                            Intent intent = new Intent(NewWorkActivity.this,
                                    MyReceiver.class);
                            intent.putExtra("myAction", "mDoNotify");
                            intent.putExtra("Name",name);
                            intent.putExtra("Description", desc);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(NewWorkActivity.this,
                                    0, intent, 0);
                            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            Intent intent1=new Intent(NewWorkActivity.this,
                                    MainActivity.class);
                            startActivity(intent1);
                            Toast.makeText(NewWorkActivity.this, "Them thanh cong!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(NewWorkActivity.this,
                                    "Them khog thanh cong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void openGallery(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            img.setImageURI(imageUri);
        }
    }

    private void initView() {
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDesc);
        txtDate = findViewById(R.id.txtDate);

        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);
        btnSelectDate=findViewById(R.id.btnChosseDay);
        img=findViewById(R.id.img);
    }
}