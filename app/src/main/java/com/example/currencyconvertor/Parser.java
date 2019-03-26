package com.example.currencyconvertor;


import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private Map<String, Double> map;

    public Parser(Map<String, Double> map) {
        this.map = map;
    }

    public Parser() {
    }

    public Map<String, Double> getMap() {
        return map;
    }

    public void setMap(Map<String, Double> map) {
        this.map = map;
    }

    public void createDummyValue(){
        map = new HashMap<>();
        map.put("EUR", 1.0);
        map.put("USD",1.1325);
        map.put("JPY",124.65);
        map.put("BGN",1.9558);
        map.put("CZK", 25.759);
        map.put("DKK",7.4646);
        map.put("GBP",0.85638);
        map.put("HUF",316.84);
        map.put("PLN",4.2953);
        map.put("RON",4.7553);
        map.put("SEK",10.4450);
        map.put("CHF",1.1237);
        map.put("ISK",136.30);
        map.put("NOK",9.6590);
        map.put("HRK",7.4170);
        map.put("RUB",72.7646);
        map.put("TRY",6.3425);
        map.put("AUD",1.5931);
        map.put("BRL",4.4032);
        map.put("CAD",1.5204);
        map.put("CNY",7.6015);
        map.put("HKD",8.8888);
        map.put("ILS",4.1041);
        map.put("INR",78.0460);
        map.put("KRW",1283.07);
        map.put("MXN",21.6039);
        map.put("MYR",4.6081);
        map.put("NZD",1.6413);
        map.put("PHP",59.411);
        map.put("SGD",1.5286);
        map.put("THB",35.804);
        map.put("ZAR",16.2997);
        map.put("IDR",16058.85);
    }

    public boolean startParser(String s)
    {
        // Create the map and add value for Euro
        map = new HashMap<>();
        map.put("EUR", 1.0);

        // Read the xml from the url
        try
        {
            URL url = new URL(s);
            InputStream stream = url.openStream();
            Handler handler = new Handler();
            Xml.parse(stream, Xml.Encoding.UTF_8, handler);
            return true;
        }
        catch (Exception e)
        {
            map.clear();
        }

        return false;
    }

    private class Handler extends DefaultHandler
    {
        // Start element
        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes)
        {
            String name = "EUR";
            double rate;

            if (localName.equals("Cube"))
            {
                for (int i = 0; i < attributes.getLength(); i++)
                {
                    // Get the date
                    switch (attributes.getLocalName(i))
                    {

                        // Get the currency name
                        case "currency":
                            name = attributes.getValue(i);
                            break;

                        // Get the currency rate
                        case "rate":
                            try
                            {
                                rate = Double.parseDouble(attributes.getValue(i));
                            }
                            catch (Exception e)
                            {
                                rate = 1.0;
                            }

                            // Add new currency to the map
                            map.put(name, rate);
                            break;
                    }
                }
            }
        }
    }


}
