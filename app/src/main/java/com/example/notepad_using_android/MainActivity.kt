package com.example.notepad_using_android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.EventLogTags
import javax.security.auth.DestroyFailedException

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoadQuery("@")

    }
    private fun LoadQuery(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID","Title","Description")
        val selectionAgrs = arrayOf(title)
        val cursor=dbManager.Query(projections, "Title like ?",selectionAgrs,"Title")
        listNotes.clear()
        if(cursor.moveToFirst())
        {
            do {
                val ID= cursor.getInt(cursor.getColumnIndex("ID"))
                val Title= cursor.getString(cursor.getColumnIndex("Title"))
                val Description
            }
        }
    }
}
