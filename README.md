# Transportes Hirata

Aplicacion Java Swing para gestionar soporte TI, mantenimiento de equipos de oficina, software instalado e inventario de repuestos.

## Configuracion de MySQL

El proyecto no guarda contrasenas reales en GitHub. Para ejecutar localmente:

1. Copia `hirata-db.example.properties` como `hirata-db.properties`.
2. Cambia `db.password` por la clave real de MySQL.
3. Deja el archivo en la raiz del repositorio o dentro de `Proyecto/`.

Ejemplo:

```properties
db.url=jdbc:mysql://localhost:3306/Transportes_Hirata
db.user=root
db.password=tu_password_local
```

Tambien puedes usar variables de entorno:

```text
HIRATA_DB_URL=jdbc:mysql://localhost:3306/Transportes_Hirata
HIRATA_DB_USER=root
HIRATA_DB_PASSWORD=tu_password_local
```

El archivo `hirata-db.properties` esta ignorado por Git para evitar filtrar credenciales.

## Base de datos

Puedes importar `TransportesHirata.sql` en MySQL. Si la base no existe, la aplicacion intenta crear la base y las tablas necesarias al iniciar.

## Ejecucion

El proyecto Maven esta en la carpeta `Proyecto/`. La clase principal configurada es:

```text
com.mycompany.transporteshirata.GUI.GuiLoginPrincipal
```
