/**************************************
 * Γεώργιος-Κρίτων Γεωργίου - p3150020
 * Αθανάσιος Μέμτσας - p3150103
 * Εργασία 1, Τεχνητή Νοημοσύνη
 * Νοέμβριος 2017
**************************************/

package Converters;


public class ByteOperations { //converting binary arrays to string and int and back
    public static int byteArrayToInt(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for(byte b : a) {
            sb.append(b);
        }
        return Integer.parseInt(sb.toString(), 2);
    }

    public static String byteArrayToString(byte[] a) {
        return String.valueOf(byteArrayToInt(a));
    }

    public static byte[] intToByteArray(int a, int length) {
        String s = Integer.toBinaryString(a);
        int sLength = s.length(); //needs to be predefined for for-loop
        for (int i=0; i < length - sLength; ++i) {
            s = new StringBuilder("0").append(s).toString();
        }

        byte[] res = new byte[length];

        for(int i = 0; i < s.length(); i++) {
            res[i] = (byte) Integer.parseInt(String.valueOf(s.charAt(i)));
        }
        return res;
    }
    public static byte[] stringToByteArray(String a, int length) {
        return intToByteArray(Integer.parseInt(a), length);
    }

}
