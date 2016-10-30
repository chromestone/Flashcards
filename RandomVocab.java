import java.util.*;
import java.io.*;

public class RandomVocab { 

   private final String filePath;

   private String slashType;
   private HashMap<Integer, Pair> vocabList;
   private HashMap<Integer, Boolean> memorizedList;
   private VocabConfig vocabConfig;
   private Definition definition;
   private VocabFrame vocabFrame;

   // constructor
   public RandomVocab() { 
      filePath = System.getProperty("user.dir");
      System.out.println(filePath);
      //finds the operating system and based on it decides which slash to use
      slashType = System.getProperty("os.name").contains("windows") == true ? "\\" : "/";
      //created to read the configuration file
      vocabConfig = new VocabConfig();
      //created to read definition file and parse definition from internet
      definition = new Definition(filePath, slashType);
      //created to have UI for user
      vocabFrame = new VocabFrame();
      //HashMap that contains vocab and sentence
      vocabList = new HashMap<Integer, Pair>();
      //HashMap that contains vocab index and a boolean representing if word is memorized
      memorizedList = new HashMap<Integer, Boolean>(); 
   }
///////////////////////////////////////////////////////////////////////////////////
//this method reads the memorized file(specific to user) and loads it into a HashMap
///////////////////////////////////////////////////////////////////////////////////
   private void memorizeFile(String filePath, String username) throws IOException {
      try {
         //created to read memorized file
         BufferedReader getMemorized = new BufferedReader(new FileReader(filePath + slashType + username + "VocabMemorized.txt"));
         String memorized;
         int vocabIndex;
         //string array is needed to hold the vocab index and memorized or not
         String[] line;
         //reads untill the end of file
         while ((memorized = getMemorized.readLine()) != null) {
            //gets two strings by splitting using the delimeter tab
            line = memorized.split("\t");
            //changes the String vocab index into an Integer
            vocabIndex = Integer.parseInt(line[0]);
            //if the number is 1 put memorized as true as key, else memorized is false
            if (line[1].equals("1")) {
               memorizedList.put(vocabIndex, true);
            }
            else {
               memorizedList.put(vocabIndex, false);
            }
         }
         //close reader when done
         getMemorized.close();
      }
      catch (IOException a) {
         System.err.println("Error when reading memorized-File");
         throw a;
      }
   }
////////////////////////////////////////////////
//this method checks if the memorized file(specific to user) exists
////////////////////////////////////////////////
   private boolean fileExists(String username) {
      //checks if memorized file exists
      if (new File(filePath + slashType + username + "VocabMemorized.txt").isFile()) {
         return true;
      }
      else {
         return false;
      }
   }
//////////////////////////////////////////////////////////////////
/* this method reads the vocab file and loads it into a HashMap
 * it also calls the method to read memorized file if the (memorized) file exists
 * if not then it creates memorized HashMap while creating the vocab HashMap
 * also returns the largest index in the vocab HashMap
*/
//////////////////////////////////////////////////////////////////
   private Integer vocabFile(String username) throws IOException {
      try {
         //create BufferredReader to read the vocab file containing vocab + sentence
         BufferedReader getVocab = new BufferedReader(new FileReader(filePath + slashType + "Vocab.txt"));
         int vocabNum = 0;
         Pair vocabPkg;
         String vocab = "";
         //if the memorized file exists then read from file, else set every word to not memorized
         if (fileExists(username)) {
            //read the memorized file
            memorizeFile(filePath, username);
            //reads untill the end of file
            while ((vocab = getVocab.readLine()) != null) {
               //substrings the vocab file to get desired info
               vocabPkg = new Pair(vocab.substring(vocab.indexOf("\t")+1, vocab.indexOf(":")),
                                      vocab.substring(vocab.indexOf(":")+1));
               vocabList.put(vocabNum, vocabPkg);
               vocabNum++;
            }
         }
         else {
            while ((vocab = getVocab.readLine()) != null) {
               vocabPkg = new Pair(vocab.substring(vocab.indexOf("\t")+1, vocab.indexOf(":")),
                                      vocab.substring(vocab.indexOf(":")+1));
               vocabList.put(vocabNum, vocabPkg);
               //creates the memorized HashMap along with creating the vocab HashMap
               memorizedList.put(vocabNum, false);
               vocabNum++;
            }
         }
         //closes the BufferredReader when done
         getVocab.close();
         //number needed as it will serve as a max number
         return vocabNum;
      }
      catch (IOException a) {
         System.err.println("Error when reading vocab-File");
         throw a;
      }
   }
////////////////////////////////////////////////////////////////
/* this method should be called at the end of the program
 * the method will
*/
////////////////////////////////////////////////////////////////
   private void writeFiles(String username) throws IOException {
      try {
         //saves definition
         definition.saveDefinition();
   
         //saves the memorized file
         BufferedWriter recordMemorized = new BufferedWriter(new FileWriter(filePath + slashType + username + "VocabMemorized.txt"));
         Set<Map.Entry<Integer, Boolean>> set = memorizedList.entrySet();
         Iterator<Map.Entry<Integer, Boolean>> it = set.iterator();
         while(it.hasNext()) {
            Map.Entry<Integer, Boolean> map = (Map.Entry<Integer, Boolean>)it.next();
         //memorized is stored as 1 and not memorized is stored as 0 in the file
            recordMemorized.write(map.getKey().toString() + "\t" + ((Boolean)map.getValue() == true ? "1" : "0") + "\n");
         }
         //closes the BufferredWriter when done
         recordMemorized.close();
         }
      catch (IOException a) {
         System.err.println("Error when writing files");
         throw a;
      }
   }


