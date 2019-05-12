package com.luteh.fcmexample

import android.content.Context
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.graphics.Bitmap
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.model.GlideUrl
import javax.xml.datatype.DatatypeConstants.SECONDS
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.signature.ObjectKey
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit


/**
 * Created by Luthfan Maftuh on 5/11/2019.
 * Email luthfanmaftuh@gmail.com
 */

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val memoryCacheSizeBytes = 1024 * 1024 * 20 // 20mb
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes.toLong()))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, memoryCacheSizeBytes.toLong()))
        builder.setDefaultRequestOptions(requestOptions(context))
//        builder.build(context)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()

        val factory = OkHttpUrlLoader.Factory(client)

        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }

    private fun requestOptions(context: Context): RequestOptions {
        return RequestOptions()
            .signature(
                ObjectKey(
                    System.currentTimeMillis() / (24 * 60 * 60 * 1000)
                )
            )
            .override(200, 200)
            .centerCrop()
            .encodeFormat(Bitmap.CompressFormat.PNG)
            .encodeQuality(100)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//            .format(PREFER_ARGB_8888)
            .skipMemoryCache(false)
    }
}