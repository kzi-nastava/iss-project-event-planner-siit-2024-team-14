package edu.ftn.iss.eventplanner.e2e.util;

import jakarta.annotation.Nullable;

public abstract class Util {

    @Nullable
    public static Double parseCurrency(String text) {
        try {
            return Double.parseDouble(text.replaceAll("[^\\d.-]", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
