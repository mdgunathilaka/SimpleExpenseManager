package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.TransactionSchema;

import android.util.Log;
import android.database.Cursor;
import android.database.sqlite.*;
import android.content.ContentValues;

public class PersistentTransactionDAO implements TransactionDAO,TransactionSchema{
    private Database db;
    private Cursor cursor;
    private ContentValues content;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public PersistentTransactionDAO(Database db){
        this.db=db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase sqliteDB = db.getWritableDatabase();
        content = new ContentValues();
        try {
            content.put(DATE, dateFormat.format(date));
            content.put(ACCOUNT_NO, accountNo);
            content.put(TYPE, expenseType.toString());
            content.put(AMOUNT, amount);
            sqliteDB.insert(TRANSACTION_TABLE,null,content);
        }catch(SQLiteConstraintException e){}
    }

    @Override
    public List<Transaction> getAllTransactionLogs(){
        SQLiteDatabase sqliteDB = db.getReadableDatabase();
        List<Transaction> transactionList = new ArrayList<>();
        cursor = sqliteDB.query(TRANSACTION_TABLE,new String[]{ACCOUNT_NO,DATE,TYPE,AMOUNT},null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String accNo = cursor.getString(cursor.getColumnIndexOrThrow(ACCOUNT_NO));
                String strDate = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
                Date date = null;
                try {
                    date = dateFormat.parse(strDate);
                }catch(ParseException e){}
                String strType = cursor.getString(cursor.getColumnIndexOrThrow(TYPE));
                ExpenseType expenseType = ExpenseType.valueOf(strType);
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(AMOUNT));
                transactionList.add(new Transaction(date,accNo,expenseType,amount));
            } while (cursor.moveToNext());
        }
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit){
        SQLiteDatabase sqliteDB = db.getReadableDatabase();
        List<Transaction> transactionList = new ArrayList<>();
        cursor = sqliteDB.query(TRANSACTION_TABLE,new String[]{ACCOUNT_NO,DATE,TYPE,AMOUNT},null,null,null,null,String.valueOf(limit));
        if(cursor.moveToFirst()){
            do{
                String accNo = cursor.getString(cursor.getColumnIndexOrThrow(ACCOUNT_NO));
                String strDate = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
                Date date = null;
                try {
                    date = dateFormat.parse(strDate);
                }catch(ParseException e){}
                String strType = cursor.getString(cursor.getColumnIndexOrThrow(TYPE));
                ExpenseType expenseType = ExpenseType.valueOf(strType);
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(AMOUNT));
                transactionList.add(new Transaction(date,accNo,expenseType,amount));
            } while (cursor.moveToNext());
        }
        return transactionList;
    }
}
