package com.google.mlkit.vision.demo

import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class cropImage : BitmapTransformation () {
    override fun updateDiskCacheKey(messageDigest: MessageDigest?) {
        TODO("Not yet implemented")
    }

    override fun transform(pool: BitmapPool,
                           toTransform: Bitmap,
                           outWidth: Int,
                           outHeight: Int): Bitmap = Bitmap.createBitmap(
            toTransform,
            0,
            0,
            toTransform.width,
            toTransform.height - 20
    )

    // Glide.with()




}




