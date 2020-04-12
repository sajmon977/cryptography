package krypto;

import java.math.BigInteger;
import java.util.*;

public class krypto {

    algorithms alg = new algorithms();
    ciphers cip = new ciphers();

    void run() {
        /* BigInteger a = new BigInteger("3");
        BigInteger b = new BigInteger("249");
        BigInteger c = new BigInteger("4409");
        System.out.println(a.modPow(b, c));
        n=243,g=77,a=143;;
(b) n = 343, g = 101, a = 100;
(c) n = 250, g = 27, a = 127;
(d) n = 242, g = 123, a = 39
        g=2, a=13, n=p−1=540
        p=4409, g=3, a=37 3555
        
        
        (a) x2 ≡ 17 (mod 83); (b) x2 ≡ 66 (mod 83); (c) x2 ≡ 42 (mod 89);
(d) x3 ≡ 11 (mod 101); (e) x5 ≡ 37 (mod 71); (f) x6 ≡ 78 (mod 79); (g) x15 ≡ 60 (mod 101).
        log_2(17) (mod 82)log_2(11) (mod 100)
         */
        //System.out.println(alg.rhoPollard(101, 2, 11, true));
        System.out.println(alg.modPow(2, 71, 101, true));

    }

    public static void main(String[] args) {
        krypto a = new krypto();
        a.run();
    }

}
