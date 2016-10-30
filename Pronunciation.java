import java.net.*;
import java.io.*;
import javax.media.*;

public class Pronunciation {

   static Player player;

   public void prepareFile(String urlStr) throws Exception{
      try {
         URL url = new URL(urlStr);
         //URL url = new URL("http://static.sfdict.com/dictstatic/dictionary/audio/luna/S07/S0715200.mp3");
         URLConnection con = url.openConnection();
         InputStream inputStream = con.getInputStream();
         OutputStream outputStream = new FileOutputStream(new File("Sound.mp3"));
         int read = 0;
         byte[] bytes = new byte[1024];
         while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
         }
         inputStream.close();
         outputStream.close();
      }
      catch (Exception a) {
         System.err.println("Error getting sound file");
         throw a;
      }
   }
   public void play() throws Exception{
      try {
         File file = new File("Sound.mp3");
         player = Manager.createRealizedPlayer(file.toURI().toURL());

         player.start();

         double sleepTime = player.getDuration().getSeconds();
         if (sleepTime < 11) {
            Thread.sleep((long)(sleepTime*1000));
         }
         else {
            System.out.println(sleepTime);
            Thread.sleep(5*1000);
         }

         player.close();
      }
      catch (Exception a) {
         System.err.println("Error playing sound file");
         throw a;
      }
   }
}
