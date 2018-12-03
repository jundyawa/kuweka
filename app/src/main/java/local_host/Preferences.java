package local_host;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import java.util.Locale;

import methods.Constants;

public class Preferences {

    // Attribute
    private SharedPreferences mySharedPref_;

    public Preferences(Context myContext){

        this.mySharedPref_ = myContext.getSharedPreferences(
                Constants.LOCAL_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // Last Username for login page
    public void saveLastUsername(final String username){

        SharedPreferences.Editor myEditor = mySharedPref_.edit();

        myEditor.putString("last_username", encrypt(username));
        myEditor.apply();
    }

    public String getLastUsername(){

        return decrypt(mySharedPref_.getString("last_username",null));
    }

    public String getPassword(final String username){

        return decrypt(mySharedPref_.getString(encrypt(username),null));
    }

    public void saveAccount(final String username, final String password){

        // Input check
        if(username == null || password == null){
            return;
        }

        if(username.isEmpty() || password.isEmpty()){
            return;
        }

        if(doesUsernameExist(username)){
            return;
        }

        SharedPreferences.Editor myEditor = mySharedPref_.edit();

        myEditor.putString(encrypt(username),encrypt(password));

        myEditor.apply();
    }

    public boolean doesUsernameExist(final String username){

        // Input check
        if(username == null){
            return false;
        }

        if(username.isEmpty()){
            return false;
        }

        return mySharedPref_.getString(encrypt(username), null) != null;
    }

    public boolean checkCredentials(final String username, final String password){

        // Input check
        if(username == null || password == null){
            return false;
        }

        if(username.isEmpty() || password.isEmpty()){
            return false;
        }

        String saved_password = mySharedPref_.getString(encrypt(username),null);

        if(saved_password != null){

            String formatedPassword = password.trim().toLowerCase(Locale.US);

            return formatedPassword.equals(decrypt(saved_password));
        }else{
            return false;
        }
    }

    private static String encrypt(final String input) {

        if(input == null){
            return null;
        }

        String formatedInput = input.trim().toLowerCase(Locale.US);

        String encodedInput = Base64.encodeToString(formatedInput.getBytes(), Base64.DEFAULT);

        return encodedInput.trim();
    }

    private static String decrypt(final String input) {

        if(input != null) {
            return new String(Base64.decode(input, Base64.DEFAULT)).trim();
        }

        return null;
    }
}
