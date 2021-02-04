package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentExpenseManager extends ExpenseManager{
    private Context context;

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.context=context;
        setup();
    }

    @Override
    public void setup() throws ExpenseManagerException {
        Database db = new Database(context, "180213f", null, 1);

        AccountDAO persistentADAO = new PersistentAccountDAO(db);
        TransactionDAO persistentTDAO = new PersistentTransactionDAO(db);
        setAccountsDAO(persistentADAO);
        setTransactionsDAO(persistentTDAO);
    }
}
