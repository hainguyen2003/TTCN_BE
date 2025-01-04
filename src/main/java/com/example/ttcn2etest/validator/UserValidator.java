package com.example.ttcn2etest.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private final static String NAME_REGEX = "^[^<>{}\\\\\\\"|;:.,~!?@#$%^=&*\\\\\\\\)(]*$";

    public static boolean validateName(String name) {
        if (!name.matches(NAME_REGEX)) {
            return false;
        }
        return true;
    }

    public static boolean validatePhone(String phone) {
        // Loại bỏ các dấu cách, gạch ngang và dấu cộng nếu có
        phone = phone.replaceAll("\\s+|-|\\+", "");

        // Kiểm tra số điện thoại có độ dài từ 10 đến 15 chữ số
        if (phone.length() < 10 || phone.length() > 15) {
            return false;
        }

        // Kiểm tra số điện thoại bắt đầu bằng +84 hoặc 84
        if (phone.startsWith("84")) {
            phone = phone.substring(2);
        } else if (phone.startsWith("+84")) {
            phone = phone.substring(3);
        }

        // Kiểm tra số điện thoại có đúng đầu số 03, 05, 07, 08, 09 không
        return phone.matches("03[2-9]\\d{7}|05[6-9]\\d{7}|07[0-9]\\d{7}|08[1-9]\\d{7}|09\\d{8}");
    }

    public static boolean validateAddress(String address) {
        return address.matches("^[\\p{L}[0-9] ,.-]+$");
    }

    public static boolean validateUsername(String username) {
        // Kiểm tra độ dài tối thiểu là 5 ký tự
        if (username.length() < 5) {
            return false;
        }

        // Kiểm tra username không chứa khoảng trắng
        if (username.contains(" ")) {
            return false;
        }

        // Kiểm tra username chỉ chứa các ký tự chữ cái (viết hoa hoặc thường),
        // số và dấu chấm, và không chứa các thuật ngữ chung hoặc phần mở rộng
        return username.matches("^[a-zA-Z0-9.]*$") && !username.contains(".com") && !username.contains(".net");
    }

    public static boolean validateBod(Date bod) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        simpleDateFormat.setLenient(false);
//        try {
//            Date date = simpleDateFormat.parse(bod);
//            return true;
//        } catch (ParseException e) {
//            return false;
//        }
        return bod != null;
    }

    public static boolean isVerifiedValid(Boolean value) {
        return value != null && (value.equals(Boolean.TRUE) || value.equals(Boolean.FALSE));
    }


    public static boolean isValidEmail(String email) {
        // Kiểm tra độ dài tổng cộng
        if (email.length() > 320) {
            return false;
        }

        // Kiểm tra ký tự @ duy nhất
        int atCount = (int) email.chars().filter(ch -> ch == '@').count();
        if (atCount != 1) {
            return false;
        }

        // Kiểm tra ký tự không được xuất hiện ở đầu hoặc cuối
        if (email.startsWith(".") || email.endsWith(".")) {
            return false;
        }

        // Kiểm tra không có hai hoặc nhiều khoảng trắng liên tiếp
        if (email.contains("..") || email.contains(".@") || email.contains("@.")) {
            return false;
        }

        // Kiểm tra độ dài tên miền không vượt quá 254 ký tự
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        String domain = parts[1];
        if (domain.length() > 254) {
            return false;
        }

        // Kiểm tra định dạng email bằng regex
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
