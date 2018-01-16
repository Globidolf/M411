package main;

import java.util.Map.Entry;

public class Pair<K, V> implements Entry<K, V> {
	private K key;
	private V value;
	public Pair(K key, V value){ this.key = key; this.value = value;}
	public K getKey() { return key; }
	public V getValue() {return value; }
	public void setKey(K key) { this.key = key; }
	public V setValue(V value) { return this.value = value; }
	@Override public String toString() { return "[" + key.toString() + "] -> [" + value.toString() + "]"; }
}