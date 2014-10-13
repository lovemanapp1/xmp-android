package org.helllabs.android.xmp.service.utils;

import org.helllabs.android.xmp.service.receiver.MediaButtonsReceiver;
import org.helllabs.android.xmp.util.Log;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.Build;

public class RemoteControl {

	private static final String TAG = "RemoteControl";
	private final ComponentName remoteControlReceiver;
	private RemoteControlClientCompat remoteControlClient;
	private final AudioManager audioManager;

	public RemoteControl(final Context context, final AudioManager audioManager) {
		this.audioManager = audioManager;

		remoteControlReceiver = new ComponentName(context.getPackageName(), MediaButtonsReceiver.class.getName());

		if (remoteControlClient == null) {
			Log.i(TAG, "Register remote control client");
			
			audioManager.registerMediaButtonEventReceiver(remoteControlReceiver);

			final Intent remoteControlIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
			remoteControlIntent.setComponent(remoteControlReceiver);

			remoteControlClient = new RemoteControlClientCompat(PendingIntent.getBroadcast(context, 0, remoteControlIntent, 0));

			if (Build.VERSION.SDK_INT >= 14) {
				remoteControlClient.setTransportControlFlags(
						RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE |
						RemoteControlClient.FLAG_KEY_MEDIA_STOP |
						RemoteControlClient.FLAG_KEY_MEDIA_NEXT |
						RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS);
			}

			RemoteControlHelper.registerRemoteControlClient(audioManager, remoteControlClient);
		}
	}

	public void unregisterReceiver() {
		Log.w(TAG, "Unregister remote control client");
		audioManager.unregisterMediaButtonEventReceiver(remoteControlReceiver);
		RemoteControlHelper.unregisterRemoteControlClient(audioManager, remoteControlClient);
	}

	@TargetApi(14)
	public void setStatePlaying() {
		if (Build.VERSION.SDK_INT >=14) {
			if (remoteControlClient != null) {
				Log.i(TAG, "Set state to playing");
				remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
			}
		}
	}

	@TargetApi(14)
	public void setStatePaused() {
		if (Build.VERSION.SDK_INT >=14) {
			if (remoteControlClient != null) {
				Log.i(TAG, "Set state to paused");
				remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
			}
		}
	}

	@TargetApi(14)
	public void setStateStopped() {
		if (Build.VERSION.SDK_INT >=14) {
			if (remoteControlClient != null) {
				Log.i(TAG, "Set state to stopped");
				remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_STOPPED);
			}
		}
	}
}
