package com.sonu.resdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sonu.resdemo.R;
import com.sonu.resdemo.utils.CommonFunctions;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

  protected TextView link_to_login,btnRegistration;
  EditText edt_name,edt_mobile_no,edt_email,edit_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
      link_to_login=(TextView)findViewById(R.id.link_to_login);
      link_to_login.setOnClickListener(this);
      btnRegistration=(TextView)findViewById(R.id.btnRegistration);
      btnRegistration.setOnClickListener(this);
      edt_name=(EditText)findViewById(R.id.edt_name);
      edt_mobile_no=(EditText)findViewById(R.id.edt_mobile_no);
      edt_email=(EditText)findViewById(R.id.edt_email);
      edit_password=(EditText)findViewById(R.id.edit_password);
    }

  @Override
  public void onClick(View v) {
      switch (v.getId()){
        case R.id.link_to_login:
          Intent intent=new Intent(RegisterActivity.this,LoginScreen.class);
          startActivity(intent);
          finish();
          break;
        case R.id.btnRegistration:
          if(CommonFunctions.isEmpty(edt_name)){
            edt_name.requestFocus();
            edt_name.setError("Please enter name");
          }else if(CommonFunctions.isEmpty(edt_mobile_no)){
            edt_mobile_no.requestFocus();
            edt_mobile_no.setError("Please enter mobile");
          }else if(CommonFunctions.isEmpty(edt_email)){
            edt_email.requestFocus();
            edt_email.setError("Please enter email ID");
          }else if(CommonFunctions.isEmpty(edit_password)){
            edit_password.requestFocus();
            edit_password.setError("Pleas enter password");
          }else if(edt_mobile_no.getText().toString().length()<10){
            edt_mobile_no.requestFocus();
            edt_mobile_no.setError("Pleas enter valid mobile");
          }else if(!CommonFunctions.emailValidator(edt_email.getText().toString().trim())){
            edt_email.requestFocus();
            edt_email.setError("Please enter valid email ID");
          }else {
            Intent intentregistration=new Intent(RegisterActivity.this,MenuActivity.class);
            startActivity(intentregistration);
            finish();
          }
          break;
      }

  }
}
