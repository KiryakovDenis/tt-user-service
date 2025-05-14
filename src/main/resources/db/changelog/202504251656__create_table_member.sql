CREATE TABLE tt_users."member"(
    team_id integer,
    user_id integer,
    created_at timestamp,
    updated_at timestamp,
    deleted_at timestamp,
    CONSTRAINT member_uk1 UNIQUE (team_id, user_id, deleted_at),
    CONSTRAINT member_team_fk FOREIGN KEY (team_id)
        REFERENCES tt_users.team(id),
    CONSTRAINT member_user_fk FOREIGN KEY (user_id)
        REFERENCES tt_users."user"(id)
);

COMMENT ON TABLE tt_users."member" IS 'Пользователи входящие в команду';

CREATE INDEX member_team_fki ON tt_users."member"(team_id);
CREATE INDEX member_user_fki ON tt_users."member"(user_id);

CREATE TRIGGER trg_member_ins_upd BEFORE INSERT OR UPDATE ON tt_users."member" FOR EACH ROW EXECUTE FUNCTION tt_users.trg_datetime_fields_ins_upd();