package com.example.ngocthat.exchangerate;

/**
 * Created by My-PC on 3/16/2017.
 */

public class Exchange {
    private String Country;
    private Double Buy;
    private Double Sell;

    public String getCountry() {
        return Country;
    }

    public Double getBuy() {
        return this.Buy;
    }

    public Double getSell() {
        return this.Sell;
    }

    public void setCountry(String country) {
        this.Country = country;
    }

    public void setBuy(Double buy) {
        this.Buy = buy;
    }

    public void setSell(Double sell) {
        this.Sell = sell;
    }
}
