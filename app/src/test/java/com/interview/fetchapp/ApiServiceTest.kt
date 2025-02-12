package com.interview.fetchapp


import com.interview.fetchapp.network.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.*
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }

    @Test
    fun `fetchListItems should return expected data`() {
        val mockResponse = """
            [
                {"id": 1, "listId": 1, "name": "Item A"},
                {"id": 2, "listId": 2, "name": "Item B"}
            ]
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        runBlocking {
            val response = apiService.getListItems()
            Assert.assertEquals(2, response.size)
            Assert.assertEquals("Item A", response[0].name)
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}
