public class Trie{
   TrieNode root; 

   public Trie(){
      root = new TrieNode(' ');
   }
   
   public void insertInTrie(String s){
      if(s.length()==0){
         root.isEndOfString = true;
         return;
      }
      
      TrieNode current = root;
      for(int i=0; i<s.length(); i++){
         TrieNode temp = current.subNode(s.charAt(i));
         if(temp==null){ 
            temp = new TrieNode(s.charAt(i));
            current.addChild(temp);
         }
         current = temp;
      }
      current.isEndOfString = true;
   }
}
