package listview_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import classes.Supplier;
import tech.kuweka.kuweka.R;

public class Suppliers_List extends BaseAdapter implements Filterable {


    // Attributes
    private Context myContext_;

    private ArrayList<Supplier> mySuppliers_;
    private ArrayList<Supplier> myFilteredSuppliers_;

    private ItemFilter mFilter = new ItemFilter();

    // Constructor
    public Suppliers_List(Context myContext, ArrayList<Supplier> mySuppliers) {

        this.myContext_ = myContext;

        this.mySuppliers_ = mySuppliers;
        this.myFilteredSuppliers_ = mySuppliers;
    }

    // Default Methods
    @Override
    public int getCount() {
        if(myFilteredSuppliers_ == null){
            return 0;
        }

        return myFilteredSuppliers_.size();
    }
    @Override
    public Supplier getItem(int position) {
        return myFilteredSuppliers_.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<Supplier> getFilteredSuppliers(){
        return myFilteredSuppliers_;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        // When view is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the view supplied
        // by ListView is null.
        if (view == null) { // On create

            // Get the layout
            view = LayoutInflater.from(myContext_).inflate(R.layout.list_suppliers, null);

            // Creates a ViewHolder and store references to the children views
            // we want to bind data to.
            viewHolder = new ViewHolder();
            viewHolder.name_ = view.findViewById(R.id.name_items);
            viewHolder.tag_ = view.findViewById(R.id.tag_items);

            // Binds the data efficiently with the holder.
            view.setTag(viewHolder);

        }else{    // Populating the already existent

            // Get the ViewHolder back to get fast access to the children's
            viewHolder = (ViewHolder) view.getTag();
        }

        // Set the Name column value
        viewHolder.name_.setText(getItem(position).getName());
        viewHolder.tag_.setText(getItem(position).getTag());

        return view;
    }

    // The ViewHolder Class
    static class ViewHolder {
        TextView name_, tag_;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            // Object containing the filtered array
            FilterResults results = new FilterResults();

            // We format the input string constraints to be all lower case
            String filterString = constraint.toString().toLowerCase();

            // The new array containing only the filtered suppliers
            final ArrayList<Supplier> filteredSupplierArray = new ArrayList<>();

            // Attributes checked
            Supplier my_supplier;
            String supplier_name;
            String supplier_tag;

            // We scan the full array of suppliers and filter the valid ones
            for (int i = 0; i < mySuppliers_.size(); i++) {

                my_supplier = mySuppliers_.get(i);
                supplier_name = my_supplier.getName();
                supplier_tag = my_supplier.getTag();

                // If input is numeric
                if(supplier_name.toLowerCase().contains(filterString) || supplier_tag.contains(filterString)){
                    filteredSupplierArray.add(my_supplier);
                }
            }

            // We inject the filtered supplier array to the returned object
            results.values = filteredSupplierArray;
            results.count = filteredSupplierArray.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if(results.values instanceof ArrayList) {
                myFilteredSuppliers_ = (ArrayList<Supplier>) results.values;
            }
            notifyDataSetChanged();
        }
    }
}