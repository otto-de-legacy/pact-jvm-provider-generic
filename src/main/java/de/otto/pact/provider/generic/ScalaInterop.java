package de.otto.pact.provider.generic;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.collection.Map;

public class ScalaInterop {
    private ScalaInterop() {}

    public static <T> ImmutableList<T> fromSeq(Seq<T> s) {
        return ImmutableList.copyOf(JavaConverters.asJavaIterableConverter(s).asJava());
    }

    public static <K, V> ImmutableMap<K, V> fromMap(Map<K, V> m) {
        return ImmutableMap.copyOf(JavaConverters.mapAsJavaMapConverter(m).asJava());
    }
}
