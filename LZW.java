/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/
package cs1501_lzw_lab;

public class LZW {
    private static final int R = 256; // number of input chars
    private static final int L = 512; // number of codewords = 2^W
    private static final int W = 9; // codeword width

    private static int codewordlength = L;
    private static int codewordbits = W;
    private static Queue<Integer> bitQ = new Queue<Integer>();
    

    public static void compress() {
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R + 1; // R is codeword for EOF

        
        
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input); // Find max prefix match s.
            BinaryStdOut.write(st.get(s), codewordbits); // Print s's encoding.
            int t = s.length();

           
            if( t < input.length() && codewordbits < 16 && code >= codewordlength){
                codewordbits++;
                codewordlength = 1 << codewordbits; 
            }

            if (t < input.length() && code < codewordlength)  { // Add s to symbol table.
                bitQ.enqueue(codewordbits);
                st.put(input.substring(0, t + 1), code++);
            }    

            
            input = input.substring(t); // Scan past s in input.
        }
        BinaryStdOut.write(R, codewordbits);
        BinaryStdOut.close();
    }

    public static void expand() {
        int y = 0;
        String[] st = new String[codewordlength];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = ""; // (unused) lookahead for EOF

        
        int codeword = BinaryStdIn.readInt(9);
        if (codeword == R)
            return; // expanded message is empty string

        String val = st[codeword];
        
       
        
       
        
        int currentBit = 9;
        while (true) {
            y++;
            BinaryStdOut.write(val);
           
            // x = bitQ.dequeue();
            
            if((256 + y) >= (1 << currentBit) && currentBit < 16){
                currentBit++;
            }

            
            codeword = BinaryStdIn.readInt(currentBit);
            if (codeword == R)
                break;
            String s = st[codeword];
            if (i == codeword)
                s = val + val.charAt(0); // special case hack
            if (i < codewordlength){
                
                st[i++] = val + s.charAt(0);
            }
            val = s;
        }
        BinaryStdOut.close();
    }
}
