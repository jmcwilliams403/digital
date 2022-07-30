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

import java.util.Arrays;
import java.util.Random;

/**
 * Static methods for various frequently-used operations on 1D and 2D arrays. Has methods for copying, inserting, and
 * filling 2D arrays of primitive types (char, int, float, double, and boolean). Has a few methods for creating ranges
 * of ints or chars easily as 1D arrays.
 */
public final class ArrayTools {

    /**
     * Cannot be instantiated.
     */
    private ArrayTools() {
    }

    /**
     * 256 Latin and Greek letters, in upper and lower case, chosen so none should look identical. Useful when randomly
     * assigning identifying characters to values when you only have one char to display, and it should look unique.
     */
    private static final char[] letters = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'À', 'Á',
            'Â', 'Ã', 'Ä', 'Å', 'Æ', 'Ç', 'È', 'É', 'Ê', 'Ë', 'Ì', 'Í', 'Î', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', 'Ø', 'Ù', 'Ú', 'Û', 'Ü', 'Ý',
            'Þ', 'ß', 'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó', 'ô', 'õ', 'ö', 'ø', 'ù',
            'ú', 'û', 'ü', 'ý', 'þ', 'ÿ', 'Ā', 'ā', 'Ă', 'ă', 'Ą', 'ą', 'Ć', 'ć', 'Ĉ', 'ĉ', 'Ċ', 'ċ', 'Č', 'č', 'Ď', 'ď', 'Đ', 'đ', 'Ē', 'ē', 'Ĕ',
            'ĕ', 'Ė', 'ė', 'Ę', 'ę', 'Ě', 'ě', 'Ĝ', 'ĝ', 'Ğ', 'ğ', 'Ġ', 'ġ', 'Ģ', 'ģ', 'Ĥ', 'ĥ', 'Ħ', 'ħ', 'Ĩ', 'ĩ', 'Ī', 'ī', 'Ĭ', 'ĭ', 'Į', 'į',
            'İ', 'ı', 'Ĵ', 'ĵ', 'Ķ', 'ķ', 'ĸ', 'Ĺ', 'ĺ', 'Ļ', 'ļ', 'Ľ', 'ľ', 'Ŀ', 'ŀ', 'Ł', 'ł', 'Ń', 'ń', 'Ņ', 'ņ', 'Ň', 'ň', 'ŉ', 'Ō', 'ō', 'Ŏ',
            'ŏ', 'Ő', 'ő', 'Œ', 'œ', 'Ŕ', 'ŕ', 'Ŗ', 'ŗ', 'Ř', 'ř', 'Ś', 'ś', 'Ŝ', 'ŝ', 'Ş', 'ş', 'Š', 'š', 'Ţ', 'ţ', 'Ť', 'ť', 'Ŧ', 'ŧ', 'Ũ', 'ũ',
            'Ū', 'ū', 'Ŭ', 'ŭ', 'Ů', 'ů', 'Ű', 'ű', 'Ų', 'ų', 'Ŵ', 'ŵ', 'Ŷ', 'ŷ', 'Ÿ', 'Ź', 'ź', 'Ż', 'ż', 'Ž', 'ž', 'Ǿ', 'ǿ', 'Ș', 'ș', 'Ț', 'ț',
            'Γ', 'Δ', 'Θ', 'Λ', 'Ξ', 'Π', 'Σ', 'Φ', 'Ψ', 'Ω', 'α', 'β', 'γ'};

    private static final char[] emptyChars = new char[0];
    private static final int[] emptyInts = new int[0];
    private static final char[][] emptyChars2D = new char[0][0];
    private static final boolean[][] emptyBooleans2D = new boolean[0][0];
    private static final int[][] emptyInts2D = new int[0][0];
    private static final long[][] emptyLongs2D = new long[0][0];
    private static final float[][] emptyFloats2D = new float[0][0];
    private static final double[][] emptyDoubles2D = new double[0][0];

    /**
     * Stupidly simple convenience method that produces a range from 0 to end, not including end, as an int array.
     *
     * @param end the exclusive upper bound on the range
     * @return the range of ints as an int array
     */
    public static int[] range(int end) {
        if (end <= 0)
            return emptyInts;
        int[] r = new int[end];
        for (int i = 0; i < end; i++) {
            r[i] = i;
        }
        return r;
    }

    /**
     * Fills the given int array with sequential ints from 0 to {@code buffer.length - 1}.
     *
     * @param buffer an int array that will be modified in-place; if null this returns null
     * @return buffer, after modifications
     */
    public static int[] range(int[] buffer) {
        int len;
        if (buffer == null || (len = buffer.length) == 0) return buffer;
        for (int i = 0; i < len; i++) {
            buffer[i] = i;
        }
        return buffer;
    }

    /**
     * Stupidly simple convenience method that produces a range from start to end, not including end, as an int array.
     *
     * @param start the inclusive lower bound on the range
     * @param end   the exclusive upper bound on the range
     * @return the range of ints as an int array
     */
    public static int[] range(int start, int end) {
        if (end - start <= 0)
            return emptyInts;
        int[] r = new int[end - start];
        for (int i = 0, n = start; n < end; i++, n++) {
            r[i] = n;
        }
        return r;
    }

    /**
     * Stupidly simple convenience method that produces a char range from start to end, including end, as a char array.
     *
     * @param start the inclusive lower bound on the range, such as 'a'
     * @param end   the inclusive upper bound on the range, such as 'z'
     * @return the range of chars as a char array
     */
    public static char[] charSpan(char start, char end) {
        if (end - start <= 0)
            return emptyChars;
        if (end == 0xffff) {

            char[] r = new char[0x10000 - start];
            for (char i = 0, n = start; n < end; i++, n++) {
                r[i] = n;
            }
            r[0xffff - start] = 0xffff;
            return r;
        }
        char[] r = new char[end - start + 1];
        for (char i = 0, n = start; n <= end; i++, n++) {
            r[i] = n;
        }
        return r;
    }

    /**
     * Stupidly simple convenience method that produces a char array containing only letters that can be reasonably
     * displayed with many fonts. The letters are copied from a single source
     * of 256 chars; if you need more chars or you don't need pure letters, you can use {@link #charSpan(char, char)}.
     * This set does not contain "visual duplicate" letters, such as Latin alphabet capital letter 'A' and Greek
     * alphabet capital letter alpha, 'Α'; it does contain many accented Latin letters and the visually-distinct Greek
     * letters, up to a point.
     *
     * @param charCount the number of letters to return in an array; the maximum this will produce is 256
     * @return the range of letters as a char array
     */
    public static char[] letterSpan(int charCount) {
        if (charCount <= 0)
            return emptyChars;
        char[] r = new char[Math.min(charCount, 256)];
        System.arraycopy(letters, 0, r, 0, r.length);
        return r;
    }

    /**
     * Gets the nth letter from the set of 256 visually distinct glyphs; from index 0 (returning 'A') to 255
     * (returning the Greek lower-case letter gamma, 'γ') and wrapping around if given negative numbers or numbers
     * larger than 255. This set does not contain "visual duplicate" letters, such as Latin alphabet capital letter 'A'
     * and Greek alphabet capital letter alpha, 'Α'; it does contain many accented Latin letters and the
     * visually-distinct Greek letters, up to a point.
     *
     * @param index typically from 0 to 255, but all ints are allowed and will produce letters
     * @return the letter at the given index in a 256-element portion of visually distinct letters
     */
    public static char letterAt(int index) {
        return letters[index & 255];
    }

    /**
     * Gets a copy of the 2D char array, source, that has the same data but shares no references with source.
     *
     * @param source a 2D char array
     * @return a copy of source, or null if source is null
     */
    public static char[][] copy(char[][] source) {
        if (source == null)
            return null;
        if (source.length < 1)
            return emptyChars2D;
        char[][] target = new char[source.length][];
        for (int i = 0; i < source.length; i++) {
            final int len = source[i].length;
            target[i] = new char[len];
            System.arraycopy(source[i], 0, target[i], 0, len);
        }
        return target;
    }

    /**
     * Gets a copy of the 2D double array, source, that has the same data but shares no references with source.
     *
     * @param source a 2D double array
     * @return a copy of source, or null if source is null
     */
    public static double[][] copy(double[][] source) {
        if (source == null)
            return null;
        if (source.length < 1)
            return emptyDoubles2D;
        double[][] target = new double[source.length][];
        for (int i = 0; i < source.length; i++) {
            final int len = source[i].length;
            target[i] = new double[len];
            System.arraycopy(source[i], 0, target[i], 0, len);
        }
        return target;
    }


    /**
     * Gets a copy of the 2D float array, source, that has the same data but shares no references with source.
     *
     * @param source a 2D float array
     * @return a copy of source, or null if source is null
     */
    public static float[][] copy(float[][] source) {
        if (source == null)
            return null;
        if (source.length < 1)
            return emptyFloats2D;
        float[][] target = new float[source.length][];
        for (int i = 0; i < source.length; i++) {
            final int len = source[i].length;
            target[i] = new float[len];
            System.arraycopy(source[i], 0, target[i], 0, len);
        }
        return target;
    }

    /**
     * Gets a copy of the 2D int array, source, that has the same data but shares no references with source.
     *
     * @param source a 2D int array
     * @return a copy of source, or null if source is null
     */
    public static int[][] copy(int[][] source) {
        if (source == null)
            return null;
        if (source.length < 1)
            return emptyInts2D;
        int[][] target = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            final int len = source[i].length;
            target[i] = new int[len];
            System.arraycopy(source[i], 0, target[i], 0, len);
        }
        return target;
    }

    /**
     * Gets a copy of the 2D long array, source, that has the same data but shares no references with source.
     *
     * @param source a 2D long array
     * @return a copy of source, or null if source is null
     */
    public static long[][] copy(long[][] source) {
        if (source == null)
            return null;
        if (source.length < 1)
            return emptyLongs2D;
        long[][] target = new long[source.length][];
        for (int i = 0; i < source.length; i++) {
            final int len = source[i].length;
            target[i] = new long[len];
            System.arraycopy(source[i], 0, target[i], 0, len);
        }
        return target;
    }

    /**
     * Gets a copy of the 2D boolean array, source, that has the same data but shares no references with source.
     *
     * @param source a 2D boolean array
     * @return a copy of source, or null if source is null
     */
    public static boolean[][] copy(boolean[][] source) {
        if (source == null)
            return null;
        if (source.length < 1)
            return emptyBooleans2D;
        boolean[][] target = new boolean[source.length][];
        for (int i = 0; i < source.length; i++) {
            final int len = source[i].length;
            target[i] = new boolean[len];
            System.arraycopy(source[i], 0, target[i], 0, len);
        }
        return target;
    }

    /**
     * Inserts as much of source into target at the given x,y position as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * Used primarily to place a smaller array into a different position in a larger array, often freshly allocated.
     *
     * @param source a 2D char array that will be copied and inserted into target
     * @param target a 2D char array that will be modified by receiving as much of source as it can hold
     * @param x      the x position in target to receive the items from the first cell in source
     * @param y      the y position in target to receive the items from the first cell in source
     * @return target, modified, with source inserted into it at the given position
     */
    public static char[][] insert(char[][] source, char[][] target, int x, int y) {
        if (source == null || target == null)
            return target;
        if (source.length < 1 || source[0].length < 1)
            return target;
        for (int i = 0; i < source.length && x + i < target.length; i++) {
            System.arraycopy(source[i], 0, target[x + i], y, Math.min(source[i].length, target[x + i].length - y));
        }
        return target;
    }

    /**
     * Inserts as much of source into target at the given x,y position as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * Used primarily to place a smaller array into a different position in a larger array, often freshly allocated.
     *
     * @param source a 2D double array that will be copied and inserted into target
     * @param target a 2D double array that will be modified by receiving as much of source as it can hold
     * @param x      the x position in target to receive the items from the first cell in source
     * @param y      the y position in target to receive the items from the first cell in source
     * @return target, modified, with source inserted into it at the given position
     */
    public static double[][] insert(double[][] source, double[][] target, int x, int y) {
        if (source == null || target == null)
            return target;
        if (source.length < 1 || source[0].length < 1)
            return target;
        for (int i = 0; i < source.length && x + i < target.length; i++) {
            System.arraycopy(source[i], 0, target[x + i], y, Math.min(source[i].length, target[x + i].length - y));
        }
        return target;
    }

    /**
     * Inserts as much of source into target at the given x,y position as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * Used primarily to place a smaller array into a different position in a larger array, often freshly allocated.
     *
     * @param source a 2D float array that will be copied and inserted into target
     * @param target a 2D float array that will be modified by receiving as much of source as it can hold
     * @param x      the x position in target to receive the items from the first cell in source
     * @param y      the y position in target to receive the items from the first cell in source
     * @return target, modified, with source inserted into it at the given position
     */
    public static float[][] insert(float[][] source, float[][] target, int x, int y) {
        if (source == null || target == null)
            return target;
        if (source.length < 1 || source[0].length < 1)
            return target;
        for (int i = 0; i < source.length && x + i < target.length; i++) {
            System.arraycopy(source[i], 0, target[x + i], y, Math.min(source[i].length, target[x + i].length - y));
        }
        return target;
    }

    /**
     * Inserts as much of source into target at the given x,y position as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * Used primarily to place a smaller array into a different position in a larger array, often freshly allocated.
     *
     * @param source a 2D int array that will be copied and inserted into target
     * @param target a 2D int array that will be modified by receiving as much of source as it can hold
     * @param x      the x position in target to receive the items from the first cell in source
     * @param y      the y position in target to receive the items from the first cell in source
     * @return target, modified, with source inserted into it at the given position
     */
    public static int[][] insert(int[][] source, int[][] target, int x, int y) {
        if (source == null || target == null)
            return target;
        if (source.length < 1 || source[0].length < 1)
            return target;
        for (int i = 0; i < source.length && x + i < target.length; i++) {
            System.arraycopy(source[i], 0, target[x + i], y, Math.min(source[i].length, target[x + i].length - y));
        }
        return target;
    }

    /**
     * Inserts as much of source into target at the given x,y position as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * Used primarily to place a smaller array into a different position in a larger array, often freshly allocated.
     *
     * @param source a 2D long array that will be copied and inserted into target
     * @param target a 2D long array that will be modified by receiving as much of source as it can hold
     * @param x      the x position in target to receive the items from the first cell in source
     * @param y      the y position in target to receive the items from the first cell in source
     * @return target, modified, with source inserted into it at the given position
     */
    public static long[][] insert(long[][] source, long[][] target, int x, int y) {
        if (source == null || target == null)
            return target;
        if (source.length < 1 || source[0].length < 1)
            return target;
        for (int i = 0; i < source.length && x + i < target.length; i++) {
            System.arraycopy(source[i], 0, target[x + i], y, Math.min(source[i].length, target[x + i].length - y));
        }
        return target;
    }

    /**
     * Inserts as much of source into target at the given x,y position as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * Used primarily to place a smaller array into a different position in a larger array, often freshly allocated.
     *
     * @param source a 2D boolean array that will be copied and inserted into target
     * @param target a 2D boolean array that will be modified by receiving as much of source as it can hold
     * @param x      the x position in target to receive the items from the first cell in source
     * @param y      the y position in target to receive the items from the first cell in source
     * @return target, modified, with source inserted into it at the given position
     */
    public static boolean[][] insert(boolean[][] source, boolean[][] target, int x, int y) {
        if (source == null || target == null)
            return target;
        if (source.length < 1 || source[0].length < 1)
            return target;
        for (int i = 0; i < source.length && x + i < target.length; i++) {
            System.arraycopy(source[i], 0, target[x + i], y, Math.min(source[i].length, target[x + i].length - y));
        }
        return target;
    }

    /**
     * Inserts as much of source into target at the given x,y position as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * Used primarily to place a smaller array into a different position in a larger array, often freshly allocated.
     * This does not create copies of any T items; {@code source} will reference the same T items as {@code target}.
     *
     * @param source a 2D generic array that will be copied and inserted into target
     * @param target a 2D generic array that will be modified by receiving as much of source as it can hold
     * @param x      the x position in target to receive the items from the first cell in source
     * @param y      the y position in target to receive the items from the first cell in source
     * @return target, modified, with source inserted into it at the given position
     */
    public static <T> T[][] insert(T[][] source, T[][] target, int x, int y) {
        if (source == null || target == null)
            return target;
        if (source.length < 1 || source[0].length < 1)
            return target;
        for (int i = 0; i < source.length && x + i < target.length; i++) {
            System.arraycopy(source[i], 0, target[x + i], y, Math.min(source[i].length, target[x + i].length - y));
        }
        return target;
    }

    /**
     * Takes a 2D source array and sets it into a 2D target array, as much as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * This is just like {@link #insert(char[][], char[][], int, int)} with x and y both always 0, but does slightly
     * less math per call, and can be clearer as to the intent of the method.
     *
     * @param source a 2D char array that will be copied and inserted into target
     * @param target a 2D char array that will be modified by receiving as much of source as it can hold
     * @return target, modified, with the values from source set as much as possible
     */
    public static char[][] set(char[][] source, char[][] target) {
        if (source == null || target == null || source.length == 0)
            return target;
        final int minWidth = Math.min(source.length, target.length);
        for (int i = 0; i < minWidth; i++) {
            System.arraycopy(source[i], 0, target[i], 0, Math.min(source[i].length, target[i].length));
        }
        return target;
    }

    /**
     * Takes a 2D source array and sets it into a 2D target array, as much as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * This is just like {@link #insert(float[][], float[][], int, int)} with x and y both always 0, but does slightly
     * less math per call, and can be clearer as to the intent of the method.
     *
     * @param source a 2D float array that will be copied and inserted into target
     * @param target a 2D float array that will be modified by receiving as much of source as it can hold
     * @return target, modified, with the values from source set as much as possible
     */
    public static float[][] set(float[][] source, float[][] target) {
        if (source == null || target == null || source.length == 0)
            return target;
        final int minWidth = Math.min(source.length, target.length);
        for (int i = 0; i < minWidth; i++) {
            System.arraycopy(source[i], 0, target[i], 0, Math.min(source[i].length, target[i].length));
        }
        return target;
    }

    /**
     * Takes a 2D source array and sets it into a 2D target array, as much as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * This is just like {@link #insert(double[][], double[][], int, int)} with x and y both always 0, but does slightly
     * less math per call, and can be clearer as to the intent of the method.
     *
     * @param source a 2D double array that will be copied and inserted into target
     * @param target a 2D double array that will be modified by receiving as much of source as it can hold
     * @return target, modified, with the values from source set as much as possible
     */
    public static double[][] set(double[][] source, double[][] target) {
        if (source == null || target == null || source.length == 0)
            return target;
        final int minWidth = Math.min(source.length, target.length);
        for (int i = 0; i < minWidth; i++) {
            System.arraycopy(source[i], 0, target[i], 0, Math.min(source[i].length, target[i].length));
        }
        return target;
    }

    /**
     * Takes a 2D source array and sets it into a 2D target array, as much as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * This is just like {@link #insert(int[][], int[][], int, int)} with x and y both always 0, but does slightly
     * less math per call, and can be clearer as to the intent of the method.
     *
     * @param source a 2D int array that will be copied and inserted into target
     * @param target a 2D int array that will be modified by receiving as much of source as it can hold
     * @return target, modified, with the values from source set as much as possible
     */
    public static int[][] set(int[][] source, int[][] target) {
        if (source == null || target == null || source.length == 0)
            return target;
        final int minWidth = Math.min(source.length, target.length);
        for (int i = 0; i < minWidth; i++) {
            System.arraycopy(source[i], 0, target[i], 0, Math.min(source[i].length, target[i].length));
        }
        return target;
    }

    /**
     * Takes a 2D source array and sets it into a 2D target array, as much as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * This is just like {@link #insert(long[][], long[][], int, int)} with x and y both always 0, but does slightly
     * less math per call, and can be clearer as to the intent of the method.
     *
     * @param source a 2D long array that will be copied and inserted into target
     * @param target a 2D long array that will be modified by receiving as much of source as it can hold
     * @return target, modified, with the values from source set as much as possible
     */
    public static long[][] set(long[][] source, long[][] target) {
        if (source == null || target == null || source.length == 0)
            return target;
        final int minWidth = Math.min(source.length, target.length);
        for (int i = 0; i < minWidth; i++) {
            System.arraycopy(source[i], 0, target[i], 0, Math.min(source[i].length, target[i].length));
        }
        return target;
    }

    /**
     * Takes a 2D source array and sets it into a 2D target array, as much as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * This is just like {@link #insert(boolean[][], boolean[][], int, int)} with x and y both always 0, but does slightly
     * less math per call, and can be clearer as to the intent of the method.
     *
     * @param source a 2D boolean array that will be copied and inserted into target
     * @param target a 2D boolean array that will be modified by receiving as much of source as it can hold
     * @return target, modified, with the values from source set as much as possible
     */
    public static boolean[][] set(boolean[][] source, boolean[][] target) {
        if (source == null || target == null || source.length == 0)
            return target;
        final int minWidth = Math.min(source.length, target.length);
        for (int i = 0; i < minWidth; i++) {
            System.arraycopy(source[i], 0, target[i], 0, Math.min(source[i].length, target[i].length));
        }
        return target;
    }

    /**
     * Takes a 2D source array and sets it into a 2D target array, as much as target can hold or source can supply.
     * Modifies target in-place and also returns target for chaining.
     * This is just like {@link #insert(Object[][], Object[][], int, int)} with x and y both always 0, but does slightly
     * less math per call, and can be clearer as to the intent of the method.
     * This does not create copies of any T items; {@code source} will reference the same T items as {@code target}.
     *
     * @param source a 2D generic array that will be copied and inserted into target
     * @param target a 2D generic array that will be modified by receiving as much of source as it can hold
     * @return target, modified, with the values from source set as much as possible
     */
    public static <T> T[][] set(T[][] source, T[][] target) {
        if (source == null || target == null || source.length == 0)
            return target;
        final int minWidth = Math.min(source.length, target.length);
        for (int i = 0; i < minWidth; i++) {
            System.arraycopy(source[i], 0, target[i], 0, Math.min(source[i].length, target[i].length));
        }
        return target;
    }

    /**
     * Creates a 2D array of the given width and height, filled with entirely with the value contents.
     * You may want to use {@link #fill(char[][], char)} to modify an existing 2D array instead.
     *
     * @param contents the value to fill the array with
     * @param width    the desired width
     * @param height   the desired height
     * @return a freshly allocated 2D array of the requested dimensions, filled entirely with contents
     */
    public static char[][] fill(char contents, int width, int height) {
        char[][] next = new char[width][height];
        for (int x = 0; x < width; x++) {
            Arrays.fill(next[x], contents);
        }
        return next;
    }

    /**
     * Creates a 2D array of the given width and height, filled with entirely with the value contents.
     * You may want to use {@link #fill(float[][], float)} to modify an existing 2D array instead.
     *
     * @param contents the value to fill the array with
     * @param width    the desired width
     * @param height   the desired height
     * @return a freshly allocated 2D array of the requested dimensions, filled entirely with contents
     */
    public static float[][] fill(float contents, int width, int height) {
        float[][] next = new float[width][height];
        for (int x = 0; x < width; x++) {
            Arrays.fill(next[x], contents);
        }
        return next;
    }

    /**
     * Creates a 2D array of the given width and height, filled with entirely with the value contents.
     * You may want to use {@link #fill(double[][], double)} to modify an existing 2D array instead.
     *
     * @param contents the value to fill the array with
     * @param width    the desired width
     * @param height   the desired height
     * @return a freshly allocated 2D array of the requested dimensions, filled entirely with contents
     */
    public static double[][] fill(double contents, int width, int height) {
        double[][] next = new double[width][height];
        for (int x = 0; x < width; x++) {
            Arrays.fill(next[x], contents);
        }
        return next;
    }

    /**
     * Creates a 2D array of the given width and height, filled with entirely with the value contents.
     * You may want to use {@link #fill(int[][], int)} to modify an existing 2D array instead.
     *
     * @param contents the value to fill the array with
     * @param width    the desired width
     * @param height   the desired height
     * @return a freshly allocated 2D array of the requested dimensions, filled entirely with contents
     */
    public static int[][] fill(int contents, int width, int height) {
        int[][] next = new int[width][height];
        for (int x = 0; x < width; x++) {
            Arrays.fill(next[x], contents);
        }
        return next;
    }

    /**
     * Creates a 2D array of the given width and height, filled with entirely with the value contents.
     * You may want to use {@link #fill(long[][], long)} to modify an existing 2D array instead.
     *
     * @param contents the value to fill the array with
     * @param width    the desired width
     * @param height   the desired height
     * @return a freshly allocated 2D array of the requested dimensions, filled entirely with contents
     */
    public static long[][] fill(long contents, int width, int height) {
        long[][] next = new long[width][height];
        for (int x = 0; x < width; x++) {
            Arrays.fill(next[x], contents);
        }
        return next;
    }

    /**
     * Creates a 2D array of the given width and height, filled with entirely with the value contents.
     * You may want to use {@link #fill(byte[][], byte)} to modify an existing 2D array instead.
     *
     * @param contents the value to fill the array with
     * @param width    the desired width
     * @param height   the desired height
     * @return a freshly allocated 2D array of the requested dimensions, filled entirely with contents
     */
    public static byte[][] fill(byte contents, int width, int height) {
        byte[][] next = new byte[width][height];
        for (int x = 0; x < width; x++) {
            Arrays.fill(next[x], contents);
        }
        return next;
    }

    /**
     * Creates a 2D array of the given width and height, filled with entirely with the value contents.
     * You may want to use {@link #fill(boolean[][], boolean)} to modify an existing 2D array instead.
     *
     * @param contents the value to fill the array with
     * @param width    the desired width
     * @param height   the desired height
     * @return a freshly allocated 2D array of the requested dimensions, filled entirely with contents
     */
    public static boolean[][] fill(boolean contents, int width, int height) {
        boolean[][] next = new boolean[width][height];
        if (contents) {
            for (int x = 0; x < width; x++) {
                Arrays.fill(next[x], true);
            }
        }
        return next;
    }

    /**
     * Fills {@code array2d} with {@code value}.
     * Not to be confused with {@link #fill(boolean, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array2D with
     */
    public static void fill(boolean[][] array2d, boolean value) {
        final int width = array2d.length;
        for (int i = 0; i < width; i++) {
            Arrays.fill(array2d[i], value);
        }
    }

    /**
     * Fills {@code array2d} with {@code value}.
     * Not to be confused with {@link #fill(char, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array2D with
     */
    public static void fill(char[][] array2d, char value) {
        final int width = array2d.length;
        for (int i = 0; i < width; i++) {
            Arrays.fill(array2d[i], value);
        }
    }

    /**
     * Fills {@code array2d} with {@code value}.
     * Not to be confused with {@link #fill(float, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array2D with
     */
    public static void fill(float[][] array2d, float value) {
        final int width = array2d.length;
        for (int i = 0; i < width; i++) {
            Arrays.fill(array2d[i], value);
        }
    }


    /**
     * Fills {@code array2d} with {@code value}.
     * Not to be confused with {@link #fill(double, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array2D with
     */
    public static void fill(double[][] array2d, double value) {
        final int width = array2d.length;
        for (int i = 0; i < width; i++) {
            Arrays.fill(array2d[i], value);
        }
    }

    /**
     * Fills {@code array2d} with {@code value}.
     * Not to be confused with {@link #fill(int, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array2D with
     */
    public static void fill(int[][] array2d, int value) {
        final int width = array2d.length;
        for (int i = 0; i < width; i++) {
            Arrays.fill(array2d[i], value);
        }
    }

    /**
     * Fills {@code array2d} with {@code value}.
     * Not to be confused with {@link #fill(long, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array2D with
     */
    public static void fill(long[][] array2d, long value) {
        final int width = array2d.length;
        for (int i = 0; i < width; i++) {
            Arrays.fill(array2d[i], value);
        }
    }

    /**
     * Fills {@code array2d} with {@code value}.
     * Not to be confused with {@link #fill(byte, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array2D with
     */
    public static void fill(byte[][] array2d, byte value) {
        final int width = array2d.length;
        for (int i = 0; i < width; i++) {
            Arrays.fill(array2d[i], value);
        }
    }
    /**
     * Fills {@code array2d} with identical references to {@code value} (not copies).
     * Note that there is no fill() method that creates a new 2D array of a generic type.
     *
     * @param array2d a 2D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array2D with
     */
    public static <T> void fill(T[][] array2d, T value) {
        final int width = array2d.length;
        for (int i = 0; i < width; i++) {
            Arrays.fill(array2d[i], value);
        }
    }

    /**
     * Fills {@code array3d} with {@code value}.
     * Not to be confused with {@link #fill(boolean[][], boolean)}, which fills a 2D array instead of a 3D one, or with
     * {@link #fill(boolean, int, int)}, which makes a new 2D array.
     * This is named differently to avoid ambiguity between a 1D array of {@code boolean[][]}, which this can take, and a
     * 2D array of {@code boolean[]}, which could be given to {@link #fill(Object[][], Object)}, but could also be given
     * to this.
     *
     * @param array3d a 3D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array3d with
     */
    public static void fill3D(boolean[][][] array3d, boolean value) {
        final int depth = array3d.length;
        final int width = depth == 0 ? 0 : array3d[0].length;
        final int height = width == 0 ? 0 : array3d[0][0].length;
        if (depth > 0 && width > 0) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Arrays.fill(array3d[i][j], value);
                }
            }
        }
    }
    
    /**
     * Fills {@code array3d} with {@code value}.
     * Not to be confused with {@link #fill(char[][], char)}, which fills a 2D array instead of a 3D one, or with
     * {@link #fill(char, int, int)}, which makes a new 2D array.
     * This is named differently to avoid ambiguity between a 1D array of {@code char[][]}, which this can take, and a
     * 2D array of {@code char[]}, which could be given to {@link #fill(Object[][], Object)}, but could also be given
     * to this.
     *
     * @param array3d a 3D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array3d with
     */
    public static void fill3D(char[][][] array3d, char value) {
        final int depth = array3d.length;
        final int width = depth == 0 ? 0 : array3d[0].length;
        final int height = width == 0 ? 0 : array3d[0][0].length;
        if (depth > 0 && width > 0) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Arrays.fill(array3d[i][j], value);
                }
            }
        }
    }
    
    /**
     * Fills {@code array3d} with {@code value}.
     * Not to be confused with {@link #fill(float[][], float)}, which fills a 2D array instead of a 3D one, or with
     * {@link #fill(float, int, int)}, which makes a new 2D array.
     * This is named differently to avoid ambiguity between a 1D array of {@code float[][]}, which this can take, and a
     * 2D array of {@code float[]}, which could be given to {@link #fill(Object[][], Object)}, but could also be given
     * to this.
     *
     * @param array3d a 3D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array3d with
     */
    public static void fill3D(float[][][] array3d, float value) {
        final int depth = array3d.length;
        final int width = depth == 0 ? 0 : array3d[0].length;
        final int height = width == 0 ? 0 : array3d[0][0].length;
        if (depth > 0 && width > 0) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Arrays.fill(array3d[i][j], value);
                }
            }
        }
    }

    /**
     * Fills {@code array3d} with {@code value}.
     * Not to be confused with {@link #fill(double[][], double)}, which fills a 2D array instead of a 3D one, or with
     * {@link #fill(double, int, int)}, which makes a new 2D array.
     * This is named differently to avoid ambiguity between a 1D array of {@code double[][]}, which this can take, and a
     * 2D array of {@code double[]}, which could be given to {@link #fill(Object[][], Object)}, but could also be given
     * to this.
     *
     * @param array3d a 3D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array3d with
     */
    public static void fill3D(double[][][] array3d, double value) {
        final int depth = array3d.length;
        final int width = depth == 0 ? 0 : array3d[0].length;
        final int height = width == 0 ? 0 : array3d[0][0].length;
        if (depth > 0 && width > 0) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Arrays.fill(array3d[i][j], value);
                }
            }
        }
    }

    /**
     * Fills {@code array3d} with {@code value}.
     * Not to be confused with {@link #fill(int[][], int)}, which fills a 2D array instead of a 3D one, or with
     * {@link #fill(int, int, int)}, which makes a new 2D array.
     * This is named differently to avoid ambiguity between a 1D array of {@code int[][]}, which this can take, and a
     * 2D array of {@code int[]}, which could be given to {@link #fill(Object[][], Object)}, but could also be given
     * to this.
     *
     * @param array3d a 3D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array3d with
     */
    public static void fill3D(int[][][] array3d, int value) {
        final int depth = array3d.length;
        final int width = depth == 0 ? 0 : array3d[0].length;
        final int height = width == 0 ? 0 : array3d[0][0].length;
        if (depth > 0 && width > 0) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Arrays.fill(array3d[i][j], value);
                }
            }
        }
    }

    /**
     * Fills {@code array3d} with {@code value}.
     * Not to be confused with {@link #fill(long[][], long)}, which fills a 2D array instead of a 3D one, or with
     * {@link #fill(long, int, int)}, which makes a new 2D array.
     * This is named differently to avoid ambiguity between a 1D array of {@code long[][]}, which this can take, and a
     * 2D array of {@code long[]}, which could be given to {@link #fill(Object[][], Object)}, but could also be given
     * to this.
     *
     * @param array3d a 3D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array3d with
     */
    public static void fill3D(long[][][] array3d, long value) {
        final int depth = array3d.length;
        final int width = depth == 0 ? 0 : array3d[0].length;
        final int height = width == 0 ? 0 : array3d[0][0].length;
        if (depth > 0 && width > 0) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Arrays.fill(array3d[i][j], value);
                }
            }
        }
    }

    /**
     * Fills {@code array3d} with {@code value}.
     * Not to be confused with {@link #fill(byte[][], byte)}, which fills a 2D array instead of a 3D one, or with
     * {@link #fill(byte, int, int)}, which makes a new 2D array.
     * This is named differently to avoid ambiguity between a 1D array of {@code byte[][]}, which this can take, and a
     * 2D array of {@code byte[]}, which could be given to {@link #fill(Object[][], Object)}, but could also be given
     * to this.
     *
     * @param array3d a 3D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array3d with
     */
    public static void fill3D(byte[][][] array3d, byte value) {
        final int depth = array3d.length;
        final int width = depth == 0 ? 0 : array3d[0].length;
        final int height = width == 0 ? 0 : array3d[0][0].length;
        if (depth > 0 && width > 0) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Arrays.fill(array3d[i][j], value);
                }
            }
        }
    }

    /**
     * Fills {@code array3d} with identical references to {@code value} (not copies).
     * Not to be confused with {@link #fill(Object[][], Object)}, which fills a 2D array instead of a 3D one.
     * This is named differently to avoid ambiguity between a 1D array of {@code T[][]}, which this can take, and a
     * 2D array of {@code T[]}, which could be given to {@link #fill(Object[][], Object)}, but could also be given
     * to this.
     *
     * @param array3d a 3D array that will be modified in-place; no sub-arrays can be null
     * @param value   the value to fill all of array3d with
     */
    public static <T> void fill3D(T[][][] array3d, T value) {
        final int depth = array3d.length;
        final int width = depth == 0 ? 0 : array3d[0].length;
        final int height = width == 0 ? 0 : array3d[0][0].length;
        if (depth > 0 && width > 0) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Arrays.fill(array3d[i][j], value);
                }
            }
        }
    }

    /**
     * Fills a sub-section of {@code array2d} with {@code value}, with the section defined by start/end x/y.
     * Not to be confused with {@link #fill(boolean, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place
     * @param value   the value to fill all of array2D with
     * @param startX  the first x position to fill (inclusive)
     * @param startY  the first y position to fill (inclusive)
     * @param endX    the last x position to fill (inclusive)
     * @param endY    the last y position to fill (inclusive)
     */
    public static void fill(boolean[][] array2d, boolean value, int startX, int startY, int endX, int endY) {
        final int width = array2d.length;
        final int height = width == 0 ? 0 : array2d[0].length;
        for (int x = startX; x <= endX && x < width; x++) {
            for (int y = startY; y <= endY && y < height; y++) {
                array2d[x][y] = value;
            }
        }
    }

    /**
     * Fills a sub-section of {@code array2d} with {@code value}, with the section defined by start/end x/y.
     * Not to be confused with {@link #fill(char, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place
     * @param value   the value to fill all of array2D with
     * @param startX  the first x position to fill (inclusive)
     * @param startY  the first y position to fill (inclusive)
     * @param endX    the last x position to fill (inclusive)
     * @param endY    the last y position to fill (inclusive)
     */
    public static void fill(char[][] array2d, char value, int startX, int startY, int endX, int endY) {
        final int width = array2d.length;
        final int height = width == 0 ? 0 : array2d[0].length;
        for (int x = startX; x <= endX && x < width; x++) {
            for (int y = startY; y <= endY && y < height; y++) {
                array2d[x][y] = value;
            }
        }
    }

    /**
     * Fills a sub-section of {@code array2d} with {@code value}, with the section defined by start/end x/y.
     * Not to be confused with {@link #fill(float, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place
     * @param value   the value to fill all of array2D with
     * @param startX  the first x position to fill (inclusive)
     * @param startY  the first y position to fill (inclusive)
     * @param endX    the last x position to fill (inclusive)
     * @param endY    the last y position to fill (inclusive)
     */
    public static void fill(float[][] array2d, float value, int startX, int startY, int endX, int endY) {
        final int width = array2d.length;
        final int height = width == 0 ? 0 : array2d[0].length;
        for (int x = startX; x <= endX && x < width; x++) {
            for (int y = startY; y <= endY && y < height; y++) {
                array2d[x][y] = value;
            }
        }
    }

    /**
     * Fills a sub-section of {@code array2d} with {@code value}, with the section defined by start/end x/y.
     * Not to be confused with {@link #fill(double, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place
     * @param value   the value to fill all of array2D with
     * @param startX  the first x position to fill (inclusive)
     * @param startY  the first y position to fill (inclusive)
     * @param endX    the last x position to fill (inclusive)
     * @param endY    the last y position to fill (inclusive)
     */
    public static void fill(double[][] array2d, double value, int startX, int startY, int endX, int endY) {
        final int width = array2d.length;
        final int height = width == 0 ? 0 : array2d[0].length;
        for (int x = startX; x <= endX && x < width; x++) {
            for (int y = startY; y <= endY && y < height; y++) {
                array2d[x][y] = value;
            }
        }
    }

    /**
     * Fills a sub-section of {@code array2d} with {@code value}, with the section defined by start/end x/y.
     * Not to be confused with {@link #fill(int, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place
     * @param value   the value to fill all of array2D with
     * @param startX  the first x position to fill (inclusive)
     * @param startY  the first y position to fill (inclusive)
     * @param endX    the last x position to fill (inclusive)
     * @param endY    the last y position to fill (inclusive)
     */
    public static void fill(int[][] array2d, int value, int startX, int startY, int endX, int endY) {
        final int width = array2d.length;
        final int height = width == 0 ? 0 : array2d[0].length;
        for (int x = startX; x <= endX && x < width; x++) {
            for (int y = startY; y <= endY && y < height; y++) {
                array2d[x][y] = value;
            }
        }
    }

    /**
     * Fills a sub-section of {@code array2d} with {@code value}, with the section defined by start/end x/y.
     * Not to be confused with {@link #fill(long, int, int)}, which makes a new 2D array.
     *
     * @param array2d a 2D array that will be modified in-place
     * @param value   the value to fill all of array2D with
     * @param startX  the first x position to fill (inclusive)
     * @param startY  the first y position to fill (inclusive)
     * @param endX    the last x position to fill (inclusive)
     * @param endY    the last y position to fill (inclusive)
     */
    public static void fill(long[][] array2d, long value, int startX, int startY, int endX, int endY) {
        final int width = array2d.length;
        final int height = width == 0 ? 0 : array2d[0].length;
        for (int x = startX; x <= endX && x < width; x++) {
            for (int y = startY; y <= endY && y < height; y++) {
                array2d[x][y] = value;
            }
        }
    }

    /**
     * Fills a sub-section of {@code array2d} with identical references to {@code value} (not copies), with the section
     * defined by start/end x/y.
     *
     * @param array2d a 2D array that will be modified in-place
     * @param value   the value to fill all of array2D with
     * @param startX  the first x position to fill (inclusive)
     * @param startY  the first y position to fill (inclusive)
     * @param endX    the last x position to fill (inclusive)
     * @param endY    the last y position to fill (inclusive)
     */
    public static <T> void fill(T[][] array2d, T value, int startX, int startY, int endX, int endY) {
        final int width = array2d.length;
        final int height = width == 0 ? 0 : array2d[0].length;
        for (int x = startX; x <= endX && x < width; x++) {
            for (int y = startY; y <= endY && y < height; y++) {
                array2d[x][y] = value;
            }
        }
    }

    /**
     * Reverses the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be reversed in-place
     * @return the array passed in, after reversal
     */
    public static long[] reverse(long[] data) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        long t;
        for (int i = 0, j = sz - 1; i < j; i++, j--) {
            t = data[j];
            data[j] = data[i];
            data[i] = t;
        }
        return data;
    }

    /**
     * Reverses the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be reversed in-place
     * @return the array passed in, after reversal
     */
    public static boolean[] reverse(boolean[] data) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        boolean t;
        for (int i = 0, j = sz - 1; i < j; i++, j--) {
            t = data[j];
            data[j] = data[i];
            data[i] = t;
        }
        return data;
    }

    /**
     * Reverses the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be reversed in-place
     * @return the array passed in, after reversal
     */
    public static char[] reverse(char[] data) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        char t;
        for (int i = 0, j = sz - 1; i < j; i++, j--) {
            t = data[j];
            data[j] = data[i];
            data[i] = t;
        }
        return data;
    }

    /**
     * Reverses the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be reversed in-place
     * @return the array passed in, after reversal
     */
    public static float[] reverse(float[] data) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        float t;
        for (int i = 0, j = sz - 1; i < j; i++, j--) {
            t = data[j];
            data[j] = data[i];
            data[i] = t;
        }
        return data;
    }

    /**
     * Reverses the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be reversed in-place
     * @return the array passed in, after reversal
     */
    public static double[] reverse(double[] data) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        double t;
        for (int i = 0, j = sz - 1; i < j; i++, j--) {
            t = data[j];
            data[j] = data[i];
            data[i] = t;
        }
        return data;
    }

    /**
     * Reverses the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be reversed in-place
     * @return the array passed in, after reversal
     */
    public static int[] reverse(int[] data) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        int t;
        for (int i = 0, j = sz - 1; i < j; i++, j--) {
            t = data[j];
            data[j] = data[i];
            data[i] = t;
        }
        return data;
    }

    /**
     * Reverses the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be reversed in-place
     * @return the array passed in, after reversal
     */
    public static byte[] reverse(byte[] data) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        byte t;
        for (int i = 0, j = sz - 1; i < j; i++, j--) {
            t = data[j];
            data[j] = data[i];
            data[i] = t;
        }
        return data;
    }

    /**
     * Reverses the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be reversed in-place
     * @return the array passed in, after reversal
     */
    public static <T> T[] reverse(T[] data) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        T t;
        for (int i = 0, j = sz - 1; i < j; i++, j--) {
            t = data[j];
            data[j] = data[i];
            data[i] = t;
        }
        return data;
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @return the array passed in, after shuffling
     */
    public static long[] shuffle(long[] data) {
        return shuffle(data, null);
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @param seed a seed for randomness; can be made null for unseeded
     * @return the array passed in, after shuffling
     */
    public static long[] shuffle(long[] data, Long seed) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        Random rand = seed == null ? new Random() : new Random(seed);
        long t;
	    for (int i = sz - 1; i > 0; i--) {
	        int j = rand.nextInt(i + 1);
	        t = data[i];
	        data[i] = data[j];
	        data[j] = t;
	    }
	    return data;
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @return the array passed in, after shuffling
     */
    public static boolean[] shuffle(boolean[] data) {
        return shuffle(data, null);
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @param seed a seed for randomness; can be made null for unseeded
     * @return the array passed in, after shuffling
     */
    public static boolean[] shuffle(boolean[] data, Long seed) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        Random rand = seed == null ? new Random() : new Random(seed);
        boolean t;
	    for (int i = sz - 1; i > 0; i--) {
	        int j = rand.nextInt(i + 1);
	        t = data[i];
	        data[i] = data[j];
	        data[j] = t;
	    }
	    return data;
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @return the array passed in, after shuffling
     */
    public static char[] shuffle(char[] data) {
        return shuffle(data, null);
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @param seed a seed for randomness; can be made null for unseeded
     * @return the array passed in, after shuffling
     */
    public static char[] shuffle(char[] data, Long seed) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        Random rand = seed == null ? new Random() : new Random(seed);
        char t;
	    for (int i = sz - 1; i > 0; i--) {
	        int j = rand.nextInt(i + 1);
	        t = data[i];
	        data[i] = data[j];
	        data[j] = t;
	    }
	    return data;
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @return the array passed in, after shuffling
     */
    public static float[] shuffle(float[] data) {
        return shuffle(data, null);
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @param seed a seed for randomness; can be made null for unseeded
     * @return the array passed in, after shuffling
     */
    public static float[] shuffle(float[] data, Long seed) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        Random rand = seed == null ? new Random() : new Random(seed);
        float t;
	    for (int i = sz - 1; i > 0; i--) {
	        int j = rand.nextInt(i + 1);
	        t = data[i];
	        data[i] = data[j];
	        data[j] = t;
	    }
	    return data;
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @return the array passed in, after shuffling
     */
    public static double[] shuffle(double[] data) {
        return shuffle(data, null);
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @param seed a seed for randomness; can be made null for unseeded
     * @return the array passed in, after shuffling
     */
    public static double[] shuffle(double[] data, Long seed) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        Random rand = seed == null ? new Random() : new Random(seed);
        double t;
	    for (int i = sz - 1; i > 0; i--) {
	        int j = rand.nextInt(i + 1);
	        t = data[i];
	        data[i] = data[j];
	        data[j] = t;
	    }
	    return data;
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @return the array passed in, after shuffling
     */
    public static int[] shuffle(int[] data) {
        return shuffle(data, null);
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @param seed a seed for randomness; can be made null for unseeded
     * @return the array passed in, after shuffling
     */
    public static int[] shuffle(int[] data, Long seed) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        Random rand = seed == null ? new Random() : new Random(seed);
        int t;
	    for (int i = sz - 1; i > 0; i--) {
	        int j = rand.nextInt(i + 1);
	        t = data[i];
	        data[i] = data[j];
	        data[j] = t;
	    }
	    return data;
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @return the array passed in, after shuffling
     */
    public static short[] shuffle(short[] data) {
        return shuffle(data, null);
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @param seed a seed for randomness; can be made null for unseeded
     * @return the array passed in, after shuffling
     */
    public static short[] shuffle(short[] data, Long seed) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        Random rand = seed == null ? new Random() : new Random(seed);
        short t;
	    for (int i = sz - 1; i > 0; i--) {
	        int j = rand.nextInt(i + 1);
	        t = data[i];
	        data[i] = data[j];
	        data[j] = t;
	    }
	    return data;
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @return the array passed in, after shuffling
     */
    public static byte[] shuffle(byte[] data) {
        return shuffle(data, null);
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @param seed a seed for randomness; can be made null for unseeded
     * @return the array passed in, after shuffling
     */
    public static byte[] shuffle(byte[] data, Long seed) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        Random rand = seed == null ? new Random() : new Random(seed);
        byte t;
	    for (int i = sz - 1; i > 0; i--) {
	        int j = rand.nextInt(i + 1);
	        t = data[i];
	        data[i] = data[j];
	        data[j] = t;
	    }
	    return data;
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @return the array passed in, after shuffling
     */
    public static <T> T[] shuffle(T[] data) {
        return shuffle(data, null);
    }
    
    /**
     * Shuffles the array given as a parameter, in-place, and returns the modified original.
     *
     * @param data an array that will be shuffled in-place
     * @param seed a seed for randomness; can be made null for unseeded
     * @return the array passed in, after shuffling
     */
    public static <T> T[] shuffle(T[] data, Long seed) {
        int sz;
        if (data == null || (sz = data.length) <= 0) return data;
        Random rand = seed == null ? new Random() : new Random(seed);
        T t;
	    for (int i = sz - 1; i > 0; i--) {
	        int j = rand.nextInt(i + 1);
	        t = data[i];
	        data[i] = data[j];
	        data[j] = t;
	    }
	    return data;
    }
}
