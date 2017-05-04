package ashir.ashblogapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateDataOnDatabase extends AppCompatActivity {

    EditText selectedText;
    Button update;
    DBHelper dbHelper;
    int postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data_on_database);

        selectedText = (EditText)findViewById(R.id.currentText);
        update = (Button)findViewById(R.id.Update);
        dbHelper = new DBHelper(UpdateDataOnDatabase.this);

        //getting values of ViewPost Activity
        Bundle extras = getIntent().getExtras();

        String selected_text = extras.getString("current_text");
        postID = extras.getInt("Post_ID");
        Log.i("ID", "" + postID);
        selectedText.setText(selected_text);

        UpdatePost();

    }

    //updating data
    public void UpdatePost(){
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedText.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Fill all fields", Toast.LENGTH_SHORT).show();
                }else {
                    boolean isUpdatated = dbHelper.updateTable(postID, selectedText.getText().toString());

                    if (isUpdatated) {
                        Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_LONG).show();
                        finish();
                        Intent i = new Intent(UpdateDataOnDatabase.this, DisplayData.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Data Not Aupdated", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }





}
