package com.apps.harsh.nutritioninfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView description, desc_name;
    Button submit;
    EditText name;
    private FatSecretSearch mFatSecretSearch;
    private FatSecretGet mFatSecretGet;
    private static int CURRENT_PAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        description = findViewById(R.id.desc);
        desc_name = findViewById(R.id.descName);
        name = findViewById(R.id.name);
        submit = findViewById(R.id.get);

        mFatSecretSearch = new FatSecretSearch();
        mFatSecretGet = new FatSecretGet();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.length() > 0) {
                    searchFood(name.getText().toString(), CURRENT_PAGE);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter something", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * FatSecret Search
     */
    String food_description, food_id;
    String brand;
    @SuppressLint("StaticFieldLeak")
    private void searchFood(final String item, final int page_num) {
        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
//                mProgressMore.setVisibility(View.VISIBLE);
//                mProgressSearch.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... arg0) {
                JSONObject food = mFatSecretSearch.searchFood(item, page_num);
                JSONArray FOODS_ARRAY;
                try {
                    if (food != null) {
                        FOODS_ARRAY = food.getJSONArray("food");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                                JSONObject food_items = FOODS_ARRAY.optJSONObject(i);
                                String food_name = food_items.getString("food_name");
                                 food_description = food_items.getString("food_description");
                                String[] row = food_description.split("-");
                                String id = food_items.getString("food_type");
                                if (id.equals("Brand")) {
                                    brand = food_items.getString("brand_name");
                                }
                                if (id.equals("Generic")) {
                                    brand = "Generic";
                                }
                                    food_id = food_items.getString("food_id");
//                                mItem.add(new Item(food_name, row[1].substring(1),
//                                        "" + brand, food_id));

                            }
                        }
                    }
                } catch (JSONException exception) {
                    return "Error";
                }
                return food_id;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result.equals("Error"))
                    Toast.makeText(MainActivity.this, "No Items Containing Your Search", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(MainActivity.this, "IN SEARCH", Toast.LENGTH_SHORT).show();
                    desc_name.setVisibility(View.VISIBLE);
                    description.setText(result);
                    getFood(Integer.valueOf(result));
                }
            }
        }.execute();
    }

    /**
     * FatSecret get
     */
    String calories, carbohydrate, protein, fat, serving_description;
    @SuppressLint("StaticFieldLeak")
    private void getFood(final long id) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                JSONObject foodGet = mFatSecretGet.getFood(id);
                try {
                    if (foodGet != null) {
                        String food_name = foodGet.getString("food_name");
                        JSONObject servings = foodGet.getJSONObject("servings");

                        JSONObject serving = servings.getJSONObject("serving");
                        calories = serving.getString("calories");
                        carbohydrate = serving.getString("carbohydrate");
                        protein = serving.getString("protein");
                        fat = serving.getString("fat");
                        serving_description = serving.getString("serving_description");
                        Log.e("serving_description", serving_description);



                        /**
                         * Displays results in the LogCat
                         */
                        Log.e("food_name", food_name);
                        Log.e("calories", calories);
                        Log.e("carbohydrate", carbohydrate);
                        Log.e("protein", protein);
                        Log.e("fat", fat);
                    }

                } catch (JSONException exception) {
                    return "Error";
                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result.equals("Error"))
                    Toast.makeText(MainActivity.this, "No Items Containing Your Search", Toast.LENGTH_SHORT).show();
//                mCallbacks.fromFragment();
                else{
                    Toast.makeText(MainActivity.this, "IN GET", Toast.LENGTH_SHORT).show();
//                    desc_name.setVisibility(View.VISIBLE);
//                    description.setText(result);
                }
            }
        }.execute();
    }

//    private void getImplementation() {
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position < mItem.size()) { // Should to be refactored
//                    getFood(Long.valueOf(mItem.get(position - 1).getID()));
//                } else {
//                    if (mItem.size() == 20)
//                        searchFood(mSearch.getText().toString(), 1);
//                    else if (mItem.size() == 40)
//                        searchFood(mSearch.getText().toString(), 2);
//                    else if (mItem.size() == 60)
//                        searchFood(mSearch.getText().toString(), 3);
//                    else if (mItem.size() == 80)
//                        searchFood(mSearch.getText().toString(), 4);
//                    else if (mItem.size() == 100)
//                        searchFood(mSearch.getText().toString(), 5);
//                    else if (mItem.size() == 120)
//                        searchFood(mSearch.getText().toString(), 6);
//                    else if (mItem.size() == 140)
//                        searchFood(mSearch.getText().toString(), 7);
//                    else if (mItem.size() == 160)
//                        searchFood(mSearch.getText().toString(), 8);
//                    else if (mItem.size() == 180)
//                        searchFood(mSearch.getText().toString(), 9);
//                    else if (mItem.size() == 200)
//                        searchFood(mSearch.getText().toString(), 10);
//                }
//            }
//        });
//    }
}
