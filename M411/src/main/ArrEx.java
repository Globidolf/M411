package main;

import java.lang.reflect.Array;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
/**
 * Replacement for the C# Linq library.
 * @author Silvan Pfister
 *
 */
public abstract class ArrEx {

	//Creating Methods
	/**
	 * Duplicates the given array into a new array instance. 
	 * Note that, if the Type T is mutable, 
	 * changes will be reflected in both arrays.
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param returnType The array element type
	 * @return The new array
	 */
	public static <T> T[] Duplicate(T[] arr, Class<T> returnType){
		T[] result = Instantiate(arr.length, returnType);
		Iterate(arr, (item, index) -> result[index] = item);
		return result;
	}
	
	/**
	 * Creates an array of specified type and size.
	 * @param <T> The type of the array elements
	 * @param length The array length
	 * @param returnType The array element type
	 * @return The new array
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] Instantiate(int length, Class<T> returnType){ return (T[]) Array.newInstance(returnType, length); }

	/**
	 * Creates an array of specified type and size using values
	 * from the provided creator function
	 * @param <T> The type of the array elements
	 * @param length The array length
	 * @param returnType The array element type
	 * @param creator The creator function
	 * @return The new array
	 */
	public static <T> T[] Instantiate(int length, Class<T> returnType , Supplier<T> creator){
		T[] result = Instantiate(length, returnType);
		for(int i = 0; i < result.length; i++) result[i] = creator.get();
		return result;
	}
	
	/**
	 * Creates an array of specified type and size using values
	 * from the provided creator function, which has access to the element index
	 * @param <T> The type of the array elements
	 * @param length The array length
	 * @param returnType The array element type
	 * @param creator The creator function
	 * @return The new array
	 */
	public static <T> T[] InstantiateIndexed(int length, Class<T> returnType , Function<Integer, T> creator){
		T[] result = Instantiate(length, returnType);
		for(int i = 0; i < result.length; i++) result[i] = creator.apply(i);
		return result;
	}
	
	/**
	 * Creates a new Array with the specified size and copies as many
	 * values as possible into it. 
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param newSize The size of the new array
	 * @param itemType The class of the item
	 * @return An array with size newSize and the contents of arr
	 */
	public static <T> T[] Resize(T[] arr, int newSize, Class<T> itemType){
		T[] newArray = Instantiate(arr.length + 1, itemType);
		for(int i = 0; i < Math.min(arr.length, newArray.length); i++)
			newArray[i] = arr[i];
		return newArray;
	}

	/**
	 * Loss-less version of Prepend.
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param newItem The new item
	 * @param itemType The class of the item
	 * @return A new array
	 */
	public static <T> T[] Prepend(T[] arr, T newItem, Class<T> itemType){
		T[] newArray = Resize(arr, arr.length + 1, itemType);
		Prepend(newArray, newItem);
		return newArray;
	}

	/**
	 * Loss-less version of Append.
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param newItem The new item
	 * @param itemType The class of the item
	 * @return A new array
	 */
	public static <T> T[] Append(T[] arr, T newItem, Class<T> itemType){
		T[] newArray = Resize(arr, arr.length + 1, itemType);
		Append(newArray, newItem);
		return newArray;
	}

	/**
	 * Loss-less version of Insert.
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param newItem The new item
	 * @param index The index
	 * @param itemType The class of the item
	 * @return A new array
	 */
	public static <T> T[] Insert(T[] arr, T newItem, int index, Class<T> itemType){
		T[] newArray = Resize(arr, arr.length + 1, itemType);
		Insert(newArray, newItem, index);
		return newArray;
	}

	// Sorting Methods
	/**
	 * Uses the QuickSort algorithm to sort the array
	 * @param <T> The type of the array elements
	 * @param arr The array
	 */
	public static <T extends Comparable<T>> void QuickSort(T[] arr){ iterateqs(arr, 0, arr.length-1); }

	/**
	 * Uses the QuickSort algorithm to sort the array
	 * @param <T> The type of the array elements
	 * @param <R> The comparable type returned by the selector
	 * @param arr The array
	 * @param selector A selector function which will extract a comparable value used for the sorting function
	 */
	public static <T, R extends Comparable<R>> void QuickSort(T[] arr, Function<T, R> selector){ iterateqs(arr, 0, arr.length - 1, selector); }
		
	/**
	 * QuickSort iteration function. I don't really get it myself...
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param low Don't ask me
	 * @param high Don't ask me
	 */
	private static <T extends Comparable<T>> void iterateqs(T[] arr, int low, int high){
		
		if (low < high){
			int i = partition(arr, low, high);
			iterateqs(arr, low, i - 1);
			iterateqs(arr, i + 1, high);
		}
	}

