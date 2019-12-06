package ch.aaap.assignment.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class CantonImpl implements Canton {
    private String code;
    private String name;

    private Set<District> districts;

    public CantonImpl(String code, String name) {
        this.code = code;
        this.name = name;

        districts = new HashSet<>();
    }

    public void addDistrict(District district){
        districts.add(district);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CantonImpl canton = (CantonImpl) o;
        return Objects.equals(code, canton.code);
    }
}
