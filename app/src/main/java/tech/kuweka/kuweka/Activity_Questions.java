package tech.kuweka.kuweka;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import classes.TimeStamp;
import custom_keyboard.Keyboard_Interface;
import custom_keyboard.Letter_Keyboard;
import custom_keyboard.Number_Keyboard;
import methods.Constants;
import methods.Prompts;
import methods.Text_Inputs;
import methods.User_Interface;

public class Activity_Questions extends AppCompatActivity {

    private Letter_Keyboard myLetterKeyboard;
    private Number_Keyboard myNumberKeyboard;

    private Toolbar myToolbar;
    private TextView textView_title;
    private EditText editText_input;
    private DatePicker datePicker;

    private String[] myTitles;
    private String[] myQuestions;
    private int[] myKeyboardSetups;
    private String[] myAnswers;

    private int nbr_of_steps = 0;
    private int current_step = 0;

    /*
    * This Activity is a preformed questioner
    *
    * Different string arrays containing all the keywords should be provided in the intent
    *
    * Call Exemple :
    *
    *     Intent i = new Intent(*****.this, Activity_Questions.class);
    *
    *     i.putExtra("questions",new String[]{"Full Name", "Phone Number",
    *            "Location", "Account Username", "Account Password"});
    *
    *     i.putExtra("titles",new String[]{"Step 1", "Step 2",
    *            "Step 3", "Step 4", "Step 5"});
    *
    *     i.putExtra("keyboard_setup",new int[]{Constants.LETTERS_NO_EXTRA,Constants.NUMBERS_WITH_PLUS,
    *           Constants.LETTERS_WITH_EXTRA,Constants.LETTERS_NO_EXTRA,Constants.LETTERS_NO_EXTRA});
    *
    *     % If needed an additional string array containing the answers can be given %
    *     i.putExtra("answers",new String[]{"Bob The Second", "+15148243530",
    *            "Montreal", "bob", "12345"});
    *
    *
    *     startActivityForResult(i,REQUEST_CODE_SIGNUP);
    *
    *
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // Receive Toolbar Titles, Questions, Keyboards and Answers through Intent
        Intent i = getIntent();
        myTitles = i.getStringArrayExtra("titles");
        myQuestions = i.getStringArrayExtra("questions");
        myKeyboardSetups = i.getIntArrayExtra("keyboard_setup");
        myAnswers = i.getStringArrayExtra("answers");
        if(myTitles == null || myQuestions == null || myKeyboardSetups == null){
            // If the intent is empty
            finish();
            return;
        }


        // Init Toolbar
        myToolbar = findViewById(R.id.toolbar);
        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this,R.drawable.ic_action_back));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // Init View Attributes
        textView_title = findViewById(R.id.textview_title);
        Button button = findViewById(R.id.button);
        myLetterKeyboard = findViewById(R.id.letter_keyboard);
        myNumberKeyboard = findViewById(R.id.number_keyboard);

        // Init the Inputs
        editText_input = findViewById(R.id.edittext_input);
        datePicker = findViewById(R.id.date_picker);


        // Init Questioner
        nbr_of_steps = myQuestions.length;
        if(myAnswers == null) {
            myAnswers = new String[nbr_of_steps];
        }
        current_step = 0;

        // Init First Question
        setTitles();

        // Button Click Listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
    }

    private boolean saveAnswer(){

        if(datePicker.getVisibility() == View.VISIBLE){
            // If input is a date picker

            // Fetch Info from Date Picker
            TimeStamp date = new TimeStamp(datePicker.getYear(),datePicker.getMonth() + 1,
                    datePicker.getDayOfMonth(),0,0,0);

            myAnswers[current_step] = date.toString();

            return true;

        }else if(editText_input.getVisibility() == View.VISIBLE){
            // If input is an edit text

            // Check if Edit Text is valid
            if(Text_Inputs.isValid(new EditText[]{editText_input})){

                myAnswers[current_step] = editText_input.getText().toString().trim();

                return true;
            }
        }

        return false;
    }

    private void setQuestionKeyboard(){

        switch (myKeyboardSetups[current_step]){

            case Constants.LETTERS_NO_EXTRA:
                editText_input.setEnabled(true);
                new Keyboard_Interface(this,myLetterKeyboard,new EditText[]{editText_input});
                myLetterKeyboard.hideSymbols();
                myLetterKeyboard.showLetters();
                myNumberKeyboard.hideIfVisible();
                myLetterKeyboard.showIfNotVisible();
                break;

            case Constants.LETTERS_WITH_EXTRA:
                editText_input.setEnabled(true);
                new Keyboard_Interface(this,myLetterKeyboard,new EditText[]{editText_input});
                myLetterKeyboard.showSymbols();
                myLetterKeyboard.showLetters();
                myNumberKeyboard.hideIfVisible();
                myLetterKeyboard.showIfNotVisible();
                break;

            case Constants.NUMBERS_NO_EXTRA:
                editText_input.setEnabled(true);
                new Keyboard_Interface(this,myNumberKeyboard,new EditText[]{editText_input});
                myNumberKeyboard.setButtonExtra(null);
                myLetterKeyboard.hideIfVisible();
                myNumberKeyboard.showIfNotVisible();
                break;

            case Constants.NUMBERS_WITH_PLUS:
                editText_input.setEnabled(true);
                new Keyboard_Interface(this,myNumberKeyboard,new EditText[]{editText_input});
                myNumberKeyboard.setButtonExtra("+");
                myLetterKeyboard.hideIfVisible();
                myNumberKeyboard.showIfNotVisible();
                break;

            case Constants.NUMBERS_WITH_POINT:
                editText_input.setEnabled(true);
                new Keyboard_Interface(this,myNumberKeyboard,new EditText[]{editText_input});
                myNumberKeyboard.setButtonExtra(".");
                myLetterKeyboard.hideIfVisible();
                myNumberKeyboard.showIfNotVisible();
                break;

            case Constants.NO_KEYBOARD:
                editText_input.setEnabled(false);
                myLetterKeyboard.hideIfVisible();
                myNumberKeyboard.hideIfVisible();
                break;
        }
    }

    private void initDatePicker(){

        // Set Date Min and Max
        TimeStamp rightNow = (new TimeStamp());
        datePicker.setMinDate(rightNow.removeDays(7).getTime());
        datePicker.setMaxDate(rightNow.toDate().getTime());

        if(myAnswers[current_step] == null){
            datePicker.init(rightNow.getYear(),rightNow.getMonth()-1,rightNow.getDay(),null);
        }else{
            TimeStamp savedTimeStamp = TimeStamp.fromString(myAnswers[current_step]);
            datePicker.init(savedTimeStamp.getYear(),savedTimeStamp.getMonth()-1,savedTimeStamp.getDay(),null);
        }
    }

    private void setTitles(){

        // Set Keyboard Setup
        setQuestionKeyboard();

        // Set Toolbar Title
        myToolbar.setTitle(myTitles[current_step]);

        // Set Input Title
        textView_title.setText(myQuestions[current_step]);

        // Set Input
        if(myQuestions[current_step].contains("Date")){

            // Set the right date
            initDatePicker();

            // Set Visibility
            datePicker.setVisibility(View.VISIBLE);
            editText_input.setVisibility(View.GONE);

        }else {
            // Clear EditText
            editText_input.setText(myAnswers[current_step]);

            // Clear Edittext Focus
            editText_input.clearFocus();

            // Set Cursor at the end
            editText_input.setSelection(editText_input.getText().length());

            // Set Visibility
            editText_input.setVisibility(View.VISIBLE);
            datePicker.setVisibility(View.GONE);
        }
    }

    private void nextQuestion() {

        // Start by saving the current step's answer
        if (!saveAnswer()) {

            // Prompt the user
            Prompts.toastMessageShort(this,"Enter " + myQuestions[current_step]);

            return;
        }


        // Jump to next step
        current_step += 1;


        // Check if we are at the last step
        if(current_step == nbr_of_steps){

            Intent i = new Intent();
            i.putExtra("answers",myAnswers);
            setResult(RESULT_OK,i);
            finish();

            return;
        }


        // Freeze UI
        User_Interface.freeze(this);


        // Fetch Previous Views
        View[] previousViews;
        if(myQuestions[current_step-1].equals("Date")){
            previousViews = new View[]{textView_title,datePicker};
        }else{
            previousViews = new View[]{textView_title,editText_input};
        }


        // Fetch Next Views
        View[] nextViews;
        if(myQuestions[current_step].equals("Date")){
            nextViews = new View[]{textView_title,datePicker};
        }else{
            nextViews = new View[]{textView_title,editText_input};
        }


        // Animate Transition
        User_Interface.exitToLeftAndEnterFromRight(this, previousViews,nextViews);


        // Wait for animation to be finished
        textView_title.postDelayed(new Runnable() {
            @Override
            public void run() {
                setTitles();
            }
        },500);


        textView_title.postDelayed(new Runnable() {
            @Override
            public void run() {
                unfreezeUI();
            }
        },800);
    }

    private void previousQuestion(){

        // If we are at the first step
        if(current_step == 0){

            finish();

            // stop reading code
            return;
        }

        // Start by saving the current step's answer
        saveAnswer();


        // Jump to previous step
        current_step -= 1;

        // Freeze UI
        User_Interface.freeze(this);


        // Fetch Previous Views
        View[] previousViews;
        if(myQuestions[current_step+1].equals("Date")){
            previousViews = new View[]{textView_title,datePicker};
        }else{
            previousViews = new View[]{textView_title,editText_input};
        }


        // Fetch Next Views
        View[] nextViews;
        if(myQuestions[current_step].equals("Date")){
            nextViews = new View[]{textView_title,datePicker};
        }else{
            nextViews = new View[]{textView_title,editText_input};
        }


        // Animate Transition
        User_Interface.exitToRightAndEnterFromLeft(this, previousViews,nextViews);


        // Wait for animation to be finished
        textView_title.postDelayed(new Runnable() {
            @Override
            public void run() {
                setTitles();
            }
        },500);

        textView_title.postDelayed(new Runnable() {
            @Override
            public void run() {
                unfreezeUI();
            }
        },800);
    }


    private void unfreezeUI(){

        User_Interface.unfreeze(this);
    }

    @Override
    public void onBackPressed() {
        previousQuestion();
    }
}
