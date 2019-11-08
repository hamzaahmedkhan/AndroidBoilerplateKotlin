package com.android.structure.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.WindowManager;

import com.android.structure.helperclasses.RunTimePermissions;
import com.android.structure.managers.SharedPreferenceManager;

import java.util.List;

import com.android.structure.R;

import com.android.structure.fragments.LoginFragment;
import com.android.structure.fragments.abstracts.BaseFragment;


public class MainActivity extends BaseActivity {

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        RunTimePermissions.verifyStoragePermissions(this);
        initFragments();
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getTitlebarLayoutId() {
        return R.id.titlebar;
    }


    @Override
    protected int getDrawerLayoutId() {
        return R.id.drawer_layout;
    }

    @Override
    protected int getDockableFragmentId() {
        return R.id.contMain;
    }

    @Override
    protected int getDrawerFragmentId() {
        return R.id.contDrawer;
    }

    private void initFragments() {
        if (SharedPreferenceManager.getInstance(getApplicationContext()).getCurrentUser() == null) {
            addDockableFragment(LoginFragment.newInstance(), false);
        } else {
            openActivity(HomeActivity.class);
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (DeviceUtils.isRooted(getApplicationContext())) {
//            showAlertDialogAndExitApp("This device is rooted. You can't use this app.");
//        }
    }

    @Override
    public void onBackPressed() {
        /**
         * Show Close app popup if no or single fragment is in stack. otherwise check if drawer is open. Close it..
         */

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                BaseFragment fragment = (BaseFragment) fragments.get(fragments.size() - 1);
                fragment.setTitlebar(titleBar);
            }
        } else {
            closeApp();
        }
    }


    public void showAlertDialogAndExitApp(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialog.show();
    }
}