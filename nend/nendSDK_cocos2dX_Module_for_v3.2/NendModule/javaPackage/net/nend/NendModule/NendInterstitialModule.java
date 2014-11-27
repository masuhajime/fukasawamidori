package net.nend.NendModule;

import net.nend.android.NendAdInterstitial;
import net.nend.android.NendAdInterstitial.NendAdInterstitialClickType;
import net.nend.android.NendAdInterstitial.NendAdInterstitialShowResult;
import net.nend.android.NendAdInterstitial.NendAdInterstitialStatusCode;
import net.nend.android.NendAdInterstitial.OnCompletionListener;

import org.cocos2dx.cpp.AppActivity;
import org.cocos2dx.lib.Cocos2dxActivity;

import android.app.Activity;

public class NendInterstitialModule {	
	
	private static int mSelectedInterstitialType = 0;
	public static final int NO_BACK_KEY_SHOW_INTERSTITIAL        = 0;  // backキー以外でインタースティシャル表示
	public static final int ON_BACK_KEY_SHOW_NORMAL_INTERSTITIAL = 1;  // backキーで通常のインタースティシャル表示
	public static final int ON_BACK_KEY_SHOW_FINISH_INTERSTITIAL = 2;  // backキーで終了時のインタースティシャル表示

	public static void createInterstitial(final String apiKey, final String spotID) {
		
		final Activity activity = (Activity)Cocos2dxActivity.getContext();

		activity.runOnUiThread(new Runnable() {
			public void run() {

				int intSpotID = Integer.parseInt(spotID);
				
				NendAdInterstitial.loadAd(activity, apiKey, intSpotID);
				// 広告ロード結果の通知を受け取るようにする
				NendAdInterstitial.setListener(new OnCompletionListener(){
					
					@Override
					public void onCompletion(NendAdInterstitialStatusCode statusCode) {
						int resultCode = statusCode.ordinal();
						loadResultStatus(resultCode);	
					}					
					
				});
			}
		});
	}
	
	// インタースティシャル広告の表示
	public static void showInterstitialView() {		

		final Activity activity = (Activity)Cocos2dxActivity.getContext();
		
		if (AppActivity.isBackKeySelected()){			

			// backキーでshowInterstitialViewが呼ばれたとき
			mSelectedInterstitialType = ON_BACK_KEY_SHOW_NORMAL_INTERSTITIAL;
		
		}else{			
			
			// backキー以外でshowInterstitialViewが呼ばれた時
			mSelectedInterstitialType = NO_BACK_KEY_SHOW_INTERSTITIAL;
			
			activity.runOnUiThread(new Runnable() {
				public void run() {
					
					// クリックイベントのタイプが返却される
					NendAdInterstitial.OnClickListener listener = new NendAdInterstitial.OnClickListener() {
						
						@Override
						public void onClick(NendAdInterstitialClickType clickType) {
							int resultCode = clickType.ordinal();
							onClickStatus(resultCode);
						}
					};
					
	                // 表示結果が返却される				
	                NendAdInterstitialShowResult result = NendAdInterstitial.showAd(activity, listener);
	                int resultCode = result.ordinal();
	                showResultStatus(resultCode);	                
				}
			});
		}
	}
	
	// インタースティシャル広告の削除
	public static void dismissNADInterstitialView(){
		final Activity activity = (Activity)Cocos2dxActivity.getContext();
		activity.runOnUiThread(new Runnable() {
			public void run() {
				
				boolean dismissResult = NendAdInterstitial.dismissAd();
                // 広告削除結果で何かの処理する場合
				if (dismissResult){
					mSelectedInterstitialType = NO_BACK_KEY_SHOW_INTERSTITIAL;
				}
			}
		});
	}
	
	// 終了時広告表示
	public static void showFinishNADInterstitialView(){
		
		final Activity activity = (Activity)Cocos2dxActivity.getContext();
		
		if (AppActivity.isBackKeySelected()){			
			
			// backキーでshowFinishNADInterstitialViewが呼ばれたとき
			mSelectedInterstitialType = ON_BACK_KEY_SHOW_FINISH_INTERSTITIAL;
		
		}else{			
			
			// backキー以外でshowFinishNADInterstitialViewが呼ばれた時
			mSelectedInterstitialType = NO_BACK_KEY_SHOW_INTERSTITIAL;
			
			activity.runOnUiThread(new Runnable() {
				public void run() {
					
					// クリックイベントのタイプが返却される
					NendAdInterstitial.OnClickListener listener = new NendAdInterstitial.OnClickListener() {
						
						@Override
						public void onClick(NendAdInterstitialClickType clickType) {
							
							int resultCode = clickType.ordinal();
							onClickStatus(resultCode);
						}
					};
					
					// 表示結果が返却される
					NendAdInterstitialShowResult result = NendAdInterstitial.showFinishAd(activity, listener);	
	                int resultCode = result.ordinal();
	                showResultStatus(resultCode);	
				}
			});
			
		}
	}
	
	
	public static void showNADInterstitialViewFromBackKey(){

		final Activity activity = (Activity)Cocos2dxActivity.getContext();

		switch (mSelectedInterstitialType){
		case 1: // 通常広告
			
			activity.runOnUiThread(new Runnable() {
				public void run() {
					
					// クリックイベントのタイプが返却される
					NendAdInterstitial.OnClickListener listener = new NendAdInterstitial.OnClickListener() {
						
						@Override
						public void onClick(NendAdInterstitialClickType clickType) {
							int resultCode = clickType.ordinal();
							onClickStatus(resultCode);
						}
					};
					
	                // 表示結果が返却される				
	                NendAdInterstitialShowResult result = NendAdInterstitial.showAd(activity, listener);
	                int resultCode = result.ordinal();
	                showResultStatus(resultCode);
				}
			});
		case 2: // 終了時広告
			
			activity.runOnUiThread(new Runnable() {
				public void run() {

					NendAdInterstitial.OnClickListener listener = new NendAdInterstitial.OnClickListener() {
						
						@Override
						public void onClick(NendAdInterstitialClickType clickType) {
							
							int resultCode = clickType.ordinal();
							onClickStatus(resultCode);
						}
					};
								
					// 表示結果が返却される
					NendAdInterstitialShowResult result = NendAdInterstitial.showFinishAd(activity, listener);	
	                int resultCode = result.ordinal();
	                showResultStatus(resultCode);	
				}
			});			
		}
		
		mSelectedInterstitialType = NO_BACK_KEY_SHOW_INTERSTITIAL;
		AppActivity.setBackKeySelected(false);
	}
	
	public static native void loadResultStatus(final int statusCode);
	public static native void showResultStatus(final int statusCode);
	public static native void onClickStatus(final int statusCode);
	
}
