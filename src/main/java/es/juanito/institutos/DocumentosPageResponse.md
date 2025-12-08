 Funcionamiento de la Paginación en API REST (Spring Boot)
 
En una arquitectura de servicios REST con Spring Boot, implementar una paginación robusta requiere tres componentes clave: la Entidad Page<T> de Spring Data, el DTO PageResponse<T> (el Contrato de Salida) y el PaginationLinksUtils (el Generador de Navegación).

1.  Spring Data JPA: El Motor de Paginación (Page<T>)
   El punto de partida es el objeto nativo que Spring Data JPA devuelve cuando se consulta un repositorio con un Pageable.

Clase: org.springframework.data.domain.Page<T>

Función: Esta interfaz es la responsable de obtener los datos limitados y, crucialmente, de realizar una segunda consulta automática para contar el total de elementos y el total de páginas.

Contenido:

getContent(): La lista de elementos de la página actual.

getTotalElements(): Número total de registros en el sistema.

getTotalPages(): El número total de páginas posibles.

getNumber(): Índice de la página actual (base 0).

getSize(): Número de elementos por página.

2. 📝 PageResponse: El Contrato de Respuesta (PageResponse<T>)
   El PageResponse<T> (o PageResponseDto) es la clase que se utiliza en el Controlador para serializar la respuesta JSON final que se envía al cliente. Su propósito es simplificar la compleja estructura de Page<T> y enriquecerla con enlaces de navegación.

Función: Actuar como un Contrato de Interfaz para la paginación, empaquetando los datos esenciales de la paginación de forma limpia y predecible.

Uso: Evita exponer la interfaz interna de Spring Data (Page<T>) al mundo exterior, manteniendo la API desacoplada.

Campo en PageResponse	Origen / Función
content / data	Lista de recursos de la página actual (Page.getContent()).
currentPage	Número de página actual (Page.getNumber()).
totalPages	Número total de páginas (Page.getTotalPages()).
totalElements	Número total de registros en la BD (Page.getTotalElements()).
links	Mapa de URLs de navegación (Generado por PaginationLinksUtils).

Ejemplo de Respuesta JSON (Contrato)
JSON

{
"content": [ /* ... lista de estudiantes ... */ ],
"currentPage": 1,
"totalPages": 5,
"totalElements": 50,
"links": {
"self": "/api/estudiantes?page=1&size=10",
"prev": "/api/estudiantes?page=0&size=10",
"next": "/api/estudiantes?page=2&size=10",
"last": "/api/estudiantes?page=4&size=10"
}
}
3. 🔗 PaginationLinksUtils: El Generador de URLs (HATEOAS)
   El PaginationLinksUtils es un utility que se encarga de crear las URLs de navegación que van en el campo links del PageResponse.

Función Principal: Proporcionar una navegación RESTful (principios HATEOAS) calculando dinámicamente las URLs next, prev, first y last, preservando los filtros y criterios de ordenación originales.

Utilidad: El cliente (frontend) no tiene que preocuparse por la lógica de construir la URL; simplemente sigue el enlace proporcionado.

Mecanismo de Funcionamiento
Recibe: El objeto Page<T> (para saber el estado de la paginación: si hay anterior, siguiente, etc.) y el HttpServletRequest (o la URL de la solicitud actual).

Determina la URL Base: Extrae la URL base y los parámetros de búsqueda (?nombre=Juan&sort=nombre:ASC).

Calcula Enlaces:

Si page.hasPrevious() es verdadero, construye la URL para page.getNumber() - 1 y la asigna a la clave prev.

Si page.hasNext() es verdadero, construye la URL para page.getNumber() + 1 y la asigna a la clave next.

Siempre genera los enlaces first (página 0) y last (página totalPages - 1).

Flujo Típico en el Controlador
El Controlador llama al Servicio y recibe Page<Estudiante>.

El Controlador inyecta el Page<Estudiante> y el Request en el PaginationLinksUtils para obtener el Map<String, String> de URLs.

El Controlador usa un Mapper para construir el PageResponse<Estudiante> con la lista de estudiantes y el mapa de enlaces.

El PageResponse se devuelve como JSON al cliente.