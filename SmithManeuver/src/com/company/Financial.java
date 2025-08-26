package com.company;

import static com.company.Constants.*;

public class Financial { // round to 4 decimal places

    //fields
    private double homeValue;
    private double homeEquity;
    private double mortgageValue;
    private double monthlyPayments;
    private double mortgageInterestRate = INTEREST_RATE;
    private double yearlyPayment;
    private double yearlyPrincipal;
    private double yearlyInterest;
    private double heloc;
    private double helocInterest = 0;
    private double helocReturn;
    private double increase = 0;
    private int time = AMORTIZATION_PERIOD + 1;
    private int term;
    private int termCount = 0;
    private int year = 1;
    private String response;
    private double monthlyMortgageInterest;
    private double monthlyPrincipal;
    private double investments = 0;

    //methods
    public void setBaseValues(int value) {
        homeValue = value;
        homeEquity = homeValue * EQUITY_RATE;
        mortgageValue = homeValue - homeEquity;
        heloc = 0.05 * homeValue;
    }

    public void setTerm(int term) {
        this.term = (term * 12) + 1;
        termCount++;
        response = "";
        calcMonthlyPayment();
        System.out.println("\n----------------------------------Term: " + termCount + " -----------------------------" +
                "----\nHome Value: $" + (Math.round(homeValue * 100d) / 100d) + "\nCurrent Mortgage Value: $" + (Math.round(mortgageValue * 100d) / 100d)
                + "\nCurrent HELOC Balance: $" + (Math.round(heloc * 100d) / 100d) + response);
    }

    public void setMortgageInterestRate(double mortgageInterestRate) {
        this.mortgageInterestRate = mortgageInterestRate / 100;
    }

    public double getMortgageValue(){
        return mortgageValue;
    }

    public int getTime() {
        return (time / 12);
    }

    public void finance() { // to put together all the data into one string
        if (year <= 25) {
            response = "\n----------------------------------Year: " + year + " ---------------------------------\n";
        } else {
            response = BETWEEN;
        }
        if (time >= COMPOUNDING_PERIODS) { // end condition --> checks to make sure there is still time left
            calcPayments(); //calls on the payment calculation
            response += "\n" + calcHELOC(); //calls on the heloc calculation
            response += calcInvestments(); // calls on the investment calculation
            calcIncrease(); //calculates the increase of the home
            response += "\nCurrent Home Value: $" + (Math.round(homeValue * 100d) / 100d);
            year++; // increases the year by 1
            double placeholder = mortgageValue - yearlyPrincipal - investments - helocReturn;
                // calculates the new mortgage value
            heloc += mortgageValue - placeholder; // calculates the new HELOC value based on the change in mortgage value
            mortgageValue = placeholder; // sets the mortgage value as the new value
            if (mortgageValue < 0){ // checks if the user has paid off their mortgage
                response += "\nCurrent Mortgage Value: $" + 0 + "\nCurrent HELOC Value: $" + (Math.round(heloc * 100d) / 100d) + "\nAdditional Income: $" + Math.abs(mortgageValue) + "\n";
            } else {
                response += "\nCurrent Mortgage Value: $" + (Math.round(mortgageValue * 100d) / 100d) + "\nCurrent HELOC Value: $" + (Math.round(heloc * 100d) / 100d + "\n");
            }
            response += BETWEEN;
            System.out.println(response);
            if ((time > 1) && (term > 1)) { //if there is still time and there's another term
                if (mortgageValue > 0) {
                    reset();
                    finance(); // recursive call
                }
            }
        }
    }

    public void calcMonthlyPayment() { // calculates everything that needs to be calculated at the start of every term
        double exponent = Math.pow((1 + (mortgageInterestRate) / 12), time);
        monthlyPayments = ((mortgageValue * mortgageInterestRate) / COMPOUNDING_PERIODS) * exponent;
        monthlyPayments = monthlyPayments / (exponent - 1);
            // calculates monthly payments based on formula from wallstreetmojo
        yearlyPayment = monthlyPayments * COMPOUNDING_PERIODS; //calculates the yearly payment, by multiplying the monthly payment by 12
        response += "\nYearly Payment: $" + ((Math.round(yearlyPayment * 100d) / 100d)) + "\nMonthly Payment: $"
                + ((Math.round(monthlyPayments * 100d) / 100d));
    }

