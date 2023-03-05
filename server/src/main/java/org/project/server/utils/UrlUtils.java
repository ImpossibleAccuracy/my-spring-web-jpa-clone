package org.project.server.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {
    private static final Pattern pathPattern = Pattern.compile("\\{(.*)?}");

    /**
     * Retrieve path params by regex {@link #pathPattern}
     *
     * @param sample Route path with placeholders
     * @param source Data path with data
     * @return Map of path params
     */
    public static Map<String, String> retrievePathVariables(@NotNull String sample, @NotNull String source) {
        List<String> sampleParts = getUrlParts(sample);
        List<String> sourceParts = getUrlParts(source);

        Map<String, String> result = new HashMap<>();

        int size = sampleParts.size();

        for (int i = 0; i < size; i++) {
            String samplePart = sampleParts.get(i);
            String sourcePart = sourceParts.get(i);

            Matcher matcher = pathPattern.matcher(samplePart);
            if (matcher.matches()) {
                result.put(samplePart.substring(1, samplePart.length() - 1), sourcePart);
            }
        }

        return result;
    }

    /**
     * Tests two routes for equality, ignoring path parameters
     *
     * @return URLs equality
     */
    public static boolean isUrlSameIgnorePathParams(@NotNull String url1, @NotNull String url2) {
        List<String> url1Parts = getUrlParts(url1);
        List<String> url2Parts = getUrlParts(url2);

        if (url1Parts.size() != url2Parts.size()) return false;

        int size = url1Parts.size();

        for (int i = 0; i < size; i++) {
            String url1Part = url1Parts.get(i);
            String url2Part = url2Parts.get(i);

            Matcher matcher = pathPattern.matcher(url1Part);
            if (!matcher.matches() && !url1Part.equals(url2Part)) return false;
        }

        return true;
    }

    public static boolean isUrlPathValid(@NotNull String url) {
        return !url.contains("//");
    }

    public static List<String> getUrlParts(@NotNull String url) {
        return Arrays.stream(url.split("/")).filter(s -> !s.isBlank()).toList();
    }
}
