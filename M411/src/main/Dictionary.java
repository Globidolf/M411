package main;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Dictionary<K, V> implements Map<K, V>, Iterable<Entry<K,V>> {

	private static final String errorKeyNotFound = "The given Key does not exist.";
	private static final String errorDuplicateKey = "The given Key is already in use.";
	
	
	Node<Entry<K,V>> head;
	
	@SafeVarargs
	public Dictionary(Pair<K,V>... pairs) throws DuplicateKeyException{
		if (ArrEx.Any(pairs, pair -> ArrEx.Count(pairs, pair2 -> pair.getKey() == pair2.getKey()) > 1))
			throw new DuplicateKeyException(errorDuplicateKey);
		if (pairs.length > 0) head = new Node<Entry<K,V>>(pairs[0]);
		if (pairs.length > 1){
			Node<Entry<K,V>> cursor = head;
			for(int i = 0; i < pairs.length-1; i++){
				cursor.setNext(new Node<Entry<K,V>>(pairs[i+1]));
				cursor = cursor.getNext();
			}
		}
	}
	
	public static class KeyNotFoundException extends Exception { public KeyNotFoundException(String message) { super(message); } }
	public static class DuplicateKeyException extends Exception { public DuplicateKeyException(String message) { super(message); } }
	@Override
	public void clear() { head = null; }
	@Override
	public boolean containsKey(Object key) {
		Node<Entry<K,V>> cursor = head;
		while(cursor != null){
			if (cursor.getValue().getKey().equals(key)) return true;
			cursor = cursor.getNext();
		}
		return false;
	}
	@Override
	public boolean containsValue(Object value) {
		Node<Entry<K,V>> cursor = head;
		while(cursor != null){
			if (cursor.getValue().getValue().equals(value)) return true;
			cursor = cursor.getNext();
		}
		return false;
	}
	@Override
	public Set<Entry<K, V>> entrySet() {
		return new NodeSet<Entry<K,V>>(head);
	}
	@Override
	public V get(Object key) {
		Node<Entry<K,V>> cursor = head;
		while(cursor != null){
			if (cursor.getValue().getKey().equals(key)) return cursor.getValue().getValue();
			cursor = cursor.getNext();
		}
		throw new NullPointerException(errorKeyNotFound);
	}
	@Override
	public boolean isEmpty() {
		return head == null;
	}
	@Override
	public Set<K> keySet() {
		// JEtzt wirds mir zu blöd...
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public V put(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator<java.util.Map.Entry<K, V>> iterator() {
		return new Iterator<Map.Entry<K,V>>() {

			Node<Entry<K,V>> cursor = head;
			
			@Override
			public boolean hasNext() {
				return cursor == null;
			}

			@Override
			public java.util.Map.Entry<K, V> next() {
				Entry<K,V> result = cursor.getValue();
				cursor = cursor.getNext();
				return result;
			}
		};
	}
}
