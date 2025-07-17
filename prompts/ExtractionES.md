Analiza el código de la aplicación y Genera un reporte en Excel con lo siguiente:
* Inventario de Archivos
* Inventario de Interacciones SQL, detecta los modos posibles de conexión y funciones para ejecutar sentencias o queries). Así como la complejidad del los queries basado en el tamaño.
* Inventario de: require a file: require("file path/name"); y dónde es usado.
* Inventario de: include a file : include("file path/name"); y dónde es usado.
* Inventario de: to make calls to coldfusion and authentication: curl_init, curl_setopt, curl_exec y dónde es usado.
* Inventario de: define function: function fucntionName(){} y dónde es usado. Complejidad de acuerdo a las líneas de código
* Inventario de: define a class: class ClassName{} y dónde es usado.
* Inventario de: inherit a trit: use y dónde es usado.
* Inventario de: inherit a class: extends y dónde es usado.
* Inventario de: implements an interface: implements y dónde es usado.
* Inventario de: Define an interface: interface InterfaceName y dónde es usado.

Reglas de análisis:
* El reporte de inventarios del contenido y hojas de Excel deben ser en inglés, de la misma manera Generar un archivo Excel con una hoja Categoría en lugar de crear un único archivo con todo. Por Ejemplo:

* Inventario de Archivos - Hoja FilesReport
* Inventario de Queries - Hoja QueriesReport
  * Formato: 
QueryName | DB Table | File:Line | Execution Count | Data Source | SQL Query | Complexity
* Inventario de: requires - Hoja phpRequiresReport
* Inventario de: includes - Hoja phpIncludesReport
* Inventario de: calls - Hoja phpCallsReport
* Inventario de: function - Hoja phpFunctionsReport
* Inventario de: class - Hoja phpClassesReport
* Inventario de: inherit a trit:  - Hoja phpInheritsFromTritReport
* Inventario de: inherit a class:  - Hoja phpInheritFromClassReport
* Inventario de: implements an interface:  - Hoja phpImplementsReport
* Inventario de: Define an interface:  - Hoja phpInterfaceReport

Generando así un archivo Excel “IX_CodeAnalysis_$TimeStamp$.xlsx con 11 hojas/reportes, en caso de no encontrar contenido para alguno de los archivos no generar dicho reporte y notificar en el Log y consola.
