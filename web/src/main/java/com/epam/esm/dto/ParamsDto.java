package com.epam.esm.dto;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ParamsDto {

    private Set<String> tagIdSet;
    private Optional<String> part;
    private Optional<String> nameSort;
    private Optional<String> descriptionSort;

    public ParamsDto(Set<String> tagIdSet, Optional<String> part, Optional<String> nameSort, Optional<String> descriptionSort) {
        this.tagIdSet = tagIdSet;
        this.part = part;
        this.nameSort = nameSort;
        this.descriptionSort = descriptionSort;
    }

    public Set<String> getTagIdSet() {
        if(tagIdSet == null){
            tagIdSet = new HashSet<>();
        }
        return tagIdSet;
    }

    public void setTagIdSet(Set<String> tagIdSet) {
        if(tagIdSet == null){
            tagIdSet = new HashSet<>();
        }
        this.tagIdSet = tagIdSet;
    }

    public Optional<String> getPart() {
        return part;
    }

    public void setPart(Optional<String> part) {
        this.part = part;
    }

    public Optional<String> getNameSort() {
        return nameSort;
    }

    public void setNameSort(Optional<String> nameSort) {
        this.nameSort = nameSort;
    }

    public Optional<String> getDescriptionSort() {
        return descriptionSort;
    }

    public void setDescriptionSort(Optional<String> descriptionSort) {
        this.descriptionSort = descriptionSort;
    }

    @Override
    public String toString() {
        return "ParamsDto{" +
                "tagIdSet=" + tagIdSet +
                ", part=" + part +
                ", nameSort=" + nameSort +
                ", descriptionSort=" + descriptionSort +
                '}';
    }
}
