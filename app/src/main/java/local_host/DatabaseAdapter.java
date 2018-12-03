package local_host;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

import classes.Supplier;
import classes.TimeStamp;
import classes.Transaction;
import methods.Constants;

public class DatabaseAdapter {

    // -------------------------------------------------------------------------
    // ------------------------------ TABLE ------------------------------------
    // -------------------------------------------------------------------------
    // Database Strings
    private static final int DB_VERSION = 1;

    // Table Names
    private static final String TABLE_SUPPLIERS = "suppliers";
    private static final String TABLE_TRANSACTIONS = "transactions";

    // Common column names
    private static final String LOCAL_ID = "local_id";
    private static final String SURROGATE_KEY = "surrogate_key";
    public static final String CREATED_TIME = "time_created";
    public static final String UPDATED_TIME = "time_updated";
    private static final String CLOUD_SYNCED = "cloud_synced";

    // Suppliers Table column names
    public static final String SUPPLIER_NAME = "supplier_name";
    public static final String SUPPLIER_TAG = "supplier_tag";
    public static final String SUPPLIER_INFORMATION = "supplier_attributes";

    // Transactions Table column names
    public static final String TRANSACTION_FOREIGN_KEY = "transaction_fk";

    public static final String TRANSACTION_DATE = "transaction_date";

    public static final String TRANSACTION_ATTRIBUTE1 = "attribute_1";
    public static final String TRANSACTION_ATTRIBUTE2 = "attribute_2";
    public static final String TRANSACTION_ATTRIBUTE3 = "attribute_3";

    // -------------------------------------------------------------------------
    // ----------------------- Table Create Statements -------------------------
    // -------------------------------------------------------------------------
    // Suppliers Table
    private static final String CREATE_TABLE_SUPPLIERS = "CREATE TABLE "
            + TABLE_SUPPLIERS + "("
            + LOCAL_ID + " INTEGER PRIMARY KEY,"
            + SURROGATE_KEY + " TEXT,"
            + CREATED_TIME + " TEXT,"
            + UPDATED_TIME + " TEXT,"
            + CLOUD_SYNCED + " INTEGER,"
            + SUPPLIER_NAME + " TEXT,"
            + SUPPLIER_TAG + " TEXT,"
            + SUPPLIER_INFORMATION + " TEXT" + ")";

    // Transactions Table
    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE "
            + TABLE_TRANSACTIONS + "("
            + LOCAL_ID + " INTEGER PRIMARY KEY,"
            + SURROGATE_KEY + " TEXT,"
            + CREATED_TIME + " TEXT,"
            + UPDATED_TIME + " TEXT,"
            + CLOUD_SYNCED + " INTEGER,"
            + TRANSACTION_FOREIGN_KEY + " TEXT,"
            + TRANSACTION_DATE + " TEXT,"
            + TRANSACTION_ATTRIBUTE1 + " REAL,"
            + TRANSACTION_ATTRIBUTE2 + " REAL,"
            + TRANSACTION_ATTRIBUTE3 + " REAL" + ")";


    // -------------------------------------------------------------------------
    // -------------------------- Database Helper ------------------------------
    // -------------------------------------------------------------------------
    // Creates Database
    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static DatabaseHelper mInstance = null;

        static DatabaseHelper getInstance(Context context) {

            if (mInstance == null) {
                mInstance = new DatabaseHelper(context.getApplicationContext());
            }
            return mInstance;
        }

        static void refreshInstance() {
            mInstance = null;
        }

        private DatabaseHelper(Context context) {
            super(context,Constants.LOCAL_DB_NAME + "_" + Constants.ACCOUNT_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL(CREATE_TABLE_SUPPLIERS);
            sqLiteDatabase.execSQL(CREATE_TABLE_TRANSACTIONS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            // on upgrade drop older tables
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIERS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);

            // create new tables
            onCreate(sqLiteDatabase);
        }
    }

    // -------------------------------------------------------------------------
    // -------------------------- Database Adapter -----------------------------
    // -------------------------------------------------------------------------
    private final Context myContext;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseAdapter(Context context) {

        this.myContext = context;
    }

