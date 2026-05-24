CREATE DATABASE Transportes_Hirata;

USE Transportes_Hirata;


CREATE TABLE Conductor (
    idConductor INT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(12) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    licencia VARCHAR(50) NOT NULL,
    telefono VARCHAR(20),
    clave VARCHAR(50) NOT NULL 
);


CREATE TABLE Camion (
    idCamion INT AUTO_INCREMENT PRIMARY KEY,
    patente VARCHAR(10) UNIQUE NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    kilometrajeActual INT NOT NULL DEFAULT 0,
    idConductor INT, 
    FOREIGN KEY (idConductor) REFERENCES Conductor(idConductor) ON DELETE SET NULL 
);


CREATE TABLE Mantenimiento (
    idMantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    tipo VARCHAR(20) NOT NULL, 
    descripcion TEXT NOT NULL,
    kilometrajeMantenimiento INT NOT NULL,
    idCamion INT NOT NULL, 
    FOREIGN KEY (idCamion) REFERENCES Camion(idCamion) ON DELETE CASCADE
);
select * from equipooficina;
DROP TABLE IF EXISTS `equipooficina`;
CREATE TABLE EquipoOficina (
    idEquipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50),
    marca VARCHAR(50),
    modelo VARCHAR(50),
    identificador INT,
    estado VARCHAR(30)
);
select * from mantenimientoequipooficina;
DROP TABLE IF EXISTS `mantenimientoequipooficina`;
-- Tabla de mantenimientos ligada a la tabla EquipoOficina que ya definiste
CREATE TABLE MantenimientoEquipoOficina (
  idMantenimiento INT AUTO_INCREMENT PRIMARY KEY,
  fecha DATE NOT NULL,
  tipo VARCHAR(50) NOT NULL,
  descripcion TEXT,
  observaciones TEXT,
  idEquipo INT NOT NULL,
  CONSTRAINT fk_mantenimiento_equipo FOREIGN KEY (idEquipo)
    REFERENCES EquipoOficina(idEquipo)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  INDEX idx_fecha (fecha),
  INDEX idx_idEquipo (idEquipo)
);

