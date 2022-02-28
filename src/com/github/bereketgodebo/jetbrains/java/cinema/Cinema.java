package com.github.bereketgodebo.jetbrains.java.cinema;

import java.util.Scanner;

public class Cinema {

    static Scanner scanner = new Scanner(System.in);
    static String[][] seatArrangement;
    static int numberOfrows;
    static int numberOfseats;
    static final String RESERVED_SEAT_SYMBOL = "B ";
    static final String FREE_SEAT_SYMBOL = "S ";
    static int maxSeatAvailable;

    /**
     * creates seat arrangement base on
     * the number of rows and seats
     */
    public static void createSeatsArragement() {
        seatArrangement = new String[numberOfrows + 1][numberOfseats + 1];

        for (int r = 0; r <= numberOfrows; r++) {
            for (int s = 0; s <= numberOfseats; s++) {
                if (r == 0) {
                    if (s == 0) {
                        seatArrangement[r][s] = "  ";
                    } else {
                        seatArrangement[r][s] = s + " ";
                    }
                } else if (s == 0){
                    seatArrangement[r][s] = r + " "; // Print Row numbers in the beginning
                } else {
                    seatArrangement[r][s] = FREE_SEAT_SYMBOL;                
                }

            }
        }

    }
    /**
     * 
     * @param request
     * @return seat row or column
     */
    private static int getSeatDetails(String request) {
        System.out.println(request);
        return scanner.nextInt();
    }

    /**
     * shows the seat details of the theater
     */
    private static void showSeats() {
        //
        System.out.println("\nCinema:");
        for (int i = 0; i < numberOfrows + 1; i++) {
            for (int k = 0; k < numberOfseats + 1; k++) {
                System.out.print(seatArrangement[i][k]);
            }
            System.out.println();
        }
    }

    /**
     * The main menu
     */
    public static void showMenu() {
        int choice = 1;

        // loop until exit is selected
        do {
            // show the menu
        System.out.println("\n1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
        // constants
        final int EXIT_CHOICE = 0;
        final int SHOW_SEAT_CHOICE = 1;
        final int BUY_TICKET_CHOICE = 2;
        final int STAT_CHOICE = 3;
        // read choices
        choice = scanner.nextInt();

        switch (choice) {
            case SHOW_SEAT_CHOICE:
                showSeats();
                break;
            case BUY_TICKET_CHOICE:
                buyTicket();
                break;
            case STAT_CHOICE:
                showStat();
                break;
            case EXIT_CHOICE: // nothing to do
                break;
            default:
                System.out.println(" Not valid input: try again!");
                break;
        }

        } while (choice != 0);

    }

    private static void buyTicket() {
        int selectedRow; // row number
        int selectedSeat; // seat number

        do {
            selectedRow = getSeatDetails("\nEnter a row number:");
            selectedSeat = getSeatDetails("Enter a seat number in that row:");

            if (!isSelectionWithinBound(selectedRow, selectedSeat)) {
                System.out.println("\nWrong input!");
            } else if (isSeatReserved(seatArrangement[selectedRow][selectedSeat])) {
                System.out.println("\nThat ticket has already been purchased!");
            }

        } while (!isSelectionWithinBound(selectedRow, selectedSeat) || 
                isSelectionWithinBound(selectedRow, selectedSeat) &&
                isSeatReserved(seatArrangement[selectedRow][selectedSeat]));
        
        confirmReservation(selectedRow, selectedSeat);
    }

    /**
     * 
     */
    private static void showStat() {
        double boughtTicketPercentage = 
                ((double)getNumResSeats() / maxSeatAvailable) * 100;
        System.out.printf("\nNumber of purchased tickets: %d%n", 
                            getNumResSeats());
        System.out.printf("Percentage: %.2f%%%n", boughtTicketPercentage);
        System.out.printf("Current income: $%d%n", getCurrentIncome());
        System.out.printf("Total income: $%d%n", getTotalIncome());

    }

    /**
     * 
     * @return
     */
    private static long getTotalIncome() {
        long sumOfAllSeatPrice = 0;
        for (int i = 1; i < numberOfrows + 1; i++) {
            for (int k = 1; k < numberOfseats + 1; k++) {
                sumOfAllSeatPrice += calculatePrice(i);
            }
        }
        return sumOfAllSeatPrice;
    }

    /**
     * 
     * @return
     */
    private static long getCurrentIncome() {
        long sumOfResSeatPrice = 0;
        for (int i = 1; i < numberOfrows + 1; i++) {
            for (int k = 1; k < numberOfseats + 1; k++) {
                if (RESERVED_SEAT_SYMBOL.equals(seatArrangement[i][k])) {
                    sumOfResSeatPrice += calculatePrice(i);
                }
            }
        }
        return sumOfResSeatPrice;
    }

    /**
     * 
     * @param seat
     * @return
     */
    private static boolean isSeatReserved(String seat) {
        return RESERVED_SEAT_SYMBOL.equals(seat);
    }

    /**
     * 
     * @return
     */
    private static int getNumResSeats() {
        int countResSeats = 0;
        for (int i = 1; i < numberOfrows + 1; i++) {
            for (int k = 1; k < numberOfseats + 1; k++) {
                if (isSeatReserved(seatArrangement[i][k])) {
                    countResSeats++;
                }
            }
        }
        return countResSeats;
    }

    /**
     * 
     * @param selectedRow
     * @return
     */
    private static int calculatePrice(int selectedRow) {
        final int PRICE_CHANGE_THRESHOLD = 60;
        // ticket prices
        final int TICKET_FIRST_ROW_PRICE = 10;
        final int TICKET_SEC_ROW_PRICE = 8;
        int ticketPrice = TICKET_FIRST_ROW_PRICE; // default price

        if (maxSeatAvailable > PRICE_CHANGE_THRESHOLD &&
            selectedRow > (numberOfrows / 2)) {
            ticketPrice = TICKET_SEC_ROW_PRICE;
        }

        return ticketPrice;
    }

    private static boolean isSelectionWithinBound(int selectedRow, int selectedSeat) {
        boolean isRowCorrect = selectedRow >= 1 && selectedRow <= numberOfrows;
        boolean isSeatNumCorrect = selectedSeat >= 1 && selectedSeat <= numberOfseats;

        return isRowCorrect && isSeatNumCorrect;
    }

    private static void setSeatReserved(int selectedRow, int selectedSeat) {
        seatArrangement[selectedRow][selectedSeat] = RESERVED_SEAT_SYMBOL;
    }

    
    /**
     * confirm reservation
     */
    private static void confirmReservation(int selectedRow, int selectedSeat) {    
        // update seat arrangement
        setSeatReserved(selectedRow, selectedSeat);
    
        // print selection price
        System.out.println("\nTicket price: $" + calculatePrice(selectedRow));

    }

    public static void main(String[] args) {
        // read the rows and seats
        numberOfrows = getSeatDetails("\nEnter the number of rows:");
        numberOfseats = getSeatDetails("Enter the number of seats in each row:");
        maxSeatAvailable = numberOfrows * numberOfseats;
        // initialize the seat arrangement
        createSeatsArragement();
        // show the main menu
        showMenu();

    }

}

