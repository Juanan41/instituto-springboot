package es.juanito.institutos.pagination.utils;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Utilidad para construir el 'Link Header' HTTP
 * basado en la información de paginación de Spring Data JPA (Page).
 */
@Component
public class PaginationLinksUtils {

    /**
     * Construye el String del Link Header con enlaces a la página next, prev, first y last.
     * @param page El objeto Page<?> devuelto por Spring Data.
     * @param uriBuilder El constructor de URI base de la petición actual.
     * @return String que contiene todos los enlaces formateados.
     */
    public String createLinkHeader(Page<?> page, UriComponentsBuilder uriBuilder) {
        final StringBuilder linkHeader = new StringBuilder();

        // 1. Enlace a la página SIGUIENTE (NEXT)
        if (page.hasNext()) {
            String uri = constructUri(page.getNumber() + 1, page.getSize(), uriBuilder);
            linkHeader.append(buildLinkHeader(uri, "next"));
        }

        // 2. Enlace a la página ANTERIOR (PREV)
        if (page.hasPrevious()) {
            String uri = constructUri(page.getNumber() - 1, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "prev"));
        }

        // 3. Enlace a la PRIMERA página (FIRST)
        if (!page.isFirst()) {
            String uri = constructUri(0, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "first"));
        }

        // 4. Enlace a la ÚLTIMA página (LAST)
        if (!page.isLast()) {
            String uri = constructUri(page.getTotalPages() - 1, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "last"));
        }

        return linkHeader.toString();
    }

    /**
     * Reemplaza los parámetros de página y tamaño en la URI base.
     */
    private String constructUri(int newPageNumber, int size, UriComponentsBuilder uriBuilder) {
        return uriBuilder
                .replaceQueryParam("page", newPageNumber)
                .replaceQueryParam("size", size)
                .build()
                .encode()
                .toUriString();
    }

    /**
     * Formatea el enlace como lo requiere el Link Header: <URI>; rel="REL_TYPE".
     */
    private String buildLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    /**
     * Añade una coma y un espacio si el encabezado ya contiene elementos.
     */
    private void appendCommaIfNecessary(final StringBuilder linkHeader) {
        if (!linkHeader.isEmpty()) {
            linkHeader.append(", ");
        }
    }

}