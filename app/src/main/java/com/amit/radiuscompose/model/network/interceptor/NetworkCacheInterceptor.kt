package com.amit.radiuscompose.model.network.interceptor

import android.content.Context
import com.amit.radiuscompose.model.network.CustomHeaders
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.io.File

private const val DEFAULT_CACHE_TTL: Long = 1000 * 60 * 60

class NetworkCacheInterceptor(private val context: Context) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cacheKey = request.header(CustomHeaders.CACHE_KEY) ?: return chain.proceed(request)
        val cacheTTLString = request.header(CustomHeaders.CACHE_TTL)
        val cacheTTL: Long = if (cacheTTLString.isNullOrEmpty()) {
            DEFAULT_CACHE_TTL
        } else {
            cacheTTLString.toLongOrNull().let { cacheTTlParsed ->
                if (cacheTTlParsed == null || cacheTTlParsed < 0) {
                    DEFAULT_CACHE_TTL
                } else {
                    cacheTTlParsed
                }
            }
        }
        val cacheFile = File(context.cacheDir, cacheKey)
        return if (cacheFile.exists() && cacheFile.lastModified() > System.currentTimeMillis() - cacheTTL) {
            buildResponseFromFile(cacheFile)
        } else {
            val response = chain.proceed(request)
            writeToCache(cacheFile, response)
            response
        }
    }

    private fun buildResponseFromFile(file: File): Response {
        assert(file.exists()) {
            Timber.e("Cache file does not exist but response requested from cache file")
        }
        val fileResponse = file.readBytes()
        return Response.Builder()
            .code(200)
            .body(fileResponse.toResponseBody())
            .message("success")
            .build()
    }

    private fun writeToCache(file: File, response: Response) {
        if (!file.exists()) {
            file.createNewFile()
        }
        if (!response.isSuccessful) {
            return
        }
        response.body?.bytes()?.let {
            file.writeBytes(it)
        }

    }
}