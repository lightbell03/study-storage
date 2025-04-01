package org.example.chap11.lazyloading;

import java.util.List;

public interface VirtualListLoader<T> {
    List<T> load();
}