    public void calcPayments() { //calculates all the payments that change yearly
        time -= COMPOUNDING_PERIODS;
        term -= COMPOUNDING_PERIODS;

        //monthly interest
        monthlyMortgageInterest = mortgageValue * (mortgageInterestRate / COMPOUNDING_PERIODS);
            //calculates mortgage interest for the very first month of the year
        response += "\nMonthly Mortgage Interest: $" + ((Math.round(monthlyMortgageInterest * 100d) / 100d));

        yearlyInterest = calculatePayment(mortgageValue, INTEREST_RATE, time);
            //calls o recursive formula to calculate the yearly interest
        yearlyPrincipal = yearlyPayment - yearlyInterest;
            //calculates the yearly principal based on the payments and interest
        response += "\nYearly Interest: $" + (Math.round(yearlyInterest * 100d) / 100d) + "\nYearly Principal: $" + (Math.round(yearlyPrincipal * 100d) / 100d);

        //principal payment
        monthlyPrincipal = monthlyPayments - monthlyMortgageInterest;
            // calculates the monthly principal payments based on the payments and the interest
        response += "\nMonthly Principal Payment: $" + ((Math.round(monthlyPrincipal * 100d) / 100d));
    }


    public static double calculatePayment(double p, double i, int t) {
            // recursive formula --> calculates the yearly interest on the mortgage payments
        if (t == 1)
            return p * (1 + i);
        return p / ((p / calculatePayment(p, i, t - 1)) +
                (1 / Math.pow(1 + i, t)));
    }

    public String calcHELOC() { //calculates the total taxes paid on the HELOC
        String heloc = "";
        helocInterest = this.heloc * HElOC_RATE * 12;
            //not compounding because we assume that the user is paying off the interest fees as they go
        helocReturn = helocInterest * TAX_REFUND; //calculating the total tax return on the heloc after the user paid them
        heloc += "Heloc Return: $" + (Math.round(helocReturn * 100d) / 100d);
        return heloc;
    }

    public String calcInvestments() { // calculates the investment dividends for the heloc balance
        String invest = "";
        investments = (heloc * Math.pow(1 + HELOC_DIVIDEND, 4) - heloc); //compounding interest formula
        invest += "\nInvestment Dividends: $" + (Math.round(investments * 100d) / 100d);
        return invest;
    }

    public void setIncrease(double increase) { // takes in the rate of increase of the home
        this.increase = increase / 100;
    }

    public void calcIncrease() {
        homeValue = homeValue * (1 + increase); // calculates the new value of the home after a year of increase
        heloc = (homeValue * 0.8) - mortgageValue; // calculates the new heloc value based on the new home value
    }

    public void reset() { // resets only the fields that change between years / terms
        monthlyPrincipal = 0;
        monthlyMortgageInterest = 0;
        yearlyPrincipal = 0;
        yearlyInterest = 0;
        helocInterest = 0;
        helocReturn = 0;
    }

    public void fullReset() { // completely resets every single field
        homeValue = 0;
        homeEquity = 0;
        mortgageValue = 0;
        monthlyPayments = 0;
        yearlyPayment = 0;
        yearlyPrincipal = 0;
        yearlyInterest = 0;
        heloc = 0;
        helocInterest = 0;
        helocReturn = 0;
        time = AMORTIZATION_PERIOD + 1;
        term = 0;
        response = "";
        monthlyMortgageInterest = 0;
        monthlyPrincipal = 0;
        investments = 0;
        year = 1;
        increase = 0;
        mortgageInterestRate = INTEREST_RATE;
        termCount = 0;
    }


}
