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

CREATE TABLE EquipoOficina (
    idEquipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50),
    marca VARCHAR(50),
    modelo VARCHAR(50),
    identificador INT,
    estado VARCHAR(30)
);

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

CREATE TABLE PiezaInventario (
    idPieza INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    cantidad INT NOT NULL DEFAULT 0,
    stockMinimo INT NOT NULL DEFAULT 0,
    ubicacion VARCHAR(100)
);

CREATE TABLE MantenimientoEquipoPieza (
    idMantenimientoEquipoPieza INT AUTO_INCREMENT PRIMARY KEY,
    idMantenimientoEquipo INT NOT NULL,
    idPieza INT NOT NULL,
    cantidadUsada INT NOT NULL,
    CONSTRAINT fk_mep_mantenimiento FOREIGN KEY (idMantenimientoEquipo)
        REFERENCES MantenimientoEquipoOficina(idMantenimiento)
        ON DELETE CASCADE,
    CONSTRAINT fk_mep_pieza FOREIGN KEY (idPieza)
        REFERENCES PiezaInventario(idPieza),
    INDEX idx_idMantenimientoEquipo (idMantenimientoEquipo),
    INDEX idx_idPieza (idPieza)
);

CREATE TABLE SoftwareEquipo (
    idSoftware         INT AUTO_INCREMENT PRIMARY KEY,
    idEquipo           INT          NOT NULL,
    nombreSoftware     VARCHAR(100) NOT NULL,
    version            VARCHAR(50)  NOT NULL,
    fechaInstalacion   DATE         NOT NULL,
    fechaActualizacion DATE,
    observaciones      VARCHAR(255),
    FOREIGN KEY (idEquipo) REFERENCES EquipoOficina(idEquipo) ON DELETE CASCADE
);

INSERT INTO Conductor (rut, nombre, licencia, telefono, clave) VALUES
('12345678-9', 'Carlos Hirata',  'A2', '+56912345678', '1234'),
('98765432-1', 'Ana González',   'B',  '+56987654321', '1234'),
('11111111-1', 'Pedro Martínez', 'A2', '+56911111111', '1234');

INSERT INTO Camion (patente, marca, modelo, anio, kilometrajeActual, idConductor) VALUES
('ABCD12', 'Volvo',    'FH16',   2020, 150000, 1),
('EFGH34', 'Mercedes', 'Actros', 2019, 200000, 2),
('IJKL56', 'Scania',   'R450',   2021,  80000, 3),
('MNOP78', 'MAN',      'TGX',    2018, 310000, NULL);

INSERT INTO Mantenimiento (fecha, tipo, descripcion, kilometrajeMantenimiento, idCamion) VALUES
('2024-01-10', 'Preventivo', 'Cambio de aceite y filtros',        150000, 1),
('2024-02-15', 'Correctivo', 'Reparacion de frenos traseros',     198000, 2),
('2024-03-05', 'Preventivo', 'Revision general y neumaticos',      78000, 3),
('2023-11-20', 'Correctivo', 'Cambio de correa de distribucion',  305000, 4),
('2024-04-01', 'Preventivo', 'Cambio aceite motor y transmision', 152000, 1);

INSERT INTO EquipoOficina (nombre, tipo, marca, modelo, identificador, estado) VALUES
('PC Administracion', 'Computador', 'Dell',   'OptiPlex 7090',   1001, 'Activo'),
('Laptop Gerencia',   'Laptop',     'HP',     'EliteBook 840',   1002, 'Activo'),
('PC Recepcion',      'Computador', 'Lenovo', 'ThinkCentre M70', 1003, 'Activo'),
('Impresora Oficina', 'Impresora',  'Epson',  'EcoTank L3150',   1004, 'Activo'),
('PC Bodega',         'Computador', 'Dell',   'OptiPlex 3080',   1005, 'En reparacion');

INSERT INTO MantenimientoEquipoOficina (fecha, tipo, descripcion, observaciones, idEquipo) VALUES
('2024-01-20', 'Preventivo', 'Limpieza interna y actualizacion SO',  'Sin inconvenientes',             1),
('2024-02-10', 'Correctivo', 'Reemplazo de disco duro',              'Disco con sectores defectuosos', 2),
('2024-03-15', 'Preventivo', 'Limpieza de cabezales impresora',      'Toner al 30%',                   4),
('2024-04-05', 'Correctivo', 'Reparacion tarjeta madre',             'En espera de repuesto',          5),
('2024-04-18', 'Preventivo', 'Actualizacion de drivers y antivirus', NULL,                             3);

INSERT INTO PiezaInventario (nombre, descripcion, cantidad, stockMinimo, ubicacion) VALUES
('SSD 512GB', 'Unidad de estado solido para notebooks y PCs', 5, 2, 'Bodega TI'),
('Memoria RAM DDR4 8GB', 'Modulo de memoria para notebook o desktop', 8, 3, 'Bodega TI'),
('Teclado notebook HP', 'Repuesto de teclado para EliteBook', 2, 1, 'Bodega TI'),
('Cable USB impresora', 'Cable USB tipo B para impresoras', 10, 4, 'Oficina soporte'),
('Pasta termica', 'Pasta termica para mantenimiento preventivo', 6, 2, 'Kit tecnico');

INSERT INTO MantenimientoEquipoPieza (idMantenimientoEquipo, idPieza, cantidadUsada) VALUES
(2, 1, 1),
(3, 5, 1),
(5, 2, 1);

INSERT INTO SoftwareEquipo (idEquipo, nombreSoftware, version, fechaInstalacion, fechaActualizacion, observaciones) VALUES
(1, 'Microsoft Office',    '2021',  '2023-01-15', '2024-03-10', 'Licencia corporativa'),
(1, 'Antivirus Kaspersky', '21.3',  '2023-02-01', '2024-01-20', 'Renovacion anual pendiente'),
(1, 'Google Chrome',       '120.0', '2022-06-10', '2024-02-28', NULL),
(2, 'Microsoft Office',    '2019',  '2022-08-20', '2023-11-05', 'Version antigua, actualizar'),
(2, 'Adobe Acrobat',       '23.0',  '2023-05-12', NULL,         'Solo lectura de PDF'),
(2, 'Zoom',                '5.16',  '2023-03-01', '2024-01-15', 'Para reuniones remotas'),
(3, 'Windows 11',          '22H2',  '2023-07-01', '2024-02-01', 'Actualizacion automatica activa'),
(3, 'Antivirus Kaspersky', '21.3',  '2023-07-01', NULL,         NULL),
(3, 'Slack',               '4.35',  '2023-09-10', '2024-03-01', 'Comunicacion interna');

