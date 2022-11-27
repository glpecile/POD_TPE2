# TPE2-POD-G3

- [TPE2-POD-G3](#tpe2-pod-g3)
  - [Autores](#autores)
  - [Dependencias](#dependencias)
  - [Compilación](#compilación)
  - [Ejecución](#ejecución)
    - [Server](#server)
    - [Query 1](#query-1)
    - [Query 2](#query-2)
    - [Query 3](#query-3)
    - [Query 4](#query-4)
    - [Query 5](#query-5)
  - [Informe](#informe)
  - [Correcciones](#correcciones)

## Autores

- [Arce, Julián Francisco](https://github.com/JuArce)
- [Catalán, Roberto José](https://github.com/rcatalan98)
- [Dell'Isola, Lucas](https://github.com/ldellisola)
- [Pecile, Gian Luca](https://github.com/glpecile)
- [Torrusio, Lucía](https://github.com/luciatorrusio)

## Dependencias

- [Apache Maven](https://maven.apache.org/)
- [Java 18](https://www.oracle.com/java/technologies/javase-jdk18-downloads.html)

## Compilación

Para compilar el proyecto, se debe ejecutar el siguiente comando en la raíz del proyecto:

```bash
mvn clean install
```

## Ejecución

Para la ejecución del proyecto, es necesario ejecutar los siguientes comandos:

```bash
chmod u+x /scripts/*
./scripts/server.sh [...params]
./scripts/query[x].sh [...params]
```

El servidor se corre en una terminal y el cliente en otra.

Donde `[x]` es un número entre 1 y 5 perteniente a la query deseada a ejecutar.
A su vez, `[...params]` son los parámetros para la query y se detallan a continuación:

### Server

Desde el directorio raíz del proyecto, ejecutar el siguiente comando:

```bash
./scripts/server.sh "-DnetworkInterfaces=<address>"
```

Donde:
| Parámetro | Descripción |
| --- | --- |
| `-DnetworkInterfaces` | Dirección IP de la interfaz de red a utilizar. |

### Query 1

Total de peatones por Sensor.

```bash
./scripts/query1.sh "-Daddresses=<address>:5701" -DinPath=./resources -DoutPath=.
```

Donde:
| Parámetro | Descripción |
| --- | --- |
| `-Daddresses` | Dirección IP del nodo que ejecuta el servidor. |
| `-DinPath` | Directorio donde se encuentran los archivos de entrada. |
| `-DoutPath` | Directorio donde se guardarán los archivos de salida. |

### Query 2

Total de Peatones por Momento de la Semana para cada año.

```bash
./scripts/query2.sh "-Daddresses=<address>:5701" -DinPath=./resources -DoutPath=.
```

Donde:
| Parámetro | Descripción |
| --- | --- |
| `-Daddresses` | Dirección IP del nodo que ejecuta el servidor. |
| `-DinPath` | Directorio donde se encuentran los archivos de entrada. |

### Query 3

La medición más alta de cada sensor (mayores a min peatones).

```bash
/scripts/query3.sh "-Daddresses=<address>:5701" -DinPath=./resources -DoutPath=. -Dmin=<min>
```

Donde:
| Parámetro | Descripción |
| --- | --- |
| `-Daddresses` | Dirección IP del nodo que ejecuta el servidor. |
| `-DinPath` | Directorio donde se encuentran los archivos de entrada. |
| `-DoutPath` | Directorio donde se guardarán los archivos de salida. |
| `-Dmin` | Valor mínimo de peatones para consultar. |

### Query 4

Top `n` sensores con mayor promedio mensual de peatones para un año en particular.

```bash
./scripts/query4.sh "-Daddresses=127.0.0.1:5701" -DinPath=./resources -DoutPath=. -Dn=<max_sensor> -Dyear=<year>
```

Donde:
| Parámetro | Descripción |
| --- | --- |
| `-Daddresses` | Dirección IP del nodo que ejecuta el servidor. |
| `-DinPath` | Directorio donde se encuentran los archivos de entrada. |
| `-DoutPath` | Directorio donde se guardarán los archivos de salida. |
| `-Dn` | Cantidad máxima de sensores a consultar. |
| `-Dyear` | Año a consultar. |

### Query 5

Pares de sensores que registran la misma cantidad de millones de peatones.

```bash
./scripts/query5.sh "-Daddresses=127.0.0.1:5701" -DinPath=./resources -DoutPath=.
```

Donde:
| Parámetro | Descripción |
| --- | --- |
| `-Daddresses` | Dirección IP del nodo que ejecuta el servidor. |
| `-DinPath` | Directorio donde se encuentran los archivos de entrada. |
| `-DoutPath` | Directorio donde se guardarán los archivos de salida. |

## Informe

La presentación se encuentra en el [directorio `/docs` del proyecto](/docs/POD-TPE2-G3-Informe.pdf).

## Correcciones

### Destacado

- Buen informe, bien presentado los análisis de tiempos.
- Modularización de la API.


### Oportunidad de Mejora

- Month y Day son funcionalidades que se pueden obtener en el paquete java.time
- Q3: Utilizar el encabezado esperado.
- Q3: Forzar la impresión de las horas con dos dígitos.

### Errores

- Q4: No está truncando los valores.
