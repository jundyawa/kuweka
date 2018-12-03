package custom_keyboard;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

import methods.Prompts;
import methods.User_Interface;
import tech.kuweka.kuweka.R;


public class Number_Keyboard extends LinearLayout implements View.OnClickListener {

    private String extraChar;

    // constructors
    public Number_Keyboard(Context context) {
        this(context, null, 0);
    }

    public Number_Keyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Number_Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setButtonExtra(String charContent){

        extraChar = charContent;
        mButtonExtra.setText(charContent);
        keyValues.put(R.id.button_extra, charContent);
    }

    private Button mButtonExtra;

    // This will map the button resource id to the String value that we want to
    // input when that button is clicked.
    SparseArray<String> keyValues = new SparseArray<>();

    // Our communication link to the EditText
    InputConnection inputConnection;

    private void init(Context context) {

        // initialize buttons
        LayoutInflater.from(context).inflate(R.layout.keyboard_numbers, this, true);
        Button mButton1 = findViewById(R.id.button_1);
        Button mButton2 = findViewById(R.id.button_2);
        Button mButton3 = findViewById(R.id.button_3);
        Button mButton4 = findViewById(R.id.button_4);
        Button mButton5 = findViewById(R.id.button_5);
        Button mButton6 = findViewById(R.id.button_6);
        Button mButton7 = findViewById(R.id.button_7);
        Button mButton8 = findViewById(R.id.button_8);
        Button mButton9 = findViewById(R.id.button_9);
        Button mButton0 = findViewById(R.id.button_0);
        mButtonExtra = findViewById(R.id.button_extra);
        Button mButtonDelete = findViewById(R.id.button_delete);

        // set button click listeners
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
        mButton7.setOnClickListener(this);
        mButton8.setOnClickListener(this);
        mButton9.setOnClickListener(this);
        mButton0.setOnClickListener(this);
        mButtonExtra.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);

        // map buttons IDs to input Text_Inputs
        keyValues.put(R.id.button_1, "1");
        keyValues.put(R.id.button_2, "2");
        keyValues.put(R.id.button_3, "3");
        keyValues.put(R.id.button_4, "4");
        keyValues.put(R.id.button_5, "5");
        keyValues.put(R.id.button_6, "6");
        keyValues.put(R.id.button_7, "7");
        keyValues.put(R.id.button_8, "8");
        keyValues.put(R.id.button_9, "9");
        keyValues.put(R.id.button_0, "0");
    }

    @Override
    public void onClick(View v) {

        // do nothing if the InputConnection has not been set yet
        if (inputConnection == null) return;

        // Delete text or input key value
        // All communication goes through the InputConnection
        if (v.getId() == R.id.button_delete) {
            CharSequence selectedText = inputConnection.getSelectedText(0);
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                inputConnection.deleteSurroundingText(1, 0);

            } else {
                // delete the selection
                inputConnection.commitText("", 1);
            }
        }else {
            String value = keyValues.get(v.getId());

            if(value == null){
                return;
            }

            if(value.equals(extraChar)){

                String textSoFar = inputConnection.getTextBeforeCursor(20, 0).toString();

                // Makes sure there isn't already a extra char
                if(textSoFar.contains(extraChar)){
                    return;
                }

                // If extra char is "+" it can only be at the first position
                if(textSoFar.length() > 0 && extraChar.equals("+")){
                    return;
                }
            }

            inputConnection.commitText(value, 1);
        }
    }

    // The activity (or some parent or controller) must give us
    // a reference to the current EditText's InputConnection
    public void setInputConnection(InputConnection ic) {
        this.inputConnection = ic;
    }

    private void hide(){

        // Disable Buttons
        User_Interface.setViewAndChildrenEnabled(this,false);

        // Animate the exit
        TranslateAnimation animate = new TranslateAnimation(0,0,0,this.getHeight());
        animate.setDuration(400);
        this.startAnimation(animate);
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                setVisibility(View.GONE);
            }
        },400);
    }

    private void show(){

        // Disable Buttons
        User_Interface.setViewAndChildrenEnabled(this,false);

        // Animate the entrance
        TranslateAnimation animate = new TranslateAnimation(0,0,this.getHeight(),0);
        animate.setDuration(400);
        this.startAnimation(animate);
        this.setVisibility(View.VISIBLE);

        // Enable Buttons
        User_Interface.setViewAndChildrenEnabled(this,true);
    }

    public boolean showIfNotVisible(){

        if(this.getVisibility() != VISIBLE){

            show();

            return true;

        }else{
            return false;
        }
    }

    public boolean hideIfVisible() {

        if(this.getVisibility() == VISIBLE){

            hide();

            return true;

        }else{
            return false;
        }
    }
}
