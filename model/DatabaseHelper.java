package com.mahediapps.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static  String DB_NAME = "voucher_recording";
    public static  String DB_PATH = "";
    public static  int DB_VERSION = 1;
    public static  String TABLE_1 = "tb1_expense";
    public static  String TABLE_2 = "tb2_icome";
    public static  String TABLE_3 = "tb3_category";

    public static final String ID = "_id";
    public static final String DAY_ID = "day_id";
    public static final String MONTH_ID = "month_id";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year_id";
    public static final String TIME = "time";
    public static final String DATE = "date";
    public static final String EXPENSE_CATEGORY = "expense_category";
    public static final String EXPENSE_AMOUNT = "expense_amount";
    public static final String EXPENSE_NOTE = "expense_note";

    private static final String CATEGORY_NUMBER = "category_number";
    private static final String CATEGORY_NAME = "category_name";

    public static SQLiteDatabase db;
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

        File outFile = context.getDatabasePath(DB_NAME);
        DB_PATH = outFile.getPath();
    }


    private boolean checkDataBase() {
        return (new File(DB_PATH)).exists();
    }


    private void copyDataBase() throws IOException {
        InputStream inputstream = context.getAssets().open(DB_NAME);
        FileOutputStream fileoutputstream = new FileOutputStream(DB_PATH);

        byte abyte0[] = new byte[1024];
        do {
            int i = inputstream.read(abyte0);
            if (i <= 0) {
                fileoutputstream.flush();
                fileoutputstream.close();
                inputstream.close();
                return;
            }
            fileoutputstream.write(abyte0, 0, i);
        } while (true);
    }


    public void openDataBase() throws SQLException {
        db = SQLiteDatabase.openDatabase(DB_PATH, null, 0);
    }

    
    public void close() {
        db.close();
    }


    public void onCreate(SQLiteDatabase sqlitedatabase) {
    }


    public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {
    }
    

    public void createDataBase() throws IOException {
        if (!checkDataBase()) {
            getReadableDatabase().close();
            try {
                copyDataBase();
                return;

            } catch (IOException ioexception) {
                throw new Error("Error copying database");
            }
        } else {
            Log.e("allah help me", "database is already exits");
            getReadableDatabase().close();
            return;
        }
    }


    //table 1
    public ContentValues setAllContentValuesForTable1(HashMap<String, String> expenseVoucher) {
        ContentValues contentvalues = new ContentValues();
        
        contentvalues.put(DAY_ID,               expenseVoucher.get(DAY_ID));
        contentvalues.put(MONTH_ID,             expenseVoucher.get(MONTH_ID));
        contentvalues.put(DAY,                  Integer.parseInt(expenseVoucher.get(DAY)));
        contentvalues.put(MONTH,                Integer.parseInt(expenseVoucher.get(MONTH)));
        contentvalues.put(YEAR,                 expenseVoucher.get(YEAR));
        contentvalues.put(TIME,                 expenseVoucher.get(TIME));
        contentvalues.put(DATE,                 expenseVoucher.get(DATE));
        contentvalues.put(EXPENSE_CATEGORY,     expenseVoucher.get(EXPENSE_CATEGORY));
        contentvalues.put(EXPENSE_AMOUNT, expenseVoucher.get(EXPENSE_AMOUNT));
        contentvalues.put(EXPENSE_NOTE, expenseVoucher.get(EXPENSE_NOTE));
        
        return contentvalues;
    }


    //Get Each Column Per 1.DAY 2.MONTH 3.YEAR
    public ArrayList<String> getEachColumnPerDay(String dayId, String requiredColumn) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList<String> valueList = new ArrayList<>();

        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT ")).append(requiredColumn).append(" FROM ")
                .append(TABLE_1).append(" WHERE ").append(DAY_ID).append(" = '").append(dayId)
                .append("' ORDER BY ").append(ID).append(" * 1 ASC").toString(), null);

        if (myCursor.moveToFirst()) {
            do {
                if((requiredColumn.equals(DAY)) || (requiredColumn.equals(MONTH))){
                    valueList.add(String.valueOf(myCursor.getInt(0)));
                }else{
                    valueList.add(myCursor.getString(0));
                }
            } while (myCursor.moveToNext());
        }

        myCursor.close();
        sqlitedatabase.close();
        return valueList;
    }


    public ArrayList<String> getEachColumnPerMonth(String monthId, String requiredColumn) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList<String> valueList = new ArrayList<>();

        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT ")).append(requiredColumn).append(" FROM ")
                .append(TABLE_1).append(" WHERE ").append(MONTH_ID).append(" = '").append(monthId)
                .append("' ORDER BY ").append(DAY).append(" * 1 DESC").toString(), null);

        if (myCursor.moveToFirst()) {
            do {
                valueList.add(myCursor.getString(0));
            } while (myCursor.moveToNext());
        }

        myCursor.close();
        sqlitedatabase.close();
        return valueList;
    }



    public ArrayList<String> getEachColumnPerYear(String yearId, String requiredColumn) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList<String> valueList = new ArrayList<>();

        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT ")).append(requiredColumn).append(" FROM ")
                .append(TABLE_1).append(" WHERE ").append(YEAR).append(" = '").append(yearId)
                .append("' ORDER BY ").append(MONTH).append(" * 1 DESC, ")
                .append(DAY).append(" * 1 DESC").toString(), null);

        if (myCursor.moveToFirst()) {
            do {
                valueList.add(myCursor.getString(0));
            } while (myCursor.moveToNext());
        }

        myCursor.close();
        sqlitedatabase.close();
        return valueList;
    }



    //Get Used Voucher type per 1. MONTH 2. YEAR
    public ArrayList<String> getUsedVoucherCategoryPerMonth(String monthId) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList<String> voucherTypeList = new ArrayList<>();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT DISTINCT ").append(EXPENSE_CATEGORY)
                .append(" FROM ").append(TABLE_1).append(" WHERE ").append(MONTH_ID)
                .append(" = '")).append(monthId).append("'").toString(), null);

        if (myCursor.moveToFirst()) {
            do {
                voucherTypeList.add(myCursor.getString(0));

            } while (myCursor.moveToNext());
        }
        myCursor.close();
        sqlitedatabase.close();
        return voucherTypeList;
    }


    public ArrayList<String> getEachColumnPerTypePerMonth(String monthId, String voucherCategory, String requiredColumn) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList<String> valueList = new ArrayList<>();

        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT ")).append(requiredColumn)
                 .append(" FROM ").append(TABLE_1).append(" WHERE ").append(MONTH_ID).append(" = '")
                 .append(monthId).append("' AND ").append(EXPENSE_CATEGORY).append(" = '").append(voucherCategory)
                 .append("' ORDER BY ").append("_id").append(" * 1 ASC").toString(), null);

        if (myCursor.moveToFirst()) {
            do {
                valueList.add(myCursor.getString(0));
            } while (myCursor.moveToNext());
        }

        myCursor.close();
        sqlitedatabase.close();
        return valueList;
    }

    public ArrayList<String> getUsedVoucherCategoryPerYear(String yearId) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList<String> voucherTypeList = new ArrayList<>();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT DISTINCT ").append(EXPENSE_CATEGORY)
                .append(" FROM ").append(TABLE_1).append(" WHERE ").append(YEAR)
                .append(" = '")).append(yearId).append("'").toString(), null);

        if (myCursor.moveToFirst()) {
            do {
                voucherTypeList.add(myCursor.getString(0));

            } while (myCursor.moveToNext());
        }
        myCursor.close();
        sqlitedatabase.close();
        return voucherTypeList;
    }


    public ArrayList<String> getEachColumnPerTypePerYear(String yearId, String voucherCategory, String requiredColumn) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList<String> valueList = new ArrayList<>();

        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT ")).append(requiredColumn)
                .append(" FROM ").append(TABLE_1).append(" WHERE ").append(YEAR).append(" = '")
                .append(yearId).append("' AND ").append(EXPENSE_CATEGORY).append(" = '").append(voucherCategory)
                .append("' ORDER BY ").append("_id").append(" * 1 ASC").toString(), null);

        if (myCursor.moveToFirst()) {
            do {
                valueList.add(myCursor.getString(0));
            } while (myCursor.moveToNext());
        }

        myCursor.close();
        sqlitedatabase.close();
        return valueList;
    }


    public HashMap<String, String> getARowFromTable1(String sID) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        HashMap<String, String> valueList = new HashMap<>();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT * FROM " + TABLE_1 + " WHERE " +
                ID + " = '")).append(sID).append("'").toString(), null);

        if (myCursor != null && myCursor.getCount() > 0) {
            myCursor.moveToFirst();

            valueList.put(ID,       myCursor.getString(0));
            valueList.put(DAY_ID,   myCursor.getString(1));
            valueList.put(MONTH_ID, myCursor.getString(2));

            valueList.put(DAY,      myCursor.getString(3));
            valueList.put(MONTH,    myCursor.getString(4));
            valueList.put(YEAR,     myCursor.getString(5));

            valueList.put(TIME,     myCursor.getString(6));
            valueList.put(DATE,     myCursor.getString(7));

            valueList.put(EXPENSE_CATEGORY, myCursor.getString(8));
            valueList.put(EXPENSE_AMOUNT,   myCursor.getString(9));
            valueList.put(EXPENSE_NOTE,     myCursor.getString(10));
        }
        
        myCursor.close();
        sqlitedatabase.close();
        return valueList;
    }



    public void insertRowInTable1(HashMap<String, String> hashmap) {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        long l = sqlitedatabase.insert(TABLE_1, null, setAllContentValuesForTable1(hashmap));
        sqlitedatabase.close();
    }
    


    public void updateRowInTable1(HashMap<String, String> hashmap) {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.update(TABLE_1, setAllContentValuesForTable1(hashmap),
                "_id = ?", new String[]{ hashmap.get(ID)});
        sqlitedatabase.close();
    }



    public void deleteRowInTable1(String sID){
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.delete(TABLE_1, "_id = ?", new String[]{ sID });
        sqlitedatabase.close();
    }
    //table 2

    
    //table 3
    public ArrayList<String> getAllContentFromTable3() {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList<String> valueList = new ArrayList<>();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT * FROM " + TABLE_3)).toString(), null);

        if (myCursor.moveToFirst()) {
            do {
                valueList.add(myCursor.getString(2));
            } while (myCursor.moveToNext());
        }
        myCursor.close();
        sqlitedatabase.close();
        return valueList;
    }
    


    public void insertRowInTable3(String categoryName) {

        ContentValues contentvalues = new ContentValues();
        contentvalues.put(CATEGORY_NUMBER,     getAllContentFromTable3().size());
        contentvalues.put(CATEGORY_NAME,       categoryName);

        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.insert(TABLE_3, null, contentvalues);
        sqlitedatabase.close();
    }



    public void updateRowInTable3(HashMap<String, String> hashmap) {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
       // sqlitedatabase.update(TABLE_3, setAllContentValuesForTableMonthlyPlan(hashmap), "month_id = ?", new String[]{ hashmap.get(AllData.MP_Month_Id)});
        sqlitedatabase.close();
    }



}
