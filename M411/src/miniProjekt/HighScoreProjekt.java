package miniProjekt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import main.Utility;

/**
 * The class for the Mini-Project for the Module 411.
 * Provides a main method plus the classes required for the project as subclasses.
 * This class uses the custom Utility class for it's simplified wrappers of the system print and read methods.
 * @author Silvan Pfister
 */
public class HighScoreProjekt {
	/**
	 * Application Entry Point.
	 * Provides a Console based interface to access the HighScoreAdmin class
	 * @param args ignored
	 */
	public static void main(String[] args) {
		// Variable Declaration
		HighScoreAdmin admin = new HighScoreAdmin();
		String input; 
		// As the console IO can throw exceptions, the whole process is wrapped in a try block 
		try {
			admin.load();
			// reads input and verifies it at the same time, if it is 'e' or 'exit', the loop will end
			while (!(input = 
					Utility.ReadInput("Load[L] / New[N] / Exit[E] / Save[S] / Display[D]")
						.toLowerCase()
						.trim()).equals("e")
					&& !input.equals("exit")){
				// switch the input, by default state that the input is invalid. 
				switch (input) {
					case "load":
					case "l": // load a save from the given file
						input = Utility.ReadInput("Enter the Filename or nothing to load from the default file");
						if(input.isEmpty()) admin.load(); 
						else admin.load(input);
						break;
					case "new":
					case "n":
						int score = 0;
						boolean success = false;
						 // as the score needs validation, it cannot be read inline.
						while(!success){
							try{
								score = Integer.parseInt(Utility.ReadInput("Enter your Score"));
								success = true;
							} catch (NumberFormatException e) {
								Utility.WriteOutput("The text you entered is not a number...");
							}
						}
						// The rest is then read inline of the add method
						Utility.WriteOutput(admin.submit(
								Utility.ReadInput("Enter your username"), 
								Instant.now().toString(), 
								Utility.ReadInput("Enter the level name"), 
								score));
						// inform that the operation was successful
						Utility.WriteOutput("Added new HighScore entry");
						break;
					case "save":
					case "s": // save the current state to a specified file
						input = Utility.ReadInput("Enter the Filename or nothing to save to the default file");
						if(input == null || input.trim().isEmpty()) admin.save(); 
						else admin.save(input.trim());
						break;
					case "display":
					case "d": // display all entries or filter by level from input of user
						String level;
						if (!(level = Utility.ReadInput("Enter the Level name, or nothing to show all entries").trim()).isEmpty())
							Utility.WriteOutput(admin.levelScoring(level));
						else
							Utility.WriteOutput(admin.completeScoring());
						break;
					default: // invalid input 
						Utility.WriteOutput("Unknown Input Value: " + input);
						break;
				}
			}
			admin.save();
		} catch (IOException e) { // exception occurred, exit program
			e.printStackTrace();
		} // program exits, inform user.
		Utility.WriteOutput("Stopping Application");
	}
	
	/**
	 * This class manages all highScores.
	 * can add scores to the list.
	 * can load and save from files.
	 * can output to console.
	 * @author Silvan Pfister
	 *
	 */
	public static class HighScoreAdmin{
		
		/**
		 * The storage of the highScore data.
		 */
		private ArrayList<HighScore> HighScores;
		
		/**
		 * Constructor. Initialises the highScore list.
		 */
		public HighScoreAdmin(){ HighScores = new ArrayList<>(); }
		
