insert into VTUSER ( viewid ,uid ,upw ,uname ,org_nm ,dept_nm ,uemail ,user_role ,description ,reg_id ,upd_id , accept_yn)
values(1 ,'varsqladmin','VARTECH:Vv3XDs8QOO7MASjFmb7+4lW86teUlKfW2hBHcg9BJNI=','관리자','orgnm','dept_nm','admin@varsql.com','ADMIN','administrator','varsqladmin','varsqladmin','Y');

insert into VTUSER ( viewid ,uid ,upw ,uname ,org_nm ,dept_nm ,uemail ,user_role ,description ,reg_id ,upd_id , accept_yn)
values(2 ,'user','VARTECH:WEBI83xrH4Uu720MwZE8V35dtNc5b2BDPdimQi+8An0=','user','userorgnm','userdept_nm','user@varsql.com','USER','administrator','varsqladmin','varsqladmin','Y');

insert into VTUSER ( viewid ,uid ,upw ,uname ,org_nm ,dept_nm ,uemail ,user_role ,description ,reg_id ,upd_id , accept_yn)
values(3 ,'manager','VARTECH:7cLiwtQyCQkkeuYireKawc+vvHKL0upLaxgDra7xWmM=','manager','manageorgnm','managedept_nm','manager@varsql.com','MANAGER','administrator','varsqladmin','varsqladmin','Y');


insert into VTDBTYPE (typeid ,name ,langkey ,urlprefix)
values('001' ,'db2','dbtype.db2','db2');
insert into VTDBTYPE (typeid ,name ,langkey ,urlprefix)
values('002' ,'oracle','dbtype.oracle','oracle');
insert into VTDBTYPE (typeid ,name ,langkey ,urlprefix)
values('003' ,'mysql','dbtype.mysql','mysql');
insert into VTDBTYPE (typeid ,name ,langkey ,urlprefix)
values('004' ,'mssql','dbtype.mssql','mssql');
insert into VTDBTYPE (typeid ,name ,langkey ,urlprefix)
values('005' ,'other','dbtype.other','other');
insert into VTDBTYPE (typeid ,name ,langkey ,urlprefix)
values('006' ,'h2','dbtype.h2','h2');
insert into VTDBTYPE (typeid ,name ,langkey ,urlprefix)
values('007' ,'sybase','dbtype.sybase','sybase');
insert into VTDBTYPE (typeid ,name ,langkey ,urlprefix)
values('008' ,'postgresql','dbtype.postgresql','postgresql');
insert into VTDBTYPE (typeid ,name ,langkey ,urlprefix)
values('009' ,'mariadb','dbtype.mariadb','mariadb');
insert into VTDBTYPE (typeid ,name ,langkey ,urlprefix)
values('010' ,'tibero','dbtype.tibero','tibero');