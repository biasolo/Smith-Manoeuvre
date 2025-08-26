package com.company;

import static com.company.Constants.*;

import java.util.Scanner;

public class Main {
    //terms are given, and interest rate changes every single term
    //take the term length input and
    //heloc balance is 5 percent of home value
    // mortgage + heloc = 80 percent of home value


    public static void main(String[] args) {

        Scanner uIn = new Scanner(System.in);
        Financial financial = new Financial();
        int value = 0;
        int term = 0;
        int termTotal = 0;
        double increase = 0;
        double interest = 0;
        boolean yn = false;
        boolean exit = false;
        boolean terms = false;
        do {

            System.out.println(BETWEEN + "This program -- Smith-Maneuver -- will recursively calculate the mortgage payments\n" +
                    "for a mortgage, when given the house value and assuming that the initial mortgage value is 25% of the home value.\n" +
                    "It will calculate the Home Equity Line of Credit Account, assuming that the mortgage and the home equity\n" +
                    "account cannot be larger than 80% of the home value. It will calculate the mortgage balance, \n" +
                    "HELOC balance, Home equity, Tax Refund" +
                    ", investment dividends, and the investment portfolio balance until the mortgage is less than 0.\n" +
                    "These calculations will run for a total of 25 years -- the amortization period\n" +
                    "The user will have to input their current home value, the rate of increase in their home, the term " +
                    "(or portion) or the mortgage, and the mortgage interest rate for each term.");
            System.out.println(BETWEEN);


            do {
                System.out.println("\nInput the current value of your home: ");
                try {
                    int placeholder = uIn.nextInt();
                    if (placeholder <= 0) {
                        System.out.println("Invalid Entry: Please enter a value above 0. Value " + placeholder + "is not valid.");
                    } else {
                        value = placeholder;
                        yn = true;
                    }
                } catch (Exception e) {
                    String garbage = uIn.next();
                    System.out.println("Error: Please input an integer value larger than 0. Value " + garbage + " is not valid.");
                }
            } while (!yn);
            financial.setBaseValues(value);

            yn = false;
            do {
                System.out.println("\nInput the rate of increase of the value of your home as a percentage (without the percent sign): ");
                try {
                    double placeholder = uIn.nextDouble();
                    if (placeholder < 0) {
                        System.out.println("Invalid Entry: Please enter a value equal to or above 0. Value " + placeholder + "is not valid.");
                    } else {
                        increase = placeholder;
                        yn = true;
                    }
                } catch (Exception e) {
                    String garbage = uIn.next();
                    System.out.println("Error: Please input an double value larger than 0. Value " + garbage + " is not valid.");
                }
            } while (!yn);

            financial.setIncrease(increase);

            do {
                yn = false;
                if(financial.getMortgageValue() > 0) {
                    do {
                        System.out.println("\nInput the length of the term in years (1-5) for the mortgage payment: ");
                        try {
                            int placeholder = uIn.nextInt();
                            if ((placeholder <= 0) || (placeholder > 5)) {
                                System.out.println("Invalid Entry: Please enter a value between 1 and 5. Value " + placeholder + " is not valid.");
                            } else if (placeholder > (AMORTIZATION_PERIOD / 12) - termTotal) {
                                System.out.println("Invalid Entry: Please enter a value that is smaller than or equal to " + ((AMORTIZATION_PERIOD / 12) - termTotal) + ".");
                            } else {
                                term = placeholder;
                                termTotal += term;
                                yn = true;
                            }
                        } catch (Exception e) {
                            String garbage = uIn.next();
                            System.out.println("Error: Please input an integer value larger than 0. Value " + garbage + " is not valid.");
                        }
                    } while (!yn);

                    yn = false;
                    do {
                        System.out.println("\nInput the interest rate of your mortgage as a percentage (without the percent sign): ");
                        try {
                            double placeholder = uIn.nextDouble();
                            if (placeholder < 0) {
                                System.out.println("Invalid Entry: Please enter a value equal to or above 0. Value " + placeholder + "is not valid.");
                            } else {
                                interest = placeholder;
                                yn = true;
                            }
                        } catch (Exception e) {
                            String garbage = uIn.next();
                            System.out.println("Error: Please input an double value larger than 0. Value " + garbage + " is not valid.");
                        }
                    } while (!yn);

                    financial.setMortgageInterestRate(interest);
                    financial.setTerm(term);

                    financial.finance();

                    if (financial.getTime() == 1) {
                        terms = true;
                    }
                } else {
                    System.out.println("\nCongratulations! You have payed off your mortgage!" + BETWEEN);
                    terms = true;
                }

            } while (!terms);

            yn = false;
            do {
                System.out.println(BETWEEN + "Would you like to rerun the program?");
                String answer = uIn.next();
                if (answer.contains("y")) {
                    financial.fullReset();
                    yn = true;
                } else if (answer.contains("n")) {
                    yn = true;
                    exit = true;
                } else {
                    System.out.println("Please input 'y' for yes or 'n' for no.");
                }
            } while (!yn);


        } while (!exit);

    }
}
