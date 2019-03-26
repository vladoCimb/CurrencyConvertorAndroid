package com.example.currencyconvertor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements TextWatcher, AdapterView.OnItemClickListener {

    // Initial currency name list
    public static final String CURRENCY_LIST[] =
    {
            "USD", "GBP", "CAD", "AUD"
    };

    // Currency names
    public static final String CURRENCY_NAMES[] =
    {
            "EUR", "USD", "JPY", "BGN",
            "CZK", "DKK", "GBP", "HUF",
            "PLN", "RON", "SEK", "CHF",
            "NOK", "HRK", "RUB", "TRY",
            "AUD", "BRL", "CAD", "CNY",
            "HKD", "IDR", "ILS", "INR",
            "ISK", "KRW", "MXN", "MYR",
            "NZD", "PHP", "SGD", "THB",
            "ZAR"
     };

    // Currency symbols
    public static final String CURRENCY_SYMBOLS[] =
    {
            "€", "$", "¥", "лв",
            "Kč", "kr", "£", "Ft",
            "zł", "lei", "kr", "",
            "kr", "kn", "₽", "₺",
            "$", "R$", "$", "¥",
            "$", "Rp", "₪", "₹",
            "kr", "₩", "$", "RM",
            "$", "₱", "$", "฿", "S"
     };

    // Currency long names
    public static final Integer CURRENCY_LONGNAMES[] =
    {
            R.string.long_eur, R.string.long_usd, R.string.long_jpy,
            R.string.long_bgn, R.string.long_czk, R.string.long_dkk,
            R.string.long_gbp, R.string.long_huf, R.string.long_pln,
            R.string.long_ron, R.string.long_sek, R.string.long_chf,
            R.string.long_nok, R.string.long_hrk, R.string.long_rub,
            R.string.long_try, R.string.long_aud, R.string.long_brl,
            R.string.long_cad, R.string.long_cny, R.string.long_hkd,
            R.string.long_idr, R.string.long_ils, R.string.long_inr,
            R.string.long_isk, R.string.long_krw, R.string.long_mxn,
            R.string.long_myr, R.string.long_nzd, R.string.long_php,
            R.string.long_sgd, R.string.long_thb, R.string.long_zar
    };

    // Currency flags
    public static final Integer CURRENCY_FLAGS[] =
    {
            R.drawable.flag_eur, R.drawable.flag_usd, R.drawable.flag_jpy,
            R.drawable.flag_bgn, R.drawable.flag_czk, R.drawable.flag_dkk,
            R.drawable.flag_gbp, R.drawable.flag_huf, R.drawable.flag_pln,
            R.drawable.flag_ron, R.drawable.flag_sek, R.drawable.flag_chf,
            R.drawable.flag_nok, R.drawable.flag_hrk, R.drawable.flag_rub,
            R.drawable.flag_try, R.drawable.flag_aud, R.drawable.flag_brl,
            R.drawable.flag_cad, R.drawable.flag_cny, R.drawable.flag_hkd,
            R.drawable.flag_idr, R.drawable.flag_ils, R.drawable.flag_inr,
            R.drawable.flag_isk, R.drawable.flag_kpw, R.drawable.flag_mxn,
            R.drawable.flag_myr, R.drawable.flag_nzd, R.drawable.flag_php,
            R.drawable.flag_sgd, R.drawable.flag_thb, R.drawable.flag_zar
    };

    final static String CHOICE = "choice";
    public static final String PREF_NAMES = "pref_names";
    public static final String PREF_VALUES = "pref_values";
    public static final String ECB_DAILY_URL =
            "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    private ImageView flagView;
    private TextView nameView;
    private TextView symbolView;
    private EditText editView;
    private TextView longNameView;
    private ListView listView;
    private CurrencyAdapter adapter;

    private boolean selectAll = true;
    private boolean select = true;

    private List<Integer> flagList;
    private List<String> nameList;
    private List<String> symbolList;
    private List<String> valueList;
    private List<Integer> longNameList;

    private List<Integer> selectList;
    private List<String> currencyNameList;

    private int currentIndex = 0;

    private Map<String, Double> valueMap;

    private Parser parser;


    private double currentValue = 1.0;
    private double convertValue = 1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flagList = new ArrayList<>();
        nameList = new ArrayList<>();
        symbolList = new ArrayList<>();
        valueList = new ArrayList<>();
        longNameList = new ArrayList<>();
        selectList = new ArrayList<>();

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        listView = findViewById(R.id.list);
        flagView = findViewById(R.id.flag);
        nameView = findViewById(R.id.name);
        symbolView = findViewById(R.id.symbol);
        editView = findViewById(R.id.edit);
        longNameView = findViewById(R.id.long_name);

        parser = new Parser();
        parser.createDummyValue();

        valueMap = parser.getMap();

        currencyNameList = Arrays.asList(CURRENCY_NAMES);


        createInitList();

        if (editView != null)
        {
            editView.addTextChangedListener(this);
        }

        if (listView != null)
        {
            listView.setOnItemClickListener(this);
        }

        adapter = new CurrencyAdapter(this, R.layout.item, flagList, nameList,
                symbolList, valueList, longNameList,
                selectList);

        listView.setAdapter(adapter);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onResume(){
        super.onResume();

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(3);
        numberFormat.setMaximumFractionDigits(3);

        NumberFormat englishFormat = NumberFormat.getInstance(Locale.ENGLISH);

        String n = editView.getText().toString();
        if (n.length() > 0)
        {
            // Parse current value
            try
            {
                Number number = numberFormat.parse(n);
                currentValue = number.doubleValue();
            }
            catch (Exception e)
            {
                // Try English locale
                try
                {
                    Number number = englishFormat.parse(n);
                    currentValue = number.doubleValue();
                }

                // Do nothing on exception
                catch (Exception ex)
                {
                    return;
                }
            }
        }

        // Recalculate all the values
        valueList.clear();
        for (String name : nameList)
        {
            Double value = (currentValue / convertValue) *
                    valueMap.get(name);

            String s = numberFormat.format(value);
            valueList.add(s);
        }

        // Notify the adapter
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            // Add
            case R.id.action_add:
                return onAddClick();

            // Refresh
            case R.id.action_refresh:
                return onRefreshClick();
        }
        return false;
    }

    private boolean onAddClick()
    {
        // Start the choice dialog
        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivityForResult(intent, 0);

        return true;
    }

    private boolean onRefreshClick()
    {
//        // Check connectivity before refresh
//        ConnectivityManager manager =
//                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        NetworkInfo info = manager.getActiveNetworkInfo();
//
//        // Check connected
//
//        // Start the task
//        if (data != null)
//            data.startParseTask(ECB_DAILY_URL);

        return true;
    }

    public void createInitList(){
        flagList.add(CURRENCY_FLAGS[1]);
        nameList.add(CURRENCY_NAMES[1]);
        symbolList.add(CURRENCY_SYMBOLS[1]);
        valueList.add(valueMap.get(CURRENCY_NAMES[1]).toString());
        longNameList.add(CURRENCY_LONGNAMES[1]);

        flagList.add(CURRENCY_FLAGS[4]);
        nameList.add(CURRENCY_NAMES[4]);
        symbolList.add(CURRENCY_SYMBOLS[4]);
        valueList.add(valueMap.get(CURRENCY_NAMES[4]).toString());
        longNameList.add(CURRENCY_LONGNAMES[4]);


        flagList.add(CURRENCY_FLAGS[6]);
        nameList.add(CURRENCY_NAMES[6]);
        symbolList.add(CURRENCY_SYMBOLS[6]);
        valueList.add(valueMap.get(CURRENCY_NAMES[6]).toString());
        longNameList.add(CURRENCY_LONGNAMES[6]);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        // Do nothing if cancelled
        if (resultCode != RESULT_OK)
            return;

        // Get index list from intent
        List<Integer> indexList = data.getIntegerArrayListExtra(CHOICE);

        // Add currencies from list
        for (int index : indexList)
        {
            // Don't add duplicates
            if (nameList.contains(CURRENCY_NAMES[index]))
                continue;

            flagList.add(CURRENCY_FLAGS[index]);
            nameList.add(CURRENCY_NAMES[index]);
            symbolList.add(CURRENCY_SYMBOLS[index]);
            longNameList.add(CURRENCY_LONGNAMES[index]);

            Double value = 1.0;


            NumberFormat numberFormat = NumberFormat.getInstance();
            String s = numberFormat.format(value);

            valueList.add(s);
        }

        // Get preferences
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        // Get editor
        SharedPreferences.Editor editor = preferences.edit();

        // Get entries
        JSONArray nameArray = new JSONArray(nameList);
        JSONArray valueArray = new JSONArray(valueList);

        //update preferences
        editor.putString(PREF_NAMES, nameArray.toString());
        editor.putString(PREF_VALUES, valueArray.toString());
        editor.apply();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(3);
        numberFormat.setMaximumFractionDigits(3);

        NumberFormat englishFormat = NumberFormat.getInstance(Locale.ENGLISH);

        String n = editable.toString();
        if (n.length() > 0)
        {
            // Parse current value
            try
            {
                Number number = numberFormat.parse(n);
                currentValue = number.doubleValue();
            }
            catch (Exception e)
            {
                // Try English locale
                try
                {
                    Number number = englishFormat.parse(n);
                    currentValue = number.doubleValue();
                }

                // Do nothing on exception
                catch (Exception ex)
                {
                    return;
                }
            }
        }

        // Recalculate all the values
        valueList.clear();
        for (String name : nameList)
        {
            Double value = (currentValue / convertValue) *
                    valueMap.get(name);

            String s = numberFormat.format(value);
            valueList.add(s);
        }

        // Notify the adapter
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String value;
        int oldIndex;
        double oldValue;

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(3);
        numberFormat.setMaximumFractionDigits(3);

        oldIndex = currentIndex;
        oldValue = currentValue;

        // Set the current currency from the list
        currentIndex = currencyNameList.indexOf(nameList.get(position));

        currentValue = (oldValue / convertValue) *
                valueMap.get(CURRENCY_NAMES[currentIndex]);

        convertValue = valueMap.get(CURRENCY_NAMES[currentIndex]);

        numberFormat.setGroupingUsed(false);
        value = numberFormat.format(currentValue);

        if (editView != null)
        {
            editView.setText(value);
            if (selectAll)
            {
                // Forces select all
                editView.clearFocus();
                editView.requestFocus();
            }

            // Do it only once
            select = false;
        }

        if (flagView != null)
            flagView.setImageResource(CURRENCY_FLAGS[currentIndex]);
        if (nameView != null)
            nameView.setText(CURRENCY_NAMES[currentIndex]);
        if (symbolView != null)
            symbolView.setText(CURRENCY_SYMBOLS[currentIndex]);
        if (longNameView != null)
            longNameView.setText(CURRENCY_LONGNAMES[currentIndex]);

        // Remove the selected currency from the lists
        flagList.remove(position);
        nameList.remove(position);
        symbolList.remove(position);
        valueList.remove(position);
        longNameList.remove(position);

        // Add the old current currency to the end of the list
        flagList.add(CURRENCY_FLAGS[oldIndex]);
        nameList.add(CURRENCY_NAMES[oldIndex]);
        symbolList.add(CURRENCY_SYMBOLS[oldIndex]);
        longNameList.add(CURRENCY_LONGNAMES[oldIndex]);

        numberFormat.setGroupingUsed(true);
        value = numberFormat.format(oldValue);
        valueList.add(value);
//
//        // Get preferences
//        SharedPreferences preferences =
//                PreferenceManager.getDefaultSharedPreferences(this);
//
//        // Get editor
//        SharedPreferences.Editor editor = preferences.edit();
//
//        // Get entries
//        JSONArray nameArray = new JSONArray(nameList);
//        JSONArray valueArray = new JSONArray(valueList);
//
//        // Update preferences
//        editor.putString(PREF_NAMES, nameArray.toString());
//        editor.putString(PREF_VALUES, valueArray.toString());
//        editor.putInt(PREF_INDEX, currentIndex);
//        numberFormat.setGroupingUsed(false);
//        value = numberFormat.format(currentValue);
//        editor.putString(PREF_VALUE, value);
//        editor.apply();

        // Notify the adapter
        adapter.notifyDataSetChanged();

    }
}
