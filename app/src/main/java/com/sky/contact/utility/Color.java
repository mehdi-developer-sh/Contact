package com.sky.contact.utility;

public class Color extends android.graphics.Color {
    public static int getRandomColor(int min, int max) {
        final int diff = max - min;

        int r = (int) (Math.random() * diff) + min;
        int g = (int) (Math.random() * diff) + min;
        int b = (int) (Math.random() * diff) + min;

        return android.graphics.Color.rgb(r, g, b);
    }
}
