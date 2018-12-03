package tech.kuweka.kuweka;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import custom_keyboard.Letter_Keyboard;
import methods.Prompts;

public class Activity_Main extends AppCompatActivity {

    private FrameLayout fragmentContainer;
    private Letter_Keyboard myLetterKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init UI
        BottomNavigationView bottomNavigationToolbar = findViewById(R.id.bottomNavigationView);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        myLetterKeyboard = findViewById(R.id.letter_keyboard);

        // Hide the standard keyboard initially
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Event Listeners
        bottomNavigationToolbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.navigation_dashboard:
                        Prompts.toastMessageShort(getBaseContext(),"Dash");
                        return true;
                    case R.id.navigation_catalog:
                        Fragment catalogFragment = new Fragment_Catalog();
                        openFragment(catalogFragment);
                        return true;
                    case R.id.navigation_reports:
                        return true;
                }

                return false;
            }
        });

        // Override Reselect
        bottomNavigationToolbar.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {}
        });
    }

    public Letter_Keyboard getMyLetterKeyboard() {
        return myLetterKeyboard;
    }

    private void openFragment(final Fragment myFragment){

        myLetterKeyboard.hideIfVisible();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer.getId(), myFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        if(myLetterKeyboard.hideIfVisible()){
            return;
        }

        finish();
        Intent i = new Intent(Activity_Main.this, Activity_Login.class);
        i.putExtra("fromMain",true);
        startActivity(i);
    }
}
