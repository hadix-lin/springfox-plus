package io.github.hadixlin.springfoxplus.external

private const val EXTERNAL_URL_PREFIX = "http"

internal fun createExternalDocsDescription(
    originalDesc: String, docName: String, docUrl: String
): String {
    val external =
        when {
            docUrl.startsWith(EXTERNAL_URL_PREFIX, true) -> docUrl
            else -> "/doc/markdown.html?file=" + docUrl.trimStart('/')
        }
    return when {
        originalDesc.isBlank() -> ("<a href=\"" + external + "\">"
            + (if (docName.isNotBlank()) docName else external) + "</a>")
        docName.isBlank() -> "<a href=\"$external\">$originalDesc</a>"
        else -> "$originalDesc -> <a href=\"$external\">$docName</a>"
    }
}
