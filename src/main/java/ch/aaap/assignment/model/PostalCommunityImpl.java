package ch.aaap.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostalCommunityImpl implements PostalCommunity {
    private String zipCode;
    private String zipCodeAddition;
    private String name;
    private String cantonCode;
    private String politicalCommunityNumber;
}
