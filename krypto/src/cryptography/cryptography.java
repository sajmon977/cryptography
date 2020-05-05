package cryptography;

import java.math.BigInteger;
import java.util.*;

public class cryptography extends algorithms {

    public void invoke(String met, long... a) throws Exception {
        print = "";
        Object ret = null;
        Class[] classes = {algorithms.class, utils.class, algorithms.log.class};

        for (Class cl : classes) {
            try {
                ret = super.invoke(cl, met, a);
                break;
            } catch (Exception e) {
                if (e.getClass() != java.lang.NoSuchMethodException.class) {
                    throw e;
                }
            }
        }

        System.out.println(print + "\n" + met + " = " + ret + "\n\n");
    }

    public void Chinese(long... params) {
        print = "";
        ArrayList<long[]> equations = new ArrayList();
        for (int i = 0; i < params.length / 2; i++) {
            equations.add(new long[]{params[2 * i], params[2 * i + 1]});
        }
        long ret = algorithms.chinese(equations);

        System.out.println(print + "\nchinese = " + ret);
    }


    /*
    invoke("mod", a, mod);//calculate a modulo mod
    invoke("gcd", a, b);//greatest common divisor of a and b
    invoke("inverse", n, mod);//finds inverse of n modulo mod (inverse * n = 1 (mod))
    invoke("primeFact", a);//finds prime factors of a number "a" without factor 1
    invoke("isPrime", a);//checks if "a" is a prime number
    invoke("fi", n);//calculate fi(n) (euler function)
    invoke("congruent", a, b, mod);//finds x that a*x = b (mod mod)
    Chinese(new ArrayList<long[]>(List.of(new long[]{a1, mod1}, new long[]{a2, mod2})));//linear congruent equations with mods pairwise coprime x = a1 (mod1), x = a2 (mod2)
    invoke("modMult", a, b, mod);//calculates a * b (mod), to prevent lost data (e.g. 10^10 * 10^10 > Long.maxValue)
    invoke("modPow", a, n, mod);//calculates a^n (mod) with binary powers
    invoke("ord", a, mod);//calculates ord of a modulo mod
    invoke("smallLog", g, a, mod);//finds logarithm log_g(a) for small modulo mod
    invoke("powersMod", a, mod);//returns powers of a modulo mod till one (a, a^2, a^3,...,1)
    
    invoke("gauss", mod);//finds primitive root modulo prime mod with gauss algorithm
    invoke("primitivRoot", mod);//finds primitive root modulo mod 
    invoke("bigPowMod", a, n, mod);//finds a_^n_ (mod mod_)
    invoke("powerMod", n, a, mod);//finds x that x^n = a (mod)
    invoke("legendre", a, mod);//finds legendre symbol of (a/mod)
    invoke("jacobi", a, mod);//finds jacobi symbol of (a/mod)
    invoke("modPoly", mod, a_n, a_n-1, ..., a_0);//solves quadratic equation a_n*x^n + ...  a_1*x + a_0 = 0 (mod) using hensel theorem
    invoke("tonelliShanks", a, mod);//finds square root for x^2 = a (mod) mod is a prime number

    invoke("shanks", g, a, mod);//finds discrete logarithm (log_g(a) modulo mod, g^shanks = a modulo mod)
    invoke("pohligHellman", g, a, mod);//finds discrete logarithm (log_g(a) modulo mod, g^pohlighellman = a modulo mod)
    invoke("rhoPollard", g, a, mod, random) {//finds discrete logarithm (log_g(a) modulo mod, g^rhoPollard = a modulo mod) (random = 1 S1, S2 will be randomise, random = 0 S1 = 3k+1, S2 = 3k)
     */
    void run() throws Exception {
        //example
        invoke("pohligHellman", 77, 143, 243);//solves quadratic equation a_n*x^n + ...  a_1*x + a_0 = 0 (mod)
    }

    public static void main(String[] args) throws Exception {
        cryptography a = new cryptography();
        a.run();
    }

}
