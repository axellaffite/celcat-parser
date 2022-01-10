package celcat.logic

import org.unbescape.html.HtmlEscape

fun String.unescapeHtml(): String = HtmlEscape.unescapeHtml(this).replace(Regex("<br\\s/?>"), "\n")