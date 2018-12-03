package tech.kuweka.kuweka;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import classes.Supplier;
import classes.TimeStamp;
import classes.Transaction;
import listview_adapters.Transaction_List;
import local_host.DatabaseAdapter;
import methods.Constants;
import methods.Prompts;

public class Activity_Supplier extends AppCompatActivity {

    private DatabaseAdapter myDBAdapter;
    private Supplier mySupplier;

    private ListView myListView;
    private Transaction_List myListViewAdapter;

    private TextView metric_textview;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        // Receive Questions through Intent
        Intent i = getIntent();
        String mySurrogateKey = i.getStringExtra("surrogate_key");
        if(mySurrogateKey == null){
            // If the intent is empty
            finish();
            return;
        }

        // Init DB
        myDBAdapter = new DatabaseAdapter(this);
        myDBAdapter.open();

        // Fetch Supplier
        mySupplier = myDBAdapter.getSupplierBySurrogateKey(mySurrogateKey);
        if(mySupplier == null){
            // If the supplier is not found
            finish();
            return;
        }

        // Init Toolbar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this,R.drawable.ic_action_back));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title = mySupplier.getName() + ", " + mySupplier.getTag();
        myToolbar.setTitle(title);

        // --- UI init ---
        Button button_add = findViewById(R.id.button_add);
        myListView = findViewById(R.id.list_layout);
        metric_textview = findViewById(R.id.top_metric_textview);

        // Event Listeners
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTransaction();
            }
        });

        // Init UI
        updateUI();

        // Event Listeners
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Transaction transactionClicked = myListViewAdapter.getItem(position);

                if(transactionClicked.isValid()){
                    editTransaction(transactionClicked);
                }
            }
        });

    }

    private void updateUI(){

        // Update Supplier
        mySupplier = myDBAdapter.getSupplierBySurrogateKey(mySupplier.getSurrogateKey());

        // Build custom adapter
        myListViewAdapter = new Transaction_List(this, mySupplier.getTransactions());

        // Set adapter to the listview
        myListView.setAdapter(myListViewAdapter);

        // Init
        double total = myDBAdapter.getAttribute1Total_ByDay_BySupplier(new TimeStamp(),mySupplier.getSurrogateKey());
        String total_str = NumberFormat.getNumberInstance(Locale.US).format(total);
        metric_textview.setText(total_str);
    }

    private void editTransaction(final Transaction myTransaction){

        // Start the Questionnary
        Intent i = new Intent(this, Activity_Questions.class);
        i.putExtra("titles",new String[]{title});
        i.putExtra("questions",new String[]{"Volume"});
        i.putExtra("answers",new String[]{String.valueOf(myTransaction.getAttribute1())});
        i.putExtra("keyboard_setup",new int[]{Constants.NUMBERS_WITH_POINT});
        startActivityForResult(i,Constants.REQUEST_CODE_EDIT_TRANSACTION);
    }


    private void newTransaction(){

        // Start the Questionnary
        Intent i = new Intent(this, Activity_Questions.class);
        i.putExtra("titles",new String[]{title, title});
        i.putExtra("questions",new String[]{"Volume", "Date"});
        i.putExtra("keyboard_setup",new int[]{Constants.NUMBERS_WITH_POINT,Constants.NO_KEYBOARD});
        startActivityForResult(i,Constants.REQUEST_CODE_ADD_TRANSACTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_ADD_TRANSACTION) {
            if (resultCode == RESULT_OK) {

                // Receive Answers
                String[] myAnswers = data.getStringArrayExtra("answers");

                try {

                    // Create new Supplier object
                    Transaction newTransaction = new Transaction(mySupplier.getSurrogateKey(),
                            TimeStamp.fromString(myAnswers[1]),Double.parseDouble(myAnswers[0]),
                            0,0);

                    // Add to Database
                    myDBAdapter.createTransaction(newTransaction);

                    updateUI();

                }catch (NumberFormatException e){
                    Log.d("NumberFormatException","error");
                }
            }
        }else if (requestCode == Constants.REQUEST_CODE_EDIT_TRANSACTION) {
            if (resultCode == RESULT_OK) {

                // Receive Answers
                String[] myAnswers = data.getStringArrayExtra("answers");

                try {

                    Prompts.toastMessageShort(getBaseContext(),myAnswers[0]);

                    updateUI();

                }catch (NumberFormatException e){
                    Log.d("NumberFormatException","error");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
