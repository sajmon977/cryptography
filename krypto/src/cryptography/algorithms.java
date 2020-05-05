package cryptography;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Collections;

public class algorithms extends utils {

    protected static long gauss(long mod) {//finds primitive root modulo prime mod with gauss algorithm
        if (!isPrime(mod)) {
            throw new java.lang.Error("gauss algorithm works for primes only");
        }

        long gcd, ordA, ordB, b, a = (mod != 2 ? 2 : 1);
        ArrayList<Long> powA, powB;

        while (mod != 2) {
            powA = powersMod(a, mod);
            ordA = powA.size() + 1;
            Collections.sort(powA);
            print += "powers of a = " + a + " till one: " + powA + "\nord_" + mod + "(" + a + ")" + " = " + ordA + "\n";

            if (ordA == mod - 1) {//stop condition
                break;
            } else {
                b = 2;//looking for b not in powA
                while (b < mod && powA.get((int) (b - 2)) == b) {
                    b++;
                }

                powB = powersMod(b, mod);
                ordB = powB.size() + 1;
                print += "powers of b = " + b + " till one: " + powB + "\nord_" + mod + "(" + b + ")" + " = " + ordB + "\n";

                if (ordB == mod - 1) {
                    a = b;
                    break;
                } else {
                    gcd = gcd(ordA, ordB);
                    print += "a1 = " + a + ", b1 = " + b + "^" + gcd + "\n";
                    b = modPow(b, gcd, mod);
                    a = modMult(a, b, mod);

                    if (ordA / gcd * ordB == mod - 1) {
                        break;
                    }
                }
            }
        }
        print += "primitive root for " + mod + " is " + a + "\n\n";

        return a;
    }

    private static long primitivRoot(long[] fact) {//finds primitive root modulo mod 
        if (fact[2] < 2 || (fact[0] == 2 && fact[1] > 2)) {//cant find primitive root for 1 or 2^k (k > 2)
            throw new java.lang.Error("cant find primitiv roots for " + fact[2]);
        }
        long pr, sqr = fact[0] * fact[0], fi = fact[0] * (fact[0] - 1), newG;
        ArrayList<long[]> factFi;
        boolean check = false;
        pr = gauss(fact[0]);

        if (fact[1] > 1) {
            print += "check if " + pr + " is primitiv root for " + fact[0] + "^2\nfi(" + sqr + ") = ";
            factFi = primeFact(fi);
            for (long[] fFi : factFi) {
                if (modPow(pr, fi / fFi[0], sqr) == 1) {
                    check = true;
                }
            }
            if (check) {//pr is not primitive root for g^2
                print += "so " + pr + " isn't primitiv root for " + sqr + "\n";
                pr += fact[0];
            }
            print += "primitiv root for " + sqr + " and for " + fact[2] + " is " + pr + "\n";
        }

        return pr;
    }

    protected static long primitivRoot(long mod) {
        ArrayList<long[]> fact = primeFact(mod);
        if (fact.size() > 2 || (fact.size() == 2 && (fact.get(0)[0] != 2 || fact.get(0)[1] > 1))) {//mod can be 2p^k or p^k depend on fact.size()
            throw new java.lang.Error("cant find primitiv roots for " + mod);
        }
        long ans = primitivRoot(fact.size() == 1 ? fact.get(0) : fact.get(1));

        if (fact.size() == 2) {
            ans = ans % 2 == 1 ? ans : ans + fact.get(1)[2];
            print += "primitiv root for " + mod + " is " + ans + "\n";
        }
        print += "\n";

        return ans;
    }

    protected static long bigPowMod(long a, long n, long mod) {//finds a_^n_ (mod mod_)
        primeFact(a);
        ArrayList<long[]> fact = primeFact(mod), equations = new ArrayList(fact.size());
        long[] eq;
        long fi, newN, ans;

        for (int i = 0; i < fact.size(); i++) {
            equations.add(new long[]{a % fact.get(i)[2], fact.get(i)[2]});
            eq = equations.get(i);

            fi = (fact.get(i)[0] - 1) * fact.get(i)[2] / fact.get(i)[0];
            newN = n % fi;
            print += "fi(" + fact.get(i)[2] + ") = " + fi + ", x = " + eq[0] + "^" + newN + " (mod " + fact.get(i)[2] + ")\n";

            if (eq[0] != 0 && newN == 0) {
                eq[0] = 1;
            } else if (eq[0] != 0 && eq[0] != 1) {
                eq[0] = modPow(eq[0], newN, fact.get(i)[2]);
            }
        }
        ans = chinese(equations);
        print += "\n";
        return ans;
    }

