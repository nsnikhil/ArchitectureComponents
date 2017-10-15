package com.nrs.nsnik.architecturecomponents.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.nrs.nsnik.architecturecomponents.BuildConfig;
import com.nrs.nsnik.architecturecomponents.MyApplication;
import com.nrs.nsnik.architecturecomponents.R;
import com.nrs.nsnik.architecturecomponents.view.fragments.ListFragment;
import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)Toolbar mToolbar;
    private static final String[] FRAGMENT_TAGS = {"listFragment"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        setSupportActionBar(mToolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer,new ListFragment(),FRAGMENT_TAGS[0]).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            RefWatcher refWatcher = MyApplication.getRefWatcher(this);
            refWatcher.watch(this);
        }
    }
}
