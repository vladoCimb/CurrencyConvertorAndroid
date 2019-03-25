package com.example.currencyconvertor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChoiceActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener{

    private Button clear;
    private Button select;

    private List<Integer> selectList;

    private ChoiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        // Get preferences
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        ListView listView = findViewById(R.id.list);

        Button cancel = findViewById(R.id.cancel);
        clear = findViewById(R.id.clear);
        select = findViewById(R.id.select);


        if (cancel != null)
            cancel.setOnClickListener(this);

        if (clear != null)
            clear.setOnClickListener(this);

        if (select != null)
            select.setOnClickListener(this);

        if (listView != null)
        {
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
        }

        // Populate the lists
        List<Integer> flagList = Arrays.asList(MainActivity.CURRENCY_FLAGS);
        List<Integer> longNameList = Arrays.asList(MainActivity.CURRENCY_LONGNAMES);
        List<String> nameList = Arrays.asList(MainActivity.CURRENCY_NAMES);
        selectList = new ArrayList<>();

        // Create the adapter
        adapter = new ChoiceAdapter(this, R.layout.choice_item, flagList,
                nameList, longNameList, selectList);
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id)
        {
            // Cancel
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;

            // Clear
            case R.id.clear:
                if (clear != null)
                    clear.setEnabled(false);
                if (select != null)
                    select.setEnabled(false);

                // Start a new selection
                selectList.clear();
                adapter.notifyDataSetChanged();
                break;

            // Select
            case R.id.select:
                // Return new currency list in intent
                Intent intent = new Intent();
                intent.putIntegerArrayListExtra(MainActivity.CHOICE,
                        (ArrayList<Integer>) selectList);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id)
    {
        if (selectList.contains(position))
            selectList.remove(selectList.indexOf(position));

        else
            selectList.add(position);

        if (selectList.isEmpty())
        {
            if (clear != null)
                clear.setEnabled(false);
            if (select != null)
                select.setEnabled(false);
        } else {
            if (clear != null)
                clear.setEnabled(true);
            if (select != null)
                select.setEnabled(true);
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id)
    {
        if (clear != null)
            clear.setEnabled(true);
        if (select != null)
            select.setEnabled(true);

        // Start a new selection
        selectList.clear();
        selectList.add(position);
        adapter.notifyDataSetChanged();
        return true;
    }

}
