/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

function getDefaults() {
    return {
        async: false,
        baseUrl: null,
        breaks: false,
        extensions: null,
        gfm: true,
        headerIds: true,
        headerPrefix: '',
        highlight: null,
        langPrefix: 'language-',
        mangle: true,
        pedantic: false,
        renderer: null,
        sanitize: false,
        sanitizer: null,
        silent: false,
        smartypants: false,
        tokenizer: null,
        walkTokens: null,
        xhtml: false
    };
}

const defaults = getDefaults();

const escapeTest = /[&<>"]/;
const escapeReplace = new RegExp(escapeTest.source, 'g');
const escapeTestNoEncode = /[<>"']|&(?!(#\d{1,7}|#[Xx][a-fA-F0-9]{1,6}|\w+);)/;
const escapeReplaceNoEncode = new RegExp(escapeTestNoEncode.source, 'g');
const escapeReplacements = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;'
};
const getEscapeReplacement = (ch) => escapeReplacements[ch];

function escape(html, encode) {
    if (encode) {
        if (escapeTest.test(html)) {
            return html.replace(escapeReplace, getEscapeReplacement);
        }
    } else {
        if (escapeTestNoEncode.test(html)) {
            return html.replace(escapeReplaceNoEncode, getEscapeReplacement);
        }
    }
    return html;
}

const unescapeTest = /&(#(?:\d+)|(?:#x[0-9A-Fa-f]+)|(?:\w+));?/ig;

/**
 * @param {string} html
 */
function unescape(html) {
    // explicitly match decimal, hex, and named HTML entities
    return html.replace(unescapeTest, (_, n) => {
        n = n.toLowerCase();
        if (n === 'colon') return ':';
        if (n.charAt(0) === '#') {
            return n.charAt(1) === 'x'
                ? String.fromCharCode(parseInt(n.substring(2), 16))
                : String.fromCharCode(+n.substring(1));
        }
        return '';
    });
}

const nonWordAndColonTest = /[^\w:]/g;
const originIndependentUrl = /^$|^[a-z][a-z0-9+.-]*:|^[?#]/i;

/**
 * @param {boolean} sanitize
 * @param {string} base
 * @param {string} href
 */
function cleanUrl(sanitize, base, href) {
    if (sanitize) {
        let prot;
        try {
            prot = decodeURIComponent(unescape(href))
                .replace(nonWordAndColonTest, '')
                .toLowerCase();
        } catch (e) {
            return null;
        }
        if (prot.indexOf('javascript:') === 0 || prot.indexOf('vbscript:') === 0 || prot.indexOf('data:') === 0) {
            return null;
        }
    }
    if (base && !originIndependentUrl.test(href)) {
        href = resolveUrl(base, href);
    }
    try {
        href = encodeURI(href).replace(/%25/g, '%');
    } catch (e) {
        return null;
    }
    return href;
}

const baseUrls = {};
const justDomain = /^[^:]+:\/*[^/]*$/;
const protocol = /^([^:]+:)[\s\S]*$/;
const domain = /^([^:]+:\/*[^/]*)[\s\S]*$/;

/**
 * @param {string} base
 * @param {string} href
 */
function resolveUrl(base, href) {
    if (!baseUrls[' ' + base]) {
        // we can ignore everything in base after the last slash of its path component,
        // but we might need to add _that_
        // https://tools.ietf.org/html/rfc3986#section-3
        if (justDomain.test(base)) {
            baseUrls[' ' + base] = base + '/';
        } else {
            baseUrls[' ' + base] = rtrim(base, '/', true);
        }
    }
    base = baseUrls[' ' + base];
    const relativeBase = base.indexOf(':') === -1;

    if (href.substring(0, 2) === '//') {
        if (relativeBase) {
            return href;
        }
        return base.replace(protocol, '$1') + href;
    } else if (href.charAt(0) === '/') {
        if (relativeBase) {
            return href;
        }
        return base.replace(domain, '$1') + href;
    } else {
        return base + href;
    }
}

/**
 * Remove trailing 'c's. Equivalent to str.replace(/c*$/, '').
 * /c*$/ is vulnerable to REDOS.
 *
 * @param {string} str
 * @param {string} c
 * @param {boolean} invert Remove suffix of non-c chars instead. Default falsey.
 */
function rtrim(str, c, invert) {
    const l = str.length;
    if (l === 0) {
        return '';
    }

    // Length of suffix matching the invert condition.
    let suffLen = 0;

    // Step left until we fail to match the invert condition.
    while (suffLen < l) {
        const currChar = str.charAt(l - suffLen - 1);
        if (currChar === c && !invert) {
            suffLen++;
        } else if (currChar !== c && invert) {
            suffLen++;
        } else {
            break;
        }
    }

    return str.slice(0, l - suffLen);
}

function tryEscape(text) {
    let encode = text.replace(/\"/g, '&quot;');
    encode = encode.replace(/\'/g, '&#39;');
    encode = encode.replace(/&/g, '&amp;');
    return encode
}

function tryMath(text) {
    return text.replace(/\"/g, '&quot;')
}

export class NaiveUIRenderer {
    constructor(options) {
        this.options = options || defaults;
    }

    code(code, infostring, escaped) {
        const lang = (infostring || '').match(/\S*/)[0];

        code = code.replace(/\n$/, '') + '\n';
        let encode = tryEscape(code)

        if (!lang) {
            return '<n-code style="overflow: auto" code="'
                + encode
                + '"/>\n';
        }
        return '<n-code style="overflow: auto" class="'
            + this.options.langPrefix
            + escape(lang)
            + ` mt-5 " code="${escape(code, true)}" language="${escape(lang)}" :show-line-numbers="true" />`
            + '\n';
    }

    /**
     * @param {string} quote
     */
    blockquote(quote) {
        return `<n-blockquote>${(quote)}</n-blockquote>\n`;
    }

    html(html) {
        return html;
    }

    /**
     * @param {string} text
     * @param {string} level
     * @param {string} raw
     * @param {any} slugger
     */
    heading(text, level, raw, slugger) {
        const id = this.options.headerPrefix + raw;
        const prefix = this.ifFirst(level) ? 'prefix="bar"' : "";
        return `<n-h${level} id="${id}" type="primary" ${prefix}><n-text type="primary">${(text)}</n-text></n-h${level}>\n`;
    }

    ifFirst(level) {
        return level === "1" || level === 1;

    }

    hr() {
        return '<n-hr/>\n';
    }

    list(body, ordered, start) {
        const type = ordered ? 'n-ol' : 'n-ul',
            startatt = (ordered && start !== 1) ? (' start="' + start + '"') : '',
            classType = ordered ? 'list-decimal' : 'list-disc';
        return '<' + type + startatt + ` class="${classType}" >\n` + body + '</' + type + '>\n';
    }

    /**
     * @param {string} text
     */
    listitem(text) {
        return `<n-li>${text}</n-li>\n`;
    }

    checkbox(checked) {
        return '<n-checkbox class="mr-2 ml-2" '
            + (checked ? 'checked' : '')
            + ' disabled'
            + '/>';
    }


    /**
     * @param {string} text
     */
    paragraph(text) {
        const isTeXInline = /\$(.*)\$/g.test(text);
        const isTeXLine = /^\$\$(\s*.*\s*)\$\$$/.test(text);
        if (!isTeXLine && isTeXInline) {
            text = text.replace(/(\$([^\$]*)\$)+/g, ($1, $2) => {
                if ($2.indexOf('<n-code') >= 0) {
                    return $2
                }
                let inText = $2.replace(/\$/g, "")
                inText = tryMath(inText)
                return `<n-equation value="${inText}" :katex-options="{displayMode: false, strict: false}"/>\n`;
            })
        } else {
            let inner = text.replace(/\$/g, "")
            inner = tryMath(inner)
            text = (isTeXLine) ? `<n-equation value="${inner}" :katex-options="{displayMode: true, strict: false}"/>\n` : text
        }
        return `<n-p>${(text)}</n-p>\n`;
    }

    /**
     * @param {string} header
     * @param {string} body
     */
    table(header, body) {
        if (body) body = `<tbody>${body}</tbody>`;

        return '<n-table size="small" :single-line="false" class="w-auto" style="overflow: auto">\n'
            + '<thead>\n'
            + header
            + '</thead>\n'
            + body
            + '</n-table>\n';
    }

    /**
     * @param {string} content
     */
    tablerow(content) {
        return `<tr>\n${content}</tr>\n`;
    }

    tablecell(content, flags) {
        const type = flags.header ? 'th' : 'td';
        const tag = flags.align
            ? `<${type} style="text-align: ${flags.align}">`
            : `<${type}>`;
        return tag + content + `</${type}>\n`;
    }

    /**
     * span level renderer
     * @param {string} text
     */
    strong(text) {
        return `<span class="font-bold">${text}</span>`;
    }

    /**
     * @param {string} text
     */
    em(text) {
        return `<span class="italic">${text}</span>`;
    }

    /**
     * @param {string} text
     */
    codespan(text) {
        return `<n-text code>${text}</n-text>`;
    }

    br() {
        return '<br/>';
    }

    /**
     * @param {string} text
     */
    del(text) {
        return `<span class="line-through">${text}</span>`;
    }

    /**
     * @param {string} href
     * @param {string} title
     * @param {string} text
     */
    link(href, title, text) {
        href = cleanUrl(this.options.sanitize, this.options.baseUrl, href);
        if (href === null) {
            return text;
        }
        let out = '<n-a href="' + href + '"';
        if (title) {
            out += ' title="' + title + '"';
        }
        out += ' target="_blank">' + text + '</n-a>';
        return out;
    }

    /**
     * @param {string} href
     * @param {string} title
     * @param {string} text
     */
    image(href, title, text) {
        href = cleanUrl(this.options.sanitize, this.options.baseUrl, href);
        if (href === null) {
            return text;
        }

        let out = `<n-image src="${href}" alt="${text}"`;
        if (title) {
            out += ` title="${title}"`;
        }
        out += '/>';
        return out;
    }

    text(text) {
        return text;
    }
}
