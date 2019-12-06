package ch.aaap.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class DistrictImpl implements District {
    private String number;
    private String name;

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistrictImpl canton = (DistrictImpl) o;
        return Objects.equals(name, canton.name);
    }
}
