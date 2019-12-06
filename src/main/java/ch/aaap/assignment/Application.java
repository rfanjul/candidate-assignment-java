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
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Application {

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
                o ->
                    new PostalCommunityImpl(
                        o.getZipCode(),
                        o.getZipCodeAddition(),
                        o.getName(),
                        o.getCantonCode(),
                        o.getPoliticalCommunityNumber()))
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
  public long getAmountOfPoliticalCommunitiesInDistict(String districtNumber) {
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

    PostalCommunity postalCommunity =
        model.getPostalCommunities().stream()
            .filter(o -> o.getZipCode().equals(zipCode))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

    return model.getPoliticalCommunities().stream()
        .filter(o -> o.getNumber().equals(postalCommunity.getPoliticalCommunityNumber()))
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

    PostalCommunity postalCommunity =
        model.getPostalCommunities().stream()
            .filter(o -> o.getName().equals(postalCommunityName))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

    TreeSet<LocalDate> updates = new TreeSet<>();
    // TODO implementation
    model.getPoliticalCommunities().stream()
        .filter(o -> o.getNumber().equals(postalCommunity.getPoliticalCommunityNumber()))
        .forEach(
            politicalCommunity -> {
              updates.add(politicalCommunity.getLastUpdate());
            });

    return updates.pollLast();
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
    // TODO implementation
    return model.getPoliticalCommunities().stream()
        .filter(
            o ->
                model.getPostalCommunities().stream()
                        .filter(a -> a.getPoliticalCommunityNumber().equals(o.getNumber()))
                        .count()
                    == 0)
        .count();
  }
}
