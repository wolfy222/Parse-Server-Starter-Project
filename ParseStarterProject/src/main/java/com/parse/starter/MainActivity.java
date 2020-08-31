/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    //Create Video Game to save score
    //Class is like a table
    ParseObject score = new ParseObject("Score");
    //Adding Info
    //     Column Name  Below it
    score.put("username","nick");
    score.put("score",45);
    //to save it in parse
    score.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e)
      {
        if(e == null)
        {
          // OK
          Log.i("Success","We saved the score");
        }
        else
        {
          e.printStackTrace();
        }
      }
    });



    //To get info from parse server

    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
    //Pass object id from parse server
    query.getInBackground("Bj2338YD6x", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e)
      {
        if(e == null && object != null)
        {
          // OK
          //Updating Info
          object.put("score",85);
          object.saveInBackground();

          Log.i("Username",object.getString("username"));
          Log.i("Score",Integer.toString(object.getInt("score")));

        }
        else
        {
          e.printStackTrace();
        }


      }
    });

    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}