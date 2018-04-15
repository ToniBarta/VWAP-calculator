package com.ubs.calculator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CalculatorImplTest {

    private Calculator calculator;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init(){
        calculator = new CalculatorImpl();
    }

    @Test
    public void testIfTheMarketIsEmpty() throws IllegalArgumentException{
        TwoWayPrice twoWayPrice = constructTwoWayPrice(Instrument.INSTRUMENT0, State.FIRM, 100, 20, 110, 10);
        MarketUpdate marketUpdate = new MarketUpdateImpl(null, twoWayPrice);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The market value cannot be null");
        calculator.applyMarketUpdate(marketUpdate);
    }

    @Test
    public void testIfThePricingForTheMarketIsEmpty() throws IllegalArgumentException {
        MarketUpdate marketUpdate = new MarketUpdateImpl(Market.MARKET0, null);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The pricing for the market cannot be null");
        calculator.applyMarketUpdate(marketUpdate);
    }

    @Test
    public void testIfTheInstrumentIsEmpty() throws IllegalArgumentException {
        TwoWayPrice twoWayPrice = constructTwoWayPrice(null, State.FIRM, 100, 20, 110, 10);
        MarketUpdate marketUpdate = new MarketUpdateImpl(Market.MARKET0, twoWayPrice);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The instrument for the market cannot be null");
        calculator.applyMarketUpdate(marketUpdate);
    }

    @Test
    public void testIfBidAmountOrBidPriceIsZero() throws IllegalArgumentException {
        TwoWayPrice twoWayPrice = constructTwoWayPrice(Instrument.INSTRUMENT0, State.FIRM, 100, 20, 0, 10);
        MarketUpdate marketUpdate = new MarketUpdateImpl(Market.MARKET0, twoWayPrice);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The bid amount/bid price needs to be grater than 0");
        calculator.applyMarketUpdate(marketUpdate);
    }

    @Test
    public void testIfOfferAmountOrBidPriceIsZero() throws IllegalArgumentException {
        TwoWayPrice twoWayPrice = constructTwoWayPrice(Instrument.INSTRUMENT0, State.FIRM, 100, 0, 100, 10);
        MarketUpdate marketUpdate = new MarketUpdateImpl(Market.MARKET0, twoWayPrice);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The offer amount/offer price needs to be grater than 0");
        calculator.applyMarketUpdate(marketUpdate);
    }

    @Test
    public void testOneSingleMarketUpdate() {
        TwoWayPrice twoWayPrice = constructTwoWayPrice(Instrument.INSTRUMENT0, State.FIRM, 100, 20, 110, 10);
        MarketUpdate marketUpdate = new MarketUpdateImpl(Market.MARKET0, twoWayPrice);

        TwoWayPrice marketTwoWayPrice = calculator.applyMarketUpdate(marketUpdate);

        Assert.assertEquals(Double.toString(marketTwoWayPrice.getOfferAmount()), "100.0");
        Assert.assertEquals(Double.toString(marketTwoWayPrice.getOfferPrice()), "20.0");
        Assert.assertEquals(Double.toString(marketTwoWayPrice.getBidAmount()), "110.0");
        Assert.assertEquals(Double.toString(marketTwoWayPrice.getBidPrice()), "10.0");
    }

    @Test
    public void testTwoMarketUpdatesForTheSameInstrument() {
        TwoWayPrice firstUpdate = constructTwoWayPrice(Instrument.INSTRUMENT0, State.FIRM, 100, 20, 110, 15);
        MarketUpdate firstMarketUpdate = new MarketUpdateImpl(Market.MARKET0, firstUpdate);
        calculator.applyMarketUpdate(firstMarketUpdate);

        TwoWayPrice secondUpdate = constructTwoWayPrice(Instrument.INSTRUMENT0, State.FIRM, 105, 20.5, 115, 10.5);
        MarketUpdate secondMarketUpdate = new MarketUpdateImpl(Market.MARKET2, secondUpdate);

        TwoWayPrice calculatorResult = calculator.applyMarketUpdate(secondMarketUpdate);

        Assert.assertEquals(Double.toString(calculatorResult.getOfferAmount()), "205.0");
        Assert.assertEquals(Double.toString(calculatorResult.getOfferPrice()), "20.25609756097561");
        Assert.assertEquals(Double.toString(calculatorResult.getBidAmount()), "225.0");
        Assert.assertEquals(Double.toString(calculatorResult.getBidPrice()), "12.7");
    }

    @Test
    public void testTwoMarketUpdatesForTheSameMarketAndInstrument() {
        TwoWayPrice firstUpdate = constructTwoWayPrice(Instrument.INSTRUMENT0, State.FIRM, 100, 20, 110, 15);
        MarketUpdate firstMarketUpdate = new MarketUpdateImpl(Market.MARKET0, firstUpdate);
        calculator.applyMarketUpdate(firstMarketUpdate);

        TwoWayPrice secondUpdate = constructTwoWayPrice(Instrument.INSTRUMENT0, State.FIRM, 105, 20.5, 115, 10.5);
        MarketUpdate secondMarketUpdate = new MarketUpdateImpl(Market.MARKET0, secondUpdate);

        TwoWayPrice calculatorResult = calculator.applyMarketUpdate(secondMarketUpdate);

        Assert.assertEquals(Double.toString(calculatorResult.getOfferAmount()), "105.0");
        Assert.assertEquals(Double.toString(calculatorResult.getOfferPrice()), "20.5");
        Assert.assertEquals(Double.toString(calculatorResult.getBidAmount()), "115.0");
        Assert.assertEquals(Double.toString(calculatorResult.getBidPrice()), "10.5");
    }

    private TwoWayPriceImpl constructTwoWayPrice(Instrument instrument,
                                                 State state, double offerAmount, double offerPrice, double bidAmount, double bidPrice) {
        return new TwoWayPriceImpl(instrument, state,  offerAmount, offerPrice, bidAmount, bidPrice);
    }
}
