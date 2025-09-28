--liquibase formatted sql
--changeset Nickolay:2

CREATE TABLE _user
(
    id uuid NOT NULL,
    user_details_id uuid NOT NULL,
    name character varying(255) NOT NULL,
    CONSTRAINT _user_pkey PRIMARY KEY (id),
    CONSTRAINT _user_user_details_id_uk UNIQUE (user_details_id),
    CONSTRAINT _user_user_details_id_fk FOREIGN KEY (user_details_id)
    REFERENCES user_details (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);

CREATE INDEX idx_user_user_details_id ON _user (user_details_id);

--rollback drop table _user;