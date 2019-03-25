package com.example.currencyconvertor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

    private ImageView flagView;
    private TextView nameView;
    private TextView symbolView;
    private EditText editView;
    private TextView longNameView;
    private ListView listView;
    private CurrencyAdapter adapter;

    private List<Integer> flagList;
    private List<String> nameList;
    private List<String> symbolList;
    private List<String> valueList;
    private List<Integer> longNameList;

    private List<Integer> selectList;


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




        createInitList();

        adapter = new CurrencyAdapter(this, R.layout.item, flagList, nameList,
                symbolList, valueList, longNameList,
                selectList);

        listView.setAdapter(adapter);
    }

    public void createInitList(){
        flagList.add(CURRENCY_FLAGS[1]);
        nameList.add(CURRENCY_NAMES[1]);
        symbolList.add(CURRENCY_SYMBOLS[1]);
        valueList.add("1");
        longNameList.add(CURRENCY_LONGNAMES[1]);

        flagList.add(CURRENCY_FLAGS[4]);
        nameList.add(CURRENCY_NAMES[4]);
        symbolList.add(CURRENCY_SYMBOLS[4]);
        valueList.add("1");
        longNameList.add(CURRENCY_LONGNAMES[4]);


        flagList.add(CURRENCY_FLAGS[6]);
        nameList.add(CURRENCY_NAMES[6]);
        symbolList.add(CURRENCY_SYMBOLS[6]);
        valueList.add("1");
        longNameList.add(CURRENCY_LONGNAMES[6]);


    }

}
