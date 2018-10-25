package com.sonu.resdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import com.sonu.resdemo.R;
import com.sonu.resdemo.model.MenuModel;
import com.sonu.resdemo.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.sonu.resdemo.fragment.MenuFragment.newInstance;

public class MenuActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    ArrayList<MenuModel> arr;
    private  ArrayList<MenuModel> colors ;
    Preferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        pref=new Preferences(MenuActivity.this);
        colors=new ArrayList<MenuModel>();
        arr=new ArrayList<MenuModel>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Toast.makeText(MenuActivity.this,"Home ",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_messages:
                        Toast.makeText(MenuActivity.this,"Message  ",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_friends:
                        Toast.makeText(MenuActivity.this,"Firends  ",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_discussion:
                        Toast.makeText(MenuActivity.this,"Discussion  ",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sub_item1:
                        Toast.makeText(MenuActivity.this,"Sub Item1  ",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sub_item2:
                        Toast.makeText(MenuActivity.this,"Sub Item2  ",Toast.LENGTH_SHORT).show();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

        }


        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        for (int i = 0; i <colors.size(); i++) {
            adapter.addFragment(newInstance(colors.get(i).getMenu_code(),"param2"), colors.get(i).getMenu_name());
        }

        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
