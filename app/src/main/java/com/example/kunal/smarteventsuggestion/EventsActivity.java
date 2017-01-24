package com.example.kunal.smarteventsuggestion;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import static java.lang.String.valueOf;

public class EventsActivity extends AppCompatActivity {

    ArrayList<String> nameArr = new ArrayList<>();
    ArrayList<String> descriptionArr = new ArrayList<>();
    ArrayList<Event> displayevents = new ArrayList<Event>();
    JSONObject response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        final Button button = (Button) findViewById(R.id.button2);
        final EditText textV = (EditText) findViewById(R.id.textView);
     //   final ListView listV = (ListView) findViewById(R.id.list);


        try{
            response = new JSONObject(getIntent().getStringExtra("Response"));
        }catch(Exception e){

        }


        displayevents = parseJSONData(response);

        CustomAdapter adapter = new CustomAdapter(getApplicationContext(),displayevents);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("enetr", displayevents.get(position).getName());
                Log.d("enetr", displayevents.get(position).toString());
                Log.d("enetr", displayevents.get(position).getId());

                //Log.d("enetr", displayevents.toString());
                String link ="http://www.facebook.com/"+ displayevents.get(position).getId() ;
                Log.d("The link is", link);
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String s = textV.getText().toString();
                ArrayList<String> eventList = findMatch(s);
                ArrayList<Event> finals = new ArrayList<Event>();



                for(String str : eventList){
                    for(int j =0 ; j< displayevents.size(); j++){


                           //Log.d("inside for", valueOf(str.equalsIgnoreCase(displayevents.get(j).getName())));
                        if(str.equalsIgnoreCase(displayevents.get(j).getName())){

                            finals.add(displayevents.get(j));
                            //Log.d("we are in loop", displayevents.get(j).toString());
                        }
                    }

                }



                ListView lv = (ListView) findViewById(R.id.list);
                Log.d("Array list values", finals.toString());
                lv.setAdapter(null);
                Log.d("finals", "adapter is set to null yayyyy");
                CustomAdapter adapter = new CustomAdapter(getApplicationContext(),finals);

                lv.setAdapter(adapter);

                if(s == null){
                    lv.setAdapter(null);
                    adapter = new CustomAdapter(getApplicationContext(),displayevents);
                     lv = (ListView) findViewById(R.id.list);
                    lv.setAdapter(adapter);

                }




            }
        });



    }
    private ArrayList<Event> parseJSONData(JSONObject response) {
        ArrayList<Event> events = new ArrayList<Event>();

        try {
            JSONArray data = response.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject singleEvent = data.getJSONObject(i);
                Event e = new Event();
                String name = " ";
                String description = "No Description found";
                String startTime = "0000-00-00 00:00:00";
                String location = " ";
                String id = " ";

                if ((String) singleEvent.getString("name") != null) {
                    name = (String) singleEvent.getString("name");
                    nameArr.add(name);
                }

                //if (null != singleEvent.getString("place.location.city"))
                //    startTime = singleEvent.getString("place.location.city");

                if (null != singleEvent.getString("id"))
                    id = singleEvent.getString("id");

                if((String) singleEvent.getString("description") != null){
                    description = (String) singleEvent.getString("description");
                    descriptionArr.add(description);
                }

                if (null != singleEvent.getString("start_time"))
                {
                    startTime = singleEvent.getString("start_time");
                    startTime = startTime.substring(0, startTime.indexOf('T'));
                }

                //Access place of event
                JSONObject place = singleEvent.getJSONObject("place");
                location  = place.getString("name");
                e.setName(name);
                e.setLocation(location);
                e.setId(id);
                e.setDescription(description);
                e.setStartTime(startTime);

                events.add(e);
                Log.d("event obj", e.toString());

            }
        } catch (JSONException e) {
            Log.d("Test", e.toString());
        }
        /**
         * Updating parsed JSON data into ListView
         * */
        return events;

    }
    public ArrayList<String> findMatch(String s){

        String words[] = s.split(" ");

        LinkedHashSet<String> finalevents = new LinkedHashSet<>();
        for ( String st : words){
            for( String stt : nameArr){
                if(stt.toLowerCase().contains(st.toLowerCase())== true){
                    finalevents.add(stt);

                }
            }
        }

     //   ArrayList<String> finalevents_desc = new ArrayList<>();

        for ( int i=0; i<words.length; i++){
            for (int j=0; j<descriptionArr.size(); j++){

                if(words[i].contains(descriptionArr.get(j))== true){
                    finalevents.add(nameArr.get(i));

                }
            }
        }


        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(finalevents);
        return arrayList;








    }
}
