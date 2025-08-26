package com.company;

public class Constants {

    public static final double EQUITY_RATE = 0.25;
    public static final double INTEREST_RATE = 0.054;
    public static final double HElOC_RATE = 0.0735 / 12;
    public static final double COMPOUNDING_PERIODS = 12;
    public static final double TAX_REFUND = 0.38;
    public static final double HELOC_DIVIDEND = 0.0225;
    public static final int AMORTIZATION_PERIOD = 300;

    public static final String BETWEEN = "\n-------------------------------------------------------------------\n";

    private Constants() {
        throw new AssertionError();
    }
}