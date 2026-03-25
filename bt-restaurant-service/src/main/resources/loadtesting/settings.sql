-- CREATE EXTENSION pg_stat_statements;
-- SHOW shared_preload_libraries;

EXPLAIN ANALYZE
select * from restaurants;

SELECT pg_stat_statements_reset();
SELECT query, calls, total_exec_time, mean_exec_time, rows
FROM pg_stat_statements;