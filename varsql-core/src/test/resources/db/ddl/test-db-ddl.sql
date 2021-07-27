/* DROP TABLE t_eplog; */
CREATE TABLE zz_test_log (
	flgcd varchar(32) NOT NULL,
	flgcd_rstvl varchar(255) NOT NULL,
	ca_uri varchar(1500),
	usr_ip varchar(30),
	usr_id varchar(20),
	yr NUMERIC(10, 0) NOT NULL,
	yymm NUMERIC(10, 0) NOT NULL,
	yymmdd NUMERIC(10, 0) NOT NULL,
	yymmddhh NUMERIC(10, 0) NOT NULL,
	reg_id varchar(20) NOT NULL,
	reg_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	upd_id varchar(20) NOT NULL,
	upd_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
) 