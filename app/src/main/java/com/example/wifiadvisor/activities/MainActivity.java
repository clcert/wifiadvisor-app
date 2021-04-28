package com.example.wifiadvisor.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;


import com.example.wifiadvisor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.wifiadvisor.R.layout.main;

/*
https://www.tutorialspoint.com/how-to-change-android-overflow-menu-icon-programmatically
https://developer.android.com/training/volley/requestqueue#java
https://github.com/kaushikgopal/moshi
https://developer.android.com/codelabs/basic-android-kotlin-training-recyclerview-scrollable-list#2


https://medium.com/@sidhanth/android-recyclerview-and-volley-c6458cc65cd
 https://stackoverflow.com/questions/43853112/how-to-serialize-arraylistfloat-using-moshi-json-library-for-android/61272734#61272734
 https://stackoverflow.com/questions/8322207/how-to-use-onblur-function-on-edittext-in-android
 */
public class MainActivity extends AppCompatActivity {
    int intentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(main);
        if(getIntent().getExtras()!= null){
            intentFragment = getIntent().getExtras().getInt("results");
        }
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        if(intentFragment == R.id.navigation_results){
            navController.navigate(R.id.navigation_results);
        }
    }
}
