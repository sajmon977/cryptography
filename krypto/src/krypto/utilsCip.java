package krypto;

public class utilsCip{
    
    public static int mod(int x, int mod) {
        x = x % mod;
        if (x < 0) {
            x += mod;
        }
        return x;
    }

    public static String tekst(String tekst) {
        String ans = "";
        for (int i = 0; i < tekst.length(); i++) {
            if (tekst.charAt(i) != ' ') {
                if ('a' <= tekst.charAt(i) && tekst.charAt(i) <= 'z') {
                    ans += (char) (tekst.charAt(i) + 'A' - 'a');
                } else {
                    ans += tekst.charAt(i);
                }
            }
        }
        return ans;
    }

    public static char mod(int x) {
        return (char) (mod(x - 'A', 26) + 'A');
    }
}
