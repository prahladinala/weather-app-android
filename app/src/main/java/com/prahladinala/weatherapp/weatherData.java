package com.prahladinala.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String mTemperature, mIcon, mCity, mWeatherType;
    private int mCondition;

    public static weatherData fromJson(JSONObject jsonObject)
    {
        try{
            weatherData weatherD = new weatherData();
            weatherD.mCity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.mIcon=updateWeatherIcon(weatherD.mCondition);
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue = (int)Math.rint(tempResult);
            weatherD.mTemperature = Integer.toString(roundedValue);
            return weatherD;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition)
    {
        if(condition>=0 && condition<=300)
        {
            return "thunderstrom1";
        }
        else if(condition>=300 && condition<=500)
        {
            return "lightrain";
        }
        else if(condition>=500 && condition<=600)
        {
            return "shower";
        }
        else  if(condition>=600 && condition<=700)
        {
            return "snow2";
        }
        else if(condition>=701 && condition<=771)
        {
            return "fog";
        }

        else if(condition>=772 && condition<=800)
        {
            return "overcast";
        }
        else if(condition==800)
        {
            return "sunny";
        }
        else if(condition>=801 && condition<=804)
        {
            return "cloudy";
        }
        else  if(condition>=900 && condition<=902)
        {
            return "thunderstrom1";
        }
        if(condition==903)
        {
            return "snow1";
        }
        if(condition==904)
        {
            return "sunny";
        }
        if(condition>=905 && condition<=1000)
        {
            return "thunderstrom2";
        }

        return "dunno";
    }

    // GETTERS
    public String getmTemperature(){
        return mTemperature + "°C";
    }

    public String getmIcon(){
        return mIcon;
    }

    public String getmCity(){
        return mCity;
    }

    public String getmWeatherType(){
        return mWeatherType;
    }
}

//          {
//        "coord": {
//        "lon": -122.08,
//        "lat": 37.39
//        },
//        "weather": [
//        {
//        "id": 800,
//        "main": "Clear",
//        "description": "clear sky",
//        "icon": "01d"
//        }
//        ],
//        "base": "stations",
//        "main": {
//        "temp": 282.55,
//        "feels_like": 281.86,
//        "temp_min": 280.37,
//        "temp_max": 284.26,
//        "pressure": 1023,
//        "humidity": 100
//        },
//        "visibility": 16093,
//        "wind": {
//        "speed": 1.5,
//        "deg": 350
//        },
//        "clouds": {
//        "all": 1
//        },
//        "dt": 1560350645,
//        "sys": {
//        "type": 1,
//        "id": 5122,
//        "message": 0.0139,
//        "country": "US",
//        "sunrise": 1560343627,
//        "sunset": 1560396563
//        },
//        "timezone": -25200,
//        "id": 420006353,
//        "name": "Mountain View",
//        "cod": 200
//        }