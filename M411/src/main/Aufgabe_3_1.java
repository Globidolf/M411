package main;

import java.util.Random;

/**
 * Demonstration der Max und Min Methoden der ArrEx Klasse. 
 * @author Silvan Pfister
 *
 */
public class Aufgabe_3_1 {

	public static void main(String[] args) {
		Random RNG = new Random();
		Integer[] data = ArrEx.Instantiate(50, Integer.class, ()->RNG.nextInt(1001));
		System.out.println("Max: " + ArrEx.Max(data));
		System.out.println("Min: " + ArrEx.Min(data));
	}
}
