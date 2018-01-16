package main;

/**
 * 
 * Liest 10 Zahlen vom Benutzer in ein Array mit fixer grösse.
 * Array wird anschliessen ausgegeben.
 * 
 * @author Silvan Pfister
 *
 */
public class Aufgabe_3_2 {

	public static void main(String[] args) {
		Integer[] data = new Integer[10];
		
		ArrEx.ForEach(data, (item) -> {
			boolean success = false;
			do{
				try {
					item = Integer.parseInt(Utility.ReadInput("Write a Number"));
					ArrEx.Append(data, item);
					success = true;
				} catch (Exception e) {
					e.printStackTrace();
					Utility.WriteOutput("THIS IS NOT A NUMBER! TRY AGAIN:");
				}
			} while(!success);
		});
		Utility.WriteOutput("You entered the following numbers: \n" + ArrEx.AsString(data));
		
	}
}