		/**
		 * submits a HighScore and returns ranking plus top ten as per project definition 
		 * @param name The name of the user
		 * @param dateTime The dateTime of the HighScore
		 * @param level The name of the level
		 * @param finishTime The time it took to finish
		 * @return A String containing the rank and the top 10
		 */
		public String submit(String name, String dateTime, String level, int finishTime){
			// Time parse may fail, 
			try{
				String result = "";
				HighScore entry = new HighScore(name, Instant.parse(dateTime), level, finishTime);
				add(entry);
				ArrayList<HighScore> levelList = new ArrayList<>();
				for(HighScore score : HighScores)
					if (score.level.equals(level))
						levelList.add(score);
				levelList.sort((s1,s2) -> s2.score.compareTo(s1.score));
				int placement = levelList.indexOf(entry);
				if (placement < 10)
					result += "You placed " + (placement+1) + (placement == 0 ? /*1*/"st!! Congratulations!" :
						(placement == 1 ? /*2*/"nd! GG" :
							(placement == 2 ? /*3*/"rd!" :
								/*4-10*/"th.")));
				else // 11-Infinite
					result += "You placed " + (placement+1) + "th...";
				// Display Top Ten
				result += "\n\n" + level + ":\n";
				for(int i = 0; i < Math.min(10, levelList.size()); i++)
					result += levelList.get(i).toString() + "\n";

				return result;
			} catch (DateTimeParseException e){
				return "Couldn't parse the DateTime:\n" + e.getLocalizedMessage();
			}
		}
		
		/**
		 * Adds a highScore instance to the list and connects it.
		 * @param highScore The highScore instance
		 */
		public void add(HighScore highScore){
			// reference used to format the output string
			highScore.admin = this;
			HighScores.add(highScore); 
		}
		
		/**
		 * Loads the default scores
		 */
		public void load(){ load("default.sav"); }
		/**
		 * Loads all hichScores from the specified file
		 * @param fileName The file to load from
		 */
		public void load(String fileName){
			// amount of highScores before loading, to calculate the new ones in the output.
			int before = HighScores.size();
			File f = new File(fileName);
			// If the file does not exist, there is nothing to load, so return
			if (!f.exists()) return;
			// potential IO errors
			try {
				// create a buffered reader from the file
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String line;
				// read all lines and add an entry for each line
				while ((line = br.readLine()) != null)
					add(HighScore.deserialise(line));
				// close the readers
				br.close();
				fr.close();
				Utility.WriteOutput("Successfully loaded " + (HighScores.size() - before) + " scores.");
			} catch (IOException e) { // some error occurred
				e.printStackTrace();
			}
		}
		/**
		 * Saves to the default scores
		 */
		public void save() { save("default.sav"); }
		/**
		 * Saves the current highScores to the specified file
		 * @param fileName The name of the file to save the highScores in
		 */
		public void save(String fileName){
			File f = new File(fileName);
			// Potential IO errors
			try {
				// We will not append to prevent duplicate entries, so if the file already exists, delete it.
				if (f.exists()) f.delete();
				// create the file anew and create a buffered writer for it
				f.createNewFile();
				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);
				// serialise each entry and write it to a separate line
				for (HighScore highScore : HighScores) {
					bw.write(highScore.serialise());
					bw.newLine();
				}
				// Close the writers
				bw.close();
				fw.close();
				Utility.WriteOutput("Succesfully saved " + HighScores.size() + " Highscores.");
			} catch (IOException e) { // an error occurred
				e.printStackTrace();
			}
		}
		
		/**
		 * Gets the size of the longest name in the highScores
		 * @return The highest length of a name available
		 */
		int nameLength(){
			// Initialise this with zero, then compare each name and reset the length if longer.
			int len = 0;
			for (HighScore highScore : HighScores)
				if (highScore.userName.length() > len) len = highScore.userName.length();
			return len;
		}
		
		/**
		 * Display every scoring available.
		 */
		public String completeScoring(){
			String result = "Showing Scores for all Levels:\n";
			// Order by score, descending
			HighScores.sort((score1, score2)-> score2.score.compareTo(score1.score));
			for (HighScore highScore : HighScores) // append level name to the score
				result += highScore.toString() + " from level '" + highScore.level + "'\n";
			return result;
		}
		
