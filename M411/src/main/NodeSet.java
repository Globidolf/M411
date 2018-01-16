package main;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class NodeSet<E> implements Set<E>{

	public NodeSet(Node<E> head){
		this.head = head;
	}
	
	private Node<E> head;
	
	@Override
	public boolean add(E e) {
		if (head == null) head = new Node<E>(e);
		else{
			if (head.getValue().equals(e)) return false;
			Node<E> cursor = head;
			while(cursor.hasNext()){
				cursor = cursor.getNext();
				if (cursor.getValue().equals(e)) return false;
			}
			cursor.setNext(new Node<E>(e));
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = false;
		for(E e : c) if (add(e)) result = true;
		return result;
	}

	@Override
	public void clear() { head = null; }

	@Override
	public boolean contains(Object o) {
		Node<E> cursor = head;
		while(cursor != null){
			if (cursor.getValue().equals(o)) return true;
			cursor = cursor.getNext();
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object e : c) if (!contains(e)) return false;
		return true;
	}

	@Override
	public boolean isEmpty() {
		return head == null;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private Node<E> cursor = head;

			@Override
			public boolean hasNext() {
				return cursor != null;
			}

			@Override
			public E next() {
				E result = cursor.getValue();
				cursor = cursor.getNext();
				return result;
			}
		};
	}

	@Override
	public boolean remove(Object o) {
		if (isEmpty()) return false;
		if (head.getValue().equals(o)) {
			head = head.getNext();
			return true;
		}
		Node<E> cursor = head;
		while (cursor.hasNext()){
			if (cursor.getNext().getValue().equals(o)) {
				cursor.setNext(cursor.getNext().getNext());
				return true;
			}
			cursor = cursor.getNext();
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = false;
		for(Object o : c) if (remove(o)) result = true;
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean result = true;
		for(E e : this) if (!c.contains(e)) result = remove(e);
		return result;
	}

	@Override
	public int size() {
		if (isEmpty()) return 0;
		int count = 1;
		Node<E> cursor = head;
		while(cursor.hasNext()){
			count++;
			cursor = cursor.getNext();
		}
		return count;
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[size()];
		int i = 0;
		for(E e : this){
			result[i] = e;
			i++;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		T[] result;
		if (a.length >= size())
			result = a;
		else
			result = (T[]) ArrEx.Instantiate(size(), a[0].getClass());
		
		int i = 0;
		for(E e : this){
			result[i] = (T) e;
			i++;
		}
		for(i = size(); i < result.length; i++) a[i] = null;
		return result;
	}
	
}
