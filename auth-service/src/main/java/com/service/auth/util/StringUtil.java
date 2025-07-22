package com.service.auth.util;

import java.util.regex.Pattern;

public class StringUtil {
	private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^01[016789]-?\\d{3,4}-?\\d{4}$"
    );

    /** 문자열이 null 또는 공백인지 확인 */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /** 문자열이 null 또는 공백이 아닌지 확인 */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /** 문자열이 이메일 형식인지 확인 */
    public static boolean isEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /** 문자열이 휴대폰 번호 형식인지 확인 */
    public static boolean isPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    /** null일 경우 기본값 반환 */
    public static String defaultIfNull(String str, String defaultStr) {
        return (str == null) ? defaultStr : str;
    }

    /** 길이 자르기 */
    public static String truncate(String str, int length) {
        if (isEmpty(str)) return str;
        return str.length() <= length ? str : str.substring(0, length);
    }
}
