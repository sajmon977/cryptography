package krypto;

import java.util.ArrayList;
import java.util.Collections;

public class algorithms extends utilsAlg {

    public static long gauss(long mod) {
        return gauss(mod, true);
    }

    public static long gauss(long mod, boolean print) {//finds primitive root modulo mod 
        long gcd, ordA, ordB, b, a = 2;
        ArrayList<Long> powA;

        while (true) {
            powA = powersMod(a, mod);
            Collections.sort(powA);
            ordA = powA.size() + 1;
            if (print) {
                System.out.println("powers of a = " + a + " till one: " + powersMod(a, mod));
                System.out.println("ord_" + mod + "(" + a + ")" + " = " + ordA);
            }
            if (ordA == mod - 1) {//stop condition
                break;
            } else {
                b = 2;
                while (b < mod && powA.get((int) (b - 2)) == b) {
                    b++;
                }
                ordB = ord(b, mod, false);
                if (print) {
                    System.out.println("powers of b = " + b + " till one: " + powersMod(b, mod));
                    System.out.println("ord_" + mod + "(" + b + ")" + " = " + ordB);
                }
                if (ordB == mod - 1) {
                    a = b;
                    break;
                } else {
                    gcd = gcd(ordA, ordB);
                    if (print) {
                        System.out.println("a1 = " + a + ", b1 = " + b + "^" + gcd + " = " + modPow(b, gcd, mod, print));
                    }
                    a = modMult(a, modPow(b, gcd, mod, print), mod);
                }
            }
        }

        return a;
    }

    public static long bigPowMod(long a_, long n_, long mod_) {
        return bigPowMod(a_, n_, mod_, true);
    }

    public static long bigPowMod(long a_, long n_, long mod_, boolean print) {//finds a_^n_ (mod mod_)
        primeFact(a_, print);
        ArrayList<long[]> fact = primeFact(mod_, print);
        ArrayList<Long> a = new ArrayList<Long>(), n = new ArrayList<Long>(), mod = new ArrayList<Long>();
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

        return chinese(a, mod, print);
    }
}
