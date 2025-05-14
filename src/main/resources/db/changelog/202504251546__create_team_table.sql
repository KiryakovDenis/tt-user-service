CREATE TABLE tt_users.team (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    owner_team INTEGER NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT team_uk1 UNIQUE (name)
);

COMMENT ON TABLE tt_users.team IS 'Информация о команде';
COMMENT ON COLUMN tt_users.team.name IS 'Наименование команды';
COMMENT ON COLUMN tt_users.team.created_at IS 'Время создания записи (заполняется автоматически триггером бд)';
COMMENT ON COLUMN tt_users.team.updated_at IS 'Время обновления записи (заполняется автоматически триггером бд)';

CREATE TRIGGER trg_team_ins_upd BEFORE INSERT OR UPDATE ON tt_users."team" FOR EACH ROW EXECUTE FUNCTION tt_users.trg_team_ins_upd();