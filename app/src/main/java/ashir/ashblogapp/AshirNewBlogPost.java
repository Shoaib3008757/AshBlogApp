package ashir.ashblogapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class AshirNewBlogPost extends AppCompatActivity {

    Context context;
    EditText newPost;
    Button postBtn;
    Button photobuton;
    //RelativeLayout photobuton;

    ImageView current_Photo;

    Bitmap bitmap;
    Bitmap photo;
    byte[] imageBytes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ashir_new_blog_post);


        newPost = (EditText)findViewById(R.id.writeNewPost);
        photobuton = (Button)findViewById(R.id.uploadImage);
        // photobuton = (RelativeLayout)findViewById(R.id.uploadImage);
        current_Photo  = (ImageView)findViewById(R.id.cameraPhoto);

        onClickPostButton();

        onPhotoButtonClick();
    }//end of onCreate
    public void onClickPostButton(){

        postBtn = (Button)findViewById(R.id.post);
        postBtn.setOnClickListener(new View.OnClickListener() {

            DBHelper databaseHelper = new DBHelper(getApplicationContext());

            @Override
            public void onClick(View v) {


                if (newPost.getText().toString().trim().length() == 0 && imageBytes == null ) {
                    Toast.makeText(getApplicationContext(), "Text Field or Image Should Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if(current_Photo.getDrawable()== null){
                    //inserting data to table

                    AshirBlogPostingHelper postHelper = new AshirBlogPostingHelper();
                    String textFromEditTextField = newPost.getText().toString();
                    postHelper.setDataPost(textFromEditTextField);

                    postHelper.setImage(null);

                    Log.d("Image", "Image from drawable " + current_Photo.getDrawable());

                    long isInsert = databaseHelper.ashirPostCreating(postHelper);
                    if (isInsert > -1) {
                        Toast.makeText(getApplicationContext(), "post Successful", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i = new Intent(AshirNewBlogPost.this, DisplayData.class);
                        startActivity(i);

                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry post Unsccessful", Toast.LENGTH_SHORT).show();
                    }
                }
                else {


                    //inserting data to table
                    AshirBlogPostingHelper postHelper = new AshirBlogPostingHelper();
                    String textFromEditTextField = newPost.getText().toString();
                    postHelper.setDataPost(textFromEditTextField);
                    postHelper.setImage(imageBytes);

                    long isInsert = databaseHelper.ashirPostCreating(postHelper);
                    if (isInsert > -1) {
                        Toast.makeText(getApplicationContext(), "post Successful", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i = new Intent(AshirNewBlogPost.this, DisplayData.class);
                        startActivity(i);

                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry post Unsccessful", Toast.LENGTH_SHORT).show();
                    }

                }
            }




        });
    }//end of postButton

    //Photo button handling
    public void onPhotoButtonClick(){
        photobuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(AshirNewBlogPost.this);
                dialog.setContentView(R.layout.image_upload_dilaog_layout);
                dialog.setTitle("Upload Photo From...");
                RelativeLayout imageFromeCamera = (RelativeLayout)dialog.findViewById(R.id.takeFromeCamera);
                imageFromeCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, 1);
                        dialog.cancel();

                    }
                });//end of image Frome Camera

                RelativeLayout imageFromeGalary = (RelativeLayout)dialog.findViewById(R.id.takeFromeGalary);
                imageFromeGalary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 2);
                        dialog.cancel();
                    }
                });


                dialog.show();



            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            bitmap = photo.createScaledBitmap(photo, 140, 160, false);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //initializing bytearryOutput stream
            //compressing image to jpeg format and storing in byte array
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            imageBytes = bytes.toByteArray();
            int NewHeight = bitmap.getHeight();
            int NewWidth = bitmap.getWidth();
            int oldheight = photo.getHeight();
            int oldwidht = photo.getWidth();
            Log.v("Height and Width ", "Old Height: " + oldheight + " Old Width: " + oldwidht);
            Log.v("Height and Width ", "New Height: " + NewHeight + " New Width: " + NewWidth);
            current_Photo.setImageBitmap(bitmap);

        }else if(requestCode==2 && resultCode==RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
                bitmap = bmp.createScaledBitmap(bmp, 140, 160, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                //initializing bytearryOutput stream
                //compressing image to jpeg format and storing in byte array
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                imageBytes = bytes.toByteArray();
                int NewHeight = bitmap.getHeight();
                int NewWidth = bitmap.getWidth();
                int oldheight = bmp.getHeight();
                int oldwidht = bmp.getWidth();
                Log.v("Height and Width ", "Old Height: " + oldheight + " Old Width: " + oldwidht);
                Log.v("Height and Width ", "New Height: " + NewHeight + " New Width: " + NewWidth);
                //current_Photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            current_Photo.setImageBitmap(bitmap);

        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }




}
