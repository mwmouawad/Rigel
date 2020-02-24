package ch.epfl.test;// Rigel stage 1


import java.util.SplittableRandom;

public final class TestRandomizer {
    // Fix random seed to guarantee reproducibility.
    public final static long SEED = 2020;

    public final static int RANDOM_ITERATIONS = 1_000;

    public static SplittableRandom newRandom() {
        return new SplittableRandom(SEED);
    }
}
