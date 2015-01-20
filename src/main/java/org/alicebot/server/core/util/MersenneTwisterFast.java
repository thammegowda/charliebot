// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import java.io.Serializable;

public class MersenneTwisterFast
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
    private double nextNextGaussian;
    private boolean haveNextNextGaussian;

    public MersenneTwisterFast() {
        setSeed(0L);
    }

    public MersenneTwisterFast(long l) {
        setSeed(l);
    }

    public static void main(String args[]) {
        MersenneTwisterFast mersennetwisterfast = new MersenneTwisterFast(System.currentTimeMillis());
        for (int i = 0; i < 0x186a0; i++)
            System.out.println(mersennetwisterfast.nextInt(5) + 1);

    }

    public final void setSeedOld(long l) {
        haveNextNextGaussian = false;
        mt = new int[624];
        mt[0] = (int) l;
        for (mti = 1; mti < 624; mti++)
            mt[mti] = 0x10dcd * mt[mti - 1];

        mag01 = new int[2];
        mag01[0] = 0;
        mag01[1] = 0x9908b0df;
    }

    public final void setSeed(int ai[]) {
        mt = new int[624];
        System.arraycopy(ai, 0, mt, 0, 624);
        mti = 624;
        mag01 = new int[2];
        mag01[0] = 0;
        mag01[1] = 0x9908b0df;
    }

    public final void setSeed(long l) {
        int i = (int) l;
        haveNextNextGaussian = false;
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

    public final int nextInt() {
        if (mti >= 624) {
            int i1;
            for (i1 = 0; i1 < 227; i1++) {
                int i = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + 397] ^ i >>> 1 ^ mag01[i & 1];
            }

            for (; i1 < 623; i1++) {
                int j = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + -227] ^ j >>> 1 ^ mag01[j & 1];
            }

            int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
            mti = 0;
        }
        int l = mt[mti++];
        l ^= l >>> 11;
        l ^= l << 7 & 0x9d2c5680;
        l ^= l << 15 & 0xefc60000;
        l ^= l >>> 18;
        return l;
    }

    public final short nextShort() {
        if (mti >= 624) {
            int i1;
            for (i1 = 0; i1 < 227; i1++) {
                int i = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + 397] ^ i >>> 1 ^ mag01[i & 1];
            }

            for (; i1 < 623; i1++) {
                int j = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + -227] ^ j >>> 1 ^ mag01[j & 1];
            }

            int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
            mti = 0;
        }
        int l = mt[mti++];
        l ^= l >>> 11;
        l ^= l << 7 & 0x9d2c5680;
        l ^= l << 15 & 0xefc60000;
        l ^= l >>> 18;
        return (short) (l >>> 16);
    }

    public final char nextChar() {
        if (mti >= 624) {
            int i1;
            for (i1 = 0; i1 < 227; i1++) {
                int i = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + 397] ^ i >>> 1 ^ mag01[i & 1];
            }

            for (; i1 < 623; i1++) {
                int j = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + -227] ^ j >>> 1 ^ mag01[j & 1];
            }

            int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
            mti = 0;
        }
        int l = mt[mti++];
        l ^= l >>> 11;
        l ^= l << 7 & 0x9d2c5680;
        l ^= l << 15 & 0xefc60000;
        l ^= l >>> 18;
        return (char) (l >>> 16);
    }

    public final boolean nextBoolean() {
        if (mti >= 624) {
            int i1;
            for (i1 = 0; i1 < 227; i1++) {
                int i = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + 397] ^ i >>> 1 ^ mag01[i & 1];
            }

            for (; i1 < 623; i1++) {
                int j = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + -227] ^ j >>> 1 ^ mag01[j & 1];
            }

            int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
            mti = 0;
        }
        int l = mt[mti++];
        l ^= l >>> 11;
        l ^= l << 7 & 0x9d2c5680;
        l ^= l << 15 & 0xefc60000;
        l ^= l >>> 18;
        return l >>> 31 != 0;
    }

    public final boolean nextBoolean(float f) {
        if (f < 0.0F || f > 1.0F)
            throw new IllegalArgumentException("probability must be between 0.0 and 1.0 inclusive.");
        if (mti >= 624) {
            int i1;
            for (i1 = 0; i1 < 227; i1++) {
                int i = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + 397] ^ i >>> 1 ^ mag01[i & 1];
            }

            for (; i1 < 623; i1++) {
                int j = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + -227] ^ j >>> 1 ^ mag01[j & 1];
            }

            int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
            mti = 0;
        }
        int l = mt[mti++];
        l ^= l >>> 11;
        l ^= l << 7 & 0x9d2c5680;
        l ^= l << 15 & 0xefc60000;
        l ^= l >>> 18;
        return (float) (l >>> 8) / 1.677722E+07F < f;
    }

    public final boolean nextBoolean(double d) {
        if (d < 0.0D || d > 1.0D)
            throw new IllegalArgumentException("probability must be between 0.0 and 1.0 inclusive.");
        if (mti >= 624) {
            int i2;
            for (i2 = 0; i2 < 227; i2++) {
                int i = mt[i2] & 0x80000000 | mt[i2 + 1] & 0x7fffffff;
                mt[i2] = mt[i2 + 397] ^ i >>> 1 ^ mag01[i & 1];
            }

            for (; i2 < 623; i2++) {
                int j = mt[i2] & 0x80000000 | mt[i2 + 1] & 0x7fffffff;
                mt[i2] = mt[i2 + -227] ^ j >>> 1 ^ mag01[j & 1];
            }

            int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
            mti = 0;
        }
        int l = mt[mti++];
        l ^= l >>> 11;
        l ^= l << 7 & 0x9d2c5680;
        l ^= l << 15 & 0xefc60000;
        l ^= l >>> 18;
        if (mti >= 624) {
            int j2;
            for (j2 = 0; j2 < 227; j2++) {
                int i1 = mt[j2] & 0x80000000 | mt[j2 + 1] & 0x7fffffff;
                mt[j2] = mt[j2 + 397] ^ i1 >>> 1 ^ mag01[i1 & 1];
            }

            for (; j2 < 623; j2++) {
                int j1 = mt[j2] & 0x80000000 | mt[j2 + 1] & 0x7fffffff;
                mt[j2] = mt[j2 + -227] ^ j1 >>> 1 ^ mag01[j1 & 1];
            }

            int k1 = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k1 >>> 1 ^ mag01[k1 & 1];
            mti = 0;
        }
        int l1 = mt[mti++];
        l1 ^= l1 >>> 11;
        l1 ^= l1 << 7 & 0x9d2c5680;
        l1 ^= l1 << 15 & 0xefc60000;
        l1 ^= l1 >>> 18;
        return (double) (((long) (l >>> 6) << 27) + (long) (l1 >>> 5)) / 9007199254740992D < d;
    }

    public final byte nextByte() {
        if (mti >= 624) {
            int i1;
            for (i1 = 0; i1 < 227; i1++) {
                int i = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + 397] ^ i >>> 1 ^ mag01[i & 1];
            }

            for (; i1 < 623; i1++) {
                int j = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + -227] ^ j >>> 1 ^ mag01[j & 1];
            }

            int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
            mti = 0;
        }
        int l = mt[mti++];
        l ^= l >>> 11;
        l ^= l << 7 & 0x9d2c5680;
        l ^= l << 15 & 0xefc60000;
        l ^= l >>> 18;
        return (byte) (l >>> 24);
    }

    public final void nextBytes(byte abyte0[]) {
        for (int i1 = 0; i1 < abyte0.length; i1++) {
            if (mti >= 624) {
                int j1;
                for (j1 = 0; j1 < 227; j1++) {
                    int i = mt[j1] & 0x80000000 | mt[j1 + 1] & 0x7fffffff;
                    mt[j1] = mt[j1 + 397] ^ i >>> 1 ^ mag01[i & 1];
                }

                for (; j1 < 623; j1++) {
                    int j = mt[j1] & 0x80000000 | mt[j1 + 1] & 0x7fffffff;
                    mt[j1] = mt[j1 + -227] ^ j >>> 1 ^ mag01[j & 1];
                }

                int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
                mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
                mti = 0;
            }
            int l = mt[mti++];
            l ^= l >>> 11;
            l ^= l << 7 & 0x9d2c5680;
            l ^= l << 15 & 0xefc60000;
            l ^= l >>> 18;
            abyte0[i1] = (byte) (l >>> 24);
        }

    }

    public final long nextLong() {
        if (mti >= 624) {
            int i2;
            for (i2 = 0; i2 < 227; i2++) {
                int i = mt[i2] & 0x80000000 | mt[i2 + 1] & 0x7fffffff;
                mt[i2] = mt[i2 + 397] ^ i >>> 1 ^ mag01[i & 1];
            }

            for (; i2 < 623; i2++) {
                int j = mt[i2] & 0x80000000 | mt[i2 + 1] & 0x7fffffff;
                mt[i2] = mt[i2 + -227] ^ j >>> 1 ^ mag01[j & 1];
            }

            int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
            mti = 0;
        }
        int l = mt[mti++];
        l ^= l >>> 11;
        l ^= l << 7 & 0x9d2c5680;
        l ^= l << 15 & 0xefc60000;
        l ^= l >>> 18;
        if (mti >= 624) {
            int j2;
            for (j2 = 0; j2 < 227; j2++) {
                int i1 = mt[j2] & 0x80000000 | mt[j2 + 1] & 0x7fffffff;
                mt[j2] = mt[j2 + 397] ^ i1 >>> 1 ^ mag01[i1 & 1];
            }

            for (; j2 < 623; j2++) {
                int j1 = mt[j2] & 0x80000000 | mt[j2 + 1] & 0x7fffffff;
                mt[j2] = mt[j2 + -227] ^ j1 >>> 1 ^ mag01[j1 & 1];
            }

            int k1 = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k1 >>> 1 ^ mag01[k1 & 1];
            mti = 0;
        }
        int l1 = mt[mti++];
        l1 ^= l1 >>> 11;
        l1 ^= l1 << 7 & 0x9d2c5680;
        l1 ^= l1 << 15 & 0xefc60000;
        l1 ^= l1 >>> 18;
        return ((long) l << 32) + (long) l1;
    }

    public final double nextDouble() {
        if (mti >= 624) {
            int i2;
            for (i2 = 0; i2 < 227; i2++) {
                int i = mt[i2] & 0x80000000 | mt[i2 + 1] & 0x7fffffff;
                mt[i2] = mt[i2 + 397] ^ i >>> 1 ^ mag01[i & 1];
            }

            for (; i2 < 623; i2++) {
                int j = mt[i2] & 0x80000000 | mt[i2 + 1] & 0x7fffffff;
                mt[i2] = mt[i2 + -227] ^ j >>> 1 ^ mag01[j & 1];
            }

            int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
            mti = 0;
        }
        int l = mt[mti++];
        l ^= l >>> 11;
        l ^= l << 7 & 0x9d2c5680;
        l ^= l << 15 & 0xefc60000;
        l ^= l >>> 18;
        if (mti >= 624) {
            int j2;
            for (j2 = 0; j2 < 227; j2++) {
                int i1 = mt[j2] & 0x80000000 | mt[j2 + 1] & 0x7fffffff;
                mt[j2] = mt[j2 + 397] ^ i1 >>> 1 ^ mag01[i1 & 1];
            }

            for (; j2 < 623; j2++) {
                int j1 = mt[j2] & 0x80000000 | mt[j2 + 1] & 0x7fffffff;
                mt[j2] = mt[j2 + -227] ^ j1 >>> 1 ^ mag01[j1 & 1];
            }

            int k1 = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k1 >>> 1 ^ mag01[k1 & 1];
            mti = 0;
        }
        int l1 = mt[mti++];
        l1 ^= l1 >>> 11;
        l1 ^= l1 << 7 & 0x9d2c5680;
        l1 ^= l1 << 15 & 0xefc60000;
        l1 ^= l1 >>> 18;
        return (double) (((long) (l >>> 6) << 27) + (long) (l1 >>> 5)) / 9007199254740992D;
    }

    public final double nextGaussian() {
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        }
        double d;
        double d1;
        double d2;
        do {
            if (mti >= 624) {
                int i4;
                for (i4 = 0; i4 < 227; i4++) {
                    int i = mt[i4] & 0x80000000 | mt[i4 + 1] & 0x7fffffff;
                    mt[i4] = mt[i4 + 397] ^ i >>> 1 ^ mag01[i & 1];
                }

                for (; i4 < 623; i4++) {
                    int j = mt[i4] & 0x80000000 | mt[i4 + 1] & 0x7fffffff;
                    mt[i4] = mt[i4 + -227] ^ j >>> 1 ^ mag01[j & 1];
                }

                int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
                mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
                mti = 0;
            }
            int l = mt[mti++];
            l ^= l >>> 11;
            l ^= l << 7 & 0x9d2c5680;
            l ^= l << 15 & 0xefc60000;
            l ^= l >>> 18;
            if (mti >= 624) {
                int j4;
                for (j4 = 0; j4 < 227; j4++) {
                    int i1 = mt[j4] & 0x80000000 | mt[j4 + 1] & 0x7fffffff;
                    mt[j4] = mt[j4 + 397] ^ i1 >>> 1 ^ mag01[i1 & 1];
                }

                for (; j4 < 623; j4++) {
                    int j1 = mt[j4] & 0x80000000 | mt[j4 + 1] & 0x7fffffff;
                    mt[j4] = mt[j4 + -227] ^ j1 >>> 1 ^ mag01[j1 & 1];
                }

                int k1 = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
                mt[623] = mt[396] ^ k1 >>> 1 ^ mag01[k1 & 1];
                mti = 0;
            }
            int l1 = mt[mti++];
            l1 ^= l1 >>> 11;
            l1 ^= l1 << 7 & 0x9d2c5680;
            l1 ^= l1 << 15 & 0xefc60000;
            l1 ^= l1 >>> 18;
            if (mti >= 624) {
                int k4;
                for (k4 = 0; k4 < 227; k4++) {
                    int i2 = mt[k4] & 0x80000000 | mt[k4 + 1] & 0x7fffffff;
                    mt[k4] = mt[k4 + 397] ^ i2 >>> 1 ^ mag01[i2 & 1];
                }

                for (; k4 < 623; k4++) {
                    int j2 = mt[k4] & 0x80000000 | mt[k4 + 1] & 0x7fffffff;
                    mt[k4] = mt[k4 + -227] ^ j2 >>> 1 ^ mag01[j2 & 1];
                }

                int k2 = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
                mt[623] = mt[396] ^ k2 >>> 1 ^ mag01[k2 & 1];
                mti = 0;
            }
            int l2 = mt[mti++];
            l2 ^= l2 >>> 11;
            l2 ^= l2 << 7 & 0x9d2c5680;
            l2 ^= l2 << 15 & 0xefc60000;
            l2 ^= l2 >>> 18;
            if (mti >= 624) {
                int l4;
                for (l4 = 0; l4 < 227; l4++) {
                    int i3 = mt[l4] & 0x80000000 | mt[l4 + 1] & 0x7fffffff;
                    mt[l4] = mt[l4 + 397] ^ i3 >>> 1 ^ mag01[i3 & 1];
                }

                for (; l4 < 623; l4++) {
                    int j3 = mt[l4] & 0x80000000 | mt[l4 + 1] & 0x7fffffff;
                    mt[l4] = mt[l4 + -227] ^ j3 >>> 1 ^ mag01[j3 & 1];
                }

                int k3 = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
                mt[623] = mt[396] ^ k3 >>> 1 ^ mag01[k3 & 1];
                mti = 0;
            }
            int l3 = mt[mti++];
            l3 ^= l3 >>> 11;
            l3 ^= l3 << 7 & 0x9d2c5680;
            l3 ^= l3 << 15 & 0xefc60000;
            l3 ^= l3 >>> 18;
            d = 2D * ((double) (((long) (l >>> 6) << 27) + (long) (l1 >>> 5)) / 9007199254740992D) - 1.0D;
            d1 = 2D * ((double) (((long) (l2 >>> 6) << 27) + (long) (l3 >>> 5)) / 9007199254740992D) - 1.0D;
            d2 = d * d + d1 * d1;
        } while (d2 >= 1.0D || d2 == 0.0D);
        double d3 = Math.sqrt((-2D * Math.log(d2)) / d2);
        nextNextGaussian = d1 * d3;
        haveNextNextGaussian = true;
        return d * d3;
    }

    public final float nextFloat() {
        if (mti >= 624) {
            int i1;
            for (i1 = 0; i1 < 227; i1++) {
                int i = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + 397] ^ i >>> 1 ^ mag01[i & 1];
            }

            for (; i1 < 623; i1++) {
                int j = mt[i1] & 0x80000000 | mt[i1 + 1] & 0x7fffffff;
                mt[i1] = mt[i1 + -227] ^ j >>> 1 ^ mag01[j & 1];
            }

            int k = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
            mt[623] = mt[396] ^ k >>> 1 ^ mag01[k & 1];
            mti = 0;
        }
        int l = mt[mti++];
        l ^= l >>> 11;
        l ^= l << 7 & 0x9d2c5680;
        l ^= l << 15 & 0xefc60000;
        l ^= l >>> 18;
        return (float) (l >>> 8) / 1.677722E+07F;
    }

    public final int nextInt(int i) {
        if (i <= 0)
            throw new IllegalArgumentException("n must be positive");
        if ((i & -i) == i) {
            if (mti >= 624) {
                int k1;
                for (k1 = 0; k1 < 227; k1++) {
                    int j = mt[k1] & 0x80000000 | mt[k1 + 1] & 0x7fffffff;
                    mt[k1] = mt[k1 + 397] ^ j >>> 1 ^ mag01[j & 1];
                }

                for (; k1 < 623; k1++) {
                    int k = mt[k1] & 0x80000000 | mt[k1 + 1] & 0x7fffffff;
                    mt[k1] = mt[k1 + -227] ^ k >>> 1 ^ mag01[k & 1];
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
            return (int) ((long) i * (long) (i1 >>> 1) >> 31);
        }
        int j1;
        int l1;
        do {
            if (mti >= 624) {
                int i3;
                for (i3 = 0; i3 < 227; i3++) {
                    int i2 = mt[i3] & 0x80000000 | mt[i3 + 1] & 0x7fffffff;
                    mt[i3] = mt[i3 + 397] ^ i2 >>> 1 ^ mag01[i2 & 1];
                }

                for (; i3 < 623; i3++) {
                    int j2 = mt[i3] & 0x80000000 | mt[i3 + 1] & 0x7fffffff;
                    mt[i3] = mt[i3 + -227] ^ j2 >>> 1 ^ mag01[j2 & 1];
                }

                int k2 = mt[623] & 0x80000000 | mt[0] & 0x7fffffff;
                mt[623] = mt[396] ^ k2 >>> 1 ^ mag01[k2 & 1];
                mti = 0;
            }
            int l2 = mt[mti++];
            l2 ^= l2 >>> 11;
            l2 ^= l2 << 7 & 0x9d2c5680;
            l2 ^= l2 << 15 & 0xefc60000;
            l2 ^= l2 >>> 18;
            j1 = l2 >>> 1;
            l1 = j1 % i;
        } while ((j1 - l1) + (i - 1) < 0);
        return l1;
    }
}
