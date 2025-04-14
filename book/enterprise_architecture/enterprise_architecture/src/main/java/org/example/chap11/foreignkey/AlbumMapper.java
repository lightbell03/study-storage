package org.example.chap11.foreignkey;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.example.common.ApplicationException;
import org.example.common.Mapper;
import org.example.common.MapperRegistry;
import org.example.config.ConnectionFactory;

public class AlbumMapper extends ForeignKeyAbstractMapper<Long, Album> implements Mapper {
	public Album find(Long id) {
		return abstractFind(id, "article_id");
	}

	@Override
	protected String findStatement() {
		return "SELECT id, title, artist_id FROM albums WHERE id = ?";
	}

	@Override
	protected Album doLoad(Long id, ResultSet rs) throws SQLException {
		String title = rs.getString("title");
		long artistId = rs.getLong("artist_id");
		Artist artist = MapperRegistry.getMapper(ArtistMapper.class).find(artistId);
		return new Album(id, title, artist);
	}

	@Override
	protected String insertStatement() {
		return null;
	}

	@Override
	protected void doInsert(Album subject, PreparedStatement insertStatement) throws SQLException {

	}

	@Override
	protected void update(Album domain) {
		final String updateStatement = "UPDATE album SET title = ?, artist_id = ? WHERE id = ?";
		PreparedStatement statement = null;
		try {
			statement = ConnectionFactory.getInstance().prepare(updateStatement);

			statement.setString(1, domain.getTitle());
			statement.setLong(2, domain.getArtist().getId());
			statement.setLong(3, domain.getId());

			statement.execute();
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			ConnectionFactory.cleanUp(statement);
		}
	}
}
