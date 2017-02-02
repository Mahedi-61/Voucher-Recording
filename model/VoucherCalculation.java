
package com.mahediapps.model;

import android.content.Context;

import java.util.ArrayList;

public class VoucherCalculation {

    private DatabaseHelper dbHelper;

    public VoucherCalculation(Context context) {
        dbHelper = new DatabaseHelper(context);
    }


    //Daily Voucher Calculation
    //Day - per day (8)
    public String getTotalAmountVoucherPerDay(String dayId) {
        Double totalAmount = 0.0D, d = 0.0D;
        ArrayList<String> amountList = getVoucherAmountPerDay(dayId);
        int i = 0;

        do {
            if (i >= amountList.size()) {
                return String.valueOf(totalAmount);
            }

            if(!(amountList.get(i)).equals(".")){
                try{
                    d = Double.parseDouble(amountList.get(i));
                }catch (Throwable e){
                    continue;
                }
                totalAmount = totalAmount + d;
            }
            i++;
        } while (true);
    }



    public ArrayList<String> getVoucherCategoryPerDay(String dayId) {
        return dbHelper.getEachColumnPerDay(dayId, "expense_category");
    }

    public ArrayList<String> getVoucherAmountPerDay(String dayId) {
        return dbHelper.getEachColumnPerDay(dayId, "expense_amount");
    }

    public ArrayList<String> getVoucherIdPerDay(String dayId) {
        return dbHelper.getEachColumnPerDay(dayId, "_id");
    }

    public ArrayList<String> getVoucherTimePerDay(String dayId) {
        return dbHelper.getEachColumnPerDay(dayId, "time");
    }

    public ArrayList<String> getVoucherNotePerDay(String dayId) {
        return dbHelper.getEachColumnPerDay(dayId, "expense_note");
    }

    //MonthlyVoucher Calculation
    //Month - per month (4)
    public String getTotalAmountVoucherPerMonth(String monthId) {
        Double totalAmount = 0.0D, d = 0.0D;
        ArrayList<String> amountList = getVoucherAmountPerMonth(monthId);
        int i = 0;

        do {
            if (i >= amountList.size()) {
                return String.valueOf(totalAmount);
            }

            if(!(amountList.get(i)).equals(".")){
                try{
                    d = Double.parseDouble(amountList.get(i));
                }catch (Throwable e){
                    continue;
                }
                totalAmount = totalAmount + d;
            }
            i++;
        } while (true);
    }

/*    public ArrayList<String> getTotalNoOfVoucherPerDayInPerMonth(String monthId){
        ArrayList<String> voucherNo = new ArrayList<>();
        ArrayList<String> voucherDatePerMonth = getVoucherDatePerMonth(monthId);

        for(String dayId: voucherDatePerMonth){
            voucherNo.add(getTotalNoOfVoucherPerDay(dayId));
        }
        return voucherNo;
    }

    public ArrayList<String> getTotalAmountVoucherPerDayInPerMonth(String monthId){
        ArrayList<String> voucherNo = new ArrayList<>();
        ArrayList<String> voucherDatePerMonth = getVoucherDatePerMonth(monthId);

        for(String dayId: voucherDatePerMonth){
            voucherNo.add(getTotalAmountVoucherPerDay(dayId));
        }
        return voucherNo;
    }*/

    public ArrayList<String> getVoucherCategoryPerMonth(String monthId) {
        return dbHelper.getEachColumnPerMonth(monthId, "expense_category");
    }

    public ArrayList<String> getVoucherAmountPerMonth(String monthId) {
        return dbHelper.getEachColumnPerMonth(monthId, "expense_amount");
    }

    public ArrayList<String> getVoucherIdPerMonth(String monthId) {
        return dbHelper.getEachColumnPerMonth(monthId, "_id");
    }

    public ArrayList<String> getVoucherNotePerMonth(String monthId) {
        return dbHelper.getEachColumnPerMonth(monthId, dbHelper.EXPENSE_NOTE);
    }

    public ArrayList<String> getVoucherDatePerMonth(String monthId) {
        return dbHelper.getEachColumnPerMonth(monthId, dbHelper.DATE);
    }

    //Month - per month per voucher type
    public ArrayList<String> getTotalAmountPerVoucherCategoryPerMonth(String monthId) {
        ArrayList<String> totalAmountList = new ArrayList<>();
        ArrayList<String> voucherTypeListPerMonth = getTotalUsedVoucherCategoryPerMonth(monthId);
        int i = 0;
        do {
            if (i >= voucherTypeListPerMonth.size()) {
                return totalAmountList;
            }

            Double totalAmount = 0.0D, d = 0.0D;
            ArrayList<String> amountList = getAmountPerVoucherCategoryPerMonth(monthId, voucherTypeListPerMonth.get(i));

            for(int j = 0; j < amountList.size(); j++){

                if(!(amountList.get(j)).equals(".")){
                    try{
                        d = Double.parseDouble(amountList.get(j));
                    }catch (Throwable e){
                        continue;
                    }
                    totalAmount = totalAmount + d;
                }
            }

            totalAmountList.add(String.valueOf(totalAmount));
            i++;
        } while (true);
    }


