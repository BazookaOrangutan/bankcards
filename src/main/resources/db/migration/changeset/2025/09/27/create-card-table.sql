--liquibase formatted sql
--changeset Nickolay:3


CREATE TABLE card
(
    id uuid NOT NULL,
    balance numeric(38,2) NOT NULL DEFAULT 0.00,
    card_number character varying(255) NOT NULL,
    expiry_date date NOT NULL,
    status character varying(30) NOT NULL,
    user_id uuid NOT NULL,
    card_number_hash character varying(255) NOT NULL,
    CONSTRAINT card_pkey PRIMARY KEY (id),
    CONSTRAINT card_card_number_hash_uk UNIQUE (card_number_hash),
    CONSTRAINT card_user_id_fk FOREIGN KEY (user_id)
    REFERENCES _user (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT card_status_check CHECK (status IN ('ACTIVE', 'BLOCKED', 'EXPIRED', 'BLOCK_REQUESTED'))
    );

CREATE INDEX idx_card_user_id ON card (user_id);
CREATE INDEX idx_card_status ON card (status);

--rollback drop table card;