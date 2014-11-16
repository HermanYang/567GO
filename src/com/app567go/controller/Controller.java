package com.app567go.controller;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebView.WebViewTransport;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.app567go.R;
import com.app567go.base.AnimationPlayer;
import com.app567go.base.AppSetting;
import com.app567go.base.ConnectionChangeBroadcaster;
import com.app567go.constant.AppState;
import com.app567go.constant.Command;
import com.app567go.constant.DialogId;
import com.app567go.constant.ResourceId;
import com.app567go.listerner.CourseImageOnClickListener;
import com.app567go.listerner.HomeImageOnClickListener;
import com.app567go.listerner.LogoImageOnClickListener;
import com.app567go.listerner.OnNetworkStateChangedListener;
import com.app567go.listerner.ShareImageOnClickListener;
import com.app567go.manager.DialogManager;
import com.app567go.manager.ResourceManager;
import com.app567go.manager.ViewStore;
import com.app567go.view.BasicWebView;
import com.app567go.view.Html5WebView;
import com.app567go.view.VideoWebView;

public class Controller implements OnNetworkStateChangedListener
{
	private static Controller	mInstance					= null;
	private static Object		INSTANCE_LOCK				= new Object();
	private static final int	PERSENT_LOGO_STATE_567GO	= 0;
	private static final int	PERSENT_LOGO_STATE_BACK		= 1;
	private ArrayList<Activity>	mActivities					= null;
	private Activity			mForegroundActivity			= null;
	private Context				mAppContext					= null;
	private AppState			mCurrentState;
	private Handler				mHandler					= null;
	private ResourceManager		mResourceManager			= null;
	private ViewStore			mViewStore					= null;
	private DialogManager		mDialogManager				= null;
	private AppSetting			mAppSetting					= null;
	private AnimationPlayer		mAnimationPlayer			= null;
	
	private int					mPersentLogoState			= PERSENT_LOGO_STATE_567GO;
	
	private Controller()
	{
		mActivities = new ArrayList<Activity>();
		mCurrentState = AppState.IN_HOME_SECTION;
		mHandler = new Handler();
		
		ConnectionChangeBroadcaster.register(this);
	}
	
	public static Controller getInstance()
	{
		if (mInstance == null)
		{
			synchronized (INSTANCE_LOCK)
			{
				if (mInstance == null)
				{
					mInstance = new Controller();
				}
			}
		}
		return mInstance;
	}
	
	public static void destroyInstance()
	{
		mInstance = null;
	}
	
	public void dealWith(Command what, ArrayList<Object> args)
	{
		switch (what)
		{
			case INITIALIZE:
			{
				destoryAllInstances();
				
				mForegroundActivity.setContentView(R.layout.layout_launch_page);
				
				// asynchronous initialization
				mHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						mResourceManager = ResourceManager.getInstance();
						
						Runnable runContinueRunnable = new Runnable()
						{
							
							@Override
							public void run()
							{
								mViewStore = ViewStore.getInstance();
								
								mViewStore.initializeAllViews();
								
								mDialogManager = DialogManager.getInstance();
								
								mAppSetting = AppSetting.getInstance();
								
								mAnimationPlayer = AnimationPlayer.getInstance();
								
								// First installed
								if (mAppSetting.getBoolean(AppSetting.BOOLEAN_FIRST_INSTALLED, true))
								{
									mAppSetting.setBoolean(AppSetting.BOOLEAN_FIRST_INSTALLED, false);
									mAppSetting.save();
								}
								
								// initilize home page
								View homePageLayout = mViewStore.getViewById(R.id.layout_home_page);
								
								mForegroundActivity.setContentView(homePageLayout);
								
								ViewGroup contentContainerLayout = (ViewGroup) mViewStore.getViewById(R.id.layout_content_container);
								View homeSectionLayout = mViewStore.getViewById(R.id.layout_home_section);
								
								contentContainerLayout.addView(homeSectionLayout);
								
								mAnimationPlayer.fadeInView(homePageLayout);
								
								View logoImageView = mViewStore.getViewById(R.id.imageView_logo);
								logoImageView.setOnClickListener(new LogoImageOnClickListener());
								
								View courseImageView = mViewStore.getViewById(R.id.imageView_course);
								courseImageView.setOnClickListener(new CourseImageOnClickListener());
								
								View homeImageView = mViewStore.getViewById(R.id.imageView_home);
								homeImageView.setOnClickListener(new HomeImageOnClickListener());
								
								View shareImageView = mViewStore.getViewById(R.id.imageView_share);
								shareImageView.setOnClickListener(new ShareImageOnClickListener());
								
								setCurrentState(AppState.IN_HOME_SECTION);
								
								// load all urls in advance
								loadAllUrls();
							}
						};
						
						mResourceManager.setCallBack(runContinueRunnable);
						
					}
				});
				
