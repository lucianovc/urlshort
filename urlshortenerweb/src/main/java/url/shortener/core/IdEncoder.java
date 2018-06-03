package url.shortener.core;

public class IdEncoder {

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_@";
    public static final int ALPHSIZE = ALPHABET.length();

    public static String encode(int num) {
        StringBuilder str = new StringBuilder();
        if (num < 0) num = -num;
        while (num > 0) {
            str.insert(0, ALPHABET.charAt(num % ALPHSIZE));
            num = num / ALPHSIZE;
        }
        return str.toString();
    }

    public static int decode(String str) {
        int num = 0;
        for (int i = 0; i < str.length(); i++) {
            num = num * ALPHSIZE + ALPHABET.indexOf(str.charAt(i));
        }
        return num;
    }

    public static void main(String[] a) {
        System.out.println(ALPHSIZE);
        System.out.println(encode(ALPHSIZE));
        System.out.println(decode("31h2Fs"));
    }

}
