package com.foodeze.rider.ActivitiesAndFragments.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.foodeze.rider.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

import javax.microedition.khronos.egl.EGLDisplay;

public class SignUpActivity extends AppCompatActivity {
    private Spinner selectTitle,selectVehical,selectValidLicense;
    private EditText firstName,sureName,cellPhoneNumber,emailAddress,BradnOfVehical,ModelOfVehical,ColorOFVehical,DriversLicenseNo,
            LicenseExpiryData,PassportNumber,StreetAddress,BussinessName,UnitNumber,Suburb,Province,EmergenContactNumber,
            AlterNativeContactNumber;
    private Button submitButton;
    private CheckBox conditionAccepted;
    private ImageView  UploadIdDocumentButton,UploadDriversLicense;
    private TextView allreadyHaveanAccountLink,drivery_number,expiryDate,driveryLicenseDocument;
    private static final int GalleryPick=1,GalleryPick2=1;
    private Bitmap currentImage;
    private ProgressDialog loadingBar;
    private String userTitle,isLicense,typeOfVehical;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializer();
        spinners();




        allreadyHaveanAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });

        selectTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userTitle=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectValidLicense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String decision=parent.getItemAtPosition(position).toString();
                if(decision.equalsIgnoreCase("No")) {

                    drivery_number.setEnabled(false);
                    drivery_number.setTextColor(Color.GRAY);
                    expiryDate.setEnabled(false);
                    expiryDate.setTextColor(Color.GRAY);
                    driveryLicenseDocument.setEnabled(false);
                    driveryLicenseDocument.setTextColor(Color.GRAY);
                    UploadDriversLicense.setEnabled(false);
                    UploadDriversLicense.setBackgroundColor(Color.TRANSPARENT);
                    DriversLicenseNo.setEnabled(false);
                    DriversLicenseNo.setTextColor(Color.GRAY);
                    LicenseExpiryData.setEnabled(false);
                    LicenseExpiryData.setTextColor(Color.GRAY);
                    isLicense=decision;

                }

                else {

                    drivery_number.setEnabled(true);
                    drivery_number.setTextColor(Color.BLACK);
                    expiryDate.setEnabled(true);
                    expiryDate.setTextColor(Color.BLACK);
                    driveryLicenseDocument.setEnabled(true);
                    driveryLicenseDocument.setTextColor(Color.BLACK);
                    UploadDriversLicense.setEnabled(true);
                    UploadDriversLicense.setBackgroundColor(Color.WHITE);
                    DriversLicenseNo.setEnabled(true);
                    DriversLicenseNo.setTextColor(Color.BLACK);
                    LicenseExpiryData.setEnabled(true);
                    LicenseExpiryData.setTextColor(Color.BLACK);
                    isLicense=decision;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectVehical.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfVehical=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        UploadIdDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);
              }
        });

        UploadDriversLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick2);
            }
        });






            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String fname, sname, cellNumber, email, vehicalBrand, vehicalModel, vehicalColor, driverLicenseNumber, licenseExpireDate,
                            idNumber, address, Bname, uNumber, suburb, privenceTxt, etContact, AContact;
                    fname = firstName.getText().toString();
                    sname = sureName.getText().toString();
                    cellNumber = cellPhoneNumber.getText().toString();
                    email = emailAddress.getText().toString();
                    vehicalBrand = BradnOfVehical.getText().toString();
                    vehicalColor = ColorOFVehical.getText().toString();
                    vehicalModel = ModelOfVehical.getText().toString();
                    driverLicenseNumber = DriversLicenseNo.getText().toString();
                    licenseExpireDate = LicenseExpiryData.getText().toString();
                    idNumber = PassportNumber.getText().toString();
                    address = StreetAddress.getText().toString();
                    Bname = BussinessName.getText().toString();
                    uNumber = UnitNumber.getText().toString();
                    suburb = Suburb.getText().toString();
                    privenceTxt = Province.getText().toString();
                    etContact = EmergenContactNumber.getText().toString();
                    AContact = AlterNativeContactNumber.getText().toString();





                    if(isLicense.equalsIgnoreCase("yes")) {
                        if (TextUtils.isEmpty(fname)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter First Name", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(sname)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter SureName", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(email)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter your Email Addresss", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(vehicalBrand)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Vehical Brand", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(vehicalModel)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Vehical Model", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(vehicalColor)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Vehical Color", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(driverLicenseNumber)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Drivery License Number", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(licenseExpireDate)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter License Expiry Date", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(idNumber)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter ID/Passport Number", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(address)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Your Street Address", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(Bname)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter your Business Name", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(uNumber)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Unit Number", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(suburb)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Suburb", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(privenceTxt)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter your Province", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(etContact)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter your Emergency Contact Number", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(AContact)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter your AlterNative Contact Number", Toast.LENGTH_SHORT).show();

                        } else {
                            if (conditionAccepted.isChecked())

                                //Here with the help of object we can pass the value to the server.
                                // use all module object ..

                                Toast.makeText(SignUpActivity.this, "You have Created Your new account", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(SignUpActivity.this, "Please Accept Term and Condition", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                    else{

                        if (TextUtils.isEmpty(fname)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter First Name", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(sname)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter SureName", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(email)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter your Email Addresss", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(vehicalBrand)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Vehical Brand", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(vehicalModel)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Vehical Model", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(vehicalColor)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Vehical Color", Toast.LENGTH_SHORT).show();

                        }  else if (TextUtils.isEmpty(idNumber)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter ID/Passport Number", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(address)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Your Street Address", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(Bname)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter your Business Name", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(uNumber)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Unit Number", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(suburb)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter Suburb", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(privenceTxt)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter your Province", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(etContact)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter your Emergency Contact Number", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(AContact)) {
                            Toast.makeText(SignUpActivity.this, "Please Enter your AlterNative Contact Number", Toast.LENGTH_SHORT).show();

                        } else {
                            if (conditionAccepted.isChecked())

                                //Here with the help of object we can pass the value to the server.
                                // use all module object ..

                                Toast.makeText(SignUpActivity.this, "You have Created Your new account", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(SignUpActivity.this, "Please Accept Term and Condition", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                }
            });



    }

    private void spinners() {
        //spinner
        ArrayAdapter<CharSequence> setTitleSpinner =  ArrayAdapter.createFromResource(SignUpActivity.this, R.array.title,android.R.layout.simple_spinner_item);
        setTitleSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTitle.setAdapter(setTitleSpinner);

        ArrayAdapter<CharSequence> verhicalTypeSpinner =  ArrayAdapter.createFromResource(SignUpActivity.this, R.array.vehical,android.R.layout.simple_spinner_item);
        verhicalTypeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectVehical.setAdapter(verhicalTypeSpinner);

        ArrayAdapter<CharSequence> driveryLicenseSpinner =  ArrayAdapter.createFromResource(SignUpActivity.this, R.array.license_spring,android.R.layout.simple_spinner_item);
        driveryLicenseSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectValidLicense.setAdapter(driveryLicenseSpinner);
    }


    private void initializer() {
        allreadyHaveanAccountLink=(TextView)findViewById(R.id.allready_have_an_account_link);
        firstName=(EditText)findViewById(R.id.first_name);
        sureName=(EditText)findViewById(R.id.surename);
        cellPhoneNumber=(EditText)findViewById(R.id.contact_number);
        emailAddress=(EditText)findViewById(R.id.email);
        BradnOfVehical=(EditText)findViewById(R.id.brand_of_vehical);
        ModelOfVehical=(EditText)findViewById(R.id.model_of_vehical);
        ColorOFVehical=(EditText)findViewById(R.id.color_of_vehical);
        DriversLicenseNo=(EditText)findViewById(R.id.drivery_license_number);
        LicenseExpiryData=(EditText)findViewById(R.id.drivery_expire_date);
        PassportNumber=(EditText)findViewById(R.id.passport_number);
        StreetAddress=(EditText)findViewById(R.id.user_street_address);
        BussinessName=(EditText)findViewById(R.id.business_name);
        UnitNumber=(EditText)findViewById(R.id.unit_number);
        Suburb=(EditText)findViewById(R.id.suburb);
        Province=(EditText)findViewById(R.id.province);
        EmergenContactNumber=(EditText)findViewById(R.id.emergency_contact_number);
        AlterNativeContactNumber=(EditText)findViewById(R.id.Alternative_contact_number);


        UploadIdDocumentButton=(ImageView) findViewById(R.id.upload_document);
        UploadDriversLicense=(ImageView) findViewById(R.id.upload_drivery_license);
        submitButton=(Button)findViewById(R.id.submit_button);
        conditionAccepted=(CheckBox)findViewById(R.id.condition_check_box);
        selectTitle=(Spinner)findViewById(R.id.select_title);
        selectVehical=(Spinner)findViewById(R.id.select_vehical);
        selectValidLicense=(Spinner)findViewById(R.id.valid_licence);
        loadingBar=new ProgressDialog(this);
        drivery_number=(TextView)findViewById(R.id.drivery_license_number_txt);
        expiryDate=(TextView)findViewById(R.id.drivery_expire_date_txt);
        driveryLicenseDocument=(TextView)findViewById(R.id.upload_drivery_license_txt);


    }



    private void sendUserToLoginActivity() {

        Intent loginIntent=new Intent(SignUpActivity.this,LoginAcitvity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri ImageUri = data.getData();
        if(ImageUri!=null){
            try{
                currentImage= MediaStore.Images.Media.getBitmap(this.getContentResolver(),ImageUri);
                UploadIdDocumentButton.setImageBitmap(currentImage);

            }catch (Exception e){
                Toast.makeText(this, "Error : "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }

        }
    }

}
