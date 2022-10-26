package ar.edu.itba.pod.models;

import lombok.Getter;

public enum Month {
    JANUARY("January", 31),
    FEBRUARY("February", 28),
    MARCH("March", 31),
    APRIL("April", 30),
    MAY("May", 31),
    JUNE("June", 30),
    JULY("July", 31),
    AUGUST("August", 31),
    SEPTEMBER("September", 30),
    OCTOBER("October", 31),
    NOVEMBER("November", 30),
    DECEMBER("December", 31);

    @Getter
    private final String name;
    @Getter
    private final int days;

    Month(String name, int days) {
        this.name = name;
        this.days = days;
    }
}
