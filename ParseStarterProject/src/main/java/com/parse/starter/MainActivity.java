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
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener
{

  Boolean signUpModeActive = true;
  TextView logInTextView;
  EditText usernameEditText;
  EditText passwordEditText;

  public void showUserList()
  {
    Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
    //intent.putExtra("name", friends.get(position));
    startActivity(intent);

  }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
      if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
      {
        signUpClicked(v);
      }
        return false;
    }

  @Override
  public void onClick(View v)
  {
    if(v.getId() == R.id.logInTextView)  // FOR LOG IN TEXT VIEW
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
    else if(v.getId() == R.id.logoImageView || v.getId() == R.id.backgroundRelativeLayout)  // TO HIDE KEYBOARD
    {
      InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      mgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

  }

  public void signUpClicked(View view)
  {

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
              showUserList();
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
              Toast.makeText(MainActivity.this, "Successfully Logged Up!", Toast.LENGTH_SHORT).show();
              showUserList();
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

    // SETTING THE TITLE ON TOP
    setTitle("Instagram");

    usernameEditText= findViewById(R.id.usernameEditText);
    passwordEditText= findViewById(R.id.passwordEditText);
    logInTextView= findViewById(R.id.logInTextView);
    ImageView  logoImageView= findViewById(R.id.logoImageView);
    ConstraintLayout relativeLayout =  findViewById(R.id.backgroundRelativeLayout);

    relativeLayout.setOnClickListener(this);
    logoImageView.setOnClickListener(this);
    logInTextView.setOnClickListener(this);
    passwordEditText.setOnKeyListener(this);

    // Checking if someone is logged in
    if(ParseUser.getCurrentUser() != null)
    {
      showUserList();
    }


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }



}