package me.scherbs.autominer.profile;

import me.scherbs.autominer.tier.Tier;
import org.json.simple.JSONObject;

public class ProfileSerializer {

  public JSONObject serialize(Profile toSerialize) {
    JSONObject toReturn = new JSONObject();
    toReturn.put("uuid", toSerialize.getUuid().toString());

    if (toSerialize.getTiers().isEmpty()) {
      return toReturn;
    } else {
      for (Tier tier : toSerialize.getTiers()) {
        toReturn.put("tier." + tier.getType().name(), tier.getRemaining());
      }
    }
    return toReturn;
  }

  public Profile deserialize(JSONObject jsonObject) {
    return new Profile(jsonObject);
  }

}