    public void open() throws SQLException {

        dbHelper = DatabaseHelper.getInstance(myContext);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close(){

        DatabaseHelper.refreshInstance();

        if(dbHelper != null) {
            dbHelper.close();
        }

        if(sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }


    // ---------------------------------------------------------------------------------------------
    // ----------------------------------------- Getters -------------------------------------------
    // ---------------------------------------------------------------------------------------------
    private ArrayList<Supplier> getSuppliersByQuery(final String SuppliersSelectQuery){

        // Check if Supplier's Select Query Exist
        if(SuppliersSelectQuery == null){
            return null;
        }

        // Create Cursor
        Cursor supplierCursor;
        if(sqLiteDatabase.isOpen()) {
            supplierCursor = sqLiteDatabase.rawQuery(SuppliersSelectQuery,null);
        }else{
            return null;
        }

        // Checks if there is data
        if(!supplierCursor.moveToLast()){
            supplierCursor.close();
            return null;
        }

        // We create an empty array of suppliers
        ArrayList<Supplier> mySuppliers = new ArrayList<>();

        while(!supplierCursor.isBeforeFirst()) {

            // We create the supplier
            Supplier mySupplier = new Supplier(
                    supplierCursor.getString(supplierCursor.getColumnIndex(SURROGATE_KEY)),
                    supplierCursor.getString(supplierCursor.getColumnIndex(SUPPLIER_NAME)),
                    supplierCursor.getString(supplierCursor.getColumnIndex(SUPPLIER_TAG)),
                    supplierCursor.getString(supplierCursor.getColumnIndex(SUPPLIER_INFORMATION)),
                    supplierCursor.getInt(supplierCursor.getColumnIndex(CLOUD_SYNCED)));

            // Add to Array
            if(mySupplier.isValid()) {
                mySuppliers.add(mySupplier);
            }

            supplierCursor.moveToPrevious();
        }

        supplierCursor.close();

        return mySuppliers;
    }

    private ArrayList<Transaction> getTransactionsByQuery(final String TransactionsSelectQuery){

        // Check if Transaction's Select Query Exist
        if(TransactionsSelectQuery == null){
            return null;
        }

        // Create Cursor
        Cursor transactionCursor;
        if(sqLiteDatabase.isOpen()) {
            transactionCursor = sqLiteDatabase.rawQuery(TransactionsSelectQuery, null);
        }else{
            return null;
        }

        // Checks if there is data
        if(!transactionCursor.moveToLast()){
            transactionCursor.close();
            return null;
        }

        // Empty array of all suppliers
        ArrayList<Transaction> myTransactions = new ArrayList<>();


        while (!transactionCursor.isBeforeFirst()) {

            // We create the Transaction
            Transaction myNewTransaction = new Transaction(
                    transactionCursor.getString(transactionCursor.getColumnIndex(SURROGATE_KEY)),
                    transactionCursor.getString(transactionCursor.getColumnIndex(TRANSACTION_FOREIGN_KEY)),

                    TimeStamp.fromString(transactionCursor.getString(transactionCursor.getColumnIndex(TRANSACTION_DATE))),

                    transactionCursor.getDouble(transactionCursor.getColumnIndex(TRANSACTION_ATTRIBUTE1)),
                    transactionCursor.getDouble(transactionCursor.getColumnIndex(TRANSACTION_ATTRIBUTE2)),
                    transactionCursor.getDouble(transactionCursor.getColumnIndex(TRANSACTION_ATTRIBUTE3)),

                    transactionCursor.getInt(transactionCursor.getColumnIndex(CLOUD_SYNCED)));

            // Add to array
            if (myNewTransaction.isValid()) {
                myTransactions.add(myNewTransaction);
            }

            transactionCursor.moveToPrevious();
        }

        transactionCursor.close();

        return myTransactions;
    }

    public ArrayList<Supplier> getAllSuppliers(final boolean withTransactions){

        // Fetch All Suppliers and order them alphabetically
        String suppliersSelectQuery = "SELECT * FROM " + TABLE_SUPPLIERS +
                " ORDER BY " + SUPPLIER_NAME + " DESC";

        // Pull all suppliers from DB
        ArrayList<Supplier> mySuppliers = getSuppliersByQuery(suppliersSelectQuery);

        if(withTransactions && mySuppliers != null){

            for(Supplier aSupplier : mySuppliers){

                setTransactionsBySupplier(aSupplier);
            }
        }

        return mySuppliers;
    }

    public Supplier getSupplierBySurrogateKey(final String mySupplierSurrogateKey){

        // Supplier select query string
        String SelectQuery = "SELECT * FROM " + TABLE_SUPPLIERS + " WHERE "
                + SURROGATE_KEY + " = '" + mySupplierSurrogateKey + "'";

        ArrayList<Supplier> mySuppliers = getSuppliersByQuery(SelectQuery);

        if(mySuppliers == null){
            return null;
        }

        if(mySuppliers.size() != 1){
            return null;
        }

        // Set the transactions and return
        return setTransactionsBySupplier(mySuppliers.get(0));
    }

    private Supplier setTransactionsBySupplier(Supplier mySupplier){
        // Not final because we change it

        if(mySupplier == null){
            return null;
        }

        if(!mySupplier.isValid()){
            return mySupplier;
        }

        String transactionsSelectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " +
                TRANSACTION_FOREIGN_KEY + " = '" + mySupplier.getSurrogateKey() + "'" +
                " ORDER BY " + TRANSACTION_DATE + " ASC";

        mySupplier.setTransactions(getTransactionsByQuery(transactionsSelectQuery));

        return mySupplier;
    }

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------- Supplier Methods_General ------------------------------
    // ---------------------------------------------------------------------------------------------
    public void createSupplier(final Supplier mySupplier){

        // To Local
        ContentValues contentValues = new ContentValues();
        contentValues.put(SURROGATE_KEY, mySupplier.getSurrogateKey());

        String rightNowString = (new TimeStamp()).toString();
        contentValues.put(CREATED_TIME, rightNowString);
        contentValues.put(UPDATED_TIME, rightNowString);

        contentValues.put(CLOUD_SYNCED, mySupplier.getCloudSynced());

        contentValues.put(SUPPLIER_NAME, mySupplier.getName());
        contentValues.put(SUPPLIER_TAG, mySupplier.getTag());
        contentValues.put(SUPPLIER_INFORMATION, mySupplier.getInformation());

        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.insert(TABLE_SUPPLIERS, null, contentValues);
        }
    }

    public void updateSupplierName(final Supplier mySupplier) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATED_TIME, (new TimeStamp()).toString());
        contentValues.put(SUPPLIER_NAME, mySupplier.getName());
        contentValues.put(CLOUD_SYNCED, mySupplier.getCloudSynced());

