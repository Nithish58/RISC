package com6441.team7.risc.utils;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * a util to check if the object is null
 */
public class NotnullUtils {
    private NotnullUtils(){}

    /**
     * if the collection is empty, return empty, else return the stream of collections
     * @param nullableCollection the collectors
     * @param <E> receives any type of data
     * @return stream of the type of data
     */
    public static <E> Stream<E> toStream(Collection<E> nullableCollection) {
        return Optional.ofNullable(nullableCollection)
                .map(Collection::stream)
                .orElse(Stream.empty());
    }
}
