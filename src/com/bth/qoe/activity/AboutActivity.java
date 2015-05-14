package com.bth.qoe.activity;

import com.bth.qoe.R;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * The page About the QoEProbe
 *
 */
public class AboutActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_about,
					container, false);
			return rootView;
		}
	}
	
	/**
	 * Start Terms and Condition Activity
	 * @param v
	 */
	public void termsAndCondition(View v){
		 Intent intent_terms = new Intent(AboutActivity.this, TermsAndConditionsActivity.class);
		 intent_terms.putExtra("termstype", "content");
		 AboutActivity.this.startActivity(intent_terms);
	}
	public void closeWindow(View v){
		
		finish();
	}

}
