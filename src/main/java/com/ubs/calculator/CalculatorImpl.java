package com.ubs.calculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CalculatorImpl implements Calculator {

    private  static final double ZERO_CONST = 0.0;
    // one instrument can be in different markets with different prices
    private Map<Instrument, Map<Market, TwoWayPrice>> instrumentMap = new HashMap<>();

    public CalculatorImpl(){
        // populating the map with the instrument values
        Arrays.stream(Instrument.values())
                .forEach(instr -> instrumentMap.put(instr, new HashMap<>()));
    }

    @Override
    public TwoWayPrice applyMarketUpdate(MarketUpdate twoWayMarketUpdate) {
        validateMarketUpdate(twoWayMarketUpdate);

        TwoWayPrice twoWayPriceUpdate = twoWayMarketUpdate.getTwoWayPrice();
        Instrument instrument = twoWayPriceUpdate.getInstrument();

        // getting the market for the instrument
        Map<Market, TwoWayPrice> marketMapBasedOnInstrument = instrumentMap.get(instrument);
        // always updating the market with the price of the instrument
        marketMapBasedOnInstrument.put(twoWayMarketUpdate.getMarket(), twoWayPriceUpdate);

        double marketBitPriceSum =  ZERO_CONST;
        double marketBitAmountSum = ZERO_CONST;
        double marketOfferPriceSum = ZERO_CONST;
        double marketOfferAmountSum = ZERO_CONST;

        // Bid = Sum(Market Bid Price * Market Bid Amount)/ Sum(Market Bid Amount)
        // Offer = Sum(Market Offer Price * Market Offer Amount)/ Sum(Market Offer Amount)
        for (TwoWayPrice market : marketMapBasedOnInstrument.values()){

            // I have noticed that there is a Side enum that is not used anywhere. I am expecting to have
            // here and condition depending on that Side we are (bid or offer) on based on that which one to increase
            marketBitPriceSum = marketBitPriceSum + (market.getBidPrice() * market.getBidAmount());
            marketBitAmountSum = marketBitAmountSum + market.getBidAmount();
            marketOfferPriceSum = marketOfferPriceSum + (market.getOfferPrice() * market.getOfferAmount());
            marketOfferAmountSum = marketOfferAmountSum + market.getOfferAmount();
        }

        double bid = marketBitPriceSum / marketBitAmountSum;
        double offer = marketOfferPriceSum / marketOfferAmountSum;

        return new TwoWayPriceImpl(
                twoWayPriceUpdate.getInstrument(),
                twoWayPriceUpdate.getState(),
                marketOfferAmountSum,
                offer,
                marketBitAmountSum,
                bid);
    }

    private void validateMarketUpdate(MarketUpdate twoWayMarketPrice) {
        TwoWayPrice twoWayPrice = twoWayMarketPrice.getTwoWayPrice();

        if (twoWayMarketPrice.getMarket() == null) {
            throw new IllegalArgumentException("The market value cannot be null");
        }

        if (twoWayPrice == null) {
            throw new IllegalArgumentException("The pricing for the market cannot be null");
        }

        if (twoWayPrice.getInstrument() == null) {
            throw new IllegalArgumentException("The instrument for the market cannot be null");
        }

        // don't know if we always have to have a bid
        if (twoWayPrice.getBidAmount() == ZERO_CONST || twoWayPrice.getBidPrice() == ZERO_CONST){
            throw new IllegalArgumentException("The bid amount/bid price needs to be grater than 0");
        }

        // don't know if we always have to have a offer
        if (twoWayPrice.getOfferAmount() == ZERO_CONST || twoWayPrice.getOfferPrice() == ZERO_CONST){
            throw new IllegalArgumentException("The offer amount/offer price needs to be grater than 0");
        }
    }
}
