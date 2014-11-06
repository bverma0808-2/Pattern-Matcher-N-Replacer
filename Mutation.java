import java.io.*;
import java.util.*;

class Mutation{
   static Trie patterns = new Trie();
   static PrintWriter propertySaver;
   static String fileExtension;


   public static void main(String []args){
      loadPatterns(args[0]);
      fileExtension = args[1];
      loadFilesAndMakeThemMutant(args[2]);
   }   


   static void loadPatterns(String path){
      try{
         File patternFile = new File(path);
         BufferedReader br = new BufferedReader(new FileReader(patternFile));
         String pat;
         while((pat=br.readLine())!=null){
            patterns.insertInTrie(pat);
         }
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }


   static void loadFilesAndMakeThemMutant(String path){
      File parentDirectory = new File(path);
      
      if(!parentDirectory.exists()){
         System.out.println("Directory doesn't exist");
         return;
      }

      LinkedList<File> queue = new LinkedList<File>();
      queue.addLast(parentDirectory);
      int count=0;

      //doing a Breadth First Search in the directory to load each and every file
      while(!queue.isEmpty()){
         File f = (File)queue.removeFirst();

         //If file is a directory, then add all of its child files back into the queue
         if(f.isDirectory()){   
             File farr[] = f.listFiles();
             for(int i=0; i<farr.length; i++){
                //System.out.println(farr[i].getName());
                queue.addLast(farr[i]);
             }
         }
         //Else file is a ".extension" file , process the file and mark the occurences
         else{
             if(f.getName().endsWith("."+fileExtension)){  
                count++;
                System.out.println("Processing.... "+f.getName());
                processFile(f,count);
             }
         }
      }
      System.out.println("\n"+count+" files are processed\n");
   }


   static void processFile(File f, int fileNumber){
      try{
          StringTokenizer stk = new StringTokenizer(f.getName(),".");
          String fileNameWithoutExt = stk.nextToken();
          BufferedReader br = new BufferedReader(new FileReader(f));
          int ch;
          //int lineNum=0,matches=0;
          String finalOutput="";
          String text="";
          //boolean fileNameLogged = false;
           
          if(fileExtension.equals("gsp")){
             finalOutput += "<g:set var=\"pkgName\" value=\"gsp."+fileNameWithoutExt+"\"></g:set>\n";
          }

          String key = ""; 
          String value = "";
          int Ccount=0,Dcount=0;
          TrieNode current = patterns.root;
          while((ch = br.read())!=-1){
              finalOutput += (char)ch;
              if(((char)ch)=='>'){
			      ch = br.read();
				  //finalOutput += (char)ch;
				  boolean flag = false;
				  String txt = "";
				  while(((char)ch)!='<'){
				     if(ch==-1){
					    flag = true;
					    break;
					 }
					 if(!isAllowed(ch)){ 
					    break;
					 }
					 txt += (char)ch;
					 ch = br.read();
				  }
				  if(flag)
				     break;
			      if(!txt.trim().equals("") && ((char)ch)=='<'){
				       String defValue = txt;
					   String code = removeWhiteSpaceChars(txt);
					   String fullyQualifiedCode = fileExtension+"."+fileNameWithoutExt+"."+code;
					   saveRecord(fullyQualifiedCode, defValue);
					   finalOutput += "<g:message code=\""+fullyQualifiedCode+"\"  default=\""+defValue+"\" />";
				  }
				  finalOutput += (char)ch;
			  }
			  
			  else {
                 TrieNode temp = current.subNode((char)ch);
                 if(temp==null){
                    current = patterns.root;
                    text = "";
                 }
                 else{
                    current = temp;
                    text += (char)ch;
                    if(current.isEndOfString){ //we found a match
             /*       if(text.equals("code:")){
                        ch = br.read();
                        finalOutput += (char)ch;
                        int enclosingQuote = 34;
                        while(ch!=34 && ch!=39){  //34 is the ascii value of "double quote" and 39 is for 'single quote'
                           ch = br.read();
						   if(ch==39)
						       ch = 34;
                           finalOutput += (char)ch;
                        }
                        
						if(ch==34)
                           enclosingQuote = 39;
                           
                        finalOutput += "${pkgName}.";
                        if(!key.equals("")){
                           saveRecord(key,key);
                        }
                        key = "gsp."+fileNameWithoutExt+".";

                        ch = br.read();
                        key += ""+(char)ch;
                        finalOutput += (char)ch;

                        while(ch!=enclosingQuote){  
                           ch = br.read();
						   if(ch==39)
						       ch = 34;
                           finalOutput += (char)ch;
						   ch=39;
                           if(ch!=enclosingQuote)
                              key += (char)ch;
                        }
                    }
*/
                 /*   else if(text.equals("default:")){
                        ch = br.read();
                        finalOutput += (char)ch;
                        int enclosingQuote;
                        while(ch!=34 && ch!=39){  //34 is the ascii value of "double quote" and 39 is for 'single quote'
                           ch = br.read();
                           finalOutput += (char)ch;
                        }

                        value = "";
                        enclosingQuote = ch;
                        ch = br.read();
                        value += ""+(char)ch;
                        finalOutput += (char)ch;
                        
                        while(ch!=enclosingQuote){  
                           ch = br.read();
                           finalOutput += (char)ch;
                           if(ch!=enclosingQuote)
                              value += (char)ch;
                        }
                        saveRecord(key , value);
                        key = "";
                        value = "";
                    }
*/
                     if(text.equals("code=") || text.equals("code:")){ Ccount++;
                        ch = br.read();
                        finalOutput += (char)ch;
                        int enclosingQuote;
                        while(ch!=34 && ch!=39){  //34 is the ascii value of "double quote" and 39 is for 'single quote'
                           ch = br.read();
                           finalOutput += (char)ch;
                        }
                        
                        enclosingQuote = ch;
                           
                        finalOutput += "${pkgName}.";
                        if(!key.equals("")){
                           saveRecord(key,key);
                        }
                        key = "gsp."+fileNameWithoutExt+".";

                        ch = br.read();
                        key += ""+(char)ch;
                        finalOutput += (char)ch;

                        while(ch!=enclosingQuote){  
                           ch = br.read();
                           finalOutput += (char)ch;
                           if(ch!=enclosingQuote)
                              key += (char)ch;
                        }
                    }
                  
                     else if(text.equals("default=") || text.equals("default:")){Dcount++; 
                        ch = br.read();
                        finalOutput += (char)ch;
                        int enclosingQuote;
                        while(ch!=34 && ch!=39){  //34 is the ascii value of "double quote" and 39 is for 'single quote'
                           ch = br.read();
                           finalOutput += (char)ch;
                        }

                        value = "";
                        enclosingQuote = ch;
                        ch = br.read();
                        value += ""+(char)ch;
                        finalOutput += (char)ch;
                        
                        while(ch!=enclosingQuote){  
                           ch = br.read();
                           finalOutput += (char)ch;
                           if(ch!=enclosingQuote)
                              value += (char)ch;
                        }
                        saveRecord(key , value);
                        key = "";
                        value = "";
                    }

                    } 
                 }
              }
          }
          FileOutputStream fos = new FileOutputStream(f);
          OutputStreamWriter osw = new OutputStreamWriter(fos);
          osw.write(finalOutput);
	      osw.close();
          System.out.println("codeCount="+Ccount+"   "+"DeCount="+Dcount);
      }
      catch(Exception e){
          e.printStackTrace();
      }
    }
	
	
	static boolean isAllowed(int ch){
	     if(ch>=47 && ch<=59 || ch>=64 && ch<=90 || ch>=97&&ch<=122  || ch == 38 ||ch==32)
		    return true;
	     return false;
	}
	
	static String removeWhiteSpaceChars(String text){
	     String ret = "";
	     for(int i=0; i<text.length(); i++){
		    char c = text.charAt(i);
		    if(c==' ')
			    ret+=".";
			else
			    ret+=c;
		 }
		 return ret;
	}

    static void saveRecord(String key, String value){
       try{    
          FileOutputStream fos = new FileOutputStream(new File("messages.properties"),true);
          OutputStreamWriter osw = new OutputStreamWriter(fos);
          osw.write(key+"="+value+"\n");
          osw.close();
       }
       catch(Exception e){
          e.printStackTrace();
       }
    }
}
