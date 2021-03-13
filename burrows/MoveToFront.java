import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/* *****************************************************************************
 *  Name: Darren
 *  Date:
 *  Description:
 **************************************************************************** */
public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int R = 256;
        char c;
        char[] ordered = new char[R];
        for (int i = 0; i < R; i++) {
            ordered[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            c = BinaryStdIn.readChar();
            for (int i = 0; i < R; i++) {
                // found the char
                if (ordered[i] == c) {
                    BinaryStdOut.write((byte) i);
                    // move to front
                    for (int j = i; j > 0; j--) {
                        ordered[j] = ordered[j - 1];
                    }
                    // set first to the char
                    ordered[0] = c;
                    break;
                }
            }
        }
        BinaryStdIn.close();
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int R = 256;
        char[] ordered = new char[R];
        for (int i = 0; i < R; i++) {
            ordered[i] = (char) i;
        }
        char c;
        while (!BinaryStdIn.isEmpty()) {
            c = BinaryStdIn.readChar();
            char saved = ordered[(int) c];
            BinaryStdOut.write(saved);
            for (int i = (int) c; i > 0; i--) {
                ordered[i] = ordered[i - 1];
            }
            ordered[0] = saved;
        }
        BinaryStdOut.flush();
        BinaryStdIn.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            MoveToFront.encode();
        }
        else if (args[0].equals("+")) {
            MoveToFront.decode();
        }
    }

}
