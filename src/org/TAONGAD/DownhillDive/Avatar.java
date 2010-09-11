package org.TAONGAD.DownhillDive;

import org.TAONGAD.DownhillDive.Accelerometer;

import android.hardware.SensorManager;
import android.util.Log;


public class Avatar {
	public enum Leaning {
		NONE,
		SLIGHTLY,
		ALOT
	}
	//SIC... float m_lean; // Leaning depending on turning
	private boolean m_leanLeft;
	private Leaning m_leanCurrent;
	
	Avatar(DownhillDive parent, SensorManager sense) {
		Accelerometer.start(sense);
		m_leanLeft = false;
		m_leanCurrent = Leaning.NONE;
	}
	
	public void Update() {
		Log.d("Avatar", "hej");
		if (Accelerometer.getX() > 0.5f) {
			m_leanCurrent = Leaning.ALOT;
		}
		else {
			m_leanCurrent = Leaning.NONE;
		}
		
		
		
	}
	
	public String getLeanPic(Leaning lean) {
		switch (lean) {
		case NONE: return "back0.png";
		case SLIGHTLY: return "back15.png";
		case ALOT: return "back35.png";
		}
		
		return "shouldnothappen";
	}
	
	public Leaning getLeanCurrent() {
		return m_leanCurrent;
	}
	/**
	 * Returns true if the pictures/textures need to be fliped
	 * @return
	 */
	public boolean getLeanLeft() {
		return m_leanLeft;
	}
}
