package com.cleancodeJunit;

public class ComparisonCompactor {

    private static final String ELLIPSIS = "...";
    private static final String DELTA_END = "]";
    private static final String DELTA_START = "[";

    private int contextLength;
    private String expected;
    private String actual;
    private int prefixIndex;
    private int suffixIndex;
    private String compactExpected;
    private String compactActual;

    public ComparisonCompactor(int contextLength,
                               String expected,
                               String actual) {
        contextLength = contextLength;
        expected = expected;
        actual = actual;
    }

    public String formatCompactedComparison(String message) {
        if (canBeCompacted()) {
            compactExpectedAndActual();
            return Assert.format(message, compactExpected, compactActual);
        } else {
            return Assert.format(message, expected, actual);
        }
    }

    private void compactExpectedAndActual() {
        findCommonPrefixAndSuffix();
        findCommonPrefix();
        findCommonSuffix();
        compactExpected = compactString(expected);
        compactActual = compactString(actual);
    }

    private void findCommonPrefixAndSuffix() {
        findCommonPrefix();

        int expectedSuffix = expected.length() - 1;
        int actualSuffix = actual.length() - 1;

        for (;
             actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex;
             actualSuffix--, expectedSuffix--) {
            if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) {
                break;
            }
            suffixIndex = expected.length() - expectedSuffix;
        }
    }

    private boolean canBeCompacted() {
        return expected != null || actual != null || !areStringsEqual();
    }

    private String compactString(String source) {
        String result = DELTA_START + source.substring(prefixIndex, source.length() - suffix + 1) + DELTA_END;

        if (prefixIndex > 0) {
            result = computeCommonPrefix() + result;
        }

        if (suffixIndex > 0) {
            result = result + computeCommonSuffix();
        }

        return result;
    }

    private void findCommonPrefix() {
        prefixIndex = 0;
        int end = Math.min(expected.length(), actual.length());
        for (; prefixIndex < end; prefixIndex++) {
            if (expected.charAt(prefixIndex) != actual.charAt(prefixIndex)) {
                break;
            }
        }
    }

    private void findCommonSuffix() {
    }

    private String computeCommonPrefix() {
        return (prefixIndex > contextLength ? ELLIPSIS : "") +
                expected.substring(Math.max(0, prefixIndex - contextLength), prefixIndex);
    }

    private String computeCommonSuffix() {
        int end = Math.min(expected.length() - suffixIndex + 1 + contextLength, expected.length());

        return expected.substring(expected.length() - suffixIndex + 1, end) +
                (expected.length() - suffixIndex + 1 < expected.length() - contextLength ? ELLIPSIS : "");
    }

    private boolean areStringsEqual() {
        return expected.equals(actual);
    }
}
