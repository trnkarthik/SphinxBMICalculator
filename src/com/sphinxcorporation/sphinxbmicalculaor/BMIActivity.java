package com.sphinxcorporation.sphinxbmicalculaor;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
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
				// Toast.makeText(getApplicationContext(),"hi 2",Toast.LENGTH_LONG).show();

			}

		});

        //age selection

        SeekBar AgeValue = (SeekBar) findViewById(R.id.AgeSeekBar);
        AgeValue.setMax(80);
        AgeValue.setProgress(23);

        AgeValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            TextView ageValue = (TextView)findViewById(R.id.ageValue);

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                ageValue.setText(progressChanged + 20 + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(BMIActivity.this,"seek bar progress:"+progressChanged,
                        Toast.LENGTH_SHORT).show();
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

				int gender = 0;
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

					Toast.makeText(
							getApplicationContext(),
							"wt in kg " + WeightInKgs + " " + "ht in mts "
									+ heightInMts, Toast.LENGTH_LONG).show();

					BMIIndex = ((WeightInKgs) / (heightInMts * heightInMts));
					Toast.makeText(getApplicationContext(), BMIIndex + " ",Toast.LENGTH_LONG).show();

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

                    Toast.makeText(getApplicationContext(), resultCategory +"",
                            Toast.LENGTH_LONG).show();

                    View BMIResultTextLayout = dialog
                            .findViewById(R.id.BMIResultTextLayout);

                    TextView BMIResultComment = (TextView) BMIResultTextLayout
                            .findViewById(R.id.BMIResultComment);
                    BMIResultComment.setText("Your BMI Suggests that you are "+ resultCategory);

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
            return("Normal");
        else if(BMI >= 25 && BMI <= 29.9)
            return("OverWeight");
        else if(BMI >= 30 && BMI <= 39.9)
            return("Obesity");
        else if(BMI >= 40 )
            return("Extreme Obesity");
        return("Normal");

    }

    protected void checkon() {
		// if(isFemaleActive)
		Toast.makeText(getApplicationContext(),
				"female " + isFemaleActive + " male " + isMaleActive,
				Toast.LENGTH_LONG).show();
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
			Toast.makeText(this, "Rate your App", Toast.LENGTH_SHORT).show();
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
        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi checkout this cool app");
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
                  //  share.putExtra(Intent.EXTRA_SUBJECT,  "subject");
                    share.putExtra(Intent.EXTRA_TEXT,     "http://www.trnkarthik.com");
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
	
	public void createDialog()
    {
        Dialog dialog = new Dialog(BMIActivity.this);
        dialog.setContentView(R.layout.activity_about_us);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("About US");

/*
        View BMIResultLayout = dialog
                .findViewById(R.id.BMIResultLayout);
        TextView BMIResultTV = (TextView) BMIResultLayout
                .findViewById(R.id.BMIResultTextView);
        BMIResultTV.setText(" " + BMIIndex);
        BMIResultTV.setTextColor(Color.BLACK);
*/

        dialog.show();
    }
	
	
}
