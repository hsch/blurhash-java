package io.trbl.blurhash;

final class Base83 {

    static final char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#$%*+,-.:;=?@[]^_{|}~"
            .toCharArray();

    private static int indexOf(char[] a, char key) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return -1;
    }

    static String encode(long value, int length) {
        char[] buffer = new char[length];
        int exp = 1;
        for (int i = 1; i <= length; i++, exp *= 83) {
            int digit = (int)(value / exp % 83);
            buffer[length - i] = ALPHABET[digit];
        }
        return new String(buffer);
    }

    static int decode(String value) {
        int result = 0;
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            result = result * 83 + indexOf(ALPHABET, chars[i]);
        }
        return result;
    }

    private Base83() {
    }
}
