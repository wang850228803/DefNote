package zinus.note;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
public class SettingActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.weirdpreference);
	        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	        this.onSharedPreferenceChanged(getPreferenceScreen().getSharedPreferences(), "size");
	 }
	   
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Preference pref = this.findPreference(key);
		   if (pref instanceof ListPreference) {
		       ListPreference listPref= (ListPreference) pref;
		        pref.setSummary(listPref.getEntry());
		   }
	}
}