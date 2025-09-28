--liquibase formatted sql
--changeset Nickolay:1

CREATE TABLE user_details
(
    id uuid NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255)  NOT NULL,
    role character varying(10) NOT NULL,
    username character varying(16) NOT NULL,
    CONSTRAINT user_details_pkey PRIMARY KEY (id),
    CONSTRAINT user_details_email_uk UNIQUE (email),
    CONSTRAINT user_details_username_uk UNIQUE (username),
    CONSTRAINT user_details_role_check CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN'))
);

--rollback drop table user_details;