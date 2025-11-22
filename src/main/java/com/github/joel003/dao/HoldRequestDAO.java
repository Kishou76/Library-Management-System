package com.github.joel003.dao;

import com.github.joel003.entity.HoldRequest;
import com.github.joel003.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoldRequestDAO {

    private final  Connection con = DBConnection.getConnection() ;

    public boolean addHoldRequest(HoldRequest holdRequest) throws SQLException {
        String sql = """
            INSERT INTO hold_requests (book_id, borrower_id, request_date)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, holdRequest.getBookId());
            ps.setInt(2, holdRequest.getBorrowerId());
            ps.setDate(3, Date.valueOf(holdRequest.getRequestDate()));

            return ps.executeUpdate() > 0;
        }
    }

    public List<HoldRequest> viewHoldRequests() throws SQLException {

        List<HoldRequest> list = new ArrayList<>();

        String query = """
            SELECT hold_id, book_id, borrower_id, request_date
            FROM hold_requests
            ORDER BY request_date ASC
            """;
        try(PreparedStatement ps = con.prepareStatement(query);ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HoldRequest holdRequest = new HoldRequest();
                holdRequest.setHoldId(rs.getInt("hold_id"));
                holdRequest.setBookId(rs.getInt("book_id"));
                holdRequest.setBorrowerId(rs.getInt("borrower_id"));
                holdRequest.setRequestDate(rs.getDate("request_date").toLocalDate());
                list.add(holdRequest);
            }
        }
        return list;
    }
}
