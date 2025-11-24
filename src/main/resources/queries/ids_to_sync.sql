-- ids_to_sync.sql : return numeric member ids to hydrate
SELECT member_id AS memberId FROM MASTER_TABLE WHERE status = 'ACTIVE';
