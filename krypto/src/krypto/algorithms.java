package krypto;

import java.util.*;
import java.math.BigInteger;

public class algorithms extends utilsAlg {

    public static long gauss(long mod, boolean print) { //finds primitive root modulo mod 
        long nwd, ordA, ordB, b, a = 2;
        List<Long> powA;

        while (true) {
            powA = powersMod(a, mod, true);
            ordA = powA.size() + 1;
            if (print) {
                System.out.println("powers of a = " + a + " till one: " + powersMod(a, mod, false));
                System.out.println("ord_" + mod + "(" + a + ")" + " = " + ordA);
            }
            if (ordA == mod - 1) {//stop condition
                break;
            } else {
                b = 2;
                while (b < mod && powA.get((int) (b - 2)) == b) {
                    b++;
                }
                ordB = ord(b, mod);
                if (print) {
                    System.out.println("powers of b = " + b + " till one: " + powersMod(b, mod, false));
                    System.out.println("ord_" + mod + "(" + b + ")" + " = " + ordB);
                }
                if (ordB == mod - 1) {
                    a = b;
                    break;
                } else {
                    nwd = nwd(ordA, ordB);
                    if (print) {
                        System.out.println("a1 = " + a + ", b1 = " + b + "^" + nwd + " = " + modPow(b, nwd, mod, print));
                    }
                    a = mod(a, '*', modPow(b, nwd, mod, print), mod);
                }
            }
        }

        return a;
    }

