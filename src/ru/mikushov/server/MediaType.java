package ru.mikushov.server;

public enum MediaType {
    GRAPHQL("application/graphql"),
    JS("application/javascript"),
    JSON("application/json"),
    LDJSON("application/ld+json"),
    DOC("application/msword (.doc)"),
    PDF("application/pdf"),
    SQL("application/sql"),
    VNDAPIJSON("application/vnd.api+json"),
    XLS("application/vnd.ms-excel (.xls)"),
    PPT("application/vnd.ms-powerpoint (.ppt)"),
    ODT("application/vnd.oasis.opendocument.text (.odt)"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation (.pptx)"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet (.xlsx)"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document (.docx)"),
    FORM_TEXT_DATA("application/x-www-form-urlencoded"),
    APP_XML("application/xml"),
    ZIP("application/zip"),
    ZST("application/zstd (.zst)"),
    MPEG("audio/mpeg"),
    OGG("audio/ogg"),
    GIF("image/gif"),
    APNG("image/apng"),
    FLIF("image/flif"),
    WEBP("image/webp"),
    X_MNG("image/x-mng"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    FORM_BIN_DATA("multipart/form-data"),
    CSS("text/css"),
    CSV("text/csv"),
    HTML("text/html"),
    PHP("text/php"),
    PLAIN("text/plain"),
    TXT_XML("text/xml");
    public final String label;

    private MediaType(String label) {
        this.label = label;
    }

    public static MediaType valueOfLabel(String label) {
        for (MediaType e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
