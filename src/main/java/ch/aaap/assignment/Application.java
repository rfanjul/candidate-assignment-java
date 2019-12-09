package ch.aaap.assignment;

import ch.aaap.assignment.model.Canton;
import ch.aaap.assignment.model.CantonImpl;
import ch.aaap.assignment.model.District;
import ch.aaap.assignment.model.DistrictImpl;
import ch.aaap.assignment.model.Model;
import ch.aaap.assignment.model.ModelImpl;
import ch.aaap.assignment.model.PoliticalCommunity;
import ch.aaap.assignment.model.PoliticalCommunityImpl;
import ch.aaap.assignment.model.PostalCommunity;
import ch.aaap.assignment.model.PostalCommunityImpl;
import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;
import ch.aaap.assignment.raw.CSVUtil;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Application {

  final Map<String, PostalCommunity> zipCodeToPostalCommunity = new HashMap<>();
  final Map<String, PostalCommunity> numberToPostalCommunity = new HashMap<>();
  final Map<String, PostalCommunity> nameToPostalCommunity = new HashMap<>();

  private Model model = null;

  public Application() {

    initModel();
  }

  public static void main(String[] args) {
    new Application();
  }

  /** Reads the CSVs and initializes a in memory model */
  private void initModel() {
    Set<CSVPoliticalCommunity> politicalCommunities = CSVUtil.getPoliticalCommunities();
    Set<CSVPostalCommunity> postalCommunities = CSVUtil.getPostalCommunities();

    //
    final Set<District> districts = new HashSet<>();
    final Set<Canton> cantons = new HashSet<>();

    Set<PoliticalCommunity> politicalCo =
        politicalCommunities.stream()
            .map(
                o -> {
                  Canton canton = new CantonImpl(o.getCantonCode(), o.getCantonName());

                  District district = new DistrictImpl(o.getDistrictNumber(), o.getDistrictName());

                  districts.add(district);

                  if (cantons.contains(canton)) {
                    for (Canton c : cantons) {
                      if (c.equals(canton)) {
                        c.addDistrict(district);
                      }
                    }
                  } else {
                    cantons.add(canton);
                  }

                  return new PoliticalCommunityImpl(
                      // return new PoliticalCommunityExt(
                      o.getNumber(),
                      o.getName(),
                      o.getShortName(),
                      canton,
                      district,
                      o.getLastUpdate());
                })
            .collect(Collectors.toSet());

    Set<PostalCommunity> postalCo =
        postalCommunities.stream()
            .map(
                o -> {
                  PostalCommunityImpl postalCommunity =
                      new PostalCommunityImpl(
                          o.getZipCode(),
                          o.getZipCodeAddition(),
                          o.getName(),
                          o.getCantonCode(),
                          o.getPoliticalCommunityNumber());

                  zipCodeToPostalCommunity.put(o.getZipCode(), postalCommunity);
                  numberToPostalCommunity.put(o.getPoliticalCommunityNumber(), postalCommunity);
                  nameToPostalCommunity.put(o.getName(), postalCommunity);

                  return postalCommunity;
                })
            .collect(Collectors.toSet());

    // TODO implementation
    model = new ModelImpl(politicalCo, postalCo, cantons, districts);
  }

  /** @return model */
  public Model getModel() {
    return model;
  }

  /**
   * @param canton code of a canton (e.g. ZH)
   * @return amount of political communities in given canton
   */
  public long getAmountOfPoliticalCommunitiesInCanton(final String cantonCode) {

    // TODO implementation
    long num =
        model.getPoliticalCommunities().stream()
            .filter(
                politicalCommunity -> politicalCommunity.getCanton().getCode().equals(cantonCode))
            .count();

    if (num == 0) {
      throw new IllegalArgumentException();
    }

    return num;
  }

  /**
   * @param canton code of a canton (e.g. ZH)
   * @return amount of districts in given canton
   */
  public long getAmountOfDistrictsInCanton(String cantonCode) {
    // TODO implementation
    return model.getCantons().stream()
        .filter(canton -> canton.getCode().equals(cantonCode))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new)
        .getDistricts()
        .size();
  }

  /**
   * @param district number of a district (e.g. 101)
   * @return amount of districts in given canton
   */
  public long getAmountOfPoliticalCommunitiesInDistrict(String districtNumber) {
    // TODO implementation
    long num =
        model.getPoliticalCommunities().stream()
            .filter(
                politicalCommunity ->
                    politicalCommunity.getDistrict().getNumber().equals(districtNumber))
            .count();

    if (num == 0) {
      throw new IllegalArgumentException();
    }

    return num;
  }

  /**
   * @param zip code 4 digit zip code
   * @return district that belongs to specified zip code
   */
  public String getDistrictForZipCode(String zipCode) {

    return model.getPoliticalCommunities().stream()
        .filter(
            o -> {
              if (zipCodeToPostalCommunity.containsKey(zipCode)) {
                return o.getNumber()
                    .equals(zipCodeToPostalCommunity.get(zipCode).getPoliticalCommunityNumber());
              }

              return false;
            })
        .findFirst()
        .orElseThrow(IllegalArgumentException::new)
        .getDistrict()
        .getName();
  }

  /**
   * @param postal community name
   * @return lastUpdate of the political community by a given postal community name
   */
  public LocalDate getLastUpdateOfPoliticalCommunityByPostalCommunityName(
      String postalCommunityName) {

    PostalCommunity postalCommunity;
    if (nameToPostalCommunity.containsKey(postalCommunityName)) {
      postalCommunity = nameToPostalCommunity.get(postalCommunityName);
    } else {
      throw new IllegalArgumentException();
    }

    TreeSet<LocalDate> updates =
        model.getPoliticalCommunities().stream()
            .filter(o -> postalCommunity.getPoliticalCommunityNumber().equals(o.getNumber()))
            .map(PoliticalCommunity::getLastUpdate)
            .collect(Collectors.toCollection(TreeSet::new));

    return updates.pollFirst();
  }

  /**
   * https://de.wikipedia.org/wiki/Kanton_(Schweiz)
   *
   * @return amount of canton
   */
  public long getAmountOfCantons() {
    // TODO implementation
    return model.getCantons().size();
  }

  /**
   * https://de.wikipedia.org/wiki/Kommunanz
   *
   * @return amount of political communities without postal communities
   */
  public long getAmountOfPoliticalCommunityWithoutPostalCommunities() {

    return model.getPoliticalCommunities().stream()
        .filter(o -> !numberToPostalCommunity.containsKey(o.getNumber()))
        .count();
  }
}
