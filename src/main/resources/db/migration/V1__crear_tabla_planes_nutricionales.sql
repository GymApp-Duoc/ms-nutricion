
CREATE TABLE planes_nutricionales (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      miembro_id BIGINT NOT NULL,
                                      nutricionista_id BIGINT NOT NULL,
                                      fecha_inicio DATE NOT NULL,
                                      objetivo VARCHAR(100) NOT NULL,
                                      calorias_diarias INT NOT NULL,
                                      detalle_menu VARCHAR(1000),
                                      activo BOOLEAN NOT NULL DEFAULT TRUE
);

