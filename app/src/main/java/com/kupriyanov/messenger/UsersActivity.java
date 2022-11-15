package com.kupriyanov.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseUser;

public class UsersActivity extends AppCompatActivity {

    private UsersViewModel viewModel;
/*    private static final String EXTRA_NAME = "name";
    private static final String EXTRA_LAST_NAME = "lastName";
    private static final String EXTRA_AGE = "age";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        /*String name = getIntent().getStringExtra(EXTRA_NAME);
        String lastName = getIntent().getStringExtra(EXTRA_NAME);
        Integer age = getIntent().getIntExtra(EXTRA_AGE);*/
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser == null) {
                    Intent intent = LoginActivity.newIntent(UsersActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_logout) {
            viewModel.logout();
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, UsersActivity.class);
    }

    /*public static Intent newIntent(
            Context context,
            String name,
            String lastName,
            Integer age
    ) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_LAST_NAME, lastName);
        intent.putExtra(EXTRA_AGE, age);
        return intent;
    }*/
}