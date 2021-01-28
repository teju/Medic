package com.moguls.medic.etc;

public class MathUtils {
    public static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }
}