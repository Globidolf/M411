package test;

import java.io.IOException;

import main.ArrEx;
import main.Stack;
import main.Utility;


public class TestAufgabe {
	
	public static void main(String[] args) throws IOException {
		String input;
		Stack<Character> messageStack = new Stack<>();
		Character[] output;
		
		while ((input = Utility.ReadInput("Geben Sie eine Nachricht mit midestens zwei Zeichen ein:")).length() < 2);
		output = new Character[input.length()];
		for(char c : input.toCharArray()) messageStack.push(c);
		
		ArrEx.Populate(output, messageStack::popSave);
		Utility.WriteOutput(String.valueOf(ArrEx.toPrim(output)));
	}
}