CREATE DATABASE IF NOT EXISTS agroconecta
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;
USE agroconecta;

CREATE USER IF NOT EXISTS 'agroconecta'@'localhost' IDENTIFIED BY 'agro123';
CREATE USER IF NOT EXISTS 'agroconecta'@'127.0.0.1' IDENTIFIED BY 'agro123';
GRANT ALL PRIVILEGES ON agroconecta.* TO 'agroconecta'@'localhost';
GRANT ALL PRIVILEGES ON agroconecta.* TO 'agroconecta'@'127.0.0.1';
FLUSH PRIVILEGES;


CREATE TABLE IF NOT EXISTS usuario (
    id_usuario    INT AUTO_INCREMENT PRIMARY KEY,
    nome          VARCHAR(150) NOT NULL,
    cpf           VARCHAR(14)  NOT NULL UNIQUE,
    telefone      VARCHAR(20),
    email         VARCHAR(150),
    senha         VARCHAR(255) NOT NULL,
    endereco      VARCHAR(255),
    tipo_usuario  VARCHAR(30)  NOT NULL
);

CREATE TABLE IF NOT EXISTS terreno (
    id_terreno        INT AUTO_INCREMENT PRIMARY KEY,
    id_produtor       INT NOT NULL,
    localizacao       VARCHAR(255),
    tamanho_hectares  DECIMAL(10,2),
    qualidade_solo    VARCHAR(100),
    CONSTRAINT fk_terreno_produtor FOREIGN KEY (id_produtor)
        REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS plantio (
    id_plantio       INT AUTO_INCREMENT PRIMARY KEY,
    id_terreno       INT NOT NULL,
    data_inicio      DATE NOT NULL,
    data_termino     DATE,
    status           VARCHAR(50),
    fotos_plantacao  VARCHAR(500),
    CONSTRAINT fk_plantio_terreno FOREIGN KEY (id_terreno)
        REFERENCES terreno(id_terreno) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS muda (
    id_muda          INT AUTO_INCREMENT PRIMARY KEY,
    id_plantio       INT NOT NULL,
    quantidade       INT NOT NULL,
    genetica         VARCHAR(150),
    valor_aquisicao  DECIMAL(10,2),
    CONSTRAINT fk_muda_plantio FOREIGN KEY (id_plantio)
        REFERENCES plantio(id_plantio) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS registro_monitoramento (
    id_monitoramento    INT AUTO_INCREMENT PRIMARY KEY,
    id_plantio          INT NOT NULL,
    data_registro       DATE NOT NULL,
    observacao          TEXT,
    tipo_monitoramento  VARCHAR(30) NOT NULL,
    CONSTRAINT fk_monit_plantio FOREIGN KEY (id_plantio)
        REFERENCES plantio(id_plantio) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS registro_praga (
    id_monitoramento  INT PRIMARY KEY,
    tipo_praga        VARCHAR(150),
    nivel_infestacao  VARCHAR(50),
    CONSTRAINT fk_praga_monit FOREIGN KEY (id_monitoramento)
        REFERENCES registro_monitoramento(id_monitoramento) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS registro_doenca (
    id_monitoramento  INT PRIMARY KEY,
    tipo_doenca       VARCHAR(150),
    nivel_gravidade   VARCHAR(50),
    CONSTRAINT fk_doenca_monit FOREIGN KEY (id_monitoramento)
        REFERENCES registro_monitoramento(id_monitoramento) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS registro_clima (
    id_monitoramento  INT PRIMARY KEY,
    volume_chuva      DECIMAL(10,2),
    CONSTRAINT fk_clima_monit FOREIGN KEY (id_monitoramento)
        REFERENCES registro_monitoramento(id_monitoramento) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS colheita (
    id_colheita         INT AUTO_INCREMENT PRIMARY KEY,
    id_plantio          INT NOT NULL,
    data_colheita       DATE,
    quantidade_colhida  DECIMAL(10,2),
    tempo_secagem       DECIMAL(10,2),
    qualidade_produto   VARCHAR(100),
    CONSTRAINT fk_colheita_plantio FOREIGN KEY (id_plantio)
        REFERENCES plantio(id_plantio) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS venda (
    id_venda               INT AUTO_INCREMENT PRIMARY KEY,
    id_colheita            INT NOT NULL,
    quantidade_disponivel  DECIMAL(10,2),
    quantidade_vendida     DECIMAL(10,2),
    valor                  DECIMAL(10,2),
    data_venda             DATE,
    CONSTRAINT fk_venda_colheita FOREIGN KEY (id_colheita)
        REFERENCES colheita(id_colheita) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS aviso (
    id_aviso          INT AUTO_INCREMENT PRIMARY KEY,
    id_produtor       INT NOT NULL,
    descricao         VARCHAR(255),
    data_aviso        DATE,
    status_conclusao  VARCHAR(30),
    CONSTRAINT fk_aviso_produtor FOREIGN KEY (id_produtor)
        REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

INSERT IGNORE INTO usuario (nome, cpf, telefone, email, senha, endereco, tipo_usuario)
VALUES ('Administrador', '000.000.000-00', NULL, 'admin@agroconecta.local',
        '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
        NULL, 'ADMINISTRADOR');
