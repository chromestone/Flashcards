/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
          import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class Definition
/*     */ {
/*     */   private HashMap<String, String> definitionList;
/*  17 */   private int initialSize = 0;
/*     */   private String xmlString;
/*     */   private Pronunciation pronunciation;
/*     */   private String _word;
/*  22 */   private final String fileName = "Definition.txt";
/*     */   private String _filePath;
/*     */   private String _slashType;
/*     */   
/*     */   public Definition(String paramString1, String paramString2)
/*     */   {
/*  29 */     this.definitionList = new HashMap();
/*  30 */     this._filePath = paramString1;
/*  31 */     this._slashType = paramString2;
/*  32 */     this.xmlString = "";
/*  33 */     this.pronunciation = new Pronunciation();
/*  34 */     this._word = "";
/*     */   }
/*     */   
/*     */   public void readDefinition()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  51 */       BufferedReader localBufferedReader = new BufferedReader(new FileReader(this._filePath + "\\Definition.txt"));
/*     */       String str;
/*  56 */       while ((str = localBufferedReader.readLine()) != null)
/*     */       {
/*  58 */         String[] arrayOfString = str.split("\t");
/*     */         
/*  60 */         this.definitionList.put(arrayOfString[0], arrayOfString[1]);
/*     */       }
/*  63 */       this.initialSize = this.definitionList.size();
/*     */       
/*  65 */       localBufferedReader.close();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*  68 */       System.out.println("Error when read definition");
/*  69 */       throw localIOException;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getDefinition()
/*     */   {
/*  75 */     return (String)this.definitionList.get(this._word);
/*     */   }
/*     */   
/*     */   public boolean containsDefinition()
/*     */   {
/*  80 */     if (this.definitionList.containsKey(this._word)) {
/*  81 */       return true;
/*     */     }
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   public void prepareXML(String paramString)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/*  93 */       this._word = paramString;
/*     */       
/*  95 */       URL localURL = new URL("http://dictionary.reference.com/browse/" + this._word);
/*     */       
/*  97 */       URLConnection localURLConnection = localURL.openConnection();
/*     */       
/*  99 */       InputStream localInputStream = localURLConnection.getInputStream();
/*     */       
/* 101 */       BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
/* 102 */       String str = null;
/*     */       
/* 104 */       StringBuilder localStringBuilder = new StringBuilder();
/* 106 */       while ((str = localBufferedReader.readLine()) != null) {
/* 108 */         localStringBuilder.append(str);
/*     */       }
/* 110 */       localInputStream.close();
/* 111 */       localBufferedReader.close();
/*     */       
/* 113 */       this.xmlString = localStringBuilder.toString();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 116 */       System.err.println("Error preparing xml string");
/* 117 */       throw localException;
/*     */     }
/*     */   }
/*     */   
/*     */   public String parseDefinition()
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 129 */       Pattern localPattern = Pattern.compile("<meta name=\"description\" content=\"" + this._word + " definition, " + ".+?" + " See more");
/*     */       
/* 131 */       Matcher localMatcher = localPattern.matcher(this.xmlString);
/*     */       
/* 133 */       String str = "";
/* 134 */       if (localMatcher.find(1))
/*     */       {
/* 136 */         str = localMatcher.group();
/* 137 */         str = str.substring(47 + this._word.length(), str.length() - 10);
/* 138 */         if (str.contains(":"))
/*     */         {
/* 139 */           str = str.substring(0, str.lastIndexOf(":"));
/* 140 */           if (str.contains(".")) {
/* 141 */             str = str.substring(0, str.lastIndexOf(".") + 1);
/*     */           }
/*     */         }
/* 145 */         this.definitionList.put(this._word, str);
/*     */       }
/*     */       else
/*     */       {
/* 151 */         str = "Definition Error: " + this._word;
/* 152 */         System.out.println(str);
/* 153 */         BufferedWriter localBufferedWriter = new BufferedWriter(new FileWriter(this._filePath + this._slashType + "Error.xml"));
/* 154 */         localBufferedWriter.write(this.xmlString);
/* 155 */         localBufferedWriter.close();
/*     */       }
/* 158 */       return str;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 161 */       System.err.println("Error when parse definition");
/* 162 */       throw localException;
/*     */     }
/*     */   }
/*     */   
/*     */   public void playPronunciation()
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 168 */       Pattern localPattern = Pattern.compile("<span class=\"speaker\" audio=\".+?\"");
/*     */       
/* 170 */       Matcher localMatcher = localPattern.matcher(this.xmlString);
/* 171 */       String str = "";
/* 172 */       if (localMatcher.find(1))
/*     */       {
/* 173 */         str = localMatcher.group(0);
/* 174 */         str = str.substring(29, str.length() - 1);
/* 175 */         if (str.contains("http://"))
/*     */         {
/* 176 */           this.pronunciation.prepareFile(str);
/* 177 */           this.pronunciation.play();
/*     */         }
/*     */         else
/*     */         {
/* 180 */           System.out.println(this._word + " " + str);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 183 */         System.out.println("SoundError " + this._word);
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 186 */       System.err.println("Error playing pronunciation");
/* 187 */       throw localException;
/*     */     }
/*     */   }
/*     */   
/*     */   public void saveDefinition()
/*     */     throws IOException
/*     */   {
/* 197 */     if (this.definitionList.size() != this.initialSize) {
/*     */       try
/*     */       {
/* 200 */         BufferedWriter localBufferedWriter = new BufferedWriter(new FileWriter(this._filePath + this._slashType + "Definition.txt"));
/* 201 */         Set localSet = this.definitionList.entrySet();
/* 202 */         Iterator localIterator = localSet.iterator();
/* 203 */         while (localIterator.hasNext())
/*     */         {
/* 204 */           Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 205 */           localBufferedWriter.write(((String)localEntry.getKey()).toString() + "\t" + ((String)localEntry.getValue()).toString() + "\n");
/*     */         }
/* 208 */         localBufferedWriter.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 211 */         System.err.println("Error when save definition");
/* 212 */         throw localIOException;
/*     */       }
/*     */     } else {
/* 216 */       System.out.println("No change detected, file not written");
/*     */     }
/*     */   }
/*     */ }
