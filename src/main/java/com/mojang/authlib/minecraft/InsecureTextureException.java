package com.mojang.authlib.minecraft;

import com.mojang.authlib.GameProfile;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class InsecureTextureException extends RuntimeException {

    public InsecureTextureException(String message) {
        super(message);
    }

    public static class OutdatedTextureException extends InsecureTextureException {

        private final Date validFrom;

        private final Calendar limit;

        public OutdatedTextureException(Date validFrom, Calendar limit) {
            super("OutdatedTextureException");
            this.validFrom = validFrom;
            this.limit = limit;
        }
    }

    public static class WrongTextureOwnerException extends InsecureTextureException {

        private final GameProfile expected;

        private final UUID resultId;

        private final String resultName;

        public WrongTextureOwnerException(GameProfile expected, UUID resultId, String resultName) {
            super("WrongTextureOwnerException");
            this.expected = expected;
            this.resultId = resultId;
            this.resultName = resultName;
        }
    }

    public static class MissingTextureException extends InsecureTextureException {

        public MissingTextureException() {
            super("MissingTextureException");
        }
    }
}
