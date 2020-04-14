package krypto;

import java.math.BigInteger;
import java.util.ArrayList;

public class utilsAlg extends utils {//extends utils with methods that use BigInteger (to make sure that return value didn't go beyond range of long)

    public static long modMult(long a_, long b_, long mod_) {
        BigInteger a = new BigInteger(Long.toString(a_)), b = new BigInteger(Long.toString(b_)), mod = new BigInteger(Long.toString(mod_));

        return a.multiply(b).mod(mod).longValue();
    }

    public static long modPow(long a_, long n, long mod_, boolean print) {
        BigInteger a = new BigInteger(Long.toString(a_)), mod = new BigInteger(Long.toString(mod_)), ans = BigInteger.ONE, temp = new BigInteger(a.toString());
        String bin = Long.toBinaryString(n), forPrint = "";
        if (print) {
            System.out.println(n + "_10 = " + bin + "_2");
            System.out.print(a + "^(2^i): ");
        }

        for (int i = bin.length() - 1; i >= 0; i--) {
            if (bin.charAt(i) == '1') {
                ans = ans.multiply(temp).mod(mod);
                forPrint += temp + ((i == 0) ? "" : " * ");
            }
            if (print) {
                System.out.print(temp + ((i == 0) ? "" : ", "));
            }
            temp = temp.modPow(BigInteger.TWO, mod);

        }
        if (print) {
            System.out.println();
            System.out.println(a_ + "^" + n + " = " + forPrint + " = " + ans + " (mod " + mod_ + ")");
        }
        return ans.longValue();
    }

    public static long ord(long a, long mod, boolean print) {
        primeFact(a, print);
        ArrayList<long[]> factMod = primeFact(mod, print);
        long ans = 0, fi, carmi = 1;

        if (print) {
            System.out.println("Carmichael for " + mod + ":");
        }
        for (int i = 0; i < factMod.size(); i++) {
            fi = ((factMod.get(i)[0] - 1) * factMod.get(i)[2]) / factMod.get(i)[0];
            if (factMod.get(i)[0] == 2 && factMod.get(i)[1] >= 3) {
                fi /= 2;
            }
            if (print) {
                System.out.print("lambda(" + factMod.get(i)[0] + "^" + factMod.get(i)[1] + ") = ");
                primeFact(fi, print);
            }
            carmi = (carmi * fi) / gcd(carmi, fi);
        }
        if (print) {
            System.out.print("lambda(" + mod + ") = ");
        }
        ArrayList<long[]> factCar = primeFact(carmi, print);
        ans = carmi;

        for (int i = 0; i < factCar.size(); i++) {
            if (modPow(a, ans / factCar.get(i)[0], mod, print) == 1) {
                ans /= factCar.get(i)[0];
                i--;
                if (print) {
                    System.out.println();
                    System.out.print("check if this is ord: ");
                    primeFact(ans, print);
                }
            }
        }

        return ans;
    }

    public static long smallLog(long g_, long a, long mod_) {
        long ans = 1;
        BigInteger g = new BigInteger(Long.toString(g_)), mod = new BigInteger(Long.toString(mod_));
        BigInteger temp = g;
        while (g.longValue() != a) {
            g = g.multiply(temp).mod(mod);
            ans++;
        }
        return ans;
    }

    public static ArrayList<Long> powersMod(long a, long mod) {
        return powersMod(a, mod, mod);
    }

    public static ArrayList<Long> powersMod(long a_, long mod_, long leng) {
        ArrayList<Long> ans = new ArrayList<Long>();
        BigInteger a = new BigInteger(Long.toString(a_));
        BigInteger mod = new BigInteger(Long.toString(mod_));
        BigInteger b = a;

        do {
            ans.add(b.longValue());
            b = (b.multiply(a)).mod(mod);
        } while (!b.equals(BigInteger.ONE) && ans.size() < leng);

        return ans;
    }
}
