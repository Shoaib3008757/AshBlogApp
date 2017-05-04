package ashir.ashblogapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayData extends AppCompatActivity {

    DBHelper dbHelper;
    int mCode;
    String postData;
    ArrayList<AshirBlogPostingHelper> postsList;
    ListView listView;
    TextView tv;
    CheckBox checkbox;

    Adaptar mAdapter;
    int thisPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        init();
    }

    public void init(){
        dbHelper = new DBHelper(DisplayData.this);
        listView = (ListView)findViewById(R.id.listView);


        setListView();
        ListViewClickListener();

    }

    //addting data to listView
    public void setListView(){

        DBHelper dh = new DBHelper(DisplayData.this);
        postsList = new ArrayList<AshirBlogPostingHelper>();

        postsList = dbHelper.getAllPots();
        // Collections.reverse(postsList);

        mAdapter = new Adaptar(DisplayData.this, postsList);
        listView.setAdapter(mAdapter);
        listView.deferNotifyDataSetChanged();



    }






    //listView Click Listener
    public void ListViewClickListener(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                thisPosition = position;

                AshirBlogPostingHelper ph = new AshirBlogPostingHelper();
                tv = (TextView) view;

                if (thisPosition == position) {

                    final Dialog dialog = new Dialog(DisplayData.this);
                    dialog.setContentView(R.layout.delete_update_share_dialog_layout);
                    dialog.setTitle("Delete, Update, or Share...");
                    RelativeLayout updateLayout = (RelativeLayout) dialog.findViewById(R.id.updateLayout);
                    RelativeLayout shareLayout = (RelativeLayout) dialog.findViewById(R.id.shareLayout);
                    RelativeLayout deleteLayout = (RelativeLayout) dialog.findViewById(R.id.deleteLayout);

                    //setting listener for update text
                    updateLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            UpdateDataFromList();
                            dialog.cancel();




                        }
                    });//end of updateListener

                    //setting button listener for share button
                    shareLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            shareOnSocial();
                            dialog.cancel();




                        }
                    });//end of share listener

                    //setting listenr for delete data from list
                    deleteLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            deleteDataFromList();
                            dialog.cancel();


                        }
                    });//end of delete listener

                    dialog.show();

                }


                return false;
            }
        });
    }

    //method for updatating data from list
    public void UpdateDataFromList(){

        DBHelper dbHelper = new DBHelper(DisplayData.this);
        //dbHelper.getEmployeeByCode(position);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        AshirBlogPostingHelper ph = new AshirBlogPostingHelper();

        //getting text from textView in list
        String s1 = tv.getText().toString();

        Toast.makeText(getApplicationContext(), " "+ s1, Toast.LENGTH_SHORT).show();


        String query = "SELECT * FROM ashirblogTable WHERE Text='"+s1+"'";


        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {

            c.moveToFirst();
            int Code = c.getInt(c.getColumnIndex("id"));
            String currentPost = c.getString(c.getColumnIndex("Text"));


            ph.setDataPost(currentPost);


            Intent updateActivity = new Intent(DisplayData.this, UpdateDataOnDatabase.class);
            updateActivity.putExtra("current_text", ph.getPost());
            updateActivity.putExtra("Post_ID", Code);

            startActivity(updateActivity);
            finish();

        }

    }//ending UpdateDataFromList method

    //method for delete Data from list
    public void deleteDataFromList(){

        //continue delete
        new AlertDialog.Builder(DisplayData.this)
                .setTitle("Alert")
                .setMessage("Are you Sure To Delete Post")
                .setCancelable(true)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteData();

                    }

                })

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }// ending of deleteDataFromList method

    //method for share content
    public void shareOnSocial(){
        String text = tv.getText().toString();
        composeEmail(text);
    }//ending of shareOnSocial mehtod

    //method for sendng text to mail
    public void composeEmail(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");

        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);

        }

    }


    public void deleteData(){
        DBHelper dbHelper = new DBHelper(DisplayData.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        AshirBlogPostingHelper ph = new AshirBlogPostingHelper();
        String s1 = tv.getText().toString();
        String query = "SELECT * FROM ashirblogTable WHERE Text='"+s1+"'";

        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {

            c.moveToFirst();
            int code = c.getInt(c.getColumnIndex("id"));
            String currentPost = c.getString(c.getColumnIndex("Text"));

            ph.setDataPost(currentPost);

            boolean isDeleted = dbHelper.deleteFromTable(code);


            if (isDeleted) {
                Toast.makeText(getApplicationContext(), "Post Deleted", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(getApplicationContext(), "Post Deleted Fail", Toast.LENGTH_SHORT).show();
            }

        }

    }


}
