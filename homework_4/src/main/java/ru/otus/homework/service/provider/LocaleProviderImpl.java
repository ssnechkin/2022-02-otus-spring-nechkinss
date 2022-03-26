package ru.otus.homework.service.provider;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleProviderImpl implements LocaleProvider {
    @lombok.Setter
    @lombok.Getter
    private Locale locale;

    public LocaleProviderImpl() {
        this.locale = Locale.forLanguageTag("en-EN");
    }
}
