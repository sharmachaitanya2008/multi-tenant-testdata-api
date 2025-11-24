package com.example.testdata.queries;

public class QueryA {
    public static final String BASE = """
                SELECT member_id AS memberId, category, status
                FROM MEMBER_TABLE
            """;

    public static String byMemberId() {
        return BASE + " WHERE member_id = :memberId";
    }
}
