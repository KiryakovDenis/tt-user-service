package ru.kdv.study.taskTrackerUser.security;

import ru.kdv.study.taskTrackerUser.exception.BadRequestException;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtl {

    public static String makeHashPassword(String password) {
        try {
            return DatatypeConverter.printHexBinary(
                    MessageDigest.getInstance("MD5").digest(password.getBytes())
            ).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw BadRequestException.create("Ошибка алгоритма шифрования пароля");
        }
    }
}
