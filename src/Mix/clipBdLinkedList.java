package Mix;

/**********************************************************************
 * A class contains a linked list of clipboards and allows the user
 * to get and add clipboards
 *
 * @author Logan Jaglowski
 * @version Winter 2019
 *********************************************************************/

public class clipBdLinkedList {

    /** the top of the node */
    private NodeCB top;

    /** the tail of the node */
    private NodeCB tail;

    /******************************************************************
     * A constructor that creates the linked list and sets both the
     * top and tail to null.
     *****************************************************************/
    public clipBdLinkedList() {
        tail = top = null;
    }

    /******************************************************************
     * A method that adds a clipboard to the linked list
     *
     * @param clipNum the chosen number for the clipboard
     * @param data the data to be added to the clipboard
     *****************************************************************/
    public void addClipboard (int clipNum, DoubleLinkedList data) {

        // case 0: top is null
        if (top == null) {
            top = new NodeCB();
            top.setClipBoardNumber(clipNum);
            top.setNext(tail);
            for (int i = data.size(); i > 0; i--) {
                top.setTopOfClipBoard(data.getNode(i - 1));
            }

        // case 1: top is not null and a new clipboard is created
        } else {
            NodeCB temp = new NodeCB();
            temp.setClipBoardNumber(clipNum);
            temp.setNext(top);
            top = temp;
            for (int i = data.size(); i > 0; i--) {
                temp.setTopOfClipBoard(data.getNode(i - 1));
            }
        }
    }

    /******************************************************************
     * A method that gets a certain clipboard based off of its number
     *
     * @param clipNum the number of the designated clipboard
     * @returns a double linked list with the data of that clipboard
     *****************************************************************/
    public DoubleLinkedList getClipBoard(int clipNum) {
        NodeCB tempClip = new NodeCB();
        tempClip = top;
        DoubleLinkedList correctList = new DoubleLinkedList();
        int count = 0;

        // cycles through until it can find the clipboard number
        while (clipNum != tempClip.getClipBoardNumber() &&
                tempClip.getNext() != null) {
            tempClip = tempClip.getNext();
        }

        // checks if this clipboard has the number.
        if (tempClip.getClipBoardNumber() != clipNum) {
            System.out.println("Clipboard does not exist");
            return null;
        }

        NodeD tempTop = tempClip.getTopOfClipBoard();

        // gathers the data back to the linked list
        while (tempTop != null) {
            correctList.addTo(tempTop.getData(), count);
            tempTop = tempTop.getNext();
            count++;
        }

        return correctList;
    }
}
