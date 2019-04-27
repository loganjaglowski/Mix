package Mix;

import java.io.*;
import java.util.Scanner;


/**********************************************************************
 * A class that mixes up a message, only to later be unmixed.
 *
 * @author Logan Jaglowski
 * @version Winter 2019
 *********************************************************************/

public class Mix {

    /** The message in the form of a double linked list */
    private DoubleLinkedList<Character> message;

    /** The commands to undo moves */
    private String undoCommands;

    /** The clipboard linked lists */
    private clipBdLinkedList clipBoards;

    /** The user's message in the form of a string */
    private String userMessage;

    /** The scanner to detect commands */
    private Scanner scan;

    /******************************************************************
     * A constructor that instantiates the scanner, messages,
     * clipboards, and undoCommands for the user to mix.
     *****************************************************************/
    public Mix() {
        scan = new Scanner(System.in);
        message = new DoubleLinkedList<Character>();
        clipBoards = new clipBdLinkedList();

        undoCommands = "";
    }

    /******************************************************************
     * The main method which makes the message equal to the argument
     * and is the start to the mixing process.
     *
     * @param args the original message of the user
     *****************************************************************/
    public static void main(String[] args) {
        Mix mix = new Mix();
        mix.userMessage = args[0];
        System.out.println (mix.userMessage);
        mix.mixture();
    }

    /******************************************************************
     * A method that displays the user's messages and takes commands
     * from the user to put onto the message.
     *****************************************************************/
    private void mixture() {

        // adds the original message to the display.
        message.addfirst(userMessage.charAt(0));
        for (int i = 1; i < userMessage.length(); i++)
            message.addTo(userMessage.charAt(i), i);

        // where the commands are taken.
        do {
            DisplayMessage();
            System.out.print("Command: ");

            // save state
            DoubleLinkedList <Character> currMessage =
                    new DoubleLinkedList<Character>();
            String currUndoCommands = undoCommands;
            currMessage = message;

            try {
                String command = scan.next("[Qbrpcxhdaz]");

                switch (command) {

                    // quits the program and saves the state
                    case "Q":
                        save(scan.next());
                        System.out.println ("Final mixed up message:" +
                                " \"" + message+"\"");
                        System.exit(0);
                        break;

                    // inserts a string to the message
                    case "b":
                        insertbefore(scan.next(), scan.nextInt());
                        break;

                    // removes a portion of the message
                    case "r":
                        remove(scan.nextInt(), scan.nextInt());
                        break;

                    // copies a certain portion to a clipboard
                    case "c":
                        copy(scan.nextInt(), scan.nextInt(),
                                scan.nextInt());
                        break;

                    // cuts a certain portion to a clipboard
                    case "x":
                        cut(scan.nextInt(), scan.nextInt(),
                                scan.nextInt());
                        break;

                    // pastes from the clipboard to the message
                    case "p":
                        paste(scan.nextInt(), scan.nextInt());
                        break;

                    // displays the helpPage
                    case "h":
                        helpPage();
                        break;

                    // deletes a certain character from the message
                    case "d":
                        deleteChar(scan.next());
                        break;

                    // replaces a character with another character
                    case "a":
                        replace(scan.next(), scan.next());
                        break;

                    // randomly chooses functions for the program to
                    // perform
                    case "z":
                        random();
                        break;
                }
                scan.nextLine();   // should flush the buffer
                System.out.println("For demonstration purposes only:\n"
                        + undoCommands);
            }
            catch (Exception e ) {
                System.out.println ("Error on input, previous state" +
                        " restored.");
                scan = new Scanner(System.in);  // should completely
                // flush the buffer

                // restore state;
                undoCommands = currUndoCommands;
                message = currMessage ;
            }

        } while (true);
    }

