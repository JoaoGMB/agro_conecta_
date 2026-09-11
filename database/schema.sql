CREATE DATABASE agroconecta;
USE agroconecta;

-- Produtores
CREATE TABLE produtor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco VARCHAR(200)
);

-- Terrenos
CREATE TABLE terreno (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produtor_id INT NOT NULL,
    localizacao VARCHAR(150),
    tamanho_hectares DECIMAL(10,2),
    qualidade_solo VARCHAR(100),

    FOREIGN KEY (produtor_id) REFERENCES produtor(id)
);

-- Plantios
CREATE TABLE plantio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    terreno_id INT NOT NULL,
    data_inicio DATE NOT NULL,
    data_termino DATE,
    status VARCHAR(30),

    FOREIGN KEY (terreno_id) REFERENCES terreno(id)
);

-- Mudas
CREATE TABLE muda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plantio_id INT NOT NULL,
    quantidade INT NOT NULL,
    genetica VARCHAR(100),
    valor_aquisicao DECIMAL(10,2),

    FOREIGN KEY (plantio_id) REFERENCES plantio(id)
);

-- Monitoramentos
CREATE TABLE monitoramento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plantio_id INT NOT NULL,
    data_registro DATE NOT NULL,
    tipo VARCHAR(30),
    descricao VARCHAR(255),
    volume_chuva DECIMAL(10,2),

    FOREIGN KEY (plantio_id) REFERENCES plantio(id)
);

-- Colheitas
CREATE TABLE colheita (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plantio_id INT NOT NULL,
    quantidade_colhida DECIMAL(10,2),
    tempo_secagem INT,
    qualidade VARCHAR(100),

    FOREIGN KEY (plantio_id) REFERENCES plantio(id)
);

-- Vendas
CREATE TABLE venda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    colheita_id INT NOT NULL,
    quantidade DECIMAL(10,2),
    valor DECIMAL(10,2),
    data_venda DATE,

    FOREIGN KEY (colheita_id) REFERENCES colheita(id)
);

-- Avisos
CREATE TABLE aviso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produtor_id INT NOT NULL,
    descricao VARCHAR(255),
    concluido BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (produtor_id) REFERENCES produtor(id)
);
