package dev.sami.politics.service

import dev.sami.politics.model.Response
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class EvaluationServiceTest {

    private val csvParseService = mock(CsvParseService::class.java)
    private val csvParseServiceTest = CsvParseServiceTest()
    private val evaluationService = EvaluationService(csvParseService)

    @Test
    fun evaluateSpeechTest() {
        val urlNo = "url1"
        val input = urlNo to "url"
        val expectedResponse = Response(urlNo, null, "Alexander Abel", "Caesare Collins")
        val speechList = csvParseServiceTest.generateSpeeches()

        Mockito.`when`(csvParseService.fetchAndParseCsv(input)).thenReturn(urlNo to speechList)

        val response = evaluationService.evaluateSpeech(input)

        assert(response == expectedResponse)
    }
}