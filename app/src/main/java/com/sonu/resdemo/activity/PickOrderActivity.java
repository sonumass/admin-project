package com.sonu.resdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sonu.resdemo.R;

public class PickOrderActivity extends AppCompatActivity  implements View.OnClickListener{
ImageView iv_take_away,iv_delivary;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pick_order);
    iv_take_away=(ImageView)findViewById(R.id.iv_take_away);
    iv_take_away.setOnClickListener(this);
    iv_delivary=(ImageView)findViewById(R.id.iv_delivary);
    iv_delivary.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.iv_take_away:

        break;
      case R.id.iv_delivary:
        break;
    }
  }
}
