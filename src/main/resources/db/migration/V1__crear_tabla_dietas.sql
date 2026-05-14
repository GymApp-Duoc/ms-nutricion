CREATE TABLE dietas (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        miembro_id BIGINT NOT NULL UNIQUE,
                        tipo_dieta VARCHAR(100) NOT NULL,
                        calorias INT NOT NULL,
                        proteinas INT NOT NULL,
                        carbohidratos INT NOT NULL,
                        grasas INT NOT NULL
);
