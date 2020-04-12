package krypto;

import java.math.BigInteger;
import java.util.*;

public class utilsAlg {

    public static long nwd(long a_, long b_) {
        if (a_ < 1 || b_ < 1) {
            throw new java.lang.Error("nwd(" + a_ + "," + b_ + ")");
        }
        BigInteger a = new BigInteger(Long.toString(a_)), b = new BigInteger(Long.toString(b_));
        return a.gcd(b).longValue();
    }

    public static long modInverse(long a_, long mod_) {
        BigInteger a = new BigInteger(Long.toString(a_)), mod = new BigInteger(Long.toString(mod_));
        return a.modInverse(mod).longValue();
    }

    public static long mod(long a_, char operant, long b_, long mod_) {
        BigInteger a = new BigInteger(Long.toString(a_)), b = new BigInteger(Long.toString(b_)), mod = new BigInteger(Long.toString(mod_));
        if (operant == '*') {
            return a.multiply(b).mod(mod).longValue();
        }
        if (operant == '+') {
            return a.add(b).mod(mod).longValue();
        }
        if (operant == '-') {
            return a.subtract(b).mod(mod).longValue();
        }

        throw new java.lang.Error("bad operant in mod");
    }

    public static long fi(long n, List<long[]> fact, boolean print) {
        long ans = 1;
        if (print) {
            System.out.print("fi(" + n + ") = ");
        }
        for (int i = 0; i < fact.size(); i++) {
            ans *= (fact.get(i)[0] - 1) * fact.get(i)[2] / fact.get(i)[0];
            if (print) {
                System.out.print((fact.get(i)[0] - 1) + "*" + (fact.get(i)[2] / fact.get(i)[0]) + " * ");
            }
        }
        if (print) {
            System.out.println("= " + ans);
        }
        return ans;
    }

    public static long modPow(long a_, long n, long mod_, boolean print) {
        BigInteger a = new BigInteger(Long.toString(a_)), mod = new BigInteger(Long.toString(mod_)), ans = BigInteger.ONE, temp = new BigInteger(a.toString());
        String bin = Long.toBinaryString(n);
        if (print) {
            System.out.println(n + "_10 = " + bin + "_2");
            System.out.print(a + "^(2^i): ");
        }

        for (int i = bin.length() - 1; i >= 0; i--) {
            if (bin.charAt(i) == '1') {
                ans = ans.multiply(temp).mod(mod);
                if (print) {
                    System.out.print("+");
                }
            }
            if (print) {
                System.out.print(temp + ", ");
            }
            temp = temp.modPow(BigInteger.TWO, mod);

        }
        if (print) {
            System.out.println();
            System.out.println(a_ + "^" + n + " = " + ans + " (mod " + mod_ + ")");
        }
        return ans.longValue();
    }

    public static long ord(long a_, long mod_) {
        BigInteger a = new BigInteger(Long.toString(a_)), mod = new BigInteger(Long.toString(mod_)), ans = BigInteger.ONE, b = a;
        while (!b.equals(BigInteger.ONE)) {
            b = (b.multiply(a)).mod(mod);
            ans = ans.add(BigInteger.ONE);
        }

        return ans.longValue();
    }

    public static long smallLog(long g_, long a, long mod_) {
        long ans = 1;
        BigInteger g = new BigInteger(Long.toString(g_)), mod = new BigInteger(Long.toString(mod_));
        BigInteger mem = g;
        while (g.longValue() != a) {
            g = g.multiply(mem).mod(mod);
            ans++;
        }
        return ans;
    }

    public static List<Long> powersMod(long a, long mod, boolean sort) {
        return powersMod(a, mod, mod, sort);
    }

    public static List<Long> powersMod(long a_, long mod_, long leng, boolean sort) {
        List<Long> ans = new ArrayList<Long>();
        BigInteger a = new BigInteger(Long.toString(a_));
        BigInteger mod = new BigInteger(Long.toString(mod_));
        BigInteger b = a;

        do {
            ans.add(b.longValue());
            b = (b.multiply(a)).mod(mod);
        } while (!b.equals(BigInteger.ONE) && ans.size() < leng);

        if (sort) {
            Collections.sort(ans);
        }
        return ans;
    }

