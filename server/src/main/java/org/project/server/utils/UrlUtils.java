package org.project.server.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {
    private static final Pattern pathPattern = Pattern.compile("\\{(.*)?}");

    public static Map<String, String> getPathVariables(String url1, String url2) {
        List<String> url1Parts = getUrlParts(url1);
        List<String> url2Parts = getUrlParts(url2);

        Map<String, String> result = new HashMap<>();

        int size = url1Parts.size();

        for (int i = 0; i < size; i++) {
            String url1Part = url1Parts.get(i);
            String url2Part = url2Parts.get(i);

            Matcher matcher = pathPattern.matcher(url1Part);
            if (matcher.matches()) {
                result.put(url1Part.substring(1, url1Part.length() - 1), url2Part);
            }
        }

        return result;
    }

    public static boolean isUrlSameIgnorePathParams(String url1, String url2) {
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

    public static boolean isUrlValid(String url) {
        return !url.contains("//");
    }

    private static List<String> getUrlParts(String url) {
        return Arrays.stream(url.split("/")).filter(s -> !s.isBlank()).toList();
    }
}