	/**
	 * Same as the overload, but uses the result of a selector function to sort instead.
	 * @param <T> The type of the array elements
	 * @param <R> The comparable type returned by the selector
	 * @param arr The array
	 * @param low Don't ask me
	 * @param high Don't ask me
	 * @param selector gets a comparable value out of the object
	 * @return don't know
	 */
	private static <T, R extends Comparable<R>> void iterateqs(T[] arr, int low, int high, Function<T, R> selector){
		if (low < high){
			int i = partition(arr, low, high, selector);
			iterateqs(arr, low, i - 1, selector);
			iterateqs(arr, i + 1, high, selector);
		}
	}
	
	/**
	 * I don't even
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param low Don't ask me
	 * @param high Don't ask me
	 * @return Some sort of index i guess
	 */
	private static <T extends Comparable<T>> int partition(T[] arr, int low, int high){
		T pivot = arr[high];
		int i = low-1;
		for(int j = low; j < high; j++)
			if(arr[j].compareTo(pivot) <= 0){
				i++;
				Swap(arr, i, j);
			}
		Swap(arr, i + 1, high);
		return i + 1;
	}

	/**
	 * Same as the overload, but uses the result of a selector function to sort instead.
	 * 
	 * @param <T> The type of the array elements
	 * @param <R> The comparable type returned by the selector
	 * @param arr The array
	 * @param low Don't ask me
	 * @param high Don't ask me
	 * @param selector gets a comparable value out of the object
	 * @return don't know
	 */
	private static <T, R extends Comparable<R>> int partition(T[] arr, int low, int high, Function<T, R> selector){
		R pivot = selector.apply(arr[high]);
		int i = low-1;
		for(int j = low; j < high; j++)
			if(selector.apply(arr[j]).compareTo(pivot) <= 0){
				i++;
				Swap(arr, i, j);
			}
		Swap(arr, i + 1, high);
		return i + 1;
	}
	
	//Manipulative Methods

	/**
	 * Populates an array using the provided creator function / Supplier
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param creator The creator function
	 */
	public static <T> void Populate(T[] arr, Supplier<T> creator){
		for(int i = 0; i < arr.length; i++)
			arr[i] = creator.get();
	}

	/**
	 * Populates an array using the provided creator function / Supplier.
	 * Provides the element index as parameter to the creator.
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param creator The creator function
	 */
	public static <T> void PopulateIndexed(T[] arr, Function<Integer, T> creator){
		for(int i = 0; i < arr.length; i++)
			arr[i] = creator.apply(i);
	}

	/**
	 * Sorts an array of a Comparable type using BubbleSort
	 * @param <T> The type of the array elements (Must be a Comparable)
	 * @param arr The array
	 */
	public static <T extends Comparable<T>> void BubbleSort(T[] arr){
		for(int i = 0; i < arr.length ; i++ )
			for(int j = arr.length - 1; j > i; j--)
				if (arr[i].compareTo(arr[j]) > 0) Swap(arr, i, j);
	}
	
	
	/**
	 * Swaps the value at index1 with the one at index2
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param index1 The first index
	 * @param index2 The second index
	 */
	public static <T> void Swap(T[] arr, int index1, int index2){
		T temp = arr[index1];
		arr[index1] = arr[index2];
		arr[index2] = temp;
	}
	
	/**
	 * Inserts a value at index 0.
	 * Remaining values are moved one index up.
	 * Last value is lost in the process.
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param newItem The new item
	 */
	public static <T> void Prepend(T[] arr, T newItem){
		for(int i = arr.length - 1; i > 0; i--) Swap(arr, i, i-1);
		arr[0] = newItem;
	}
	
	/**
	 * Inserts a value at index N-1.
	 * Remaining values are moved one index down.
	 * First value is lost in the process.
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param newItem The new item
	 */
	public static <T> void Append(T[] arr, T newItem){
		for (int i = 1; i < arr.length; i++) Swap(arr, i-1, i);
		arr[arr.length-1] = newItem;
	}

	/**
	 * Inserts an item into the array at the specified index.
	 * Values beginning from specified index are moved one index up.
	 * Last value is lost in the process
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param newItem The new item
	 * @param index The index
	 */
	public static <T> void Insert(T[] arr, T newItem, int index){
		for(int i = arr.length - 1; i > index; i--)
			Swap(arr, i, i-1);
		arr[index] = newItem;
	}
     
	/**
	 * Reverses an array
	 * @param <T> The type of the array elements
	 * @param arr The array
	 */
	public static <T> void Reverse(T[] arr){ for(int i = 0; i < arr.length / 2; i++) Swap(arr, i, arr.length - 1 - i); }
	
	//Deterministic Methods

