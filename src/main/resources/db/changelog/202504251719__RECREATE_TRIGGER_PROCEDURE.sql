CREATE OR REPLACE FUNCTION tt_users.trg_datetime_fields_ins_upd()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
    DECLARE
      column_exist boolean;
    BEGIN
        IF TG_OP = 'INSERT' THEN
            SELECT EXISTS(SELECT 1
                            FROM information_schema.COLUMNS a
                           WHERE a.table_name = 'user'
                             AND a.column_name = 'created_at')
              INTO column_exist;

            IF column_exist THEN
                NEW.created_at := CURRENT_TIMESTAMP;
            END IF;
        ELSIF TG_OP = 'UPDATE' THEN
            SELECT EXISTS(SELECT 1
                            FROM information_schema.COLUMNS a
                           WHERE a.table_name = 'user'
                             AND a.column_name = 'updated_at')
              INTO column_exist;

            IF column_exist THEN
                NEW.updated_at := CURRENT_TIMESTAMP;
            END IF;

            SELECT EXISTS(SELECT 1
                            FROM information_schema.COLUMNS a
                           WHERE a.table_name = 'user'
                             AND a.column_name = 'deleted_at')
              INTO column_exist;

            IF NEW.is_deleted AND column_exist THEN
                NEW.deleted_at := CURRENT_TIMESTAMP;
            END IF;
        END IF;
    RETURN NEW;
    END;
$function$
;

DROP TRIGGER trg_user_ins_upd ON tt_users."user";
CREATE TRIGGER trg_user_ins_upd BEFORE INSERT OR UPDATE ON tt_users."user" FOR EACH ROW EXECUTE FUNCTION tt_users.trg_datetime_fields_ins_upd();

DROP TRIGGER trg_member_ins_upd ON tt_users."member";
CREATE TRIGGER trg_member_ins_upd BEFORE INSERT OR UPDATE ON tt_users."member" FOR EACH ROW EXECUTE FUNCTION tt_users.trg_datetime_fields_ins_upd();

DROP TRIGGER trg_team_ins_upd ON tt_users."team";
CREATE TRIGGER trg_team_ins_upd BEFORE INSERT OR UPDATE ON tt_users."team" FOR EACH ROW EXECUTE FUNCTION tt_users.trg_datetime_fields_ins_upd();