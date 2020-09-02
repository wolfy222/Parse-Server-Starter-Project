/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
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
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

  Boolean signUpModeActive = true;
  TextView logInTextView;

  @Override
  public void onClick(View v)
  {
    if(v.getId() == R.id.logInTextView)
    {
//      Log.i("Switch"," was tapped!");
      Button signUpButton = findViewById(R.id.signUpButton);

      if(signUpModeActive)
      {
        signUpModeActive =false;
        signUpButton.setText("Login");
        logInTextView.setText("Sign Up, instead?");
      }
      else
      {
        signUpModeActive =true;
        signUpButton.setText("Sign Up");
        logInTextView.setText("Login in, insted? ");
      }

    }

  }

  public void signUpClicked(View view)
  {

    EditText usernameEditText= findViewById(R.id.usernameEditText);
    EditText passwordEditText= findViewById(R.id.passwordEditText);
    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    mgr.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);

    if (usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals(""))
    {
      Toast.makeText(this, "A username and a password are required", Toast.LENGTH_SHORT).show();

    }
    else
    {
      if (signUpModeActive)
      {
        // Sing up a user
        ParseUser user = new ParseUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback()
        {
          @Override
          public void done(ParseException e)
          {
            if (e == null)
            {
              Log.i("Sign Up!", "Success");
              Toast.makeText(MainActivity.this, "Successfully Signed Up!", Toast.LENGTH_SHORT).show();
            }
            else
            {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
      else
      {
        // LOGIN CODE

        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e)
          {
            if(user != null )
            {
              Log.i("Login","ok!");
            }
            else
            {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    logInTextView= findViewById(R.id.logInTextView);
    logInTextView.setOnClickListener(this);


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}