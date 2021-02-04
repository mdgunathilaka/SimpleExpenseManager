package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

public interface TransactionSchema {
    String TRANSACTION_TABLE = "Transactions";
    String DATE = "Date";
    String ACCOUNT_NO = "Account_No";
    String TYPE = "Type";
    String AMOUNT = "Amount";
    String TRANSACTION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TRANSACTION_TABLE
            + " ("
            + DATE
            + " TEXT, "
            + ACCOUNT_NO
            + " TEXT NOT NULL, "
            + TYPE
            + " TEXT, "
            + AMOUNT
            + " DOUBLE,"
            +"FOREIGN KEY (Account_No) REFERENCES Accounts(Account_No)"
            + ")";
}
