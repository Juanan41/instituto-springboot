package es.juanito.institutos.config.i18n;

import io.pebbletemplates.pebble.extension.Filter;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Map;

public class MessageFilter implements Filter {

    private final MessageSource messageSource;

    public MessageFilter(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Object apply(Object input,
                        Map<String, Object> args,
                        PebbleTemplate self,
                        EvaluationContext context,
                        int lineNumber) {

        if (input == null) return "";
        String code = input.toString();

        return messageSource.getMessage(code, null, code, LocaleContextHolder.getLocale());
    }

    @Override
    public List<String> getArgumentNames() {
        return List.of();
    }
}
