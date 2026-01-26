package es.juanito.institutos.config.i18n;

import io.pebbletemplates.pebble.extension.AbstractExtension;
import io.pebbletemplates.pebble.extension.Filter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class I18nPebbleExtension extends AbstractExtension {

    private final MessageSource messageSource;

    public I18nPebbleExtension(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Map<String, Filter> getFilters() {
        return Map.of(
                "message", new MessageFilter(messageSource)
        );
    }
}

