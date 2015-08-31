package com.github.rosjava.robotino.run_robotino;



import java.util.ArrayList;
import java.util.Vector;

import com.uppaal.engine.QueryFeedback;
import com.uppaal.engine.QueryVerificationResult;
import com.uppaal.model.system.symbolic.SymbolicTransition;

public class QueryFeedbackCustomized implements QueryFeedback {
	
	@Override
	public void appendText(String s) {

	}

	@Override
	public void setCurrent(int pos) {

	}

	@Override
	public void setFeedback(String feedback) {

	}

	@Override
	public void setLength(int length) {

	}

	@Override
	public void setProgress(int load, long vm, long rss, long cached,
			long avail, long swap, long swapfree, long user, long sys,
			long timestamp) {

	}

	@Override
	public void setProgressAvail(boolean availability) {

	}

	@Override
	public void setResultText(String s) {
		if (s != null && s.length() > 0) {
			System.out.println(s);
		}
	}

	@Override
	public void setSystemInfo(long vmsize, long physsize, long swapsize) {

	}

	@Override
	public void setTrace(char result, String feedback,
			ArrayList<SymbolicTransition> trace, int cycle,
			QueryVerificationResult arg4) {

	}

}
