package com.haishinkit.haishin_kit

import android.util.Size
import android.view.Surface
import com.haishinkit.graphics.PixelTransform
import com.haishinkit.graphics.VideoGravity
import com.haishinkit.graphics.effect.VideoEffect
import com.haishinkit.media.Stream
import com.haishinkit.media.StreamDrawable
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.view.TextureRegistry

class StreamDrawableTexture(binding: FlutterPlugin.FlutterPluginBinding) :
    StreamDrawable {
    override var videoGravity: VideoGravity
        get() = pixelTransform.videoGravity
        set(value) {
            pixelTransform.videoGravity = value
        }

    override var frameRate: Int
        get() = pixelTransform.frameRate
        set(value) {
            pixelTransform.frameRate = value
        }

    override var videoEffect: VideoEffect
        get() = pixelTransform.videoEffect
        set(value) {
            pixelTransform.videoEffect = value
        }

    val id: Long
        get() = entry?.id() ?: 0

    var imageExtent: Size
        get() = pixelTransform.imageExtent
        set(value) {
            pixelTransform.imageExtent = value
            entry?.surfaceTexture()?.setDefaultBufferSize(value.width, value.height)
        }

    private val pixelTransform: PixelTransform by lazy {
        PixelTransform.create(binding.applicationContext)
    }

    private var stream: Stream? = null
        set(value) {
            field?.drawable = null
            field = value
            field?.drawable = this
            pixelTransform.screen = value?.screen
        }

    private var entry: TextureRegistry.SurfaceTextureEntry? = null

    init {
        val entry = binding.textureRegistry.createSurfaceTexture()
        pixelTransform.surface = Surface(entry.surfaceTexture())
        this.entry = entry
    }

    fun dispose() {
        entry?.release()
        pixelTransform.dispose();
    }

    override fun attachStream(stream: Stream?) {
        this.stream = stream
    }
}