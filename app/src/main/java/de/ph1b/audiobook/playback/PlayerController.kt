package de.ph1b.audiobook.playback

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class PlayerController
@Inject constructor(
  private val context: Context
) {

  private var _controller: MediaControllerCompat? = null

  private val callback = object : MediaBrowserCompat.ConnectionCallback() {
    override fun onConnected() {
      super.onConnected()
      Timber.d("onConnected")
      _controller = MediaControllerCompat(context, browser.sessionToken)
    }

    override fun onConnectionSuspended() {
      super.onConnectionSuspended()
      Timber.d("onConnectionSuspended")
      _controller = null
    }

    override fun onConnectionFailed() {
      super.onConnectionFailed()
      Timber.d("onConnectionFailed")
      _controller = null
    }
  }

  private val browser: MediaBrowserCompat = MediaBrowserCompat(
    context,
    ComponentName(context, PlaybackService::class.java),
    callback,
    null
  )

  init {
    browser.connect()
  }

  fun setPosition(time: Long, file: File) = execute { it.setPosition(time, file) }

  fun setLoudnessGain(mB: Int) = execute { it.setLoudnessGain(mB) }

  fun skipSilence(skip: Boolean) = execute { it.skipSilence(skip) }

  fun stop() = execute { it.stop() }

  fun fastForward() = execute { it.fastForward() }

  fun rewind() = execute { it.rewind() }

  fun play() = execute { it.play() }

  fun playPause() = execute { it.playPause() }

  fun setSpeed(speed: Float) = execute { it.setPlaybackSpeed(speed) }

  private inline fun execute(action: (MediaControllerCompat.TransportControls) -> Unit) {
    _controller?.transportControls?.let(action)
  }

  fun fadeOut() {
    // todo implement
  }

  fun cancelFadeout() {
    // todo implement
  }
}
