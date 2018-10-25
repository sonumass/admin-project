package com.sonu.resdemo.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sonu.resdemo.R;
import com.sonu.resdemo.scripts.Scripts;
import com.sonu.resdemo.utils.CommonFunctions;

import org.json.JSONObject;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{
EditText edt_feedback ,edt_mobile;
TextView txt_send;
String mobile,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        init();
    }
    public void init(){
        edt_feedback=(EditText)findViewById(R.id.edt_feedback);
        edt_mobile=(EditText)findViewById(R.id.edt_mobile_no);
        txt_send=(TextView)findViewById(R.id.txt_send);
        txt_send.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {

        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Toast.makeText(UserDetails.this, "1", Toast.LENGTH_SHORT).show();

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_send:
                if(CommonFunctions.isEmpty(edt_feedback)){
                    edt_feedback.setError("Please enter feedback");
                    edt_feedback.requestFocus();
                }else if(CommonFunctions.isEmpty(edt_mobile)){
                    edt_mobile.setError("Please enter Mobile ");
                    edt_mobile.requestFocus();
                }else if(edt_mobile.getText().toString().length()<10){
                    edt_mobile.setError("Please enter valid mobile");
                    edt_mobile.requestFocus();
                }else {
                    mobile=edt_mobile.getText().toString();
                    message=edt_feedback.getText().toString();
                    if (CommonFunctions.isConnected(FeedbackActivity.this)) {
                        new SendFeedBackAsynTask().execute();
                    } else {
                        Toast.makeText(FeedbackActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    class SendFeedBackAsynTask extends AsyncTask<String, Void, String> {
        String f_id = "", field = "", path;
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(FeedbackActivity.this);
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Please wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            try{
                result= Scripts.SendFeedback(mobile,message,CommonFunctions.getDateTime());
            }catch (Exception e){
                Log.e("error","errroe");
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("script rasponse", s);
            progress.cancel();
            try{

                JSONObject jsonObject=new JSONObject(s);

                String str=jsonObject.getString("success");
                switch (str){
                    case "1":

                        finish();


                        break;
                    case "0":
                        Toast.makeText(FeedbackActivity.this,"Somthing want wrong!", Toast.LENGTH_LONG).show();
                        break;
                }

            }catch (Exception e){
                Log.e("error","error in branch uploaded data");
            }


        }

    }
}
