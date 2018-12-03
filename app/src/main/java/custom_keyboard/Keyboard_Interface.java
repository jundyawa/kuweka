package custom_keyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Keyboard_Interface {

    private Context myContext_;

    private Number_Keyboard myNumberKeyboard_;
    private Letter_Keyboard myLetterKeyboard_;


    public Keyboard_Interface(Context myContext, final Letter_Keyboard myLetterKeyboard,
                              final EditText[] myEditTexts) {

        this.myContext_ = myContext;

        this.myNumberKeyboard_ = null;
        this.myLetterKeyboard_ = myLetterKeyboard;

        init(myEditTexts);
    }

    public Keyboard_Interface(Context myContext, final Number_Keyboard myNumberKeyboard,
                              final EditText[] myEditTexts) {

        this.myContext_ = myContext;

        this.myNumberKeyboard_ = myNumberKeyboard;
        this.myLetterKeyboard_ = null;

        init(myEditTexts);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void init(final EditText[] myEditTexts){

        for (int i = 0; i < myEditTexts.length ; ++i) {

            // prevent system keyboard from appearing when EditText is tapped
            // Disable spell check (hex Text_Inputs look like words to Android)
            myEditTexts[i].setRawInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            myEditTexts[i].setTextIsSelectable(true);

            // Event Listeners
            final int finalI = i;

            myEditTexts[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {

                        // Link the keyboard to this new edittext
                        changeInputConnection(myEditTexts[finalI]);
                    }
                }
            });


            myEditTexts[i].setOnTouchListener(new View.OnTouchListener() {
                @Override public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() != MotionEvent.ACTION_MOVE) {

                        // Grab edittext from view parameter
                        EditText edittext = (EditText) v;

                        // Set an input type that disables the standard keyboard
                        edittext.setInputType(InputType.TYPE_NULL);

                        // Call native handler to produce the event manually
                        edittext.onTouchEvent(event);

                        // Restore input type
                        edittext.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

                        if (event.getAction() == MotionEvent.ACTION_UP) {

                            // Raise the custom keyboard
                            showCustomKeyboard(v);

                            // Link the keyboard to this new edittext
                            changeInputConnection(myEditTexts[finalI]);
                        }
                    }

                    // Consume touch event
                    return true;
                }
            });
        }
    }

    private void changeInputConnection(final EditText myEditText){

        // pass the InputConnection from the EditText to the keyboard
        InputConnection ic = myEditText.onCreateInputConnection(new EditorInfo());

        if(myLetterKeyboard_ != null){
            myLetterKeyboard_.setInputConnection(ic);
        }else{
            myNumberKeyboard_.setInputConnection(ic);
        }
    }

    private void showCustomKeyboard(View v) {

        if(myLetterKeyboard_ != null){
            // Letter Keyboard
            myLetterKeyboard_.showIfNotVisible();

        }else{
            // Number Keyboard
            myNumberKeyboard_.showIfNotVisible();
        }

        if (v != null) {
            ((InputMethodManager) myContext_.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
