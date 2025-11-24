-- queryB.sql : one-to-many properties (orders, events, etc.), master key aliased as memberId
SELECT master_member_id AS memberId,
       amount,
       txn_date AS txnDate
FROM MEMBER_ORDERS
