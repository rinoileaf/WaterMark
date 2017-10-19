package com.watermark.photo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.watermark.image.RubberStamp;
import com.watermark.image.RubberStampConfig;
import com.watermark.image.RubberStampPosition;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;


public class WaterMarkActivity extends Activity {
	ImageView img_watermark,img_watermark1,img_watermark2,img_watermark3;
	Button btn_watermark;
	Bitmap watermark_logo,watermark_source;

	private RubberStamp mRubberStamp;
	private Spinner mRubberStampPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watermark);

		img_watermark = (ImageView) findViewById(R.id.img_watermark);
		img_watermark1 = (ImageView) findViewById(R.id.img_watermark1);
		img_watermark2 = (ImageView) findViewById(R.id.img_watermark2);
		img_watermark3 = (ImageView) findViewById(R.id.img_watermark3);
		mRubberStampPosition = (Spinner) findViewById(R.id.spn_rubberStampPositions);
		btn_watermark = (Button) findViewById(R.id.btn_watermark);

		mRubberStamp = new RubberStamp(this);
		watermark_source = BitmapFactory.decodeResource(getResources(),
				R.drawable.motivation);

		img_watermark1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				watermark_logo = ((BitmapDrawable)img_watermark1.getDrawable()).getBitmap();
			}
		});

		img_watermark2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				watermark_logo = ((BitmapDrawable)img_watermark2.getDrawable()).getBitmap();
			}
		});

		img_watermark3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				watermark_logo = ((BitmapDrawable)img_watermark3.getDrawable()).getBitmap();
			}
		});

		btn_watermark.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				generateRubberStamp();
			}
		});


	}


	public void generateRubberStamp() {

		RubberStampConfig config;
		int alpha = 255;
		int rotation =0;
		RubberStampPosition rubberStampPosition =
				convertToRubberStampPosition(mRubberStampPosition.getSelectedItemPosition());

		RubberStampConfig.RubberStampConfigBuilder builder = new RubberStampConfig.RubberStampConfigBuilder()
				.base(watermark_source)
				.alpha(alpha)
				.rotation(rotation)
				.rubberStampPosition(rubberStampPosition);

			config = builder
					.rubberStamp(watermark_logo)
					.build();



		Observable<Bitmap> observable = getBitmap(mRubberStamp, config);
		observable.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.single()
				.subscribe(new Action1<Bitmap>() {
					@Override
					public void call(Bitmap bitmap) {
						img_watermark.setImageBitmap(bitmap);
					}
				}, new Action1<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						Log.e("", throwable.getMessage());
					}
				});
	}
	public Observable<Bitmap> getBitmap(final RubberStamp rubberStamp,
										final RubberStampConfig config) {
		return Observable.defer(new Func0<Observable<Bitmap>>() {
			@Override
			public Observable<Bitmap> call() {
				return Observable.just(rubberStamp.addStamp(config));
			}
		});
	}
	private RubberStampPosition convertToRubberStampPosition(int selectedItemPosition) {
		switch (selectedItemPosition) {
			case 0: return RubberStampPosition.TOP_LEFT;

			case 1: return RubberStampPosition.TOP_CENTER;

			case 2: return RubberStampPosition.TOP_RIGHT;

			case 3: return RubberStampPosition.CENTER_LEFT;

			case 4: return RubberStampPosition.CENTER;

			case 5: return RubberStampPosition.CENTER_RIGHT;

			case 6: return RubberStampPosition.BOTTOM_LEFT;

			case 7: return RubberStampPosition.BOTTOM_CENTER;

			case 8: return RubberStampPosition.BOTTOM_RIGHT;

			case 9: return RubberStampPosition.CUSTOM;

			case 10: return RubberStampPosition.TILE;

			default: return RubberStampPosition.CENTER;
		}
	}

}
