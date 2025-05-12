-- DROP FUNCTION tt_users.trg_datetime_fields_ins_upd();

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
                           WHERE a.table_name = TG_TABLE_NAME
                             AND a.column_name = 'created_at')
              INTO column_exist;

            IF column_exist THEN
                NEW.created_at := CURRENT_TIMESTAMP;
            END IF;
        ELSIF TG_OP = 'UPDATE' THEN
            SELECT EXISTS(SELECT 1
                            FROM information_schema.COLUMNS a
                           WHERE a.table_name = TG_TABLE_NAME
                             AND a.column_name = 'updated_at')
              INTO column_exist;

            IF column_exist THEN
                NEW.updated_at := CURRENT_TIMESTAMP;
            END IF;

            SELECT EXISTS(SELECT 1
                            FROM information_schema.COLUMNS a
                           WHERE a.table_name = TG_TABLE_NAME
                             AND (a.column_name = 'deleted_at')) AND
                   EXISTS(SELECT 1
                            FROM information_schema.COLUMNS a
                           WHERE a.table_name = TG_TABLE_NAME
                             AND (a.column_name = 'is_deleted'))
              INTO column_exist;

            IF column_exist THEN
              IF NEW.is_deleted THEN
                NEW.deleted_at := CURRENT_TIMESTAMP;
              END IF;
            END IF;

        END IF;
    RETURN NEW;
    END;
$function$
;