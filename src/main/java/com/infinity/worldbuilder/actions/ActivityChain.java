package com.infinity.worldbuilder.actions;

import java.util.ArrayList;
import java.util.List;

import com.infinity.worldbuilder.util.PanUtil;

public class ActivityChain {

	private List<Activity> activities = new ArrayList<Activity>();
	
	public void addActivity(Activity activity) {
		activities.add(activity);
	}
	
	public PanUtil execute(PanUtil util) {
		
		PanUtil currentGraphUtil = util;
		
		for (Activity activity : activities) {
			currentGraphUtil = activity.performAction(currentGraphUtil);
		}
		
		return currentGraphUtil;
	}
}
