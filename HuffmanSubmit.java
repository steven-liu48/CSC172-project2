/*
Name: Winnie Wan
BB ID: wwan5
wwan5@u.rochester.edu

Name: Xiaoxiang Liu
BB ID: xliu102
xliu102@u.rochester.edu

Date: March 9, 2019
Project 2
*/
// Import any package as required
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class HuffmanSubmit implements Huffman {
    static Node root = null;
    static String decodeStrings;
    static Map<Character, Integer> frequencyOfChar = new HashMap<>();
    static Map<Character, String> allBinaryCodes = new HashMap<>();
    static StringBuilder sb = new StringBuilder();
    static PriorityQueue<Node> pq;

    public static class Node{
        char ch;
        int freq;
        Node left = null;
        Node right = null;

        public Node(char ch, int freq){
            this.ch = ch;
            this.freq = freq;
        }
        public Node(char ch, int freq, Node left, Node right){
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }
    }

    /*
    storeInMapR() will take in a node, an empty string, and a map that will store
    in the char and the string of bits. Ex: <'c',"101010">.
    It will traverse the tree that is already created, reach every leaf and store it
    in the map along with the binary we have created.
     */
    public static void storeInMapR(Node root, String str, Map<Character, String> codes){
        if(root == null){
            return;
        }

        if(root.left == null && root.right == null){
            codes.put(root.ch, str);
        }else {
            storeInMapR(root.left, str + "0", codes);
            storeInMapR(root.right, str + "1", codes);
        }
    }

    /*
    creatFreqMap: Make a frequency map that stores character and #ofOccurrence.
        If it does not exist in the map, declare it with a value 0, then add
        1 afterwards. If it does exist, just add 1 to the count/value.
     */
    public static void createFreqMap(String text){
        for(int i = 0; i < text.length(); i++){
            if(!frequencyOfChar.containsKey((text.charAt(i))) ){
                frequencyOfChar.put(text.charAt(i), 0);
            }
            frequencyOfChar.put(text.charAt(i), frequencyOfChar.get(text.charAt(i)) + 1);
        }
    }

    /*
    createATree() will create the huffman tree.
    1) Make freq Map
    2) Create Priority Queue to store with lowest frequency as priority.
     */
    public static void createATree(){
        /*
        Priority Queue:
        compareTo(Node l, Node r){ return l.freq-r.freq; }
        which will check every single node to make sure the
        priority is the lowest frequency one.
         */
        pq = new PriorityQueue<>((l,r) -> l.freq - r.freq);

        //Add leaf nodes to Queue
        for(Map.Entry<Character, Integer> entry: frequencyOfChar.entrySet()){
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        //Create tree: We loop until one node is left which is the root.
        while(pq.size() != 1){
            //Remove 2 nodes at a time.
            Node left = pq.poll();
            Node right = pq.poll();

            int sum = left.freq + right.freq;
            pq.add(new Node('\0', sum, left, right));
        }

        //Root of tree
        root = pq.peek();

        //Store all the binary codes in a hashmap
        storeInMapR(root, "", allBinaryCodes);
}

    /*
    BinaryString() will give us the output binary encode.
     */
    public static StringBuilder BinaryString(String text){
        //Gives you the output binary string
        for(int i = 0; i < text.length(); i++){
            sb.append(allBinaryCodes.get(text.charAt(i)));
        }

        return sb;
    }

    /*
    decodeR() will take in the binary string input(Ex: input = "101010")
    and traverse the tree with the Node root until we reach a leaf. We store
    the char/binary into a string, decodeString, and repeat recursively until
    we have the whole decoded string.
     */
    public static int decodeR(Node root, int index, String input){
        if(root == null){
            return index;
        }

        if(root.left == null && root.right == null){
            decodeStrings += root.ch;
            return index;
        }
        index++;
        if(input.length() > index) {
            if (input.charAt(index) == '0') {
                index = decodeR(root.left, index, input);
            }
            else{
                index = decodeR(root.right, index, input);
            }
        }
        return index;
    }

    /*
    decode the encoded string
     */
    public static void decodeString(String encodedText){
        int index = -1;
        while(index < encodedText.length() - 6){
            index = decodeR(root, index, encodedText);
        }
    }


	public void encode(String inputFile, String outputFile, String freqFile){
		//turn file into a string
        String text = "";
        BinaryIn bin = new BinaryIn(inputFile);
        try{
            while(true)
                text += bin.readChar();
        }catch(Exception e){
        }

        createFreqMap(text);

        //Write/Create freq file
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(freqFile));
            for(Character key : frequencyOfChar.keySet()){
                String temp = Integer.toBinaryString(key);
                while(temp.length() != 8){
                    temp = "0" + temp;
                }
                writer.write(temp + ":" + frequencyOfChar.get(key));
                writer.write("\n");
            }
            writer.close();
        }catch(Exception e){
        }
        createATree();
        BinaryString(text);

        BinaryOut out = new BinaryOut(outputFile);
        for(String i: sb.toString().split("")){
            if(i.equals("1")){
                out.write(true);
            }else{
                out.write(false);
            }
        }

        out.close();
    }


   public void decode(String inputFile, String outputFile, String freqFile){
       //turn file into a string
       String text = "";
       BinaryIn bin2 = new BinaryIn(inputFile);
       try{
           while(true){
               if(bin2.readBoolean())
                   text += 1;
               else
                   text += 0;
           }
       }catch(Exception e){
       }

       frequencyOfChar.clear();
       BinaryIn in = new BinaryIn(freqFile);
       String temp = in.readString();
       String [] encoded = temp.split("\n");

       for(int i =0; i < encoded.length; i++){
           String line = encoded[i];

           if(line.length() > 0){
               String [] arr = line.split(":");
               frequencyOfChar.put((char)Integer.parseInt(arr[0], 2), Integer.parseInt(arr[1]));
           }
       }

       //Make a tree
       decodeStrings="";
       createATree();

       decodeString(text);

       BinaryOut out2 = new BinaryOut(outputFile);
           out2.write(decodeStrings);
       out2.flush();
       out2.close();
    }


   public static void main(String[] args) throws FileNotFoundException, IOException {
      Huffman  huffman = new HuffmanSubmit();
		huffman.encode("alice30.txt", "alice.enc", "freq.txt");
		huffman.decode("alice.enc", "alice_new.txt", "freq.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   }

}
