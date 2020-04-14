package krypto;

import java.math.BigInteger;
import java.util.*;

public class krypto {

    log log = new log(); 
    //shanks(mod, g, a) - finds log_g(a) (mod)
    //pohligHellman(mod, g, a) - finds log_g(a) (mod)
    //rhoPollard(mod, g, a, random) - finds log_g(a) (mod), if random is true S1 and S2 will be random if random is false S1 = {3k+1}, S2 = {3k}
    algorithms alg = new algorithms();
    //gauss(mod) - finds primitive root modulo mod
    //bigPowMod(a, n, mod) finds a^n (mod)

    void run() {
        /*
        343,g=101,a=100;
(c) n=250,g=27,a=127;
(d) n=242,g=123,a=39.
        */
        log.rhoPollard(242, 123, 39, false);
    }

    public static void main(String[] args) {
        krypto a = new krypto();
        a.run();
    }

}
