package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.izabarovsky.callsign.telegram.bot.DataHelper.getExistsCallSign;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SearchTest {

    @Autowired
    private CallSignRepository repository;

    @Test
    void searchByK2CallSign() {
        var expected = getExistsCallSign(repository).getK2CallSign();
        var caseIsSame = repository.findByK2CallSign(expected)
                .orElseThrow(() -> new AssertionFailedError("Search failed"));
        assertEquals(expected, caseIsSame.getK2CallSign());
        var caseIsLower = repository.findByK2CallSign(expected.toLowerCase())
                .orElseThrow(() -> new AssertionFailedError("Search by lowercase is failed"));
        var caseIsUpper = repository.findByK2CallSign(expected.toUpperCase())
                .orElseThrow(() -> new AssertionFailedError("Search by uppercase is failed"));
        assertAll(
                () -> assertEquals(expected, caseIsLower.getK2CallSign(), "Lower case"),
                () -> assertEquals(expected, caseIsUpper.getK2CallSign(), "Upper case")
        );
    }

    @Test
    void searchByOfficialCallSign() {
        var expected = getExistsCallSign(repository).getOfficialCallSign();
        var caseIsSame = repository.findByOfficialCallSign(expected)
                .orElseThrow(() -> new AssertionFailedError("Search failed"));
        assertEquals(expected, caseIsSame.getOfficialCallSign());
        var caseIsLower = repository.findByOfficialCallSign(expected.toLowerCase())
                .orElseThrow(() -> new AssertionFailedError("Search by lowercase is failed"));
        var caseIsUpper = repository.findByOfficialCallSign(expected.toUpperCase())
                .orElseThrow(() -> new AssertionFailedError("Search by uppercase is failed"));
        assertAll(
                () -> assertEquals(expected, caseIsLower.getOfficialCallSign(), "Lower case"),
                () -> assertEquals(expected, caseIsUpper.getOfficialCallSign(), "Upper case")
        );
    }

}
