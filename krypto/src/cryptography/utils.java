package cryptography;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class utils {

    protected static String print = "";

    protected Object invoke(Class cl, String met, long... a) throws Exception {
        Object ret;
        try {
            ret = cl.getDeclaredMethod(met, long.class, long[].class).invoke(this, a[0], Arrays.copyOfRange(a, 1, a.length));
        } catch (Exception e) {
            switch (a.length) {
                case 1:
                    ret = cl.getDeclaredMethod(met, long.class).invoke(this, a[0]);
                    break;
                case 2:
                    ret = cl.getDeclaredMethod(met, long.class, long.class).invoke(this, a[0], a[1]);
                    break;
                case 3:
                    ret = cl.getDeclaredMethod(met, long.class, long.class, long.class).invoke(this, a[0], a[1], a[2]);
                    break;
                case 4:
                    try {
                        ret = cl.getDeclaredMethod(met, long.class, long.class, long.class, long.class).invoke(this, a[0], a[1], a[2], a[3]);
                    } catch (Exception f) {
                        ret = cl.getDeclaredMethod(met, long.class, long.class, long.class, boolean.class).invoke(this, a[0], a[1], a[2], a[3]);
                    }
                    break;
                default:
                    ret = cl.getDeclaredMethod(met, long.class, long.class, long.class, long.class, long.class).invoke(this, a[0], a[1], a[2], a[3], a[4]);
                    break;
            }
        }
        return (met.equals("primeFact") || met.equals("congruent")) ? "" : ret;
    }

    protected static long mod(long a, long mod) { //calculate a modulo mod
        return ((a % mod) + mod) % mod;
    }

    protected static long gcd(long a, long b) {//greatest common divisor of a and b
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

    protected static long inverse(long n, long mod) {//finds inverse of n modulo mod (inverse * n = 1 (mod))
        if (mod < 2) {
            throw new java.lang.Error("modulo = " + mod + " < 2");
        }
        long a = mod(n, mod), b = mod, temp, invPrev = 0, inv = 1;

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
        if (((a == 0) ? b : a) != 1) {//gcd != 1
            throw new java.lang.Error("gcd(" + n + "," + mod + ") = " + ((a == 0) ? b : a) + " != 1");
        }
        return mod(invPrev, mod);
    }

    protected static ArrayList<long[]> primeFact(long a) {//finds prime factors of a number "a" without factor 1
        if (a < 1) {
            throw new java.lang.Error("cant find prime factors for " + a);
        }
        print += a + " = ";
        ArrayList<long[]> ans = new ArrayList();//a = p^b*... ans.get()[0] = p prime, ans.get()[1] = b - how many this prime, ans.get()[3] = a^b 
        long last = 0, max = (long) Math.sqrt(a);

        for (int i = 2; i <= max; i++) {
            if (a % i == 0) {
                ans.add(new long[]{i, 1, i});
                a /= i;

                while (a % i == 0) {
                    ans.get((int) last)[1]++;
                    ans.get((int) last)[2] *= i;
                    a /= i;
                }
                print += (ans.size() > 1 ? " * " : "") + ans.get((int) last)[0] + (ans.get((int) last)[1] > 1 ? "^" + ans.get((int) last)[1] + " (" + ans.get((int) last)[2] + ")" : "");
                max = (long) Math.sqrt(a);
                last++;
            }
        }
        if (a != 1) {
            ans.add(new long[]{a, 1, a});
            print += (ans.size() > 1 ? " * " : "") + a;
        }
        print += "\n";
        return ans;
    }

    protected static boolean isPrime(long a) {//checks if "a" is a prime number
        if (a < 0) {
            throw new java.lang.Error("wrong number " + a + " for check if it is a prime number");
        } else if (a < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(a); i++) {
            if (a % i == 0) {
                return false;
            }
        }
        return true;
    }

    protected static long fi(long n) {//calculate fi(n) (euler function)
        ArrayList<long[]> fact = primeFact(n);
        print += "fi(" + n + ") = ";

        long ans = 1;

        for (int i = 0; i < fact.size(); i++) {
            ans *= (fact.get(i)[0] - 1) * fact.get(i)[2] / fact.get(i)[0];
            print += (i == 0 ? "" : " * ") + (fact.get(i)[0] - 1) + "*" + fact.get(i)[0] + "^" + (fact.get(i)[1] - 1) + " (" + (fact.get(i)[2] / fact.get(i)[0]) + ")";
        }
        print += " = " + ans + "\n";

        return ans;
    }

    protected static long[] congruent(long a, long b, long mod) {
        return congruent(a, b, mod, "x");
    }

    private static long[] congruent(long a_, long b_, long mod, String var) {//finds x that a_*x = b_ (mod mod)
        long a = mod(a_, mod), b = mod(b_, mod), gcd = gcd(a, mod);
        if (b % gcd != 0) {
            throw new java.lang.Error("no solution for " + a + var + " = " + b + " (mod " + mod + ") because of gcd(" + a + "," + mod + ") = " + gcd);
        }
        print += (a_ != 1 ? a_ : "") + var + " = " + b_ + " (mod " + mod + ")" + (a != a_ || b != b_ ? " <=> " + (a != 1 ? a : "") + var + " = " + b + " (mod " + mod + ")" : "") + (gcd > 1 ? " |/ " + gcd : "") + (a != 1 ? " <=> " : "");
        if (a != 0 && a != 1) {
            a /= gcd;
            b /= gcd;
            mod /= gcd;

            while (a != 1) {
                gcd = gcd(a, b);
                print += gcd > 1 ? a + var + " = " + b % mod + (b / mod > 0 ? " + " + b / mod + "*" + mod : "") + " (mod " + mod + ") |/" + gcd + " <=> " : "";

                a /= gcd;
                b /= gcd;
                b += mod;
            }
            b = mod(b, mod);
            print += var + " = " + b + " (mod " + mod + ")";
        }
        if (a == 0) {
            mod = 1;
        }
        print += "\n";
        return new long[]{b, mod};
    }

    private static void assign(long[] a, long[] b) {
        a[0] = b[0];
        a[1] = b[1];
    }

    protected static long chinese(ArrayList<long[]> equations) {//linear congruent equations with mods pairwise coprime
        for (int i = 0; i < equations.size(); i++) {
            for (long[] eq : equations.subList(i + 1, equations.size())) {
                if (gcd(equations.get(i)[1], eq[1]) != 1) {
                    throw new java.lang.Error("modulos must be coprime, gcd(" + equations.get(i)[1] + "," + eq[1] + ") = " + gcd(equations.get(i)[1], eq[1]));
                }
            }
        }
        String temp = "k";
        long ans = 0, mult = 1;

        for (long[] eq : equations) {
            assign(eq, congruent(1, eq[0], eq[1]));
        }
        print += "x = " + equations.get(0)[0] + " + " + equations.get(0)[1] + "*x_1\n\n";

        for (int i = 1; i < equations.size(); i++) {
            for (long[] eq : equations.subList(i, equations.size())) {
                assign(eq, congruent(equations.get(i - 1)[1], eq[0] - equations.get(i - 1)[0], eq[1], "x_" + i));
            }
            print += "x_" + i + " = " + equations.get(i)[0] + " + " + equations.get(i)[1] + "*" + (i == (equations.size() - 2) ? "x_" + (i + 1) : "k") + "\n\n";
        }

        Collections.reverse(equations);
        for (long[] eq : equations) {
            ans = eq[0] + eq[1] * ans;
            mult *= eq[1];
            temp = "(" + eq[0] + " + " + eq[1] + "*" + temp + ")";
        }
        print += "x = " + temp + " = " + ans + " + " + mult + "*k\n";
        return ans;
    }

    protected static long modMult(long a_, long b_, long mod_) {//calculates a_ * b_ (mod_), to prevent lost data (e.g. 10^10 * 10^10 > Long.maxValue)
        BigInteger a = new BigInteger(Long.toString(a_)), b = new BigInteger(Long.toString(b_)), mod = new BigInteger(Long.toString(mod_));
        return a.multiply(b).mod(mod).longValue();
    }

    protected static long modPow(long a_, long n, long mod_) {//calculates a_^n (mod_) with binary powers
        if (n < 0) {
            throw new java.lang.Error("can't calculate " + a_ + "^" + n + " (mod " + mod_ + ")");
        } else if (n == 0 || n == 1 || n == 2) {
            print += a_ + "^" + n + " = " + (n == 0 ? 1 : (n == 1 ? a_ : modMult(a_, a_, mod_))) + " (mod " + mod_ + ")\n";
            return (n == 0 ? 1 : (n == 1 ? a_ : modMult(a_, a_, mod_)));
        }

        BigInteger a = new BigInteger(Long.toString(a_)), mod = new BigInteger(Long.toString(mod_)), ans = BigInteger.ONE, temp = new BigInteger(a.toString());
        String bin = Long.toBinaryString(n), forPrint = "";
        print += n + "_10 = " + bin + "_2\n" + a + "^(2^i): ";

        for (int i = bin.length() - 1; i >= 0; i--) {
            if (bin.charAt(i) == '1') {
                ans = ans.multiply(temp).mod(mod);
                forPrint += temp + ((i == 0) ? "" : " * ");
            }
            print += temp + ((i == 0) ? "" : ", ");

            temp = temp.modPow(BigInteger.TWO, mod);
        }
        print += "\n" + a_ + "^" + n + " = " + forPrint + " = " + ans + " (mod " + mod_ + ")" + "\n";

        return ans.longValue();
    }

    protected static long ord(long a, long mod) {//calculates ord of a modulo mod
        ArrayList<long[]> factMod = primeFact(mod), factCar;
        long ans, fi, carmi = 1;
        print += "Carmichael for " + mod + ":\n";

        for (int i = 0; i < factMod.size(); i++) {
            fi = ((factMod.get(i)[0] - 1) * factMod.get(i)[2]) / factMod.get(i)[0];
            if (factMod.get(i)[0] == 2 && factMod.get(i)[1] >= 3) {
                fi /= 2;
            }
            print += "lambda(" + factMod.get(i)[0] + "^" + factMod.get(i)[1] + ") = ";
            primeFact(fi);
            carmi *= fi / gcd(carmi, fi);
        }
        print += "lambda(" + mod + ") = ";
        factCar = primeFact(carmi);
        ans = carmi;

        for (int i = 0; i < factCar.size(); i++) {
            if (modPow(a, ans / factCar.get(i)[0], mod) == 1) {
                ans /= factCar.get(i)[0];
                i--;
                print += "\ncheck if this is ord: ";
                primeFact(ans);
            }
        }
        return ans;
    }

    protected static long smallLog(long g, long a, long mod) {//finds logarithm log_g(a) for small modulo mod
        if (g < 2 || mod > 10000) {
            throw new java.lang.Error("smallLog function should calculate only small logs: modulo = " + mod + " or g is wrong " + g);
        }
        long ans = 1, mult = g;

        while (mult != a) {
            mult = mod(g * mult, mod);
            ans++;
        }

        return ans;
    }

    protected static ArrayList<Long> powersMod(long a, long mod) {
        return powersMod(a, mod, mod);
    }

    protected static ArrayList<Long> powersMod(long a_, long mod_, long leng) {//returns powers of a_ modulo mod_ till one (a_, a_^2, a_^3,...,1)
        ArrayList<Long> ans = new ArrayList();
        BigInteger a = new BigInteger(Long.toString(a_)), mod = new BigInteger(Long.toString(mod_)), b = a;

        do {
            ans.add(b.longValue());
            b = b.multiply(a).mod(mod);
        } while (!b.equals(BigInteger.ONE) && ans.size() < leng);

        return ans;
    }

    protected static long polyVal(long x, long[] coef, long mod) {//returns value of a plynomial with coefficient in x modulo mod
        long polyVal = 0;
        for (int i = 0; i < coef.length; i++) {
            polyVal = modMult(x, polyVal, mod) + coef[i];
        }
        return mod(polyVal, mod);
    }

    protected static String polyStr(long[] coef) {//returns value of a plynomial with coefficient in x modulo mod (coef[0] != 0) 
        if (coef.length > 0) {
            if (coef[0] == 0) {
                throw new java.lang.Error("first coefficient can't be zero");
            }
            String ans = coef[0] == 1 && coef.length == 1 ? "1" : (coef[0] > 1 ? coef[0] : "") + (coef[0] < 0 ? "-" + (-coef[0]) : coef[0] == -1 ? "-" : "") + (coef.length > 1 ? "x" : "") + (coef.length > 2 ? "^" + (coef.length - 1) : "");
            for (int i = 1; i < coef.length; i++) {
                ans += (coef[i] != 0 ? (coef[i] > 0 ? " + " + (coef[i] == 1 && i != coef.length - 1 ? "" : coef[i]) : " - " + (coef[i] == -1 && i != coef.length - 1 ? "" : -coef[i])) + (i != coef.length - 1 ? "x" : "") + (i < coef.length - 2 ? "^" + (coef.length - i - 1) : "") : "");
            }
            return ans;
        }
        return "";
    }
}
