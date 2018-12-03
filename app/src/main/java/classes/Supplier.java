package classes;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

import methods.Constants;
import local_host.DatabaseAdapter;

public class Supplier {

    // ---------------------------------------------------------------------------------------------
    // --------------------------------------- Attributes ------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ID
    private String surrogateKey_;

    // Basic Info
    private String name_;
    private String tag_;
    private String information_;

    // Arrays
    private ArrayList<Transaction> transactions_;

    // Online
    private int cloudSynced_;

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------- Constructors ------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // Create Constructor
    public Supplier(final String name,
                    final String tag){

        this.surrogateKey_ = UUID.randomUUID().toString();

        this.name_ = name;
        this.tag_ = tag;

        this.cloudSynced_ = Constants.PENDING;
    }

    // Pull from Local DB Constructor
    public Supplier(final String surrogateKey,
                    final String name,
                    final String tag,
                    final String information,
                    final int cloudSynced) {

        this.surrogateKey_ = surrogateKey;

        this.name_ = name;
        this.tag_ = tag;

        this.information_ = information;

        this.cloudSynced_ = cloudSynced;
    }


    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------- Getters --------------------------------------------
    // ---------------------------------------------------------------------------------------------
    public String getSurrogateKey(){
        return surrogateKey_;
    }

    public int getCloudSynced(){
        return cloudSynced_;
    }

    public String getName(){
        return name_;
    }

    public String getTag(){
        return tag_;
    }

    public String getInformation() {
        return information_;
    }

    public ArrayList<Transaction> getTransactions() {

        return transactions_;
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------- Setters --------------------------------------------
    // ---------------------------------------------------------------------------------------------
    public void setTransactions(final ArrayList<Transaction> transactions) {

        this.transactions_ = transactions;
    }

    public void setName(final Context myContext, final String name){

        this.name_ = name;
        this.cloudSynced_ = Constants.PENDING;

        // Update Locally
        getDB(myContext).updateSupplierName(this);
    }

    public void setTag(final Context myContext, final String tag){

        this.tag_ = tag;
        this.cloudSynced_ = Constants.PENDING;

        // Update Locally
        getDB(myContext).updateSupplierTag(this);
    }

    public void setInformation(final Context myContext, final String information){

        this.information_ = information;
        this.cloudSynced_ = Constants.PENDING;

        // Update Locally
        getDB(myContext).updateSupplierInformation(this);
    }

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------ Other Getters ------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /*public double getTotalByDay(final TimeStamp myTimeStamp){
        // Computes from the collections attribute the total amount for that day

        if(transactions_ == null){
            return 0;
        }

        if(transactions_.size() == 0){
            return 0;
        }

        double total = 0.0;

        for(int i = 0 ; i < transactions_.size() ; ++i){
            if(transactions_.get(i).getDate().isSameDay(myTimeStamp)){
                total += transactions_.get(i).getVolume();
            }
        }

        return total;
    }*/

    // For internal processes
    private DatabaseAdapter getDB(final Context myContext){

        DatabaseAdapter myDBAdapter = new DatabaseAdapter(myContext);
        myDBAdapter.open();

        return myDBAdapter;
    }


    // ---------------------------------------------------------------------------------------------
    // ------------------------------------- Custom Methods_General ----------------------------------------
    // ---------------------------------------------------------------------------------------------
    public void addTransaction(final Context myContext, final Transaction myTransaction){

        // Add to DB
        getDB(myContext).createTransaction(myTransaction);

    }

    public void delete(final Context myContext){

        // Update Locally
        getDB(myContext).deleteSupplier(this);
    }

    public boolean isValid(){

        if(surrogateKey_ == null){
            return false;

        }else if(surrogateKey_.isEmpty()){
            return false;

        }else if(name_ == null){
            return false;

        }else if(name_.isEmpty()){
            return false;

        }else if(tag_ == null){
            return false;

        }else if(tag_.isEmpty()){
            return false;

        }else{
            return true;
        }
    }
}
