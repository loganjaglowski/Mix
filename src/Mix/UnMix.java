package Mix;
import java.io.*;
import java.util.Scanner;

/**********************************************************************
 * A class that unmixes a message after it was previously mixed.
 *
 * @author Logan Jaglowski
 * @version Winter 2019
 *********************************************************************/

public class UnMix {

    /** The message in the form of a double linked list */
	private DoubleLinkedList<Character> message;

    /******************************************************************
     * A constructor that instantiates the message to unmix.
     *****************************************************************/
	public UnMix() {
		message = new DoubleLinkedList<Character>();
	}

    /******************************************************************
     * The main method which makes the message and file equal to
     * the argument and is the start to the unmixing process.
     *
     * @param args the original file and message of the user
     *****************************************************************/
	public static void main(String[] args) {
	    UnMix v = new UnMix();
		v.unMixture(args[0], args[1]);
	}

    /******************************************************************
     * A method that unmixes the message based off of the undo commands
     *
     * @param command the current undo command
     * @returns the string version of the current message
     *****************************************************************/
	public String processCommand(String command) {
		Scanner scan = new Scanner(command);
		char charInput;

		try {
			command = scan.next();
			switch (command.charAt(0)) {

                // inserts a string to the message
                case 'b':
                    insertbefore(scan.next(), scan.nextInt());
                    break;

                // removes a portion of the message
                case 'r':
                    remove(scan.nextInt(), scan.nextInt());
                    break;

                // replaces a character with another character
                case 'a':
                    replace(scan.next(), scan.next());
                    break;
			}
		} catch (Exception e) {
			System.out.println("Error in command!  Problem!!!!" +
                    " in undo commands");
			System.exit(0);
		}
		finally {
			scan.close();
		}

		return message.toString();
	}

    /******************************************************************
     * A method that unmixes the message based off of the undo commands
     *
     * @param filename the filename which the undo commands are on
     * @param userMessage the string version of the current message
     *****************************************************************/
	private void unMixture(String filename, String userMessage) {
        message.addfirst(userMessage.charAt(0));
        for (int i = 1; i < userMessage.length(); i++)
            message.addTo(userMessage.charAt(i), i);
		String original = UnMixUsingFile (filename, userMessage);
		System.out.println ("The Original message was: \"" + original
         + "\"");
	}

    /******************************************************************
     * A method that finds the undo commends and sends them to be
     * processed
     *
     * @param filename the filename which the undo commands are on
     * @param userMessage the string version of the original message
     * @returns the string version of the final message
     *****************************************************************/
	public String UnMixUsingFile(String filename, String userMessage) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scanner.hasNext()) {
			String command = scanner.nextLine();
			userMessage = processCommand(command);
		} 
		return userMessage;
	}

    /******************************************************************
     * A method that removes a certain portion from the message.
     *
     * @param start the start index of what is to be removed
     * @param stop the stop index of what is to be removed
     *****************************************************************/
    private void remove(int start, int stop) {

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

        // removes portion from the message
        for (int i = 0; i <= stop - start; i++) {
            message.removeAt(start);
        }
    }

    /******************************************************************
     * A method that inserts a string into the message
     *
     * @param token the values that will be added to the message
     * @param index the start index of where the values will be
     *              inserted.
     *****************************************************************/
    private void insertbefore(String token, int index) {
        if (message.size() == 0)
            index = 0;

        // adds the token to the message
        for (int i = token.length() - 1; i >= 0; i--){
            char rChar = token.charAt(i);
            if (rChar == '~')
                rChar = ' ';
            message.addTo(rChar, index);
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
        // new character
        for (int i = 0; i < message.size(); i++)
            if (message.get(i).equals(start)) {
                message.getNode(i).setData(end);
            }
    }
}
