package com.andrejanesic.cads.homework1.utils;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Creates a file that mocks a corpus of text to index.
 */
public class MockTextCorpus {

    /**
     * The frequency of each keyword in this text.
     */
    @Getter
    @NonNull
    private final Map<String, Integer> frequency = new HashMap<>();

    /**
     * Path to the file of the corpus.
     */
    @Getter
    @NonNull
    private final String path;

    /**
     * Delimiter between keywords in the text.
     */
    @Getter
    @NonNull
    private final String delimiter;

    /**
     * Special keywords to use when generating the text. Keywords from the app configuration should be passed here.
     */
    @Getter
    @NonNull
    private final String[] targetKeywords;

    /**
     * Maximum length of the text in bytes.
     */
    @Getter
    @NonNull
    private final int maxLength;

    /**
     * Creates a file that mocks a corpus of text to index.
     *
     * @param path           Path to the file of the corpus.
     * @param delimiter      Delimiter between keywords in the text.
     * @param targetKeywords Special keywords to use when generating the text. Keywords from the app configuration should be passed here.
     * @param maxLength      Maximum length of the text in bytes.
     */
    public MockTextCorpus(String path, String delimiter, String[] targetKeywords, int maxLength) {
        this.path = path;
        this.delimiter = delimiter;
        this.targetKeywords = targetKeywords;
        this.maxLength = maxLength;
    }

    /**
     * Creates a file that mocks a corpus of text to index.
     *
     * @param builder {@link Builder} object.
     */
    public MockTextCorpus(Builder builder) {
        this(builder.path, builder.delimiter, builder.targetKeywords, builder.maxLength);
    }

    /**
     * Creates a file that mocks a corpus of text to index.
     *
     * @param path Path to the file of the corpus.
     */
    public MockTextCorpus(String path) {
        this(path, " ", new String[]{}, 1024);
    }

    /**
     * Generates a random keyword to insert into the text.
     *
     * @return Random keyword.
     */
    private static String getRandomKeyword(int length) {
        int leftLimit = 33; // '!'
        int rightLimit = 122; // 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Generates and writes the text corpus to file.
     */
    public void generate() throws IOException {
        int l = 0;
        int targetLength = (int) (Math.random() * maxLength + 1);
        boolean prevDelimiter = false;
        String targetKeyword = null;

        StringBuilder sb = new StringBuilder();
        while (true) {
            int r = (int) (Math.random() * 100);
            String kw;

            if (r < 33 && targetKeywords.length > 0) {
                kw = targetKeywords[(int) (Math.random() * targetKeywords.length)];
                if (r > 16) {
                    if (prevDelimiter) {
                        targetKeyword = new String(kw);
                    } else {
                        targetKeyword = null;
                    }
                    kw += delimiter;
                    prevDelimiter = true;
                } else {
                    targetKeyword = null;
                    prevDelimiter = false;
                }
            } else {
                kw = getRandomKeyword((int) (Math.random() * 50 + 1));
                targetKeyword = null;
                if (r > 67) {
                    kw += delimiter;
                    prevDelimiter = true;
                } else {
                    prevDelimiter = false;
                }
            }

            if (l + kw.length() > targetLength) break;
            sb.append(kw);
            l += kw.length();
            if (targetKeyword != null) {
                frequency.put(targetKeyword, frequency.getOrDefault(targetKeyword, 0) + 1);
            }
        }

        String content = sb.toString();
        Path p = Paths.get(path);
        Files.write(p, content.getBytes());
        assertEquals(content, String.join("", Files.readAllLines(p)));
    }

    /**
     * Deletes the generated file.
     */
    public void remove() throws IOException {
        Files.delete(Paths.get(path));
    }

    @Data
    public static class Builder {

        /**
         * Path to the file of the corpus.
         */
        @NonNull
        private String path;

        /**
         * Delimiter between keywords in the text.
         */
        @NonNull
        private String delimiter;

        /**
         * Special keywords to use when generating the text. Keywords from the app configuration should be passed here.
         */
        @NonNull
        private String[] targetKeywords;

        /**
         * Maximum length of the text in bytes.
         */
        @NonNull
        private int maxLength;
    }
}
