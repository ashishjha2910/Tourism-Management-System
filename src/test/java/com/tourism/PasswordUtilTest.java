package com.tourism;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {
    @Test
    void hashesAndVerifiesPassword() {
        String hash = PasswordUtil.hash("Tourism@123");
        assertNotEquals("Tourism@123", hash);
        assertTrue(PasswordUtil.verify("Tourism@123", hash));
        assertFalse(PasswordUtil.verify("wrong-password", hash));
    }

    @Test
    void rejectsShortPassword() {
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hash("short"));
    }
}
