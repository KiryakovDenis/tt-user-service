CREATE OR REPLACE FUNCTION tt_users.trg_user_ins_upd() RETURNS trigger AS $trg_user_ins_upd$
    BEGIN
        IF TG_OP = 'INSERT' THEN
            NEW.created_at := CURRENT_TIMESTAMP;
        ELSIF TG_OP = 'UPDATE' THEN
            NEW.updated_at := CURRENT_TIMESTAMP;
            IF NEW.is_deleted = TRUE THEN
                NEW.deleted_at := CURRENT_TIMESTAMP;
            END IF;
        END IF;
    RETURN NEW;
    END;
$trg_user_ins_upd$ LANGUAGE plpgsql;

CREATE TRIGGER trg_user_ins_upd
BEFORE INSERT OR UPDATE
    ON tt_users.user
FOR EACH ROW
EXECUTE FUNCTION tt_users.trg_user_ins_upd();