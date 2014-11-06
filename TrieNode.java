import java.util.*;

class TrieNode{
   char data;
   boolean isEndOfString;
   LinkedList <TrieNode>  child;
   
   public TrieNode(char c){
      child = new LinkedList<TrieNode>();
      isEndOfString = false;
      data = c;
   }
 
   public TrieNode subNode(char c){
      if(child!=null){
         for(TrieNode eachChild:child){  
            if(eachChild.data == c)
                return eachChild;
         }
      }
      return null;
   }

   public void addChild(TrieNode tn){
      child.addLast(tn);
   }
}
