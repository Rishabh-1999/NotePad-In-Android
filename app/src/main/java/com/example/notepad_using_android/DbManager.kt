package com.example.notepad_using_android

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQuery
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast
import kotlin.reflect.KTypeProjection

class DbManager {

    //database Name
    var dbName = "MyNotes"
    //tale Name
    var dbTable = "Notes"
    //columns
    var colID = "ID"
    var colTitle = "Title"
    var colDes = "Description"
    //database version
    var dbVersion = 1

    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS "+dbTable+" ("+colID+" INTEGER PRIMARY KEY, "+ colTitle + " TEXT, "+colDes+" TEXT);"

    var sqlDB:SQLiteDatabase?=null

    constructor(context: Context) {
        var db = DatabaseHeplerNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHeplerNotes:SQLiteOpenHelper{

        var context: Context?=null
        constructor(context:Context):super(context,dbName,null,dbVersion) {
            this.context=context
        }

        override fun onCreate(db: SQLiteDatabase?) {
           db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context,"database created...",Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP table if Exists "+dbTable)
        }


    }

    fun insert(values:ContentValues):Long{
        val ID= sqlDB!!.insert(dbTable, "",values)
        return ID;
    }
    fun Query(projection: Array<String>,selection:String,selectionArgs: Array<String>, sorOrder:String): Cursor {
        val qb = SQLiteQueryBuilder();
        qb.tables=dbTable;
        val cursor=qb.query(sqlDB, projection,selection,selectionArgs, null,null,sorOrder)
        return cursor
    }

    fun delete(selection: String, selectionArgs: Array<String>):Int{
        val count = sqlDB!!.delete(dbTable, selection,selectionArgs)
        return count
    }

    fun update(values: ContentValues, selection:String,selectionArgs: Array<String>):Int{
        val count = sqlDB!!.update(dbTable,values, selection, selectionArgs)
        return count
    }

}