    public static List<long[]> primeFact(long a, boolean print) {// finds prime factors of a number "a"
        List<long[]> ans = new ArrayList<long[]>();
        long n = a, i = 2, max = (long) Math.sqrt(n);
        while (i <= max) {
            if (n % i == 0) {
                ans.add(new long[3]);
                ans.get(ans.size() - 1)[0] = i;
                ans.get(ans.size() - 1)[1] = 1l;
                ans.get(ans.size() - 1)[2] = i;

                n /= i;
                max = (long) Math.sqrt(n);
            }
            while (n % i == 0) {
                ans.get(ans.size() - 1)[1]++;
                ans.get(ans.size() - 1)[2] *= i;

                n /= i;
                max = (long) Math.sqrt(n);
            }
            i++;
        }
        if (n != 1) {
            ans.add(new long[3]);
            ans.get(ans.size() - 1)[0] = n;
            ans.get(ans.size() - 1)[1] = 1l;
            ans.get(ans.size() - 1)[2] = n;
        }
        if (print) {
            System.out.print(a + " = " + ans.get(0)[0] + "^" + ans.get(0)[1] + " (" + ans.get(0)[2] + ")");
            for (int j = 1; j < ans.size(); j++) {
                System.out.print(" * " + ans.get(j)[0] + "^" + ans.get(j)[1] + " (" + ans.get(j)[2] + ")");
            }
            System.out.println();
        }
        return ans;
    }

    public static long chinese(List<Long> a, List<Long> mods, boolean print) {//linear congruent equations with mods pairwise coprime
        long ans, temp;

        if (print) {
            System.out.println("mod = " + mods);
        }
        for (int i = 0; i < a.size(); i++) {
            if (print) {
                System.out.println("a = " + a);
            }
            for (int j = i + 1; j < a.size(); j++) {
                temp = mods.get(i) % mods.get(j);
                a.set(j, a.get(j) - a.get(i));//a[j] = mod(a[j] - a[i], mods[j]);
                while (a.get(j) < 0) {
                    a.set(j, a.get(j) + mods.get(j));
                }

                while (a.get(j) % temp != 0) {
                    a.set(j, a.get(j) + mods.get(j));
                }

                a.set(j, a.get(j) / temp);
            }
        }

        ans = a.get(a.size() - 1);

        for (int i = a.size() - 2; i >= 0; i--) {
            ans = a.get(i) + mods.get(i) * ans;
        }

        return ans;
    }

    public static long[] congruent(long a, long b, long mod, boolean print) {//ax = b (mod mod)
        if (print) {
            System.out.print(a + "x = " + b + " (mod " + mod + ") <=> ");
        }
        a = a % mod >= 0 ? a % mod : ((a % mod) + mod);
        b = b % mod >= 0 ? b % mod : ((b % mod) + mod);
        if (print) {
            System.out.println(a + "x = " + b + " (mod " + mod + ")");
        }
        long nwd = nwd(a, mod), i = 0, bprev;

        if (b % nwd == 0) {
            a /= nwd;
            b /= nwd;
            mod /= nwd;
            bprev = b;

            while (a != 1) {
                nwd = nwd(a, b);
                a /= nwd;
                b /= nwd;

                if (nwd > 1 && print) {
                    System.out.print(a * nwd + "x = " + bprev + " + " + i + "*" + mod + " (mod " + mod + ") |/" + nwd + " <=> ");
                    i = 0;
                    bprev = b;
                }
                if (a > 1) {
                    b += mod;
                    i++;
                }
            }
            if (print) {
                System.out.println("x = " + b + " (mod " + mod + ")");
            }
            return new long[]{b, mod};
        } else {
            throw new java.lang.Error("no solution for " + a + "x = " + b + " (mod " + mod + ")");
        }
    }
}
