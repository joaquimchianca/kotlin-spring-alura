CREATE TABLE role(
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100),
    PRIMARY KEY(id)
);

INSERT INTO role VALUES(1, 'LEITURA_ESCRITA');
INSERT INTO role VALUES(2, 'LEITURA');