package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

public interface AccountSchema {
    String ACCOUNT_TABLE = "Accounts";
    String ACCOUNT_NO = "Account_No";
    String BANK = "Bank";
    String ACCOUNT_HOLDER = "Account_Holder";
    String BALANCE = "Balance";
    String ACCOUNT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + ACCOUNT_TABLE
            + " ("
            + ACCOUNT_NO
            + " TEXT PRIMARY KEY, "
            + BANK
            + " TEXT NOT NULL, "
            + ACCOUNT_HOLDER
            + " TEXT NOT NULL, "
            + BALANCE
            + " DOUBLE "
            + ")";
}