	/**
	 * Checks if any element of the array matches a condition
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param predicate The condition
	 * @return True if at least one item matches the condition. Otherwise false.
	 */
	public static <T> boolean Any(T[] arr, Predicate<T> predicate){
		for(T item : arr) if (predicate.test(item)) return true;
		return false;
	}
	
	/**
	 * Checks if all elements of the array match a condition
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param predicate The condition
	 * @return True if all items match the condition. Otherwise false.
	 */
	public static <T> boolean All(T[] arr, Predicate<T> predicate){
		for(T item : arr) if (!predicate.test(item)) return false;
		return true;
	}
	
	/**
	 * Counts the amount of elements in the array that match a condition 
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param predicate The condition
	 * @return The amount of matches
	 */
	public static <T> int Count(T[] arr, Predicate<T> predicate){
		int result = 0;
		for(T item : arr) if (predicate.test(item)) result++;
		return result;
	}
	
	/**
	 * Determines the first item of an array that matches
	 * the specified predicate, or null.
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param predicate The predicate
	 * @return The first match, or null if there are none.
	 */
	public static <T> T First(T[] arr, Predicate<T> predicate)
	{ 
		for (T t : arr) if (predicate.test(t)) return t; 
		return null; 
	}

	/**
	 * Determines the last item of an array that matches
	 * the specified predicate, or null.
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param predicate The predicate
	 * @return The last match, or null if there are none.
	 */
	public static <T> T Last(T[] arr, Predicate<T> predicate)
	{
		for(int i = arr.length-1; i > 0; i++) if (predicate.test(arr[i])) return arr[i];
		return null;
	}
	
	/**
	 * Compares all values of an array and returns the highest one
	 * @param <T> The type of the array elements (Must be a Comparable)
	 * @param arr The array
	 * @return The highest value
	 */
	public static <T extends Comparable<T>> T Max(T[] arr){
		T result = arr[0];
		for(T item : arr) if (result.compareTo(item) < 0) result = item;
		return result;
	}

	/**
	 * Compares all values of an array and returns the lowest one
	 * @param <T> The type of the array elements (Must be a Comparable)
	 * @param arr The array
	 * @return The lowest value
	 */
	public static <T extends Comparable<T>> T Min(T[] arr){
		T result = arr[0];
		for(T item : arr) if (result.compareTo(item) > 0) result = item;
		return result;
	}
	
	// Conversion Methods
	
	/**
	 * Creates a string representation of the array using the provided
	 * StringConverter function for each element
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param StringConverter The StringConverter
	 * @return The string representation
	 */
	public static <T> String AsString(T[] arr, Function<T, String> StringConverter){
		BiFunction<String, String, String> Accumulator = (str1, str2) -> str1 + ',' + str2;
		return "[" + Aggregate(arr, StringConverter, Accumulator) + "]";
	}

	/**
	 * Creates a string representation of the array using the provided
	 * StringConverter function for each element
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param StringConverter The StringConverter
	 * @param separator A Separator
	 * @return The string representation
	 */
	public static <T> String AsString(T[] arr, Function<T, String> StringConverter, String separator){
		BiFunction<String, String, String> Accumulator = (str1, str2) -> str1 + separator + str2;
		return "[" + Aggregate(arr, StringConverter, Accumulator) + "]";
	}
	
	/**
	 * Creates a string representation of the array using the
	 * default toString() method of each element
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @return The string representation
	 */
	public static <T> String AsString(T[] arr){
		Function<T, String> StringConverter = (obj) -> obj.toString();
		BiFunction<String, String, String> Accumulator = (str1, str2) -> str1 + ", " + str2;
		return "[" + Aggregate(arr, StringConverter, Accumulator) + "]";
	}
	
	/**
	 * Creates a string representation of the array using the
	 * default toString() method of each element
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param separator A separator
	 * @return The string representation
	 */
	public static <T> String AsString(T[] arr, String separator){
		Function<T, String> StringConverter = (obj) -> obj.toString();
		BiFunction<String, String, String> Accumulator = (str1, str2) -> str1 + separator + str2;
		return "[" + Aggregate(arr, StringConverter, Accumulator) + "]";
	}
	
	/**
	 * Converts an iterable instance to an array
	 * @param iterable The iterable
	 * @param returnType The class of the instances
	 * @return The array
	 */
	public static <T> T[] AsArray(Iterable<T> iterable, Class<T> returnType){
		LinkedList<T> temp = new LinkedList<>();
		for(T item : iterable) temp.append(item);
		T[] result = Instantiate(temp.getCount(), returnType);
		for(int i = 0; i < result.length; i++) result[i] = temp.get(i);
		return result;
	}
	
