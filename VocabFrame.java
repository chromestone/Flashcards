import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VocabFrame {
   private JFrame vocabFrame;
   private JPanel mainPanel;
   private JLabel label;
   private final VocabFrame thisClass;
   private int counter;
   private String _vocab;
   private String _sentence;
   private String _definition;
   private boolean exit;

	public VocabFrame() {
	   //create the vocabFrame and adds the title for it
	   vocabFrame = new JFrame("FlashCards");
	   //set the size of the vocabFrame
	   vocabFrame.setSize(700, 400);
	   //prevents users from closing the window
	   vocabFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	   //centers the frame
	   vocabFrame.setLocationRelativeTo(null);
	   //prevents users from resizing window causing unbalanced display
	   vocabFrame.setResizable(false);
      exit = false;
	   //checks if user has attempted to close window, if so changes exit's value
	   vocabFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            exit = true;
         }
      });

	   //create JPanel to hold the vocab + definition + sentence
      mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      
      thisClass = this;

      Component topComp = Box.createRigidArea(new Dimension(100, 100));
      topComp.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            switch (thisClass.counter-1) {
            case 0: thisClass.updateLabel(thisClass._vocab, "v");
               break;
            case 1: thisClass.updateLabel(thisClass._sentence, "s");
               break;
            default: break;
            }
         }
      });
      mainPanel.add(topComp);

      label = new JLabel();
      label.setFont(new Font("Arial", 13, 45));
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
      mainPanel.add(label);

      Component bottomComp = Box.createRigidArea(new Dimension(100, 100));
      bottomComp.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            switch (thisClass.counter+1) {
            case 1: thisClass.updateLabel(thisClass._sentence, "s");
               break;
            case 2: thisClass.updateLabel(thisClass._definition, "d");
               break;
            default: break;
            }
         }
      });
      mainPanel.add(bottomComp);

      mainPanel.setOpaque(true);

      vocabFrame.add(mainPanel);
	}
   public void show() {
	   //displays the frame to the user
      vocabFrame.setVisible(true);
   }
   public void hide() {
	   //hides the frame from the user
      vocabFrame.setVisible(false);
   }
   public void displayVocab(String vocab) {
      updateLabel(vocab, "v");
      _vocab = vocab;
   }
   public void displaySentence(String sentence) {
      updateLabel(sentence, "s");
      _sentence = sentence;
   }
   public void displayDefinition(String definition) {
      updateLabel(definition, "d");
      _definition = definition;
   }
   private synchronized void updateLabel(String str, String id) {
      if (id.equals("v")) {
         label.setText(str);
         counter = 0;
      }
      else {
         String formattedString = String.format("<html><div WIDTH=%d>%s</div><html>", 600, str);
         label.setText(formattedString);
         if (id.equals("s")) {
            counter = 1;
         }
         if (id.equals("d")) {
            counter = 2;
         }
      }
   }
   public boolean shouldExit() {
      return exit;
   }
   public String queryMemorized() {
	   //object array containing the choices the user will see
      Object[] options = {"Yes", "No", "More Time"};

	   //queries user whether or not the vocab is memorized and for more time, the response is represented as an int
      int input = JOptionPane.showOptionDialog(vocabFrame,
               "Memorized?",
               "FlashCards-Memorized?",
               JOptionPane.YES_NO_CANCEL_OPTION,
               JOptionPane.QUESTION_MESSAGE,
               null,
               options,
               options[0]);

	   //checks what the integer is equal to
	   //if the user chose yes then command string is m
      if (input == JOptionPane.YES_OPTION) {
         return "m";
      }
	   //otherwise if the user chose More Time(cancel) the command string is w
      else if (input == JOptionPane.CANCEL_OPTION) {
         return "w";
      }
      //if niether Yes or Cancel is chosen then an empty string is returned
      else {
         return "";
      }
   }
   public void dispose() {
	   //disposes of the frame and everything associated with it
      vocabFrame.dispose();
   }
}