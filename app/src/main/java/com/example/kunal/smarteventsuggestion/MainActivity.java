package com.example.kunal.smarteventsuggestion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    AccessToken accessToken;

    // Will show the string "data" that holds the results
    TextView results;
    // URL of object to be parsed
    String JsonURL = "https://raw.githubusercontent.com/ianbar20/JSON-Volley-Tutorial/master/Example-JSON-Files/Example-Object.JSON";
    // This string will hold the results
    String data = "";
    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment

        //We need permissions for events that the user has rsvp-ed to
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_events"));

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                accessToken = AccessToken.getCurrentAccessToken();
                GraphRequest request = GraphRequest.newGraphPathRequest(
                        accessToken,
                        "/me/events",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                // Insert your code here
                                Log.d("Response", response.toString());
                                JSONObject graphResponse = response.getJSONObject();
                                //parseJSONData(graphResponse);
                                Intent i = new Intent(MainActivity.this,EventsActivity.class);
                                i.putExtra("Response",graphResponse.toString());
                                startActivity(i);
                                finish();
                            }
                        });
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        //A simple access token tracker
        accessTokenTracker = new AccessTokenTracker()
        {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };


        // A simple profile change tracker
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };


//        // Creates the Volley request queue
//        requestQueue=Volley.newRequestQueue(this);
//
//        // Casts results into the TextView found within the main layout XML with id jsonData
//        results=(TextView)
//
//                findViewById(R.id.jsonData);
//
//        // Creating the JsonObjectRequest class called obreq, passing required parameters:
//        //GET is used to fetch data from the server, JsonURL is the URL to be fetched from.
//        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, JsonURL,
//                // The third parameter Listener overrides the method onResponse() and passes
//                //JSONObject as a parameter
//                new Response.Listener<JSONObject>() {
//
//                    // Takes the response from the JSON request
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONObject obj = response.getJSONObject("colorObject");
//                            // Retrieves the string labeled "colorName" and "description" from
//                            //the response JSON Object
//                            //and converts them into javascript objects
//                            String color = obj.getString("colorName");
//                            String desc = obj.getString("description");
//
//                            // Adds strings from object to the "data" string
//                            data += "Color Name: " + color +
//                                    "nDescription : " + desc;
//
//                            // Adds the data string to the TextView "results"
//                            results.setText(data);
//                        }
//                        // Try and catch are included to handle any errors due to JSON
//                        catch (JSONException e) {
//                            // If an error occurs, this prints the error to the log
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                // The final parameter overrides the method onErrorResponse() and passes VolleyError
//                //as a parameter
//                new Response.ErrorListener() {
//                    @Override
//                    // Handles errors that occur due to Volley
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley", "Error");
//                    }
//                }
//        );
//        // Adds the JSON object request "obreq" to the request queue
//        requestQueue.add(obreq);


    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //We would want to stop certain trackers when we destroy a fragment or a screen
    public void onDestroy(){
        super.onDestroy();
        profileTracker.stopTracking();
        accessTokenTracker.stopTracking();
    }

    private ArrayList<Event> parseJSONData(JSONObject response) {
        Log.d("sDFVSDVSDVDS", response.toString());
        //Toast.makeText(MainActivity.this, "Repsonse: " + response.toString(), Toast.LENGTH_LONG).show();
        ArrayList<Event> events = new ArrayList<Event>();
        try {
            JSONArray data = response.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject singleEvent = data.getJSONObject(i);
                String name = (String) singleEvent.getString("name");
                String description = (String) singleEvent.getString("description");
                String startTime = singleEvent.getString("start_time");
                Event e = new Event();
                e.setName(name);
                e.setDescription(description);
                e.setStartTime(startTime);
                events.add(e);
            }
        } catch (JSONException e) {
        }

        return events;
    }

}
