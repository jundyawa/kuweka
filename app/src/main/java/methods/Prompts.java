package methods;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tech.kuweka.kuweka.R;

public class Prompts {

    public static void toastMessageShort(Context myContext, final String message){

        Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastMessageLong(Context myContext, final String message){

        Toast.makeText(myContext, message, Toast.LENGTH_LONG).show();
    }

    public static void popUp(Context myContext, final String title, final String subtitle){

        // We build the dialog box
        final AlertDialog.Builder builder = new AlertDialog.Builder(myContext);

        // We fetch our custom layout
        LayoutInflater myInflater = ((Activity) myContext).getLayoutInflater();
        final View myDialogView = myInflater.inflate(R.layout.popup_text_ok,null);

        // We set our custom layout in the dialog box
        builder.setView(myDialogView);

        // Fetch UI objects
        Button okButton = myDialogView.findViewById(R.id.ok_button);

        TextView title_textView = myDialogView.findViewById(R.id.title_textview);
        TextView subtitle_textView = myDialogView.findViewById(R.id.subtitle_textview);

        title_textView.setText(title);
        subtitle_textView.setText(subtitle);

        final AlertDialog myAlertDialog = builder.create();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myAlertDialog.cancel();
            }
        });

        // We show the box
        myAlertDialog.show();
    }

}
