package com.example.j.soundclouddemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.j.soundclouddemo.R;

/**
 * Home page of the list view of moods for the user to pick.
 */

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /*
        These methods each have a preloaded string inside each button function
        with the putExtra that will call the search for query method inside the MainActivity class
        for the list of the specific songs corresponding with that string depending
        with mood button the user chooses.
     */

    public void getHappy(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Integer.toString(R.string.mood_key), "happy");
        startActivity(intent);
    }

    public void getSad(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Integer.toString(R.string.mood_key), "sad");
        startActivity(intent);
    }

    public void getAngry(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Integer.toString(R.string.mood_key), "angry");
        startActivity(intent);
    }

    public void getExcited(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Integer.toString(R.string.mood_key), "excited");
        startActivity(intent);
    }

    public void getRelaxed(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Integer.toString(R.string.mood_key), "relaxed");
        startActivity(intent);
    }
}
