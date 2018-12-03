package classes;

import android.content.Context;

import java.util.UUID;

import methods.Constants;
import local_host.DatabaseAdapter;

public class Transaction {

    // ---------------------------------------------------------------------------------------------
    // --------------------------------------- Attributes ------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ID
    private String surrogateKey_;
    private String foreignKey_;

    // Basic Info
    private double attribute1_;
    private double attribute2_;
    private double attribute3_;
    private TimeStamp date_;

    // Online
    private int cloudSynced_;

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------- Constructors ------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // User Input Constructor
    public Transaction(final String foreignKey,
                       final TimeStamp date,
                       final double attribute1,
                       final double attribute2,
                       final double attribute3){

        this.surrogateKey_ = date.getYearMonthDay() + "_" + UUID.randomUUID().toString();

        this.foreignKey_ = foreignKey;

        this.attribute1_ = attribute1;
        this.attribute2_ = attribute2;
        this.attribute3_ = attribute3;
        this.date_ = date;

        this.cloudSynced_ = Constants.PENDING;
    }

    // Full Constructor - From Local DB
    public Transaction(final String surrogateKey,
                       final String foreignKey,
                       final TimeStamp date,
                       final double attribute1,
                       final double attribute2,
                       final double attribute3,
                       final int cloudSynced){

        this.surrogateKey_ = surrogateKey;
        this.foreignKey_ = foreignKey;

        this.attribute1_ = attribute1;
        this.attribute2_ = attribute2;
        this.attribute3_ = attribute3;
        this.date_ = date;

        this.cloudSynced_ = cloudSynced;
    }


    // ---------------------------------------------------------------------------------------------
    // --------------------------------------- Getters ---------------------------------------------
    // ---------------------------------------------------------------------------------------------
    public String getSurrogateKey() {
        return surrogateKey_;
    }

    public String getForeignKey() {
        return foreignKey_;
    }

    public TimeStamp getDate() {
        return date_;
    }

    public double getAttribute1() {
        return attribute1_;
    }

    public double getAttribute2() {
        return attribute2_;
    }

    public double getAttribute3() {
        return attribute3_;
    }

    public int getCloudSynced() {
        return cloudSynced_;
    }


    // ---------------------------------------------------------------------------------------------
    // --------------------------------------- Setters ---------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // For internal processes
    private DatabaseAdapter getDB(final Context myContext){

        DatabaseAdapter myDBAdapter = new DatabaseAdapter(myContext);
        myDBAdapter.open();

        return myDBAdapter;
    }

    public void setAttribute1(final Context myContext, final double attribute1){

        this.attribute1_ = attribute1;
        this.cloudSynced_ = Constants.PENDING;

        // Update Locally
        getDB(myContext).updateTransactionAttribute1(this);
    }

    public void setAttribute2(final Context myContext, final double attribute2){

        this.attribute2_ = attribute2;
        this.cloudSynced_ = Constants.PENDING;

        // Update Locally
        getDB(myContext).updateTransactionAttribute2(this);
    }

    public void setAttribute3(final Context myContext, final double attribute3){

        this.attribute3_ = attribute3;
        this.cloudSynced_ = Constants.PENDING;

        // Update Locally
        getDB(myContext).updateTransactionAttribute3(this);
    }

    public void delete(final Context myContext) {

        // Update Locally
        getDB(myContext).deleteTransaction(this);
    }

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------- Custom Methods_General ----------------------------------------
    // ---------------------------------------------------------------------------------------------
    public boolean isValid(){

        if(surrogateKey_ == null){
            return false;

        }else if(surrogateKey_.isEmpty()){
            return false;

        }else if(foreignKey_ == null){
            return false;

        }else if(foreignKey_.isEmpty()){
            return false;

        }else if(date_ == null){
            return false;

        }else if(attribute1_ <= 0){
            return false;
        }

        return true;
    }
}
