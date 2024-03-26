package vn.edu.tdtu.musicapplication.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class VietnameseStringUtils {
    public static String toSlug(String input) {
        if (input == null) {
            return null;
        }

        String result = input.toLowerCase(Locale.ROOT);

        result = Normalizer.normalize(result, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        result = pattern.matcher(result).replaceAll("");

        result = result.replaceAll("[^a-z0-9\\s]", "");
        result = result.replaceAll("\\s+", "-");

        return result;
    }
}
