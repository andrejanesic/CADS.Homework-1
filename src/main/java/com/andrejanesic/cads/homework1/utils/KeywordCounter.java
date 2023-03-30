package com.andrejanesic.cads.homework1.utils;

import com.andrejanesic.cads.homework1.core.exceptions.UnexpectedRuntimeComponentException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class KeywordCounter {

    /**
     * Counts the frequency of each keyword in the text.
     *
     * @param text      The target text.
     * @param delimiter Delimiter used to split the text into tokens (regex).
     * @return {@link Map} of strings and their frequencies in the text.
     */
    public static Map<String, Integer> getFrequency(String text, String delimiter) {
        if (delimiter == null || delimiter.length() == 0)
            throw new UnexpectedRuntimeComponentException("KeywordCounter: delimiter shouldn't be null or 0");
        Map<String, Integer> frequency = new HashMap<>();
        Scanner sc = new Scanner(text).useDelimiter(delimiter);
        while (sc.hasNext()) {
            String tok = sc.next();
            frequency.put(tok, frequency.getOrDefault(tok, 0) + 1);
        }
        return frequency;
    }
}
