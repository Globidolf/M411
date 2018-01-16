package main;

/**
 * This class describes a generic Stack structure.
 * @author Silvan Pfister
 *
 * @param <T> The type of the elements in this stack 
 */
public class Stack<T> {
	
	private static final String errorStackEmpty = "The Stack is currently empty."; 
	
	private Node<T> head;
	
	/**
	 * Pushes an item on top of the stack
	 * @param item The item
	 */
	public void push(T item){ head = new Node<T>(item, head); }

	/**
	 * Pops the most recent item pushed on the stack
	 * @return The item or null if the stack is empty
	 */
	public T popSave() {
		try {
		if (head == null)
			throw new StackEmptyException(errorStackEmpty);
			T result = head.getValue();
			head = head.getNext();
			return result;
		} catch (StackEmptyException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Pops the most recent item pushed on the stack
	 * @return The item
	 * @throws StackEmptyException If the Stack is empty
	 */
	public T pop() throws StackEmptyException{
		if (head == null) throw new StackEmptyException(errorStackEmpty);
		T result = head.getValue();
		head = head.getNext();
		return result;
	}
	
	/**
	 * Peeks at the next item that will be popped without removing it from the stack
	 * @return The item
	 * @throws StackEmptyException If the Stack is empty
	 */
	public T peek() throws StackEmptyException {
		if (head==null) throw new StackEmptyException(errorStackEmpty);
		return head.getValue();
	}
	
	/**
	 * Checks if there are items in the Stack
	 * @return False if empty, true otherwise
	 */
	public boolean available(){ return head != null; } 
	
	public static class StackEmptyException extends Exception{ public StackEmptyException(String message){ super(message); } }
}
