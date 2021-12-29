package com.github.eternajunior.core;

public interface HasId<ID extends Number & Comparable<ID>> {

    ID getId();

    void setId(ID id);
}