    /******************************************************************
     * A method that deletes a certain character from the entirety of
     * the message.
     *
     * @param character the character that will be deleted from the
     *                  message
     *****************************************************************/
    private void deleteChar(String character) {

        // checking if it's valid
        if (character.length() != 1) {
            System.out.print("Error, invalid character length");
            return;
        }
        if (character == null) {
            System.out.print("Error, cannot be null");
            return;
        }


        char deleteChar = character.charAt(0);

        // establishing whether or not the message character is equal
        // to what should be deleted.
        for (int i = 0; i < message.size(); i++)
            if (message.get(i).equals(deleteChar)) {
                message.removeAt(i);
                i--;

                //creates the undo commands
                if (deleteChar == ' ') {
                    undoCommands = "b ~ " + (i + 1) + "\n" +
                            undoCommands;
                } else {
                    undoCommands = "b " + deleteChar + " " + (i + 1)
                            + "\n" + undoCommands;
                }
            }
    }

    /******************************************************************
     * A method that removes a certain portion from the message.
     *
     * @param start the start index of what is to be removed
     * @param stop the stop index of what is to be removed
     *****************************************************************/
    private void remove(int start, int stop) {
        char removedChar;

        //checking to see if it's valid
        if (start < 0 || start > message.size()) {
            System.out.println("Error, invalid start");
            return;
        }
        if (stop < 0 || stop > message.size()) {
            System.out.println("Error invalid stop");
            return;
        }
        if (stop < start) {
            System.out.println("Error, stop is larger than stop");
            return;
        }

        // removes portion from the message and creates undo commands
        for (int i = 0; i <= stop - start; i++) {
            removedChar = message.removeAt(start);
            if (removedChar == ' ')
                undoCommands = "b ~ " + start + "\n" + undoCommands;
            else
                undoCommands = "b " + removedChar + " " + start + "\n"
                        + undoCommands;
        }
    }

    /******************************************************************
     * A method that cuts a certain portion to a clipboard
     *
     * @param start the start index of what is to be cut
     * @param stop the stop index of what is to be cut
     * @param clipNum the clipboard number that the cut portion will
     *                be moved to.
     *****************************************************************/
    private void cut(int start, int stop, int clipNum) {
        copy(start, stop, clipNum);
        remove(start, stop);
    }

    /******************************************************************
     * A method that randomly selects functions and values to mix up
     * the message.
     *****************************************************************/
    private void random() {

        // creates the random values to be used later in the method.
        int randomNumTimes = (int)(Math.random() * 5 + 5);
        int randomMethod;
        int randomInt1;
        int randomChar;
        int randomChar2;
        String randomString2;
        String randomString;
        String alphabet = "abcdefghijklmnopqrstuvwxyz";

        // method that repeats itself a random number of times
        for (int i = 0; i < randomNumTimes; i++) {

            // randomizes the values each time the loop is completed
            randomMethod = (int)(Math.random() * 4 + 1);
            randomInt1 = (int)(Math.random() * 5);
            randomChar = (int)(Math.random() * 26);
            randomChar2 = (int)(Math.random() * 26);
            randomString = "" + alphabet.charAt(randomChar);
            randomString2 = "" + alphabet.charAt(randomChar2);

            // methods are decided based off of a random number and
            // the parameters are also filled with random inputs that
            // fit the requirements.
            if (randomMethod == 1) {
                insertbefore(randomString, randomInt1);
            }
            if (randomMethod == 2) {
                remove(randomInt1, randomInt1);
            }
            if (randomMethod == 3) {
                replace(randomString, randomString2);
            }
            if (randomMethod == 4) {
                deleteChar(randomString);
            }
        }
    }

    /******************************************************************
     * A method that replaces a certain character with another
     * character
     *
     * @param startChar the original character
     * @param endChar what the character will be changed to.
     *****************************************************************/
    private void replace (String startChar, String endChar) {

        // check to see if it's valid
        if (startChar.length() != 1) {
            System.out.print("Error, invalid character length");
            return;
        }
        if (startChar == null) {
            System.out.print("Error, cannot be null");
            return;
        }
        if (endChar.length() != 1) {
            System.out.print("Error, invalid character length");
            return;
        }
        if (endChar == null) {
            System.out.print("Error, cannot be null");
            return;
        }

        char start = startChar.charAt(0);
        char end = endChar.charAt(0);

        // replaces any time the start character appears with the
        // new character and adds undo commands.
        for (int i = 0; i < message.size(); i++)
            if (message.get(i).equals(start)) {
                message.getNode(i).setData(end);
            }
        undoCommands = "a " + endChar + " " + startChar + "\n" +
                undoCommands;
    }

