package org.example.chap10.datamapper;

public interface StatementSource {
    String sql();

    Object[] parameters();
}
