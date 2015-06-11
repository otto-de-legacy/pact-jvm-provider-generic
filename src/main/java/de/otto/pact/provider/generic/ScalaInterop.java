package de.otto.pact.provider.generic;

import com.google.common.collect.ImmutableList;
import scala.collection.JavaConverters;
import scala.collection.Seq;

public class ScalaInterop {
    private ScalaInterop() {}

    public static <T> ImmutableList<T> fromSeq(Seq<T> s) {
        return ImmutableList.copyOf(JavaConverters.asJavaIterableConverter(s).asJava());
    }
}
