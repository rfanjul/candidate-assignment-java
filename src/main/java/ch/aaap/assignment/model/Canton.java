package ch.aaap.assignment.model;

import java.util.Set;

public interface Canton {

  public String getCode();

  public String getName();

  public Set<District> getDistricts();

  public void addDistrict(District district);
}
