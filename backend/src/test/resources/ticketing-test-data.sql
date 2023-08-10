insert into festival (end_date, name, start_date, thumbnail)
values ('2023-07-30', '테코 대학교', '2023-08-02', '');

insert into stage (festival_id, line_up, start_time, ticket_open_time)
values (1, '', '2023-07-30T03:21:31.964676', '2023-07-23T03:21:31.964676');

insert into ticket (stage_id, ticket_type)
values (1, 'VISITOR');

insert into ticket_amount (ticket_id, reserved_amount, total_amount)
values (1, 0, 50);

insert into ticket_entry_time (amount, entry_time, ticket_id)
values (10, '2023-07-30T00:21:31.964676', 1);

insert into ticket_entry_time (amount, entry_time, ticket_id)
values (20, '2023-07-30T01:21:31.964676', 1);

insert into ticket_entry_time (amount, entry_time, ticket_id)
values (20, '2023-07-30T02:21:31.964676', 1);
