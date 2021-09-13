package com.cashmanagment.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.cashmanagment.models.HistoryModel

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CashDatabase"
        private const val TABLE_ACTIONS = "ActionTable"
        private const val TABLE_COUNT = "CountTable"

        // COLUMNS TABLE_CASH
        private const val KEY_ID = "_id"
        private const val KEY_TYPE = "type"
        private const val KEY_TAG = "tag"
        private const val KEY_AMOUNT = "amount"

        // COLUMNS TABLE_COUNT
        private const val KEY_COUNT = "count"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val queryCreateCashTable = (
                "CREATE TABLE $TABLE_ACTIONS (" +
                "$KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_TYPE INTEGER," +
                "$KEY_TAG TEXT," +
                "$KEY_AMOUNT INTEGER)")
        val queryCreateCounterTable = (
                "CREATE TABLE $TABLE_COUNT (" +
                "$KEY_COUNT NUMERIC)")

        db?.execSQL(queryCreateCashTable)
        db?.execSQL(queryCreateCounterTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ACTIONS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COUNT")
        onCreate(db)
    }

    fun updateCounter(newValue: Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        val currentValue: Int = getCounter()

        try {
           if (checkTableCount()) {
               // UPDATE
               cv.put(KEY_COUNT, currentValue + newValue )
               db.update(TABLE_COUNT, cv, "count = $currentValue", null )
               db.close()
            } else {
               // FIRST TIME
               cv.put(KEY_COUNT, newValue)
               db.insert(TABLE_COUNT, null, cv)
               db.close()
            }
        }catch (e: SQLiteException){
            Log.e("updateCounter",e.stackTraceToString())
        }
    }

    fun insertData(history: HistoryModel) {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(KEY_TYPE, history.type)
            put(KEY_TAG, history.tag)
            put(KEY_AMOUNT, history.amount)
        }
        try {
            db.insert(TABLE_ACTIONS, null, cv)
            db.close()
            updateCounter(history.amount)
        } catch (e: SQLiteException){
            Log.e("insertData", e.stackTraceToString())
        }
    }

    fun deleteData(id: String): Int{
        val db = this.writableDatabase
        return try {
            db.delete(TABLE_ACTIONS,"_id = ?", arrayOf(id))
        }catch (e: SQLiteException){
            0
        }
    }

    fun getCounter(): Int {
        val db = this.writableDatabase
        var counter = 0
        val queryTable = "SELECT * FROM $TABLE_COUNT"
        if (checkTableCount()) {
            try {
                val newCursor : Cursor = db.rawQuery(queryTable, null)
                newCursor.moveToFirst()
                counter = newCursor.getInt(0)
                newCursor.close()
            }catch (e: SQLiteException){
                Log.e("readCounter", e.stackTraceToString())
            }
        }
        return counter
    }

    private fun checkTableCount():Boolean{
        val db = this.writableDatabase
        val queryCounter = "SELECT count(*) FROM $TABLE_COUNT"
        val cursor : Cursor = db.rawQuery(queryCounter, null)
        try {
            cursor.moveToFirst()
            val tableRow = cursor.getInt(0)

            if (tableRow > 0) {
                return true
            }
        }catch (e: SQLiteException){
            Log.e("readCounter", e.stackTraceToString())
        }finally {
            cursor.close()
        }

        return false
    }

    fun readAll():ArrayList<HistoryModel> {
        val cashItemList = ArrayList<HistoryModel>()
        val query = "SELECT * FROM $TABLE_ACTIONS"
        val db = this.readableDatabase

        try {
            val cursor : Cursor = db.rawQuery(query, null)
            if(cursor.moveToFirst()){
                do {
                    val item = HistoryModel(
                            cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_TYPE)),
                            cursor.getString(cursor.getColumnIndex(KEY_TAG)),
                            cursor.getInt(cursor.getColumnIndex(KEY_AMOUNT)),
                    )
                    cashItemList.add(item)

                }while (cursor.moveToNext())
            }

            cursor.close()
        } catch (e: SQLiteException){
            db.execSQL(query)
            return ArrayList()
        }

        return cashItemList
    }

}