package com.sonu.resdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sonu.resdemo.R;
import com.sonu.resdemo.utils.CommonFunctions;

/**
 * Created by Sonu Saini on 5/26/2017.
 */

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {
 protected    TextView txt_register,btn_login,btn_skip;

    EditText edit_password,edt_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
      edit_password=(EditText)findViewById(R.id.edit_password);
      edt_email=(EditText)findViewById(R.id.edt_email);
        txt_register=(TextView)findViewById(R.id.link_to_register);
        btn_login=(TextView) findViewById(R.id.btnLogin);
      btn_skip=(TextView)findViewById(R.id.btn_skip);
      btn_skip.setOnClickListener(this);
        btn_login.setOnClickListener(this);
txt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.link_to_register:
                Intent inte=new Intent(LoginScreen.this,RegisterActivity.class);
                startActivity(inte);
                finish();
                break;
            case R.id.btnLogin:
              if(edt_email.getText().toString().equals("")){
                edt_email.requestFocus();
                edt_email.setError("Please enter email");
              }else if(edit_password.getText().toString().equals("")){
                edit_password.requestFocus();
                edit_password.setError("Please enter password");
              }else if(!CommonFunctions.emailValidator(edt_email.getText().toString())){
                edt_email.requestFocus();
                edt_email.setError("Please enter valid email");
              }else {
                Intent intent=new Intent(LoginScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
              }

                break;
          case R.id.btn_skip:
            Intent intentskip=new Intent(LoginScreen.this,MainActivity.class);
            startActivity(intentskip);
            finish();
            break;
        }
    }
}
