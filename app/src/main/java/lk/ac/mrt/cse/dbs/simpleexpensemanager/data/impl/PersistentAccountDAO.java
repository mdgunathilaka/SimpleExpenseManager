package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.AccountSchema;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.*;

public class PersistentAccountDAO implements AccountDAO,AccountSchema{
    private Database db;
    private Cursor cursor;
    private ContentValues content;

    public PersistentAccountDAO(Database db){
        this.db=db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase sqliteDB = db.getReadableDatabase();
        List<String> accountNumberList = new ArrayList<>();
        int index;
        cursor = sqliteDB.query(ACCOUNT_TABLE, new String[]{ACCOUNT_NO}, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                index = cursor.getColumnIndexOrThrow(ACCOUNT_NO);
                accountNumberList.add(cursor.getString(index));
            } while (cursor.moveToNext());
        }
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase sqliteDB = db.getReadableDatabase();
        List<Account> accountsList = new ArrayList<>();

        cursor = sqliteDB.query(ACCOUNT_TABLE,new String[]{ACCOUNT_NO, BANK, ACCOUNT_HOLDER, BALANCE}, null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String accNo = cursor.getString(cursor.getColumnIndexOrThrow(ACCOUNT_NO));
                String bank = cursor.getString(cursor.getColumnIndexOrThrow(BANK));
                String accHolder = cursor.getString(cursor.getColumnIndexOrThrow(ACCOUNT_HOLDER));
                double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(BALANCE));
                accountsList.add(new Account(accNo, bank, accHolder, balance));
            } while (cursor.moveToNext());
        }
        return accountsList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqliteDB = db.getReadableDatabase();
        cursor = sqliteDB.query(ACCOUNT_TABLE,new String[]{ACCOUNT_NO, BANK, ACCOUNT_HOLDER, BALANCE}, ACCOUNT_NO+" = ?", new String[]{accountNo},null,null,null);
        cursor.moveToFirst();
        String accNo = cursor.getString(cursor.getColumnIndexOrThrow(ACCOUNT_NO));
        String bank = cursor.getString(cursor.getColumnIndexOrThrow(BANK));
        String accHolder = cursor.getString(cursor.getColumnIndexOrThrow(ACCOUNT_HOLDER));
        double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(BALANCE));
        return new Account(accNo,bank,accHolder,balance);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sqliteDB = db.getWritableDatabase();
        try {
            content = new ContentValues();
            content.put(ACCOUNT_NO, account.getAccountNo());
            content.put(BANK, account.getBankName());
            content.put(ACCOUNT_HOLDER, account.getAccountHolderName());
            content.put(BALANCE, account.getBalance());
            sqliteDB.insert(ACCOUNT_TABLE, null, content);
        }catch(SQLiteConstraintException e){}
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqliteDB = db.getWritableDatabase();
        int deleted = sqliteDB.delete(ACCOUNT_TABLE, ACCOUNT_NO+" = ?", new String[]{accountNo});
        if(deleted<1) throw new InvalidAccountException(accountNo + " Invalid");
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase sqliteDB = db.getWritableDatabase();
        content = new ContentValues();
        double newAmount;
        Account currentDetails = getAccount(accountNo);
        if(expenseType.toString().equals("EXPENSE")){
            newAmount = currentDetails.getBalance()-amount;
        }
        else {
            newAmount = currentDetails.getBalance()+amount;
        }
        currentDetails.setBalance(newAmount);
        content.put(BALANCE,currentDetails.getBalance());

        int updated = sqliteDB.update(ACCOUNT_TABLE,content,ACCOUNT_NO+" = ?", new String[]{accountNo});
    }
}
