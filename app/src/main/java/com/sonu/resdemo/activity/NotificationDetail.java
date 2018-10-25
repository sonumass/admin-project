package com.sonu.resdemo.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.sonu.resdemo.R;

public class NotificationDetail extends AppCompatActivity {

  String message;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notification_detail);
    Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle("Notification detail");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    message=getIntent().getStringExtra("message");

    TextView txt_message=(TextView)findViewById(R.id.txt_message);
    txt_message.setText(message);

  }
  @Override
  public void onBackPressed() {

    Intent intent=new Intent(NotificationDetail.this,HomeActivity.class);
    startActivity(intent);
    finish();
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();

    if (id == android.R.id.home) {
      // Toast.makeText(UserDetails.this, "1", Toast.LENGTH_SHORT).show();

      Intent intent=new Intent(NotificationDetail.this,HomeActivity.class);
      startActivity(intent);
      finish();

      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