    private static long powerModPrime(long n, long a, long[] fact) {//finds x that x^n = a (fact[2])
        long g, log_, fi = (fact[0] - 1) * fact[2] / fact[0], ans;
        n = mod(n, fact[2]);
        a = mod(a, fact[2]);
        print += "x^" + n + " = " + a + " (mod " + fact[0] + "^" + fact[1] + ")\n";

        if (fact[0] == 2 && fact[1] > 2) {
            print += "check if " + a + "^" + fi / gcd(fi, n) + " = 1 (mod " + fact[2] + ")\n";
            if (modPow(a, fi / gcd(fi, n), fact[2]) == 1) {
                fi /= 2;
                ArrayList<long[]> equations = new ArrayList();
                g = (a % 8 == 1 || a % 8 == 3) ? 3 : -3;
            } else {
                throw new java.lang.Error(a + "^(" + fi + "/gcd(" + fi + "," + n + ")) != 1 (mod " + fact[2]);
            }
        } else {
            g = primitivRoot(fact);
            print += "\n";
        }
        log_ = log.shanks(g, a, fact[2]);
        ans = modPow(g, congruent(n, log_, fi)[0], fact[2]);
        print += "\n";
        return ans;
    }

    protected static long powerMod(long n, long a, long mod) {//finds x that x^n = a (mod)
        print += "x^" + n + " = " + a + " (mod " + mod + ")\n";
        ArrayList<long[]> fact = primeFact(mod), equations = new ArrayList(fact.size());
        long g, log_, fi, ans;
        print += "\n";
        a = mod(a, mod);

        for (long[] fc : fact) {
            equations.add(new long[]{powerModPrime(n, a, fc), fc[2]});
        }
        ans = chinese(equations);
        print += "\n";
        return ans;
    }

    protected static int legendre(long a, long mod) {
        int ans = legendre(a, mod, "");
        print += "(" + a + "/" + mod + ") = " + ans + "\n";
        return ans;
    }

    private static int legendre(long a, long mod, String space) {//finds legendre symbol of (a/mod)
        if (!isPrime(mod)) {
            throw new java.lang.Error("cant find legendre symbol for not prime: " + mod);
        }
        int ans = 1;
        a = mod(a, mod);
        ArrayList<long[]> fact;
        if (a == 1 || mod == 1) {
            print += space + "(" + a + "/" + mod + ") = 1\n";
            return 1;
        }
        if (a == 0) {
            return 0;
        }
        fact = primeFact(a);

        print += space + "(" + a + "/" + mod + ") = ";

        for (int i = 0; i < fact.size() && (fact.size() > 1 || fact.get(0)[1] > 1); i++) {
            print += "(" + (fact.get(i)[1] > 1 ? "(" + fact.get(i)[0] + "^" + fact.get(i)[1] + ")" : fact.get(i)[0]) + "/" + mod + ")" + (i < fact.size() - 1 ? " * " : " = ");
            if (fact.get(i)[1] % 2 == 0) {
                fact.get(i)[0] = 1;
            }
        }

        for (int i = 0; i < fact.size(); i++) {
            if (fact.get(i)[0] == 1) {
                print += "1" + (i < fact.size() - 1 ? " * " : "\n");
            } else {
                if (fact.get(i)[0] == 2) {
                    if (mod % 8 == 3 || mod % 8 == 5) {
                        ans *= -1;
                        print += "(-1)(" + mod + " = " + (mod % 8) + " (mod 8))" + (i < fact.size() - 1 ? " * " : "\n");
                    } else {
                        print += "1(" + mod + " = " + (mod % 8) + " (mod 8))" + (i < fact.size() - 1 ? " * " : "\n");
                    }
                    fact.get(i)[0] = 1;
                } else {
                    if ((((mod - 1) / 2) * ((fact.get(i)[0] - 1) / 2)) % 2 == 1) {
                        ans *= -1;
                    }
                    print += "(-1)^(" + ((mod - 1) / 2) + "*" + ((fact.get(i)[0] - 1) / 2) + ") * (" + mod + "/" + fact.get(i)[0] + ")" + (i < fact.size() - 1 ? " * " : "\n");
                }
            }
        }
        for (int i = 0; i < fact.size(); i++) {
            if (fact.get(i)[0] != 1) {
                ans *= legendre(mod, fact.get(i)[0], "    " + space);
            }
        }
        return ans;
    }

