package cn.edu.hznu.me;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class AlarmAlertActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
		wakeUpAndUnlock();
		int type = getIntent().getIntExtra("type",0);
		String tile ="";
		String content="";
		if(type == 1){
			tile = "种子来信";
			content = "不要忘记给你的小树苗浇水哦";
		}else{
			finish();
		}

		new AlertDialog.Builder(AlarmAlertActivity.this).setIcon(R.drawable.icon_switch)
				.setTitle(tile).setMessage(content).setCancelable(false)
				.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				}).show();
		notificationVibrator();
		notificationRing();
	}

	public  void wakeUpAndUnlock(){
		KeyguardManager km= (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
		//解锁
		kl.disableKeyguard();
		//获取电源管理器对象
		PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
		//获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
		//点亮屏幕
		wl.acquire();
		//释放
		wl.release();
	}

	private MediaPlayer mediaPlayer = new MediaPlayer();
	public Vibrator vibrator;
	/**
	 * 振动通知
	 */
	private void notificationVibrator() {

			if (vibrator == null) {
				vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
			}
			vibrator.vibrate(new long[]{500, 50, 50, 1000, 50}, -1);
	}

	/**
	 * 铃声通知
	 */
	private void notificationRing() {

		if (mediaPlayer == null)
			mediaPlayer = new MediaPlayer();
		if (mediaPlayer.isPlaying())
			return;

		try {
			// 这里是调用系统自带的铃声
			Uri uri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_ALARM);
			mediaPlayer.stop();
			mediaPlayer.reset();
			mediaPlayer.setDataSource(this, uri);
			mediaPlayer.prepare();
		} catch (Exception e) {

		}
		mediaPlayer.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mediaPlayer.stop();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			// Volume keys and camera keys dismiss the alarm
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_MENU:
			case KeyEvent.KEYCODE_FOCUS:
				return true;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
