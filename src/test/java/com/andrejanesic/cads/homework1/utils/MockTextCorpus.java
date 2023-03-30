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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Creates a file that mocks a corpus of text to index.
 */
public class MockTextCorpus {

    private static final String[] WORDLIST = new String[]{
            "the",
            "be",
            "of",
            "and",
            "a",
            "to",
            "in",
            "he",
            "have",
            "it",
            "that",
            "for",
            "they",
            "I",
            "with",
            "as",
            "not",
            "on",
            "she",
            "at",
            "by",
            "this",
            "we",
            "you",
            "do",
            "but",
            "from",
            "or",
            "which",
            "one",
            "would",
            "all",
            "will",
            "there",
            "say",
            "who",
            "make",
            "when",
            "can",
            "more",
            "if",
            "no",
            "man",
            "out",
            "other",
            "so",
            "what",
            "time",
            "up",
            "go",
            "about",
            "than",
            "into",
            "could",
            "state",
            "only",
            "new",
            "year",
            "some",
            "take",
            "come",
            "these",
            "know",
            "see",
            "use",
            "get",
            "like",
            "then",
            "first",
            "any",
            "work",
            "now",
            "may",
            "such",
            "give",
            "over",
            "think",
            "most",
            "even",
            "find",
            "day",
            "also",
            "after",
            "way",
            "many",
            "must",
            "look",
            "before",
            "great",
            "back",
            "through",
            "long",
            "where",
            "much",
            "should",
            "well",
            "people",
            "down",
            "own",
            "just",
            "because",
            "good",
            "each",
            "those",
            "feel",
            "seem",
            "how",
            "high",
            "too",
            "place",
            "little",
            "world",
            "very",
            "still",
            "nation",
            "hand",
            "old",
            "life",
            "tell",
            "write",
            "become",
            "here",
            "show",
            "house",
            "both",
            "between",
            "need",
            "mean",
            "call",
            "develop",
            "under",
            "last",
            "right",
            "move",
            "thing",
            "general",
            "school",
            "never",
            "same",
            "another",
            "begin",
            "while",
            "number",
            "part",
            "turn",
            "real",
            "leave",
            "might",
            "want",
            "point",
            "form",
            "off",
            "child",
            "few",
            "small",
            "since",
            "against",
            "ask",
            "late",
            "home",
            "interest",
            "large",
            "person",
            "end",
            "open",
            "public",
            "follow",
            "during",
            "present",
            "without",
            "again",
            "hold",
            "govern",
            "around",
            "possible",
            "head",
            "consider",
            "word",
            "program",
            "problem",
            "however",
            "lead",
            "system",
            "set",
            "order",
            "eye",
            "plan",
            "run",
            "keep",
            "face",
            "fact",
            "group",
            "play",
            "stand",
            "increase",
            "early",
            "course",
            "change",
            "help",
            "line",
    };
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
    private final int minLength;
    /**
     * Maximum length of the text in bytes.
     */
    @Getter
    private final int maxLength;

    /**
     * Creates a file that mocks a corpus of text to index.
     *
     * @param path           Path to the file of the corpus.
     * @param delimiter      Delimiter between keywords in the text.
     * @param targetKeywords Special keywords to use when generating the text. Keywords from the app configuration should be passed here.
     * @param minLength      Minimum length of the text in bytes.
     * @param maxLength      Maximum length of the text in bytes.
     */
    public MockTextCorpus(String path, String delimiter, String[] targetKeywords, int minLength, int maxLength) {
        this.path = path;
        this.delimiter = delimiter;
        this.targetKeywords = targetKeywords;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /**
     * Creates a file that mocks a corpus of text to index.
     *
     * @param builder {@link Builder} object.
     */
    public MockTextCorpus(Builder builder) {
        this(builder.path, builder.delimiter, builder.targetKeywords, builder.minLength, builder.maxLength);
    }

    /**
     * Creates a file that mocks a corpus of text to index.
     *
     * @param path Path to the file of the corpus.
     */
    public MockTextCorpus(String path) {
        this(path, " ", new String[]{}, 0, 2048);
    }

    /**
     * Generates a random keyword to insert into the text.
     *
     * @return Random keyword.
     */
    private static String getRandomKeyword() {
        int index = (int) (Math.random() * WORDLIST.length);
        return WORDLIST[index];
    }

    /**
     * Generates and writes the text corpus to file.
     */
    public void generate() throws IOException {
        int targetLength = (int) (Math.random() * (maxLength - minLength) + minLength);
        boolean targetKeyword;

        String content = "";
        String temp;
        StringBuilder pot = new StringBuilder();
        while (true) {
            int r = (int) (Math.random() * 100);
            String kw;

            if (r < 33 && targetKeywords.length > 0) {
                kw = targetKeywords[(int) (Math.random() * targetKeywords.length)];
                targetKeyword = true;
            } else {
                kw = getRandomKeyword();
                targetKeyword = false;
            }

            pot.append(kw);
            pot.append(delimiter);
            temp = pot.toString();
            if (temp.getBytes().length > targetLength) break;
            content = temp;
            if (targetKeyword) {
                frequency.put(kw, frequency.getOrDefault(kw, 0) + 1);
                targetKeyword = false;
            }

            if (r < 33) {
                pot.append(System.lineSeparator());
                temp = pot.toString();
                if (temp.getBytes().length > targetLength) break;
                content = temp;
            }
        }

        content = content.trim();
        Path p = Paths.get(path);
        Files.write(p, content.getBytes());
        assertEquals(content, String.join(System.lineSeparator(), Files.readAllLines(p)));
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
         * Minimum length of the text in bytes.
         */
        @NonNull
        private int minLength;

        /**
         * Maximum length of the text in bytes.
         */
        @NonNull
        private int maxLength;
    }
}
