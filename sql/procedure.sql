

drop PROCEDURE if exists user_work_count;

create procedure user_work_count(in p_com_id bigint(20), in p_begin_date varchar(20), in p_end_date varchar(20), in p_verify_status varchar(10))
begin
  declare term_id bigint(20);
  declare term_name varchar(250);
  declare count int;
  declare i int default 0;
  set @s = 'select t.user_id, t.nick_name';
  set count = (select count(1) from yy_term where com_id = p_com_id and status = 1);
  while i < count do
    set term_id = (select id from yy_term where com_id = p_com_id and status = 1 order by id limit i, 1);
    set term_name = (select name from yy_term where com_id = p_com_id and status = 1 order by id limit i, 1);
    set @s = concat(@s, ',sum(ifnull(case term_id when ', term_id, ' then count end, 0)) as \'', term_name, '\'');
    set i = i + 1;
end while;
  set @s = concat(
    @s,
    ' from (select ro.user_id, u.nick_name, r.term_id, count(1) as count from yy_reserve_operator ro',
    ' inner join yy_reserve r on ro.reserve_id = r.id',
    ' inner join sys_user u on ro.user_id = u.user_id',
    ' where r.com_id = ', p_com_id, ' and r.date >= \'', p_begin_date, '\' and r.date <= \'', p_end_date, '\' and r.status = 1 and r.verify_status = \'', p_verify_status, '\'',
    ' group by ro.user_id, u.nick_name, r.term_id) t',
    ' group by t.user_id, t.nick_name'
  );
prepare stmt from @s;
execute stmt;
end;

# test
# call user_work_count(32, '2021-03-01', '2021-03-31', 'verified');