    public static long shanks(long mod, long g, long a, boolean print) {//finds discrete logarithm (log_g(a) modulo mod, g^shanks = a modulo mod)
        List<Long> smallSteps = new ArrayList<Long>(), bigSteps = new ArrayList<Long>();
        int n = 1 + (int) Math.sqrt(mod);

        smallSteps.add(1l);
        bigSteps.add(1l);

        BigInteger g_ = new BigInteger(Long.toString(g)), n_ = new BigInteger(Long.toString(-n)), mod_ = new BigInteger(Long.toString(mod));
        long g_minus = g_.modPow(n_, mod_).longValue();
        smallSteps.addAll(powersMod(g, mod, n, false));
        bigSteps.addAll(powersMod(g_minus, mod, n, false));

        System.out.println(smallSteps);
        System.out.println(bigSteps);
        for (int i = 0; i < n; i++) {
            bigSteps.set(i, (bigSteps.get(i) * a) % mod);
        }

        if (print) {
            System.out.println("p=" + mod + ", g=" + g + ", a=" + a);
            System.out.println("n: " + n);
            System.out.println("small steps: " + smallSteps);
            System.out.println("g^(-n): " + g_minus);
            System.out.println("big steps: " + bigSteps);
        }

        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < n + 1; j++) {
                if (smallSteps.get(i).equals(bigSteps.get(j))) {
                    if (print) {
                        System.out.println("x = " + i + "+" + j + "*" + n + " = " + (i + j * n));
                    }
                    return i + n * j;
                }
            }
        }
        return 0;
    }

    public static long pohligHellman(long mod, long g, long a, boolean print) {//finds discrete logarithm (log_g(a) modulo mod, g^pohlighellman = a modulo mod)
        List<long[]> fact = primeFact(mod - 1, print);
        List<Long>[] pows = new ArrayList[4];//0 p_i^alfa_i;    1 g^(n/pows[0]);    2 a^(n/pows[0]);    3 log_pows[1](pows[2])
        for (int i = 0; i < pows.length; i++) {
            pows[i] = new ArrayList<Long>();
        }
        for (int i = 0; i < fact.size(); i++) {

            pows[0].add(fact.get(i)[2]);
            pows[1].add(modPow(g, (mod - 1) / fact.get(i)[2], mod, print));
            pows[2].add(modPow(a, (mod - 1) / fact.get(i)[2], mod, print));
            pows[3].add(smallLog(pows[1].get(i), pows[2].get(i), mod));
        }
        if (print) {
            String[] tekst = {"n_i = p_i^alfa_i:    ", "g_i = g^((p-1)/n_i): ", "a_i = a^((p-1)/n_i): ", "log_(g_i) (a_i):     "};
            for (int i = 0; i < pows.length; i++) {
                System.out.println(tekst[i] + pows[i]);
            }
        }

        return chinese(pows[3], pows[0], print);
    }

    public static long rhoPollard(long mod, long g, long a, boolean print) {//finds discrete logarithm (log_g(a) modulo mod, g^rhoPollard = a modulo mod)
        LinkedList<long[]> x = new LinkedList<long[]>();//x,u,v
        long fi = fi(mod, primeFact(mod,print),print);
        int i = 1;
        x.add(new long[]{1, 0, 0});//0
        if (print) {
            System.out.println("i: x_i, u_i, v_i");
        }
        do {
            for (int j = 0; j < 2; j++) {
                if (print) {
                    System.out.print(i++ + ": ");
                }
                x.add(rPFunc(x.getLast(), mod, g, a, print));
            }
            x.removeFirst();
        } while (x.getLast()[0] != x.getFirst()[0]);
        long[] solution = congruent(x.getFirst()[1] - x.getLast()[1], x.getLast()[2] - x.getFirst()[2], fi, print);
        long modPow = modPow(g, solution[1], mod, print), pow = modPow(g, solution[0], mod, print), ans = solution[0];

        while (ans < fi) {
            if (pow == a) {
                return ans;
            }
            ans += solution[1];
            if (print) {
                System.out.print(g + "^" + ans + " = " + pow + "*" + modPow + " = ");
            }
            pow = mod(pow, '*', modPow, mod);
            if (print) {
                System.out.println(pow + " (mod " + mod + ")");
            }
        }

        throw new java.lang.Error("no solution");
    }

    private static long[] rPFunc(long[] x, long mod, long g, long a, boolean print) {
        //S1 = {x=1 (mod 3)},S2 = {x=0 (mod 3)},S3 = {x=2 (mod 3)}
        long[] ans = new long[3];//x,u,v
        if (x[0] % 3 == 1) {//S1 x[0] < mod/3
            ans[0] = mod(a, '*', x[0], mod); //ax (mod)
            ans[1] = mod(x[1], '+', 1, mod - 1);//u+1 (mod -1)
            ans[2] = x[2];//v
        } else if (x[0] % 3 == 0) {//S2 mod/3 <= x[0] && x[0] < 2*mod/3
            ans[0] = mod(x[0], '*', x[0], mod);//xx
            ans[1] = mod(2, '*', x[1], mod - 1);//2u (mod -1)
            ans[2] = mod(2, '*', x[2], mod - 1);//2v (mod -1)
        } else {//S3
            ans[0] = mod(g, '*', x[0], mod);//gx
            ans[1] = x[1];//u
            ans[2] = mod(x[2], '+', 1, mod - 1);//v+1 (mod -1)
        }
        if (print) {
            System.out.println(ans[0] + ", " + ans[1] + ", " + ans[2]);
        }
        return ans;
    }

    public static long bigPowMod(long a_, long n_, long mod_, boolean print) {
        primeFact(a_, print);
        List<long[]> fact = primeFact(mod_, print);
        List<Long> a = new ArrayList<Long>(), n = new ArrayList<Long>(), mod = new ArrayList<Long>();
        long fi;

        for (int i = 0; i < fact.size(); i++) {
            mod.add(fact.get(i)[2]);
            a.add(a_ % mod.get(i));
            fi = (fact.get(i)[0] - 1) * fact.get(i)[2] / fact.get(i)[0];
            n.add(n_ % fi);
            if (print) {
                System.out.println("fi(" + fact.get(i)[2] + ") = " + fi + ", x = " + a.get(i) + "^" + n.get(i) + " (mod " + fact.get(i)[2] + ")");
            }

            if (!a.get(i).equals(0l) && n.get(i).equals(0l)) {
                a.set(i, 1l);
            } else if (!a.get(i).equals(0l) && !a.get(i).equals(1l)) {
                a.set(i, modPow(a.get(i), n.get(i), fact.get(i)[2], print));
            }
        }

        return chinese(a, mod, true);
    }

    public static long bigOrdMod(long a, long mod, boolean print) {
        primeFact(a, print);
        List<long[]> factMod = primeFact(mod, print);
        long ans = 0, fi, carmi = 1;

        if (print) {
            System.out.println("Carmichaela for " + mod + ":");
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
            carmi = (carmi * fi) / nwd(carmi, fi);
        }
        if (print) {
            System.out.print("lambda(" + mod + ") = ");
        }
        List<long[]> factCar = primeFact(carmi, print);
        ans = carmi;

        for (int i = 0; i < factCar.size(); i++) {
            if (1 == modPow(a, ans / factCar.get(i)[0], mod, print)) {
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
}