   private void run() throws Exception{
      try {
         //requests user for name
         System.out.print("Enter Name:\n>");
         Scanner user_input = new Scanner(System.in);
         String username = user_input.next();
         System.out.println("loading...");
         
         //creates configuration HashMap from reading the configuration file
         HashMap<Integer, String> configuration = vocabConfig.readConfig(filePath, slashType);
         //gets the mode to be used, if true then the program will not ask user memorized
         boolean browseMode = Boolean.parseBoolean(configuration.get(4));
         
         //gets the maximum number of vocabs and also adds values the the memorized and vocab HashMaps
         int vocabNum = vocabFile(username);
   
         //reads the definition file
         definition.readDefinition();
   
         vocabFrame.show();
         String command = "";
         int vocabID = -1;
         Random randomNum = new Random();
         Pair pair;
         String vocab;
         boolean isSequential = Boolean.parseBoolean(configuration.get(5));
         while (memorizedList.containsValue(false)) {
            if (isSequential) {
               vocabID++;
               if (vocabID > vocabNum) {
                  vocabID = 0;
               }
            }
            else {
               //gets the vocab ID randomly
               vocabID = randomNum.nextInt(vocabNum);
            }
            //checks if the vocab HashMap has such value
            if (vocabList.containsKey(vocabID)) {
               //creates a pair containing values needed later on
               pair = (Pair)vocabList.get(vocabID);
               //checks to see if the word is already memorized
               //boolean notShowWord = (Boolean)memorizedList.get(vocabID);
               if ((Boolean)memorizedList.get(vocabID) != true) {
                  //display the vocab on the frame
                  vocab = (String)pair.getKey();
                  vocabFrame.displayVocab(vocab);

                  definition.prepareXML(vocab);
                  definition.playPronunciation();

                  //pause the program based on config file(between vocab and sentence
                  Thread.sleep(Long.parseLong(configuration.get(2)));
                  //display the sentence on the frame
                  vocabFrame.displaySentence((String)pair.getValue());

                  //pause the program based on config file(between sentence and definition
                  Thread.sleep(Long.parseLong(configuration.get(1)));
                  //checks if the definition exists, if not then parse from internet, then display definition on the frame
                  if (definition.containsDefinition()) {
                     vocabFrame.displayDefinition(definition.getDefinition());
                  }
                  else {
                     vocabFrame.displayDefinition(definition.parseDefinition());
                  }
   
                  //if browse mod is off then ask user memorized or not or more time
                  if (browseMode == false) {
                     //as long as the user asks for more time the question will be displayed at intervals based on config file 
                     do {
                        Thread.sleep(Long.parseLong(configuration.get(3)));
                        command = vocabFrame.queryMemorized();
                     }
                     while (command.equals("w"));
                     //if the user chooses memorized then change memorized to true in memorzied HashMap
                     if (command.equals("m")) {
                        memorizedList.put(vocabID, true);
                     }
                  }
                  else {
                //pause the program based on config file
                Thread.sleep(Long.parseLong(configuration.get(3)));
                  }
                  //clears the display in the UI, but also checks if the user has attempted to exit 
                  if (vocabFrame.shouldExit()) {
                     break;
                  }
               }
            }
         }
         //writes the file upon the user exiting 
         writeFiles(username);
         //clears up anything tied to the UI(JFrame) 
         vocabFrame.dispose();
      }
      catch (Exception a) {
         System.err.println("Error when running in RandomVocab");
         throw a;
      }
   }

   public static void main(String args[]) {
      // build object
      RandomVocab randomVocab = new RandomVocab();

      try {

         // execute behavior
         randomVocab.run();

      }
      catch (Exception a) {
        System.err.println(a.getMessage());
        a.printStackTrace();
      }
   }
}
