package org.example.chap11.lazyloading;

import java.util.List;

import org.example.chap10.datamapper.annotation.Id;
import org.example.chap10.datamapper.annotation.Table;

@Table(name = "test_product")
public class LazyLoadingProduct {
	@Id
	private Long id;
	private String name;
	@Lazy
	private List<Product> products;

	public LazyLoadingProduct(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
