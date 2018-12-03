package tech.kuweka.kuweka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import methods.Constants;
import methods.Prompts;
import methods.Text_Inputs;
import custom_keyboard.Keyboard_Interface;
import custom_keyboard.Letter_Keyboard;
import local_host.Preferences;

public class Activity_Login extends AppCompatActivity {

    // UI Objects
    private EditText editText_username;
    private EditText editText_password;
    private Letter_Keyboard myLetterKeyboard;

    // Libraries
    private Preferences myPreferences_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI Objects Init
        editText_username = findViewById(R.id.edittext_username);
        editText_password = findViewById(R.id.edittext_password);
        Button button_SignIn = findViewById(R.id.button_signin);
        TextView button_SignUp = findViewById(R.id.textview_signup);
        myLetterKeyboard = findViewById(R.id.letter_keyboard);

        // Hide the standard keyboard initially
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Set the Keyboard
        final EditText[] myEditTexts = new EditText[]{editText_username,editText_password};
        new Keyboard_Interface(this,myLetterKeyboard,myEditTexts);

        // Init Link to Preferences
        myPreferences_ = new Preferences(this);

        // Fetch Last Credentials from preferences
        String savedUsername = myPreferences_.getLastUsername();
        String savedPassword = null;
        if(savedUsername != null) {
            editText_username.setText(savedUsername);
            savedPassword = myPreferences_.getPassword(savedUsername);
        }

        // Fetch Intent, if we are coming from the main menu
        Intent i = getIntent();
        boolean fromMain = i.getBooleanExtra("fromMain",false);

        if (!fromMain) {
            if(savedUsername != null && savedPassword != null){
                signIn(savedUsername,savedPassword);
            }
        }

        // Set click listeners
        button_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if input values are valid
                if(Text_Inputs.isValid(getBaseContext(),myEditTexts,
                        new String[]{getString(R.string.username),getString(R.string.password)})){

                    myLetterKeyboard.hideIfVisible();

                    signIn(editText_username.getText().toString().trim(),
                            editText_password.getText().toString().trim());
                }
            }
        });

        button_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUpQuestions();
            }
        });
    }

    private void startSignUpQuestions(){

        Intent i = new Intent(Activity_Login.this, Activity_Questions.class);
        i.putExtra("titles",new String[]{"Step 1", "Step 2","Step 3", "Step 4", "Step 5"});
        i.putExtra("questions",new String[]{"Full Name", "Phone Number",
                "Location", "Account Username", "Account Password"});
        i.putExtra("keyboard_setup",new int[]{Constants.LETTERS_NO_EXTRA,Constants.NUMBERS_WITH_PLUS,
                Constants.LETTERS_WITH_EXTRA,Constants.LETTERS_NO_EXTRA,Constants.LETTERS_NO_EXTRA});
        startActivityForResult(i,Constants.REQUEST_CODE_SIGNUP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_SIGNUP) {
            if (resultCode == RESULT_OK) {

                String[] myAnswers = data.getStringArrayExtra("answers");

                signUp(myAnswers[3],myAnswers[4]);

            }
        }
    }

    private void signUp(final String username, final String password){

        if(myPreferences_.doesUsernameExist(username)){

            Prompts.toastMessageShort(this,getString(R.string.username_taken));

        }else{

            // We create a new account
            myPreferences_.saveAccount(username,password);

            // Save username in saved preferences
            myPreferences_.saveLastUsername(username);

            // Start Main Menu
            goToMainMenu();
        }
    }

    // Cloud Transactions
    private void signIn(final String username, final String password){

        // Check if account exists locally
        if(myPreferences_.checkCredentials(username,password)) {

            // Save username in saved preferences
            myPreferences_.saveLastUsername(username);

            // Start Main Menu
            goToMainMenu();

        }else if(myPreferences_.doesUsernameExist(username)){
            Prompts.toastMessageShort(this,getString(R.string.wrong_password));
        }else{
            Prompts.toastMessageShort(this,getString(R.string.username_doesnt_exists));
        }
    }

    private void goToMainMenu(){

        finish();
        Intent i = new Intent(Activity_Login.this, Activity_Main.class);
        i.putExtra("fromLogin",true);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

        if(myLetterKeyboard.hideIfVisible()){
            return;
        }

        finish();
    }
}
