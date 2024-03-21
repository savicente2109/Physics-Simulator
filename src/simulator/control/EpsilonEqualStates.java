package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator {
	
	public static final double EPS = 0.0;
	
	private double eps;
	
	public EpsilonEqualStates() {
		eps = EPS;
	}
	
	public EpsilonEqualStates(double eps) {
		this.eps = eps;
	}

	@Override
	public boolean equal(JSONObject s1, JSONObject s2) {
		
		boolean equal = false;
		
		if (s1.getDouble("time") == s2.getDouble("time"))
			equal = true;
		
		JSONArray ja1 = s1.getJSONArray("bodies"), ja2 = s2.getJSONArray("bodies");
		
		if (ja1.length() == ja2.length())
			for (int i = 0; i < ja1.length() && equal; ++i) {
				
				JSONObject body_jo1 = ja1.getJSONObject(i), body_jo2 = ja2.getJSONObject(i);
				
				JSONArray p1 = body_jo1.getJSONArray("p"), p2 = body_jo2.getJSONArray("p"),
						v1 = body_jo1.getJSONArray("v"), v2 = body_jo2.getJSONArray("v"),
						f1 = body_jo1.getJSONArray("f"), f2 = body_jo2.getJSONArray("f");
				
				equal = body_jo1.getString("id").equals(body_jo2.getString("id")) &&
						Math.abs(body_jo1.getDouble("m") - body_jo2.getDouble("m")) <= eps &&
						epsEqualVectors(p1, p2) && epsEqualVectors(v1, v2) && epsEqualVectors(f1, f2);
				
			}
		
		else equal = false;
		
		return equal;
	}
	
	private boolean epsEqualVectors(JSONArray a1, JSONArray a2) {
		if (a1.length() != a2.length())
			return false;

		Vector2D v1 = new Vector2D(a1.getDouble(0), a1.getDouble(1));
		Vector2D v2 = new Vector2D(a2.getDouble(0), a2.getDouble(1));

		return (v1.distanceTo(v2) <= eps);
	}

}
