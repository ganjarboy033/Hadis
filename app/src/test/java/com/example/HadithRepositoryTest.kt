package com.example

import com.example.data.repository.HadithRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HadithRepositoryTest {

    @Test
    fun testAll42HadithsPresent() {
        val hadiths = HadithRepository.allHadithsList
        assertEquals(42, hadiths.size)

        // Verify each hadith has authentic arabic text, translation, word-by-word, and syarah
        hadiths.forEach { h ->
            assertTrue("Hadits #${h.number} Arabic text must not be empty", h.arabicText.isNotBlank())
            assertTrue("Hadits #${h.number} Indonesian translation must not be empty", h.translationId.isNotBlank())
            assertTrue("Hadits #${h.number} must contain word-by-word translations", h.words.isNotEmpty())
            assertTrue("Hadits #${h.number} must contain syarah explanation", h.syarahExplanation.isNotBlank())
            assertTrue("Hadits #${h.number} must contain rawi", h.rawi.isNotBlank())
        }
    }

    @Test
    fun testSpecificHadithsContent() {
        val hadiths = HadithRepository.allHadithsList
        val hadith1 = hadiths.find { it.number == 1 }
        assertNotNull(hadith1)
        assertTrue(hadith1!!.titleId.contains("Niat", ignoreCase = true))

        val hadith2 = hadiths.find { it.number == 2 }
        assertNotNull(hadith2)
        assertTrue(hadith2!!.titleId.contains("Jibril", ignoreCase = true) || hadith2.titleId.contains("Islam", ignoreCase = true))

        val hadith42 = hadiths.find { it.number == 42 }
        assertNotNull(hadith42)
        assertTrue(hadith42!!.arabicText.contains("ابْنَ آدَمَ"))
    }
}