    protected static int jacobi(long a, long mod) {//finds jacobi symbol of (a/mod)
        int ans = 1;
        if (isPrime(mod)) {
            return legendre(a, mod);
        }
        ArrayList<long[]> fact = primeFact(mod);
        a = mod(a, mod);
        print += "(" + a + "/" + mod + ")";

        if (gcd(a, mod) > 1) {
            print += "0 (NWD(" + a + "," + mod + ") = " + gcd(a, mod) + ")";
            return 0;
        }

        for (int i = 0; i < fact.size() && fact.size() > 1; i++) {
            print += (i == 0 ? " = (" : "(") + a + "/" + fact.get(i)[0] + ")" + (fact.get(i)[1] > 1 ? "^" + fact.get(i)[1] : "") + (i < fact.size() - 1 ? " * " : "");
        }
        print += "\n";

        for (int i = 0; i < fact.size(); i++) {
            if (fact.get(i)[1] % 2 == 1) {
                ans *= legendre(a, fact.get(i)[0], "    ");
            }
        }
        print += "(" + a + "/" + mod + ") = " + ans + "\n";
        return ans;
    }

    protected static ArrayList<Long> modPoly(long Mod, long... coef) {//solves quadratic equation coef[0]x^n + coef[1]x^(n-1) + ... + coef[n+1] = 0 (mod) using hensel theorem
        long mod, polyVal, derivVal, temp;
        long[] deriv = new long[coef.length - 1], fact, max;
        for (int i = 0; i < deriv.length; i++) {
            deriv[i] = (deriv.length - i) * coef[i];
        }
        print += polyStr(coef) + " = 0 (mod " + Mod + ")\n" + "derivative: " + polyStr(deriv) + "\n";
        ArrayList<long[]> facts = primeFact(Mod), equations;
        ArrayList<Long> ans = new ArrayList(), newX;
        ArrayList<Long>[] X = new ArrayList[facts.size()];
        print += "\n";

        for (int i = 0; i < facts.size(); i++) {//loop over factories of Mod to find roots for each fact[2]
            fact = facts.get(i);
            mod = fact[0];
            X[i] = new ArrayList();
            for (long x = 0; x < mod; x++) {
                polyVal = polyVal(x, coef, mod);
                if (polyVal == 0) {
                    X[i].add(x);
                }
            }
            if (X[i].isEmpty()) {
                throw new java.lang.Error("no roots for mod = " + mod);
            }
            print += "x_0 = " + X[i] + " for mod " + mod + "\n\n";

            for (int k = 1; k < fact[1]; k++) {//loop over roots mod = fact[0]^(k+1)
                mod *= fact[0];
                newX = new ArrayList(X[i].size());
                print += "mod = " + mod + "\n\n";

                for (Long x : X[i]) {//loop over all roots modulo fact[0]^k and find roots for fact[0]^(k+1)
                    polyVal = polyVal(x, coef, mod);
                    derivVal = polyVal(x, deriv, mod);
                    print += "x_0 = " + x + ", f(x_0) = " + polyVal + ", f'(x_0) = " + derivVal + " = " + derivVal % fact[0] + " (mod " + fact[0] + ")\n";

                    if (derivVal % fact[0] != 0) {
                        print += "(1) " + derivVal + "*" + (mod / fact[0]) + "x = " + (-polyVal) + " (mod " + mod + ")\n";
                        temp = congruent(modMult(derivVal, mod / fact[0], mod), -polyVal, mod)[0];
                        newX.add(mod(x + modMult(temp, mod / fact[0], mod), mod));
                        print += "x = " + x + " + " + temp + "*" + (mod / fact[0]) + " = " + newX.get(newX.size() - 1) + " (mod " + mod + ")\n\n";
                    } else {
                        if (polyVal == 0) {
                            print += "(2) so x = " + x + " + i*" + mod / fact[0] + " where i=0,..." + (fact[0] - 1) + "\nx = " + x;
                            for (int j = 0; j < fact[0]; j++) {
                                newX.add(mod(x + j * mod / fact[0], mod));
                                print += j > 0 ? ", " + newX.get(newX.size() - 1) : "";
                            }
                            print += "\n\n";
                        } else {
                            print += "(3) x = " + x + " isn't root for: " + mod + "\n\n";
                        }
                    }
                }
                if (newX.isEmpty()) {
                    throw new java.lang.Error("no roots for mod = " + mod);
                }
                X[i] = newX;
            }
            print += "\n";
        }
        max = new long[X.length + 1];
        max[0] = 1;
        for (int i = 1; i < max.length; i++) {
            print += "roots for mod = " + facts.get(i - 1)[2] + " are: " + X[i - 1] + "\n";
            max[i] = max[i - 1] * X[i - 1].size();
        }
        print += "\n";
        for (int i = 0; i < max[max.length - 1]; i++) {
            equations = new ArrayList(X.length);
            for (int j = 0; j < X.length; j++) {
                equations.add(new long[]{X[j].get((int) ((i / max[j]) % max[j + 1])), facts.get(j)[2]});
            }
            ans.add(chinese(equations));
            print += "\n\n";
        }

        print += "Solutions for " + polyStr(coef) + " = 0 (mod " + Mod + ") are: " + ans + "\n";
        return ans;
    }

