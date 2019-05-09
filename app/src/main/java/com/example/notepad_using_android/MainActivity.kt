package com.example.notepad_using_android

import android.app.AlertDialog
import android.app.SearchManager
import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.*
import kotlinx.android.synthetic.main.row.view.*

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()


    //shared preferences
    var mSharedPref:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Shared Pref for Sorting Data
        mSharedPref=this.getSharedPreferences("My_data", Context.MODE_PRIVATE)
        //load sorting technique as selected before, default setting will be newest
        val mSorting=mSharedPref!!.getString("Sort","newest")
        when(mSorting)
        {
            "newest" -> loadQueryNewest("%")
            "oldest" -> loadQueryOldest("%")
            "ascending" -> loadQueryAscending("%")
            "descending" -> loadQueryDescending("%")
        }
        floatingActionButton.setOnClickListener {
            startActivity(Intent(this,AddNoteActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        //load sorting technique as selected before, default setting will be newest
        val mSorting=mSharedPref!!.getString("Sort","newest")
        when(mSorting)
        {
            "newest" -> loadQueryNewest("%")
            "oldest" -> loadQueryOldest("%")
            "ascending" -> loadQueryAscending("%")
            "descending" -> loadQueryDescending("%")
        }
    }

    private fun loadQueryAscending(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID","Title","Description")
        val selectionAgrs = arrayOf(title)
        //sort by title
        val cursor=dbManager.Query(projections, "Title like ?",selectionAgrs,"Title")
        listNotes.clear()
        //ascending
        if(cursor.moveToFirst())
        {
            do {
                val ID= cursor.getInt(cursor.getColumnIndex("ID"))
                val Title= cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"));

                listNotes.add(Note(ID,Title,Description))
            }
                while(cursor.moveToNext())
        }

        //adapter
        var myNotesAdapter=MyNoteAdapter(this,listNotes)
        //set adapter
        notesLv.adapter=myNotesAdapter

        //get total no tasks from list
        val total = notesLv.count
        //actionbar
        val mActionBar = supportActionBar
        if(mActionBar!=null)
        {
            //set to actionbox as subtitle of sctionbox
            mActionBar.subtitle="You have "+total+" note(s) in list..."
        }
    }

    private fun loadQueryDescending(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID","Title","Description")
        val selectionAgrs = arrayOf(title)
        //sort by title
        val cursor=dbManager.Query(projections, "Title like ?",selectionAgrs,"Title")
        listNotes.clear()
        //Descending
        if(cursor.moveToLast())
        {
            do {
                val ID= cursor.getInt(cursor.getColumnIndex("ID"))
                val Title= cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"));

                listNotes.add(Note(ID,Title,Description))
            }
            while(cursor.moveToPrevious())
        }
        //adapter
        var myNotesAdapter=MyNoteAdapter(this,listNotes)
        //set adapter
        notesLv.adapter=myNotesAdapter


        //get total no tasks from list
        val total = notesLv.count
        //actionbar
        val mActionBar = supportActionBar
        if(mActionBar!=null)
        {
            //set to actionbox as subtitle of sctionbox
            mActionBar.subtitle="You have "+total+" note(s) in list..."
        }

    }

    private fun loadQueryNewest(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID","Title","Description")
        val selectionAgrs = arrayOf(title)
        //sort by ID
        val cursor=dbManager.Query(projections, "ID like ?",selectionAgrs,"Title")
        listNotes.clear()
        if(cursor.moveToLast())
        {
            do {
                val ID= cursor.getInt(cursor.getColumnIndex("ID"))
                val Title= cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"));
                listNotes.add(Note(ID,Title,Description))
            }
            while(cursor.moveToPrevious())
        }

        //adapter
        var myNotesAdapter=MyNoteAdapter(this,listNotes)
        //set adapter
        notesLv.adapter=myNotesAdapter
        //get total no tasks from list
        val total = notesLv.count
        //actionbar
        val mActionBar = supportActionBar
        if(mActionBar!=null)
        {
            //set to actionbox as subtitle of sctionbox
            mActionBar.subtitle="You have "+total+" note(s) in list..."
        }
    }

    private fun loadQueryOldest(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID","Title","Description")
        val selectionAgrs = arrayOf(title)
        //sort by ID
        val cursor=dbManager.Query(projections, "ID like ?",selectionAgrs,"Title")
        listNotes.clear()
        if(cursor.moveToFirst())
        {
            do {
                val ID= cursor.getInt(cursor.getColumnIndex("ID"))
                val Title= cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"));

                listNotes.add(Note(ID,Title,Description))
            }
            while(cursor.moveToNext())
        }

        //adapter
        var myNotesAdapter=MyNoteAdapter(this,listNotes)
        //set adapter
        notesLv.adapter=myNotesAdapter
        //get total no tasks from list
        val total = notesLv.count
        //actionbar
        val mActionBar = supportActionBar
        if(mActionBar!=null)
        {
            //set to actionbox as subtitle of sctionbox
            mActionBar.subtitle="You have "+total+" note(s) in list..."
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        //searchView
        val sv: SearchView =menu!!.findItem(R.id.app_bar_serach).actionView as SearchView
        val sm=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQueryAscending("%" + query + "%" )
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadQueryAscending("%" + newText + "%" )
                return false
            }
        });
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!=null)
        {
            when(item.itemId)
            {
                R.id.action_sort->
                {
                    showSortDialog()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
       //list of sorting options
        val sortOption= arrayOf("Newest","Oldest","Title(Ascending)","Title(Descending)")
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle("Sort by")
        mBuilder.setIcon(R.drawable.ic_action_sort)
        mBuilder.setSingleChoiceItems(sortOption,-1){
            dialogInterface, i ->
            if(i==0){
                //newest first
                Toast.makeText(this,"Newest",Toast.LENGTH_SHORT).show()
                val editor=mSharedPref!!.edit()
                editor.putString("Sort","Newest")//where 'Sort' is key & 'newest' is value
                editor.apply()
                loadQueryNewest("%")
            }
            if(i==1)
            {
                //oldest first
                Toast.makeText(this,"Oldest",Toast.LENGTH_SHORT).show()
                val editor=mSharedPref!!.edit()
                editor.putString("Sort","Oldest")//where 'Sort' is key & 'Oldest' is value
                editor.apply()
                loadQueryOldest("%")
            }
            if(i==2)
            {
                //title ascending
                Toast.makeText(this,"Title(Ascending)",Toast.LENGTH_SHORT).show()
                val editor=mSharedPref!!.edit()
                editor.putString("Sort","ascending")//where 'Sort' is key & 'Ascending' is value
                editor.apply()
                loadQueryAscending("%")
            }
            if(i==3)
            {
                //title descending
                Toast.makeText(this,"Title(Descending)",Toast.LENGTH_SHORT).show()
                val editor=mSharedPref!!.edit()
                editor.putString("Sort","descending")//where 'Sort' is key & 'descending' is value
                editor.apply()
                loadQueryDescending("%")
            }
            dialogInterface.dismiss()
        }

        val mDialog=mBuilder.create()
        mDialog.show()
    }

    inner class MyNoteAdapter : BaseAdapter
    {
        var listNoteAdapter=ArrayList<Note>()
        var context:Context?=null

        constructor(context:Context,listNoteAdapter: ArrayList<Note>) : super() {
            this.listNoteAdapter = listNoteAdapter
            this.context=context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView=layoutInflater.inflate(R.layout.row,null)
            var myNote=listNoteAdapter[position]
            myView.titleTv.text = myNote.nodeName
            myView.descTv.text = myNote.nodeDes

            //deleteBtn
            myView.deleteBtn.setOnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myNote.nodeID.toString())
                dbManager.delete("ID=?",selectionArgs)
                loadQueryAscending("%")
            }
            myView.editBtn.setOnClickListener {
                GoToUpdateFun(myNote)
            }
            //copy btn click
            myView.copyBtn.setOnClickListener {
                //get Title
                val title=myView.titleTv.text.toString()
                //get Desc
                val desc=myView.descTv.text.toString()
                //concatition
                val s =title+"\n"+desc
                val cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cb.text= s
                Toast.makeText(this@MainActivity,"Copied...",Toast.LENGTH_SHORT).show()

            }

            //update btn
            myView.editBtn.setOnClickListener {
                GoToUpdateFun(myNote)
            }

            //share btn
            myView.shareBtn.setOnClickListener {
                val title=myView.titleTv.text.toString()
                //get Desc
                val desc=myView.descTv.text.toString()
                //concatition
                val s =title+"\n"+desc
                //
                val shareIntent = Intent()
                shareIntent.action=Intent.ACTION_SEND;
                shareIntent.type="text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT,s)
                startActivity(Intent.createChooser(shareIntent,s))
            }
            return myView
        }

        override fun getItem(position: Int): Any {
           return listNoteAdapter[position]
        }

        override fun getItemId(position: Int): Long {
           return position.toLong()
        }

        override fun getCount(): Int {
            return listNoteAdapter.size
        }

    }

    private fun GoToUpdateFun(myNote: Note) {
        var intent = Intent(this,AddNoteActivity::class.java)
        intent.putExtra("ID",myNote.nodeID)
        intent.putExtra("name",myNote.nodeName)
        intent.putExtra("des",myNote.nodeDes)
        startActivity(intent)
    }
}
