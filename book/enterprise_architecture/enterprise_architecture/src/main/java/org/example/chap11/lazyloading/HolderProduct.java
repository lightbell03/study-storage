package org.example.chap11.lazyloading;

import org.example.chap10.datamapper.annotation.Id;
import org.example.chap10.datamapper.annotation.Table;

import java.util.List;

@Table(name = "test_product")
public class HolderProduct {
    @Id
    private Long id;

    private String name;

    @Lazy
    private Product products;
}
