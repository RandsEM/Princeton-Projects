/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class testing {
    public static void main(String[] args) {
        StringBuilder[] array = new StringBuilder[2];
        array[0] = new StringBuilder("Jew");
        array[1] = new StringBuilder("Ass");
        Arrays.sort(array);
        for (StringBuilder s : array) {
            System.out.println(s);
        }
    }
}
