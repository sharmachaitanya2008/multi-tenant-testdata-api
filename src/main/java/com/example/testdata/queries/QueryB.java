package com.example.testdata.queries;

public class QueryB {
    public static final String BASE = """
                SELECT master_member_id AS memberId, amount, txn_date AS txnDate
                FROM MEMBER_ORDERS
            """;

    public static String byMemberId() {
        return BASE + " WHERE master_member_id = :memberId";
    }
}
