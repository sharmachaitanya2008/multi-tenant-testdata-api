-- Return list of primary keys we want to build docs for
SELECT CONVERT(CHAR(50), id) AS id FROM TEST_DATA WHERE status = 'ACTIVE';
