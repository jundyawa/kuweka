package tech.kuweka.kuweka;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import classes.Supplier;
import custom_keyboard.Keyboard_Interface;
import custom_keyboard.Letter_Keyboard;
import listview_adapters.Suppliers_List;
import local_host.DatabaseAdapter;
import methods.Constants;
import methods.Prompts;
import methods.User_Interface;

import static android.app.Activity.RESULT_OK;

public class Fragment_Catalog extends Fragment{

    private Suppliers_List myListViewAdapter;
    private ListView myListView;

    private Letter_Keyboard myLetterKeyboard;
    private EditText inputSearch;

    private DatabaseAdapter myDBAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_catalog, container, false);

        // UI Init
        myListView = rootView.findViewById(R.id.list_layout);
        inputSearch = rootView.findViewById(R.id.inputSearch);
        final FloatingActionButton add_floatingbutton = rootView.findViewById(R.id.button_add);

        // Init Keyboard
        if(getActivity() != null){
            myLetterKeyboard = ((Activity_Main) getActivity()).getMyLetterKeyboard();

            new Keyboard_Interface(getContext(), myLetterKeyboard,
                    new EditText[]{inputSearch});
        }

        // Init DB
        myDBAdapter = new DatabaseAdapter(getContext());
        myDBAdapter.open();

        // Event Listeners
        add_floatingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newSupplier();
            }
        });

        myListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {

                Supplier supplierClicked = myListViewAdapter.getFilteredSuppliers().get(position);

                Intent i = new Intent(getActivity(),Activity_Supplier.class);
                i.putExtra("surrogate_key",supplierClicked.getSurrogateKey());
                startActivity(i);
            }
        });

        myListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == 1) {
                    User_Interface.scaleDown(getContext(),add_floatingbutton);
                }else if(scrollState == 0){
                    User_Interface.scaleUp(getContext(),add_floatingbutton);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        // Set the input search functionality
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // When user changed the Text
                String input_constraint = s.toString().trim();
                if(myListViewAdapter != null) {
                    myListViewAdapter.getFilter().filter(input_constraint);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public void onStop() {
        super.onStop();
        // When this fragment is stopped

        // Hide Keyboard
        myLetterKeyboard.hideIfVisible();

        // Clear Input Search Field
        inputSearch.setText("");
    }

    private void updateUI(){

        // Update Suppliers
        ArrayList<Supplier> mySuppliers = myDBAdapter.getAllSuppliers(false);

        // Build custom adapter
        myListViewAdapter = new Suppliers_List(getActivity(), mySuppliers);

        // Set adapter to the listview
        myListView.setAdapter(myListViewAdapter);
    }

    private void newSupplier(){

        // Start the Questionnary
        Intent i = new Intent(getActivity(), Activity_Questions.class);
        i.putExtra("titles",new String[]{"Step 1", "Step 2"});
        i.putExtra("questions",new String[]{"Full Name", "Phone Number"});
        i.putExtra("keyboard_setup",new int[]{Constants.LETTERS_NO_EXTRA,Constants.NUMBERS_WITH_PLUS});
        startActivityForResult(i,Constants.REQUEST_CODE_ADD_SUPPLIER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_ADD_SUPPLIER) {
            if (resultCode == RESULT_OK) {

                // Receive Answers
                String[] myAnswers = data.getStringArrayExtra("answers");

                // Create new Supplier object
                Supplier newSupplier = new Supplier(myAnswers[0],myAnswers[1]);

                // Add to Database
                myDBAdapter.createSupplier(newSupplier);
            }
        }
    }

}
