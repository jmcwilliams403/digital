/*
 * Copyright (c) 2022 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tommyettinger.digital;

import java.util.Random;

import static com.github.tommyettinger.digital.TrigTools.PI;
import static com.github.tommyettinger.digital.TrigTools.PI2;
import static com.github.tommyettinger.digital.TrigTools.PI2_D;

/**
 * Mathematical operations not provided by {@link Math java.lang.Math}.
 * <br>
 * Includes code that was originally part of the
 * <a href="http://maths.uncommons.org/">Uncommon Maths software package</a> as Maths.
 * Also includes code adapted from libGDX as their MathUtils class. There's also
 * {@link #cbrt(float)} by Marc B. Reynolds, building on the legendary fast inverse square root,
 * and a generalized bias/gain function, {@link #barronSpline(float, float, float)}, popularized by Jon Barron.
 * The {@link #fastFloor(float)} and {@link #fastCeil(float)} methods were devised by Riven on JavaGaming.org .
 * {@link #factorial(float)} and {@link #gamma(float)} are by T. J. Stieltjes.
 * The lerp methods that operate on angles are originally from MathUtils in libGDX, which credits Nathan Sweet.
 *
 * @author Daniel Dyer
 * @author Tommy Ettinger
 * @author Marc B. Reynolds
 * @author Jon Barron
 * @author Riven
 * @author T. J. Stieltjes
 * @author Nathan Sweet
 */
public final class MathTools {
    private MathTools() {
        // Prevent instantiation.
    }

    /**
     * A float that is meant to be used as the smallest reasonable tolerance for methods like {@link #isEqual(float, float, float)}.
     * This is sufficient if the rounding error is the result of one addition or subtraction between numbers smaller
     * than 16 (closer to 0). More math operations or larger numbers can produce larger rounding errors; you can try
     * multiplying this constant by, for instance, 64, and using that as the tolerance if you need precision with
     * three-digit numbers (16 * 64 is 1024, so 0 to 999 should be precise there). A larger rounding error can introduce
     * false-positive equivalence with very small inputs.
     */
    public static final float FLOAT_ROUNDING_ERROR = 0x1p-20f; // was 0.000001f
    
    /**
     * 2 to the -24 as a float; this is equal to {@code Math.ulp(0.5f)}, and is the smallest non-zero distance possible
     * between two results of {@link Random#nextFloat()}.
     * Useful for converting a 24-bit {@code int} or {@code long} value to a gradient between 0 and 1.
     */
    public static final float EPSILON = 0x1p-24f;
    
    /**
     * 2 to the -53 as a float; this is equal to {@code Math.ulp(0.5)}, and is the smallest non-zero distance possible
     * between two results of {@link Random#nextDouble()}.
     * Useful for converting a 53-bit {@code long} value to a gradient between 0 and 1.
     */
    public static final double EPSILON_D = 0x1p-53;

    /**
     * The {@code float} value that is closer than any other to
     * <i>e</i>, the base of the natural logarithms.
     */
    public static final float E = 2.7182818284590452354f;

    /**
     * The {@code float} value that is closer than any other to
     * {@code Math.sqrt(2.0)}, the ratio of the hypotenuse of an
     * isosceles right triangle to one of its legs.
     */
    public static final float ROOT2 = 1.4142135623730950488f;
    
    /**
     * The {@code double} value that is closer than any other to
     * {@code Math.sqrt(2.0)}, the ratio of the hypotenuse of an
     * isosceles right triangle to one of its legs.
     */
    public static final double ROOT2_D = 1.4142135623730950488;
    
    /**
     * The {@code float} value that is closer than any other to
     * {@code Math.sqrt(3.0)}, the ratio of the diagonal length
     * of a cube to its edge length.
     */
    public static final float ROOT3 = 1.7320508075688772935f;
    
    /**
     * The {@code double} value that is closer than any other to
     * {@code Math.sqrt(3.0)}, the ratio of the diagonal length
     * of a cube to its edge length.
     */
    public static final double ROOT3_D = 1.7320508075688772935;
    
    /**
     * The {@code float} value that is closer than any other to
     * {@code Math.sqrt(5.0)}, which has various useful properties,
     * such as appearing in many formulae involving the golden ratio
     * which is of course chiefly due to being part of its calculation.
     */
    public static final float ROOT5 = 2.2360679774997896964f;
    
    /**
     * The {@code double} value that is closer than any other to
     * {@code Math.sqrt(5.0)}, which has various useful properties,
     * such as appearing in many formulae involving the golden ratio
     * which is of course chiefly due to being part of its calculation.
     */
    public static final double ROOT5_D = 2.2360679774997896964;
    
    /**
     * The famous golden ratio, {@code (1.0 + Math.sqrt(5.0)) * 0.5}; this is the "most irrational" of irrational
     * numbers, and has various useful properties.
     * <br>
     * The same as {@link #PHI}.
     */
    public static final float GOLDEN_RATIO = 1.6180339887498949f;

    /**
     * The famous golden ratio, {@code (1.0 + Math.sqrt(5.0)) * 0.5}; this is the "most irrational" of irrational
     * numbers, and has various useful properties.
     * <br>
     * The same as {@link #GOLDEN_RATIO}.
     */
    public static final float PHI = GOLDEN_RATIO;
    
    /**
     * The famous golden ratio, {@code (1.0 + Math.sqrt(5.0)) * 0.5}, as a double; this is the "most irrational" of
     * irrational numbers, and has various useful properties.
     * <br>
     * The same as {@link #PHI_D}.
     */
    public static final double GOLDEN_RATIO_D = 1.6180339887498949;

    /**
     * The famous golden ratio, {@code (1.0 + Math.sqrt(5.0)) * 0.5}, as a double; this is the "most irrational" of
     * irrational numbers, and has various useful properties.
     * <br>
     * The same as {@link #GOLDEN_RATIO_D}.
     */
    public static final double PHI_D = GOLDEN_RATIO_D;
    
    /**
     * The inverse of the golden ratio, {@code (1.0 - Math.sqrt(5.0)) * -0.5} or {@code GOLDEN_RATIO - 1.0}; this also
     * has various useful properties.
     */
    public static final float GOLDEN_RATIO_INVERSE = 0.6180339887498949f;

    /**
     * The inverse of the golden ratio, {@code (1.0 - Math.sqrt(5.0)) * -0.5} or {@code GOLDEN_RATIO - 1.0}, as a
     * double; this also has various useful properties.
     */
    public static final double GOLDEN_RATIO_INVERSE_D = 0.6180339887498949;
    
    /**
     * The conjugate of the golden ratio, {@code (1.0 - Math.sqrt(5.0)) * 0.5} or {@code 1.0 - GOLDEN_RATIO}; this also
     * has various useful properties.
     */
    public static final float PSI = -GOLDEN_RATIO_INVERSE;

    /**
     * The conjugate of the golden ratio, {@code (1.0 - Math.sqrt(5.0)) * 0.5} or {@code 1.0 - GOLDEN_RATIO}, as a
     * double; this also has various useful properties.
     */
    public static final double PSI_D = -GOLDEN_RATIO_INVERSE_D;

    private static final int BIG_ENOUGH_INT = 16384;
    private static final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;
    private static final double CEIL = 0x1.fffffep-1f; // was 0.9999999
    private static final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5f;

    /**
     * Calculate the first argument raised to the power of the second.
     * This method only supports non-negative powers.
     *
     * @param value The number to be raised.
     * @param power The exponent (must be positive).
     * @return {@code value} raised to {@code power}.
     */
    public static long raiseToPower(int value, int power) {
        if (power < 0) {
            throw new IllegalArgumentException("This method does not support negative powers.");
        }
        long result = 1;
        for (int i = 0; i < power; i++) {
            result *= value;
        }
        return result;
    }


    /**
     * Calculate logarithms for arbitrary bases.
     *
     * @param base The base for the logarithm.
     * @param arg  The value to calculate the logarithm for.
     * @return The log of {@code arg} in the specified {@code base}.
     */
    public static double log(double base, double arg) {
        // Use natural logarithms and change the base.
        return Math.log(arg) / Math.log(base);
    }

    /**
     * Calculate logarithms for arbitrary bases.
     *
     * @param base  the logarithm base to use
     * @param value what value to get the logarithm of, using the given base
     * @return the logarithm of value with the given base
     */
    public static float log(float base, float value) {
        return (float) (Math.log(value) / Math.log(base));
    }

    /**
     * @param value what value to get the logarithm of, using base 2
     * @return the logarithm of value with base 2
     */
    public static float log2(float value) {
        return (float) (Math.log(value) / 0.6931471805599453);
    }

    /**
     * Equivalent to libGDX's isEqual() method in MathUtils; this compares two doubles for equality and allows the given
     * tolerance during comparison. An example is {@code 0.3 - 0.2 == 0.1} vs. {@code isEqual(0.3 - 0.2, 0.1, 0.000001)};
     * the first is incorrectly false, while the second is correctly true.
     *
     * @param a         the first float to compare
     * @param b         the second float to compare
     * @param tolerance the maximum difference between a and b permitted for this to return true, inclusive
     * @return true if a and b have a difference less than or equal to tolerance, or false otherwise.
     */
    public static boolean isEqual(double a, double b, double tolerance) {
        return Math.abs(a - b) <= tolerance;
    }

    /**
     * Equivalent to libGDX's isEqual() method in MathUtils; this compares two floats for equality and allows just enough
     * tolerance to ignore a rounding error. An example is {@code 0.3f - 0.2f == 0.1f} vs. {@code isEqual(0.3f - 0.2f, 0.1f)};
     * the first is incorrectly false, while the second is correctly true. This uses {@link #FLOAT_ROUNDING_ERROR} as
     * its tolerance.
     *
     * @param a the first float to compare
     * @param b the second float to compare
     * @return true if a and b are equal or extremely close to equal, or false otherwise.
     */
    public static boolean isEqual(float a, float b) {
        return Math.abs(a - b) <= FLOAT_ROUNDING_ERROR;
    }

    /**
     * Equivalent to libGDX's isEqual() method in MathUtils; this compares two floats for equality and allows the given
     * tolerance during comparison. An example is {@code 0.3f - 0.2f == 0.1f} vs. {@code isEqual(0.3f - 0.2f, 0.1f, 0.000001f)};
     * the first is incorrectly false, while the second is correctly true. See {@link #FLOAT_ROUNDING_ERROR} for advice
     * on choosing a value for tolerance.
     *
     * @param a         the first float to compare
     * @param b         the second float to compare
     * @param tolerance the maximum difference between a and b permitted for this to return true, inclusive
     * @return true if a and b have a difference less than or equal to tolerance, or false otherwise.
     */
    public static boolean isEqual(float a, float b, float tolerance) {
        return Math.abs(a - b) <= tolerance;
    }

    /**
     * If the specified value is not greater than or equal to the specified minimum and
     * less than or equal to the specified maximum, adjust it so that it is.
     * <br>
     * Note that it can often be just as easy to directly call the same code this calls, while being slightly friendlier
     * to inlining in large methods: {@code Math.min(Math.max(value, min), max)}.
     *
     * @param value The value to check.
     * @param min   The minimum permitted value.
     * @param max   The maximum permitted value.
     * @return {@code value} if it is between the specified limits, {@code min} if the value
     * is too low, or {@code max} if the value is too high.
     */
    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * If the specified value is not greater than or equal to the specified minimum and
     * less than or equal to the specified maximum, adjust it so that it is.
     * <br>
     * Note that it can often be just as easy to directly call the same code this calls, while being slightly friendlier
     * to inlining in large methods: {@code Math.min(Math.max(value, min), max)}.
     *
     * @param value The value to check.
     * @param min   The minimum permitted value.
     * @param max   The maximum permitted value.
     * @return {@code value} if it is between the specified limits, {@code min} if the value
     * is too low, or {@code max} if the value is too high.
     */
    public static long clamp(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }
    
    /**
     * If the specified value is not greater than or equal to the specified minimum and
     * less than or equal to the specified maximum, adjust it so that it is.
     * <br>
     * Note that it can often be just as easy to directly call the same code this calls, while being slightly friendlier
     * to inlining in large methods: {@code Math.min(Math.max(value, min), max)}.
     *
     * @param value The value to check.
     * @param min   The minimum permitted value.
     * @param max   The maximum permitted value.
     * @return {@code value} if it is between the specified limits, {@code min} if the value
     * is too low, or {@code max} if the value is too high.
     */
    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * If the specified value is not greater than or equal to the specified minimum and
     * less than or equal to the specified maximum, adjust it so that it is.
     * <br>
     * Note that it can often be just as easy to directly call the same code this calls, while being slightly friendlier
     * to inlining in large methods: {@code Math.min(Math.max(value, min), max)}.
     *
     * @param value The value to check.
     * @param min   The minimum permitted value.
     * @param max   The maximum permitted value.
     * @return {@code value} if it is between the specified limits, {@code min} if the value
     * is too low, or {@code max} if the value is too high.
     */
    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Like the modulo operator {@code %}, but the result will always match the sign of {@code d} instead of {@code op}.
     *
     * @param op the dividend; negative values are permitted and wrap instead of producing negative results
     * @param d  the divisor; if this is negative then the result will be negative, otherwise it will be positive
     * @return the remainder of the division of op by d, with a sign matching d
     */
    public static float remainder(final float op, final float d) {
        return (op % d + d) % d;
    }

