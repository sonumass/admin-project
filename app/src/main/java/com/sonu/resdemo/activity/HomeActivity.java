package com.sonu.resdemo.activity;

/**
 * Created by Devesh D on 2/22/2018.
 */
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.sonu.resdemo.R;
import com.sonu.resdemo.app.Config;
import com.sonu.resdemo.fragment.MenuFragment;
import com.sonu.resdemo.model.MenuModel;
import com.sonu.resdemo.utils.CommonFunctions;
import com.sonu.resdemo.utils.NotificationUtils;
import com.sonu.resdemo.utils.Preferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.sonu.resdemo.fragment.MenuFragment.newInstance;
public class HomeActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    public static final int RequestPermissionCode = 7;
    private TabLayout tabLayout;
    ArrayList<MenuModel> arr;
    private  ArrayList<MenuModel> colors ;
    private static String TAG = HomeActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    Preferences pref;
  private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        pref=new Preferences(HomeActivity.this);
        colors=new ArrayList<MenuModel>();
        arr=new ArrayList<MenuModel>();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        marshmallowcondition();
       String str=pref.getString("list");
try {
    JSONObject jsonObject=new JSONObject(str);

    String s=jsonObject.getString("success");
    switch (s){

        case "1":

            JSONArray json=jsonObject.getJSONArray("data");
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonnewsobject = json.getJSONObject(i);

            String name=jsonnewsobject.getString("menu_name");
            String menu_code=jsonnewsobject.getString("menu_code");
            String coupon = jsonnewsobject.getString("coupon");
            MenuModel menu=new MenuModel(name,menu_code,coupon);
            colors.add(menu);

        }

            break;
        case "0":
            break;

    }
} catch (JSONException e) {
    Log.e("error",str);
}
    /*  mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

          if(intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
            // new push notification is received

            String message = intent.getStringExtra("message");

            //  Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
            alertDialogBuilder.setMessage(message);
           final AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialogBuilder.setPositiveButton("OK",
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                  alertDialog.dismiss();
                }
              });




            alertDialog.show();
          }


        }
      };

*/
      /*  if (null != b) {
            arr = b.getString("list");
            Log.i("List", "Passed Array List :: " + arr);
        }
        for (int i = 0; i < arr.size(); i++) {
            colors.add(arr.get(i));
        }*/
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        for (MenuModel color : colors) {
            tabLayout.addTab(tabLayout.newTab().setText(color.getMenu_name()));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int colorFrom = ((ColorDrawable) mToolbar.getBackground()).getColor();
                int colorTo = getColorForTab(tab.getPosition());

                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(1000);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int color = (int) animator.getAnimatedValue();

                        mToolbar.setBackgroundColor(color);
                        tabLayout.setBackgroundColor(color);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                    }

                });
                colorAnimation.start();
                mToolbar.setTitle(colors.get(tab.getPosition()).getMenu_name());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // display the first navigation drawer view on app launch
        displayView(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id == R.id.action_search){
            Intent intent =new Intent(HomeActivity.this,SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                MenuFragment frag = new MenuFragment();
                fragment=newInstance(colors.get(position).getMenu_code(),"param2");
                title = "BESTSELLERS";
                break;
            case 1:
              Intent intent=new Intent(HomeActivity.this,MyOrderList.class);
              startActivity(intent);
                break;
            case 2:
                CommonFunctions.sharelink(HomeActivity.this);
                break;
            case 3:

                Intent sendintent=new Intent(HomeActivity.this,FeedbackActivity.class);
                startActivity(sendintent);
                break;
            case 4:
               /* Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +getPackageName()));
                startActivity(rateIntent);*/
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mindshapers.apps"));
                startActivity(rateIntent);
                break;
                case 5:
                  Intent couponintent=new Intent(HomeActivity.this,CouponActivity.class);
                  startActivity(couponintent);
            break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public int getColorForTab(int position) {
        String title=null;
        Fragment fragment = null;

            MenuFragment frag = new MenuFragment();
            fragment=newInstance(colors.get(position).getMenu_code(),"param2");
            title = "BESTSELLERS";

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();

                // set the toolbar title
                getSupportActionBar().setTitle(title);
            }
            return ContextCompat.getColor(this, R.color.colorPrimary);



    }
public void marshmallowcondition(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        RequestMultiplePermission();
    }

}
    public void StringView(){
        Toast.makeText(HomeActivity.this,"test",Toast.LENGTH_SHORT).show();
    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]
                {
                        ACCESS_FINE_LOCATION,
                        GET_ACCOUNTS,
                        READ_CONTACTS,
                        READ_PHONE_STATE,
                        WRITE_EXTERNAL_STORAGE,

                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean Accessfinelocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean Get_Accounts = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean Read_contact = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean Read_Phone_State = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean Write_external_STORAGE = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (Accessfinelocation && Get_Accounts && Read_contact && Read_Phone_State &&Write_external_STORAGE) {

                       // Toast.makeText(HomeActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                       // Toast.makeText(HomeActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    // Checking permission is enabled or not using function starts from here.
    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int FiFthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FiFthPermissionResult == PackageManager.PERMISSION_GRANTED ;
    }
}
