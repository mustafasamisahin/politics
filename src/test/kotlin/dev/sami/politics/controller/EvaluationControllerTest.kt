package dev.sami.politics.controller

import dev.sami.politics.model.Response
import dev.sami.politics.service.EvaluationService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.FileNotFoundException

class EvaluationControllerTest {
    private val evaluationService: EvaluationService = mock(EvaluationService::class.java)
    private val evaluationController = EvaluationController(evaluationService)

    @Test
    fun evaluateSpeechesTest() {
        val urlNo = "url1"
        val url = "www.google.com"
        val input = (urlNo to url)

        val response = Response(urlNo, null, "Alexander Abel", "Caesare Collins")
        val expectedResponse = ResponseEntity.ok().body(listOf(response))

        Mockito.`when`(evaluationService.evaluateSpeech(input)).thenReturn(response)

        val actualResponse = evaluationController.evaluateSpeeches(mapOf(input))

        assert(actualResponse == expectedResponse)
    }

    @Test
    fun evaluateSpeechesWithMultiUrlTest() {
        val input = mapOf("url1" to "url1", "url2" to "url2")
        val responseList = listOf(
            Response("url1", null, "Alexander Abel", "Caesare Collins"),
            Response("url2", null, "Alexander Abel", "Caesare Collins")
        )
        val expectedResponse = ResponseEntity.ok().body(responseList)

        Mockito.`when`(evaluationService.evaluateSpeech(input.entries.first().toPair()))
            .thenReturn(responseList.first())
        Mockito.`when`(evaluationService.evaluateSpeech(input.entries.last().toPair())).thenReturn(responseList.last())

        val response = evaluationController.evaluateSpeeches(input)

        assert(response == expectedResponse)
    }

    @Test
    fun evaluateSpeechesWithWrongUrl() {
        val input = "url1" to "no-found.csv"
        val expectedResponse = ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Couldn't fetch from url1: src\\main\\resources\\no-found.csv (The system cannot find the file specified)")

        Mockito.`when`(evaluationService.evaluateSpeech(input))
            .thenAnswer { throw FileNotFoundException("src\\main\\resources\\no-found.csv (The system cannot find the file specified)") }

        val response = evaluationController.evaluateSpeeches(mapOf(input))

        assert(response == expectedResponse)
    }
}