package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    List<TimeEntry> aList = new ArrayList<>();



    private DataSource ds;
    public JdbcTimeEntryRepository(DataSource datasource) {
        jdbcTemplate = new JdbcTemplate(datasource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            int result = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) {
                    PreparedStatement prepStatement = null;
                    try {
                        prepStatement = con.prepareStatement("INSERT INTO time_entries (project_id,user_id,date,hours) values (?,?,?,?) ", RETURN_GENERATED_KEYS);

                        prepStatement.setLong(1, timeEntry.getProjectId());
                        prepStatement.setLong(2, timeEntry.getUserId());
                        prepStatement.setDate(3, Date.valueOf(timeEntry.getDate()));
                        prepStatement.setLong(4, timeEntry.getHours());
                        return prepStatement;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }, generatedKeyHolder);

            //timeEntry.setId(generatedKeyHolder.getKey().longValue());

        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {

//        Map<String, Object> foundEntry = jdbcTemplate.queryForMap("select id, project_id, user_id, date, hours from time_entries where id = ?", timeEntryId);
//
//        if (foundEntry.size() > 0) {
//            return new TimeEntry((Long)foundEntry.get("id"),
//                    (Long)foundEntry.get("project_id"),
//                    (Long)foundEntry.get("user_id"),
//                    ((Date)foundEntry.get("date")).toLocalDate(),
//                    (Integer)foundEntry.get("hours"));
//        } else
//            return  null;

        String sql = "select * from time_entries where id = ?";

        List<TimeEntry> timeEntriesList = jdbcTemplate.query(sql, new RowMapper<TimeEntry>() {

            @Override
            public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {

                return new TimeEntry(
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getLong(3),
                        rs.getDate(4).toLocalDate(),
                        rs.getInt(5));

            }
        }, timeEntryId);

        if (timeEntriesList.size() == 0) {
            return null;
        }
        else
            return timeEntriesList.get(0);
    }

    @Override
    public List<TimeEntry> list() {

//        Map<String, Object> foundEntry = jdbcTemplate.queryForMap("Select * from time_entries where id = ?", timeEntry.getId());
//        String
//        for (Map.Entry<String, Object> entry : foundEntry.entrySet())
//        {
//            aList.add(entry.getValue());
//        }
//        return aList;
        String sql = "Select * from time_entries";

        List<TimeEntry> timeEntriesList = jdbcTemplate.query(sql, new RowMapper<TimeEntry>() {

            @Override
            public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {

                return new TimeEntry(
                rs.getLong(1),
                rs.getLong(2),
                rs.getLong(3),
                rs.getDate(4).toLocalDate(),
                rs.getInt(5));

            }
        });
                return timeEntriesList;
    }

    @Override
    public TimeEntry update(long eq, TimeEntry timeEntry) {

        int result = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) {
                PreparedStatement prepStatement = null;
                try {
                    prepStatement = con.prepareStatement("UPDATE time_entries set project_id = ?,user_id = ?,date = ?,hours = ? where id = ?");

                    prepStatement.setLong(1, timeEntry.getProjectId());
                    prepStatement.setLong(2, timeEntry.getUserId());
                    prepStatement.setDate(3, Date.valueOf(timeEntry.getDate()));
                    prepStatement.setLong(4, timeEntry.getHours());
                    prepStatement.setLong(5, eq);
                    return prepStatement;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });


        return find(eq);
    }

    @Override
    public void delete(long timeEntryId) {
        // Prepare our SQL statement using Unnamed Parameters style
        String query = "delete from time_entries where id = ?";

        // Delete
        jdbcTemplate.update(query, timeEntryId);
    }
//
//    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
//            rs.getLong("id"),
//            rs.getLong("project_id"),
//            rs.getLong("user_id"),
//            rs.getDate("date").toLocalDate(),
//            rs.getInt("hours")
//    );

//    private final ResultSetExtractor<TimeEntry> extractor =
//            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