    /******************************************************************
     * A method that copies a certain portion to a clipboard
     *
     * @param start the start index of what is to be copied
     * @param stop the stop index of what is to be copied
     * @param clipNum the clipboard number that the copied portion will
     *                be moved to.
     *****************************************************************/
    private void copy(int start, int stop, int clipNum) {
        DoubleLinkedList<Character> clipboard =
                new DoubleLinkedList<Character>();

        //checks to see if it's valid
        if (start - stop > 0){
            System.out.println("Error, start is larger than stop.");
            return;
        } else if (stop - start == 0) {
            // checks to see if it's only one character
            clipboard.addfirst(message.get(start));
        } else if (stop - start == 1) {
            // checks to see if it's two characters
            clipboard.addfirst(message.get(start));
            clipboard.addToBottom(message.get(stop));
        } else {
            // for three or more characters
            int count = 0;
            for (int i = start; i <= stop; i++) {
                clipboard.addTo(message.get(i), count);
                count++;
            }
        }
        clipBoards.addClipboard(clipNum, clipboard);
    }

    /******************************************************************
     * A method that pastes a certain portion to the message
     *
     * @param index the start index of where it's to be pasted
     * @param clipNum the clipboard number that the cut portion will
     *                be moved to.
     *****************************************************************/
    private void paste( int index, int clipNum) {
        insertbefore(clipBoards.getClipBoard(clipNum).toString(),
                index);
    }

    /******************************************************************
     * A method that inserts a string into the message
     *
     * @param token the values that will be added to the message
     * @param index the start index of where the values will be
     *              inserted.
     *****************************************************************/
    private void insertbefore(String token, int index) {
        if (index < 0 || index > message.size()) {
            return;
        }
        if (message.size() == 0)
            index = 0;

        // adds the token to the message and creates undo commands.
        for (int i = token.length() - 1; i >= 0; i--){
            char rChar = token.charAt(i);
            if (rChar == '~')
                rChar = ' ';
            message.addTo(rChar, index);
            undoCommands = "r " + index + " " + index + "\n" +
                    undoCommands;
        }
    }

    /******************************************************************
     * A method that displays the message to the user
     *****************************************************************/
    private void DisplayMessage() {
        System.out.print ("Message:\n");
        userMessage = message.toString();

        for (int i = 0; i < userMessage.length(); i++)
            System.out.format ("%3d", i);
        System.out.format ("\n");
        for (char c : userMessage.toCharArray())
            System.out.format("%3c",c);
        System.out.format ("\n");
    }

    /******************************************************************
     * A method that saves the values to a certain file
     *
     * @param filename the name of the file to which it will be saved
     *****************************************************************/
    public void save(String filename) {

        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter
                    (new FileWriter(filename)));

        } catch (IOException e) {
            e.printStackTrace();
        }

        out.println(undoCommands);
        out.close();
    }

    /******************************************************************
     * A method that displays the help page to the user.
     *****************************************************************/
    private void helpPage() {
        System.out.println("Commands:");
        System.out.println("\tQ filename	means, quit! "
                + " save to filename" );
        System.out.println("\t  ~ is used for a space character" );
        System.out.println("\t b s # means insert");
        System.out.println("\t r # * means remove ");
        System.out.println("\t d # means delete a character ");
        System.out.println("\t z means randomized functions ");
        System.out.println("\t a # * means replace ");
        System.out.println("\t c # * & means copy ");
        System.out.println("\t x # * & means cut ");
        System.out.println("\t p # * means paste ");
        System.out.println("\th\tmeans to show this help page");
    }
}
