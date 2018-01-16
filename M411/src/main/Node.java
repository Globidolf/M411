package main;
/**
 * Generic Node with a next element and a value
 * @author Silvan Pfister
 *
 * @param <T> Type of the node value
 */
class Node<T> {
	private T value;
	private Node<T> next;

	/**
	 * There is no point in this method, as it is completely redundant.
	 * It is part of an exercise.
	 * @param before The target node
	 * @param after The inserted node
	 */
	public static <T2> void insertAfter(Node<T2> before, Node<T2> after){
		before.setNext(after);
	}
	
	/**
	 * Creates a node with a val
	 * 
	 * <br/>
	 * Same as {@linkplain Node#Node(Object, Node) new Node<>(value, null)}
	 * @param value The value
	 */
	public Node(T value){ this.value = value; }
	
	/**
	 * Creates a node with a value and a next node
	 * @param value The value
	 * @param next The next node
	 */
	public Node(T value, Node<T> next){
		this.value = value;
		this.next = next;
	}
	
	/**
	 * Gets the value of this node
	 * @return The value
	 */
	public T getValue(){ return value; }
	/**
	 * Sets the value of this node
	 * @param val The value
	 */
	public void setValue(T val) { value = val; }
	/**
	 * Checks if there is a next node
	 * @return False if next is null, true otherwise.
	 */
	public boolean hasNext() { return next != null; }
	/**
	 * Gets the next node
	 * @return The node
	 */
	public Node<T> getNext() { return next; }
	/**
	 * Sets the next node
	 * @param node The node
	 */
	public void setNext(Node<T> node) { next = node; }
}
