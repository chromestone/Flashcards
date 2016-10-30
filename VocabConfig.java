import java.util.*;
import java.io.*;
public class VocabConfig {
   public HashMap<Integer, String> readConfig(String filePath, String slashType) throws IOException{
      try {
		   //create BufferredReader to read the config file
         BufferedReader getConfig = new BufferedReader(new FileReader(filePath + slashType + "VocabConfiguration.txt"));
         //string array needed to hold two strings after a string is split
		   String[] splitConfig;
         String config;
		   //create HashMap to hold the list of configuration
         HashMap<Integer, String> configList = new HashMap<Integer, String>();
		   //gets the delay between vocab and definition, if line does not exist default is 3 seconds
         if ((config = getConfig.readLine()) != null) {
            splitConfig = config.split("\t");
            config = splitConfig[1];
            configList.put(1, config + "000");
         }
         else {
            configList.put(1, "3000");
         }
		   //gets the delay between defition and sentence, if line does not exist default is 4 seconds
         if ((config = getConfig.readLine()) != null) {
            splitConfig = config.split("\t");
            config = splitConfig[1];
            configList.put(2, config + "000");
         }
         else {
            configList.put(2, "4000");
         }
		   //gets the delay between sentence and the query(or next vocab), if line does not exist default is 5 seconds
         if ((config = getConfig.readLine()) != null) {
            splitConfig = config.split("\t");
            config = splitConfig[1];
            configList.put(3, config + "000");
         }
         else {
            configList.put(3, "5000");
         }
		   //gets the mode from the config file(either browse mode or not), if line does not exist default is false
         if ((config = getConfig.readLine()) != null) {
            splitConfig = config.split("\t");
            config = splitConfig[1];
            configList.put(4, config);
         }
         else {
            configList.put(4, "false");
         }
         //something
         if ((config = getConfig.readLine()) != null) {
            splitConfig = config.split("\t");
            config = splitConfig[1];
            configList.put(5, config);
         }
         else {
            configList.put(5, "false");
         }
		   //closes the BufferredReader when done
         getConfig.close();
         return configList;
      }
      catch (IOException a) {
         System.err.println("Error when reading configuration");
         throw a;
      }
   }
}
