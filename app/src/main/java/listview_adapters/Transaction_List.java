package listview_adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import classes.TimeStamp;
import classes.Transaction;
import tech.kuweka.kuweka.R;

public class Transaction_List extends BaseAdapter {

    // Attributes
    private Context myContext_;
    private ArrayList<Transaction> myTransactions_;

    private int colorDarkText;
    private int colorLightText;


    // Constructor
    public Transaction_List(Context myContext, final ArrayList<Transaction> myTransactions) {

        this.myContext_ = myContext;
        this.myTransactions_ = myTransactions; // ordered already

        colorDarkText = ContextCompat.getColor(myContext,R.color.colorTextDark);
        colorLightText = ContextCompat.getColor(myContext,R.color.colorTextLight);
    }

    // Default Methods
    @Override
    public int getCount() {
        if(myTransactions_ == null){
            return 0;
        }
        return myTransactions_.size();
    }
    @Override
    public Transaction getItem(int position) {
        return myTransactions_.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        ViewHolder viewHolder;

        if (view == null) { // On create

            // Get the layout
            view = LayoutInflater.from(myContext_).inflate(R.layout.list_transactions, null);

            // Creates a ViewHolder and store references to the children views
            // we want to bind data to.
            viewHolder = new ViewHolder();
            viewHolder.date_ = view.findViewById(R.id.date_items);
            viewHolder.volume_ = view.findViewById(R.id.volume_items);

            // Binds the data efficiently with the holder.
            view.setTag(viewHolder);

        }else{    // Populating the already existent

            // Get the ViewHolder back to get fast access to the children's
            viewHolder = (ViewHolder) view.getTag();
        }

        // Set the columns value
        Transaction myTransaction = getItem(position);
        TimeStamp myTimeStamp = myTransaction.getDate();

        String date = myTimeStamp.getMonthName() + " " + myTimeStamp.getDay();
        viewHolder.date_.setText(date);

        String volumeSTR = NumberFormat.getNumberInstance(Locale.US).format(myTransaction.getAttribute1())
                + " " + myContext_.getResources().getString(R.string.main_unit);
        viewHolder.volume_.setText(volumeSTR);

        if(myTimeStamp.isToday()){
            viewHolder.date_.setTextColor(colorDarkText);
            viewHolder.volume_.setTextColor(colorDarkText);
        }else{
            viewHolder.date_.setTextColor(colorLightText);
            viewHolder.volume_.setTextColor(colorLightText);
        }

        return view;
    }

    // The ViewHolder Class
    static class ViewHolder {

        // The milk entries
        TextView date_, volume_;
    }
}
