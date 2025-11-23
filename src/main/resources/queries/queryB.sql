-- Query B (aggregate or joined data)
SELECT COALESCE(SUM(o.amount),0) AS totalOrders, COUNT(o.id) AS orderCount
FROM ORDERS o
WHERE o.test_data_id = :id;
