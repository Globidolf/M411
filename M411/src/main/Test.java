package main;

import java.time.Duration;
import java.time.Instant;
import java.util.Map.Entry;
import java.util.Random;

import main.Dictionary.DuplicateKeyException;
import main.LinkedList.ListEmptyException;
import main.Stack.StackEmptyException;

/**
 * Testklasse ohne fixen zweck
 * @author Silvan Pfister
 *
 */
public class Test {
	
	static Random rng = new Random();
	
	public static void main(String[] args) {
		try {
			TestDictionary();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static void TestDictionary() throws DuplicateKeyException {
		Dictionary<Integer, String> Dict = new Dictionary<>(
				new Pair(12, "test"),
				new Pair(224, "ROFL"));
		Utility.WriteOutput("");
		for(Entry p : Dict) {
			Utility.WriteOutput(p.toString());
		}
	}
	
	static void TestAsArray() {
		LinkedList<Integer> intList = new LinkedList<>(1,2,3,4,5,6);
		Integer[] intArray = ArrEx.AsArray(intList, Integer.class);
		Utility.WriteOutput(ArrEx.AsString(intArray));
	}
	
	static void TestStack() throws StackEmptyException {
		Stack<Integer> intStack = new Stack<>();
		
		intStack.push(1);
		intStack.push(2);
		intStack.push(3);
		intStack.push(4);
		
		for(int i = 0; i < 5; i++){
			Utility.WriteOutput(intStack.pop().toString());
		}
	}
	
	static void TestLinkedList() throws IndexOutOfBoundsException, ListEmptyException {
		
		LinkedList<String> LLS = new LinkedList<String>("0", "1", "2", "3", "4");
		
		Utility.WriteOutput(LLS.toString());
		
		LLS.removeAt(2);

		Utility.WriteOutput(LLS.toString());
		
		LLS.removeAt(0);

		Utility.WriteOutput(LLS.toString());
		
		LLS.append("APPEND");

		Utility.WriteOutput(LLS.toString());
		
		LLS.prepend("PREPEND");

		Utility.WriteOutput(LLS.toString());

		LLS.insert("INSERT", 4);

		Utility.WriteOutput(LLS.toString());

		LLS.replaceAt(3, "REPLACE");

		Utility.WriteOutput(LLS.toString());

		Utility.WriteOutput("");
		
		for(String s : LLS.toList())
			Utility.WriteOutput(s);

		Utility.WriteOutput("");
		
		for(String s : LLS)
			Utility.WriteOutput(s);
	}
	
	static void TestArrays(){
		Integer[] base, arr;
		base = ArrEx.Instantiate(100000, Integer.class, rng::nextInt);
		
		arr = ArrEx.Duplicate(base, Integer.class);
		
		Instant tStart, tEnd;
		Duration dur;
		
		tStart = Instant.now();
		ArrEx.BubbleSort(arr);
		tEnd = Instant.now();
		dur = Duration.between(tStart, tEnd);
		
		Utility.WriteOutput("Bubblesort took " + dur.toMillis() + " ms.");
		Utility.WriteOutput(ArrEx.AsString(arr));
		
		arr = ArrEx.Duplicate(base, Integer.class);
		
		tStart = Instant.now();
		ArrEx.QuickSort(arr);
		tEnd = Instant.now();
		dur = Duration.between(tStart, tEnd);
		
		Utility.WriteOutput("Quicksort took " + dur.toMillis() + " ms.");
		Utility.WriteOutput(ArrEx.AsString(arr));
	}

}
