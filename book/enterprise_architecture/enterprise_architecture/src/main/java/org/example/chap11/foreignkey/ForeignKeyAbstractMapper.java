package org.example.chap11.foreignkey;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.example.chap10.datamapper.mapper.AbstractMapper;
import org.example.common.ApplicationException;
import org.example.config.ConnectionFactory;

public abstract class ForeignKeyAbstractMapper<K, D> extends AbstractMapper<K, D> {
	abstract protected String findStatement();

	protected D load(ResultSet rs, String idColumnName) throws SQLException, IllegalAccessException {
		K id = (K) rs.getObject(idColumnName);
		if (loadedMap.containsKey(id)) {
			return loadedMap.get(id);
		}
		D result = doLoad(id, rs);
		loadedMap.put(id, result);
		return result;
	}

	protected D abstractFind(K id, String idColumnName) {
		D result = loadedMap.get(id);
		if (result != null) return result;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionFactory.getInstance().prepare(findStatement());
			stmt.setObject(1, id);
			rs = stmt.executeQuery();
			rs.next();
			result = load(rs, idColumnName);
			return result;
		} catch (SQLException | IllegalAccessException e) {
			throw new ApplicationException(e);
		} finally {
			ConnectionFactory.cleanUp(stmt, rs);
		}
	}
	abstract protected void update(D domain);
}
