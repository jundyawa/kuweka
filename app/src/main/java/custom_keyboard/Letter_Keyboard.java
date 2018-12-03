package custom_keyboard;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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


public class Letter_Keyboard extends LinearLayout implements View.OnClickListener {

    // constructors
    public Letter_Keyboard(Context context) {
        this(context, null, 0);
    }

    public Letter_Keyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Letter_Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void hideSymbols(){

        mButtonComma.setVisibility(INVISIBLE);
        mButtonPeriod.setVisibility(INVISIBLE);
    }

    public void showSymbols(){


        mButtonComma.setVisibility(VISIBLE);
        mButtonPeriod.setVisibility(VISIBLE);
    }

    private void hideNumbers(){

        if(numbLayout1.getVisibility() == VISIBLE) {
            numbLayout1.setVisibility(GONE);
            numbLayout2.setVisibility(GONE);
            numbLayout3.setVisibility(GONE);
            numbLayout4.setVisibility(GONE);
        }
    }

    public void showNumbers(){

        hideLetters();

        numbLayout1.setVisibility(VISIBLE);
        numbLayout2.setVisibility(VISIBLE);
        numbLayout3.setVisibility(VISIBLE);
        numbLayout4.setVisibility(VISIBLE);
    }

    private void hideLetters(){

        if(letterLayout1.getVisibility() == VISIBLE){
            letterLayout1.setVisibility(GONE);
            letterLayout2.setVisibility(GONE);
            letterLayout3.setVisibility(GONE);
            letterLayout4.setVisibility(GONE);
        }
    }

    public void showLetters(){

        hideNumbers();

        letterLayout1.setVisibility(VISIBLE);
        letterLayout2.setVisibility(VISIBLE);
        letterLayout3.setVisibility(VISIBLE);
        letterLayout4.setVisibility(VISIBLE);
    }

    // keyboard keys (buttons)
    private LinearLayout numbLayout1;
    private LinearLayout numbLayout2;
    private LinearLayout numbLayout3;
    private LinearLayout numbLayout4;
    private LinearLayout letterLayout1;
    private LinearLayout letterLayout2;
    private LinearLayout letterLayout3;
    private LinearLayout letterLayout4;

    private Button mButtonComma;
    private Button mButtonPeriod;

    // This will map the button resource id to the String value that we want to
    // input when that button is clicked.
    private SparseArray<String> keyValues = new SparseArray<>();

    // Our communication link to the EditText
    private InputConnection inputConnection;

