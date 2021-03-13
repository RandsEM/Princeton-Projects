/* *****************************************************************************
 *  Name: Darren
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() { // transform screwed
        String input = BinaryStdIn.readString();
        BinaryStdIn.close();
        CircularSuffixArray csa = new CircularSuffixArray(input);

        // get first
        for (int i = 0; i < input.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
            }
        }
        for (int i = 0; i < input.length(); i++) {
            int originalIndex = csa.index(i);
            if (originalIndex == 0) {
                BinaryStdOut.write(input.charAt(input.length() - 1));
            }
            else {
                BinaryStdOut.write(input.charAt(originalIndex - 1));
            }
        }
        BinaryStdOut.flush();
    }


    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int R = 256;
        int first = BinaryStdIn.readInt();
        String last = BinaryStdIn.readString();
        BinaryStdIn.close();
        // key indexed counting
        int[] count = new int[R + 1];
        // compute the count
        for (int i = 0; i < last.length(); i++) {
            count[last.charAt(i) + 1]++;
        }
        // transform into indices
        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }

        int[] nextArray = new int[last.length()];
        char[] aux = new char[last.length()];
        for (int i = 0; i < last.length(); i++) {
            nextArray[count[last.charAt(i)]] = i;
            aux[count[last.charAt(i)]] = last.charAt(i);
            count[last.charAt(i)]++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(aux[first]);
        for (int i = 0; i < last.length() - 1; i++) {
            sb.append(aux[nextArray[first]]);
            first = nextArray[first];
        }
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.flush();
    }


    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
