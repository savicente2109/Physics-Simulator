package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;


public class MassEqualStates implements StateComparator {


	@Override
	public boolean equal(JSONObject s1, JSONObject s2) {
		
		boolean equal = false;
		
		if (s1.getDouble("time") == s2.getDouble("time"))
			equal = true;
		
		JSONArray ja1 = s1.getJSONArray("bodies"), ja2 = s2.getJSONArray("bodies");
		
		if (ja1.length() == ja2.length())
			for (int i = 0; i < ja1.length() && equal; ++i)
				equal = ja1.getJSONObject(i).getDouble("m") == ja2.getJSONObject(i).getDouble("m") && ja1.getJSONObject(i).getString("id").equals(ja2.getJSONObject(i).getString("id"));
		
		else equal = false;
		
		return equal;
	}

}
