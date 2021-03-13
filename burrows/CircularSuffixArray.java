/* *****************************************************************************
 *  Name: Darren
 *  Date:
 *  Description:
 **************************************************************************** */
public class CircularSuffixArray {
    private String string;
    private Suffix[] suffixes;

    private static class Suffix {
        private int start; // don't want to generate new string, keep the start

        public Suffix(int start) {
            this.start = start;
        }


        public int getStart() {
            return this.start;
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        this.string = s;
        // compute all prefix and enter
        this.suffixes = new Suffix[s.length()];
        for (int i = 0; i < s.length(); i++) {
            Suffix current = new Suffix(i);
            this.suffixes[i] = current;
        }
        CircularSuffixArray.sort(this.suffixes, 0, s.length() - 1, 0, s.length(), s);
    }

    private static int charAt(String s, int current) {
        if (current >= s.length()) {
            return -1;
        }
        return s.charAt(current);
    }

    private static void exchange(Suffix[] a, int first, int second) {
        Suffix saved = a[first];
        a[first] = a[second];
        a[second] = saved;
    }

    private static void sort(Suffix[] a, int lo, int hi, int current, int stringLength, String s) {
        if (hi <= lo) {
            return;
        }
        int lessThan = lo;
        int greaterThan = hi;
        String part = s;
        int partition;
        if (current + a[lo].getStart() >= stringLength) {
            partition = charAt(part, current + a[lo].getStart() - stringLength);
        }
        else {
            partition = charAt(part, current + a[lo].getStart());
        }
        int i = lo + 1;
        while (i <= greaterThan) {
            String currentString = s;
            int c;
            if (current + a[i].getStart() >= stringLength) {
                c = charAt(currentString, current + a[i].getStart() - stringLength);
            }
            else {
                c = charAt(currentString, current + a[i].getStart());
            }
            if (c < partition) {
                CircularSuffixArray.exchange(a, i, lessThan);
                lessThan++;
                i++;
            }
            else if (c > partition) {
                CircularSuffixArray.exchange(a, i, greaterThan);
                greaterThan--;
            }
            else {
                i++;
            }
        }
        sort(a, lo, lessThan - 1, current, stringLength, s);
        if (partition >= 0) {
            sort(a, lessThan, greaterThan, current + 1, stringLength, s);
        }
        sort(a, greaterThan + 1, hi, current, stringLength, s);
    }


    // length of s
    public int length() {
        return this.string.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= this.string.length()) {
            throw new IllegalArgumentException();
        }
        return this.suffixes[i].getStart();
    }

    /*
    private static String printSuffix(Suffix s) {
        StringBuilder sb = new StringBuilder();
        if (s.getStart() == 0) {
            return s.getString();
        }
        else {
            for (int i = s.getStart(); i < s.getString().length(); i++) {
                sb.append(s.getString().charAt(i));
            }
            for (int i = 0; i < s.getStart(); i++) {
                sb.append(s.getString().charAt(i));
            }
        }
        return sb.toString();
    }
     */

    // unit testing (required)
    public static void main(String[] args) {
        String lmao = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(lmao);
        for (int i = 0; i < lmao.length(); i++) {
            System.out.println(csa.index(i));
        }
        System.out.println(csa.length());
    }
}

