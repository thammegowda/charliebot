// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

public class MersenneTwister extends Random
        implements Serializable {

    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = 0x9908b0df;
    private static final int UPPER_MASK = 0x80000000;
    private static final int LOWER_MASK = 0x7fffffff;
    private static final int TEMPERING_MASK_B = 0x9d2c5680;
    private static final int TEMPERING_MASK_C = 0xefc60000;
    private static final long GOOD_SEED = 0L;
    private int mt[];
    private int mti;
    private int mag01[];
    private double __nextNextGaussian;
    private boolean __haveNextNextGaussian;

    public MersenneTwister() {
        super(0L);
        setSeed(0L);
    }

    public MersenneTwister(long l) {
        super(l);
        setSeed(l);
    }

    public static void main(String args[]) {
        System.out.println("\nGrab the first 1000 booleans");
        MersenneTwister mersennetwister = new MersenneTwister();
        int i;
        for (i = 0; i < 1000; i++) {
            System.out.print(mersennetwister.nextBoolean() + " ");
            if (i % 8 == 7)
                System.out.println();
        }

        if (i % 8 != 7)
            System.out.println();
        System.out.println("\nGrab 1000 booleans of increasing probability using nextBoolean(double)");
        mersennetwister = new MersenneTwister();
        for (i = 0; i < 1000; i++) {
            System.out.print(mersennetwister.nextBoolean((double) i / 999D) + " ");
            if (i % 8 == 7)
                System.out.println();
        }

        if (i % 8 != 7)
            System.out.println();
        System.out.println("\nGrab 1000 booleans of increasing probability using nextBoolean(float)");
        mersennetwister = new MersenneTwister();
        for (i = 0; i < 1000; i++) {
            System.out.print(mersennetwister.nextBoolean((float) i / 999F) + " ");
            if (i % 8 == 7)
                System.out.println();
        }

        if (i % 8 != 7)
            System.out.println();
        byte abyte0[] = new byte[1000];
        System.out.println("\nGrab the first 1000 bytes using nextBytes");
        mersennetwister = new MersenneTwister();
        mersennetwister.nextBytes(abyte0);
        for (i = 0; i < 1000; i++) {
            System.out.print(abyte0[i] + " ");
            if (i % 16 == 15)
                System.out.println();
        }

        if (i % 16 != 15)
            System.out.println();
        System.out.println("\nGrab the first 1000 bytes -- must be same as nextBytes");
        mersennetwister = new MersenneTwister();
        for (i = 0; i < 1000; i++) {
            byte byte0;
            System.out.print((byte0 = mersennetwister.nextByte()) + " ");
            if (byte0 != abyte0[i])
                System.out.print("BAD ");
            if (i % 16 == 15)
                System.out.println();
        }

        if (i % 16 != 15)
            System.out.println();
        System.out.println("\nGrab the first 1000 shorts");
        mersennetwister = new MersenneTwister();
        for (i = 0; i < 1000; i++) {
            System.out.print(mersennetwister.nextShort() + " ");
            if (i % 8 == 7)
                System.out.println();
        }

        if (i % 8 != 7)
            System.out.println();
        System.out.println("\nGrab the first 1000 ints");
        mersennetwister = new MersenneTwister();
        for (i = 0; i < 1000; i++) {
            System.out.print(mersennetwister.nextInt() + " ");
            if (i % 4 == 3)
                System.out.println();
        }

        if (i % 4 != 3)
            System.out.println();
        System.out.println("\nGrab the first 1000 ints of different sizes");
        mersennetwister = new MersenneTwister();
        for (i = 0; i < 1000; i++) {
            System.out.print(mersennetwister.nextInt(i + 1) + " ");
            if (i % 4 == 3)
                System.out.println();
        }

        if (i % 4 != 3)
            System.out.println();
        System.out.println("\nGrab the first 1000 longs");
        mersennetwister = new MersenneTwister();
        for (i = 0; i < 1000; i++) {
            System.out.print(mersennetwister.nextLong() + " ");
            if (i % 3 == 2)
                System.out.println();
        }

        if (i % 3 != 2)
            System.out.println();
        System.out.println("\nGrab the first 1000 floats");
        mersennetwister = new MersenneTwister();
        for (i = 0; i < 1000; i++) {
            System.out.print(mersennetwister.nextFloat() + " ");
            if (i % 4 == 3)
                System.out.println();
        }

        if (i % 4 != 3)
            System.out.println();
        System.out.println("\nGrab the first 1000 doubles");
        mersennetwister = new MersenneTwister();
        for (i = 0; i < 1000; i++) {
            System.out.print(mersennetwister.nextDouble() + " ");
            if (i % 3 == 2)
                System.out.println();
        }

        if (i % 3 != 2)
            System.out.println();
        System.out.println("\nGrab the first 1000 gaussian doubles");
        mersennetwister = new MersenneTwister();
        for (i = 0; i < 1000; i++) {
            System.out.print(mersennetwister.nextGaussian() + " ");
            if (i % 3 == 2)
                System.out.println();
        }

        if (i % 3 != 2)
            System.out.println();
    }

    public synchronized void setSeedOld(long l) {
        super.setSeed(l);
        __haveNextNextGaussian = false;
        mt = new int[624];
        mt[0] = (int) l;
        for (mti = 1; mti < 624; mti++)
            mt[mti] = 0x10dcd * mt[mti - 1];

        mag01 = new int[2];
        mag01[0] = 0;
        mag01[1] = 0x9908b0df;
    }

    public synchronized void setSeed(int ai[]) {
        super.setSeed(0L);
        __haveNextNextGaussian = false;
        mt = new int[624];
        System.arraycopy(ai, 0, mt, 0, 624);
        mti = 624;
        mag01 = new int[2];
        mag01[0] = 0;
        mag01[1] = 0x9908b0df;
    }

    public synchronized void setSeed(long l) {
        super.setSeed(l);
        int i = (int) l;
        __haveNextNextGaussian = false;
        mt = new int[624];
        for (int j = 0; j < 624; j++) {
            mt[j] = i & 0xffff0000;
            i = 0x10dcd * i + 1;
            mt[j] |= (i & 0xffff0000) >>> 16;
            i = 0x10dcd * i + 1;
        }

        mti = 624;
        mag01 = new int[2];
        mag01[0] = 0;
        mag01[1] = 0x9908b0df;
    }

    protected synchronized int next(int i) {
        if (mti >= 624) {
            int j1;
            for (j1 = 0; j1 < 227; j1++) {
                int j = mt[j1] & 0x80000000 | mt[j1 + 1] & 0x7fffffff;
                mt[j1] = mt[j1 + 397] ^ j >>> 1 ^ mag01[j & 1];
            }

            for (; j1 < 623; j1++) {
                int k = mt[j1] & 0x80000000 | mt[j1 + 1] & 0x7fffffff;
                mt[j1] = mt[j1 + -227] ^ k >>> 1 ^ mag01[k & 1];
            }

            int l = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ l >>> 1 ^ mag01[l & 1];
            mti = 0;
        }
        int i1 = mt[mti++];
        i1 ^= i1 >>> 11;
        i1 ^= i1 << 7 & 0x9d2c5680;
        i1 ^= i1 << 15 & 0xefc60000;
        i1 ^= i1 >>> 18;
        return i1 >>> 32 - i;
    }

    private synchronized void writeObject(ObjectOutputStream objectoutputstream)
            throws IOException {
        objectoutputstream.defaultWriteObject();
    }

    private synchronized void readObject(ObjectInputStream objectinputstream)
            throws IOException, ClassNotFoundException {
        objectinputstream.defaultReadObject();
    }

    public boolean nextBoolean() {
        return next(1) != 0;
    }

    public boolean nextBoolean(float f) {
        if (f < 0.0F || f > 1.0F)
            throw new IllegalArgumentException("probability must be between 0.0 and 1.0 inclusive.");
        else
            return nextFloat() < f;
    }

    public boolean nextBoolean(double d) {
        if (d < 0.0D || d > 1.0D)
            throw new IllegalArgumentException("probability must be between 0.0 and 1.0 inclusive.");
        else
            return nextDouble() < d;
    }

    public int nextInt(int i) {
        if (i <= 0)
            throw new IllegalArgumentException("n must be >= 0");
        if ((i & -i) == i)
            return (int) ((long) i * (long) next(31) >> 31);
        int j;
        int k;
        do {
            j = next(31);
            k = j % i;
        } while ((j - k) + (i - 1) < 0);
        return k;
    }

    public double nextDouble() {
        return (double) (((long) next(26) << 27) + (long) next(27)) / 9007199254740992D;
    }

    public float nextFloat() {
        return (float) next(24) / 1.677722E+07F;
    }

    public void nextBytes(byte abyte0[]) {
        for (int i = 0; i < abyte0.length; i++)
            abyte0[i] = (byte) next(8);

    }

    public char nextChar() {
        return (char) next(16);
    }

    public short nextShort() {
        return (short) next(16);
    }

    public byte nextByte() {
        return (byte) next(8);
    }

    public synchronized double nextGaussian() {
        if (__haveNextNextGaussian) {
            __haveNextNextGaussian = false;
            return __nextNextGaussian;
        }
        double d;
        double d1;
        double d2;
        do {
            d = 2D * nextDouble() - 1.0D;
            d1 = 2D * nextDouble() - 1.0D;
            d2 = d * d + d1 * d1;
        } while (d2 >= 1.0D || d2 == 0.0D);
        double d3 = Math.sqrt((-2D * Math.log(d2)) / d2);
        __nextNextGaussian = d1 * d3;
        __haveNextNextGaussian = true;
        return d * d3;
    }
}
