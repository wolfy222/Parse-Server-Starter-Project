package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity
{
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        //GETTING THE USERNAME WHICH IS CLICKED
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        setTitle(username+"'s Photos");


// to set images
       linearLayout = findViewById(R.id.linearLayout);



        // QUERY TO GET ALL IMAGE OBJECT  FOR A PARTICULAR USER THAT HAS THIS USERNAME
        ParseQuery<ParseObject> query = new ParseQuery<>("Image");
        query.whereEqualTo("username",username);
        query.orderByDescending("createdAt");


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e)
            {
                if(e == null )
                {
                    if(objects.size() > 0)
                    {
                        for (ParseObject object : objects)
                        {
                            // GETTING THE PARSE FILE FROM THEM TO GET THE ACTUAL IMAGE DATA
                            ParseFile file = (ParseFile) object.get("image");
                            //Downloading
                            file.getDataInBackground(new GetDataCallback() {

                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        // CREATING A IMAGE VIEW, ADDING THE BITMAP TO IT SETTING IMAGE TO IMAGE VIEW
                                        ImageView imageView = new ImageView(getApplicationContext());
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));
                                      //  imageView.setImageDrawable(getResources().getDrawable(R.drawable.instagram));
                                        imageView.setImageBitmap(bitmap);

                                        linearLayout.addView(imageView);

                                    }
                                }

                            });
                        }
                    }
                }
                else
                {
                    e.printStackTrace();
                }
            }
        });



    }
}
