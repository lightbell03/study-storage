package org.example.chap11.lazyloading;


import java.util.List;

public class SupplierMapper {

    public static class ProductLoader implements VirtualListLoader<Product> {
        private Long id;

        public ProductLoader(Long id) {
            this.id = id;
        }

        @Override
        public List<Product> load() {
            return ProductMapper.;
        }
    }
}
