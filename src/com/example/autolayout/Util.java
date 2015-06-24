package com.example.autolayout;

import java.util.ArrayList;
import java.util.List;

public class Util {	
	public final static boolean LOG = true;
	
	public static List<Integer> getFactors(int val) {

		List<Integer> factors = new ArrayList<Integer>();
		for (int i = 1; i <= val / 2; i++) {
			if (val % i == 0) {
				factors.add(i);
			}
		}
		factors.add(val);
		return factors;
	}
}
