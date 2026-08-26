--create exception trace table

CREATE TABLE IF NOT EXISTS exception_trace (
	exception_class varchar(255) NULL,
	exception_id varchar(255) NOT NULL,
	exception_message varchar(255) NULL,
	stack_trace varchar(255) NULL,
	trace_id varchar(255) NULL,
	"time_stamp" timestamp(6) NULL,
	CONSTRAINT exception_trace_pkey PRIMARY KEY (exception_id)
);

--create http trace table

CREATE TABLE IF NOT EXISTS http_trace (
	client_ip varchar(255) NULL,
	duration_ms int8 NULL,
	http_id varchar(255) NOT NULL,
	"method" varchar(255) NULL,
	"path" varchar(255) NULL,
	status varchar(255) NULL,
	trace_id varchar(255) NULL,
	user_agent varchar(255) NULL,
	CONSTRAINT http_trace_pkey PRIMARY KEY (http_id)
);

--create geo co-ordinate table

CREATE TABLE IF NOT EXISTS user_geo_coordinate (
	geo_location_id varchar(255) NOT NULL,
	city varchar(255) NULL,
	client_ip varchar(255) NULL UNIQUE,
	continent_code varchar(255) NULL,
	continent_name varchar(255) NULL,
	country varchar(255) NULL,
	country_code varchar(255) NULL,
	latitude varchar(255) NULL,
	longitude varchar(255) NULL,
	region varchar(255) NULL,
	time_zone varchar(255) NULL,
	trace_id varchar(255) NULL,
	zip_code varchar(255) NULL,
	traffic_count int8 DEFAULT 1 NULL,
	CONSTRAINT user_geo_coordinate_pkey PRIMARY KEY (geo_location_id)
);


--create log trace table

CREATE TABLE IF NOT EXISTS  log_trace (
	environment varchar(255) NULL,
	event_type varchar(255) NULL,
	exception_trace_id varchar(255) NULL,
	host varchar(255) NULL,
	http_trace_id varchar(255) NULL,
	instance_id varchar(255) NULL,
	"level" varchar(255) NULL,
	log_id varchar(255) NOT NULL,
	logger varchar(255) NULL,
	message varchar(255) NULL,
	region varchar(255) NULL,
	service_name varchar(255) NULL,
	span_id varchar(255) NULL,
	thread varchar(255) NULL,
	"time_stamp" timestamp NOT NULL,
	time_zone varchar(255) NULL,
	trace_id varchar(255) NOT NULL,
	"version" varchar(255) NULL,
	incoming_time time(0) NULL,
-- 	coordinate_trace_id varchar(255) NULL,
	operation_status varchar(255) DEFAULT 'COMPLETE'::character varying NULL,
	CONSTRAINT log_trace_event_type_check CHECK (((event_type)::text = ANY ((ARRAY['APPLICATION_LOG'::character varying, 'API_REQUEST'::character varying, 'EXCEPTION'::character varying, 'CUSTOM_EVENT'::character varying])::text[]))),
	CONSTRAINT log_trace_exception_trace_id_key UNIQUE (exception_trace_id),
	CONSTRAINT log_trace_http_trace_id_key UNIQUE (http_trace_id),
	CONSTRAINT log_trace_level_check CHECK (((level)::text = ANY ((ARRAY['TRACE'::character varying, 'DEBUG'::character varying, 'INFO'::character varying, 'WARN'::character varying, 'ERROR'::character varying, 'FATAL'::character varying])::text[]))),
	CONSTRAINT log_trace_pkey PRIMARY KEY (log_id, time_stamp, trace_id)
--     ,CONSTRAINT uk9crje0ys3yfm7v1pxo7i0r7v1 UNIQUE (coordinate_trace_id)
);

ALTER TABLE log_trace ADD CONSTRAINT fk6sulwel397lykfnigdwoht263 FOREIGN KEY (exception_trace_id) REFERENCES exception_trace(exception_id);
ALTER TABLE log_trace ADD CONSTRAINT fk9vxe8qbruagm17tfu3npg4xwi FOREIGN KEY (http_trace_id) REFERENCES http_trace(http_id);
-- ALTER TABLE log_trace ADD CONSTRAINT fkediabh9rgpgyrbln73lhy3tkh FOREIGN KEY (coordinate_trace_id) REFERENCES user_geo_coordinate(geo_location_id);

