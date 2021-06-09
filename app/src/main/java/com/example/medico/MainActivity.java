package com.example.medico;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

EditText profession;
EditText mobileNumber;
EditText name;
    TextView txtDate;
    Calendar c;
    DatePickerDialog dpd;
    Button updatePic;
    ImageView profilePic;
    Button submitButton;
    Uri selectedImageUri;
    EditText address;
    RadioButton male;
    RadioButton female;
    RadioButton others;
    Uri my_uri;


    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 3;
    private String mUsername;
    private TextView username_TextView;
    private Button signoutButton;
    private RelativeLayout mainLayout;
    StorageReference photoref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//sushant's code

        txtDate=findViewById(R.id.txtDate);
        profilePic=findViewById(R.id.profilePic);
        updatePic=findViewById(R.id.updatePic);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        others=findViewById(R.id.others);
        submitButton=findViewById(R.id.submitButton);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c= Calendar.getInstance();
                int day=c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);
                dpd=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        txtDate.setText(day + "/" + (month + 1) + "/" + year);
                    }
                },day,month,year);
                dpd.show();

            }
        });

//for updating profile picture

        updatePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                uploadtofirebase();

            }
        });
        //sushant's code


        mainLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        signoutButton = (Button)findViewById(R.id.signoutButton);




        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //sign in

                    onSignedINInisilise(user.getDisplayName());
                    mainLayout.setVisibility(View.VISIBLE);


                } else {
                    //sign out
                    onSignedOutInisilise();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(true)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    ))
                                    .setLogo(R.drawable.logo2)
                                    .setTheme(R.style.Theme_Medico)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(MainActivity.this);
                finish();

            }
        });




    }

    private void uploadtofirebase() {
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("File Uploader");
        dialog.show();
        FirebaseStorage storage=FirebaseStorage.getInstance();

        StorageReference uploader=storage.getReference().child("profile");
        photoref=uploader.child(selectedImageUri.getLastPathSegment());
        photoref.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "File uploaded", Toast.LENGTH_LONG).show();
                        photoref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                my_uri=uri;

                                profession=(EditText)findViewById(R.id.profession);
                                mobileNumber=(EditText)findViewById(R.id.mobileNumber);
                                address=(EditText)findViewById(R.id.address);
                                name=(EditText)findViewById(R.id.name);
                                String s1=male.getText().toString();
                                String s2=female.getText().toString();
                                String s3=others.getText().toString();

                                String pro=profession.getText().toString().trim();
                                String num=mobileNumber.getText().toString().trim();
                                String addr=address.getText().toString().trim();
                                String date=txtDate.getText().toString().trim();
                                String nam=name.getText().toString().trim();
                                String s;
                                String photourl=my_uri.toString();
                                if(male.isChecked()) {
                                    s=s1;
                                }
                                else if(female.isChecked()){
                                    s=s2;
                                }
                                else
                                {
                                    s=s3;
                                }
                                dataholder obj=new dataholder(pro,num,addr,date,nam,s,photourl);

                                FirebaseDatabase db=FirebaseDatabase.getInstance();
                                DatabaseReference node=db.getReference("user");
                                node.push().setValue(obj);
                                profession.setText("");
                                mobileNumber.setText("");
                                address.setText("");
                                txtDate.setText("");
                                name.setText("");
                                Toast.makeText(getApplicationContext(),"value Inserted",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(MainActivity.this,HomeScreen.class);
                                startActivity(intent);
                                finish();

                            }
                        });





                    }



                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                        float percentage =(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded : "+(int)percentage+" %");
                    }
                });
    }

    //second copy of sushant

    private void chooseProfilePicture(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_profile_picture, null);
        builder.setCancelable(false);
        builder.setView(dialogView);


        ImageView imageViewADDPPCCamera = dialogView.findViewById(R.id.imageViewADPPCCamera);
        ImageView imageViewADPPGallery = dialogView.findViewById(R.id.imageViewADPPCGallery);

        final AlertDialog alertDialogProfilePicture = builder.create();
        alertDialogProfilePicture.show();
        alertDialogProfilePicture.setCanceledOnTouchOutside(true);
        alertDialogProfilePicture.getWindow().setLayout(500,200);


        imageViewADDPPCCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermissions()) {
                    takePictureFromCamera();
                    alertDialogProfilePicture.dismiss();
                }
            }
        });

        imageViewADPPGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromGallery();
                alertDialogProfilePicture.dismiss();
            }
        });
    }

    private void takePictureFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void takePictureFromCamera(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePicture, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            if (resultCode == RESULT_OK) {
                 selectedImageUri = data.getData();
                profilePic.setImageURI(selectedImageUri);
            }
        }
        else if(requestCode==2){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                Bitmap bitmapImage = (Bitmap) bundle.get("data");
                profilePic.setImageBitmap(bitmapImage);
            }
        }
        else if(requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "You Successfully Signed In", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Sign In Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean checkAndRequestPermissions(){
        if(Build.VERSION.SDK_INT >= 23){
            int cameraPermission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
            if(cameraPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            takePictureFromCamera();
        }
        else
            Toast.makeText(MainActivity.this, "Permission not Granted", Toast.LENGTH_SHORT).show();
    }

//upto here for update image

    //

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener!=null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignedOutInisilise() {

        mUsername = "Unknown";

    }


    private void onSignedINInisilise(String username) {

        mUsername = username;
        EditText editText = (EditText)findViewById(R.id.name);
        editText.setText(mUsername);
//        Glide.with(getApplicationContext())
//                .load(photoUrl)
//                .asBitmap()
//                .centerCrop()
//                .into(profilePic);

    }

}