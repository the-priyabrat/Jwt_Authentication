
--enum for operation status
-- CREATE TYPE operation_status AS ENUM (
--     'PENDING',
--     'PROCESSING',
--     'COMPLETE',
--     'FAILED',
--     'MAXIMUM_RETRY_FAILED'
-- );

ALTER TABLE log_trace RENAME COLUMN operation_status TO elastic_operation_status;

--change the data type of elastic operation status
-- ALTER TABLE log_trace
-- ALTER COLUMN elastic_operation_status TYPE operation_status
-- USING elastic_operation_status::operation_status;

ALTER TABLE log_trace ADD COLUMN geo_location_operation_status varchar(255) NOT NULL DEFAULT 'PENDING';