package krypto;

import java.util.ArrayList;

public class utils {

    public static long gcd(long a, long b) {
        if (a < 0 || b < 0) {
            throw new java.lang.Error("gcd(" + a + "," + b + ") cant be calculated");
        }
        while (a != 0 && b != 0) {
            if (a > b) {
                a -= a / b * b;
            } else {
                b -= b / a * a;
            }
        }
        return (a == 0) ? b : a;
    }

    public static long inverse(long n, long mod) {
        if (mod < 2) {
            throw new java.lang.Error("modulo = " + mod + " < 2");
        }
        long a = (n % mod + mod) % mod, b = mod, gcd, temp, invPrev = 0, inv = 1;

        while (a != 0 && b != 0) {
            temp = invPrev;
            invPrev = inv;
            if (a > b) {
                inv = temp - a / b * inv;
                a -= a / b * b;
            } else {
                inv = temp - b / a * inv;
                b -= b / a * a;
            }
        }
        gcd = (a == 0) ? b : a;
        if (gcd != 1) {
            throw new java.lang.Error("gcd(" + n + "," + mod + ") = " + gcd + " != 1");
        }
        return (invPrev % mod + mod) % mod;
    }

    public static ArrayList<long[]> primeFact(long a, boolean print) {// finds prime factors of a number "a"
        if (a < 1) {
            throw new java.lang.Error("cant find prime factors for " + a);
        }
        ArrayList<long[]> ans = new ArrayList<long[]>();
        long n = a, max = (long) Math.sqrt(n);

        for (int i = 2; i <= max; i++) {
            if (n % i == 0) {
                ans.add(new long[3]);
                ans.get(ans.size() - 1)[0] = i;
                ans.get(ans.size() - 1)[1] = 1;
                ans.get(ans.size() - 1)[2] = i;

                n /= i;
                while (n % i == 0) {
                    ans.get(ans.size() - 1)[1]++;
                    ans.get(ans.size() - 1)[2] *= i;

                    n /= i;
                }
                max = (long) Math.sqrt(n);
            }
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

    public static long fi(long n, boolean print) {
        ArrayList<long[]> fact = primeFact(n, false);
        long ans = (fact.get(0)[0] - 1) * fact.get(0)[2] / fact.get(0)[0];
        if (print) {
            System.out.print("fi(" + n + ") = " + (fact.get(0)[0] - 1) + "*" + (fact.get(0)[2] / fact.get(0)[0]));
        }

        for (int i = 1; i < fact.size(); i++) {
            ans *= (fact.get(i)[0] - 1) * fact.get(i)[2] / fact.get(i)[0];
            if (print) {
                System.out.print(" * " + (fact.get(i)[0] - 1) + "*" + (fact.get(i)[2] / fact.get(i)[0]));
            }
        }
        if (print) {
            System.out.println(" = " + ans);
        }
        return ans;
    }

    public static long[] congruent(long a_, long b_, long mod, boolean print) {//ax = b (mod mod)
        long a = a_ % mod >= 0 ? a_ % mod : ((a_ % mod) + mod), b = b_ % mod >= 0 ? b_ % mod : ((b_ % mod) + mod);
        if (print) {
            System.out.print(a_ + "x = " + b_ + " (mod " + mod + ") <=> " + a + "x = " + b + " (mod " + mod + ") <=> ");
        }
        long gcd = gcd(a, mod), i = 0;

        if (b % gcd == 0) {
            a /= gcd;
            b /= gcd;
            mod /= gcd;

            while (a != 1) {
                gcd = gcd(a, b);
                a /= gcd;
                b /= gcd;

                if (gcd > 1 && print) {
                    System.out.print(a * gcd + "x = " + b * gcd + " + " + i + "*" + mod + " (mod " + mod + ") |/" + gcd + " <=> ");
                    i = 0;
                }
                b += mod;
                i++;
            }
            b = b % mod;

            if (print) {
                System.out.println("x = " + b + " (mod " + mod + ")");
            }
            return new long[]{b, mod};
        } else {
            throw new java.lang.Error("no solution for " + a + "x = " + b + " (mod " + mod + ") because of gcd(" + a + "," + mod + ") = " + gcd);
        }
    }

    public static long chinese(ArrayList<Long> a, ArrayList<Long> mods, boolean print) {//linear congruent equations with mods pairwise coprime
        long ans, temp, k = mods.get(a.size() - 1);
        String str = "";

        for (int i = 0; i < a.size(); i++) {
            if (print) {
                for (int j = i; j < a.size(); j++) {
                    System.out.println("x" + (i == 0 ? "" : "_" + i) + " = " + a.get(j) + " (mod " + mods.get(j) + ")");
                }
                System.out.println("x" + (i == 0 ? "" : "_" + i) + " = " + a.get(i) + " + " + mods.get(i) + "*x_" + (i + 1));
                System.out.println();
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
        str += "(" + ans + " + " + mods.get(a.size() - 1) + "*k)";
        for (int i = a.size() - 2; i >= 0; i--) {
            ans = a.get(i) + mods.get(i) * ans;
            k *= mods.get(i);
            str = "(" + a.get(i) + " + " + mods.get(i) + "*" + str + ")";
        }
        if (print) {
            System.out.println("x = " + str + " = " + ans + " + " + k + "*k");
        }
        return ans;
    }
}
