package miniProjekt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import main.Utility;

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
		// As the console io can throw exceptions, the whole process is wrapped in a try block 
		try {
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
						admin.load(Utility.ReadInput("Enter the Filename"));
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
						admin.add(new HighScore(
								Utility.ReadInput("Enter your username"),
								Utility.ReadInput("Enter the level name"),
								score));
						// inform that the operation was successful
						Utility.WriteOutput("Added new HighScore entry");
						break;
					case "save":
					case "s": // save the current state to a specified file
						admin.save(Utility.ReadInput("Enter the Filename"));
						break;
					case "display":
					case "d": // display all entries or filter by level from input of user
						String level;
						if (!(level = Utility.ReadInput("Enter the Level name, or nothing to show all entries").trim()).isEmpty())
							admin.levelScoring(level);
						else
							admin.completeScoring();
						break;
					default: // invalid input 
						Utility.WriteOutput("Unknown Input Value: " + input);
						break;
				}
			}
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
		 * The storage of the highscore data.
		 */
		private ArrayList<HighScore> HighScores;
		
		/**
		 * Constructor. Initializes the highscore list.
		 */
		public HighScoreAdmin(){ HighScores = new ArrayList<>(); }
		
		public void add(HighScore highScore){
			highScore.admin = this;
			HighScores.add(highScore); 
		}
		
		public void load(String fileName){
			int before = HighScores.size();
			File f = new File(fileName);
			if (!f.exists()) return;
			try {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while ((line = br.readLine()) != null)
					add(HighScore.Deserialize(line));
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Utility.WriteOutput("Successfully loaded " + (HighScores.size() - before) + " scores.");
		}
		
		public void save(String fileName){
			File f = new File(fileName);
			try {
				if (f.exists()) 
					f.delete();
				f.createNewFile();
				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);
				for (HighScore highScore : HighScores) {
					bw.write(highScore.Serialize());
					bw.newLine();
				}
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Utility.WriteOutput("Succesfully saved " + HighScores.size() + " Highscores.");
		}
		
		public int nameLength(){
			int len = 0;
			for (HighScore highScore : HighScores)
				if (highScore.userName.length() > len) len = highScore.userName.length();
			return len;
		}
		
		public void completeScoring(){
			Utility.WriteOutput("Showing Scores for all Levels:");
			HighScores.sort((score1, score2)-> score2.score.compareTo(score1.score));
			for (HighScore highScore : HighScores)
				Utility.WriteOutput(highScore.toString() + " from level '" + highScore.level + "'");
		}
		
		public void levelScoring(String level){
			HighScores.sort((score1, score2)-> score2.score.compareTo(score1.score));
			Utility.WriteOutput("Showing Scores for level '" + level + "':");
			for (HighScore highScore : HighScores)
				if (highScore.level.equals(level))
					Utility.WriteOutput(highScore.toString());
		}
	}
	
	public static class HighScore{
		public HighScoreAdmin admin;
		public String userName;
		public Instant date;
		public String level;
		public Integer score;
		
		public HighScore(String userName, String level, Integer score){
			this.userName = userName;
			this.date = Instant.now();
			this.level = level;
			this.score = score;
		}
		public HighScore(String userName, Instant date, String level, Integer score){ 
			this.userName = userName;
			this.date = date;
			this.level = level;
			this.score = score;
		}
		
		public static HighScore Deserialize(String raw){
			raw = raw.replace("\\\\", "\\");
			String[] parts = raw.split("(?<!\\\\);");
			return new HighScore(
					parts[0],
					Instant.parse(parts[1]),
					parts[2],
					Integer.parseInt(parts[3]));
		}
		
		public String Serialize(){
			String result = "";
			result += userName.replace("\\", "\\\\").replace(";", "\\;") + ";";
			result += date.toString() + ";";
			result += level.replace("\\", "\\\\").replace(";", "\\;") + ";";
			result += score + ";";
			return result;
		}
		
		@Override
		public String toString() {
			int len = 0;
			if (admin != null) len = admin.nameLength();
			return String.format( // YYYY.MM.DD HH:MM:SS | userName____ | ____score
					"%1$tY.%1$tm.%1$td %1$tH:%1$tM:%1$tS | %2$-"+len+"s | %3$12d", 
					date.toEpochMilli(), //$1
					userName, //$2
					score); //$3
		}
	}
}