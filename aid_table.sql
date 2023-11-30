

insert into aid_table (id, created_date, description, latitude, longitude, category_id) values (1, '2023-08-09 16:29:13', 'CREATING', 36.806669, 118.017938, 1);
insert into aid_table (id, created_date, description, latitude, longitude, category_id) values (2, '2022-12-05 17:04:32', 'CREATING', 52.0108, 86.5467, 2);
insert into aid_table (id, created_date, description, latitude, longitude, category_id) values (3, '2023-03-24 04:35:58', 'CREATING', 20.9570351, false, 3);
insert into aid_table (id, created_date, description, latitude, longitude, category_id) values (4, '2023-05-25 04:17:20', 'CREATING', 49.978837, 35.759315, 4);
insert into aid_table (id, created_date, description, latitude, longitude, category_id) values (5, '2023-04-30 13:05:06', 'CREATING', false, 106.7994022, 5);
insert into aid_table (id, created_date, description, latitude, longitude, category_id) values (6, '2023-01-02 03:11:33', 'CREATING', false, 128.1160213, 6);



insert into aid_table (aid_interaction, aid_id, user_id) values ('Creating', 1, 1);
insert into aid_table (aid_interaction, aid_id, user_id) values ('Creating', 2, 2);
insert into aid_table (aid_interaction, aid_id, user_id) values ('Creating', 3, 1);
insert into aid_table (aid_interaction, aid_id, user_id) values ('Creating', 4, 2);
insert into aid_table (aid_interaction, aid_id, user_id) values ('Creating', 5, 1);
insert into aid_table (aid_interaction, aid_id, user_id) values ('Creating', 6, 2);


CREATE TRIGGER eventInserter
AFTER INSERT ON aid_table
FOR EACH ROW 
BEGIN
    DECLARE category_count INT;
    
--     Check if there is no existing record with the same category_id in aid_table
--     SELECT COUNT(*) INTO category_count
--     FROM aid_table
--     WHERE category_id = NEW.category_id
--     AND id != NEW.id; -- Exclude the current record
    
    -- If no existing record found, insert into users_aids_table
--     IF aid_description = 0 THEN
        INSERT INTO users_aids_table (aid_interaction, aid_id, user_id)
        VALUES ('CREATING', NEW.id, 1);
--     END IF;
END;


CREATE TRIGGER eventInserter 
AFTER INSERT ON aid_table
FOR EACH ROW 
BEGIN
	
    INSERT INTO users_aids_table (aid_interaction, aid_id, user_id)
    VALUES ('CREATING', NEW.id, 1);
--     DECLARE categoryExists INT;
-- 
--     -- Check if a record exists in users_aids_table with the same category_id and 'CREATING' interaction
--     SELECT COUNT(*) INTO categoryExists 
--     FROM users_aids_table 
--     WHERE aid_id = NEW.id 
--     and
--     AND aid_interaction = 'CREATING';
-- 
--     -- If the record doesn't exist, insert a new record into users_aids_table
--     IF categoryExists = 0 THEN
-- 
--     ELSE
--         INSERT INTO users_aids_table (aid_interaction, aid_id, user_id)
--         VALUES ('MODIFYING', NEW.id, 1);
--     END IF;
END;
drop TRIGGER eventInserter;



