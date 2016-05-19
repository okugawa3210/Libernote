package com.okugawa3210.libernote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.fragment.MemoMainListFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.content);
        NavigationView sideView = (NavigationView) findViewById(R.id.side_view);
        ActionBar actionBar = getSupportActionBar();

        assert toolbar != null && drawer != null && sideView != null && actionBar != null;

        actionBar.setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        sideView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new MemoMainListFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case R.id.side_menu_search:
                intent = new Intent(MainActivity.this, MemoSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.side_menu_create:
                intent = new Intent(MainActivity.this, MemoFormActivity.class);
                startActivity(intent);
                break;
            case R.id.side_menu_tag:
                intent = new Intent(MainActivity.this, TagListActivity.class);
                startActivity(intent);
                break;
            case R.id.side_menu_trash:
                intent = new Intent(MainActivity.this, MemoTrashActivity.class);
                startActivity(intent);
                break;
            case R.id.side_menu_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                return false;
        }

        drawer.closeDrawers();

        return true;
    }
}