				setCurrentState(AppState.IN_LAUNCH_PAGE);
				
				break;
			}
			
			case BACK_TO_HOME_SECTION:
			{
				backToHomeSection();
				break;
			}
			
			case LOGO_IMAGE_PRESSED:
			{
				switch (mPersentLogoState)
				{
					case PERSENT_LOGO_STATE_567GO:
					{
						backToHomeSection();
						break;
					}
					
					case PERSENT_LOGO_STATE_BACK:
					{
						onBackPressed();
						break;
					}
				}
				
				break;
			}
			
			case HOME_SECTION_TO_INSTITUTE_INTRODUCTION_SECTION:
			{
				View instituteIntroduction = mViewStore.getViewById(R.id.layout_institute_introduction_section);
				String title = mResourceManager.getString(R.string.navigation_title_institute_introduction);
				
				switchTo(instituteIntroduction);
				
				changeNaigationBarUI(title, false);
				
				setCurrentState(AppState.IN_INSTITUTE_INTRODUCTION_SECTION);
				break;
			}
			
			case HOME_SECTION_TO_INSTITUTE_NEWS_SECTION:
			{
				View institutenews = mViewStore.getViewById(R.id.layout_institute_news_section);
				String title = mResourceManager.getString(R.string.navigation_title_institute_news);
				
				switchTo(institutenews);
				
				changeNaigationBarUI(title, false);
				
				setCurrentState(AppState.IN_INSTITUTE_NEWS_SECTION);
				break;
			}
			
			case HOME_SECTION_TO_INSTITUE_MOMENT_SECTION:
			{
				View instituteMoment = mViewStore.getViewById(R.id.layout_institute_moment_section);
				String title = mResourceManager.getString(R.string.navigation_title_institute_moment);
				
				switchTo(instituteMoment);
				
				changeNaigationBarUI(title, false);
				
				setCurrentState(AppState.IN_INSTITUTE_MOMENT_SECTION);
				
				break;
			}
			
			case HOME_SECTION_TO_FREQUENT_QUESTION_SECTION:
			{
				View frequentQuetion = mViewStore.getViewById(R.id.layout_frequent_question_section);
				String title = mResourceManager.getString(R.string.navigation_title_frequent_question);
				
				switchTo(frequentQuetion);
				
				changeNaigationBarUI(title, false);
				
				setCurrentState(AppState.IN_FREQUENT_QUESTION_SECTION);
				break;
			}
			
			case HOME_SECTION_TO_COURSE_SECTION:
			{
				View course = mViewStore.getViewById(R.id.layout_course_section);
				String title = mResourceManager.getString(R.string.navigation_title_course);
				
				switchTo(course);
				
				changeNaigationBarUI(title, false);
				
				setCurrentState(AppState.IN_COURSE_SECTION);
				break;
			}
			
			case HOME_SECTION_TO_MENTOR_SECTION:
			{
				View mentor = mViewStore.getViewById(R.id.layout_photo_section);
				String title = mResourceManager.getString(R.string.navigation_title_mentors);
				
				switchTo(mentor);
				
				changeNaigationBarUI(title, false);
				
				setCurrentState(AppState.IN_MENTOR_SECTION);
				break;
			}
			
			case HOME_SECTION_TO_RECRUITMENT_NEWS_SECTION:
			{
				View recruitmentNews = mViewStore.getViewById(R.id.layout_recruitment_news_section);
				String title = mResourceManager.getString(R.string.navigation_title_recruitment_news);
				
				switchTo(recruitmentNews);
				
				changeNaigationBarUI(title, false);
				
				setCurrentState(AppState.IN_RECRUITMENT_NEWS_SECTION);
				break;
			}
			
			case HOME_SECTION_TO_ONLINE_SIGN_UP_SECTION:
			{
				View onlineSignUp = mViewStore.getViewById(R.id.layout_online_sign_up_section);
				String title = mResourceManager.getString(R.string.navigation_title_online_sign_up);
				
				switchTo(onlineSignUp);
				
				changeNaigationBarUI(title, false);
				
				setCurrentState(AppState.IN_ONLINE_SIGN_UP_SECTION);
				break;
			}
			
			case HOME_SECTION_TO_CONTACT_US_SECTION:
			{
				View contactUs = mViewStore.getViewById(R.id.layout_contact_us_section);
				String title = mResourceManager.getString(R.string.navigation_title_contact_us);
				
				switchTo(contactUs);
				
				changeNaigationBarUI(title, false);
				
				setCurrentState(AppState.IN_CONTACT_US_SECTION);
				break;
			}
			
			case SHOW_WEB_CONTENT:
			{
				String url = (String) args.get(0);
				ViewGroup photoItemContentView = (ViewGroup) mViewStore.getViewById(R.id.layout_common_webview_container);
				switchTo(photoItemContentView);
				
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.basic_webView);
				webView.loadUrl("about:blank");
				webView.loadUrl(url);
				
				switch (mCurrentState)
				{
					case IN_MENTOR_SECTION:
					{
						setCurrentState(AppState.IN_PHOTO_ITEM_SECTION);
						break;
					}
					
					default:
						break;
				}
				
				break;
			}
			
			case START_LOADING_WEB_PAGE:
			{
				
				ViewGroup contentContainerLayout = (ViewGroup) mViewStore.getViewById(R.id.layout_content_container);
				View view = (View) args.get(0);
				
				if (view.getParent() == contentContainerLayout)
				{
				}
				mDialogManager.getDialogById(DialogId.PROGRESS).show();
				break;
			}
			
			case FINISH_LOADING_WEB_PAGE:
			{
				Dialog dialog = mDialogManager.getDialogById(DialogId.PROGRESS);
				
				if (dialog.isShowing())
				{
					dialog.dismiss();
				}
				break;
			}
			
			case START_ACTIVITY:
			{
				Intent intent = (Intent) args.get(0);
				mForegroundActivity.startActivity(intent);
				break;
			}
			
			case ONE_KEY_SHARE:
			{
				oneKeyShare();
				break;
			}
			
			case ORIENTATION_CHANGED:
			{
				break;
			}
			
			case QUIT:
			{
				mAppSetting.save();
				
				mForegroundActivity.finish();
				
				break;
			}
			
			default:
				break;
		}
	}
	
	public void dealWith(Command what)
	{
		dealWith(what, null);
	}
	
	public void registerActivity(Activity activity)
	{
		mActivities.add(activity);
	}
	
	public void unregisterActivity(Activity activity)
	{
		mActivities.remove(activity);
		if (mForegroundActivity == activity)
		{
			mForegroundActivity = null;
		}
	}
	
	public void markForegroundActivity(Activity activity)
	{
		mForegroundActivity = activity;
	}
	
	public Activity getForegroundActivity()
	{
		return mForegroundActivity;
	}
	
	public void setAppContext(Context context)
	{
		mAppContext = context;
	}
	
	public Context getAppContext()
	{
		return mAppContext;
	}
	
	// return true means the event is consumed
	public boolean onBackPressed()
	{
		switch (mCurrentState)
		{
			case IN_LAUNCH_PAGE:
			{
				return true;
			}
			
			case IN_HOME_SECTION:
			{
				Dialog quitDialog = mDialogManager.getDialogById(DialogId.QUIT);
				quitDialog.show();
				return true;
			}
			
			case IN_PHOTO_ITEM_SECTION:
			{
				View photoSectionLayout = mViewStore.getViewById(R.id.layout_photo_section);
				
				switchTo(photoSectionLayout);
				
				setCurrentState(AppState.IN_MENTOR_SECTION);
				return true;
			}
			
			case IN_INSTITUTE_INTRODUCTION_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_institute_introduction);
				
				if (webView.canGoBack())
				{
					webView.goBack();
				}
				else
				{
					backToHomeSection();
				}
				
				return true;
			}
			
			case IN_INSTITUTE_NEWS_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_institute_news);
				
				if (webView.canGoBack())
				{
					webView.goBack();
				}
				else
				{
					backToHomeSection();
				}
				
				return true;
			}
			
			case IN_INSTITUTE_MOMENT_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_institute_moments);
				
				if (webView.canGoBack())
				{
					webView.goBack();
				}
				else
				{
					backToHomeSection();
				}
				
				return true;
			}
			
			case IN_FREQUENT_QUESTION_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_frequent_questions);
				
				if (webView.canGoBack())
				{
					webView.goBack();
				}
				else
				{
					backToHomeSection();
				}
				
				return true;
			}
			
			case IN_COURSE_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_course);
				
				if (webView.canGoBack())
				{
					webView.goBack();
				}
				else
				{
					backToHomeSection();
				}
				
				return true;
			}
			
			case IN_RECRUITMENT_NEWS_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_recruitment_news);
				
				if (webView.canGoBack())
				{
					webView.goBack();
				}
				else
				{
					backToHomeSection();
				}
				
				return true;
			}
			
			case IN_ONLINE_SIGN_UP_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_online_sign_up);
				
				if (webView.canGoBack())
				{
					webView.goBack();
				}
				else
				{
					backToHomeSection();
				}
				
				return true;
			}
			case IN_CONTACT_US_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_contact_us);
				
				if (webView.canGoBack())
				{
					webView.goBack();
				}
				else
				{
					backToHomeSection();
				}
				
				return true;
			}
			
			case IN_MENTOR_SECTION:
			{
				backToHomeSection();
				return true;
			}
			
			default:
				return false;
		}
	}
	
	private void changeNaigationBarUI(String title, boolean presentLogo)
	{
		TextView navigationTitleTextView = (TextView) mViewStore.getViewById(R.id.textView_navigation_title);
		navigationTitleTextView.setText(title);
		
		ImageView navigationLogoImageView = (ImageView) mViewStore.getViewById(R.id.imageView_logo);
		
		if (presentLogo)
		{
			navigationLogoImageView.setImageResource(R.drawable.logo_567go);
			mPersentLogoState = PERSENT_LOGO_STATE_567GO;
		}
		else
		{
			navigationLogoImageView.setImageResource(R.drawable.back_btn_selector);
			mPersentLogoState = PERSENT_LOGO_STATE_BACK;
		}
	}
	
	private void destoryAllInstances()
	{
		ViewStore.destoryInstance();
		ResourceManager.destroyInstance();
		DialogManager.destoryInstance();
		AppSetting.destoryInstance();
		AnimationPlayer.destoryInstance();
	}
	
	private void setCurrentState(AppState state)
	{
		mCurrentState = state;
	}
	
	private void backToHomeSection()
	{
		View homeSectionLayout = mViewStore.getViewById(R.id.layout_home_section);
		
		switchTo(homeSectionLayout);
		
		changeNaigationBarUI(mResourceManager.getString(R.string.navigation_title_home), true);
		
		setCurrentState(AppState.IN_HOME_SECTION);
	}
	
	private void loadAllUrls()
	{
		BasicWebView instituteIntroductionWebView = (BasicWebView) mViewStore.getViewById(R.id.webView_institute_introduction);
		instituteIntroductionWebView.loadUrl(mResourceManager.getJSONString(ResourceId.INSTITUTE_INTRODUCTION_SECTION_URL));
		
		BasicWebView instituteNewsWebView = (BasicWebView) mViewStore.getViewById(R.id.webView_institute_news);
		instituteNewsWebView.loadUrl(mResourceManager.getJSONString(ResourceId.INSTITUTE_NEWS_SECTION_URL));
		
		BasicWebView instituteMomentsWebView = (BasicWebView) mViewStore.getViewById(R.id.webView_institute_moments);
		instituteMomentsWebView.loadUrl(mResourceManager.getJSONString(ResourceId.INTITUTE_MOMENT_SECTION_URL));
		
		BasicWebView frequentQuestionsWebView = (BasicWebView) mViewStore.getViewById(R.id.webView_frequent_questions);
		frequentQuestionsWebView.loadUrl(mResourceManager.getJSONString(ResourceId.FREQUENT_QUESTION_SECTION_URL));
		
		BasicWebView courseWebView = (BasicWebView) mViewStore.getViewById(R.id.webView_course);
		courseWebView.loadUrl(mResourceManager.getJSONString(ResourceId.COURSE_SECTION_URL));
		
		BasicWebView recruitmentNewsWebView = (BasicWebView) mViewStore.getViewById(R.id.webView_recruitment_news);
		recruitmentNewsWebView.loadUrl(mResourceManager.getJSONString(ResourceId.RECRUITMENT_NEWS_SECTION_URL));
		
		BasicWebView onlineSignUpWebView = (BasicWebView) mViewStore.getViewById(R.id.webView_online_sign_up);
		onlineSignUpWebView.loadUrl(mResourceManager.getJSONString(ResourceId.ONLINE_SIGN_UP_SECTION_URL));
		
		BasicWebView contactUsWebView = (BasicWebView) mViewStore.getViewById(R.id.webView_contact_us);
		contactUsWebView.loadUrl(mResourceManager.getJSONString(ResourceId.CONTACT_US_SECTION_URL));
		
	}
	
	private void switchTo(View newContentView)
	{
		ViewGroup contentContainerLayout = (ViewGroup) mViewStore.getViewById(R.id.layout_content_container);
		ViewGroup parentViewGroup = (ViewGroup) newContentView.getParent();
		
		if (parentViewGroup != contentContainerLayout)
		{
			contentContainerLayout.removeAllViews();
			
			if (parentViewGroup != null)
			{
				parentViewGroup.removeView(newContentView);
			}
			
			contentContainerLayout.addView(newContentView);
			
			mAnimationPlayer.fadeInView(newContentView);
		}
		
		newContentView.requestFocus();
	}
	
	private void oneKeyShare()
	{
		OnekeyShare oks = new OnekeyShare();
		
		String title = mResourceManager.getString(R.string.app_name);
		String url = "http://www.567go.com";
		
		switch (mCurrentState)
		{
			case IN_HOME_SECTION:
			{
				url = "http://121.199.47.174/home";
				break;
			}
			
			case IN_INSTITUTE_INTRODUCTION_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_institute_introduction);
				title = webView.getTitle();
				url = webView.getUrl();
				break;
			}
			
			case IN_INSTITUTE_NEWS_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_institute_news);
				title = webView.getTitle();
				url = webView.getUrl();
				break;
			}
			
			case IN_FREQUENT_QUESTION_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_frequent_questions);
				title = webView.getTitle();
				url = webView.getUrl();
				break;
			}
			
			case IN_COURSE_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_course);
				title = webView.getTitle();
				url = webView.getUrl();
				break;
			}
			
			case IN_MENTOR_SECTION:
			{
				title = "567GO培训导师团队";
				url = "http://www.567go.com/Peixun";
				break;
			}
			
			case IN_PHOTO_ITEM_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.basic_webView);
				title = webView.getTitle();
				url = webView.getUrl();
				break;
			}
			
			case IN_RECRUITMENT_NEWS_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_recruitment_news);
				title = webView.getTitle();
				url = webView.getUrl();
				break;
			}
			
			case IN_ONLINE_SIGN_UP_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_online_sign_up);
				title = webView.getTitle();
				url = webView.getUrl();
				break;
			}
			
			case IN_CONTACT_US_SECTION:
			{
				BasicWebView webView = (BasicWebView) mViewStore.getViewById(R.id.webView_contact_us);
				title = webView.getTitle();
				url = webView.getUrl();
				break;
			}
			
			default:
			{
				break;
			}
		}
		
		oks.disableSSOWhenAuthorize();
		
		oks.setNotification(R.drawable.logo_567go, title);
		
		oks.setTitle(title);
		
		oks.setImagePath(mResourceManager.getShareFilePath());
		
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		
		oks.show(mForegroundActivity);
	}
	
	@Override
	public void onNetworkConnected()
	{
		loadAllUrls();
	}
	
	@Override
	public void onNetworkDisconnected()
	{
		int duration = 1000;
		Toast.makeText(mAppContext, R.string.network_disconnected, duration).show();
	}
	
}