package ch.aaap.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PoliticalCommunityImpl implements PoliticalCommunity {
    private String number;
    private String name;
    private String shortName;
    private Canton canton;
    private District district;
    private LocalDate lastUpdate;
}
