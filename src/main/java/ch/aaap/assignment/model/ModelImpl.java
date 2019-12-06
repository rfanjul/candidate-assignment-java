package ch.aaap.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Set;

@Getter
@AllArgsConstructor
public class ModelImpl implements Model {
    private Set<PoliticalCommunity> politicalCommunities;
    private Set<PostalCommunity> postalCommunities;
    private Set<Canton> cantons;
    private Set<District> districts;
}
