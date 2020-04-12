package krypto;

import java.util.Arrays;

public class ciphers extends utilsCip {

    public static String cezar(String text, int shift, boolean encode) {
        String ans = "";

        for (int i = 0; i < text.length(); i++) {
            if (encode) {
                ans += mod(text.charAt(i) + shift);
            } else {
                ans += mod(text.charAt(i) - shift);
            }
        }
        return ans;
    }

    public static String vigener(String text, String key, boolean encode) {
        String ans = "";

        for (int i = 0; i < text.length(); i++) {
            if (encode) {
                ans += mod(text.charAt(i) + (key.charAt(i % key.length()) - 'A'));
            } else {
                ans += mod(text.charAt(i) - (key.charAt(i % key.length()) - 'A'));
            }
        }
        return ans;
    }

    public static String substitution(String text, String key, boolean encode) {
        String ans = "";

        for (int i = 0; i < text.length(); i++) {
            if (encode) {
                ans += key.charAt(text.charAt(i) - 'A');
            } else {
                for (int j = 0; j < 26; j++) {
                    if (text.charAt(i) == key.charAt(j)) {
                        ans += (char) ('A' + j);
                        break;
                    }
                }
            }
        }
        return ans;
    }

    public static String permutation(String text, String key, boolean encode) {
        String ans = "";
        int m = key.length();

        while (text.length() % m != 0) {
            text += 'A';
        }

        for (int i = 0; i < text.length() / m; i++) {
            if (encode) {
                for (int j = 0; j < m; j++) {
                    ans += text.charAt(i * m + key.charAt(j) - '1');
                }
            } else {
                for (int j = 0; j < m; j++) {
                    for (int k = 0; k < m; k++) {
                        if (key.charAt(k) - '1' == j) {
                            ans += text.charAt(i * m + k);
                        }
                    }
                }
            }
        }
        return ans;
    }

    public static int Vernam(int text, int key) {//encoding and decoding is the same so no need for encode parameter
        String ans = "";
        String textBin = Integer.toBinaryString(text);
        String keyBin = Integer.toBinaryString(key);

        while (textBin.length() < keyBin.length()) {
            textBin = "0" + textBin;
        }
        while (textBin.length() > keyBin.length()) {
            keyBin = "0" + keyBin;
        }

        for (int i = 0; i < textBin.length(); i++) {
            if ((textBin.charAt(i) == '1' && textBin.charAt(i) == '0') || (textBin.charAt(i) == '0' && textBin.charAt(i) == '1')) {
                ans += "1";
            } else {
                ans += "0";
            }
        }
        return Integer.parseInt(ans, 2);
    }

    public static int[] hill(int[] text, int[][] key1, int[] key2, int p, boolean encode) {
        int[] ans = new int[text.length];
        int n = key1.length;
        int temp;

        if (encode) {
            for (int i = 0; i < text.length; i++) {
                text[i] -= key2[i % key2.length];
            }
            for (int i = 0; i < key2.length; i++) {
                key2[i] = 0;
            }
        }

        for (int i = 0; i < text.length / n; i++) {

            for (int j = 0; j < n; j++) {
                temp = key2[j];

                for (int k = 0; k < key1[j].length; k++) {
                    temp += key1[j][k] * text[i * n + k];
                }
                temp = temp % p;
                if (temp < 0) {
                    temp += p;
                }
                ans[i * n + j] = temp;
            }
        }

        return ans;
    }

    public static boolean equal(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public static void findHill(int[] m1, int[] c1, int[] m2, int[] c2, int[] m3, int[] c3, int p, int d) {
        int[][] klucz1 = new int[d][d];
        int[] klucz2 = new int[d];

        for (int i = 0; i < Math.pow(p, d * (d + 1)); i++) {
            for (int j = 0; j < 3; j++) {
                klucz2[j] = i / (int) Math.pow(p, j) % p;
            }
            for (int j = 0; j < 3 * 3; j++) {
                klucz1[j / d][j % d] = i / (int) Math.pow(p, d + j) % p;
            }

            if (equal(hill(m1, klucz1, klucz2, p, true), c1) && equal(hill(m2, klucz1, klucz2, p, true), c2) && equal(hill(m3, klucz1, klucz2, p, true), c3)) {
                System.out.println(Arrays.toString(klucz1[0]));
                System.out.println(Arrays.toString(klucz1[1]));
                System.out.println(Arrays.toString(klucz2));
                System.out.println();
            }
        }
    }
}
