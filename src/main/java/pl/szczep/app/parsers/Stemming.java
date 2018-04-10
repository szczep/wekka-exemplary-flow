package pl.szczep.app.parsers;

import java.util.HashMap;
import java.util.Map;

import morfologik.stemming.polish.PolishStemmer;


public final class Stemming {

    private static final PolishStemmer POLISH_STEMMER = new PolishStemmer();

    private static Map<String, String> stemCache = new HashMap<>();

    public static String stem(String word) {

        return stemCache.computeIfAbsent(word, keyWord ->
            POLISH_STEMMER
                .lookup(keyWord)
                .stream()
                .findFirst()
                .map(stem -> stem.getStem().toString())
                .orElse(keyWord)
        );
    }
}