    private void init(Context context) {

        // initialize buttons
        LayoutInflater.from(context).inflate(R.layout.keyboard_letters, this, true);

        numbLayout1 = findViewById(R.id.numb1_layout);
        numbLayout2 = findViewById(R.id.numb2_layout);
        numbLayout3 = findViewById(R.id.numb3_layout);
        numbLayout4 = findViewById(R.id.numb4_layout);
        letterLayout1 = findViewById(R.id.letter1_layout);
        letterLayout2 = findViewById(R.id.letter2_layout);
        letterLayout3 = findViewById(R.id.letter3_layout);
        letterLayout4 = findViewById(R.id.letter4_layout);

        Button mButtonABC = findViewById(R.id.button_abc);
        Button mButtonDELETE2 = findViewById(R.id.button_delete2);

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

        Button mButtonQ = findViewById(R.id.button_q);
        Button mButtonW = findViewById(R.id.button_w);
        Button mButtonE = findViewById(R.id.button_e);
        Button mButtonR = findViewById(R.id.button_r);
        Button mButtonT = findViewById(R.id.button_t);
        Button mButtonY = findViewById(R.id.button_y);
        Button mButtonU = findViewById(R.id.button_u);
        Button mButtonI = findViewById(R.id.button_i);
        Button mButtonO = findViewById(R.id.button_o);
        Button mButtonP = findViewById(R.id.button_p);

        Button mButtonA = findViewById(R.id.button_a);
        Button mButtonS = findViewById(R.id.button_s);
        Button mButtonD = findViewById(R.id.button_d);
        Button mButtonF = findViewById(R.id.button_f);
        Button mButtonG = findViewById(R.id.button_g);
        Button mButtonH = findViewById(R.id.button_h);
        Button mButtonJ = findViewById(R.id.button_j);
        Button mButtonK = findViewById(R.id.button_k);
        Button mButtonL = findViewById(R.id.button_l);

        mButtonComma = findViewById(R.id.button_comma);
        Button mButtonZ = findViewById(R.id.button_z);
        Button mButtonX = findViewById(R.id.button_x);
        Button mButtonC = findViewById(R.id.button_c);
        Button mButtonV = findViewById(R.id.button_v);
        Button mButtonB = findViewById(R.id.button_b);
        Button mButtonN = findViewById(R.id.button_n);
        Button mButtonM = findViewById(R.id.button_m);
        mButtonPeriod = findViewById(R.id.button_period);

        Button mButton123 = findViewById(R.id.button_123);
        Button mButtonSPACE = findViewById(R.id.button_space);
        Button mButtonDELETE = findViewById(R.id.button_delete);


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

        mButtonQ.setOnClickListener(this);
        mButtonW.setOnClickListener(this);
        mButtonE.setOnClickListener(this);
        mButtonR.setOnClickListener(this);
        mButtonT.setOnClickListener(this);
        mButtonY.setOnClickListener(this);
        mButtonU.setOnClickListener(this);
        mButtonI.setOnClickListener(this);
        mButtonO.setOnClickListener(this);
        mButtonP.setOnClickListener(this);

        mButtonA.setOnClickListener(this);
        mButtonS.setOnClickListener(this);
        mButtonD.setOnClickListener(this);
        mButtonF.setOnClickListener(this);
        mButtonG.setOnClickListener(this);
        mButtonH.setOnClickListener(this);
        mButtonJ.setOnClickListener(this);
        mButtonK.setOnClickListener(this);
        mButtonL.setOnClickListener(this);

        mButtonComma.setOnClickListener(this);
        mButtonZ.setOnClickListener(this);
        mButtonX.setOnClickListener(this);
        mButtonC.setOnClickListener(this);
        mButtonV.setOnClickListener(this);
        mButtonB.setOnClickListener(this);
        mButtonN.setOnClickListener(this);
        mButtonM.setOnClickListener(this);
        mButtonPeriod.setOnClickListener(this);

        mButton123.setOnClickListener(this);
        mButtonSPACE.setOnClickListener(this);
        mButtonDELETE.setOnClickListener(this);

        mButtonABC.setOnClickListener(this);
        mButtonDELETE2.setOnClickListener(this);

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

        keyValues.put(R.id.button_q, "q");
        keyValues.put(R.id.button_w, "w");
        keyValues.put(R.id.button_e, "e");
        keyValues.put(R.id.button_r, "r");
        keyValues.put(R.id.button_t, "t");
        keyValues.put(R.id.button_y, "y");
        keyValues.put(R.id.button_u, "u");
        keyValues.put(R.id.button_i, "i");
        keyValues.put(R.id.button_o, "o");
        keyValues.put(R.id.button_p, "p");

        keyValues.put(R.id.button_a, "a");
        keyValues.put(R.id.button_s, "s");
        keyValues.put(R.id.button_d, "d");
        keyValues.put(R.id.button_f, "f");
        keyValues.put(R.id.button_g, "g");
        keyValues.put(R.id.button_h, "h");
        keyValues.put(R.id.button_j, "j");
        keyValues.put(R.id.button_k, "k");
        keyValues.put(R.id.button_l, "l");

        keyValues.put(R.id.button_comma, ",");
        keyValues.put(R.id.button_z, "z");
        keyValues.put(R.id.button_x, "x");
        keyValues.put(R.id.button_c, "c");
        keyValues.put(R.id.button_v, "v");
        keyValues.put(R.id.button_b, "b");
        keyValues.put(R.id.button_n, "n");
        keyValues.put(R.id.button_m, "m");
        keyValues.put(R.id.button_period, ".");

        keyValues.put(R.id.button_space, " ");

        // Hide Non Letters
        hideNumbers();
        hideSymbols();
    }

    @Override
    public void onClick(View v) {

        // do nothing if the InputConnection has not been set yet
        if (inputConnection == null) return;

        // Delete text or input key value
        // All communication goes through the InputConnection
        if (v.getId() == R.id.button_delete || v.getId() == R.id.button_delete2) {
            CharSequence selectedText = inputConnection.getSelectedText(0);
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                inputConnection.deleteSurroundingText(1, 0);
            } else {
                // delete the selection
                inputConnection.commitText("", 1);
            }
        }else if (v.getId() == R.id.button_abc  || v.getId() == R.id.button_123) {
            if(numbLayout1.getVisibility() == View.GONE){
                showNumbers();
            }else{
                showLetters();
            }
        } else {
            String value = keyValues.get(v.getId());

            if(value == null){
                return;
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

        // Animate the entrance
        int keyboardHeight = this.getHeight();

        if(keyboardHeight == 0){
            keyboardHeight = 500;
        }

        // Animate the exit
        TranslateAnimation animate = new TranslateAnimation(0,0,0,keyboardHeight);
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
        int keyboardHeight = this.getHeight();

        if(keyboardHeight == 0){
            keyboardHeight = 500;
        }

        TranslateAnimation animate = new TranslateAnimation(0,0,keyboardHeight,0);
        animate.setDuration(400);
        this.startAnimation(animate);

        this.setVisibility(View.VISIBLE);

        // Enable Buttons
        User_Interface.setViewAndChildrenEnabled(this,true);
    }

    public boolean showIfNotVisible(){

        if(this.getVisibility() != VISIBLE){

            Log.d("Letter_Keyboard","UP");

            show();

            return true;

        }else{
            return false;
        }
    }

    public boolean hideIfVisible() {

        if(this.getVisibility() == VISIBLE){

            Log.d("Letter_Keyboard","Down");

            hide();

            return true;
        }else{
            return false;
        }
    }
}