    public ArrayList<String> getAmountPerVoucherCategoryPerMonth(String monthId, String voucherType) {

        return dbHelper.getEachColumnPerTypePerMonth(monthId, voucherType, dbHelper.EXPENSE_AMOUNT);
    }


    public ArrayList<String> getNotePerVoucherCategoryPerMonth(String monthId, String voucherType) {

        return dbHelper.getEachColumnPerTypePerMonth(monthId, voucherType, dbHelper.EXPENSE_NOTE);
    }


    public ArrayList<String> getDatePerVoucherCategoryPerMonth(String monthId, String voucherType) {

        return dbHelper.getEachColumnPerTypePerMonth(monthId, voucherType, dbHelper.DATE);
    }

    public ArrayList<String> getIdPerVoucherCategoryPerMonth(String monthId, String voucherType) {

        return dbHelper.getEachColumnPerTypePerMonth(monthId, voucherType, dbHelper.ID);
    }


    public ArrayList<String> getTotalUsedVoucherCategoryPerMonth(String monthId) {
        return dbHelper.getUsedVoucherCategoryPerMonth(monthId);
    }


/*
    //YearlyVoucher Calculation
    public ArrayList<String> getTotalAmountPerVoucherTypePerYear(String yearId) {
        ArrayList<String> totalAmountList = new ArrayList<>();
        ArrayList<String> usedVoucherTypePerYearList = getUsedVoucherTypePerYear(yearId);
        int i = 0;
        do {
            if (i >= usedVoucherTypePerYearList.size()) {
                return totalAmountList;
            }
            totalAmountList.add(dbHelper.getAmountForEachVoucherTypePerYear(yearId, usedVoucherTypePerYearList.get(i)));
            i++;
        } while (true);
    }


    public String getTotalAmountVoucherPerYear(String yearId) {
        Double totalAmount = Double.valueOf(0.0D);
        ArrayList<String> usedVoucherTypePerYear = getUsedVoucherTypePerYear(yearId);
        int i = 0;
        do {
            if (i >= usedVoucherTypePerYear.size()) {
                return String.valueOf(totalAmount);
            }
            totalAmount = totalAmount +
                    Double.parseDouble(dbHelper.getAmountForEachVoucherTypePerYear(yearId, usedVoucherTypePerYear.get(i)));
            i++;
        } while (true);
    } */

    public String getTotalAmountVoucherPerYear(String yearId) {
        Double totalAmount = 0.0D, d = 0.0D;
        ArrayList<String> amountList = getVoucherAmountPerYear(yearId);
        int i = 0;

        do {
            if (i >= amountList.size()) {
                return String.valueOf(totalAmount);
            }

            if(!(amountList.get(i)).equals(".")){
                try{
                    d = Double.parseDouble(amountList.get(i));
                }catch (Throwable e){
                    continue;
                }
                totalAmount = totalAmount + d;
                i++;
            }
        } while (true);
    }


    public ArrayList<String> getVoucherCategoryPerYear(String yearId) {
        return dbHelper.getEachColumnPerYear(yearId, "expense_category");
    }

    public ArrayList<String> getVoucherAmountPerYear(String yearId) {
        return dbHelper.getEachColumnPerYear(yearId, "expense_amount");
    }

    public ArrayList<String> getVoucherIdPerYear(String yearId) {
        return dbHelper.getEachColumnPerYear(yearId, "_id");
    }

    public ArrayList<String> getVoucherNotePerYear(String yearId) {
        return dbHelper.getEachColumnPerYear(yearId, "expense_note");
    }

    public ArrayList<String> getVoucherDatePerYear(String yearId) {
        return dbHelper.getEachColumnPerYear(yearId, "date");
    }
    
     public ArrayList<String> getTotalUsedVoucherCategoryPerYear(String yearId) {
        return dbHelper.getUsedVoucherCategoryPerYear(yearId);
    }

    //Year - per year per voucher type
    public ArrayList<String> getTotalAmountPerVoucherCategoryPerYear(String yearId) {
        ArrayList<String> totalAmountList = new ArrayList<>();
        ArrayList<String> voucherTypeListPerYear = getTotalUsedVoucherCategoryPerYear(yearId);
        int i = 0;
        do {
            if (i >= voucherTypeListPerYear.size()) {
                return totalAmountList;
            }

            Double totalAmount = 0.0D, d = 0.0D;
            ArrayList<String> amountList = getVoucherAmountPerVoucherCategoryPerYear(yearId, voucherTypeListPerYear.get(i));

            for(int j = 0; j < amountList.size(); j++){

                if(!(amountList.get(j)).equals(".")){
                    try{
                        d = Double.parseDouble(amountList.get(j));
                    }catch (Throwable e){
                        continue;
                    }
                    totalAmount = totalAmount + d;
                }
            }
            totalAmountList.add(String.valueOf(totalAmount));
            i++;
        } while (true);
    }


    public ArrayList<String> getVoucherAmountPerVoucherCategoryPerYear(String yearId, String voucherType) {

        return dbHelper.getEachColumnPerTypePerYear(yearId, voucherType, dbHelper.EXPENSE_AMOUNT);
    }


}
