package krypto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class log extends utilsAlg {

    static ArrayList<Long> S1, S2;

    public static long shanks(long mod, long g, long a) {
        return shanks(mod, g, a, true);
    }

    public static long shanks(long mod, long g, long a, boolean print) {//finds discrete logarithm (log_g(a) modulo mod, g^shanks = a modulo mod)
        ArrayList<Long> smallSteps = new ArrayList<Long>(), bigSteps = new ArrayList<Long>();
        int n = 1 + (int) Math.sqrt(mod);

        smallSteps.add(1l);
        bigSteps.add(1l);

        smallSteps.addAll(powersMod(g, mod, n));
        long g_minus = inverse(smallSteps.get(smallSteps.size() - 1), mod);
        bigSteps.addAll(powersMod(g_minus, mod, n));

        for (int i = 0; i < n; i++) {
            bigSteps.set(i, (bigSteps.get(i) * a) % mod);
        }

        if (print) {
            System.out.println("p=" + mod + ", g=" + g + ", a=" + a);
            System.out.println("n = " + n);
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

    public static long pohligHellman(long mod, long g, long a) {
        return pohligHellman(mod, g, a, true);
    }

    public static long pohligHellman(long mod, long g, long a, boolean print) {//finds discrete logarithm (log_g(a) modulo mod, g^pohlighellman = a modulo mod)
        long fi = fi(mod, print);
        ArrayList<long[]> fact = primeFact(fi, print);
        ArrayList<Long>[] pows = new ArrayList[4];//0 p_i^alfa_i;    1 g^(n/pows[0]);    2 a^(n/pows[0]);    3 log_pows[1](pows[2])
        for (int i = 0; i < pows.length; i++) {
            pows[i] = new ArrayList<Long>();
        }
        for (int i = 0; i < fact.size(); i++) {

            pows[0].add(fact.get(i)[2]);
            pows[1].add(modPow(g, fi / fact.get(i)[2], mod, print));
            pows[2].add(modPow(a, fi / fact.get(i)[2], mod, print));
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

    public static long rhoPollard(long mod, long g, long a, boolean random) {
        return rhoPollard(mod, g, a, random, true);
    }

    public static long rhoPollard(long mod, long g, long a, boolean random, boolean print) {//finds discrete logarithm (log_g(a) modulo mod, g^rhoPollard = a modulo mod)
        S1 = new ArrayList<Long>((int) mod / 3);
        S2 = new ArrayList<Long>((int) mod / 3);

        for (long i = 0; i < mod; i++) {
            if (random) {
                Random gen = new Random();
                int rand = gen.nextInt(3);
                if (rand == 0 || i == 1) {//i % 3 == 1
                    S1.add(i);
                } else if (rand == 1) {//i % 3 == 0
                    S2.add(i);
                }
            } else {
                if (i % 3 == 1) {
                    S1.add(i);
                } else if (i % 3 == 0) {
                    S2.add(i);
                }
            }
        }

        if (print) {
            System.out.println("S1 = " + S1);
            System.out.println("S2 = " + S2);
        }

        LinkedList<long[]> x = new LinkedList<long[]>();//x,u,v
        long fi = fi(mod, print);
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
                x.add(rPFunc(x.getLast(), mod, fi, g, a, print));
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
            pow = modMult(pow, modPow, mod);
            if (print) {
                System.out.println(pow + " (mod " + mod + ")");
            }
        }

        throw new java.lang.Error("no solution");
    }

    private static long[] rPFunc(long[] x, long mod, long fiMod, long g, long a, boolean print) {
        long[] ans = new long[3];//x,u,v
        if (S1.contains(x[0])) {//S1
            ans[0] = modMult(a, x[0], mod);//x
            ans[1] = x[1] + 1 % fiMod;//u
            ans[2] = x[2];//v
        } else if (S2.contains(x[0])) {//S2
            ans[0] = modMult(x[0], x[0], mod);//x
            ans[1] = modMult(2, x[1], fiMod);//u
            ans[2] = modMult(2, x[2], fiMod);//v
        } else {//S3
            ans[0] = modMult(g, x[0], mod);//x
            ans[1] = x[1];//u
            ans[2] = x[2] + 1 % fiMod;//v
        }
        if (print) {
            System.out.println(ans[0] + ", " + ans[1] + ", " + ans[2]);
        }
        return ans;
    }
}
