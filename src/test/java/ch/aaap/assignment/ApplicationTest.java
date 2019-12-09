package ch.aaap.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ApplicationTest {

  /* The system under test */
  Application sut = new Application();

  @Test
  public void testModel() {
    assertEquals(26, sut.getModel().getCantons().size(), "Correct amount of cantons");
    assertEquals(143, sut.getModel().getDistricts().size(), "Correct amount of districts");
    assertEquals(
        2215,
        sut.getModel().getPoliticalCommunities().size(),
        "Correct amount of political communities");
    assertEquals(
        5633, sut.getModel().getPostalCommunities().size(), "Correct amount of postal communities");
  }

  @Test
  public void returnsCorrectAmountOfPoliticalCommunitiesInCanton() {
    assertEquals(
        162,
        sut.getAmountOfPoliticalCommunitiesInCanton("ZH"),
        "Correct amount of political communities in canton with short code 'ZH'");
    assertEquals(
        6,
        sut.getAmountOfPoliticalCommunitiesInCanton("AI"),
        "Correct amount of political communities in canton with short code 'AI'");
    assertThrows(
        IllegalArgumentException.class,
        () -> sut.getAmountOfPoliticalCommunitiesInCanton("XX"),
        "Expected IllegalArgumentException");
  }

  @Test
  public void returnsCorrectmountOfDistrictsInCanton() {
    assertEquals(
        12,
        sut.getAmountOfDistrictsInCanton("ZH"),
        "Correct amount of districts in canton with short code 'ZH'");
    assertEquals(
        11,
        sut.getAmountOfDistrictsInCanton("GR"),
        "Correct amount of districts in canton with short code 'GR'");
    assertThrows(
        IllegalArgumentException.class,
        () -> sut.getAmountOfDistrictsInCanton("XX"),
        "Expected IllegalArgumentException");
  }

  @Test
  public void returnsAmountOfPoliticalCommunitiesInDistict() {
    assertEquals(
        14,
        sut.getAmountOfPoliticalCommunitiesInDistrict("101"),
        "Correct amount of political communities in in distric with number '101'");

    assertThrows(
        IllegalArgumentException.class,
        () -> sut.getAmountOfPoliticalCommunitiesInDistrict("9999"),
        "Expected IllegalArgumentException");
  }

  @Test
  public void returnsCorrectDistrictNameForZipCode() {
    assertEquals(
        "Bezirk Bülach",
        sut.getDistrictForZipCode("8305"),
        "Correct district name for zip code '8305'");
    assertEquals(
        "Region Albula",
        sut.getDistrictForZipCode("7457"),
        "Correct district name for zip code '7457'");

    assertThrows(
        IllegalArgumentException.class,
        () -> sut.getDistrictForZipCode("9999"),
        "Expected IllegalArgumentException");
  }

  @Test
  public void returnsCorrectLastUpdateOfMunicipalityByPostalCommunityName() {
    assertEquals(
        "2016-04-10",
        sut.getLastUpdateOfPoliticalCommunityByPostalCommunityName("Vergeletto").toString(),
        "Correct last update of political community by postal community name 'Vergeletto'");

    assertThrows(
        IllegalArgumentException.class,
        () -> sut.getDistrictForZipCode("Los Angeles"),
        "Expected IllegalArgumentException");
  }

  @Test
  public void returnsCorrectAmountOfCantons() {
    assertEquals(26, sut.getAmountOfCantons(), "Correct amount of cantons");
  }

  @Test
  public void returnsAmountOfPoliticalCommunityWithoutPostalCommunities() {
    assertEquals(
        3,
        sut.getAmountOfPoliticalCommunityWithoutPostalCommunities(),
        "Correct amount of political communities without postal communities");
  }
}