    protected static long tonelliShanks(long a, long mod) {//finds square root for x^2 = a (mod) mod is a prime number
        if (mod == 2 || !isPrime(mod)) {//DO DOKONCZENIA!!!
            throw new java.lang.Error("Tonelli and Shanks algorithm works for prime only");
        }
        print += "x^2 = " + a + " (mod " + mod + ")\n\n";
        a = mod(a, mod);
        long ans = 0, d, fi = mod - 1, D, t, inv;
        ArrayList<long[]> fact;

        if (legendre(a, mod) == 1) {
            print += "\nso there exists a solution, " + mod + " = " + mod % 8 + " (mod 8)\n";

            if (mod % 8 == 5) {
                print += "check if x = " + a + "^((" + mod + " + 3)/8) (mod " + mod + ") solves x^2 = " + a + " (mod " + mod + ")\n";
                ans = modPow(a, mod / 8 + 1, mod);
                if (modPow(ans, 2, mod) != a) {
                    print += ans + "^2 != " + a + " (mod " + mod + "), new solution is x = " + ans + "*" + 2 + "^((" + mod + " - 1)/4) (mod " + mod + ")\n";
                    ans = modMult(ans, modPow(2, (mod - 1) / 4, mod), mod);
                    print += "x = " + ans + "(mod " + mod + ")\n";
                }
            } else if (mod % 8 == 1) {
                for (d = 2; d < mod; d++) {
                    if (legendre(d, mod) == -1) {
                        break;
                    }
                    print += "\n";
                }
                if (d == mod) {
                    throw new java.lang.Error("can't find a number x such that (x/" + mod + ") = -1");
                }
                fact = primeFact(fi);
                print += "x_0 = " + a + "^((" + (fi / fact.get(0)[2]) + " + 1)/2) (mod " + mod + "), D = " + d + "^" + (fi / fact.get(0)[2]) + "(mod " + mod + "), t_0 = log_2(ord_" + mod + "((x_0)^2 * " + a + "^(-1)))\n";
                ans = modPow(a, (fi / fact.get(0)[2]) / 2 + 1, mod);
                D = modPow(d, fi / fact.get(0)[2], mod);
                inv = inverse(a, mod);
                print += "a^(-1) = " + inv + " (mod " + mod + ")\n";
                t = log.shanks(2, ord(modMult(modPow(ans, 2, mod), inv, mod), mod), mod);
                print += "x_0 = " + ans + ", D = " + D + ", t_0 = " + t + "\n\n";

            }
        }
        return ans;
    }

    protected static class log {

        private static ArrayList<Long> S1, S2;

        protected static long shanks(long g, long a, long mod) {//finds discrete logarithm (log_g(a) modulo mod, g^shanks = a modulo mod)
            ArrayList<Long> smallSteps = new ArrayList(), bigSteps = new ArrayList();
            int n = 1 + (int) Math.sqrt(mod);
            long g_minus, ans = -1;
            g = mod(g, mod);
            a = mod(a, mod);

            smallSteps.add(1l);
            bigSteps.add(1l);
            smallSteps.addAll(powersMod(g, mod, n));
            g_minus = inverse(smallSteps.get(smallSteps.size() - 1), mod);
            bigSteps.addAll(powersMod(g_minus, mod, n));
            for (int i = 0; i < bigSteps.size(); i++) {
                bigSteps.set(i, (bigSteps.get(i) * a) % mod);
            }
            print += "n = " + n + "\nsmall steps: " + smallSteps + "\ng^(-n): " + g_minus + "\nbig steps: " + bigSteps + "\n";

            for (int i = 0; i < bigSteps.size(); i++) {
                for (int j = 0; j < smallSteps.size(); j++) {
                    if (bigSteps.get(i).equals(smallSteps.get(j))) {
                        smallSteps.add(1l);
                        print += "log_" + g + "(" + a + ")" + " = " + j + "+" + i + "*" + n + " = " + (j + i * n) + " (mod " + mod + ")\n\n";

                        return j + n * i;
                    }
                }
            }
            throw new java.lang.Error("log_" + g + "(" + a + ") couldn't be find");
        }

