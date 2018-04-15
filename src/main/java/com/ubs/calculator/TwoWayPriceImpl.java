package com.ubs.calculator;


public class TwoWayPriceImpl implements TwoWayPrice {

    private Instrument instrument;
    private State state;
    private double bidPrice;
    private double offerAmount;
    private double offerPrice;
    private double bidAmount;

    public TwoWayPriceImpl(Instrument instrument, State state, double offerAmount, double offerPrice, double bidAmount, double bidPrice) {
        this.instrument = instrument;
        this.state = state;
        this.bidPrice = bidPrice;
        this.offerAmount = offerAmount;
        this.offerPrice = offerPrice;
        this.bidAmount = bidAmount;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public State getState() {
        return state;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public double getOfferAmount() {
        return offerAmount;
    }

    public double getOfferPrice() {
        return offerPrice;
    }

    public double getBidAmount() {
        return bidAmount;
    }
}
