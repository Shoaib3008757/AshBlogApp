package ashir.ashblogapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button creatBlog;
    Button viewBlogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        onClickNewBlog();
        onClickViewBlogs();

    }

    public void init(){

        creatBlog = (Button) findViewById(R.id.btNewBlog);
        viewBlogs = (Button)findViewById(R.id.btViewBlogs);


    }

    //onClick handler for Create New Post Button
    public void onClickNewBlog() {

        creatBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, AshirNewBlogPost.class);
                startActivity(i);


            }
        });
    }



    //onClick handler for view post button

    public void onClickViewBlogs(){

        viewBlogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent viewPostActivity = new Intent(MainActivity.this, DisplayData.class);
                startActivity(viewPostActivity);

            }
        });
    }//end of onClickViewPost method
}

