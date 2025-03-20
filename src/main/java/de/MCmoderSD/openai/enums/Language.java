package de.MCmoderSD.openai.enums;

/**
 * Enum representing various languages with their respective codes and names.
 */
@SuppressWarnings("unused")
public enum Language {

    // Enum Values
    AFRIKAANS("af", "Afrikaans"),
    ARABIC("ar", "Arabic"),
    ARMENIAN("hy", "Armenian"),
    AZERBAIJANI("az", "Azerbaijani"),
    BELARUSIAN("be", "Belarusian"),
    BOSNIAN("bs", "Bosnian"),
    BULGARIAN("bg", "Bulgarian"),
    CATALAN("ca", "Catalan"),
    CHINESE("zh", "Chinese"),
    CROATIAN("hr", "Croatian"),
    CZECH("cs", "Czech"),
    DANISH("da", "Danish"),
    DUTCH("nl", "Dutch"),
    ENGLISH("en", "English"),
    ESTONIAN("et", "Estonian"),
    FINNISH("fi", "Finnish"),
    FRENCH("fr", "French"),
    GALICIAN("gl", "Galician"),
    GERMAN("de", "German"),
    GREEK("el", "Greek"),
    HEBREW("he", "Hebrew"),
    HINDI("hi", "Hindi"),
    HUNGARIAN("hu", "Hungarian"),
    ICELANDIC("is", "Icelandic"),
    INDONESIAN("id", "Indonesian"),
    ITALIAN("it", "Italian"),
    JAPANESE("ja", "Japanese"),
    KANNADA("kn", "Kannada"),
    KAZAKH("kk", "Kazakh"),
    KOREAN("ko", "Korean"),
    LATVIAN("lv", "Latvian"),
    LITHUANIAN("lt", "Lithuanian"),
    MACEDONIAN("ml", "Macedonian"),
    MALAY("ms", "Malay"),
    MARATHI("mr", "Marathi"),
    MAORI("mi", "Maori"),
    NEPALI("ne", "Nepali"),
    NORWEGIAN("no", "Norwegian"),
    PERSIAN("fa", "Persian"),
    POLISH("pl", "Polish"),
    PORTUGUESE("pt", "Portuguese"),
    ROMANIAN("ro", "Romanian"),
    RUSSIAN("ru", "Russian"),
    SERBIAN("sr", "Serbian"),
    SLOVAK("sk", "Slovak"),
    SLOVENIAN("sl", "Slovenian"),
    SPANISH("es", "Spanish"),
    SWAHILI("sw", "Swahili"),
    SWEDISH("sv", "Swedish"),
    TAGALOG("tl", "Tagalog"),
    TAMIL("ta", "Tamil"),
    THAI("th", "Thai"),
    TURKISH("tr", "Turkish"),
    UKRAINIAN("uk", "Ukrainian"),
    URDU("ur", "Urdu"),
    VIETNAMESE("vi", "Vietnamese"),
    WELSH("cy", "Welsh");

    // Attributes
    private final String code;
    private final String name;

    /**
     * Constructor for Language enum.
     *
     * @param code The language code.
     * @param name The language name.
     */
    Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Gets the language code.
     *
     * @return The language code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the language name.
     *
     * @return The language name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Language enum corresponding to the given code.
     *
     * @param code The language code.
     * @return The Language enum.
     * @throws IllegalArgumentException If the code is invalid.
     */
    public static Language fromCode(String code) {
        for (Language language : Language.values()) if (language.getCode().equalsIgnoreCase(code)) return language;
        throw new IllegalArgumentException("Invalid language code: " + code);
    }

    /**
     * Returns the Language enum corresponding to the given name.
     *
     * @param name The language name.
     * @return The Language enum.
     * @throws IllegalArgumentException If the name is invalid.
     */
    public static Language fromName(String name) {
        for (Language language : Language.values()) if (language.getName().equalsIgnoreCase(name)) return language;
        throw new IllegalArgumentException("Invalid language name: " + name);
    }

    /**
     * Returns the Language enum corresponding to the given language string.
     *
     * @param language The language string (code, name, or enum name).
     * @return The Language enum.
     * @throws IllegalArgumentException If the language string is invalid.
     */
    public static Language getLanguage(String language) {
        if (language == null || language.isBlank()) return null;
        for (Language values : Language.values()) if (values.getCode().equalsIgnoreCase(language) || values.getName().equalsIgnoreCase(language) || values.name().equalsIgnoreCase(language)) return values;
        throw new IllegalArgumentException("Invalid language code: " + language);
    }
}