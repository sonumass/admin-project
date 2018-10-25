package com.sonu.resdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;

import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

public class ShimmerActivity extends AppCompatActivity {

    ImageView ivImage;

    TextView tvHello;

    TextView tvHello2;

    ShimmerFrameLayout shimmerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shimmer);
        shimmerView=(ShimmerFrameLayout)findViewById(R.id.shimmer_view);
        ivImage=(ImageView)findViewById(R.id.iv_image);
        tvHello=(TextView)findViewById(R.id.tv_hello);
        tvHello2=(TextView)findViewById(R.id.tv_hello_2);
        shimmerView.startShimmerAnimation();
        mHandler.sendEmptyMessageDelayed(101, 5000);

    }
    /**
     * Resetting to default values
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 101) {
                tvHello.setText("Loading task completed..");
                tvHello2.setText("Loading task completed..");
                ivImage.setImageResource(R.drawable.ic_launcher_background);
                shimmerView.stopShimmerAnimation();
                reset();
            }
            return false;
        }
    });
    private void reset()
    {
        tvHello.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tvHello2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
}
