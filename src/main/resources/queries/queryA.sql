-- Query A (single-row per id)
SELECT CONVERT(CHAR(50), t.id) AS id, t.category AS category, t.status AS status
FROM TEST_DATA t
WHERE CONVERT(CHAR(50), t.id) = :id;
