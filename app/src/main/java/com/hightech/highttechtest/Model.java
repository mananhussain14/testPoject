package com.hightech.highttechtest;

public class Model {
    String countryName, currencyValue;
    public Model(String countryName, String currencyValue){
        this.countryName = countryName;
        this.currencyValue = currencyValue;
    }


    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(String currencyValue) {
        this.currencyValue = currencyValue;
    }
}