		/**
		 * Display every scoring from the specified level
		 * @param level The level to list the scores for
		 */
		public String levelScoring(String level){
			String result = "Showing Scores for level '" + level + "':\n";
			// Order by score, descending
			HighScores.sort((score1, score2)-> score2.score.compareTo(score1.score));
			for (HighScore highScore : HighScores) // iterate all scores and only display those with the specified level.
				if (highScore.level.equals(level))
					result += highScore.toString() + "\n";
			return result;
		}
		
		/**
		 * Gets all HighScores of the specified level and returns them as array sorted by score
		 * @param level The level
		 * @return An Array of HighScores
		 */
		public HighScore[] getHighScores(String level){
			ArrayList<HighScore> result = new ArrayList<>();
			for(HighScore score : HighScores)
				if (score.level.equals(level)) result.add(score);
			result.sort((s1,s2)->s2.score.compareTo(s1.score));
			return result.toArray(new HighScore[result.size()]);
		}
	}
	
	/**
	 * This class represents a HighScore, including name, level, date and score itself
	 * @author Silvan Pfister
	 *
	 */
	public static class HighScore{
		/**
		 * The HighScoreAdmin instance used to manage this HighScore instance
		 */
		private HighScoreAdmin admin;
		/**
		 * The userName of the person that achieved this score 
		 */
		private String userName;
		/**
		 * The date when this score was taken
		 */
		private Instant date;
		/**
		 * The level this score was made in
		 */
		private String level;
		/**
		 * The score value
		 */
		private Integer score;
		
		/**
		 * Creates a new HighScore instance with the specified values, the date being the moment the score is created.
		 * @param userName The userName
		 * @param level The level
		 * @param score The score 
		 */
		public HighScore(String userName, String level, Integer score){
			this.userName = userName;
			this.date = Instant.now();
			this.level = level;
			this.score = score;
		}
		/**
		 * Creates a new HighScore instance with the specified values.
		 * @param userName The userName
		 * @param date The date
		 * @param level The level
		 * @param score The score
		 */
		public HighScore(String userName, Instant date, String level, Integer score){ 
			this.userName = userName;
			this.date = date;
			this.level = level;
			this.score = score;
		}
		
		/**
		 * Creates a HighScore instance from a serialised string
		 * @param raw The raw serialised string from a previous serialise method call
		 * @return The HighScore instance that was represented by the raw string
		 */
		public static HighScore deserialise(String raw){
			// Turns escaped backslashes back to normal backslashes
			raw = raw.replace("\\\\", "\\");
			// Splits the raw to the semicolon separated parts. ignores escaped semicolons
			String[] parts = raw.split("(?<!\\\\);");
			// Creates a new instance from the parts
			return new HighScore(
					parts[0],
					Instant.parse(parts[1]),
					parts[2],
					Integer.parseInt(parts[3]));
		}
		/**
		 * Creates a serialised String instance from the current HighScore instance which can be recovered using the deserialise method.
		 * @return A String representation of this HighScore instance.
		 */
		public String serialise(){
			// Creates the raw string while escaping backslashes and unintended semicolons.
			String result = "";
			result += userName.replace("\\", "\\\\").replace(";", "\\;") + ";";
			result += date.toString() + ";";
			result += level.replace("\\", "\\\\").replace(";", "\\;") + ";";
			result += score + ";";
			return result;
		}
		
		/**
		 * Represents this HighScore instance in a table like String format. The Level is omitted.
		 */
		@Override
		public String toString() {
			// if this instance is part of an admin instance, get the name length from there
			int len = 0;
			if (admin != null) len = admin.nameLength();
			// The following format call formats the text as in the example below
			return String.format( // YYYY.MM.DD HH:MM:SS | userName____ | ____score
					"%1$tY.%1$tm.%1$td %1$tH:%1$tM:%1$tS | %2$-"+len+"s | %3$12d", 
					date.toEpochMilli(), //$1
					userName, //$2
					score); //$3
		}
	}
}