        // updating row
        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.update(TABLE_SUPPLIERS, contentValues, SURROGATE_KEY + " = ?", new String[]{mySupplier.getSurrogateKey()});
        }
    }

    public void updateSupplierTag(final Supplier mySupplier) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATED_TIME, (new TimeStamp()).toString());
        contentValues.put(SUPPLIER_TAG, mySupplier.getTag());
        contentValues.put(CLOUD_SYNCED, mySupplier.getCloudSynced());

        // updating row
        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.update(TABLE_SUPPLIERS, contentValues, SURROGATE_KEY + " = ?", new String[]{mySupplier.getSurrogateKey()});
        }
    }

    public void updateSupplierInformation(final Supplier mySupplier) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATED_TIME, (new TimeStamp()).toString());
        contentValues.put(SUPPLIER_INFORMATION, mySupplier.getInformation());
        contentValues.put(CLOUD_SYNCED, mySupplier.getCloudSynced());

        // updating row
        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.update(TABLE_SUPPLIERS, contentValues, SURROGATE_KEY + " = ?", new String[]{mySupplier.getSurrogateKey()});
        }
    }

    public void updateCloudSynced(final Supplier mySupplier, final int responseCode){

        ContentValues contentValues = new ContentValues();
        contentValues.put(CLOUD_SYNCED, responseCode);

        // updating row
        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.update(TABLE_SUPPLIERS, contentValues, SURROGATE_KEY + " = ?", new String[]{mySupplier.getSurrogateKey()});
        }
    }

    public void deleteSupplier(final Supplier mySupplier) {

        // If transactions havent been set
        if(mySupplier.getTransactions() == null){
            setTransactionsBySupplier(mySupplier);
        }

        // Delete Transactions
        for(Transaction aTransaction : mySupplier.getTransactions()){
            deleteTransaction(aTransaction);
        }

        // Delete the supplier
        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.delete(TABLE_SUPPLIERS,SURROGATE_KEY + " = ?", new String[]{mySupplier.getSurrogateKey()});
        }
    }

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------ Transaction Methods_General ----------------------------
    // ---------------------------------------------------------------------------------------------
    public void createTransaction(final Transaction myTransaction) {

        // Insert Transaction in DB
        ContentValues contentValues = new ContentValues();
        contentValues.put(SURROGATE_KEY, myTransaction.getSurrogateKey());

        String rightNowString = (new TimeStamp()).toString();
        contentValues.put(CREATED_TIME, rightNowString);
        contentValues.put(UPDATED_TIME, rightNowString);

        contentValues.put(CLOUD_SYNCED, myTransaction.getCloudSynced());

        contentValues.put(TRANSACTION_FOREIGN_KEY, myTransaction.getForeignKey());

        contentValues.put(TRANSACTION_DATE, myTransaction.getDate().toString());

        contentValues.put(TRANSACTION_ATTRIBUTE1, myTransaction.getAttribute1());
        contentValues.put(TRANSACTION_ATTRIBUTE2, myTransaction.getAttribute2());
        contentValues.put(TRANSACTION_ATTRIBUTE3, myTransaction.getAttribute3());

        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.insert(TABLE_TRANSACTIONS, null, contentValues);
        }
    }

    public void updateTransactionAttribute1(final Transaction myTransaction) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATED_TIME, (new TimeStamp()).toString());
        contentValues.put(TRANSACTION_ATTRIBUTE1, myTransaction.getAttribute1());
        contentValues.put(CLOUD_SYNCED, myTransaction.getCloudSynced());

        // updating row
        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.update(TABLE_TRANSACTIONS, contentValues, SURROGATE_KEY + " = ?", new String[]{myTransaction.getSurrogateKey()});
        }
    }

    public void updateTransactionAttribute2(final Transaction myTransaction) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATED_TIME, (new TimeStamp()).toString());
        contentValues.put(TRANSACTION_ATTRIBUTE2, myTransaction.getAttribute2());
        contentValues.put(CLOUD_SYNCED, myTransaction.getCloudSynced());

        // updating row
        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.update(TABLE_TRANSACTIONS, contentValues, SURROGATE_KEY + " = ?", new String[]{myTransaction.getSurrogateKey()});
        }
    }

    public void updateTransactionAttribute3(final Transaction myTransaction) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATED_TIME, (new TimeStamp()).toString());
        contentValues.put(TRANSACTION_ATTRIBUTE3, myTransaction.getAttribute3());
        contentValues.put(CLOUD_SYNCED, myTransaction.getCloudSynced());

        // updating row
        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.update(TABLE_TRANSACTIONS, contentValues, SURROGATE_KEY + " = ?", new String[]{myTransaction.getSurrogateKey()});
        }
    }

    public void updateCloudSynced(final Transaction myTransaction, final int responseCode){

        ContentValues contentValues = new ContentValues();
        contentValues.put(CLOUD_SYNCED, responseCode);

        // updating row
        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.update(TABLE_TRANSACTIONS, contentValues, SURROGATE_KEY + " = ?", new String[]{myTransaction.getSurrogateKey()});
        }
    }

    public void deleteTransaction(final Transaction myTransaction) {

        // Delete from the Transactions Table
        if(sqLiteDatabase.isOpen()) {
            sqLiteDatabase.delete(TABLE_TRANSACTIONS,SURROGATE_KEY + " = ?", new String[]{myTransaction.getSurrogateKey()});
        }
    }


    // ---------------------------------------------------------------------------------------------
    // ------------------------------------ Special Getters ----------------------------------------
    // ---------------------------------------------------------------------------------------------
    public double getAttribute1Total_ByDay_BySupplier(final TimeStamp myTimeStamp, final String surrogateKey){

        String TransactionSelectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " +
                TRANSACTION_FOREIGN_KEY + " = '" + surrogateKey + "' AND " + TRANSACTION_DATE
                + " LIKE '" + myTimeStamp.getYearMonthDay() + "%'";

        ArrayList<Transaction> myTransactions = getTransactionsByQuery(TransactionSelectQuery);

        if(myTransactions != null){
            if(myTransactions.size() > 0) {

                double total_attribute1 = 0;

                for (Transaction aTransaction : myTransactions) {
                    total_attribute1 += aTransaction.getAttribute1();
                }

                return total_attribute1;
            }
        }

        return 0.0;
    }

}
