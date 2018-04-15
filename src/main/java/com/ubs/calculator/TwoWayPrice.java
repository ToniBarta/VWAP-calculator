package com.ubs.calculator;

public interface TwoWayPrice {
    Instrument getInstrument();
    State getState();
    double getOfferAmount();
    double getOfferPrice();
    double getBidAmount();
    double getBidPrice();
}