        protected static long pohligHellman(long g, long a, long mod) {//finds discrete logarithm (log_g(a) modulo mod, g^pohlighellman = a modulo mod)
            print += "log_" + g + "(" + a + ") (mod " + mod + ")\n";
            long fi = fi(mod), gpow, apow, ans;
            ArrayList<long[]> fact = primeFact(fi), equations = new ArrayList();//0 log_(g_i) (a_i); 1 n_i = p_i^alfa_i
            String nStr = "", gStr = "", aStr = "", logStr = "";

            for (int i = 0; i < fact.size(); i++) {
                gpow = modPow(g, fi / fact.get(i)[2], mod);
                apow = modPow(a, fi / fact.get(i)[2], mod);
                equations.add(new long[]{smallLog(gpow, apow, mod), fact.get(i)[2]});
                nStr += (i != 0 ? ", " : "") + fact.get(i)[2];
                gStr += (i != 0 ? ", " : "") + gpow;
                aStr += (i != 0 ? ", " : "") + apow;
                logStr += (i != 0 ? ", " : "") + equations.get(i)[0];
            }
            print += "n_i = p_i^alfa_i:    " + nStr + "\ng_i = g^((p-1)/n_i): " + gStr + "\na_i = a^((p-1)/n_i): " + aStr + "\nlog_(g_i) (a_i):     " + logStr + "\n";
            ans = chinese(equations);
            print += "\n";
            return ans;
        }

        protected static long rhoPollard(long g, long a, long mod, long random) {//finds discrete logarithm (log_g(a) modulo mod, g^rhoPollard = a modulo mod)
            S1 = new ArrayList((int) mod / 3);
            S2 = new ArrayList((int) mod / 3);
            LinkedList<long[]> x = new LinkedList<>();//x,u,v
            long fi, modPow, pow, ans, iter = 1;
            long[] solution, temp;

            for (long i = 0; i < mod; i++) {
                if (random == 0) {
                    if (i % 3 == 1) {
                        S1.add(i);
                    } else if (i % 3 == 0) {
                        S2.add(i);
                    }
                } else {
                    Random gen = new Random();
                    int rand = gen.nextInt(3);
                    if (rand == 0 || i == 1) {//i % 3 == 1
                        S1.add(i);
                    } else if (rand == 1) {//i % 3 == 0
                        S2.add(i);
                    }
                }
            }

            print += "S1 = " + S1 + "\nS2 = " + S2 + "\n";
            fi = fi(mod);

            x.add(new long[]{1, 0, 0});//0
            print += "i: x_i, u_i, v_i\n";
            do {
                for (int j = 0; j < 2; j++) {
                    print += iter++ + ": ";
                    x.add(rPFunc(x.getLast(), mod, fi, g, a));
                }
                x.removeFirst();
            } while (x.getLast()[0] != x.getFirst()[0]);
            print += "\n";

            solution = congruent(x.getFirst()[1] - x.getLast()[1], x.getLast()[2] - x.getFirst()[2], fi);
            modPow = modPow(g, solution[1], mod);
            pow = modPow(g, solution[0], mod);
            ans = solution[0];

            while (ans < fi) {
                if (pow == a) {
                    break;
                }
                ans += solution[1];
                print += g + "^" + ans + " = " + pow + "*" + modPow + " = ";
                pow = modMult(pow, modPow, mod);
                print += pow + " (mod " + mod + ")\n";
            }
            print += "\n";
            return ans;
        }

        private static long[] rPFunc(long[] x, long mod, long fiMod, long g, long a) {
            long[] ans;//x,u,v
            if (S1.contains(x[0])) {//S1
                ans = new long[]{modMult(a, x[0], mod), x[1] + 1 % fiMod, x[2]};
            } else if (S2.contains(x[0])) {//S2
                ans = new long[]{modMult(x[0], x[0], mod), modMult(2, x[1], fiMod), modMult(2, x[2], fiMod)};
            } else {//S3
                ans = new long[]{modMult(g, x[0], mod), x[1], x[2] + 1 % fiMod};
            }
            print += ans[0] + ", " + ans[1] + ", " + ans[2] + "\n";
            return ans;
        }
    }
}
