package com.crowded_geek.jbossoutreachapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get repos JSON
    private static String url = "https://api.github.com/orgs/JBossOutreach/repos";

    ArrayList<HashMap<String, String>> repos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checking if internet is available
        try{
            if(!isConnected()){
                Toast.makeText(this, "Internet connection needed!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        repos = new ArrayList<>();
        lv = (ListView) findViewById(R.id.repoList);
        new GetRepos().execute();
    }

    public boolean isConnected() throws InterruptedException, IOException {
        final String command = "ping -i 5 -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    private class GetRepos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpDude sh = new HttpDude();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            System.out.println(jsonStr);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONArray reps = new JSONArray(jsonStr);
                    // looping through All Repositories
                    for (int i = 0; i < reps.length(); i++) {
                        JSONObject c = reps.getJSONObject(i);

                        String name = c.getString("name");
                        String description = c.getString("description");
                        if(description=="null"){
                            description="No Description provided";
                        }
                        // tmp hash map for single repo
                        HashMap<String, String> repo = new HashMap<>();

                        // adding each child node to HashMap key => value
                        repo.put("name", name);
                        repo.put("description", description);

                        // adding repo to repos list
                        repos.add(repo);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, repos,
                    R.layout.list_item, new String[]{"name", "description"}, new int[]{R.id.repName,
                    R.id.repDesc});

            lv.setAdapter(adapter);
        }

    }
}