    /**
     * Like the modulo operator {@code %}, but the result will always match the sign of {@code d} instead of {@code op}.
     *
     * @param op the dividend; negative values are permitted and wrap instead of producing negative results
     * @param d  the divisor; if this is negative then the result will be negative, otherwise it will be positive
     * @return the remainder of the division of op by d, with a sign matching d
     */
    public static double remainder(final double op, final double d) {
        return (op % d + d) % d;
    }

    /**
     * Determines the greatest common divisor of a pair of natural numbers
     * using the Euclidean algorithm.  This method only works with natural
     * numbers.  If negative integers are passed in, the absolute values will
     * be used.  The return value is always positive.
     *
     * @param a The first value.
     * @param b The second value.
     * @return The greatest common divisor.
     */
    public static long greatestCommonDivisor(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /**
     * Given any odd int {@code a}, this finds another odd int {@code b} such that {@code a * b == 1}.
     * <br>
     * This is incompatible with GWT, but it should usually only find uses in exploratory code or in tests anyway...
     * It is only incompatible because it tends to rely on multiplication overflow to work. The overload that takes a
     * long and gets the inverse modulo (2 to the 64) is GWT-compatible.
     *
     * @param a any odd int; note that even numbers do not have inverses modulo 2 to the 32
     * @return the multiplicative inverse of {@code a} modulo 4294967296 (or, 2 to the 32)
     */
    public static int modularMultiplicativeInverse(final int a) {
        int x = 2 ^ a * 3;
        x *= 2 - a * x;
        x *= 2 - a * x;
        x *= 2 - a * x;
        return x;
    }

    /**
     * Given any odd long {@code a}, this finds another odd long {@code b} such that {@code a * b == 1L}.
     *
     * @param a any odd long; note that even numbers do not have inverses modulo 2 to the 64
     * @return the multiplicative inverse of {@code a} modulo 18446744073709551616 (or, 2 to the 64)
     */
    public static long modularMultiplicativeInverse(final long a) {
        long x = 2 ^ a * 3;
        x *= 2 - a * x;
        x *= 2 - a * x;
        x *= 2 - a * x;
        x *= 2 - a * x;
        return x;
    }

    /**
     * Integer square root (using floor), maintaining correct results even for very large {@code long} values. This
     * version treats negative inputs as unsigned and returns positive square roots for them (these are usually large).
     * <br>
     * This is based on <a href="https://github.com/python/cpython/pull/13244">code used by Python</a>, but
     * isn't identical. Notably, this doesn't branch except in the for loop, and it handles negative inputs differently.
     *
     * @param n a {@code long} value that will be treated as if unsigned
     * @return the square root of n, rounded down to the next lower {@code long} if the result isn't already a {@code long}
     */
    public static long isqrt(final long n) {
        final int c = 63 - Long.numberOfLeadingZeros(n) >> 1;
        long a = 1, d = 0, e;
        for (int s = 31 & 32 - Integer.numberOfLeadingZeros(c); s > 0; ) {
            e = d;
            d = c >>> --s;
            a = (a << d - e - 1) + (n >>> c + c - e - d + 1) / a;
        }
        return a - (n - a * a >>> 63);
    }

    /**
     * An approximation of the cube-root function for float inputs and outputs.
     * This can be about twice as fast as {@link Math#cbrt(double)}. It
     * correctly returns negative results when given negative inputs.
     * <br>
     * Has very low relative error (less than 1E-9) when inputs are uniformly
     * distributed between -512 and 512, and absolute mean error of less than
     * 1E-6 in the same scenario. Uses a bit-twiddling method similar to one
     * presented in Hacker's Delight and also used in early 3D graphics (see
     * <a href="https://en.wikipedia.org/wiki/Fast_inverse_square_root">Wikipedia</a> for more, but
     * this code approximates cbrt(x) and not 1/sqrt(x)). This specific code
     * was originally by Marc B. Reynolds, posted in his
     * <a href="https://github.com/Marc-B-Reynolds/Stand-alone-junk/blob/master/src/Posts/ballcube.c#L182-L197">"Stand-alone-junk" repo</a> .
     * <br>
     * If you need to work with doubles, or need higher precision, use {@link Math#cbrt(double)}.
     * @param x any finite float to find the cube root of
     * @return the cube root of x, approximated
     */
    public static float cbrt(float x) {
        int ix = BitConversion.floatToIntBits(x);
        final int sign = ix & 0x80000000;
        ix &= 0x7FFFFFFF;
        final float x0 = x;
        ix = (ix >>> 2) + (ix >>> 4);
        ix += (ix >>> 4);
        ix = ix + (ix >>> 8) + 0x2A5137A0 | sign;
        x = BitConversion.intBitsToFloat(ix);
        x = 0.33333334f * (2f * x + x0 / (x * x));
        x = 0.33333334f * (2f * x + x0 / (x * x));
        return x;
    }

    /**
     * The nth root of x, taking advantage of how any nth root can be written as raising x to power of 1/n.
     * <br>
     * For a detailed description of how this function handles negative roots and roots of negative numbers,
     * refer to the documentation for {@link Math#pow(double, double)}.
     * <br>
     * Additionally, this function aliases results which are extremely close to mathematical integers to actual
     * {@code int} values to compensate for any internal precision loss from {@code Math.pow(double, double)}.
     * 
     * @param x a number to find the nth root of
     * @param n the degree of the root
     * @return a number which, when raised to the power n, yields x
     */
    public static float nthrt(final float x, final float n) {        
        float f = (float) Math.pow(x, 1f / n);
        if (Float.isNaN(f) || Float.isInfinite(f))
            return f;
        int i = round(f);
        return isEqual(i, f) ? i : f;
    }
    
    /**
     * Fast inverse square root, best known for its implementation in Quake III Arena.
     * This is an algorithm that estimates the {@code float} value of 1/sqrt(x). It has
     * comparable performance to the more-straightforward {@code 1f/(float)Math.sqrt(x)}
     * on HotSpot JDKs, but this method outperforms the Math-based approach by over 40%
     * on GraalVM 17. Some other platforms, such as Android and GWT, may have similar or
     * very different performance relative to using Math, so if you expect to use this
     * method often, you should test it in your app on the platforms you target.
     * Precision will always be best with Math.
     * <br>
     * It is often used for vector normalization, i.e. scaling it to a length of 1.
     * For example, it can be used to compute angles of incidence and reflection for
     * lighting and shading.
     * <br>
     * For more information, see <a href="https://en.wikipedia.org/wiki/Fast_inverse_square_root">Wikipedia</a>
     *
     * @param x a non-negative finite float to find the inverse square root of
     * @return the inverse square root of x, approximated
     */
    public static float invSqrt(float x) {
        int i = 0x5F3759DF - (BitConversion.floatToIntBits(x) >> 1);
        float y = BitConversion.intBitsToFloat(i);
        return y * (1.5f - 0.5f * x * y * y);
    }

    /**
     * Fast inverse square root, best known for its implementation in Quake III Arena.
     * This is an algorithm that estimates the {@code double} value of 1/sqrt(x). It has
     * comparable performance to the more-straightforward {@code 1.0/Math.sqrt(x)}
     * on HotSpot JDKs, but this method may outperform the Math-based approach on GraalVM
     * 17 (the float version, {@link #invSqrt(float)}, does so by 40% or more). Some other
     * platforms, such as Android and GWT, may have similar or
     * very different performance relative to using Math, so if you expect to use this
     * method often, you should test it in your app on the platforms you target.
     * Precision will always be best with Math.
     * <br>
     * It is often used for vector normalization, i.e. scaling it to a length of 1.
     * For example, it can be used to compute angles of incidence and reflection for
     * lighting and shading.
     * <br>
     * For more information, see <a href="https://en.wikipedia.org/wiki/Fast_inverse_square_root">Wikipedia</a>
     *
     * @param x a non-negative finite double to find the inverse square root of
     * @return the inverse square root of x, approximated
     */
    public static double invSqrt(double x) {
        long i = 0x5FE6EC85E7DE30DAL - (BitConversion.doubleToLongBits(x) >> 1);
        double y = BitConversion.longBitsToDouble(i);
        return y * (1.5 - 0.5 * x * y * y);
    }
    /**
     * A generalization on bias and gain functions that can represent both; this version is branch-less.
     * This is based on <a href="https://arxiv.org/abs/2010.09714">this micro-paper</a> by Jon Barron, which
     * generalizes the earlier bias and gain rational functions by Schlick. The second and final page of the
     * paper has useful graphs of what the s (shape) and t (turning point) parameters do; shape should be 0
     * or greater, while turning must be between 0 and 1, inclusive. This effectively combines two different
     * curving functions so that they continue into each other when x equals turning. The shape parameter will
     * cause this to imitate "smoothstep-like" splines when greater than 1 (where the values ease into their
     * starting and ending levels), or to be the inverse when less than 1 (where values start like square
     * root does, taking off very quickly, but also end like square does, landing abruptly at the ending
     * level). You should only give x values between 0 and 1, inclusive.
     *
     * @param x       progress through the spline, from 0 to 1, inclusive
     * @param shape   must be greater than or equal to 0; values greater than 1 are "normal interpolations"
     * @param turning a value between 0.0 and 1.0, inclusive, where the shape changes
     * @return a float between 0 and 1, inclusive
     */
    public static float barronSpline(final float x, final float shape, final float turning) {
        final float d = turning - x;
        final int f = BitConversion.floatToIntBits(d) >> 31, n = f | 1;
        return ((turning * n - f) * (x + f)) / (Float.MIN_NORMAL - f + (x + shape * d) * n) - f;
    }

    /**
     * A generalization on bias and gain functions that can represent both; this version is branch-less.
     * This is based on <a href="https://arxiv.org/abs/2010.09714">this micro-paper</a> by Jon Barron, which
     * generalizes the earlier bias and gain rational functions by Schlick. The second and final page of the
     * paper has useful graphs of what the s (shape) and t (turning point) parameters do; shape should be 0
     * or greater, while turning must be between 0 and 1, inclusive. This effectively combines two different
     * curving functions so they continue into each other when x equals turning. The shape parameter will
     * cause this to imitate "smoothstep-like" splines when greater than 1 (where the values ease into their
     * starting and ending levels), or to be the inverse when less than 1 (where values start like square
     * root does, taking off very quickly, but also end like square does, landing abruptly at the ending
     * level). You should only give x values between 0 and 1, inclusive.
     *
     * @param x       progress through the spline, from 0 to 1, inclusive
     * @param shape   must be greater than or equal to 0; values greater than 1 are "normal interpolations"
     * @param turning a value between 0.0 and 1.0, inclusive, where the shape changes
     * @return a double between 0 and 1, inclusive
     */
    public static double barronSpline(final double x, final double shape, final double turning) {
        final double d = turning - x;
        final int f = BitConversion.doubleToHighIntBits(d) >> 31, n = f | 1;
        return ((turning * n - f) * (x + f)) / (Double.MIN_NORMAL - f + (x + shape * d) * n) - f;
    }

    /**
     * Returns the next higher power of two relative to {@code n}, or n if it is already a power of two. This returns 2
     * if n is any value less than 2 (including negative numbers, but also 1, which is a power of two).
     *
     * @param n the lower bound for the result
     * @return the next higher power of two that is greater than or equal to n
     */
    public static int nextPowerOfTwo(final int n) {
        return 1 << -Integer.numberOfLeadingZeros(Math.max(2, n) - 1);
    }

    /**
     * Returns true if {@code value} is a power of two or is equal to {@link Integer#MIN_VALUE}; false otherwise.
     *
     * @param value any int
     * @return true if {@code value} is a power of two (when treated as unsigned)
     */
    public static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & value - 1) == 0;
    }

    /**
     * A close approximation to the gamma function for positive doubles, using an algorithm by T. J. Stieltjes.
     * <a href="http://www.luschny.de/math/factorial/approx/SimpleCases.html">Source here</a>. This is exactly
     * equivalent to {@code MathExtras.factorial(x - 1.0)}.
     * @param x a real number; should usually be positive
     * @return the approximate gamma of the given x
     */
    public static double gamma(double x) {
        return factorial(x - 1.0);
    }

    /**
     * A close approximation to the factorial function for real numbers, using an algorithm by T. J. Stieltjes.
     * This performs a variable number of multiplications that starts at 1 when x is between 5 and 6, and requires more
     * multiplications the lower x goes (to potentially many if x is, for instance, -1000.0, which would need 1006
     * multiplications per call). As such, you should try to call this mostly on x values that are positive or have a
     * low magnitude. <a href="http://www.luschny.de/math/factorial/approx/SimpleCases.html">Source here</a>.
     * @param x a real number; should not be both large and negative
     * @return the generalized factorial of the given x
     */
    public static double factorial(double x) {
        double y = x + 1.0, p = 1.0;
        for (; y < 7; y++)
            p *= y;
        double r = Math.exp(y * Math.log(y) - y + 1.0 / (12.0 * y + 2.0 / (5.0 * y + 53.0 / (42.0 * y))));
        if (x < 7.0) r /= p;
        return r * Math.sqrt(PI2_D / y);
    }

    /**
     * A close approximation to the gamma function for positive floats, using an algorithm by T. J. Stieltjes.
     * <a href="http://www.luschny.de/math/factorial/approx/SimpleCases.html">Source here</a>. This is exactly
     * equivalent to {@code MathExtras.factorial(x - 1f)}.
     * <br>
     * This does all of its math on doubles internally and only casts to float at the end.
     *
     * @param x a real number; should usually be positive
     * @return the approximate gamma of the given x
     */
    public static float gamma(float x) {
        return (float) factorial(x - 1.0);
    }

    /**
     * A close approximation to the factorial function for real numbers, using an algorithm by T. J. Stieltjes.
     * This performs a variable number of multiplications that starts at 1 when x is between 5 and 6, and requires more
     * multiplications the lower x goes (to potentially many if x is, for instance, -1000.0, which would need 1006
     * multiplications per call). As such, you should try to call this mostly on x values that are positive or have a
     * low magnitude. <a href="http://www.luschny.de/math/factorial/approx/SimpleCases.html">Source here</a>.
     * <br>
     * This does all of its math on doubles internally and only casts to float at the end.
     *
     * @param x a real number; should not be both large and negative
     * @return the generalized factorial of the given x
     */
    public static float factorial(float x) {
        double y = x + 1.0, p = 1.0;
        for (; y < 7; y++)
            p *= y;
        double r = Math.exp(y * Math.log(y) - y + 1.0 / (12.0 * y + 2.0 / (5.0 * y + 53.0 / (42.0 * y))));
        if (x < 7.0) r /= p;
        return (float) (r * Math.sqrt(PI2_D / y));
    }

    /**
     * Binet's formula for the Fibonacci sequence, which is a closed-form expression where which each resulting value
     * is the sum of the two proceeding values. This has several useful applications, such as finding values within
     * Pascal's triangle, which is itself useful in various areas of mathematics involving polynomial functions.
     * <br>
     * Negative inputs are allowed here, but may behave differently than positive inputs. When given non-negative
     * integer inputs, this is only correct for inputs from 0 to 46 inclusive; the largest Fibonacci number this can
     * correctly calculate is 1836311903, given an input of 46. You can get a larger range of values by passing a
     * {@code long} input to {@link #fibonacci(long)}.
     * <br>
     * For more information see <a href="https://en.wikipedia.org/wiki/Fibonacci_number#Closed-form_expression">Wikipedia</a>.
     * This does not use the exact constant values in the "Computation by rounding" section, because extremely small
     * adjustments to those constants proved to counterbalance accrued floating-point error for a few more inputs.
     *
     * @param n an int index; should be less than 47
     * @return the Fibonacci number at index n, as an int
     */
    public static int fibonacci(int n) {
        return (int) ((Math.pow(1.618033988749895, n)) / 2.236067977499795 + 0.49999999999999917);
    }

    /**
     * Binet's formula for the Fibonacci sequence, which is a closed-form expression where which each resulting value
     * is the sum of the two proceeding values. This has several useful applications, such as finding values within
     * Pascal's triangle, which is itself useful in various areas of mathematics involving polynomial functions.
     * <br>
     * Negative inputs are allowed here, but may behave differently than positive inputs. When given non-negative
     * integer inputs, this is only correct for inputs from 0 to 77 inclusive; the largest Fibonacci number this can
     * correctly calculate is 5527939700884757, given an input of 77. This means that all Fibonacci numbers that can be
     * stored in a non-negative {@code int} can be produced by this method, as well as a substantial amount of
     * non-negative {@code long} Fibonacci numbers. If you only have inputs that are less than 47, and you want
     * {@code int} results, you can use {@link #fibonacci(int)} instead.
     * <br>
     * For more information see <a href="https://en.wikipedia.org/wiki/Fibonacci_number#Closed-form_expression">Wikipedia</a>.
     * This does not use the exact constant values in the "Computation by rounding" section, because extremely small
     * adjustments to those constants proved to counterbalance accrued floating-point error for a few more inputs.
     *
     * @param n a long index; should be less than 78
     * @return the Fibonacci number at index n, as a long
     */
    public static long fibonacci(long n) {
        return (long) ((Math.pow(1.618033988749895, n)) / 2.236067977499795 + 0.49999999999999917);
    }

    /**
     * Returns the square (second power) of its parameter. Purely here for convenience.
     * @param n any float
     * @return {@code n * n}
     */
    public static float square(final float n) {
        return n * n;
    }

    /**
     * Returns the square (second power) of its parameter. Purely here for convenience.
     * @param n any double
     * @return {@code n * n}
     */
    public static double square(final double n) {
        return n * n;
    }

    /**
     * Returns the cube (third power) of its parameter. Purely here for convenience.
     * @param n any float
     * @return {@code n * n * n}
     */
    public static float cube(final float n) {
        return n * n * n;
    }

    /**
     * Returns the cube (third power) of its parameter. Purely here for convenience.
     * @param n any double
     * @return {@code n * n * n}
     */
    public static double cube(final double n) {
        return n * n * n;
    }

    /**
     * Like {@link Math#floor}, but returns a long.
     * Doesn't consider "weird doubles" like INFINITY and NaN.
     * This is only faster than {@code (long)Math.floor(t)} on Java 8 for supported desktop platforms.
     *
     * @param t the double to find the floor for
     * @return the floor of t, as a long
     */
    public static long longFloor(final double t) {
        final long z = (long) t;
        return t < z ? z - 1L : z;
    }

    /**
     * Like {@link Math#floor(double)}, but takes a float and returns a long.
     * Doesn't consider "weird floats" like INFINITY and NaN.
     * This is only faster than {@code (long)Math.floor(t)} on Java 8 for supported desktop platforms.
     *
     * @param t the double to find the floor for
     * @return the floor of t, as a long
     */
    public static long longFloor(final float t) {
        final long z = (long) t;
        return t < z ? z - 1L : z;
    }

    /**
     * Like {@link Math#floor(double)} , but returns an int.
     * Doesn't consider "weird doubles" like INFINITY and NaN.
     * This is only faster than {@code (int)Math.floor(t)} on Java 8 for supported desktop platforms.
     *
     * @param t the float to find the floor for
     * @return the floor of t, as an int
     */
    public static int floor(final double t) {
        final int z = (int) t;
        return t < z ? z - 1 : z;
    }

    /**
     * Like {@link Math#floor(double)}, but takes a float and returns an int.
     * Doesn't consider "weird floats" like INFINITY and NaN. This method will only properly floor
     * floats from {@code -16384} to {@code Integer.MAX_VALUE - 16384}, or {@code 2147467263}.
     * Unlike {@link #floor(double)}, {@link #longFloor(float)}, and {@link #longFloor(double)},
     * this is significantly faster than {@code (int)Math.floor(t)}.
     * <br>
     * Taken from libGDX MathUtils.
     *
     * @param t a float from -16384 to 2147467263 (both inclusive)
     * @return the floor of t, as an int
     */
    public static int fastFloor(final float t) {
        return ((int) (t + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT);
    }

    /**
     * Like {@link Math#ceil(double)}, but returns an int.
     * Doesn't consider "weird doubles" like INFINITY and NaN.
     * This is only faster than {@code (int)Math.ceil(t)} on Java 8 for supported desktop platforms.
     *
     * @param t the float to find the ceiling for
     * @return the ceiling of t, as an int
     */
    public static int ceil(final double t) {
        final int z = (int) t;
        return t > z ? z + 1 : z;
    }

    /**
     * Like {@link Math#ceil(double)}, but takes a float and returns an int.
     * Doesn't consider "weird floats" like INFINITY and NaN. This method will only properly ceil
     * floats from {@code -16384} to {@code Integer.MAX_VALUE - 16384}, or {@code 2147467263}.
     * Unlike {@link #ceil(float)}, this is significantly faster than {@code (int)Math.ceil(t)}.
     *
     * @param t the float to find the ceiling for
     * @return the ceiling of t, as an int
     */
    public static int fastCeil(final float t) {
        return BIG_ENOUGH_INT - (int) (BIG_ENOUGH_FLOOR - t);
    }

    /**
     * Returns the largest int less than or equal to the specified float.
     * Doesn't consider "weird floats" like INFINITY and NaN.
     * This is only faster than {@code (int)Math.floor(t)} on Java 8 for supported desktop platforms.
     * <br>
     * Taken from libGDX MathUtils.
     *
     * @param value any float
     * @return the floor of value, as an int
     */
    public static int floor(float value) {
        final int z = (int) value;
        return value < z ? z - 1 : z;
    }

    /**
     * Returns the largest int less than or equal to the specified float. This method will only properly floor floats that are
     * positive. Note, this method simply casts the float to int.
     *
     * @param value any positive float
     */
    public static int floorPositive(float value) {
        return (int) value;
    }

    /**
     * Returns the smallest int greater than or equal to the specified float.
     * Doesn't consider "weird floats" like INFINITY and NaN.
     * This is only faster than {@code (int)Math.ceil(t)} on Java 8 for supported desktop platforms.
     *
     * @param value a float from -(2^14) to (Float.MAX_VALUE - 2^14)
     */
    public static int ceil(float value) {
        final int z = (int) value;
        return value > z ? z + 1 : z;
    }

    /**
     * Returns the smallest integer greater than or equal to the specified float. This method will only properly ceil floats that
     * are positive.
     *
     * @param value any positive float
     */
    public static int ceilPositive(float value) {
        return (int) (value + CEIL);
    }

    /**
     * Returns the closest integer to the specified float. This method will only properly round floats from -(2^14) to
     * (Float.MAX_VALUE - 2^14).
     *
     * @param value a float from -(2^14) to (Float.MAX_VALUE - 2^14)
     */
    public static int round(float value) {
        return (int) (value + BIG_ENOUGH_ROUND) - BIG_ENOUGH_INT;
    }

    /**
     * Returns the closest integer to the specified float. This method will only properly round floats that are positive.
     *
     * @param value any positive float
     */
    public static int roundPositive(float value) {
        return (int) (value + 0.5f);
    }

    /**
     * Forces precision loss on the given float so very small fluctuations away from an integer will be erased.
     * This is meant primarily for cleaning up floats, so they can be presented without needing scientific notation.
     * It leaves about 3 decimal digits after the point intact, and should make any digits after that simply 0.
     *
     * @param n any float, but typically a fairly small one (between -8 and 8, as a guideline)
     * @return {@code n} with its 13 least significant bits effectively removed
     */
    public static float truncate(final float n) {
        long i = (long) (n * 0x1p13f); // 0x1p13f is 2 raised to the 13 as a float, or 8192.0f
        return i * 0x1p-13f;           // 0x1p-13f is 1 divided by (2 raised to the 13) as a float, or 1.0f/8192.0f
    }

    /**
     * Forces precision loss on the given double so very small fluctuations away from an integer will be erased.
     * This is meant primarily for cleaning up doubles, so they can be presented without needing scientific notation.
     * It leaves about 3 decimal digits after the point intact, and should make any digits after that simply 0.
     *
     * @param n any double, but typically a fairly small one (between -8 and 8, as a guideline)
     * @return {@code n} with its 42 least significant bits effectively removed
     */
    public static double truncate(final double n) {
        long i = (long) (n * 0x1p42); // 0x1p42 is 2 raised to the 42 as a double
        return i * 0x1p-42;          // 0x1p-42 is 1 divided by (2 raised to the 42) as a double
    }

    /**
     * Linearly interpolates between fromValue to toValue on progress position.
     *
     * @param fromValue starting float value; can be any finite float
     * @param toValue   ending float value; can be any finite float
     * @param progress  how far the interpolation should go, between 0 (equal to fromValue) and 1 (equal to toValue)
     */
    public static float lerp(final float fromValue, final float toValue, final float progress) {
        return fromValue + (toValue - fromValue) * progress;
    }

    /**
     * Linearly normalizes value from a range. Range must not be empty. This is the inverse of {@link #lerp(float, float, float)}.
     *
     * @param rangeStart range start normalized to 0
     * @param rangeEnd   range end normalized to 1
     * @param value      value to normalize
     * @return normalized value; values outside the range are not clamped to 0 and 1
     */
    public static float norm(float rangeStart, float rangeEnd, float value) {
        return (value - rangeStart) / (rangeEnd - rangeStart);
    }

    /**
     * Linearly map a value from one range to another. Input range must not be empty. This is the same as chaining
     * {@link #norm(float, float, float)} from input range and {@link #lerp(float, float, float)} to output range.
     *
     * @param inRangeStart  input range start
     * @param inRangeEnd    input range end
     * @param outRangeStart output range start
     * @param outRangeEnd   output range end
     * @param value         value to map
     * @return mapped value; values outside the input range are not clamped to output range
     */
    public static float map(float inRangeStart, float inRangeEnd, float outRangeStart, float outRangeEnd, float value) {
        return outRangeStart + (value - inRangeStart) * (outRangeEnd - outRangeStart) / (inRangeEnd - inRangeStart);
    }

    /**
     * Linearly interpolates between fromValue to toValue on progress position.
     *
     * @param fromValue starting double value; can be any finite double
     * @param toValue   ending double value; can be any finite double
     * @param progress  how far the interpolation should go, between 0 (equal to fromValue) and 1 (equal to toValue)
     */
    public static double lerp(final double fromValue, final double toValue, final double progress) {
        return fromValue + (toValue - fromValue) * progress;
    }

    /**
     * Linearly normalizes value from a range. Range must not be empty. This is the inverse of {@link #lerp(double, double, double)}.
     *
     * @param rangeStart range start normalized to 0
     * @param rangeEnd   range end normalized to 1
     * @param value      value to normalize
     * @return normalized value; values outside the range are not clamped to 0 and 1
     */
    public static double norm(double rangeStart, double rangeEnd, double value) {
        return (value - rangeStart) / (rangeEnd - rangeStart);
    }

    /**
     * Linearly map a value from one range to another. Input range must not be empty. This is the same as chaining
     * {@link #norm(double, double, double)} from input range and {@link #lerp(double, double, double)} to output range.
     *
     * @param inRangeStart  input range start
     * @param inRangeEnd    input range end
     * @param outRangeStart output range start
     * @param outRangeEnd   output range end
     * @param value         value to map
     * @return mapped value; values outside the input range are not clamped to output range
     */
    public static double map(double inRangeStart, double inRangeEnd, double outRangeStart, double outRangeEnd, double value) {
        return outRangeStart + (value - inRangeStart) * (outRangeEnd - outRangeStart) / (inRangeEnd - inRangeStart);
    }

    /**
     * Linearly interpolates between two angles in radians. Takes into account that angles wrap at {@code PI2} and
     * always takes the direction with the smallest delta angle.
     *
     * @param fromRadians start angle in radians
     * @param toRadians   target angle in radians
     * @param progress    interpolation value in the range [0, 1]
     * @return the interpolated angle in the range [0, PI2)
     */
    public static float lerpAngle(float fromRadians, float toRadians, float progress) {
        float delta = ((toRadians - fromRadians + PI2 + PI) % PI2) - PI;
        return (fromRadians + delta * progress + PI2) % PI2;
    }

    /**
     * Linearly interpolates between two angles in degrees. Takes into account that angles wrap at 360 degrees and
     * always takes the direction with the smallest delta angle.
     *
     * @param fromDegrees start angle in degrees
     * @param toDegrees   target angle in degrees
     * @param progress    interpolation value in the range [0, 1]
     * @return the interpolated angle in the range [0, 360)
     */
    public static float lerpAngleDeg(float fromDegrees, float toDegrees, float progress) {
        float delta = ((toDegrees - fromDegrees + 360f + 180f) % 360f) - 180f;
        return (fromDegrees + delta * progress + 360f) % 360f;
    }

    /**
     * Linearly interpolates between two angles in turns. Takes into account that angles wrap at 1.0 and always takes
     * the direction with the smallest delta angle. This version, unlike the versions for radians and degrees, avoids
     * any modulus operation (instead calling {@link #fastFloor(float)} twice).
     *
     * @param fromTurns start angle in turns
     * @param toTurns   target angle in turns
     * @param progress  interpolation value in the range [0, 1]
     * @return the interpolated angle in the range [0, 1)
     */
    public static float lerpAngleTurns(float fromTurns, float toTurns, float progress) {
        float d = toTurns - fromTurns + 0.5f;
        d = fromTurns + progress * (d - fastFloor(d) - 0.5f);
        return d - fastFloor(d);
    }

    /**
     * Linearly interpolates between two angles in radians. Takes into account that angles wrap at {@code PI2} and
     * always takes the direction with the smallest delta angle.
     *
     * @param fromRadians start angle in radians
     * @param toRadians   target angle in radians
     * @param progress    interpolation value in the range [0, 1]
     * @return the interpolated angle in the range [0, PI2)
     */
    public static double lerpAngle(double fromRadians, double toRadians, double progress) {
        double delta = ((toRadians - fromRadians + PI2_D + Math.PI) % PI2_D) - Math.PI;
        return (fromRadians + delta * progress + PI2_D) % PI2_D;
    }

    /**
     * Linearly interpolates between two angles in degrees. Takes into account that angles wrap at 360 degrees and
     * always takes the direction with the smallest delta angle.
     *
     * @param fromDegrees start angle in degrees
     * @param toDegrees   target angle in degrees
     * @param progress    interpolation value in the range [0, 1]
     * @return the interpolated angle in the range [0, 360)
     */
    public static double lerpAngleDeg(double fromDegrees, double toDegrees, double progress) {
        double delta = ((toDegrees - fromDegrees + 360.0 + 180.0) % 360.0) - 180.0;
        return (fromDegrees + delta * progress + 360.0) % 360.0;
    }

    /**
     * Linearly interpolates between two angles in turns. Takes into account that angles wrap at 1.0 and always takes
     * the direction with the smallest delta angle. This version, unlike the versions for radians and degrees, avoids
     * any modulus operation (instead calling {@link #floor(double)} twice).
     *
     * @param fromTurns start angle in turns
     * @param toTurns   target angle in turns
     * @param progress  interpolation value in the range [0, 1]
     * @return the interpolated angle in the range [0, 1)
     */
    public static double lerpAngleTurns(double fromTurns, double toTurns, double progress) {
        double d = toTurns - fromTurns + 0.5;
        d = fromTurns + progress * (d - floor(d) - 0.5);
        return d - floor(d);
    }

    /**
     * Returns true if the value is zero (using the default tolerance, {@link #FLOAT_ROUNDING_ERROR}, as upper bound).
     *
     * @param value any float
     */
    public static boolean isZero(float value) {
        return Math.abs(value) <= FLOAT_ROUNDING_ERROR;
    }

    /**
     * Returns true if the value is zero.
     *
     * @param value     any float
     * @param tolerance represent an upper bound below which the value is considered zero.
     */
    public static boolean isZero(float value, float tolerance) {
        return Math.abs(value) <= tolerance;
    }


    /**
     * Returns true if the value is zero. A suggested tolerance is {@code 0x1p-20}, which is the same value the float
     * overload uses for its default tolerance, or a smaller number to reduce false-positives, such as {@code 0x1p-32}.
     *
     * @param value     any double
     * @param tolerance represent an upper bound below which the value is considered zero.
     */
    public static boolean isZero(double value, double tolerance) {
        return Math.abs(value) <= tolerance;
    }

    /**
     * Takes any float and produces a float in the -1f to 1f range, with similar inputs producing
     * close to a consistent rate of up and down through the range. This is meant for noise, where it may be useful to
     * limit the amount of change between nearby points' noise values and prevent sudden "jumps" in noise value. An
     * input of any even number should produce something very close to -1f, any odd
     * number should produce something very close to 1f, and any number halfway between two incremental integers (like
     * 8.5f or -10.5f) should produce 0f or a very small fraction. This method is closely related to
     * {@link #sway(float)}, which will smoothly curve its output to produce more values that are close to -1 or 1.
     *
     * @param value any float
     * @return a float from -1f (inclusive) to 1f (inclusive)
     */
    public static float zigzag(float value) {
        int floor = (value >= 0f ? (int) value : (int) value - 1);
        value -= floor;
        floor = (-(floor & 1) | 1);
        return value * (floor << 1) - floor;
    }

    /**
     * Very similar to {@link TrigTools#sinTurns(float)} with half frequency, or {@link Math#sin(double)} with {@link Math#PI}
     * frequency, but optimized (and shaped) a little differently. This looks like a squished sine wave when graphed,
     * and is essentially just interpolating between each pair of odd and even inputs using what FastNoise calls
     * {@code QUINTIC} interpolation. This interpolation is slightly flatter at peaks and valleys than a sine wave is.
     * <br>
     * An input of any even number should produce something very close to -1f, any odd number should produce something
     * very close to 1f, and any number halfway between two incremental integers (like 8.5f or -10.5f) should produce 0f
     * or a very small fraction. In the (unlikely) event that this is given a float that is too large to represent
     * many or any non-integer values, this will simply return -1f or 1f.
     * <br>
     * This version of a sway method uses quintic interpolation; it uses up to the fifth power of value.
     *
     * @param value any float other than NaN or infinite values; extremely large values can't work properly
     * @return a float from -1f (inclusive) to 1f (inclusive)
     */
    public static float sway(float value) {
        int floor = (value >= 0f ? (int) value : (int) value - 1);
        value -= floor;
        floor = (-(floor & 1) | 1);
        return value * value * value * (value * (value * 6f - 15f) + 10f) * (floor << 1) - floor;
    }

    /**
     * Very similar to {@link TrigTools#sinTurns(float)} with half frequency, or {@link Math#sin(double)} with {@link Math#PI}
     * frequency, but optimized (and shaped) a little differently. This looks like a squished sine wave when graphed,
     * and is essentially just interpolating between each pair of odd and even inputs using what is sometimes called
     * {@code HERMITE} interpolation. This interpolation is rounder at peaks and valleys than a sine wave is; it is
     * also called {@code smoothstep} in GLSL, and is called cubic here because it gets the third power of a value.
     * <br>
     * An input of any even number should produce something very close to -1f, any odd number should produce something
     * very close to 1f, and any number halfway between two incremental integers (like 8.5f or -10.5f) should produce 0f
     * or a very small fraction. In the (unlikely) event that this is given a float that is too large to represent
     * many or any non-integer values, this will simply return -1f or 1f.
     *
     * @param value any float other than NaN or infinite values; extremely large values can't work properly
     * @return a float from -1f (inclusive) to 1f (inclusive)
     */
    public static float swayCubic(float value) {
        int floor = (value >= 0f ? (int) value : (int) value - 1);
        value -= floor;
        floor = (-(floor & 1) | 1);
        return value * value * (3f - value * 2f) * (floor << 1) - floor;
    }

    /**
     * Takes any float and produces a float in the 0f to 1f range, with a graph of input to output that
     * looks much like a sine wave, curving to have a flat slope when given an integer input and a steep slope when the
     * input is halfway between two integers, smoothly curving at any points between those extremes. This is meant for
     * noise, where it may be useful to limit the amount of change between nearby points' noise values and prevent both
     * sudden "jumps" in noise value and "cracks" where a line takes a sudden jagged movement at an angle.
     * <br>
     * An input of any even number should produce something very close to 0f, any odd number should produce something
     * very close to 1f, and any number halfway between two incremental integers (like 8.5f or -10.5f) should produce
     * 0.5f. In the (unlikely) event that this is given a float that is too large to represent many or any non-integer
     * values, this will simply return 0f or 1f. This version is called "Tight" because its range is tighter than
     * {@link #sway(float)}.
     * <br>
     * This version of a sway method uses quintic interpolation; it uses up to the fifth power of value.
     *
     * @param value any float other than NaN or infinite values; extremely large values can't work properly
     * @return a float from 0f (inclusive) to 1f (inclusive)
     */
    public static float swayTight(float value) {
        int floor = (value >= 0f ? (int) value : (int) value - 1);
        value -= floor;
        floor &= 1;
        return value * value * value * (value * (value * 6f - 15f) + 10f) * (-floor | 1) + floor;
    }

    /**
     * Takes any double and produces a double in the -1.0 to 1.0 range, with similar inputs producing
     * close to a consistent rate of up and down through the range. This is meant for noise, where it may be useful to
     * limit the amount of change between nearby points' noise values and prevent sudden "jumps" in noise value. An
     * input of any even number should produce something very close to -1.0, any odd
     * number should produce something very close to 1.0, and any number halfway between two incremental integers (like
     * 8.5 or -10.5) should produce 0.0 or a very small fraction. This method is closely related to
     * {@link #sway(double)}, which will smoothly curve its output to produce more values that are close to -1 or 1.
     *
     * @param value any double
     * @return a double from -1.0 (inclusive) to 1.0 (inclusive)
     */
    public static double zigzag(double value) {
        int floor = (value >= 0.0 ? (int) value : (int) value - 1);
        value -= floor;
        floor = (-(floor & 1) | 1);
        return value * (floor << 1) - floor;
    }

    /**
     * Very similar to {@link TrigTools#sinTurns(double)} with half frequency, or {@link Math#sin(double)} with {@link Math#PI}
     * frequency, but optimized (and shaped) a little differently. This looks like a squished sine wave when graphed,
     * and is essentially just interpolating between each pair of odd and even inputs using what FastNoise calls
     * {@code QUINTIC} interpolation. This interpolation is slightly flatter at peaks and valleys than a sine wave is.
     * <br>
     * An input of any even number should produce something very close to -1.0, any odd number should produce something
     * very close to 1.0, and any number halfway between two incremental integers (like 8.5 or -10.5) should produce 0.0
     * or a very small fraction. In the (unlikely) event that this is given a double that is too large to represent
     * many or any non-integer values, this will simply return -1.0 or 1.0.
     * <br>
     * This version of a sway method uses quintic interpolation; it uses up to the fifth power of value.
     *
     * @param value any double other than NaN or infinite values; extremely large values can't work properly
     * @return a double from -1.0 (inclusive) to 1.0 (inclusive)
     */
    public static double sway(double value) {
        int floor = (value >= 0.0 ? (int) value : (int) value - 1);
        value -= floor;
        floor = (-(floor & 1) | 1);
        return value * value * value * (value * (value * 6.0 - 15.0) + 10.0) * (floor << 1) - floor;
    }

    /**
     * Very similar to {@link TrigTools#sinTurns(double)} with half frequency, or {@link Math#sin(double)} with {@link Math#PI}
     * frequency, but optimized (and shaped) a little differently. This looks like a squished sine wave when graphed,
     * and is essentially just interpolating between each pair of odd and even inputs using what is sometimes called
     * {@code HERMITE} interpolation. This interpolation is rounder at peaks and valleys than a sine wave is; it is
     * also called {@code smoothstep} in GLSL, and is called cubic here because it gets the third power of a value.
     * <br>
     * An input of any even number should produce something very close to -1.0, any odd number should produce something
     * very close to 1.0, and any number halfway between two incremental integers (like 8.5 or -10.5) should produce 0.0
     * or a very small fraction. In the (unlikely) event that this is given a double that is too large to represent
     * many or any non-integer values, this will simply return -1.0 or 1.0.
     *
     * @param value any double other than NaN or infinite values; extremely large values can't work properly
     * @return a double from -1.0 (inclusive) to 1.0 (inclusive)
     */
    public static double swayCubic(double value) {
        int floor = (value >= 0.0 ? (int) value : (int) value - 1);
        value -= floor;
        floor = (-(floor & 1) | 1);
        return value * value * (3.0 - value * 2.0) * (floor << 1) - floor;
    }

    /**
     * Takes any double and produces a double in the 0.0 to 1.0 range, with a graph of input to output that
     * looks much like a sine wave, curving to have a flat slope when given an integer input and a steep slope when the
     * input is halfway between two integers, smoothly curving at any points between those extremes. This is meant for
     * noise, where it may be useful to limit the amount of change between nearby points' noise values and prevent both
     * sudden "jumps" in noise value and "cracks" where a line takes a sudden jagged movement at an angle.
     * <br>
     * An input of any even number should produce something very close to 0.0, any odd number should produce something
     * very close to 1.0, and any number halfway between two incremental integers (like 8.5 or -10.5) should produce
     * 0.5. In the (unlikely) event that this is given a double that is too large to represent many or any non-integer
     * values, this will simply return 0.0 or 1.0. This version is called "Tight" because its range is tighter than
     * {@link #sway(double)}.
     * <br>
     * This version of a sway method uses quintic interpolation; it uses up to the fifth power of value.
     *
     * @param value any double other than NaN or infinite values; extremely large values can't work properly
     * @return a double from 0.0 (inclusive) to 1.0 (inclusive)
     */
    public static double swayTight(double value) {
        int floor = (value >= 0.0 ? (int) value : (int) value - 1);
        value -= floor;
        floor &= 1;
        return value * value * value * (value * (value * 6.0 - 15.0) + 10.0) * (-floor | 1) + floor;
    }

    /**
     * 1275 negative, odd {@code long} values that are calculated using a generalization of the golden ratio and
     * exponents of those generalizations. Mostly, these are useful because they are all 64-bit constants that have an
     * irrational-number-like pattern to their bits, which makes them pretty much all useful as increments for large
     * counters (also called Weyl sequences) and also sometimes as multipliers for data that should be somewhat random.
     * The earlier numbers in the array are closer to the bit patterns of irrational numbers. Note that these are not at
     * all uniformly-distributed, and should not be used for tasks where random negative longs must be uniform.
     * See <a href="http://extremelearning.com.au/unreasonable-effectiveness-of-quasirandom-sequences/#GeneralizingGoldenRatio">Martin Roberts' blog post</a>
     * for more information on how these were constructed. This can be organized into groups of increasing size -- 1
     * number from the 1D sequence in that post, 2 numbers from the 2D sequence, 3 numbers from the 3D sequence, etc.
     */
    public static final long[] GOLDEN_LONGS = {
            0x9E3779B97F4A7C15L,
            0xC13FA9A902A6328FL, 0x91E10DA5C79E7B1DL,
            0xD1B54A32D192ED03L, 0xABC98388FB8FAC03L, 0x8CB92BA72F3D8DD7L,
            0xDB4F0B9175AE2165L, 0xBBE0563303A4615FL, 0xA0F2EC75A1FE1575L, 0x89E182857D9ED689L,
            0xE19B01AA9D42C633L, 0xC6D1D6C8ED0C9631L, 0xAF36D01EF7518DBBL, 0x9A69443F36F710E7L, 0x881403B9339BD42DL,
            0xE60E2B722B53AEEBL, 0xCEBD76D9EDB6A8EFL, 0xB9C9AA3A51D00B65L, 0xA6F5777F6F88983FL, 0x9609C71EB7D03F7BL,
            0x86D516E50B04AB1BL,
            0xE95E1DD17D35800DL, 0xD4BC74E13F3C782FL, 0xC1EDBC5B5C68AC25L, 0xB0C8AC50F0EDEF5DL,
            0xA127A31C56D1CDB5L, 0x92E852C80D153DB3L, 0x85EB75C3024385C3L,
            0xEBEDEED9D803C815L, 0xD96EB1A810CAAF5FL, 0xC862B36DAF790DD5L, 0xB8ACD90C142FE10BL,
            0xAA324F90DED86B69L, 0x9CDA5E693FEA10AFL, 0x908E3D2C82567A73L, 0x8538ECB5BD456EA3L,
            0xEDF84ED4185625ADL, 0xDD35B2CD88449739L, 0xCDA146AB203DAD89L, 0xBF25C1FA63435003L,
            0xB1AF5C04970044E1L, 0xA52BB0C808635D87L, 0x9989A7D8994B239DL, 0x8EB95D05457AA16BL, 0x84AC0AA2B7D7E1C5L,
            0xEFA239AADFF080FFL, 0xE0504E7A17640707L, 0xD1F91E9D401F2CD3L, 0xC48CA286CD4C4B4DL, 0xB7FBD901347B3E45L,
            0xAC38B6695442DF75L, 0xA13614FB593F897DL, 0x96E7A62094FC418FL, 0x8D41E4ADD988EA91L, 0x843A0802F95827ADL,
            0xF104272092F57C5DL, 0xE2E8D1BC93B1BCBFL, 0xD5A0DBC4254CF233L, 0xC91FE60DC666F779L, 0xBD5A4AD0037BF1F9L,
            0xB24512C7D7E9D953L, 0xA7D5EB01A237A78BL, 0x9E031B3B27A07ECDL, 0x94C37CD5B1796B93L, 0x8C0E724FD5446177L,
            0x83DBDF3EF6A3B35BL,
            0xF22EECF611D9436FL, 0xE51CC09B3D83CAFFL, 0xD8BF2D5017352FF9L, 0xCD0C73D06EEC786BL,
            0xC1FB5B846B56C221L, 0xB7832B3BCCB1A38BL, 0xAD9BA24D9CF0D513L, 0xA43CF216E1960C65L, 0x9B5FB7D32E8D899FL,
            0x92FCF6CA403AB5A5L, 0x8B0E12CE05E62F5BL, 0x838CCB04C5269A59L,
            0xF32E81F362A69FDDL, 0xE701532702B6F82DL,
            0xDB70396E42C39DCBL, 0xD073641228368B05L, 0xC60366898AB7FC8FL, 0xBC193374F397645DL, 0xB2AE17DAC99253B9L,
            0xA9BBB6A090C6F39DL, 0xA13C043E2FE475FBL, 0x992942A852DFBF6FL, 0x917DFD6F28AFDA5DL, 0x8A35060EDCF5BD75L,
            0x8349706F500D7573L,
            0xF40BA295557EB087L, 0xE8A62E740B1030B7L, 0xDDC8F72B7958B04FL, 0xD36DA0129AF999DDL,
            0xC98E188E5138882DL, 0xC02498843215A687L, 0xB72B9CF7CB66736FL, 0xAE9DE4D05F66AF41L, 0xA6766DC536E4D933L,
            0x9EB0716EBBC7DEE5L, 0x9747627AA435C841L, 0x9036EA018B2BCF9FL, 0x897AE4FC66EB83E9L, 0x830F61D86049BD33L,
            0xF4CCD627D640C91BL, 0xEA171C21F30F9703L, 0xDFD9550E85ACC5E5L, 0xD60E418421899E89L, 0xCCB0DCDF5F586A6BL,
            0xC3BC5AB09BC44545L, 0xBB2C24468285A235L, 0xB2FBD654234A1D55L, 0xAB273EB15C029011L, 0xA3AA5A3471A8B1F5L,
            0x9C8152A3BD74C39DL, 0x95A87CBE60D27129L, 0x8F1C565AFE309975L, 0x88D9849B80100109L, 0x82DCD235027EF537L,
            0xF57716BC263D1509L, 0xEB5D28EA462157E9L, 0xE1ADA55D371AAD51L, 0xD8642B04DD8237EDL, 0xCF7C86F34CD519B3L,
            0xC6F2B276C5AAA9AFL, 0xBEC2D147B3AAF99BL, 0xB6E92FC9D8D36E9DL, 0xAF62415FDC02D5B3L, 0xA82A9ED07916A9A9L,
            0xA13F04BC98DBF8F5L, 0x9A9C52259EBB171FL, 0x943F870341597D83L, 0x8E25C2E84A744115L, 0x884C43B5A0F27CEDL,
            0x82B0645B06A50EEBL,
            0xF60E40931CDDA7E5L, 0xEC7F64E5DBDB09E5L, 0xE34F959426E19C8FL, 0xDA7B216D11305FB7L,
            0xD1FE7BF6F82D85CBL, 0xC9D63C0265C9E5E3L, 0xC1FF1A4B21B8D3A7L, 0xBA75F026E4723465L, 0xB337B641246F3469L,
            0xAC4183637B560F87L, 0xA5908B4A25D9127DL, 0x9F221D8425EA5847L, 0x98F3A45E9392C40DL, 0x9302A3DAAD39363DL,
            0x8D4CB8AE3C78C8BFL, 0x87CF974DE8CBB6C7L, 0x82890B01154E248BL,
            0xF6955E0400B91807L, 0xED8367D64493823BL,
            0xE4C6DA7D4C75AFC7L, 0xDC5C91B62A616C9DL, 0xD44186D34B52C711L, 0xCC72CFA5E47D8AB3L, 0xC4ED9D719FBC9AFFL,
            0xBDAF3BEA26B5C657L, 0xB6B5103A2FC143B5L, 0xAFFC9813B30F3AFBL, 0xA98368C8F1D0B4A7L, 0xA3472E6DFC5547E1L,
            0x9D45AB02671CFAD7L, 0x977CB5A2E1CF20EBL, 0x91EA39C265DCA3FBL, 0x8C8C366AB54F1765L, 0x8760BD83E4E72E47L,
            0x8265F322AF35A6BFL,
            0xF70EDC6AB8A796DBL, 0xEE6DAE32C9C7314DL, 0xE619AA5C2E8EEB25L, 0xDE101EE43CF747A9L,
            0xD64E71E254B3D023L, 0xCED220B05B087579L, 0xC798BF1ABDC1A781L, 0xC09FF697B9FEC8F7L, 0xB9E58585A5D89199L,
            0xB3673E6FFE31D46DL, 0xAD23075AFC31BA73L, 0xA716D9157803872DL, 0xA140BE90E0821C0BL, 0x9B9ED43F116D5253L,
            0x962F4775D3AD8C5DL, 0x90F055D7D4FF2B53L, 0x8BE04CC2E6247F0FL, 0x86FD88C35071DBFDL, 0x8246750C152D54B9L,
            0xF77CB1D641353E21L, 0xEF41DBE834EDE2F5L, 0xE74D154856960573L, 0xDF9C098CFBA62BC3L, 0xD82C7821AF16CE8DL,
            0xD0FC339E5B8BC6BBL, 0xCA09212412C75829L, 0xC35137BF42996A17L, 0xBCD27FCF29165CF9L, 0xB68B12725B6A44F9L,
            0xB07918F83419939BL, 0xAA9ACC56FEEFD88DL, 0xA4EE74A6BA40A66FL, 0x9F7268A046763B11L, 0x9A250D20DE35B0E7L,
            0x9504D4B1B2A19A37L, 0x90103F13887A534BL, 0x8B45D8CE340773A7L, 0x86A43AC3D2D7E5D9L, 0x822A09C7A37EA76BL,
            0xF7E0785B8323F5F9L, 0xF002ED136FCACF3FL, 0xE86546245CFD180FL, 0xE1057C8D01770237L, 0xD9E199C40A65E4C5L,
            0xD2F7B73254740353L, 0xCC45FDB16385F085L, 0xC5CAA50DF6AE27A1L, 0xBF83F38E96F3936DL, 0xB9703D7E0096EF81L,
            0xB38DE4B947898D35L, 0xADDB584198C64BF5L, 0xA85713D17B33C9EDL, 0xA2FF9F7573A53727L, 0x9DD38F27F07609E7L,
            0x98D18270621C5493L, 0x93F8240566E5E78DL, 0x8F462971F0E5F545L, 0x8ABA52BD4DE3D4BFL, 0x86536A15F9DFF487L,
            0x8210437F25824997L,
            0xF83B8239BBCD51D9L, 0xF0B35A2524B90EF9L, 0xE965B31B1E6CBB7FL, 0xE250C6ACD48A7A13L,
            0xDB72DC3546A1A6C1L, 0xD4CA486E2E16DE85L, 0xCE556D0823F747B3L, 0xC812B845ED79238BL, 0xC200A49AD6AFC313L,
            0xBC1DB84C03B717C3L, 0xB6688514A1537297L, 0xB0DFA7CCDEB4C5FBL, 0xAB81C8139ABA1463L, 0xA64D97FAAFB9C87BL,
            0xA141D3B5C976AD69L, 0x9C5D414BB1874509L, 0x979EB049FF0E766DL, 0x9304F97B17392461L, 0x8E8EFE9E6C844FD5L,
            0x8A3BAA22EB5B1E49L, 0x8609EEE3832393E3L, 0x81F8C7E5BB542C07L,
            0xF88EE8EC947BAC23L, 0xF15533119A11FE77L,
            0xEA51424F9DD1C415L, 0xE381868217F635A9L, 0xDCE47B2644A27E2BL, 0xD678A70494127F57L, 0xD03C9BDC9EF788C5L,
            0xCA2EF6138C473C7FL, 0xC44E5C64D64E360BL, 0xBE997F955D5F55F7L, 0xB90F1A28B6FBF225L, 0xB3ADF018A8D0B781L,
            0xAE74CE8EBF5EE175L, 0xA9628B9FF0A1A8E9L, 0xA476060A3B748BC9L, 0x9FAE24F434F06829L, 0x9B09D7AE756374C3L,
            0x96881576D6F2FC29L, 0x9227DD3D785D806DL, 0x8DE8356B76B8A95BL, 0x89C82BAB51682E05L, 0x85C6D4B2EBE9E125L,
            0x81E34C0F216F3C09L,
            0xF8DB9899F68818F9L, 0xF1EA3408C00AAE97L, 0xEB2A65F78A5C978BL, 0xE49ACC3BA426BE8FL,
            0xDE3A0E8BE3F8B7A7L, 0xD806DE3A15DCA725L, 0xD1FFF5EE60F535A5L, 0xCC24196497159D91L, 0xC672152B60A84E73L,
            0xC0E8BE65379A4499L, 0xBB86F28B24601C5BL, 0xB64B973130862B7FL, 0xB13599CC82969B59L, 0xAC43EF7B1777ADA7L,
            0xA77594CD0DB91721L, 0xA2C98D8F7799BE3BL, 0x9E3EE498A8DF438DL, 0x99D4AB95F5E69475L, 0x9589FADAD9A07791L,
            0x915DF13178769511L, 0x8D4FB3AC765D01CDL, 0x895E6D7A1699DA71L, 0x85894FB89C101161L, 0x81CF914BE11C4529L,
            0xF92258D8434F8A31L, 0xF273D5706B3800EDL, 0xEBF332215412178BL, 0xE59F33F1FFAA79DBL, 0xDF76A85BFC5FCBEBL,
            0xD9786511656FDABFL, 0xD3A347C4717A9A2BL, 0xCDF635F0848CE469L, 0xC8701CA4BB4E43A7L, 0xC30FF04FE53469DFL,
            0xBDD4AC8DE3E46D41L, 0xB8BD53F6662D74EDL, 0xB3C8EFECF54A46EBL, 0xAEF690724B5729F1L, 0xAA454BF6EA27D80DL,
            0xA5B43F2EE9E5DD71L, 0xA1428CE6F71ABD83L, 0x9CEF5DDA7802A12BL, 0x98B9E08AD13C30D7L, 0x94A14917C2209C1FL,
            0x90A4D118D143BB19L, 0x8CC3B777C1CFB485L, 0x88FD404C0AA1B13DL, 0x8550B4B7483DEA9DL, 0x81BD62C2A2E0D811L,
            0xF963D37E6AD397B1L, 0xF2F358592E4FDF43L, 0xECAD6DC073812739L, 0xE690FA59735A5CA5L, 0xE09CEC0D2BB40BEFL,
            0xDAD037D85A1FAFE9L, 0xD529D99CB4258D85L, 0xCFA8D3F354BAF8AFL, 0xCA4C300056F50BBFL, 0xC512FD479630A193L,
            0xBFFC51828C1BCC49L, 0xBB074877453F18A7L, 0xB63303D064D5B8FDL, 0xB17EAAF630F33AD9L, 0xACE96AE8A023B92FL,
            0xA872761A61E09887L, 0xA419044CDB5FCCEDL, 0x9FDC526D126E78A5L, 0x9BBBA27180316765L, 0x97B63B38C5CE8A63L,
            0x93CB68693D2A2E11L, 0x8FFA7A51600930CBL, 0x8C42C5C9000F11A1L, 0x88A3A4134A3345FDL, 0x851C72C1906CEA55L,
            0x81AC9396D4769C31L,
            0xF9A099FB652F7569L, 0xF369D04BE1B34389L, 0xED5AA0254466E043L, 0xE7720D2C9634DC87L,
            0xE1AF214F0C24C34FL, 0xDC10EC99FF092E81L, 0xD6968513E24AD1C3L, 0xD13F06963376BC15L, 0xCC0992A85C5E894FL,
            0xC6F5505B81C0B31DL, 0xC2016C273895ABE9L, 0xBD2D17C71C43F3EFL, 0xB8778A194021EF43L, 0xB3DFFEFD76CFF10DL,
            0xAF65B7356A17BA2BL, 0xAB07F8457E209615L, 0xA6C60C567AE854B7L, 0xA29F4217F711A191L, 0x9E92ECA37F37AC39L,
            0x9AA063607515C1B5L, 0x96C701E8A1EF5E21L, 0x930627ED77C26777L, 0x8F5D391DFCE9B1DBL, 0x8BCB9D0D5DF1A803L,
            0x8850BF1A217BFC29L, 0x84EC0E55FA29A321L, 0x819CFD6E329C1075L,
            0xF9D929986B5EEEB9L, 0xF3D82B220C06E07BL,
            0xEDFC1BCF7B16B91BL, 0xE844186B738DCE55L, 0xE2AF4336683C2DFFL, 0xDD3CC3C4ED67D397L, 0xD7EBC6DEF1107135L,
            0xD2BB7E5FBCDABC13L, 0xCDAB2116BCCAB79DL, 0xC8B9EAA90612499FL, 0xC3E71B73995679F7L, 0xBF31F86E5BEA2525L,
            0xBA99CB0FC3989F53L, 0xB61DE13130B5D161L, 0xB1BD8CF3F243BEB5L, 0xAD7824A6F01725D3L, 0xA94D02ACF6FE122DL,
            0xA53B8563A303BF93L, 0xA1430F0AE4052279L, 0x9D6305AD18E0C827L, 0x999AD307BDA3917BL, 0x95E9E474A92A0E69L,
            0x924FAAD3D6C3FB33L, 0x8ECB9A75B87C94B9L, 0x8B5D2B060EBF2C2DL, 0x8803D77742239521L, 0x84BF1DEE3C41BFEBL,
            0x818E7FAEBC7F0FFBL,
            0xFA0DEED8E62B756BL, 0xF43F3741C26C457DL, 0xEE93070DBC849FE3L, 0xE90890F198E9682FL,
            0xE39F0C66B318DD27L, 0xDE55B58EA47EDAA5L, 0xD92BCD1793E4C8BDL, 0xD420982129831E45L, 0xCF33602223E1A33DL,
            0xCA6372CE89CA5B13L, 0xC5B021FE75A93343L, 0xC118C39576C84389L, 0xBC9CB16A84ED9C1DL, 0xB83B493082F45033L,
            0xB3F3EC5F4D0EA19FL, 0xAFC6001D4F72F081L, 0xABB0ED29A2485B47L, 0xA7B41FC6A7BAD15BL, 0xA3CF07A52930C135L,
            0xA00117CFF0AE8395L, 0x9C49C697DB853389L, 0x98A88D80637BC8CFL, 0x951CE92C9BB209A9L, 0x91A6594C9E8D459BL,
            0x8E44608B6A0EC52DL, 0x8AF6847D28037703L, 0x87BC4D8DDF8BA161L, 0x849546F08D8843C7L, 0x8180FE8EA18A6047L,
            0xFA3F486F7B176F11L, 0xF49FA91E77C7403FL, 0xEF2063A9CDE20F67L, 0xE9C0BDF597E5D9BFL, 0xE4800214962B496BL,
            0xDF5D7E301FAC5725L, 0xDA5884709D33B639L, 0xD5706AE68BD8CD09L, 0xD0A48B7403BDE0F3L, 0xCBF443B6C0179A11L,
            0xC75EF4F2A5961C7DL, 0xC2E403FCC458AB43L, 0xBE82D926D2A51FD9L, 0xBA3AE02B1DAC798FL, 0xB60B8818EDB564FDL,
            0xB1F443415B14DE55L, 0xADF48724916BFCADL, 0xAA0BCC5F7EB08975L, 0xA6398E99EB9448BFL, 0xA27D4C74FAECC655L,
            0x9ED6877A0DCB1ABDL, 0x9B44C40A0A006C2DL, 0x97C7894D00D8F3A3L, 0x945E612233E407B1L, 0x9108D81075AC1E93L,
            0x8DC67D36E44DE1F9L, 0x8A96E23DFBE9515DL, 0x87799B48FEF38DFBL, 0x846E3EE7B27B4743L, 0x817466086C7CD82DL,
            0xFA6D85BD7B83D8BFL, 0xF4FA18113E772031L, 0xEFA509F857AEFD4BL, 0xEA6DB233E30452E9L, 0xE5536B340D76D84BL,
            0xE05593038E3D2B9BL, 0xDB738B3392365683L, 0xD6ACB8C8173F6B75L, 0xD2008424B4FDC1ABL, 0xCD6E58F9D0BBE7DDL,
            0xC8F5A6323A04AC75L, 0xC495DDE12DB49653L, 0xC04E7530BD46DD6DL, 0xBC1EE450982F5FFFL, 0xB806A665351F3943L,
            0xB4053977591D82B1L, 0xB01A1E63FA696B4DL, 0xAC44D8CC7D2631A7L, 0xA884EF0747DCAEFDL, 0xA4D9EA10ADE9061BL,
            0xA143557C2DF5AF63L, 0x9DC0BF6602AF8781L, 0x9A51B86503E8B7EFL, 0x96F5D37CD66A4963L, 0x93ACA61068BDF1B3L,
            0x9075C7D4BB43399BL, 0x8D50D2C3F1EC6CD1L, 0x8A3D6310AE08E88DL, 0x873B1719AE8B4BADL, 0x84498F5DB542C8AFL,
            0x81686E6FAF87664DL,
            0xFA98EFEF3C53DD4BL, 0xF54F0FFD0ECC0203L, 0xF021C278AD401597L, 0xEB106D053D813FA3L,
            0xE61A7887DAAF4445L, 0xE13F5115FBAFF277L, 0xDC7E65E438BC1DFDL, 0xD7D729356E007179L, 0xD349104A395B3CA7L,
            0xCED39350D14AFF63L, 0xCA762D55332BD5AFL, 0xC6305C31A6EC13F9L, 0xC201A07F966A5779L, 0xBDE97D88B6B90CF3L,
            0xB9E77938818CFA07L, 0xB5FB1C0DFD25ABBBL, 0xB223F10DD107D0C5L, 0xAE6185B4A5EA78B3L, 0xAAB369E9CF40F355L,
            0xA7192FF23CD39E85L, 0xA3926C63B2E2553DL, 0xA01EB618475369FBL, 0x9CBDA622227A2153L, 0x996ED7BF82078107L,
            0x9631E84EFCBFFCC7L, 0x9306774405981503L, 0x8FEC261BACE15EAFL, 0x8CE298519E38A56BL, 0x89E9735559ECED09L,
            0x87005E7FA89E04BBL, 0x8427030847D82601L, 0x815D0BFBCE78BCDBL,
            0xFAC1E364FEA8C287L, 0xF59F42F9E9A5D9DDL,
            0xF0978EA6AB84D035L, 0xEBAA39469D2D7F79L, 0xE6D6B8990D712A5BL, 0xE21C853219B4AFDDL, 0xDD7B1A6BD60CA405L,
            0xD8F1F657C32ACDEDL, 0xD48099B0908512C5L, 0xD02687CC29263023L, 0xCBE3468E09A0D73BL, 0xC7B65E59DDA5BE8DL,
            0xC39F5A0663C519BBL, 0xBF9DC6D095EB983DL, 0xBBB1344F1532941BL, 0xB7D93465D7A27B45L, 0xB4155B3A168DB819L,
            0xB0653F267C3172B1L, 0xACC878AF8F507395L, 0xA93EA2785B833BC7L, 0xA5C7593754FF0D63L, 0xA2623BAB769C24A7L,
            0x9F0EEA9198E9C3B9L, 0x9BCD089A0124F43BL, 0x989C3A5E26ED0205L, 0x957C2656AF96B259L, 0x926C74D19E051B71L,
            0x8F6CCFE8B5F4C1FFL, 0x8C7CE37811AB45E7L, 0x899C5D14E9036D5BL, 0x86CAEC0488D2C6B3L, 0x8408413379AB65D1L,
            0x81540F2CD5017313L,
            0xFAE842F1FD2D2CA7L, 0xF5EA757A0A98C863L, 0xF10613828C28F7FBL, 0xEC3A9B96914C96B9L,
            0xE7878ED473415E13L, 0xE2EC70E0B7805833L, 0xDE68C7D934F58C01L, 0xD9FC1C487AAEB91BL, 0xD5A5F91976B3BE43L,
            0xD165EB8B5BC1E6E5L, 0xCD3B8325C4A9DC47L, 0xC92651AD1416562FL, 0xC525EB170F87E511L, 0xC139E57FB4584EF5L,
            0xBD61D91E459DF78BL, 0xB99D603A91CDAD27L, 0xB5EC17226EFEF75BL, 0xB24D9C1F6CBCA9C1L, 0xAEC18F6CBA510669L,
            0xAB47932D40822855L, 0xA7DF4B61EDB8BF7BL, 0xA4885DE033905DEFL, 0xA1427248B4D5B15FL, 0x9E0D31FE22FC05B1L,
            0x9AE8481C4A185585L, 0x97D3616F4A74FC6BL, 0x94CE2C6AFED5D56BL, 0x91D859228E892EB9L, 0x8EF199402A6682CBL,
            0x8C199FFCF3E056D3L, 0x895022190D52F827L, 0x8694D5D3D2BE1549L, 0x83E772E43A1B5CC1L, 0x8147B2715A886473L,
            0xFB0C904DEDC71EABL, 0xF631A396A8EA8405L, 0xF16EC07F404C404BL, 0xECC3700594D89347L, 0xE82F3D74BAE83EF3L,
            0xE3B1B6599529F247L, 0xDF4A6A77A7F5FB99L, 0xDAF8EBBE23F4F3D7L, 0xD6BCCE3D27077AF3L, 0xD295A81B326269ABL,
            0xCE83118AD4D80EEDL, 0xCA84A4C0884C271DL, 0xC699FDE8C1553A63L, 0xC2C2BB1E3012F58FL, 0xBEFE7C603145DEB5L,
            0xBB4CE3896EB97D21L, 0xB7AD9446AE16AAD9L, 0xB420340DCD384DF7L, 0xB0A46A14EB212977L, 0xAD39DF49BCB5D0F1L,
            0xA9E03E490C621029L, 0xA697335663D548B5L, 0xA35E6C53DF056371L, 0xA03598BA27ABFBAFL, 0x9D1C699098755D61L,
            0x9A129165871CCFF3L, 0x9717C446B4B57445L, 0x942BB7B9E362B6B3L, 0x914E22B590C6FD67L, 0x8E7EBD99D472D11BL,
            0x8BBD422961A2424BL, 0x89096B82AB99BF13L, 0x8662F6192BF6EFA7L, 0x83C99FAECA4D7FB3L, 0x813D274D646AFB8FL,
            0xFB2E70F0672DEAC3L, 0xF67419E4328C5AE5L, 0xF1D08AF9A011B93BL, 0xED43566A0A91EB2DL, 0xE8CC107FC3FB0607L,
            0xE46A4F8C20778F89L, 0xE01DABDDB18AAEF3L, 0xDBE5BFB6B03F20DBL, 0xD7C2274395861A1BL, 0xD3B28091DFE78B43L,
            0xCFB66B8705A9661BL, 0xCBCD89D79297A36BL, 0xC7F77EFE709ACF33L, 0xC433F0345A4ED6BFL, 0xC082846776CFB603L,
            0xBCE2E4331DF57083L, 0xB954B9D7C43C7F5DL, 0xB5D7B1330D9B8881L, 0xB26B77B8068ACC91L, 0xAF0FBC6782854095L,
            0xABC42FC89F4EBF83L, 0xA88883E16C4E25EFL, 0xA55C6C2FB54D7F91L, 0xA23F9DA1EFF5B477L, 0x9F31CE904B5E5947L,
            0x9C32B6B5E10D6C97L, 0x99420F2A06C5E287L, 0x965F9259C086E847L, 0x938AFC015220C2F1L, 0x90C40925EFC71851L,
            0x8E0A780F8D0B4DFBL, 0x8B5E0842C9AC7A2DL, 0x88BE7A7AFBAD244BL, 0x862B90A45621C743L, 0x83A50DD62C2DB015L,
            0x812AB64D4FA66D45L,
            0xFB50D0447B78F80DL, 0xF6B791E91E5BBCFBL, 0xF233DE244FEC61C3L, 0xEDC5500DF4CB9F07L,
            0xE96B84969F82E781L, 0xE5261A7EEA55C0C7L, 0xE0F4B24EF9970D8BL, 0xDCD6EE4E25C47F99L, 0xD8CC727ACCADE12FL,
            0xD4D4E48248F14DF5L, 0xD0EFEBB90F18CC85L, 0xCD1D3112EFA902DFL, 0xC95C5F1B7D73F963L, 0xC5AD21EE97860B19L,
            0xC20F273116063D41L, 0xBE821E09996645E9L, 0xBB05B7197B4187B7L, 0xB799A475E04D3A53L, 0xB43D99A0EABED9CFL,
            0xB0F14B830C90CD01L, 0xADB47064790FFB1BL, 0xAA86BFE6B51FC425L, 0xA767F2FE45A47E49L, 0xA457C3EC7B873C09L,
            0xA155EE395CC834D1L, 0x9E622EADAA17B30BL, 0x9B7C434D006FE831L, 0x98A3EB50162C79AFL, 0x95D8E71F131EFF4BL,
            0x931AF84C032207B3L, 0x9069E18D62AE8765L, 0x8DC566B8C4F9DBF5L, 0x8B2D4CBD9324C81FL, 0x88A1599FE405FD95L,
            0x862154736C1CEF53L, 0x83AD0556853BC35BL, 0x8144356D4D794CB5L,
            0xFB6D5599BDF133D1L, 0xF6EF948D7C022F77L,
            0xF2865D3AD18E3E67L, 0xEE3151B6A0B5A4E3L, 0xE9F015C346AAD57BL, 0xE5C24EC8EFB818A7L, 0xE1A7A3CE0E5A4DD5L,
            0xDD9FBD6FF4D0581BL, 0xD9AA45DB9081A661L, 0xD5C6E8C646A11777L, 0xD1F55366F1743DEDL, 0xCE35346EFDA9BEEDL,
            0xCA863C03A72C3191L, 0xC6E81BB754E182C5L, 0xC35A868312C97437L, 0xBFDD30C029F0535DL, 0xBC6FD021D5AD80E3L,
            0xB9121BAF15A7D079L, 0xB5C3CBBC9C1C2DA1L, 0xB28499E6D7E54BD3L, 0xAF54410C19C57735L, 0xAC327D46D475DF91L,
            0xA91F0BE7F700F123L, 0xA619AB7160EF7EB9L, 0xA3221B906FD2A687L, 0xA0381D18A5B676E5L, 0x9D5B71FE680B6AC1L,
            0x9A8BDD51D696DE37L, 0x97C92339B9FC9B9FL, 0x951308EE89759779L, 0x926954B58749DF79L, 0x8FCBCDDBF3A5A54DL,
            0x8D3A3CB2556329E7L, 0x8AB46A87D8652177L, 0x883A21A5C11EF379L, 0x85CB2D4AF4E9FD39L, 0x836759A796C8B7D1L,
            0x810E73D8B83A55FDL,
            0xFB8F8AF296473783L, 0xF732CAF4391DFA4FL, 0xF2E9688872F4556DL, 0xEEB30DB72E847BD9L,
            0xEA8F6605FAB5E921L, 0xE67E1E716C665867L, 0xE27EE5669D93D64BL, 0xDE916ABCC965815BL, 0xDAB55FAF0492CFAFL,
            0xD6EA76D611AB6957L, 0xD330642250C3D62DL, 0xCF86DCD5CA0D633FL, 0xCBED977E52E1BF99L, 0xC8644BEFCCCCE2EFL,
            0xC4EAB33E7E21D9CBL, 0xC18087B983A91355L, 0xBE2584E55AF8C3DFL, 0xBAD967768509DE5DL, 0xB79BED4C409E0CB9L,
            0xB46CD56B5C0CECC5L, 0xB14BDFF91E10ACD5L, 0xAE38CE36452BF031L, 0xAB33627A1D44A8FFL, 0xA83B602DAB115255L,
            0xA5508BC6ECF7AB17L, 0xA272AAC430FDBFF7L, 0x9FA183A77F6FB9E9L, 0x9CDCDDF219DE8547L, 0x9A2482200E1CFE17L,
            0x977839A3DCE2DF07L, 0x94D7CEE233BE3A55L, 0x92430D2DB9FDC787L, 0x8FB9C0C2F03FCCE9L, 0x8D3BB6C42252E1F5L,
            0x8AC8BD356B173707L, 0x8860A2F8CA1077C7L, 0x860337CA4A59C09DL, 0x83B04C3C3AAE7BEFL, 0x8167B1B3763C52A9L,
            0xFBAD8DEED8FB2209L, 0xF76DC9FB80FEB207L, 0xF340636968788E7FL, 0xEF250AD8F2743C11L, 0xEB1B7241906F3CF1L,
            0xE7234CEBF7A3CD01L, 0xE33C4F6C6F5BF5B5L, 0xDF662F9D37E0C88DL, 0xDBA0A499099B628BL, 0xD7EB66B5ABFF26C7L,
            0xD4462F7EA3D6623FL, 0xD0B0B9AFF88C4749L, 0xCD2AC1311010E3E1L, 0xC9B4030FA0F56381L, 0xC64C3D7ABA609379L,
            0xC2F32FBDE17D3F97L, 0xBFA89A3C4405955BL, 0xBC6C3E6BFF904CA1L, 0xB93DDED17D45DFA9L, 0xB61D3EFAE1A59F8FL,
            0xB30A237B9003EFEBL, 0xB00451E7C16C6AC5L, 0xAD0B90D02E941EB1L, 0xAA1FA7BDCC8982EBL, 0xA7405F2D9BD12195L,
            0xA46D808C899F5623L, 0xA1A6D63362E0D645L, 0x9EEC2B62D8C50E0FL, 0x9C3D4C3F967EA489L, 0x999A05CE67EFC51DL,
            0x970225F070F90BC5L, 0x94757B5F75232DCBL, 0x91F3D5AA2F5CAFF5L, 0x8F7D0530B9862C37L, 0x8D10DB210388D58BL,
            0x8AAF297359B40FA7L, 0x8857C2E6FA2012D3L, 0x860A7AFEB8D4B141L, 0x83C725FDB2746C03L, 0x818D98E40D2D198FL,
            0xFBBBA0DAAEEC680DL, 0xF78976F1379136DFL, 0xF3693491C2390A33L, 0xEF5A8D55FEAAF3D3L, 0xEB5D361D9D81532BL,
            0xE770E508E115260BL, 0xE3955173459932CDL, 0xDFCA33EE40021AE5L, 0xDC0F463C135A0B75L, 0xD864434ABC206085L,
            0xD4C8E72EF15727A1L, 0xD13CEF1F3AE20029L, 0xCDC0196F1CDB6263L, 0xCA52258A5786DA61L, 0xC6F2D3F03B88419BL,
            0xC3A1E62F12097969L, 0xC05F1EDF98799989L, 0xBD2A41A08F91F0EDL, 0xBA0313125D4E9BBBL, 0xB6E958D2C189D077L,
            0xB3DCD9789CEA5F55L, 0xB0DD5C8FC9D731F7L, 0xADEAAA950722E7A5L, 0xAB048CF1F423F1F3L, 0xA82ACDF91DEED89DL,
            0xA55D38E21D698881L, 0xA29B99C5C5EFC9ADL, 0x9FE5BD9A64422B13L, 0x9D3B72300D79E05BL, 0x9A9C862CFDBD36ABL,
            0x9808C90A0671690BL, 0x95800B0F0BA7BBF1L, 0x93021D4F9084E135L, 0x908ED1A75262BB93L, 0x8E25FAB6F26DA8EDL,
            0x8BC76BE0AD7F873DL, 0x8972F94521FBAF7DL, 0x872877C02370254FL, 0x84E7BCE59BC138E5L, 0x82B09EFE79A5D3D9L,
            0x8082F505AC3B9F93L,
            0xFBE7BD0A608BAD17L, 0xF7E03E790DA47477L, 0xF3E93FA3A9F8B32BL, 0xF0027CFAFB6A1463L,
            0xEC2BB4046BDBEE9FL, 0xE864A3559C6B8123L, 0xE4AD0A900AC6AB77L, 0xE104AA5CC856ED81L, 0xDD6B446842F7AE65L,
            0xD9E09B5E1EEFF399L, 0xD66472E521E8D767L, 0xD2F68F9B2E9B2FDFL, 0xCF96B71150EFF3CDL, 0xCC44AFC7DA5003D5L,
            0xC900412A8DE11143L, 0xC5C9338CDC6E6BE7L, 0xC29F50262FBD8AFBL, 0xBF82610E450F2C2FL, 0xBC723139968EE63BL,
            0xB96E8C75D3740AF5L, 0xB6773F666697AF3DL, 0xB38C17810B44A553L, 0xB0ACE30A700728ADL, 0xADD97112E742E8EBL,
            0xAB11917325570C3BL, 0xA85514C90C18A85BL, 0xA5A3CC74836D14E7L, 0xA2FD8A945ECE591BL, 0xA06222034F84D331L,
            0x9DD16654E362103BL, 0x9B4B2BD28FC9A05FL, 0x98CF4778C8D584CDL, 0x965D8EF42464A387L, 0x93F5D89E88E277A1L,
            0x9197FB7C6797FCA7L, 0x8F43CF3A02569879L, 0x8CF92C28BC4E8805L, 0x8AB7EB3C75E31201L, 0x887FE608F34F7DD1L,
            0x8650F6BF4DF08511L, 0x842AF82B7006ABD1L, 0x820DC5B19AC69DA5L,
            0xFBF33ADB4A247B63L, 0xF7F6DC82CD3A64A7L,
            0xF40AA289E556077BL, 0xF02E4B90F15A2E05L, 0xEC61974111819B83L, 0xE8A44647F724B981L, 0xE4F61A53C575AAD9L,
            0xE156D60F02EE122DL, 0xDDC63D1C9B2AF001L, 0xDA441413F0F40E21L, 0xD6D0207D002D7BBFL, 0xD36A28CC8F72A6ABL,
            0xD011F460711BA35BL, 0xCCC74B7BD36E35FBL, 0xC989F7439FBD2BEBL, 0xC659C1BAE8398DADL, 0xC33675BF643A2645L,
            0xC01FDF05FABECDF1L, 0xBD15CA175AF5D381L, 0xBA18044CA28AC8EDL, 0xB7265BCC1186DD6FL, 0xB4409F85CB8BD1DFL,
            0xB1669F30A6337225L, 0xAE982B47045E59F9L, 0xABD51503BE3DA293L, 0xA91D2E5F15E3EB11L, 0xA6704A0BB82AFE25L,
            0xA3CE3B73C9BC2527L, 0xA136D6B6000A02EBL, 0x9EA9F0A2C60B978BL, 0x9C275EB96C88D2A3L, 0x99AEF72565C9D9CBL,
            0x974090BB8C7AE74FL, 0x94DC02F775975F89L, 0x928125F8CD2F733DL, 0x902FD280BDDC5A1BL, 0x8DE7E1EF62B7E2BBL,
            0x8BA92E4143ACC451L, 0x8973920CDBF5CB1DL, 0x8746E8802AA2A203L, 0x85230D5E4CF9A2E7L, 0x8307DCFD228EBBC1L,
            0x80F53442FAE81817L,
            0xFBF8641D0EBD9F37L, 0xF80105531A5B1C69L, 0xF419A2322FD2AF1DL, 0xF041FA520DD315C5L,
            0xEC79CE4DFE225559L, 0xE8C0DFC0BFBA8355L, 0xE516F140815D3291L, 0xE17BC65AEC5B2B4FL, 0xDDEF23913F4F21DFL,
            0xDA70CE54788B2693L, 0xD7008D018FF98CB3L, 0xD39E26DDC03304F5L, 0xD0496412DE8CA2FBL, 0xCD020DABC1E17C4DL,
            0xC9C7ED90B7DC8245L, 0xC69ACE84088827A5L, 0xC37A7C1E87EA4E29L, 0xC066C2CC3573DFF5L, 0xBD5F6FC8E90C5D07L,
            0xBA64511D0D828555L, 0xB775359A682C2529L, 0xB491ECD8ED7FE2F1L, 0xB1BA4733A274C3BDL, 0xAEEE15C58A73EE4BL,
            0xAC2D2A66A1AA0429L, 0xA97757A8E386344DL, 0xA6CC70D55D35F399L, 0xA42C49E94BED0D87L, 0xA196B79346CA81D7L,
            0x9F0B8F30742B63A1L, 0x9C8AA6C9CA3DAADDL, 0x9A13D5115AA5A2F7L, 0x97A6F15FA90957BDL, 0x9543D3B10C5615B5L,
            0x92EA54A31A94C409L, 0x909A4D721F228C2FL, 0x8E5397F69B23EF17L, 0x8C160EA2D009104FL, 0x89E18C8053FAA5B1L,
            0x87B5ED2DB0079D91L, 0x85930CDC07EC2E7DL, 0x8378C84CCB4BA1BDL, 0x8166FCCF7036C5DBL, 0x7F5D883F36D98FB9L,
            0xFC371C6CEDDC7205L, 0xF87C8BD3AE403793L, 0xF4D017FDC4B91205L, 0xF1318B81E30CC827L, 0xEDA0B1C0E0ABFC8DL,
            0xEA1D56E2BDA00BFFL, 0xE6A747D3B0C88081L, 0xE33E5241413D4A33L, 0xDFE244976AAB90CBL, 0xDC92EDFDCC839099L,
            0xD9501E54E3CE92E1L, 0xD619A6334F84AC69L, 0xD2EF56E31F3A85D5L, 0xCFD1025F2C00079DL, 0xCCBE7B507B4957D5L,
            0xC9B7950BABBC29B5L, 0xC6BC238E6BBBEEC7L, 0xC3CBFB7CF9900767L, 0xC0E6F21FACFF9B93L, 0xBE0CDD608A3F4EE1L,
            0xBB3D93C8DE0D89BFL, 0xB878EC7EE2DA97A9L, 0xB5BEBF436EDA5D9DL, 0xB30EE46FAADDED93L, 0xB06934F2D1D3BB5FL,
            0xADCD8A4FF8CDB553L, 0xAB3BBE9BDF6CFEBBL, 0xA8B3AC7AC893846FL, 0xA6352F1E5B3C1C03L, 0xA3C022438B5A54A1L,
            0xA15462308AA39537L, 0x9EF1CBB2C12396F1L, 0x9C983C1CCD7EBBEFL, 0x9A4791448CC53213L, 0x97FFA98129BA3FADL,
            0x95C063A933738415L, 0x93899F10BB346123L, 0x915B3B87796A2BA5L, 0x8F351956F9AE2877L, 0x8D171940CDB2C21FL,
            0x8B011C7CC701C767L, 0x88F304B73771E64FL, 0x86ECB40F3839F8BFL, 0x84EE0D14F7891817L, 0x82F6F2C80C8ACBFBL,
            0xFC58F81E60DA5B81L, 0xF8BF4767540B5989L, 0xF532BD2113F26E23L, 0xF1B32943D2EFE0BBL, 0xEE405C77315E8669L,
            0xEADA280FBCD3AD3DL, 0xE7805E0C788358CFL, 0xE432D1146EA76C1DL, 0xE0F154744AC8D6D1L, 0xDDBBBC1BFCCA5427L,
            0xDA91DC9C6494C03BL, 0xD7738B2506457D31L, 0xD4609D81C6BFD51FL, 0xD158EA18B082B6BBL, 0xCE5C47E7C0A49B07L,
            0xCB6A8E82BBD7D049L, 0xC88396110B59D367L, 0xC5A7374BA1B0CBF9L, 0xC2D54B7AE71AA8D7L, 0xC00DAC74AD91C33BL,
            0xBD50349A2C4B5513L, 0xBA9CBED60294744FL, 0xB7F3269A41F2A8D9L, 0xB55347DE7F6D94E1L, 0xB2BCFF1DEBE7886DL,
            0xB0302955736B386BL, 0xADACA401E3552FB7L, 0xAB324D1E173FED4DL, 0xA8C103212C99FE8FL, 0xA658A4FCBCCDBE79L,
            0xA3F9121B1DE2BB8FL, 0xA1A22A5DA9811E33L, 0x9F53CE1B0A3FBF0DL, 0x9D0DDE1D8F25F2AFL, 0x9AD03BA1854A6351L,
            0x989AC8539778A469L, 0x966D664F33C77D53L, 0x9447F81CF70A38ADL, 0x922A60B11E07955BL, 0x90148369FC614411L,
            0x8E06440E7917299BL, 0x8BFF86CC9091E98BL, 0x8A003037DC208843L, 0x880825481ED53CAFL, 0x86174B57D7ADD2E5L,
            0x842D8822D8F4489BL,
            0xFC62A7389C25206BL, 0xF8D25F3BE7FB8C7BL, 0xF54EF8CFAF339DF7L, 0xF1D84564732D7D13L,
            0xEE6E171301EB70CFL, 0xEB10409A15BA9BBFL, 0xE7BE955BFD72A71BL, 0xE478E95C4D2D4FBFL, 0xE13F113D975738FFL,
            0xDE10E23F2DFBD7D5L, 0xDAEE323AEC2EB61DL, 0xD7D6D7A30774B8DFL, 0xD4CAA97FE9107EFBL, 0xD1C97F6E0F1555D9L,
            0xCED3319BF524A8EDL, 0xCBE798C804BA364DL, 0xC9068E3E8CEBB6F7L, 0xC62FEBD7C1810C09L, 0xC3638BF5C14A6369L,
            0xC0A14982A39A269BL, 0xBDE8FFEE8CC8E4CFL, 0xBB3A8B2DC9A9C6D7L, 0xB895C7B6F1D67747L, 0xB5FA928110B9C405L,
            0xB368C901D54097BFL, 0xB0E0492BC81941F7L, 0xAE60F16C88695B1DL, 0xABEAA0AB0EE2E7F7L, 0xA97D3645F721B3EBL,
            0xA7189211CF3A2D29L, 0xA4BC94576D635F85L, 0xA2691DD24BA5FC11L, 0xA01E0FAEE97AAAF3L, 0x9DDB4B89334233FBL,
            0x9BA0B36AEF8257ADL, 0x996E29CA31D27F19L, 0x97439187D363B43DL, 0x9520CDEDF10F9ECFL, 0x9305C2AE6EDA8AB9L,
            0x90F253E180D4C5C5L, 0x8EE666043947E8FFL, 0x8CE1DDF71C1CE8BBL, 0x8AE4A0FCB7680B4DL, 0x88EE94B841082A9DL,
            0x86FF9F2C3946E83FL, 0x8517A6B91267BDD5L, 0x8336921BDD1414F1L,
            0xFC8C1FB2CA6255C7L, 0xF9242B1AADEBBE4FL,
            0xF5C7F90F371F9D33L, 0xF27760F60D177BB7L, 0xEF323AC106E0D2BFL, 0xEBF85EEC4778D02FL, 0xE8C9A67C604F3EFDL,
            0xE5A5EAFC7A3A080BL, 0xE28D067C84C30FE3L, 0xDF7ED38F6BBA81FBL, 0xDC7B2D4952F7E615L, 0xD981EF3DD834A6D9L,
            0xD692F57E5AEAFA9DL, 0xD3AE1C984A2467EFL, 0xD0D3419378236739L, 0xCE0241F073D3E9A5L, 0xCB3AFBA6E7EED305L,
            0xC87D4D23FFBCB9B5L, 0xC5C91548D1648307L, 0xC31E3368CDB2B5FBL, 0xC07C87483546A027L, 0xBDE3F11A9312A9D7L,
            0xBB5451813C1D76CFL, 0xB8CD8989D471B091L, 0xB64F7AACD92A95ABL, 0xB3DA06CC2F8BA769L, 0xB16D1031B9120BFDL,
            0xAF08798DEC6E77A1L, 0xACAC25F67356AA49L, 0xAA57F8E4CD1DCA0FL, 0xA80BD634F6031C45L, 0xA5C7A2241326D801L,
            0xA38B414F2315052DL, 0xA15698B1B2D69281L, 0x9F298DA497790377L, 0x9D0405DCABFD4D61L, 0x9AE5E769939EAFBBL,
            0x98CF18B48062888FL, 0x96BF807EFDE25957L, 0x94B705E1C04163FBL, 0x92B5904B773F79F5L, 0x90BB077FA55AC933L,
            0x8EC753957AF2A37DL, 0x8CDA5CF6B55D6D8DL, 0x8AF40C5E81E41293L, 0x89144AD864958831L, 0x873B01BF22E51D3DL,
            0x85681ABBB2067B7BL,
            0xFC7A9A2A032A8977L, 0xF9019A3A031D55E3L, 0xF594D4886ED4F4B3L, 0xF2341E076B6CF033L,
            0xEEDF4C40B6E4AD29L, 0xEB963553925602DFL, 0xE858AFF2B3835555L, 0xE52693623DA357C7L, 0xE1FFB775C150F88DL,
            0xDEE3F48E43865153L, 0xDBD323984B89CEFBL, 0xD8CD1E09F7B51BF1L, 0xD5D1BDE118FDADE3L, 0xD2E0DDA155272C7BL,
            0xCFFA58524F883AE1L, 0xCD1E097DD84A8003L, 0xCA4BCD2E220F1B75L, 0xC7837FEBFDE104C1L, 0xC4C4FEBD1D5F2291L,
            0xC21027225B083507L, 0xBF64D7160892FBD3L, 0xBCC2ED0A433D4C8DL, 0xBA2A47E74DFD18D1L, 0xB79AC709F17EADA9L,
            0xB5144A41E1DBBE89L, 0xB296B1D029F71665L, 0xB021DE659C690FE7L, 0xADB5B12149E93C39L, 0xAB520B8EFD21E485L,
            0xA8F6CFA5BBDA566FL, 0xA6A3DFC64D652EFBL, 0xA4591EB9C640198DL, 0xA2166FB018D2B9ADL, 0x9FDBB63EAB3AB70DL,
            0x9DA8D65EF213227BL, 0x9B7DB46D1025A921L, 0x995A35267AF44947L, 0x973E3DA8A40A7899L, 0x9529B36FA704E833L,
            0x931C7C54FC3F4DBFL, 0x91167E8E3017D383L, 0x8F17A0AB9EB80AE7L, 0x8D1FC997345375ABL, 0x8B2EE09331CBF23FL,
            0x8944CD38F5AC8ED3L, 0x87617777C96B7D5DL, 0x8584C793B2E41889L, 0x83AEA62449FA1E85L, 0x81DEFC1392577B03L,
            0xFC89901CE7581669L, 0xF91F1DA477858FEDL, 0xF5C07F1263DFFDA1L, 0xF26D8B7221C57113L, 0xEF261A5CF6D22527L,
            0xEBEA03F80DD3CBDDL, 0xE8B920F29261309BL, 0xE5934A83D2FF2F77L, 0xE2785A6969BC515DL, 0xDF682AE56B2CABA7L,
            0xDC6296BC9BAFEF53L, 0xD9677934AAEBE025L, 0xD676AE127565B7BFL, 0xD39011984C254301L, 0xD0B38084424CD14FL,
            0xCDE0D80E809155D9L, 0xCB17F5E79E7E6275L, 0xC858B8370171E9F3L, 0xC5A2FD99413BFD59L, 0xC2F6A51E924EFC1DL,
            0xC0538E49356CF2EDL, 0xBDB9990BECBF2773L, 0xBB28A5C87645111DL, 0xB8A0954E0B884035L, 0xB62148D7E682F43BL,
            0xB3AAA20BCBA761E3L, 0xB13C82F898F5E761L, 0xAED6CE14DA10AB17L, 0xAC79663D613B5D61L, 0xAA242EB3E536122FL,
            0xA7D70B1DA3E261EBL, 0xA591DF8209A23B9BL, 0xA35490495D600B75L, 0xA11F023B713011C9L, 0x9EF11A7E577AFE2FL,
            0x9CCABE951CA219C3L, 0x9AABD45E850D81E9L, 0x98944213CF952A87L, 0x9683EE477C359391L, 0x947ABFE417015227L,
            0x92789E2B0740C0CFL, 0x907D70B362B15D3FL, 0x8E891F68C4D68C4DL, 0x8C9B928A2A4DAE11L, 0x8AB4B2A8D0179CD7L,
            0x88D468A716C9E203L, 0x86FA9DB7699A1B09L, 0x85273B5B293637D3L, 0x835A2B619A5C69D3L, 0x819357E6D825C8C5L
    };

}