	/**
	 * Converts an array to a new form using the converter function
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param ReturnType The type of the new array elements
	 * @param converter The converter function
	 * @return The new array
	 */
	public static <T, R> R[] Select(T[] arr, Class<R> ReturnType, Function<T, R> converter){
		R[] result = Instantiate(arr.length, ReturnType);
		Iterate(arr, (item, index) -> result[index] = converter.apply(item));
		return result;
	}

	// Iterative Methods

	/**
	 * Method version of a For loop.
	 * First argument of the BiConsumer is the object, second argument is the index
	 * @param <T> The type of the array elements
	 * @param arr The array to iterate
	 * @param bc The equivalent function to the for block content
	 */
	public static <T> void Iterate(T[] arr, BiConsumer<T, Integer> bc){ for(int i = 0; i < arr.length; i++) bc.accept(arr[i], i); }
	
	/**
	 * Method version of a ForEach loop. The argument of c is the object.
	 * @param <T> The type of the array elements
	 * @param arr The array to iterate
	 * @param c The equivalent function to the ForEach block content
	 */
	public static <T> void ForEach(T[] arr, Consumer<T> c){ for (T t : arr) { c.accept(t); } }

	/**
	 * Applies an accumulator function to an array and returns the result
	 * @param <T> The type of the array elements
	 * @param arr The array
	 * @param aggregator The accumulator function
	 * @return The result
	 */
	public static <T> T Aggregate(T[] arr, BiFunction<T, T, T> aggregator){
		T result = arr[0];
		for (int i = 1; i < arr.length;i++) result = aggregator.apply(result, arr[i]); 
		return result;
	}
	
	/**
	 * Applies a seeded accumulator function to an array and returns the result
	 * @param <T> The type of the array elements
	 * @param <A> The type of the accumulate
	 * @param arr The array
	 * @param seed The seed
	 * @param aggregator The accumulator function
	 * @return The result
	 */
	public static <T, A> A Aggregate(T[] arr, A seed, BiFunction<A, T, A> aggregator){
		A result = seed;
		for (T item : arr) result = aggregator.apply(result, item);
		return result;
	}
	
	/**
	 * Applies a seeded accumulator function to an array, then converts the final accumulate with the converter function and returns the result
	 * @param <T> The type of the array elements
	 * @param <A> The type of the accumulate
	 * @param <R> The type of the result
	 * @param arr The array
	 * @param seed The seed
	 * @param aggregator The accumulator function
	 * @param converter The converter function
	 * @return The result
	 */
	public static <T, A, R> R Aggregate(T[] arr, A seed, BiFunction<A, T, A> aggregator, Function<A, R> converter){ 
		return converter.apply(Aggregate(arr, seed, aggregator)); 
	}
	
	/**
	 * Applies a an accumulator function to the results of a converter function of each element of an array.
	 * @param <T> The type of the array elements
	 * @param <A> The type to convert the elements to, and the result type
	 * @param arr The array
	 * @param converter The converter function
	 * @param aggregator The accumulator function
	 * @return The result
	 */
	public static <T, A> A Aggregate(T[] arr, Function<T, A> converter, BiFunction<A, A, A> aggregator){
		A result = converter.apply(arr[0]);
		for (int i = 1; i < arr.length;i++) result = aggregator.apply(result, converter.apply(arr[i])); 
		return result;
	}

	
	//I HATE JAVA: (Primitive array conversion functions...)
	
	// Primary To Object
	public static Byte[] toObj(byte[] arr){
		Byte[] result = new Byte[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static Integer[] toObj(int[] arr){
		Integer[] result = new Integer[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static Float[] toObj(float[] arr){
		Float[] result = new Float[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static Double[] toObj(double[] arr){
		Double[] result = new Double[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static Long[] toObj(long[] arr){
		Long[] result = new Long[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static Short[] toObj(short[] arr){
		Short[] result = new Short[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static Boolean[] toObj(boolean[] arr){
		Boolean[] result = new Boolean[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static Character[] toObj(char[] arr){
		Character[] result = new Character[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	
	//Object to Primary
	public static byte[] toPrim(Byte[] arr){
		byte[] result = new byte[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static int[] toPrim(Integer[] arr){
		int[] result = new int[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static float[] toPrim(Float[] arr){
		float[] result = new float[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static double[] toPrim(Double[] arr){
		double[] result = new double[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static long[] toPrim(Long[] arr){
		long[] result = new long[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static short[] toPrim(Short[] arr){
		short[] result = new short[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static boolean[] toPrim(Boolean[] arr){
		boolean[] result = new boolean[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
	public static char[] toPrim(Character[] arr){
		char[] result = new char[arr.length];
		for (int i = 0; i < arr.length;i++) result[i] = arr[i];
		return result;
	}
}
