package com6441.team7.risc.utils;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * a util to check if the object is null
 */
public class NotnullUtils {
    private NotnullUtils(){}

    public static <E> Stream<E> toStream(Collection<E> nullableCollection) {
        return Optional.ofNullable(nullableCollection)
                .map(Collection::stream)
                .orElse(Stream.empty());
    }
}
