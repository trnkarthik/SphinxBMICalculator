package com.sphinxcorporation.sphinxbmicalculaor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class BMIActivity extends Activity {

	public Float height = (float) 0.0;
	public Float weight = (float) 0.0;
	public Float heightInMts;
	public Float WeightInKgs;
	public Float BMIIndex;
	public int Img = 10;
	public View heightLayout;
	public View genderLayout;
	public View DropDownheightInchesLayout;
	public EditText heightEditText;
	public EditText heightInchesET;
	public Spinner heightSpinner;
	public DropDownAnim dropDown;
	public DropDownAnim dropUp;
	public ImageView maleIcon;
	public ImageView femaleIcon;
	public boolean isMaleActive = false;
	public boolean isFemaleActive = false;
    public int gender = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bmi);

		// images
		genderLayout = findViewById(R.id.GenderLayout);
		maleIcon = (ImageView) genderLayout.findViewById(R.id.maleIcon);
		femaleIcon = (ImageView) genderLayout.findViewById(R.id.femaleIcon);

		maleIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isMaleActive == true) {
					// change to black and white and set clicked to false
					maleIcon.setImageDrawable(getResources().getDrawable(
							R.drawable.male_off));
					isMaleActive = false;


				} else if (isMaleActive == false) {
					// check if female is active.If so deactivate it
					if (isFemaleActive == true) {
						isFemaleActive = false;
						femaleIcon.setImageDrawable(getResources().getDrawable(
								R.drawable.female_off));
					}

					// change to color and set clicked to true
					maleIcon.setImageDrawable(getResources().getDrawable(
							R.drawable.male_on));
					isMaleActive = true;

					checkon();

				}

			}

		});

		femaleIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isFemaleActive == true) {
					// change to black and white and set clicked to false
					femaleIcon.setImageDrawable(getResources().getDrawable(
							R.drawable.female_off));
					isFemaleActive = false;

				} else if (isFemaleActive == false) {
					// check if female is active.If so deactivate it
					if (isMaleActive == true) {
						isMaleActive = false;
						maleIcon.setImageDrawable(getResources().getDrawable(
								R.drawable.male_off));
					}

					// change to color and set clicked to true
					femaleIcon.setImageDrawable(getResources().getDrawable(
							R.drawable.female_on));
					isFemaleActive = true;

				}

				checkon();

			}

		});

		// height layout
		heightLayout = (View) (findViewById(R.id.HeightLayout));
		heightEditText = (EditText) heightLayout
				.findViewById(R.id.BMIHeightEditText);

		DropDownheightInchesLayout = heightLayout
				.findViewById(R.id.DropDownHeightInchesLayout);
		heightInchesET = (EditText) DropDownheightInchesLayout
				.findViewById(R.id.BMIHeightInchesEditText);

		// dropdown/dropUp animation object for height layout.
		dropDown = new DropDownAnim(DropDownheightInchesLayout, 100, true);
		dropUp = new DropDownAnim(DropDownheightInchesLayout, 0, true);

		// getting spinner
		heightSpinner = (Spinner) heightLayout
				.findViewById(R.id.BMIHeightUnitsSpinner);
		heightSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				// Toast.makeText(getApplicationContext(), "hi 1",
				// Toast.LENGTH_LONG).show();

				// height units spinner
				String tempHeightUnit = (String) heightSpinner
						.getSelectedItem().toString().trim();
				if (tempHeightUnit.equals("feet")) {
					heightInchesET.setEnabled(true);
					dropDown.setDuration(500);
					DropDownheightInchesLayout.startAnimation(dropDown);
				} else if (tempHeightUnit.equals("centimeters")) {
					heightInchesET.setEnabled(false);
					dropDown.setDuration(500);
					DropDownheightInchesLayout.startAnimation(dropUp);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

        //age selection

        final SeekBar AgeValue = (SeekBar) findViewById(R.id.AgeSeekBar);
        AgeValue.setMax(80);
        AgeValue.setProgress(5);
        final EditText ageValueET = (EditText)findViewById(R.id.ageValue);
        AgeValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                ageValueET.setText(progressChanged + 20 + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(BMIActivity.this,"seek bar progress:"+progressChanged,Toast.LENGTH_SHORT).show();
            }

        });

        ageValueET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //Toast.makeText(getApplicationContext(),""+ageValueET.getText(),Toast.LENGTH_LONG).show();
                Integer tempInt = Integer.parseInt(ageValueET.getText().toString());
                AgeValue.setProgress(tempInt-20);
                if(tempInt > 120){
                    Toast.makeText(getApplicationContext(),"Please tell me you are kiddinbg me!",Toast.LENGTH_LONG).show();
                }

            }
        });



        // calculate button
		Button bt = (Button) this.findViewById(R.id.BMISubmit);
		bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean errorFlag = false;
				String errorMsg = "";
				String tempHeightUnit = (String) heightSpinner
						.getSelectedItem().toString().trim();
				try {
					// height and its units
					height = Float.parseFloat(heightEditText.getText()
							.toString());
					if (tempHeightUnit.equals("feet")) {
						heightInMts = (float) (height * (0.3048));
						float heightInInches;
						if (heightInchesET.getText().toString().trim()
								.equals("")) {
							heightInInches = 0;
						} else {
							float tempHeightInches = Float
									.parseFloat(heightInchesET.getText()
											.toString().trim());
							heightInInches = (float) (tempHeightInches * (0.0254));
						}
						heightInMts += heightInInches;

					} else if (tempHeightUnit.equals("centimeters")) {
						heightInMts = (float) (height * (0.01));
					}
				} catch (Exception e) {

					errorFlag = true;
					if (heightEditText.getText().toString().equals(""))
						errorMsg = "Please Enter Your Height";
					else
						errorMsg = "Please Enter Valid Height";
					heightEditText.setError(errorMsg);

				}
				View weightLayout = findViewById(R.id.WeightLayout);
				EditText weightEditText = (EditText) weightLayout
						.findViewById(R.id.BMIWeightEditText);
				try {

					weight = Float.parseFloat(weightEditText.getText()
							.toString());
				} catch (Exception e) {
					errorFlag = true;
					if (weightEditText.getText().toString().equals("")) {
						weightEditText.setError("Please Enter your Weight");
						errorMsg = "Please Enter your Weight";
					} else {
						errorMsg = "Please Enter Valid Weight";
						weightEditText.setError("Please Enter Valid Weight");

					}
				}
				if (!isMaleActive && !isFemaleActive && !errorFlag) {
					errorFlag = true;
					errorMsg = "Please Select a  Gender";
				}

				if (!errorFlag) {

					Spinner weightSpinner = (Spinner) weightLayout
							.findViewById(R.id.BMIWeightUnitsSpinner);
					String tempWeightUnit = (String) weightSpinner
							.getSelectedItem().toString().trim();
					if (tempWeightUnit.equals("pounds")) {
						WeightInKgs = (float) (weight * (0.453592));
					} else if (tempWeightUnit.equals("Kgs")) {
						WeightInKgs = weight;
					}

					//Toast.makeText(getApplicationContext(),"wt in kg " + WeightInKgs + " " + "ht in mts "+ heightInMts, Toast.LENGTH_LONG).show();

					BMIIndex = ((WeightInKgs) / (heightInMts * heightInMts));
					//Toast.makeText(getApplicationContext(), BMIIndex + " ",Toast.LENGTH_LONG).show();

					if (isFemaleActive)
						gender = 1;
					if (isMaleActive)
						gender = 2;

					Dialog dialog = new Dialog(BMIActivity.this);
					dialog.setContentView(R.layout.bmi_result_custom_dialog);
					dialog.setCanceledOnTouchOutside(true);
					dialog.setTitle("Your BMI");

					View BMIResultLayout = dialog
							.findViewById(R.id.BMIResultLayout);
					TextView BMIResultTV = (TextView) BMIResultLayout
							.findViewById(R.id.BMIResultTextView);
					BMIResultTV.setText(" " + BMIIndex);
					BMIResultTV.setTextColor(Color.BLACK);

                    String resultCategory = "";

                    resultCategory = determineCategory(BMIIndex);

                    //Toast.makeText(getApplicationContext(), resultCategory +"",Toast.LENGTH_LONG).show();

                    View BMIResultTextLayout = dialog
                            .findViewById(R.id.BMIResultTextLayout);

                    TextView BMIResultComment = (TextView) BMIResultTextLayout
                            .findViewById(R.id.BMIResultComment);
                    BMIResultComment.setText(Html.fromHtml("Your BMI Suggests that you are <b><font color='maroon'>" + resultCategory + ".</font></b> <br />" + getSuggestion(BMIIndex)));

                    View BMIResultFullLayout = dialog
                            .findViewById(R.id.BMIResultFullLayout);

                    Button shareBigButton = (Button)BMIResultFullLayout.findViewById(R.id.shareBigButton);
                    shareBigButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initShareResultIntent(BMIIndex,getBMIImage(gender,BMIIndex));
                        }
                    });

                    ImageView BMIImage = (ImageView)BMIResultFullLayout.findViewById(R.id.bmiImage);

                    //setting image to result
                    BMIImage.setImageDrawable(getBMIImage(gender,BMIIndex));


                    dialog.show();
				} else {
					Toast.makeText(getApplicationContext(), errorMsg,
							Toast.LENGTH_LONG).show();
				}

			} // onclick ends here


        }); // Anonymous class ends here

        //click here for more about BKI link

        TextView knowMore = (TextView)findViewById(R.id.knowMore);
        knowMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(),"Why do you need to know more supid!",Toast.LENGTH_LONG).show();
                Intent toAboutBMI = new Intent(BMIActivity.this,AboutBMI.class);
                startActivity(toAboutBMI);
            }
        });


	}

    public String determineCategory(Float BMI){

        if(BMI< 18.5)
            return("Underweight");
        else if(BMI >= 18.5 && BMI <= 24.9)
            return("Perfect!");
        else if(BMI >= 25 && BMI <= 29.9)
            return("OverWeight");
        else if(BMI >= 30 && BMI <= 39.9)
            return("Obese");
        else if(BMI >= 40 )
            return("Extreme Obese");
        return("Normal");

    }

    public String getSuggestion(Float BMI){

        if(BMI< 18.5)
            return("Time to get some really good diet.");
        else if(BMI >= 18.5 && BMI <= 24.9)
            return("Great job.");
        else if(BMI >= 25 && BMI <= 29.9)
            return("Time to hit the gym.");
        else if(BMI >= 30 && BMI <= 39.9)
            return("No worries.Organised diet and physical exercise can get you back on track.");
        else if(BMI >= 40 )
            return("Time to change your eating habits.");
        return("Normal");

    }

    protected void checkon() {
		// if(isFemaleActive)
		//Toast.makeText(getApplicationContext(),"female " + isFemaleActive + " male " + isMaleActive,Toast.LENGTH_LONG).show();
		// if(isMaleActive)
		// Toast.makeText(getApplicationContext(), "Male " + isMaleActive +
		// " female "+isFemaleActive, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.activity_bmi, menu);
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_about_us:
            createDialog();
/*
			Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(BMIActivity.this, AboutUs.class));
*/
			return true;

		case R.id.menu_share:
			initShareIntent();
            // initShareIntent("twi");

			return true;
			

		case R.id.menu_rateApp:
			//Toast.makeText(this, "Rate this App", Toast.LENGTH_SHORT).show();
            rateApp();
			return true;

		case R.id.menu_exit:
			this.finish();
			System.exit(1);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class DropDownAnim extends Animation {
		int targetHeight;
		View view;
		boolean down;

		public DropDownAnim(View view, int targetHeight, boolean down) {
			this.view = view;
			this.targetHeight = targetHeight;
			this.down = down;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			int newHeight;
			if (down) {
				newHeight = (int) (targetHeight * interpolatedTime);
			} else {
				newHeight = (int) (targetHeight / (interpolatedTime));
			}
			view.getLayoutParams().height = newHeight;
			view.requestLayout();
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
		}

		@Override
		public boolean willChangeBounds() {
			return true;
		}
	}

	public void initShareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sphinx BMI Application");
        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, "\nHey,\n\n I just downloaded Sphinx BMI Application for my Android. It's an insanely awesome app to know your BMI." +
                "Download it from http://www.trnkarthik.com/project.php?id=5.Because you being healthy is important to me!\n\n Take Care");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
	}

    private void initShareIntent(String type) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type) ) {
                    share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sphinx BMI Application");
                    share.putExtra(android.content.Intent.EXTRA_TEXT, "\nHey,\n\n I just downloaded Sphinx BMI Application for my Android. It's an insanely awesome app to know your BMI." +
                            "Download it from http://www.bmi.trnkarthik.com.Because you being healthy is important to me!\n\n Take Care");
                    //share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(myPath)) ); // Optional, just if you wanna share an image.
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return;

            startActivity(Intent.createChooser(share, "Select"));
        }
    }

    public void initShareResultIntent(Float BMIIndex,Drawable image) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My BMI is "+String.format("%.2f", BMIIndex));
        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My BMI of "+ String.format("%.2f", BMIIndex) +" shows that I am "+determineCategory(BMIIndex)+
                ".\nWhat's yours?\nWanna know...then get Sphinx BMI App for Android from http://bmi.trnkarthik.com.Waiting for your reply.\n\nBye.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    public void rateApp(){
        final Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);

        if (getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0)
        {
            startActivity(rateAppIntent);
        }
        else
        {
            Toast.makeText(this, "Some Error!Please try again later.Thanks", Toast.LENGTH_SHORT).show();
        }
    }

    public Drawable getBMIImage(int gender,Float BMIIndex){
            Float x = BMIIndex;
           Toast.makeText(getApplicationContext(),gender + ":" + BMIIndex ,Toast.LENGTH_LONG).show();
            int k = 1;
            String res = "";

            //determining the bmi part
            if(x<=17){
                k=1;
            }
            else if(x>17 && x<=20){
                k=2;
            }
            else if(x>20 && x<=23){
                k=3;
            }
            else if(x>23 && x<=25){
                k=4;
            }
            else if(x>25 && x<=27){
                k=5;
            }
            else if(x>27 && x<=31){
                k=6;
            }
            else if(x>31 && x<=35){
                k=7;
            }
            else if(x>35 && x<=38){
                k=8;
            }
            else if(x>=39){
                k=9;
            }

        //determining the gender part
        if(gender == 1){
            res = "f"+k;
        }
        else if(gender == 2){
            res = "m"+k;
        }

        int resu = getResources().getIdentifier(res,"drawable", getPackageName());
        Drawable drawable = getResources().getDrawable(resu);
        return(drawable);
    }


	public void createDialog()
    {
        Dialog dialog = new Dialog(BMIActivity.this);
        dialog.setContentView(R.layout.activity_about_us);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("About US");
        dialog.show();
    }



}
