package methods;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class Text_Inputs {

    static public boolean isValid(final EditText[] myEditTexts){

        if(myEditTexts == null){
            return false;
        }

        if(myEditTexts.length == 0){
            return false;
        }

        for (EditText myEditText : myEditTexts) {

            String text = myEditText.getText().toString().trim();

            if (text.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    static public boolean isValid(final Context myContext, final EditText[] myEditTexts, final String[] hints){

        if(myEditTexts == null){
            return false;
        }

        if(myEditTexts.length == 0){
            return false;
        }

        if(myEditTexts.length != hints.length){
            return false;
        }

        for (int i = 0 ; i < myEditTexts.length ; ++i) {

            String text = myEditTexts[i].getText().toString().trim();

            if(text.isEmpty()){
                Prompts.toastMessageShort(myContext,"Enter " + hints[i]);
                return false;
            }
        }

        return true;
    }

    static public String capitalizeEveryWord(final String myText){

        if(myText == null){
            return "";
        }

        // Capitalize every word
        StringBuilder sb = new StringBuilder(myText);
        for (int i = 0; i < sb.length(); i++){
            if (i == 0 || sb.charAt(i - 1) == ' ') {
                sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
            }
        }
        return sb.toString();
    }
}
