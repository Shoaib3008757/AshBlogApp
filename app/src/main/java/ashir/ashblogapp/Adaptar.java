package ashir.ashblogapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class Adaptar extends BaseAdapter {
    private Context mContext;

    public ArrayList<AshirBlogPostingHelper> postHelpers;
    private static LayoutInflater inflater=null;
    byte[] mImageBytes;
    boolean isChecked;



    //constructor
    public Adaptar(Context context, ArrayList<AshirBlogPostingHelper> arrayList){
        this.mContext = context;
        this.postHelpers = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return postHelpers.size();
    }

    @Override
    public Object getItem(int position) {
        return postHelpers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView==null){
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_layout, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.cutom_layout_textView);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.checkbox);


            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isChecked = isChecked;
                    AshirBlogPostingHelper ph = new AshirBlogPostingHelper();
                    ph.setIsValueChecked(isChecked);
                    if (isChecked){


                    }else{

                    }
                }
            });


            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        AshirBlogPostingHelper pH = new AshirBlogPostingHelper();
        pH = postHelpers.get(position);
        String post = pH.getPost();
        mImageBytes = pH.getImage();
        holder.textView.setText(String.valueOf(post.toString()));

        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(mImageBytes);
            Bitmap photo = BitmapFactory.decodeStream(stream);
            holder.imageView.setImageBitmap(photo);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return convertView;
    }

    public class Holder{
        ImageView imageView;
        TextView textView;
        CheckBox checkBox;


    }


}
