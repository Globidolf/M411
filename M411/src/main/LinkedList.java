package main;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Generic Linked List with basic functionality
 * @author Silvan Pfister
 *
 * @param <T> Type of the values in the linked list
 */
public class LinkedList<T> implements Iterable<T>, List<T> {
	private static final String errorOutOfBounds = "index must be between 0(inclusive) and count(exclusive)";
	private static final String errorDeleteNumber = "there must be at least 'number' items available starting from 'index'";
	private Node<T> head;
	private int count;

	
	/**
	 * Throws an Exception if the list is empty
	 * @throws Utility.ListEmptyException If the list is empty
	 */
	private void checkEmpty() throws ListEmptyException { if (count == 0) throw new ListEmptyException("list is empty"); }
	/**
	 * Throws an Exception if the index is out of bounds 
	 * @param index The index
	 * @throws IndexOutOfBoundsException If the index is out of bounds
	 */
	private void checkIndex(int index) throws IndexOutOfBoundsException{
		if (index < 0 || index >= count) throw new IndexOutOfBoundsException(errorOutOfBounds);
	}
	/**
	 * Throws a custom Exception if the index is out of bounds
	 * @param index
	 * @param errorMessage
	 * @throws IndexOutOfBoundsException
	 */
	private void checkIndex(int index, String errorMessage) throws IndexOutOfBoundsException{
		if (index < 0 || index >= count) throw new IndexOutOfBoundsException(errorMessage);
	}
	
	/**
	 * Creates a Linked List using the given items
	 * @param items The items
	 */
	@SafeVarargs
	public LinkedList(T... items){
		count = items.length;
		if (items != null && items.length > 0){ // if the items list is not empty, set the head to the first element
			head = new Node<T>(items[0]);
			if (items.length > 1){				// if there is more then one element, add the to the list
				Node<T> cursor = head;
				for(int i = 1; i < items.length; i++){
					cursor.setNext(new Node<T>(items[i]));
					cursor = cursor.getNext();
				}
			}
		}
	}
	
	private Node<T> getNode(int index) throws IndexOutOfBoundsException{
		checkIndex(index);
		Node<T> cursor = head;
		while (index > 0){
			index--;
			cursor = cursor.getNext();
		}
		return cursor;
	}
	
	public int getCount() { return count; }
	
	public T get(int index){ return getNode(index).getValue(); }
	
	public void removeAt(int index) throws ListEmptyException, IndexOutOfBoundsException { removeAt(index, 1); }
	
	public void removeAt(int index, int number) throws ListEmptyException, InvalidParameterException, IndexOutOfBoundsException{
		checkEmpty();
		checkIndex(index);
		checkIndex(index + number - 1, errorDeleteNumber);
		if (index == 0){
			if (number == count) head = null;
			else head = getNode(number);
		} else{
			if (index + number == count) getNode(index - 1).setNext(null);
			else getNode(index-1).setNext(getNode(index + number));
		}
		count -= number;
	}
	
	
	
	public void replaceAt(int index, T item) throws IndexOutOfBoundsException, ListEmptyException {
		checkEmpty();
		checkIndex(index);
		Node<T> next = new Node<T>(item);
		next.setNext(getNode(index).getNext());
		if (index == 0) head = next;
		else getNode(index - 1).setNext(next);
			
	}
	
	public void insert(T item, int index) throws IndexOutOfBoundsException{
		if (index == 0){
			head = new Node<T>(item, head);
		} else {
			Node<T> cursor = getNode(index-1);
			Node<T> next = new Node<T>(item, cursor != null ? cursor.getNext() : null);
			cursor.setNext(next);
		}
		count++;
	}
	
	public void append(T item){
		Node<T> next = new Node<T>(item);
		if (head == null) head = next;
		else{
			Node<T> cursor = head;
			while(cursor.hasNext()) cursor = cursor.getNext();
			cursor.setNext(next);
		}
		count++;
	}
	
	public void prepend(T item){ 
		head = new Node<T>(item, head); 
		count++;
	}

	@Override
	public String toString() {
		String result = count + "[";
		
		Node<T> cursor = head;
		
		while (cursor != null){
			result += cursor.getValue().toString();
			if (cursor.hasNext()) result += ",";
			cursor = cursor.getNext();
		}
		
		result += "]";
		
		return result;
	}
	
	public ArrayList<T> toList(){
		ArrayList<T> result = new ArrayList<>();
		
		for(T item : this) result.add(item);
		
		return result;
	}
	
	// implements Iterable<T> 
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			Node<T> cursor = head;
			
			@Override
			public boolean hasNext() { return cursor != null; }

			@Override
			public T next() {
				T result = cursor.getValue();
				cursor = cursor.getNext();
				return result;
			}
		};
	}
	public static class ListEmptyException extends Exception{ public ListEmptyException(String message){ super(message); } }
	
	@Override
	public boolean add(T element) {
		Node<T> next = new Node<T>(element);
		if (head == null) head = next;
		else{
			Node<T> cursor = head;
			while(cursor.hasNext()) cursor = cursor.getNext();
			cursor.setNext(next);
		}
		count++;
		return true;
	}
	@Override
	public void add(int index, T element) {
		if (index == 0){
			head = new Node<T>(element, head);
		} else {
			Node<T> cursor = getNode(index-1);
			Node<T> next = new Node<T>(element, cursor != null ? cursor.getNext() : null);
			cursor.setNext(next);
		}
		count++;
	}
	@Override
	public boolean addAll(Collection<? extends T> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public ListIterator<T> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ListIterator<T> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public T remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public T set(int index, T element) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <T2> T2[] toArray(T2[] a) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
