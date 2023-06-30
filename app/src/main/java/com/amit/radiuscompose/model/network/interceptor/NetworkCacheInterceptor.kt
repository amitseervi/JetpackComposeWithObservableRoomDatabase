package com.amit.radiuscompose.model.network.interceptor

import android.content.Context
import com.amit.radiuscompose.model.network.CustomHeaders
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
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
            Timber.i("NetworkCacheInterceptor : read from file")
            buildResponseFromFile(cacheFile, request)
        } else {
            val result = try {
                val response = chain.proceed(request)
                if (response.isSuccessful) {
                    Timber.i("NetworkCacheInterceptor : write response to file and build from data array")
                    return writeToCache(cacheFile, response)
                } else {
                    Timber.i("NetworkCacheInterceptor : return same response to consumer")
                    return response
                }
            } catch (e: Exception) {
                Timber.i("NetworkCacheInterceptor : build error response")
                buildResponseFromError(e, request)
            }
            result
        }
    }

    private fun buildResponseFromError(exception: java.lang.Exception, request: Request): Response {
        return Response.Builder()
            .code(500)
            .body("error : ${exception.message}".toResponseBody(contentType = "text/html".toMediaType()))
            .message(exception.message ?: "error")
            .protocol(Protocol.HTTP_1_1)
            .request(request)
            .build()
    }

    private fun buildResponseFromFile(file: File, request: Request): Response {
        assert(file.exists()) {
            Timber.e("Cache file does not exist but response requested from cache file")
        }
        val fileResponse = file.readBytes()
        return Response.Builder()
            .code(200)
            .body(fileResponse.toResponseBody())
            .message("success")
            .protocol(Protocol.HTTP_1_1)
            .request(request)
            .build()
    }

    private fun buildResponseFromByteArray(data: ByteArray, request: Request): Response {
        return Response.Builder()
            .code(200)
            .body(data.toResponseBody())
            .message("success")
            .protocol(Protocol.HTTP_1_1)
            .request(request)
            .build()
    }

    private fun writeToCache(file: File, response: Response): Response {
        if (!file.exists()) {
            file.createNewFile()
            file.setWritable(true)
        }
        response.body.let { responseBody ->
            if (responseBody != null) {
                val data = responseBody.bytes()
                file.writeBytes(data)
                file.setLastModified(System.currentTimeMillis())
                return buildResponseFromByteArray(data, response.request)
            } else {
                return response
            }
        }
    }
}