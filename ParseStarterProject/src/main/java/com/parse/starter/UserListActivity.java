package com.parse.starter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity
{

    public void getPhoto()
    {
        Intent intent = new Intent(Intent.ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);

    }
    @Override



    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage = data.getData();

        if(requestCode == 1 && resultCode == RESULT_OK && data != null)
        {
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Log.i("Image Selected","Good Work");

                // UPLOADING IMAGE TO PARSE
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);

                byte[] byteArray = stream.toByteArray();
                ParseFile file = new ParseFile("image.png", byteArray);

                ParseObject object = new ParseObject("Image");

                object.put("image",file);
                object.put("username",ParseUser.getCurrentUser().getUsername());
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null)
                        {
                            Toast.makeText(UserListActivity.this, "Image has been shared!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(UserListActivity.this, "There has been an issue uploading the image:( "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getPhoto();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.share:
 //               Log.i("Itemm ", "settings");

                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                else
                {
                    getPhoto();
                }

                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        final ListView listView = findViewById(R.id.listView);
        final ArrayList<String> usernames = new ArrayList<String>();

        usernames.add("test");

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,usernames);

        // To show all users
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        // Don't get current user
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e)
            {
                if(e == null )
                {
                    if(objects.size() > 0)
                    {
                        for (ParseUser user : objects)
                        {
                            Log.i("Username", user.getString("username"));
                            usernames.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
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
