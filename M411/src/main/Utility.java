package main;

import java.io.IOException;

public class Utility {
	public static String ReadInput(String message) throws IOException {
		WriteOutput(message);
		return ReadInput();
	}
	public static String ReadInput() throws IOException{
		while(System.in.available() == 0);
		byte[] buffer = new byte[System.in.available()];
		System.in.read(buffer);
		return String.valueOf(ArrEx.toPrim(ArrEx.Select(ArrEx.toObj(buffer), Character.class, (B) ->  Character.valueOf((char)(byte)B)))).trim();
	}
	public static void WriteOutput(String message){
		//System.out.println(ArrEx.AsString(Thread.currentThread().getStackTrace(), "\n\t")); 
		System.out.println(message);
	}
	
}
