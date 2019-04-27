package Mix;

/**********************************************************************
 * A class used for creating, getting, and editing values within a
 * double linked list.
 *
 * @author Logan Jaglowski
 * @version Winter 2019
 *********************************************************************/
public class DoubleLinkedList<E>  {

    /** The first NodeD<E> in the list */
	protected NodeD<E> top;

    /** The current NodeD<E> in the list */
	protected NodeD<E> cursor;

    /******************************************************************
     * A constructor that creates the linked list and sets both the
     * top and cursor to null.
     *****************************************************************/
	public DoubleLinkedList() {
		top = null;
		cursor = null;
	}

    /******************************************************************
     * A method that adds a Node first to the linked list
     *
     * @param data the data of the new node.
     *****************************************************************/
    public void addfirst(E data) {
        if (top == null) {
            top = new NodeD<E>(data, null, null);
        }
        else {
            cursor = top;
            top = new NodeD<E>(data, cursor, null);
            cursor.setPrev(top);
        }
    }

    /******************************************************************
     * A method that adds a Node to the bottom of the linked list
     *
     * @param data the data of the new node.
     *****************************************************************/
    public void addToBottom(E data) {

        // Case 0: the list does not exist
        if (top == null)
            top = new NodeD<E>(data, null, null);

            // case 1: the list exists and top.getNext is null
        else if (top.getNext() == null){
            cursor = new NodeD<E>(data, null, top);
            top.setNext(cursor);
        } else {

            // case 2: the list exists and top.getNext is not null
            cursor = top;
            while (cursor.getNext() != null)
                cursor = cursor.getNext();
            NodeD<E> temp = new NodeD<E>(data, null, cursor);
            cursor.setNext(temp);
        }
    }

    /******************************************************************
     * A method that removes the first Node on the linked list
     *
     * @returns the data of the deleted node.
     *****************************************************************/
    public E removeFromTop() {
        // no list
        if (top == null)
            throw new RuntimeException();

        E data = top.getData();

        top = top.getNext();
        return data;
    }

    /******************************************************************
     * A method that removes the last Node on the linked list
     *
     * @returns the data of the deleted node.
     *****************************************************************/
    public E removeFromBottom() {
        // case 0: no list
        if (top == null)
            return null;

        // case 1: one item in the list
        if (top.getNext() == null) {
            E data = top.getData();
            top = null;
            return data;
        }

        // case 2: multi-item list
        NodeD<E> temp = top;
        while (temp.getNext().getNext() != null)
            temp = temp.getNext();


        E data = ((NodeD<E>) temp.getNext()).getData();
        temp.setNext(null);
        return data;
    }

    /******************************************************************
     * A method that removes a specific Node on the linked list
     *
     * @param index the index of the Node to be deleted
     * @returns the data of the deleted node.
     *****************************************************************/
    public E removeAt(int index) {
        // case 0: no list
        if (top == null)
            return null;

        // case 1: index out of bounds
        if (index < 0 || index > size() - 1)
            return null;

        // case 2: remove from top, index = 0;
        if (index == 0)
            return removeFromTop();

        // case 3: remove from bottom;
        if (top.getNext().getNext() == null)
            return removeFromBottom();

        NodeD<E> temp = top;
        for (int i = 0; i < index - 1; i++)
            temp = temp.getNext();

        E data = (E)temp.getNext().getData();
        if (temp.getNext().getNext() == null) {
            temp.setNext(null);
            return data;
        }
        temp.getNext().getNext().setPrev(temp);
        temp.setNext(temp.getNext().getNext());
        return data;
    }

    /******************************************************************
     * A method that adds a Node to the linked list in a specific spot
     *
     * @param index the position of the new Node.
     * @returns the data of the deleted node.
     *****************************************************************/
    public void addTo(E data, int index) {

        // case 1: index out of bounds
        if (index < 0 || index > size()) {
            return;
        }

        // case 2: add to top, index = 0;
        if (index == 0) {
            addfirst(data);
            return;
        }

        // case 3: add to bottom;
        if (index == size()) {
            addToBottom(data);
            return;
        }

        cursor = top;
        for (int i = 0; i < index; i++)
            cursor = cursor.getNext();
        NodeD temp = new NodeD<E>(data, cursor, cursor.getPrev());
        cursor.getPrev().setNext(temp);
        cursor.setPrev(temp);
    }

    /******************************************************************
     * A method that finds the size of the double linked list
     *
     * @returns the size of the double linked list
     *****************************************************************/
    public int size() {
        int count = 0;
        NodeD<E> temp = top;
        while (temp != null) {
            count++;
            temp = temp.getNext();
        }
        return count;
    }

    /******************************************************************
     * A method that returns a specific Node in the linked list
     *
     * @returns the requested node in the linked list
     *****************************************************************/
    public NodeD getNode (int position) {
        cursor = top;
        for (int i = 0; i < position; i++)
            cursor = cursor.getNext();
        return cursor;

    }

    /******************************************************************
     * A method that gets the specific data of a certain node
     *
     * @returns the data of the requested node.
     *****************************************************************/
	public E get(int position) {
		cursor = top;
		for (int i = 0; i < position; i++)
			cursor = cursor.getNext();
		return cursor.getData();

	}

    /******************************************************************
     * A method that creates the linked list to a readable string
     *
     * @returns the string version of the linked list
     *****************************************************************/
	public String toString() {
		String retVal = "";
		NodeD<E> cur = top;
		while (cur != null) {
			retVal += cur.getData();
			cur = cur.getNext();
		}
		return retVal;
	}
}
