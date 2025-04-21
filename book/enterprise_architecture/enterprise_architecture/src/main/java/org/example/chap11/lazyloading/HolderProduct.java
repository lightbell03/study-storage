package org.example.chap11.lazyloading;

import org.example.common.Id;
import org.example.common.Table;

@Table(name = "test_product")
public class HolderProduct {
    @Id
    private Long id;

    private String name;

    @Lazy
    private Product products;
}
