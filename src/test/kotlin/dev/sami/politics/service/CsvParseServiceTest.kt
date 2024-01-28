package dev.sami.politics.service

import dev.sami.politics.model.PoliticalSpeech
import org.junit.jupiter.api.Test

class CsvParseServiceTest {
    private val csvParseService = CsvParseService()

    @Test
    fun validateAndExtractDataTest() {
        val urlKey = "url1"
        val url = "political-speeches.csv"
        val input = urlKey to url

        val speechList = generateSpeeches()

        val expectedResponse = urlKey to speechList
        val actualResponse = csvParseService.fetchAndParseCsv(input)

        assert(actualResponse == expectedResponse)
    }

    fun generateSpeeches(): List<PoliticalSpeech> {
        val speech1 = PoliticalSpeech("Alexander Abel", "education policty", "2012-10-30", 5310)
        val speech2 = PoliticalSpeech("Bernhard Belling", "coal subsidies", "2012-11-05", 1210)
        val speech3 = PoliticalSpeech("Caesare Collins", "coal subsidies", "2012-11-06", 1119)
        val speech4 = PoliticalSpeech("Alexander Abel", "homeland security", "2012-12-11", 911)
        return listOf(speech1, speech2, speech3, speech4)
    }
}