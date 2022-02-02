(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory();
	else if(typeof define === 'function' && define.amd)
		define([], factory);
	else if(typeof exports === 'object')
		exports["sqlFormatter"] = factory();
	else
		root["sqlFormatter"] = factory();
})(window, function() {
return /******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "./src/sqlFormatter.js");
/******/ })
/************************************************************************/
/******/ ({

/***/ "./src/core/Formatter.js":
/*!*******************************!*\
  !*** ./src/core/Formatter.js ***!
  \*******************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return Formatter; });
/* harmony import */ var _tokenTypes__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./tokenTypes */ "./src/core/tokenTypes.js");
/* harmony import */ var _Indentation__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./Indentation */ "./src/core/Indentation.js");
/* harmony import */ var _InlineBlock__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./InlineBlock */ "./src/core/InlineBlock.js");
/* harmony import */ var _Params__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./Params */ "./src/core/Params.js");
/* harmony import */ var _utils__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../utils */ "./src/utils.js");
/* harmony import */ var _token__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./token */ "./src/core/token.js");
function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }








var Formatter = /*#__PURE__*/function () {
  /**
   * @param {Object} cfg
   *  @param {String} cfg.language
   *  @param {String} cfg.indent
   *  @param {Boolean} cfg.uppercase
   *  @param {Integer} cfg.linesBetweenQueries
   *  @param {Object} cfg.params
   */
  function Formatter(cfg) {
    _classCallCheck(this, Formatter);

    this.cfg = cfg;
    this.indentation = new _Indentation__WEBPACK_IMPORTED_MODULE_1__["default"](this.cfg.indent);
    this.inlineBlock = new _InlineBlock__WEBPACK_IMPORTED_MODULE_2__["default"]();
    this.params = new _Params__WEBPACK_IMPORTED_MODULE_3__["default"](this.cfg.params);
    this.previousReservedToken = {};
    this.tokens = [];
    this.index = 0;
  }
  /**
   * SQL Tokenizer for this formatter, provided by subclasses.
   */


  _createClass(Formatter, [{
    key: "tokenizer",
    value: function tokenizer() {
      throw new Error('tokenizer() not implemented by subclass');
    }
    /**
     * Reprocess and modify a token based on parsed context.
     *
     * @param {Object} token The token to modify
     *  @param {String} token.type
     *  @param {String} token.value
     * @return {Object} new token or the original
     *  @return {String} token.type
     *  @return {String} token.value
     */

  }, {
    key: "tokenOverride",
    value: function tokenOverride(token) {
      // subclasses can override this to modify tokens during formatting
      return token;
    }
    /**
     * Formats whitespace in a SQL string to make it easier to read.
     *
     * @param {String} query The SQL query string
     * @return {String} formatted query
     */

  }, {
    key: "format",
    value: function format(query) {
      this.tokens = this.tokenizer().tokenize(query);
      var formattedQuery = this.getFormattedQueryFromTokens();
      return formattedQuery.trim();
    }
  }, {
    key: "getFormattedQueryFromTokens",
    value: function getFormattedQueryFromTokens() {
      var _this = this;

      var formattedQuery = '';
      this.tokens.forEach(function (token, index) {
        _this.index = index;
        token = _this.tokenOverride(token);

        if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].LINE_COMMENT) {
          formattedQuery = _this.formatLineComment(token, formattedQuery);
        } else if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].BLOCK_COMMENT) {
          formattedQuery = _this.formatBlockComment(token, formattedQuery);
        } else if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_TOP_LEVEL) {
          formattedQuery = _this.formatTopLevelReservedWord(token, formattedQuery);
          _this.previousReservedToken = token;
        } else if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_TOP_LEVEL_NO_INDENT) {
          formattedQuery = _this.formatTopLevelReservedWordNoIndent(token, formattedQuery);
          _this.previousReservedToken = token;
        } else if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_NEWLINE) {
          formattedQuery = _this.formatNewlineReservedWord(token, formattedQuery);
          _this.previousReservedToken = token;
        } else if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED) {
          formattedQuery = _this.formatWithSpaces(token, formattedQuery);
          _this.previousReservedToken = token;
        } else if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].OPEN_PAREN) {
          formattedQuery = _this.formatOpeningParentheses(token, formattedQuery);
        } else if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].CLOSE_PAREN) {
          formattedQuery = _this.formatClosingParentheses(token, formattedQuery);
        } else if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].PLACEHOLDER) {
          formattedQuery = _this.formatPlaceholder(token, formattedQuery);
        } else if (token.value === ',') {
          formattedQuery = _this.formatComma(token, formattedQuery);
        } else if (token.value === ':') {
          formattedQuery = _this.formatWithSpaceAfter(token, formattedQuery);
        } else if (token.value === '.') {
          formattedQuery = _this.formatWithoutSpaces(token, formattedQuery);
        } else if (token.value === ';') {
          formattedQuery = _this.formatQuerySeparator(token, formattedQuery);
        } else {
          formattedQuery = _this.formatWithSpaces(token, formattedQuery);
        }
      });
      return formattedQuery;
    }
  }, {
    key: "formatLineComment",
    value: function formatLineComment(token, query) {
      // add comment indent
      if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].LINE_COMMENT && token.whitespaceBefore.match(/\n/)) {
        value = '\n' + value;
        query = query.replace(/\s+$/g, "");
        var value = this.indentation.getIndent() + this.show(token);
        return this.addNewline(query + '\n' + value);
      } else {
        return this.addNewline(query + this.show(token));
      }
    }
  }, {
    key: "formatBlockComment",
    value: function formatBlockComment(token, query) {
      return this.addNewline(this.addNewline(query) + this.indentComment(token.value));
    }
  }, {
    key: "indentComment",
    value: function indentComment(comment) {
      return comment.replace(/\n[\t ]*/g, '\n' + this.indentation.getIndent() + ' ');
    }
  }, {
    key: "formatTopLevelReservedWordNoIndent",
    value: function formatTopLevelReservedWordNoIndent(token, query) {
      this.indentation.decreaseTopLevel();
      query = this.addNewline(query) + this.equalizeWhitespace(this.show(token));
      return this.addNewline(query);
    }
  }, {
    key: "formatTopLevelReservedWord",
    value: function formatTopLevelReservedWord(token, query) {
      this.indentation.decreaseTopLevel();
      query = this.addNewline(query);
      this.indentation.increaseTopLevel();
      query += this.equalizeWhitespace(this.show(token));
      return this.addNewline(query);
    }
  }, {
    key: "formatNewlineReservedWord",
    value: function formatNewlineReservedWord(token, query) {
      if (Object(_token__WEBPACK_IMPORTED_MODULE_5__["isAnd"])(token) && Object(_token__WEBPACK_IMPORTED_MODULE_5__["isBetween"])(this.tokenLookBehind(2))) {
        return this.formatWithSpaces(token, query);
      }

      return this.addNewline(query) + this.equalizeWhitespace(this.show(token)) + ' ';
    } // Replace any sequence of whitespace characters with single space

  }, {
    key: "equalizeWhitespace",
    value: function equalizeWhitespace(string) {
      return string.replace(/[\t-\r \xA0\u1680\u2000-\u200A\u2028\u2029\u202F\u205F\u3000\uFEFF]+/g, ' ');
    } // Opening parentheses increase the block indent level and start a new line

  }, {
    key: "formatOpeningParentheses",
    value: function formatOpeningParentheses(token, query) {
      var _preserveWhitespaceFo, _this$tokenLookBehind;

      // Take out the preceding space unless there was whitespace there in the original query
      // or another opening parens or line comment
      var preserveWhitespaceFor = (_preserveWhitespaceFo = {}, _defineProperty(_preserveWhitespaceFo, _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].OPEN_PAREN, true), _defineProperty(_preserveWhitespaceFo, _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].LINE_COMMENT, true), _defineProperty(_preserveWhitespaceFo, _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].OPERATOR, true), _preserveWhitespaceFo);

      if (token.whitespaceBefore.length === 0 && !preserveWhitespaceFor[(_this$tokenLookBehind = this.tokenLookBehind()) === null || _this$tokenLookBehind === void 0 ? void 0 : _this$tokenLookBehind.type]) {
        query = Object(_utils__WEBPACK_IMPORTED_MODULE_4__["trimSpacesEnd"])(query);
      }

      query += this.show(token);
      this.inlineBlock.beginIfPossible(this.tokens, this.index);

      if (!this.inlineBlock.isActive()) {
        this.indentation.increaseBlockLevel();
        query = this.addNewline(query);
      }

      return query;
    } // Closing parentheses decrease the block indent level

  }, {
    key: "formatClosingParentheses",
    value: function formatClosingParentheses(token, query) {
      if (this.inlineBlock.isActive()) {
        this.inlineBlock.end();
        return this.formatWithSpaceAfter(token, query);
      } else {
        this.indentation.decreaseBlockLevel();
        return this.formatWithSpaces(token, this.addNewline(query));
      }
    }
  }, {
    key: "formatPlaceholder",
    value: function formatPlaceholder(token, query) {
      return query + this.params.get(token) + ' ';
    } // Commas start a new line (unless within inline parentheses or SQL "LIMIT" clause)

  }, {
    key: "formatComma",
    value: function formatComma(token, query) {
      query = Object(_utils__WEBPACK_IMPORTED_MODULE_4__["trimSpacesEnd"])(query) + this.show(token) + ' ';

      if (this.inlineBlock.isActive()) {
        return query;
      } else if (Object(_token__WEBPACK_IMPORTED_MODULE_5__["isLimit"])(this.previousReservedToken)) {
        return query;
      } else {
        return this.addNewline(query);
      }
    }
  }, {
    key: "formatWithSpaceAfter",
    value: function formatWithSpaceAfter(token, query) {
      return Object(_utils__WEBPACK_IMPORTED_MODULE_4__["trimSpacesEnd"])(query) + this.show(token) + ' ';
    }
  }, {
    key: "formatWithoutSpaces",
    value: function formatWithoutSpaces(token, query) {
      return Object(_utils__WEBPACK_IMPORTED_MODULE_4__["trimSpacesEnd"])(query) + this.show(token);
    }
  }, {
    key: "formatWithSpaces",
    value: function formatWithSpaces(token, query) {
      return query + this.show(token) + ' ';
    }
  }, {
    key: "formatQuerySeparator",
    value: function formatQuerySeparator(token, query) {
      this.indentation.resetIndentation();
      return Object(_utils__WEBPACK_IMPORTED_MODULE_4__["trimSpacesEnd"])(query) + this.show(token) + '\n'.repeat(this.cfg.linesBetweenQueries || 1);
    } // Converts token to string (uppercasing it if needed)

  }, {
    key: "show",
    value: function show(_ref) {
      var type = _ref.type,
          value = _ref.value;

      if (this.cfg.uppercase && (type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED || type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_TOP_LEVEL || type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_TOP_LEVEL_NO_INDENT || type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_NEWLINE || type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].OPEN_PAREN || type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].CLOSE_PAREN)) {
        return value.toUpperCase();
      } else {
        return value;
      }
    }
  }, {
    key: "addNewline",
    value: function addNewline(query) {
      query = Object(_utils__WEBPACK_IMPORTED_MODULE_4__["trimSpacesEnd"])(query);

      if (!query.endsWith('\n')) {
        query += '\n';
      }

      return query + this.indentation.getIndent();
    }
  }, {
    key: "tokenLookBehind",
    value: function tokenLookBehind() {
      var n = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 1;
      return this.tokens[this.index - n];
    }
  }, {
    key: "tokenLookAhead",
    value: function tokenLookAhead() {
      var n = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 1;
      return this.tokens[this.index + n];
    }
  }]);

  return Formatter;
}();



/***/ }),

/***/ "./src/core/Indentation.js":
/*!*********************************!*\
  !*** ./src/core/Indentation.js ***!
  \*********************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return Indentation; });
/* harmony import */ var _utils__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../utils */ "./src/utils.js");
function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }


var INDENT_TYPE_TOP_LEVEL = 'top-level';
var INDENT_TYPE_BLOCK_LEVEL = 'block-level';
/**
 * Manages indentation levels.
 *
 * There are two types of indentation levels:
 *
 * - BLOCK_LEVEL : increased by open-parenthesis
 * - TOP_LEVEL : increased by RESERVED_TOP_LEVEL words
 */

var Indentation = /*#__PURE__*/function () {
  /**
   * @param {String} indent Indent value, default is "  " (2 spaces)
   */
  function Indentation(indent) {
    _classCallCheck(this, Indentation);

    this.indent = indent || '  ';
    this.indentTypes = [];
  }
  /**
   * Returns current indentation string.
   * @return {String}
   */


  _createClass(Indentation, [{
    key: "getIndent",
    value: function getIndent() {
      return this.indent.repeat(this.indentTypes.length);
    }
    /**
     * Increases indentation by one top-level indent.
     */

  }, {
    key: "increaseTopLevel",
    value: function increaseTopLevel() {
      this.indentTypes.push(INDENT_TYPE_TOP_LEVEL);
    }
    /**
     * Increases indentation by one block-level indent.
     */

  }, {
    key: "increaseBlockLevel",
    value: function increaseBlockLevel() {
      this.indentTypes.push(INDENT_TYPE_BLOCK_LEVEL);
    }
    /**
     * Decreases indentation by one top-level indent.
     * Does nothing when the previous indent is not top-level.
     */

  }, {
    key: "decreaseTopLevel",
    value: function decreaseTopLevel() {
      if (this.indentTypes.length > 0 && Object(_utils__WEBPACK_IMPORTED_MODULE_0__["last"])(this.indentTypes) === INDENT_TYPE_TOP_LEVEL) {
        this.indentTypes.pop();
      }
    }
    /**
     * Decreases indentation by one block-level indent.
     * If there are top-level indents within the block-level indent,
     * throws away these as well.
     */

  }, {
    key: "decreaseBlockLevel",
    value: function decreaseBlockLevel() {
      while (this.indentTypes.length > 0) {
        var type = this.indentTypes.pop();

        if (type !== INDENT_TYPE_TOP_LEVEL) {
          break;
        }
      }
    }
  }, {
    key: "resetIndentation",
    value: function resetIndentation() {
      this.indentTypes = [];
    }
  }]);

  return Indentation;
}();



/***/ }),

/***/ "./src/core/InlineBlock.js":
/*!*********************************!*\
  !*** ./src/core/InlineBlock.js ***!
  \*********************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return InlineBlock; });
/* harmony import */ var _tokenTypes__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./tokenTypes */ "./src/core/tokenTypes.js");
function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }


var INLINE_MAX_LENGTH = 50;
/**
 * Bookkeeper for inline blocks.
 *
 * Inline blocks are parenthized expressions that are shorter than INLINE_MAX_LENGTH.
 * These blocks are formatted on a single line, unlike longer parenthized
 * expressions where open-parenthesis causes newline and increase of indentation.
 */

var InlineBlock = /*#__PURE__*/function () {
  function InlineBlock() {
    _classCallCheck(this, InlineBlock);

    this.level = 0;
  }
  /**
   * Begins inline block when lookahead through upcoming tokens determines
   * that the block would be smaller than INLINE_MAX_LENGTH.
   * @param  {Object[]} tokens Array of all tokens
   * @param  {Number} index Current token position
   */


  _createClass(InlineBlock, [{
    key: "beginIfPossible",
    value: function beginIfPossible(tokens, index) {
      if (this.level === 0 && this.isInlineBlock(tokens, index)) {
        this.level = 1;
      } else if (this.level > 0) {
        this.level++;
      } else {
        this.level = 0;
      }
    }
    /**
     * Finishes current inline block.
     * There might be several nested ones.
     */

  }, {
    key: "end",
    value: function end() {
      this.level--;
    }
    /**
     * True when inside an inline block
     * @return {Boolean}
     */

  }, {
    key: "isActive",
    value: function isActive() {
      return this.level > 0;
    } // Check if this should be an inline parentheses block
    // Examples are "NOW()", "COUNT(*)", "int(10)", key(`somecolumn`), DECIMAL(7,2)

  }, {
    key: "isInlineBlock",
    value: function isInlineBlock(tokens, index) {
      var length = 0;
      var level = 0;

      for (var i = index; i < tokens.length; i++) {
        var token = tokens[i];
        length += token.value.length; // Overran max length

        if (length > INLINE_MAX_LENGTH) {
          return false;
        }

        if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].OPEN_PAREN) {
          level++;
        } else if (token.type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].CLOSE_PAREN) {
          level--;

          if (level === 0) {
            return true;
          }
        }

        if (this.isForbiddenToken(token)) {
          return false;
        }
      }

      return false;
    } // Reserved words that cause newlines, comments and semicolons
    // are not allowed inside inline parentheses block

  }, {
    key: "isForbiddenToken",
    value: function isForbiddenToken(_ref) {
      var type = _ref.type,
          value = _ref.value;
      return type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_TOP_LEVEL || type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_NEWLINE || type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].COMMENT || type === _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].BLOCK_COMMENT || value === ';';
    }
  }]);

  return InlineBlock;
}();



/***/ }),

/***/ "./src/core/Params.js":
/*!****************************!*\
  !*** ./src/core/Params.js ***!
  \****************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return Params; });
function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

/**
 * Handles placeholder replacement with given params.
 */
var Params = /*#__PURE__*/function () {
  /**
   * @param {Object} params
   */
  function Params(params) {
    _classCallCheck(this, Params);

    this.params = params;
    this.index = 0;
  }
  /**
   * Returns param value that matches given placeholder with param key.
   * @param {Object} token
   *   @param {String} token.key Placeholder key
   *   @param {String} token.value Placeholder value
   * @return {String} param or token.value when params are missing
   */


  _createClass(Params, [{
    key: "get",
    value: function get(_ref) {
      var key = _ref.key,
          value = _ref.value;

      if (!this.params) {
        return value;
      }

      if (key) {
        return this.params[key];
      }

      return this.params[this.index++];
    }
  }]);

  return Params;
}();



/***/ }),

/***/ "./src/core/Tokenizer.js":
/*!*******************************!*\
  !*** ./src/core/Tokenizer.js ***!
  \*******************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return Tokenizer; });
/* harmony import */ var _tokenTypes__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./tokenTypes */ "./src/core/tokenTypes.js");
/* harmony import */ var _regexFactory__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./regexFactory */ "./src/core/regexFactory.js");
/* harmony import */ var _utils__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../utils */ "./src/utils.js");
function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); if (enumerableOnly) symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; }); keys.push.apply(keys, symbols); } return keys; }

function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i] != null ? arguments[i] : {}; if (i % 2) { ownKeys(Object(source), true).forEach(function (key) { _defineProperty(target, key, source[key]); }); } else if (Object.getOwnPropertyDescriptors) { Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)); } else { ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } } return target; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _toConsumableArray(arr) { return _arrayWithoutHoles(arr) || _iterableToArray(arr) || _unsupportedIterableToArray(arr) || _nonIterableSpread(); }

function _nonIterableSpread() { throw new TypeError("Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); }

function _unsupportedIterableToArray(o, minLen) { if (!o) return; if (typeof o === "string") return _arrayLikeToArray(o, minLen); var n = Object.prototype.toString.call(o).slice(8, -1); if (n === "Object" && o.constructor) n = o.constructor.name; if (n === "Map" || n === "Set") return Array.from(o); if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen); }

function _iterableToArray(iter) { if (typeof Symbol !== "undefined" && Symbol.iterator in Object(iter)) return Array.from(iter); }

function _arrayWithoutHoles(arr) { if (Array.isArray(arr)) return _arrayLikeToArray(arr); }

function _arrayLikeToArray(arr, len) { if (len == null || len > arr.length) len = arr.length; for (var i = 0, arr2 = new Array(len); i < len; i++) { arr2[i] = arr[i]; } return arr2; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }





var Tokenizer = /*#__PURE__*/function () {
  /**
   * @param {Object} cfg
   *  @param {String[]} cfg.reservedWords Reserved words in SQL
   *  @param {String[]} cfg.reservedTopLevelWords Words that are set to new line separately
   *  @param {String[]} cfg.reservedNewlineWords Words that are set to newline
   *  @param {String[]} cfg.reservedTopLevelWordsNoIndent Words that are top level but have no indentation
   *  @param {String[]} cfg.stringTypes String types to enable: "", '', ``, [], N''
   *  @param {String[]} cfg.openParens Opening parentheses to enable, like (, [
   *  @param {String[]} cfg.closeParens Closing parentheses to enable, like ), ]
   *  @param {String[]} cfg.indexedPlaceholderTypes Prefixes for indexed placeholders, like ?
   *  @param {String[]} cfg.namedPlaceholderTypes Prefixes for named placeholders, like @ and :
   *  @param {String[]} cfg.lineCommentTypes Line comments to enable, like # and --
   *  @param {String[]} cfg.specialWordChars Special chars that can be found inside of words, like @ and #
   *  @param {String[]} [cfg.operator] Additional operators to recognize
   */
  function Tokenizer(cfg) {
    _classCallCheck(this, Tokenizer);

    cfg = Object(_utils__WEBPACK_IMPORTED_MODULE_2__["objectMerge"])({
      stringTypes: ["\"\"", "''", "mybatis"],
      openParens: ['(', 'CASE'],
      closeParens: [')', 'END'],
      indexedPlaceholderTypes: ['?'],
      namedPlaceholderTypes: [],
      lineCommentTypes: ['--']
    }, cfg);
    this.WHITESPACE_REGEX = /^([\t-\r \xA0\u1680\u2000-\u200A\u2028\u2029\u202F\u205F\u3000\uFEFF]+)/;
    this.NUMBER_REGEX = /^((\x2D[\t-\r \xA0\u1680\u2000-\u200A\u2028\u2029\u202F\u205F\u3000\uFEFF]*)?[0-9]+(\.[0-9]+)?([Ee]\x2D?[0-9]+(\.[0-9]+)?)?|0x[0-9A-Fa-f]+|0b[01]+)\b/;
    this.OPERATOR_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createOperatorRegex"](['<>', '<=', '>='].concat(_toConsumableArray(cfg.operators || [])));
    this.BLOCK_COMMENT_REGEX = /^(\/\*(?:(?![])[\s\S])*?(?:\*\/|$))/;
    this.LINE_COMMENT_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createLineCommentRegex"](cfg.lineCommentTypes);
    this.RESERVED_TOP_LEVEL_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createReservedWordRegex"](cfg.reservedTopLevelWords);
    this.RESERVED_TOP_LEVEL_NO_INDENT_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createReservedWordRegex"](cfg.reservedTopLevelWordsNoIndent);
    this.RESERVED_NEWLINE_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createReservedWordRegex"](cfg.reservedNewlineWords);
    this.RESERVED_PLAIN_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createReservedWordRegex"](cfg.reservedWords);
    this.WORD_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createWordRegex"](cfg.specialWordChars);
    this.STRING_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createStringRegex"](cfg.stringTypes);
    this.OPEN_PAREN_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createParenRegex"](cfg.openParens);
    this.CLOSE_PAREN_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createParenRegex"](cfg.closeParens);
    this.INDEXED_PLACEHOLDER_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createPlaceholderRegex"](cfg.indexedPlaceholderTypes, '[0-9]*');
    this.IDENT_NAMED_PLACEHOLDER_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createPlaceholderRegex"](cfg.namedPlaceholderTypes, '[a-zA-Z0-9._$]+');
    this.STRING_NAMED_PLACEHOLDER_REGEX = _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createPlaceholderRegex"](cfg.namedPlaceholderTypes, _regexFactory__WEBPACK_IMPORTED_MODULE_1__["createStringPattern"](cfg.stringTypes));
  }
  /**
   * Takes a SQL string and breaks it into tokens.
   * Each token is an object with type and value.
   *
   * @param {String} input The SQL string
   * @return {Object[]} tokens An array of tokens.
   *  @return {String} token.type
   *  @return {String} token.value
   *  @return {String} token.whitespaceBefore Preceding whitespace
   */


  _createClass(Tokenizer, [{
    key: "tokenize",
    value: function tokenize(input) {
      var tokens = [];
      var token; // Keep processing the string until it is empty

      while (input.length) {
        // grab any preceding whitespace
        var whitespaceBefore = this.getWhitespace(input);
        input = input.substring(whitespaceBefore.length);

        if (input.length) {
          // Get the next token and the token type
          token = this.getNextToken(input, token); // Advance the string

          input = input.substring(token.value.length);
          tokens.push(_objectSpread(_objectSpread({}, token), {}, {
            whitespaceBefore: whitespaceBefore
          }));
        }
      }

      return tokens;
    }
  }, {
    key: "getWhitespace",
    value: function getWhitespace(input) {
      var matches = input.match(this.WHITESPACE_REGEX);
      return matches ? matches[1] : '';
    }
  }, {
    key: "getNextToken",
    value: function getNextToken(input, previousToken) {
      return this.getCommentToken(input) || this.getStringToken(input) || this.getOpenParenToken(input) || this.getCloseParenToken(input) || this.getPlaceholderToken(input) || this.getNumberToken(input) || this.getReservedWordToken(input, previousToken) || this.getWordToken(input) || this.getOperatorToken(input);
    }
  }, {
    key: "getCommentToken",
    value: function getCommentToken(input) {
      return this.getLineCommentToken(input) || this.getBlockCommentToken(input);
    }
  }, {
    key: "getLineCommentToken",
    value: function getLineCommentToken(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].LINE_COMMENT,
        regex: this.LINE_COMMENT_REGEX
      });
    }
  }, {
    key: "getBlockCommentToken",
    value: function getBlockCommentToken(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].BLOCK_COMMENT,
        regex: this.BLOCK_COMMENT_REGEX
      });
    }
  }, {
    key: "getStringToken",
    value: function getStringToken(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].STRING,
        regex: this.STRING_REGEX
      });
    }
  }, {
    key: "getOpenParenToken",
    value: function getOpenParenToken(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].OPEN_PAREN,
        regex: this.OPEN_PAREN_REGEX
      });
    }
  }, {
    key: "getCloseParenToken",
    value: function getCloseParenToken(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].CLOSE_PAREN,
        regex: this.CLOSE_PAREN_REGEX
      });
    }
  }, {
    key: "getPlaceholderToken",
    value: function getPlaceholderToken(input) {
      return this.getIdentNamedPlaceholderToken(input) || this.getStringNamedPlaceholderToken(input) || this.getIndexedPlaceholderToken(input);
    }
  }, {
    key: "getIdentNamedPlaceholderToken",
    value: function getIdentNamedPlaceholderToken(input) {
      return this.getPlaceholderTokenWithKey({
        input: input,
        regex: this.IDENT_NAMED_PLACEHOLDER_REGEX,
        parseKey: function parseKey(v) {
          return v.slice(1);
        }
      });
    }
  }, {
    key: "getStringNamedPlaceholderToken",
    value: function getStringNamedPlaceholderToken(input) {
      var _this = this;

      return this.getPlaceholderTokenWithKey({
        input: input,
        regex: this.STRING_NAMED_PLACEHOLDER_REGEX,
        parseKey: function parseKey(v) {
          return _this.getEscapedPlaceholderKey({
            key: v.slice(2, -1),
            quoteChar: v.slice(-1)
          });
        }
      });
    }
  }, {
    key: "getIndexedPlaceholderToken",
    value: function getIndexedPlaceholderToken(input) {
      return this.getPlaceholderTokenWithKey({
        input: input,
        regex: this.INDEXED_PLACEHOLDER_REGEX,
        parseKey: function parseKey(v) {
          return v.slice(1);
        }
      });
    }
  }, {
    key: "getPlaceholderTokenWithKey",
    value: function getPlaceholderTokenWithKey(_ref) {
      var input = _ref.input,
          regex = _ref.regex,
          parseKey = _ref.parseKey;
      var token = this.getTokenOnFirstMatch({
        input: input,
        regex: regex,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].PLACEHOLDER
      });

      if (token) {
        token.key = parseKey(token.value);
      }

      return token;
    }
  }, {
    key: "getEscapedPlaceholderKey",
    value: function getEscapedPlaceholderKey(_ref2) {
      var key = _ref2.key,
          quoteChar = _ref2.quoteChar;
      return key.replace(new RegExp(Object(_utils__WEBPACK_IMPORTED_MODULE_2__["escapeRegExp"])('\\' + quoteChar), 'gu'), quoteChar);
    } // Decimal, binary, or hex numbers

  }, {
    key: "getNumberToken",
    value: function getNumberToken(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].NUMBER,
        regex: this.NUMBER_REGEX
      });
    } // Punctuation and symbols

  }, {
    key: "getOperatorToken",
    value: function getOperatorToken(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].OPERATOR,
        regex: this.OPERATOR_REGEX
      });
    }
  }, {
    key: "getReservedWordToken",
    value: function getReservedWordToken(input, previousToken) {
      // A reserved word cannot be preceded by a "."
      // this makes it so in "mytable.from", "from" is not considered a reserved word
      if (previousToken && previousToken.value && previousToken.value === '.') {
        return undefined;
      }

      return this.getTopLevelReservedToken(input) || this.getNewlineReservedToken(input) || this.getTopLevelReservedTokenNoIndent(input) || this.getPlainReservedToken(input);
    }
  }, {
    key: "getTopLevelReservedToken",
    value: function getTopLevelReservedToken(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_TOP_LEVEL,
        regex: this.RESERVED_TOP_LEVEL_REGEX
      });
    }
  }, {
    key: "getNewlineReservedToken",
    value: function getNewlineReservedToken(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_NEWLINE,
        regex: this.RESERVED_NEWLINE_REGEX
      });
    }
  }, {
    key: "getTopLevelReservedTokenNoIndent",
    value: function getTopLevelReservedTokenNoIndent(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_TOP_LEVEL_NO_INDENT,
        regex: this.RESERVED_TOP_LEVEL_NO_INDENT_REGEX
      });
    }
  }, {
    key: "getPlainReservedToken",
    value: function getPlainReservedToken(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED,
        regex: this.RESERVED_PLAIN_REGEX
      });
    }
  }, {
    key: "getWordToken",
    value: function getWordToken(input) {
      return this.getTokenOnFirstMatch({
        input: input,
        type: _tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].WORD,
        regex: this.WORD_REGEX
      });
    }
  }, {
    key: "getTokenOnFirstMatch",
    value: function getTokenOnFirstMatch(_ref3) {
      var input = _ref3.input,
          type = _ref3.type,
          regex = _ref3.regex;
      var matches = input.match(regex);
      return matches ? {
        type: type,
        value: matches[1]
      } : undefined;
    }
  }]);

  return Tokenizer;
}();



/***/ }),

/***/ "./src/core/regexFactory.js":
/*!**********************************!*\
  !*** ./src/core/regexFactory.js ***!
  \**********************************/
/*! exports provided: createOperatorRegex, createLineCommentRegex, createReservedWordRegex, createWordRegex, createStringRegex, createStringPattern, createParenRegex, createPlaceholderRegex */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "createOperatorRegex", function() { return createOperatorRegex; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "createLineCommentRegex", function() { return createLineCommentRegex; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "createReservedWordRegex", function() { return createReservedWordRegex; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "createWordRegex", function() { return createWordRegex; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "createStringRegex", function() { return createStringRegex; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "createStringPattern", function() { return createStringPattern; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "createParenRegex", function() { return createParenRegex; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "createPlaceholderRegex", function() { return createPlaceholderRegex; });
/* harmony import */ var _utils__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../utils */ "./src/utils.js");

function createOperatorRegex(multiLetterOperators) {
  return new RegExp("^(".concat(Object(_utils__WEBPACK_IMPORTED_MODULE_0__["sortByLengthDesc"])(multiLetterOperators).map(_utils__WEBPACK_IMPORTED_MODULE_0__["escapeRegExp"]).join('|'), "|.)"), 'u');
}
function createLineCommentRegex(lineCommentTypes) {
  return new RegExp("^((?:".concat(lineCommentTypes.map(function (c) {
    return Object(_utils__WEBPACK_IMPORTED_MODULE_0__["escapeRegExp"])(c);
  }).join('|'), ").*?)(?:\r\n|\r|\n|$)"), 'u');
}
function createReservedWordRegex(reservedWords) {
  if (reservedWords.length === 0) {
    return new RegExp("^\b$", 'u');
  }

  var reservedWordsPattern = Object(_utils__WEBPACK_IMPORTED_MODULE_0__["sortByLengthDesc"])(reservedWords).join('|').replace(/ /g, '\\s+');
  return new RegExp("^(".concat(reservedWordsPattern, ")\\b"), 'iu');
}
function createWordRegex() {
  var specialChars = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : [];
  return new RegExp("^([\\p{Alphabetic}\\p{Mark}\\p{Decimal_Number}\\p{Connector_Punctuation}\\p{Join_Control}".concat(specialChars.join(''), "]+)"), 'u');
}
function createStringRegex(stringTypes) {
  return new RegExp('^(' + createStringPattern(stringTypes) + ')', 'u');
} // This enables the following string patterns:
// 1. backtick quoted string using `` to escape
// 2. square bracket quoted string (SQL Server) using ]] to escape
// 3. double quoted string using "" or \" to escape
// 4. single quoted string using '' or \' to escape
// 5. national character quoted string using N'' or N\' to escape
// 6. Unicode single-quoted string using \' to escape
// 7. Unicode double-quoted string using \" to escape
// 8. PostgreSQL dollar-quoted strings
// 9. mybatis parameter string

function createStringPattern(stringTypes) {
  var patterns = {
    '``': '((`[^`]*($|`))+)',
    '{}': '((\\{[^\\}]*($|\\}))+)',
    '[]': '((\\[[^\\]]*($|\\]))(\\][^\\]]*($|\\]))*)',
    '""': '(("[^"\\\\]*(?:\\\\.[^"\\\\]*)*("|$))+)',
    "''": "(('[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)",
    "N''": "((N'[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)",
    "U&''": "((U&'[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)",
    'U&""': '((U&"[^"\\\\]*(?:\\\\.[^"\\\\]*)*("|$))+)',
    $$: '((?<tag>\\$\\w*\\$)[\\s\\S]*?(?:\\k<tag>|$))',
    'mybatis': '[#|$]\\{.+?\\}' // add mybatis parameter 

  };
  return stringTypes.map(function (t) {
    return patterns[t];
  }).join('|');
}
function createParenRegex(parens) {
  return new RegExp('^(' + parens.map(escapeParen).join('|') + ')', 'iu');
}

function escapeParen(paren) {
  if (paren.length === 1) {
    // A single punctuation character
    return Object(_utils__WEBPACK_IMPORTED_MODULE_0__["escapeRegExp"])(paren);
  } else {
    // longer word
    return '\\b' + paren + '\\b';
  }
}

function createPlaceholderRegex(types, pattern) {
  if (Object(_utils__WEBPACK_IMPORTED_MODULE_0__["isEmpty"])(types)) {
    return false;
  }

  var typesRegex = types.map(_utils__WEBPACK_IMPORTED_MODULE_0__["escapeRegExp"]).join('|');
  return new RegExp("^((?:".concat(typesRegex, ")(?:").concat(pattern, "))"), 'u');
}

/***/ }),

/***/ "./src/core/token.js":
/*!***************************!*\
  !*** ./src/core/token.js ***!
  \***************************/
/*! exports provided: isAnd, isBetween, isLimit, isSet, isBy, isWindow, isEnd */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isAnd", function() { return isAnd; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isBetween", function() { return isBetween; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isLimit", function() { return isLimit; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isSet", function() { return isSet; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isBy", function() { return isBy; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isWindow", function() { return isWindow; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isEnd", function() { return isEnd; });
/* harmony import */ var _tokenTypes__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./tokenTypes */ "./src/core/tokenTypes.js");


var isToken = function isToken(type, regex) {
  return function (token) {
    return (token === null || token === void 0 ? void 0 : token.type) === type && regex.test(token === null || token === void 0 ? void 0 : token.value);
  };
};

var isAnd = isToken(_tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_NEWLINE, /^AND$/i);
var isBetween = isToken(_tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED, /^BETWEEN$/i);
var isLimit = isToken(_tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_TOP_LEVEL, /^LIMIT$/i);
var isSet = isToken(_tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_TOP_LEVEL, /^[S\u017F]ET$/i);
var isBy = isToken(_tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED, /^BY$/i);
var isWindow = isToken(_tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].RESERVED_TOP_LEVEL, /^WINDOW$/i);
var isEnd = isToken(_tokenTypes__WEBPACK_IMPORTED_MODULE_0__["default"].CLOSE_PAREN, /^END$/i);

/***/ }),

/***/ "./src/core/tokenTypes.js":
/*!********************************!*\
  !*** ./src/core/tokenTypes.js ***!
  \********************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/**
 * Constants for token types
 */
/* harmony default export */ __webpack_exports__["default"] = ({
  WORD: 'word',
  STRING: 'string',
  RESERVED: 'reserved',
  RESERVED_TOP_LEVEL: 'reserved-top-level',
  RESERVED_TOP_LEVEL_NO_INDENT: 'reserved-top-level-no-indent',
  RESERVED_NEWLINE: 'reserved-newline',
  OPERATOR: 'operator',
  OPEN_PAREN: 'open-paren',
  CLOSE_PAREN: 'close-paren',
  LINE_COMMENT: 'line-comment',
  BLOCK_COMMENT: 'block-comment',
  NUMBER: 'number',
  PLACEHOLDER: 'placeholder'
});

/***/ }),

/***/ "./src/languages/Db2Formatter.js":
/*!***************************************!*\
  !*** ./src/languages/Db2Formatter.js ***!
  \***************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return Db2Formatter; });
/* harmony import */ var _core_Formatter__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../core/Formatter */ "./src/core/Formatter.js");
/* harmony import */ var _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../core/Tokenizer */ "./src/core/Tokenizer.js");
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _createSuper(Derived) { var hasNativeReflectConstruct = _isNativeReflectConstruct(); return function _createSuperInternal() { var Super = _getPrototypeOf(Derived), result; if (hasNativeReflectConstruct) { var NewTarget = _getPrototypeOf(this).constructor; result = Reflect.construct(Super, arguments, NewTarget); } else { result = Super.apply(this, arguments); } return _possibleConstructorReturn(this, result); }; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _isNativeReflectConstruct() { if (typeof Reflect === "undefined" || !Reflect.construct) return false; if (Reflect.construct.sham) return false; if (typeof Proxy === "function") return true; try { Date.prototype.toString.call(Reflect.construct(Date, [], function () {})); return true; } catch (e) { return false; } }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }



var reservedWords = ['ABS', 'ACTIVATE', 'ALIAS', 'ALL', 'ALLOCATE', 'ALLOW', 'ALTER', 'ANY', 'ARE', 'ARRAY', 'AS', 'ASC', 'ASENSITIVE', 'ASSOCIATE', 'ASUTIME', 'ASYMMETRIC', 'AT', 'ATOMIC', 'ATTRIBUTES', 'AUDIT', 'AUTHORIZATION', 'AUX', 'AUXILIARY', 'AVG', 'BEFORE', 'BEGIN', 'BETWEEN', 'BIGINT', 'BINARY', 'BLOB', 'BOOLEAN', 'BOTH', 'BUFFERPOOL', 'BY', 'CACHE', 'CALL', 'CALLED', 'CAPTURE', 'CARDINALITY', 'CASCADED', 'CASE', 'CAST', 'CCSID', 'CEIL', 'CEILING', 'CHAR', 'CHARACTER', 'CHARACTER_LENGTH', 'CHAR_LENGTH', 'CHECK', 'CLOB', 'CLONE', 'CLOSE', 'CLUSTER', 'COALESCE', 'COLLATE', 'COLLECT', 'COLLECTION', 'COLLID', 'COLUMN', 'COMMENT', 'COMMIT', 'CONCAT', 'CONDITION', 'CONNECT', 'CONNECTION', 'CONSTRAINT', 'CONTAINS', 'CONTINUE', 'CONVERT', 'CORR', 'CORRESPONDING', 'COUNT', 'COUNT_BIG', 'COVAR_POP', 'COVAR_SAMP', 'CREATE', 'CROSS', 'CUBE', 'CUME_DIST', 'CURRENT', 'CURRENT_DATE', 'CURRENT_DEFAULT_TRANSFORM_GROUP', 'CURRENT_LC_CTYPE', 'CURRENT_PATH', 'CURRENT_ROLE', 'CURRENT_SCHEMA', 'CURRENT_SERVER', 'CURRENT_TIME', 'CURRENT_TIMESTAMP', 'CURRENT_TIMEZONE', 'CURRENT_TRANSFORM_GROUP_FOR_TYPE', 'CURRENT_USER', 'CURSOR', 'CYCLE', 'DATA', 'DATABASE', 'DATAPARTITIONNAME', 'DATAPARTITIONNUM', 'DATE', 'DAY', 'DAYS', 'DB2GENERAL', 'DB2GENRL', 'DB2SQL', 'DBINFO', 'DBPARTITIONNAME', 'DBPARTITIONNUM', 'DEALLOCATE', 'DEC', 'DECIMAL', 'DECLARE', 'DEFAULT', 'DEFAULTS', 'DEFINITION', 'DELETE', 'DENSERANK', 'DENSE_RANK', 'DEREF', 'DESCRIBE', 'DESCRIPTOR', 'DETERMINISTIC', 'DIAGNOSTICS', 'DISABLE', 'DISALLOW', 'DISCONNECT', 'DISTINCT', 'DO', 'DOCUMENT', 'DOUBLE', 'DROP', 'DSSIZE', 'DYNAMIC', 'EACH', 'EDITPROC', 'ELEMENT', 'ELSE', 'ELSEIF', 'ENABLE', 'ENCODING', 'ENCRYPTION', 'END', 'END-EXEC', 'ENDING', 'ERASE', 'ESCAPE', 'EVERY', 'EXCEPTION', 'EXCLUDING', 'EXCLUSIVE', 'EXEC', 'EXECUTE', 'EXISTS', 'EXIT', 'EXP', 'EXPLAIN', 'EXTENDED', 'EXTERNAL', 'EXTRACT', 'FALSE', 'FENCED', 'FETCH', 'FIELDPROC', 'FILE', 'FILTER', 'FINAL', 'FIRST', 'FLOAT', 'FLOOR', 'FOR', 'FOREIGN', 'FREE', 'FULL', 'FUNCTION', 'FUSION', 'GENERAL', 'GENERATED', 'GET', 'GLOBAL', 'GOTO', 'GRANT', 'GRAPHIC', 'GROUP', 'GROUPING', 'HANDLER', 'HASH', 'HASHED_VALUE', 'HINT', 'HOLD', 'HOUR', 'HOURS', 'IDENTITY', 'IF', 'IMMEDIATE', 'IN', 'INCLUDING', 'INCLUSIVE', 'INCREMENT', 'INDEX', 'INDICATOR', 'INDICATORS', 'INF', 'INFINITY', 'INHERIT', 'INNER', 'INOUT', 'INSENSITIVE', 'INSERT', 'INT', 'INTEGER', 'INTEGRITY', 'INTERSECTION', 'INTERVAL', 'INTO', 'IS', 'ISOBID', 'ISOLATION', 'ITERATE', 'JAR', 'JAVA', 'KEEP', 'KEY', 'LABEL', 'LANGUAGE', 'LARGE', 'LATERAL', 'LC_CTYPE', 'LEADING', 'LEAVE', 'LEFT', 'LIKE', 'LINKTYPE', 'LN', 'LOCAL', 'LOCALDATE', 'LOCALE', 'LOCALTIME', 'LOCALTIMESTAMP', 'LOCATOR', 'LOCATORS', 'LOCK', 'LOCKMAX', 'LOCKSIZE', 'LONG', 'LOOP', 'LOWER', 'MAINTAINED', 'MATCH', 'MATERIALIZED', 'MAX', 'MAXVALUE', 'MEMBER', 'MERGE', 'METHOD', 'MICROSECOND', 'MICROSECONDS', 'MIN', 'MINUTE', 'MINUTES', 'MINVALUE', 'MOD', 'MODE', 'MODIFIES', 'MODULE', 'MONTH', 'MONTHS', 'MULTISET', 'NAN', 'NATIONAL', 'NATURAL', 'NCHAR', 'NCLOB', 'NEW', 'NEW_TABLE', 'NEXTVAL', 'NO', 'NOCACHE', 'NOCYCLE', 'NODENAME', 'NODENUMBER', 'NOMAXVALUE', 'NOMINVALUE', 'NONE', 'NOORDER', 'NORMALIZE', 'NORMALIZED', 'NOT', 'NULL', 'NULLIF', 'NULLS', 'NUMERIC', 'NUMPARTS', 'OBID', 'OCTET_LENGTH', 'OF', 'OFFSET', 'OLD', 'OLD_TABLE', 'ON', 'ONLY', 'OPEN', 'OPTIMIZATION', 'OPTIMIZE', 'OPTION', 'ORDER', 'OUT', 'OUTER', 'OVER', 'OVERLAPS', 'OVERLAY', 'OVERRIDING', 'PACKAGE', 'PADDED', 'PAGESIZE', 'PARAMETER', 'PART', 'PARTITION', 'PARTITIONED', 'PARTITIONING', 'PARTITIONS', 'PASSWORD', 'PATH', 'PERCENTILE_CONT', 'PERCENTILE_DISC', 'PERCENT_RANK', 'PIECESIZE', 'PLAN', 'POSITION', 'POWER', 'PRECISION', 'PREPARE', 'PREVVAL', 'PRIMARY', 'PRIQTY', 'PRIVILEGES', 'PROCEDURE', 'PROGRAM', 'PSID', 'PUBLIC', 'QUERY', 'QUERYNO', 'RANGE', 'RANK', 'READ', 'READS', 'REAL', 'RECOVERY', 'RECURSIVE', 'REF', 'REFERENCES', 'REFERENCING', 'REFRESH', 'REGR_AVGX', 'REGR_AVGY', 'REGR_COUNT', 'REGR_INTERCEPT', 'REGR_R2', 'REGR_SLOPE', 'REGR_SXX', 'REGR_SXY', 'REGR_SYY', 'RELEASE', 'RENAME', 'REPEAT', 'RESET', 'RESIGNAL', 'RESTART', 'RESTRICT', 'RESULT', 'RESULT_SET_LOCATOR', 'RETURN', 'RETURNS', 'REVOKE', 'RIGHT', 'ROLE', 'ROLLBACK', 'ROLLUP', 'ROUND_CEILING', 'ROUND_DOWN', 'ROUND_FLOOR', 'ROUND_HALF_DOWN', 'ROUND_HALF_EVEN', 'ROUND_HALF_UP', 'ROUND_UP', 'ROUTINE', 'ROW', 'ROWNUMBER', 'ROWS', 'ROWSET', 'ROW_NUMBER', 'RRN', 'RUN', 'SAVEPOINT', 'SCHEMA', 'SCOPE', 'SCRATCHPAD', 'SCROLL', 'SEARCH', 'SECOND', 'SECONDS', 'SECQTY', 'SECURITY', 'SENSITIVE', 'SEQUENCE', 'SESSION', 'SESSION_USER', 'SIGNAL', 'SIMILAR', 'SIMPLE', 'SMALLINT', 'SNAN', 'SOME', 'SOURCE', 'SPECIFIC', 'SPECIFICTYPE', 'SQL', 'SQLEXCEPTION', 'SQLID', 'SQLSTATE', 'SQLWARNING', 'SQRT', 'STACKED', 'STANDARD', 'START', 'STARTING', 'STATEMENT', 'STATIC', 'STATMENT', 'STAY', 'STDDEV_POP', 'STDDEV_SAMP', 'STOGROUP', 'STORES', 'STYLE', 'SUBMULTISET', 'SUBSTRING', 'SUM', 'SUMMARY', 'SYMMETRIC', 'SYNONYM', 'SYSFUN', 'SYSIBM', 'SYSPROC', 'SYSTEM', 'SYSTEM_USER', 'TABLE', 'TABLESAMPLE', 'TABLESPACE', 'THEN', 'TIME', 'TIMESTAMP', 'TIMEZONE_HOUR', 'TIMEZONE_MINUTE', 'TO', 'TRAILING', 'TRANSACTION', 'TRANSLATE', 'TRANSLATION', 'TREAT', 'TRIGGER', 'TRIM', 'TRUE', 'TRUNCATE', 'TYPE', 'UESCAPE', 'UNDO', 'UNIQUE', 'UNKNOWN', 'UNNEST', 'UNTIL', 'UPPER', 'USAGE', 'USER', 'USING', 'VALIDPROC', 'VALUE', 'VARCHAR', 'VARIABLE', 'VARIANT', 'VARYING', 'VAR_POP', 'VAR_SAMP', 'VCAT', 'VERSION', 'VIEW', 'VOLATILE', 'VOLUMES', 'WHEN', 'WHENEVER', 'WHILE', 'WIDTH_BUCKET', 'WINDOW', 'WITH', 'WITHIN', 'WITHOUT', 'WLM', 'WRITE', 'XMLELEMENT', 'XMLEXISTS', 'XMLNAMESPACES', 'YEAR', 'YEARS'];
var reservedTopLevelWords = ['ADD', 'AFTER', 'ALTER COLUMN', 'ALTER TABLE', 'DELETE FROM', 'EXCEPT', 'FETCH FIRST', 'FROM', 'GROUP BY', 'GO', 'HAVING', 'INSERT INTO', 'INTERSECT', 'LIMIT', 'ORDER BY', 'SELECT', 'SET CURRENT SCHEMA', 'SET SCHEMA', 'SET', 'UPDATE', 'VALUES', 'WHERE'];
var reservedTopLevelWordsNoIndent = ['INTERSECT', 'INTERSECT ALL', 'MINUS', 'UNION', 'UNION ALL'];
var reservedNewlineWords = ['AND', 'OR', // joins
'JOIN', 'INNER JOIN', 'LEFT JOIN', 'LEFT OUTER JOIN', 'RIGHT JOIN', 'RIGHT OUTER JOIN', 'FULL JOIN', 'FULL OUTER JOIN', 'CROSS JOIN', 'NATURAL JOIN']; // For reference: https://www.ibm.com/support/knowledgecenter/en/ssw_ibm_i_72/db2/rbafzintro.htm

var Db2Formatter = /*#__PURE__*/function (_Formatter) {
  _inherits(Db2Formatter, _Formatter);

  var _super = _createSuper(Db2Formatter);

  function Db2Formatter() {
    _classCallCheck(this, Db2Formatter);

    return _super.apply(this, arguments);
  }

  _createClass(Db2Formatter, [{
    key: "tokenizer",
    value: function tokenizer() {
      return new _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__["default"]({
        reservedWords: reservedWords,
        reservedTopLevelWords: reservedTopLevelWords,
        reservedNewlineWords: reservedNewlineWords,
        reservedTopLevelWordsNoIndent: reservedTopLevelWordsNoIndent,
        stringTypes: ["\"\"", "''", '``', '[]'],
        openParens: ['('],
        closeParens: [')'],
        indexedPlaceholderTypes: ['?'],
        namedPlaceholderTypes: [':'],
        lineCommentTypes: ['--'],
        specialWordChars: ['#', '@'],
        operators: ['**', '!=', '!>', '!>', '||']
      });
    }
  }]);

  return Db2Formatter;
}(_core_Formatter__WEBPACK_IMPORTED_MODULE_0__["default"]);



/***/ }),

/***/ "./src/languages/MariaDbFormatter.js":
/*!*******************************************!*\
  !*** ./src/languages/MariaDbFormatter.js ***!
  \*******************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return MariaDbFormatter; });
/* harmony import */ var _core_Formatter__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../core/Formatter */ "./src/core/Formatter.js");
/* harmony import */ var _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../core/Tokenizer */ "./src/core/Tokenizer.js");
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _createSuper(Derived) { var hasNativeReflectConstruct = _isNativeReflectConstruct(); return function _createSuperInternal() { var Super = _getPrototypeOf(Derived), result; if (hasNativeReflectConstruct) { var NewTarget = _getPrototypeOf(this).constructor; result = Reflect.construct(Super, arguments, NewTarget); } else { result = Super.apply(this, arguments); } return _possibleConstructorReturn(this, result); }; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _isNativeReflectConstruct() { if (typeof Reflect === "undefined" || !Reflect.construct) return false; if (Reflect.construct.sham) return false; if (typeof Proxy === "function") return true; try { Date.prototype.toString.call(Reflect.construct(Date, [], function () {})); return true; } catch (e) { return false; } }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }



var reservedWords = ['ACCESSIBLE', 'ADD', 'ALL', 'ALTER', 'ANALYZE', 'AND', 'AS', 'ASC', 'ASENSITIVE', 'BEFORE', 'BETWEEN', 'BIGINT', 'BINARY', 'BLOB', 'BOTH', 'BY', 'CALL', 'CASCADE', 'CASE', 'CHANGE', 'CHAR', 'CHARACTER', 'CHECK', 'COLLATE', 'COLUMN', 'CONDITION', 'CONSTRAINT', 'CONTINUE', 'CONVERT', 'CREATE', 'CROSS', 'CURRENT_DATE', 'CURRENT_ROLE', 'CURRENT_TIME', 'CURRENT_TIMESTAMP', 'CURRENT_USER', 'CURSOR', 'DATABASE', 'DATABASES', 'DAY_HOUR', 'DAY_MICROSECOND', 'DAY_MINUTE', 'DAY_SECOND', 'DEC', 'DECIMAL', 'DECLARE', 'DEFAULT', 'DELAYED', 'DELETE', 'DESC', 'DESCRIBE', 'DETERMINISTIC', 'DISTINCT', 'DISTINCTROW', 'DIV', 'DO_DOMAIN_IDS', 'DOUBLE', 'DROP', 'DUAL', 'EACH', 'ELSE', 'ELSEIF', 'ENCLOSED', 'ESCAPED', 'EXCEPT', 'EXISTS', 'EXIT', 'EXPLAIN', 'FALSE', 'FETCH', 'FLOAT', 'FLOAT4', 'FLOAT8', 'FOR', 'FORCE', 'FOREIGN', 'FROM', 'FULLTEXT', 'GENERAL', 'GRANT', 'GROUP', 'HAVING', 'HIGH_PRIORITY', 'HOUR_MICROSECOND', 'HOUR_MINUTE', 'HOUR_SECOND', 'IF', 'IGNORE', 'IGNORE_DOMAIN_IDS', 'IGNORE_SERVER_IDS', 'IN', 'INDEX', 'INFILE', 'INNER', 'INOUT', 'INSENSITIVE', 'INSERT', 'INT', 'INT1', 'INT2', 'INT3', 'INT4', 'INT8', 'INTEGER', 'INTERSECT', 'INTERVAL', 'INTO', 'IS', 'ITERATE', 'JOIN', 'KEY', 'KEYS', 'KILL', 'LEADING', 'LEAVE', 'LEFT', 'LIKE', 'LIMIT', 'LINEAR', 'LINES', 'LOAD', 'LOCALTIME', 'LOCALTIMESTAMP', 'LOCK', 'LONG', 'LONGBLOB', 'LONGTEXT', 'LOOP', 'LOW_PRIORITY', 'MASTER_HEARTBEAT_PERIOD', 'MASTER_SSL_VERIFY_SERVER_CERT', 'MATCH', 'MAXVALUE', 'MEDIUMBLOB', 'MEDIUMINT', 'MEDIUMTEXT', 'MIDDLEINT', 'MINUTE_MICROSECOND', 'MINUTE_SECOND', 'MOD', 'MODIFIES', 'NATURAL', 'NOT', 'NO_WRITE_TO_BINLOG', 'NULL', 'NUMERIC', 'ON', 'OPTIMIZE', 'OPTION', 'OPTIONALLY', 'OR', 'ORDER', 'OUT', 'OUTER', 'OUTFILE', 'OVER', 'PAGE_CHECKSUM', 'PARSE_VCOL_EXPR', 'PARTITION', 'POSITION', 'PRECISION', 'PRIMARY', 'PROCEDURE', 'PURGE', 'RANGE', 'READ', 'READS', 'READ_WRITE', 'REAL', 'RECURSIVE', 'REF_SYSTEM_ID', 'REFERENCES', 'REGEXP', 'RELEASE', 'RENAME', 'REPEAT', 'REPLACE', 'REQUIRE', 'RESIGNAL', 'RESTRICT', 'RETURN', 'RETURNING', 'REVOKE', 'RIGHT', 'RLIKE', 'ROWS', 'SCHEMA', 'SCHEMAS', 'SECOND_MICROSECOND', 'SELECT', 'SENSITIVE', 'SEPARATOR', 'SET', 'SHOW', 'SIGNAL', 'SLOW', 'SMALLINT', 'SPATIAL', 'SPECIFIC', 'SQL', 'SQLEXCEPTION', 'SQLSTATE', 'SQLWARNING', 'SQL_BIG_RESULT', 'SQL_CALC_FOUND_ROWS', 'SQL_SMALL_RESULT', 'SSL', 'STARTING', 'STATS_AUTO_RECALC', 'STATS_PERSISTENT', 'STATS_SAMPLE_PAGES', 'STRAIGHT_JOIN', 'TABLE', 'TERMINATED', 'THEN', 'TINYBLOB', 'TINYINT', 'TINYTEXT', 'TO', 'TRAILING', 'TRIGGER', 'TRUE', 'UNDO', 'UNION', 'UNIQUE', 'UNLOCK', 'UNSIGNED', 'UPDATE', 'USAGE', 'USE', 'USING', 'UTC_DATE', 'UTC_TIME', 'UTC_TIMESTAMP', 'VALUES', 'VARBINARY', 'VARCHAR', 'VARCHARACTER', 'VARYING', 'WHEN', 'WHERE', 'WHILE', 'WINDOW', 'WITH', 'WRITE', 'XOR', 'YEAR_MONTH', 'ZEROFILL'];
var reservedTopLevelWords = ['ADD', 'ALTER COLUMN', 'ALTER TABLE', 'DELETE FROM', 'EXCEPT', 'FROM', 'GROUP BY', 'HAVING', 'INSERT INTO', 'INSERT', 'LIMIT', 'ORDER BY', 'SELECT', 'SET', 'UPDATE', 'VALUES', 'WHERE'];
var reservedTopLevelWordsNoIndent = ['INTERSECT', 'INTERSECT ALL', 'UNION', 'UNION ALL'];
var reservedNewlineWords = ['AND', 'ELSE', 'OR', 'WHEN', // joins
'JOIN', 'INNER JOIN', 'LEFT JOIN', 'LEFT OUTER JOIN', 'RIGHT JOIN', 'RIGHT OUTER JOIN', 'CROSS JOIN', 'NATURAL JOIN', // non-standard joins
'STRAIGHT_JOIN', 'NATURAL LEFT JOIN', 'NATURAL LEFT OUTER JOIN', 'NATURAL RIGHT JOIN', 'NATURAL RIGHT OUTER JOIN']; // For reference: https://mariadb.com/kb/en/sql-statements-structure/

var MariaDbFormatter = /*#__PURE__*/function (_Formatter) {
  _inherits(MariaDbFormatter, _Formatter);

  var _super = _createSuper(MariaDbFormatter);

  function MariaDbFormatter() {
    _classCallCheck(this, MariaDbFormatter);

    return _super.apply(this, arguments);
  }

  _createClass(MariaDbFormatter, [{
    key: "tokenizer",
    value: function tokenizer() {
      return new _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__["default"]({
        reservedWords: reservedWords,
        reservedTopLevelWords: reservedTopLevelWords,
        reservedNewlineWords: reservedNewlineWords,
        reservedTopLevelWordsNoIndent: reservedTopLevelWordsNoIndent,
        stringTypes: ['``', "''", '""'],
        openParens: ['(', 'CASE'],
        closeParens: [')', 'END'],
        indexedPlaceholderTypes: ['?'],
        namedPlaceholderTypes: [],
        lineCommentTypes: ['--', '#'],
        specialWordChars: ['@'],
        operators: [':=', '<<', '>>', '!=', '<>', '<=>', '&&', '||']
      });
    }
  }]);

  return MariaDbFormatter;
}(_core_Formatter__WEBPACK_IMPORTED_MODULE_0__["default"]);



/***/ }),

/***/ "./src/languages/MySqlFormatter.js":
/*!*****************************************!*\
  !*** ./src/languages/MySqlFormatter.js ***!
  \*****************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return MySqlFormatter; });
/* harmony import */ var _core_Formatter__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../core/Formatter */ "./src/core/Formatter.js");
/* harmony import */ var _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../core/Tokenizer */ "./src/core/Tokenizer.js");
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _createSuper(Derived) { var hasNativeReflectConstruct = _isNativeReflectConstruct(); return function _createSuperInternal() { var Super = _getPrototypeOf(Derived), result; if (hasNativeReflectConstruct) { var NewTarget = _getPrototypeOf(this).constructor; result = Reflect.construct(Super, arguments, NewTarget); } else { result = Super.apply(this, arguments); } return _possibleConstructorReturn(this, result); }; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _isNativeReflectConstruct() { if (typeof Reflect === "undefined" || !Reflect.construct) return false; if (Reflect.construct.sham) return false; if (typeof Proxy === "function") return true; try { Date.prototype.toString.call(Reflect.construct(Date, [], function () {})); return true; } catch (e) { return false; } }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }



var reservedWords = ['ACCESSIBLE', 'ADD', 'ALL', 'ALTER', 'ANALYZE', 'AND', 'AS', 'ASC', 'ASENSITIVE', 'BEFORE', 'BETWEEN', 'BIGINT', 'BINARY', 'BLOB', 'BOTH', 'BY', 'CALL', 'CASCADE', 'CASE', 'CHANGE', 'CHAR', 'CHARACTER', 'CHECK', 'COLLATE', 'COLUMN', 'CONDITION', 'CONSTRAINT', 'CONTINUE', 'CONVERT', 'CREATE', 'CROSS', 'CUBE', 'CUME_DIST', 'CURRENT_DATE', 'CURRENT_TIME', 'CURRENT_TIMESTAMP', 'CURRENT_USER', 'CURSOR', 'DATABASE', 'DATABASES', 'DAY_HOUR', 'DAY_MICROSECOND', 'DAY_MINUTE', 'DAY_SECOND', 'DEC', 'DECIMAL', 'DECLARE', 'DEFAULT', 'DELAYED', 'DELETE', 'DENSE_RANK', 'DESC', 'DESCRIBE', 'DETERMINISTIC', 'DISTINCT', 'DISTINCTROW', 'DIV', 'DOUBLE', 'DROP', 'DUAL', 'EACH', 'ELSE', 'ELSEIF', 'EMPTY', 'ENCLOSED', 'ESCAPED', 'EXCEPT', 'EXISTS', 'EXIT', 'EXPLAIN', 'FALSE', 'FETCH', 'FIRST_VALUE', 'FLOAT', 'FLOAT4', 'FLOAT8', 'FOR', 'FORCE', 'FOREIGN', 'FROM', 'FULLTEXT', 'FUNCTION', 'GENERATED', 'GET', 'GRANT', 'GROUP', 'GROUPING', 'GROUPS', 'HAVING', 'HIGH_PRIORITY', 'HOUR_MICROSECOND', 'HOUR_MINUTE', 'HOUR_SECOND', 'IF', 'IGNORE', 'IN', 'INDEX', 'INFILE', 'INNER', 'INOUT', 'INSENSITIVE', 'INSERT', 'INT', 'INT1', 'INT2', 'INT3', 'INT4', 'INT8', 'INTEGER', 'INTERVAL', 'INTO', 'IO_AFTER_GTIDS', 'IO_BEFORE_GTIDS', 'IS', 'ITERATE', 'JOIN', 'JSON_TABLE', 'KEY', 'KEYS', 'KILL', 'LAG', 'LAST_VALUE', 'LATERAL', 'LEAD', 'LEADING', 'LEAVE', 'LEFT', 'LIKE', 'LIMIT', 'LINEAR', 'LINES', 'LOAD', 'LOCALTIME', 'LOCALTIMESTAMP', 'LOCK', 'LONG', 'LONGBLOB', 'LONGTEXT', 'LOOP', 'LOW_PRIORITY', 'MASTER_BIND', 'MASTER_SSL_VERIFY_SERVER_CERT', 'MATCH', 'MAXVALUE', 'MEDIUMBLOB', 'MEDIUMINT', 'MEDIUMTEXT', 'MIDDLEINT', 'MINUTE_MICROSECOND', 'MINUTE_SECOND', 'MOD', 'MODIFIES', 'NATURAL', 'NOT', 'NO_WRITE_TO_BINLOG', 'NTH_VALUE', 'NTILE', 'NULL', 'NUMERIC', 'OF', 'ON', 'OPTIMIZE', 'OPTIMIZER_COSTS', 'OPTION', 'OPTIONALLY', 'OR', 'ORDER', 'OUT', 'OUTER', 'OUTFILE', 'OVER', 'PARTITION', 'PERCENT_RANK', 'PRECISION', 'PRIMARY', 'PROCEDURE', 'PURGE', 'RANGE', 'RANK', 'READ', 'READS', 'READ_WRITE', 'REAL', 'RECURSIVE', 'REFERENCES', 'REGEXP', 'RELEASE', 'RENAME', 'REPEAT', 'REPLACE', 'REQUIRE', 'RESIGNAL', 'RESTRICT', 'RETURN', 'REVOKE', 'RIGHT', 'RLIKE', 'ROW', 'ROWS', 'ROW_NUMBER', 'SCHEMA', 'SCHEMAS', 'SECOND_MICROSECOND', 'SELECT', 'SENSITIVE', 'SEPARATOR', 'SET', 'SHOW', 'SIGNAL', 'SMALLINT', 'SPATIAL', 'SPECIFIC', 'SQL', 'SQLEXCEPTION', 'SQLSTATE', 'SQLWARNING', 'SQL_BIG_RESULT', 'SQL_CALC_FOUND_ROWS', 'SQL_SMALL_RESULT', 'SSL', 'STARTING', 'STORED', 'STRAIGHT_JOIN', 'SYSTEM', 'TABLE', 'TERMINATED', 'THEN', 'TINYBLOB', 'TINYINT', 'TINYTEXT', 'TO', 'TRAILING', 'TRIGGER', 'TRUE', 'UNDO', 'UNION', 'UNIQUE', 'UNLOCK', 'UNSIGNED', 'UPDATE', 'USAGE', 'USE', 'USING', 'UTC_DATE', 'UTC_TIME', 'UTC_TIMESTAMP', 'VALUES', 'VARBINARY', 'VARCHAR', 'VARCHARACTER', 'VARYING', 'VIRTUAL', 'WHEN', 'WHERE', 'WHILE', 'WINDOW', 'WITH', 'WRITE', 'XOR', 'YEAR_MONTH', 'ZEROFILL'];
var reservedTopLevelWords = ['ADD', 'ALTER COLUMN', 'ALTER TABLE', 'DELETE FROM', 'EXCEPT', 'FROM', 'GROUP BY', 'HAVING', 'INSERT INTO', 'INSERT', 'LIMIT', 'ORDER BY', 'SELECT', 'SET', 'UPDATE', 'VALUES', 'WHERE'];
var reservedTopLevelWordsNoIndent = ['INTERSECT', 'INTERSECT ALL', 'UNION', 'UNION ALL'];
var reservedNewlineWords = ['AND', 'ELSE', 'OR', 'WHEN', // joins
'JOIN', 'INNER JOIN', 'LEFT JOIN', 'LEFT OUTER JOIN', 'RIGHT JOIN', 'RIGHT OUTER JOIN', 'CROSS JOIN', 'NATURAL JOIN', // non-standard joins
'STRAIGHT_JOIN', 'NATURAL LEFT JOIN', 'NATURAL LEFT OUTER JOIN', 'NATURAL RIGHT JOIN', 'NATURAL RIGHT OUTER JOIN'];

var MySqlFormatter = /*#__PURE__*/function (_Formatter) {
  _inherits(MySqlFormatter, _Formatter);

  var _super = _createSuper(MySqlFormatter);

  function MySqlFormatter() {
    _classCallCheck(this, MySqlFormatter);

    return _super.apply(this, arguments);
  }

  _createClass(MySqlFormatter, [{
    key: "tokenizer",
    value: function tokenizer() {
      return new _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__["default"]({
        reservedWords: reservedWords,
        reservedTopLevelWords: reservedTopLevelWords,
        reservedNewlineWords: reservedNewlineWords,
        reservedTopLevelWordsNoIndent: reservedTopLevelWordsNoIndent,
        stringTypes: ['``', "''", '""'],
        openParens: ['(', 'CASE'],
        closeParens: [')', 'END'],
        indexedPlaceholderTypes: ['?'],
        namedPlaceholderTypes: [],
        lineCommentTypes: ['--', '#'],
        specialWordChars: ['@'],
        operators: [':=', '<<', '>>', '!=', '<>', '<=>', '&&', '||', '->', '->>']
      });
    }
  }]);

  return MySqlFormatter;
}(_core_Formatter__WEBPACK_IMPORTED_MODULE_0__["default"]);



/***/ }),

/***/ "./src/languages/N1qlFormatter.js":
/*!****************************************!*\
  !*** ./src/languages/N1qlFormatter.js ***!
  \****************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return N1qlFormatter; });
/* harmony import */ var _core_Formatter__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../core/Formatter */ "./src/core/Formatter.js");
/* harmony import */ var _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../core/Tokenizer */ "./src/core/Tokenizer.js");
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _createSuper(Derived) { var hasNativeReflectConstruct = _isNativeReflectConstruct(); return function _createSuperInternal() { var Super = _getPrototypeOf(Derived), result; if (hasNativeReflectConstruct) { var NewTarget = _getPrototypeOf(this).constructor; result = Reflect.construct(Super, arguments, NewTarget); } else { result = Super.apply(this, arguments); } return _possibleConstructorReturn(this, result); }; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _isNativeReflectConstruct() { if (typeof Reflect === "undefined" || !Reflect.construct) return false; if (Reflect.construct.sham) return false; if (typeof Proxy === "function") return true; try { Date.prototype.toString.call(Reflect.construct(Date, [], function () {})); return true; } catch (e) { return false; } }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }



var reservedWords = ['ALL', 'ALTER', 'ANALYZE', 'AND', 'ANY', 'ARRAY', 'AS', 'ASC', 'BEGIN', 'BETWEEN', 'BINARY', 'BOOLEAN', 'BREAK', 'BUCKET', 'BUILD', 'BY', 'CALL', 'CASE', 'CAST', 'CLUSTER', 'COLLATE', 'COLLECTION', 'COMMIT', 'CONNECT', 'CONTINUE', 'CORRELATE', 'COVER', 'CREATE', 'DATABASE', 'DATASET', 'DATASTORE', 'DECLARE', 'DECREMENT', 'DELETE', 'DERIVED', 'DESC', 'DESCRIBE', 'DISTINCT', 'DO', 'DROP', 'EACH', 'ELEMENT', 'ELSE', 'END', 'EVERY', 'EXCEPT', 'EXCLUDE', 'EXECUTE', 'EXISTS', 'EXPLAIN', 'FALSE', 'FETCH', 'FIRST', 'FLATTEN', 'FOR', 'FORCE', 'FROM', 'FUNCTION', 'GRANT', 'GROUP', 'GSI', 'HAVING', 'IF', 'IGNORE', 'ILIKE', 'IN', 'INCLUDE', 'INCREMENT', 'INDEX', 'INFER', 'INLINE', 'INNER', 'INSERT', 'INTERSECT', 'INTO', 'IS', 'JOIN', 'KEY', 'KEYS', 'KEYSPACE', 'KNOWN', 'LAST', 'LEFT', 'LET', 'LETTING', 'LIKE', 'LIMIT', 'LSM', 'MAP', 'MAPPING', 'MATCHED', 'MATERIALIZED', 'MERGE', 'MISSING', 'NAMESPACE', 'NEST', 'NOT', 'NULL', 'NUMBER', 'OBJECT', 'OFFSET', 'ON', 'OPTION', 'OR', 'ORDER', 'OUTER', 'OVER', 'PARSE', 'PARTITION', 'PASSWORD', 'PATH', 'POOL', 'PREPARE', 'PRIMARY', 'PRIVATE', 'PRIVILEGE', 'PROCEDURE', 'PUBLIC', 'RAW', 'REALM', 'REDUCE', 'RENAME', 'RETURN', 'RETURNING', 'REVOKE', 'RIGHT', 'ROLE', 'ROLLBACK', 'SATISFIES', 'SCHEMA', 'SELECT', 'SELF', 'SEMI', 'SET', 'SHOW', 'SOME', 'START', 'STATISTICS', 'STRING', 'SYSTEM', 'THEN', 'TO', 'TRANSACTION', 'TRIGGER', 'TRUE', 'TRUNCATE', 'UNDER', 'UNION', 'UNIQUE', 'UNKNOWN', 'UNNEST', 'UNSET', 'UPDATE', 'UPSERT', 'USE', 'USER', 'USING', 'VALIDATE', 'VALUE', 'VALUED', 'VALUES', 'VIA', 'VIEW', 'WHEN', 'WHERE', 'WHILE', 'WITH', 'WITHIN', 'WORK', 'XOR'];
var reservedTopLevelWords = ['DELETE FROM', 'EXCEPT ALL', 'EXCEPT', 'EXPLAIN DELETE FROM', 'EXPLAIN UPDATE', 'EXPLAIN UPSERT', 'FROM', 'GROUP BY', 'HAVING', 'INFER', 'INSERT INTO', 'LET', 'LIMIT', 'MERGE', 'NEST', 'ORDER BY', 'PREPARE', 'SELECT', 'SET CURRENT SCHEMA', 'SET SCHEMA', 'SET', 'UNNEST', 'UPDATE', 'UPSERT', 'USE KEYS', 'VALUES', 'WHERE'];
var reservedTopLevelWordsNoIndent = ['INTERSECT', 'INTERSECT ALL', 'MINUS', 'UNION', 'UNION ALL'];
var reservedNewlineWords = ['AND', 'OR', 'XOR', // joins
'JOIN', 'INNER JOIN', 'LEFT JOIN', 'LEFT OUTER JOIN', 'RIGHT JOIN', 'RIGHT OUTER JOIN']; // For reference: http://docs.couchbase.com.s3-website-us-west-1.amazonaws.com/server/6.0/n1ql/n1ql-language-reference/index.html

var N1qlFormatter = /*#__PURE__*/function (_Formatter) {
  _inherits(N1qlFormatter, _Formatter);

  var _super = _createSuper(N1qlFormatter);

  function N1qlFormatter() {
    _classCallCheck(this, N1qlFormatter);

    return _super.apply(this, arguments);
  }

  _createClass(N1qlFormatter, [{
    key: "tokenizer",
    value: function tokenizer() {
      return new _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__["default"]({
        reservedWords: reservedWords,
        reservedTopLevelWords: reservedTopLevelWords,
        reservedNewlineWords: reservedNewlineWords,
        reservedTopLevelWordsNoIndent: reservedTopLevelWordsNoIndent,
        stringTypes: ["\"\"", "''", '``'],
        openParens: ['(', '[', '{'],
        closeParens: [')', ']', '}'],
        namedPlaceholderTypes: ['$'],
        lineCommentTypes: ['#', '--'],
        operators: ['==', '!=']
      });
    }
  }]);

  return N1qlFormatter;
}(_core_Formatter__WEBPACK_IMPORTED_MODULE_0__["default"]);



/***/ }),

/***/ "./src/languages/PlSqlFormatter.js":
/*!*****************************************!*\
  !*** ./src/languages/PlSqlFormatter.js ***!
  \*****************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return PlSqlFormatter; });
/* harmony import */ var _core_Formatter__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../core/Formatter */ "./src/core/Formatter.js");
/* harmony import */ var _core_token__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../core/token */ "./src/core/token.js");
/* harmony import */ var _core_Tokenizer__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../core/Tokenizer */ "./src/core/Tokenizer.js");
/* harmony import */ var _core_tokenTypes__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../core/tokenTypes */ "./src/core/tokenTypes.js");
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _createSuper(Derived) { var hasNativeReflectConstruct = _isNativeReflectConstruct(); return function _createSuperInternal() { var Super = _getPrototypeOf(Derived), result; if (hasNativeReflectConstruct) { var NewTarget = _getPrototypeOf(this).constructor; result = Reflect.construct(Super, arguments, NewTarget); } else { result = Super.apply(this, arguments); } return _possibleConstructorReturn(this, result); }; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _isNativeReflectConstruct() { if (typeof Reflect === "undefined" || !Reflect.construct) return false; if (Reflect.construct.sham) return false; if (typeof Proxy === "function") return true; try { Date.prototype.toString.call(Reflect.construct(Date, [], function () {})); return true; } catch (e) { return false; } }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }





var reservedWords = ['A', 'ACCESSIBLE', 'AGENT', 'AGGREGATE', 'ALL', 'ALTER', 'ANY', 'ARRAY', 'AS', 'ASC', 'AT', 'ATTRIBUTE', 'AUTHID', 'AVG', 'BETWEEN', 'BFILE_BASE', 'BINARY_INTEGER', 'BINARY', 'BLOB_BASE', 'BLOCK', 'BODY', 'BOOLEAN', 'BOTH', 'BOUND', 'BREADTH', 'BULK', 'BY', 'BYTE', 'C', 'CALL', 'CALLING', 'CASCADE', 'CASE', 'CHAR_BASE', 'CHAR', 'CHARACTER', 'CHARSET', 'CHARSETFORM', 'CHARSETID', 'CHECK', 'CLOB_BASE', 'CLONE', 'CLOSE', 'CLUSTER', 'CLUSTERS', 'COALESCE', 'COLAUTH', 'COLLECT', 'COLUMNS', 'COMMENT', 'COMMIT', 'COMMITTED', 'COMPILED', 'COMPRESS', 'CONNECT', 'CONSTANT', 'CONSTRUCTOR', 'CONTEXT', 'CONTINUE', 'CONVERT', 'COUNT', 'CRASH', 'CREATE', 'CREDENTIAL', 'CURRENT', 'CURRVAL', 'CURSOR', 'CUSTOMDATUM', 'DANGLING', 'DATA', 'DATE_BASE', 'DATE', 'DAY', 'DECIMAL', 'DEFAULT', 'DEFINE', 'DELETE', 'DEPTH', 'DESC', 'DETERMINISTIC', 'DIRECTORY', 'DISTINCT', 'DO', 'DOUBLE', 'DROP', 'DURATION', 'ELEMENT', 'ELSIF', 'EMPTY', 'END', 'ESCAPE', 'EXCEPTIONS', 'EXCLUSIVE', 'EXECUTE', 'EXISTS', 'EXIT', 'EXTENDS', 'EXTERNAL', 'EXTRACT', 'FALSE', 'FETCH', 'FINAL', 'FIRST', 'FIXED', 'FLOAT', 'FOR', 'FORALL', 'FORCE', 'FROM', 'FUNCTION', 'GENERAL', 'GOTO', 'GRANT', 'GROUP', 'HASH', 'HEAP', 'HIDDEN', 'HOUR', 'IDENTIFIED', 'IF', 'IMMEDIATE', 'IN', 'INCLUDING', 'INDEX', 'INDEXES', 'INDICATOR', 'INDICES', 'INFINITE', 'INSTANTIABLE', 'INT', 'INTEGER', 'INTERFACE', 'INTERVAL', 'INTO', 'INVALIDATE', 'IS', 'ISOLATION', 'JAVA', 'LANGUAGE', 'LARGE', 'LEADING', 'LENGTH', 'LEVEL', 'LIBRARY', 'LIKE', 'LIKE2', 'LIKE4', 'LIKEC', 'LIMITED', 'LOCAL', 'LOCK', 'LONG', 'MAP', 'MAX', 'MAXLEN', 'MEMBER', 'MERGE', 'MIN', 'MINUTE', 'MLSLABEL', 'MOD', 'MODE', 'MONTH', 'MULTISET', 'NAME', 'NAN', 'NATIONAL', 'NATIVE', 'NATURAL', 'NATURALN', 'NCHAR', 'NEW', 'NEXTVAL', 'NOCOMPRESS', 'NOCOPY', 'NOT', 'NOWAIT', 'NULL', 'NULLIF', 'NUMBER_BASE', 'NUMBER', 'OBJECT', 'OCICOLL', 'OCIDATE', 'OCIDATETIME', 'OCIDURATION', 'OCIINTERVAL', 'OCILOBLOCATOR', 'OCINUMBER', 'OCIRAW', 'OCIREF', 'OCIREFCURSOR', 'OCIROWID', 'OCISTRING', 'OCITYPE', 'OF', 'OLD', 'ON', 'ONLY', 'OPAQUE', 'OPEN', 'OPERATOR', 'OPTION', 'ORACLE', 'ORADATA', 'ORDER', 'ORGANIZATION', 'ORLANY', 'ORLVARY', 'OTHERS', 'OUT', 'OVERLAPS', 'OVERRIDING', 'PACKAGE', 'PARALLEL_ENABLE', 'PARAMETER', 'PARAMETERS', 'PARENT', 'PARTITION', 'PASCAL', 'PCTFREE', 'PIPE', 'PIPELINED', 'PLS_INTEGER', 'PLUGGABLE', 'POSITIVE', 'POSITIVEN', 'PRAGMA', 'PRECISION', 'PRIOR', 'PRIVATE', 'PROCEDURE', 'PUBLIC', 'RAISE', 'RANGE', 'RAW', 'READ', 'REAL', 'RECORD', 'REF', 'REFERENCE', 'RELEASE', 'RELIES_ON', 'REM', 'REMAINDER', 'RENAME', 'RESOURCE', 'RESULT_CACHE', 'RESULT', 'RETURN', 'RETURNING', 'REVERSE', 'REVOKE', 'ROLLBACK', 'ROW', 'ROWID', 'ROWNUM', 'ROWTYPE', 'SAMPLE', 'SAVE', 'SAVEPOINT', 'SB1', 'SB2', 'SB4', 'SEARCH', 'SECOND', 'SEGMENT', 'SELF', 'SEPARATE', 'SEQUENCE', 'SERIALIZABLE', 'SHARE', 'SHORT', 'SIZE_T', 'SIZE', 'SMALLINT', 'SOME', 'SPACE', 'SPARSE', 'SQL', 'SQLCODE', 'SQLDATA', 'SQLERRM', 'SQLNAME', 'SQLSTATE', 'STANDARD', 'START', 'STATIC', 'STDDEV', 'STORED', 'STRING', 'STRUCT', 'STYLE', 'SUBMULTISET', 'SUBPARTITION', 'SUBSTITUTABLE', 'SUBTYPE', 'SUCCESSFUL', 'SUM', 'SYNONYM', 'SYSDATE', 'TABAUTH', 'TABLE', 'TDO', 'THE', 'THEN', 'TIME', 'TIMESTAMP', 'TIMEZONE_ABBR', 'TIMEZONE_HOUR', 'TIMEZONE_MINUTE', 'TIMEZONE_REGION', 'TO', 'TRAILING', 'TRANSACTION', 'TRANSACTIONAL', 'TRIGGER', 'TRUE', 'TRUSTED', 'TYPE', 'UB1', 'UB2', 'UB4', 'UID', 'UNDER', 'UNIQUE', 'UNPLUG', 'UNSIGNED', 'UNTRUSTED', 'USE', 'USER', 'USING', 'VALIDATE', 'VALIST', 'VALUE', 'VARCHAR', 'VARCHAR2', 'VARIABLE', 'VARIANCE', 'VARRAY', 'VARYING', 'VIEW', 'VIEWS', 'VOID', 'WHENEVER', 'WHILE', 'WITH', 'WORK', 'WRAPPED', 'WRITE', 'YEAR', 'ZONE'];
var reservedTopLevelWords = ['ADD', 'ALTER COLUMN', 'ALTER TABLE', 'BEGIN', 'CONNECT BY', 'DECLARE', 'DELETE FROM', 'DELETE', 'END', 'EXCEPT', 'EXCEPTION', 'FETCH FIRST', 'FROM', 'GROUP BY', 'HAVING', 'INSERT INTO', 'INSERT', 'LIMIT', 'LOOP', 'MODIFY', 'ORDER BY', 'SELECT', 'SET CURRENT SCHEMA', 'SET SCHEMA', 'SET', 'START WITH', 'UPDATE', 'VALUES', 'WHERE'];
var reservedTopLevelWordsNoIndent = ['INTERSECT', 'INTERSECT ALL', 'MINUS', 'UNION', 'UNION ALL'];
var reservedNewlineWords = ['AND', 'CROSS APPLY', 'ELSE', 'END', 'OR', 'OUTER APPLY', 'WHEN', 'XOR', // joins
'JOIN', 'INNER JOIN', 'LEFT JOIN', 'LEFT OUTER JOIN', 'RIGHT JOIN', 'RIGHT OUTER JOIN', 'FULL JOIN', 'FULL OUTER JOIN', 'CROSS JOIN', 'NATURAL JOIN'];

var PlSqlFormatter = /*#__PURE__*/function (_Formatter) {
  _inherits(PlSqlFormatter, _Formatter);

  var _super = _createSuper(PlSqlFormatter);

  function PlSqlFormatter() {
    _classCallCheck(this, PlSqlFormatter);

    return _super.apply(this, arguments);
  }

  _createClass(PlSqlFormatter, [{
    key: "tokenizer",
    value: function tokenizer() {
      return new _core_Tokenizer__WEBPACK_IMPORTED_MODULE_2__["default"]({
        reservedWords: reservedWords,
        reservedTopLevelWords: reservedTopLevelWords,
        reservedNewlineWords: reservedNewlineWords,
        reservedTopLevelWordsNoIndent: reservedTopLevelWordsNoIndent,
        stringTypes: ["\"\"", "N''", "''", '``'],
        openParens: ['(', 'CASE'],
        closeParens: [')', 'END'],
        indexedPlaceholderTypes: ['?'],
        namedPlaceholderTypes: [':'],
        lineCommentTypes: ['--'],
        specialWordChars: ['_', '$', '#', '.', '@'],
        operators: ['||', '**', '!=', ':=']
      });
    }
  }, {
    key: "tokenOverride",
    value: function tokenOverride(token) {
      if (Object(_core_token__WEBPACK_IMPORTED_MODULE_1__["isSet"])(token) && Object(_core_token__WEBPACK_IMPORTED_MODULE_1__["isBy"])(this.previousReservedToken)) {
        return {
          type: _core_tokenTypes__WEBPACK_IMPORTED_MODULE_3__["default"].RESERVED,
          value: token.value
        };
      }

      return token;
    }
  }]);

  return PlSqlFormatter;
}(_core_Formatter__WEBPACK_IMPORTED_MODULE_0__["default"]);



/***/ }),

/***/ "./src/languages/PostgreSqlFormatter.js":
/*!**********************************************!*\
  !*** ./src/languages/PostgreSqlFormatter.js ***!
  \**********************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return PostgreSqlFormatter; });
/* harmony import */ var _core_Formatter__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../core/Formatter */ "./src/core/Formatter.js");
/* harmony import */ var _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../core/Tokenizer */ "./src/core/Tokenizer.js");
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _createSuper(Derived) { var hasNativeReflectConstruct = _isNativeReflectConstruct(); return function _createSuperInternal() { var Super = _getPrototypeOf(Derived), result; if (hasNativeReflectConstruct) { var NewTarget = _getPrototypeOf(this).constructor; result = Reflect.construct(Super, arguments, NewTarget); } else { result = Super.apply(this, arguments); } return _possibleConstructorReturn(this, result); }; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _isNativeReflectConstruct() { if (typeof Reflect === "undefined" || !Reflect.construct) return false; if (Reflect.construct.sham) return false; if (typeof Proxy === "function") return true; try { Date.prototype.toString.call(Reflect.construct(Date, [], function () {})); return true; } catch (e) { return false; } }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }



var reservedWords = ['ABORT', 'ABSOLUTE', 'ACCESS', 'ACTION', 'ADD', 'ADMIN', 'AFTER', 'AGGREGATE', 'ALL', 'ALSO', 'ALTER', 'ALWAYS', 'ANALYSE', 'ANALYZE', 'AND', 'ANY', 'ARRAY', 'AS', 'ASC', 'ASSERTION', 'ASSIGNMENT', 'ASYMMETRIC', 'AT', 'ATTACH', 'ATTRIBUTE', 'AUTHORIZATION', 'BACKWARD', 'BEFORE', 'BEGIN', 'BETWEEN', 'BIGINT', 'BINARY', 'BIT', 'BOOLEAN', 'BOTH', 'BY', 'CACHE', 'CALL', 'CALLED', 'CASCADE', 'CASCADED', 'CASE', 'CAST', 'CATALOG', 'CHAIN', 'CHAR', 'CHARACTER', 'CHARACTERISTICS', 'CHECK', 'CHECKPOINT', 'CLASS', 'CLOSE', 'CLUSTER', 'COALESCE', 'COLLATE', 'COLLATION', 'COLUMN', 'COLUMNS', 'COMMENT', 'COMMENTS', 'COMMIT', 'COMMITTED', 'CONCURRENTLY', 'CONFIGURATION', 'CONFLICT', 'CONNECTION', 'CONSTRAINT', 'CONSTRAINTS', 'CONTENT', 'CONTINUE', 'CONVERSION', 'COPY', 'COST', 'CREATE', 'CROSS', 'CSV', 'CUBE', 'CURRENT', 'CURRENT_CATALOG', 'CURRENT_DATE', 'CURRENT_ROLE', 'CURRENT_SCHEMA', 'CURRENT_TIME', 'CURRENT_TIMESTAMP', 'CURRENT_USER', 'CURSOR', 'CYCLE', 'DATA', 'DATABASE', 'DAY', 'DEALLOCATE', 'DEC', 'DECIMAL', 'DECLARE', 'DEFAULT', 'DEFAULTS', 'DEFERRABLE', 'DEFERRED', 'DEFINER', 'DELETE', 'DELIMITER', 'DELIMITERS', 'DEPENDS', 'DESC', 'DETACH', 'DICTIONARY', 'DISABLE', 'DISCARD', 'DISTINCT', 'DO', 'DOCUMENT', 'DOMAIN', 'DOUBLE', 'DROP', 'EACH', 'ELSE', 'ENABLE', 'ENCODING', 'ENCRYPTED', 'END', 'ENUM', 'ESCAPE', 'EVENT', 'EXCEPT', 'EXCLUDE', 'EXCLUDING', 'EXCLUSIVE', 'EXECUTE', 'EXISTS', 'EXPLAIN', 'EXPRESSION', 'EXTENSION', 'EXTERNAL', 'EXTRACT', 'FALSE', 'FAMILY', 'FETCH', 'FILTER', 'FIRST', 'FLOAT', 'FOLLOWING', 'FOR', 'FORCE', 'FOREIGN', 'FORWARD', 'FREEZE', 'FROM', 'FULL', 'FUNCTION', 'FUNCTIONS', 'GENERATED', 'GLOBAL', 'GRANT', 'GRANTED', 'GREATEST', 'GROUP', 'GROUPING', 'GROUPS', 'HANDLER', 'HAVING', 'HEADER', 'HOLD', 'HOUR', 'IDENTITY', 'IF', 'ILIKE', 'IMMEDIATE', 'IMMUTABLE', 'IMPLICIT', 'IMPORT', 'IN', 'INCLUDE', 'INCLUDING', 'INCREMENT', 'INDEX', 'INDEXES', 'INHERIT', 'INHERITS', 'INITIALLY', 'INLINE', 'INNER', 'INOUT', 'INPUT', 'INSENSITIVE', 'INSERT', 'INSTEAD', 'INT', 'INTEGER', 'INTERSECT', 'INTERVAL', 'INTO', 'INVOKER', 'IS', 'ISNULL', 'ISOLATION', 'JOIN', 'KEY', 'LABEL', 'LANGUAGE', 'LARGE', 'LAST', 'LATERAL', 'LEADING', 'LEAKPROOF', 'LEAST', 'LEFT', 'LEVEL', 'LIKE', 'LIMIT', 'LISTEN', 'LOAD', 'LOCAL', 'LOCALTIME', 'LOCALTIMESTAMP', 'LOCATION', 'LOCK', 'LOCKED', 'LOGGED', 'MAPPING', 'MATCH', 'MATERIALIZED', 'MAXVALUE', 'METHOD', 'MINUTE', 'MINVALUE', 'MODE', 'MONTH', 'MOVE', 'NAME', 'NAMES', 'NATIONAL', 'NATURAL', 'NCHAR', 'NEW', 'NEXT', 'NFC', 'NFD', 'NFKC', 'NFKD', 'NO', 'NONE', 'NORMALIZE', 'NORMALIZED', 'NOT', 'NOTHING', 'NOTIFY', 'NOTNULL', 'NOWAIT', 'NULL', 'NULLIF', 'NULLS', 'NUMERIC', 'OBJECT', 'OF', 'OFF', 'OFFSET', 'OIDS', 'OLD', 'ON', 'ONLY', 'OPERATOR', 'OPTION', 'OPTIONS', 'OR', 'ORDER', 'ORDINALITY', 'OTHERS', 'OUT', 'OUTER', 'OVER', 'OVERLAPS', 'OVERLAY', 'OVERRIDING', 'OWNED', 'OWNER', 'PARALLEL', 'PARSER', 'PARTIAL', 'PARTITION', 'PASSING', 'PASSWORD', 'PLACING', 'PLANS', 'POLICY', 'POSITION', 'PRECEDING', 'PRECISION', 'PREPARE', 'PREPARED', 'PRESERVE', 'PRIMARY', 'PRIOR', 'PRIVILEGES', 'PROCEDURAL', 'PROCEDURE', 'PROCEDURES', 'PROGRAM', 'PUBLICATION', 'QUOTE', 'RANGE', 'READ', 'REAL', 'REASSIGN', 'RECHECK', 'RECURSIVE', 'REF', 'REFERENCES', 'REFERENCING', 'REFRESH', 'REINDEX', 'RELATIVE', 'RELEASE', 'RENAME', 'REPEATABLE', 'REPLACE', 'REPLICA', 'RESET', 'RESTART', 'RESTRICT', 'RETURNING', 'RETURNS', 'REVOKE', 'RIGHT', 'ROLE', 'ROLLBACK', 'ROLLUP', 'ROUTINE', 'ROUTINES', 'ROW', 'ROWS', 'RULE', 'SAVEPOINT', 'SCHEMA', 'SCHEMAS', 'SCROLL', 'SEARCH', 'SECOND', 'SECURITY', 'SELECT', 'SEQUENCE', 'SEQUENCES', 'SERIALIZABLE', 'SERVER', 'SESSION', 'SESSION_USER', 'SET', 'SETOF', 'SETS', 'SHARE', 'SHOW', 'SIMILAR', 'SIMPLE', 'SKIP', 'SMALLINT', 'SNAPSHOT', 'SOME', 'SQL', 'STABLE', 'STANDALONE', 'START', 'STATEMENT', 'STATISTICS', 'STDIN', 'STDOUT', 'STORAGE', 'STORED', 'STRICT', 'STRIP', 'SUBSCRIPTION', 'SUBSTRING', 'SUPPORT', 'SYMMETRIC', 'SYSID', 'SYSTEM', 'TABLE', 'TABLES', 'TABLESAMPLE', 'TABLESPACE', 'TEMP', 'TEMPLATE', 'TEMPORARY', 'TEXT', 'THEN', 'TIES', 'TIME', 'TIMESTAMP', 'TO', 'TRAILING', 'TRANSACTION', 'TRANSFORM', 'TREAT', 'TRIGGER', 'TRIM', 'TRUE', 'TRUNCATE', 'TRUSTED', 'TYPE', 'TYPES', 'UESCAPE', 'UNBOUNDED', 'UNCOMMITTED', 'UNENCRYPTED', 'UNION', 'UNIQUE', 'UNKNOWN', 'UNLISTEN', 'UNLOGGED', 'UNTIL', 'UPDATE', 'USER', 'USING', 'VACUUM', 'VALID', 'VALIDATE', 'VALIDATOR', 'VALUE', 'VALUES', 'VARCHAR', 'VARIADIC', 'VARYING', 'VERBOSE', 'VERSION', 'VIEW', 'VIEWS', 'VOLATILE', 'WHEN', 'WHERE', 'WHITESPACE', 'WINDOW', 'WITH', 'WITHIN', 'WITHOUT', 'WORK', 'WRAPPER', 'WRITE', 'XML', 'XMLATTRIBUTES', 'XMLCONCAT', 'XMLELEMENT', 'XMLEXISTS', 'XMLFOREST', 'XMLNAMESPACES', 'XMLPARSE', 'XMLPI', 'XMLROOT', 'XMLSERIALIZE', 'XMLTABLE', 'YEAR', 'YES', 'ZONE'];
var reservedTopLevelWords = ['ADD', 'AFTER', 'ALTER COLUMN', 'ALTER TABLE', 'CASE', 'DELETE FROM', 'END', 'EXCEPT', 'FETCH FIRST', 'FROM', 'GROUP BY', 'HAVING', 'INSERT INTO', 'INSERT', 'LIMIT', 'ORDER BY', 'SELECT', 'SET CURRENT SCHEMA', 'SET SCHEMA', 'SET', 'UPDATE', 'VALUES', 'WHERE'];
var reservedTopLevelWordsNoIndent = ['INTERSECT', 'INTERSECT ALL', 'UNION', 'UNION ALL'];
var reservedNewlineWords = ['AND', 'ELSE', 'OR', 'WHEN', // joins
'JOIN', 'INNER JOIN', 'LEFT JOIN', 'LEFT OUTER JOIN', 'RIGHT JOIN', 'RIGHT OUTER JOIN', 'FULL JOIN', 'FULL OUTER JOIN', 'CROSS JOIN', 'NATURAL JOIN'];

var PostgreSqlFormatter = /*#__PURE__*/function (_Formatter) {
  _inherits(PostgreSqlFormatter, _Formatter);

  var _super = _createSuper(PostgreSqlFormatter);

  function PostgreSqlFormatter() {
    _classCallCheck(this, PostgreSqlFormatter);

    return _super.apply(this, arguments);
  }

  _createClass(PostgreSqlFormatter, [{
    key: "tokenizer",
    value: function tokenizer() {
      return new _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__["default"]({
        reservedWords: reservedWords,
        reservedTopLevelWords: reservedTopLevelWords,
        reservedNewlineWords: reservedNewlineWords,
        reservedTopLevelWordsNoIndent: reservedTopLevelWordsNoIndent,
        stringTypes: ["\"\"", "''", "U&''", 'U&""', '$$'],
        openParens: ['(', 'CASE'],
        closeParens: [')', 'END'],
        indexedPlaceholderTypes: ['$'],
        namedPlaceholderTypes: [':'],
        lineCommentTypes: ['--'],
        operators: ['!=', '<<', '>>', '||/', '|/', '::', '->>', '->', '~~*', '~~', '!~~*', '!~~', '~*', '!~*', '!~', '!!']
      });
    }
  }]);

  return PostgreSqlFormatter;
}(_core_Formatter__WEBPACK_IMPORTED_MODULE_0__["default"]);



/***/ }),

/***/ "./src/languages/RedshiftFormatter.js":
/*!********************************************!*\
  !*** ./src/languages/RedshiftFormatter.js ***!
  \********************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return RedshiftFormatter; });
/* harmony import */ var _core_Formatter__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../core/Formatter */ "./src/core/Formatter.js");
/* harmony import */ var _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../core/Tokenizer */ "./src/core/Tokenizer.js");
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _createSuper(Derived) { var hasNativeReflectConstruct = _isNativeReflectConstruct(); return function _createSuperInternal() { var Super = _getPrototypeOf(Derived), result; if (hasNativeReflectConstruct) { var NewTarget = _getPrototypeOf(this).constructor; result = Reflect.construct(Super, arguments, NewTarget); } else { result = Super.apply(this, arguments); } return _possibleConstructorReturn(this, result); }; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _isNativeReflectConstruct() { if (typeof Reflect === "undefined" || !Reflect.construct) return false; if (Reflect.construct.sham) return false; if (typeof Proxy === "function") return true; try { Date.prototype.toString.call(Reflect.construct(Date, [], function () {})); return true; } catch (e) { return false; } }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }



var reservedWords = ['AES128', 'AES256', 'ALLOWOVERWRITE', 'ANALYSE', 'ARRAY', 'AS', 'ASC', 'AUTHORIZATION', 'BACKUP', 'BINARY', 'BLANKSASNULL', 'BOTH', 'BYTEDICT', 'BZIP2', 'CAST', 'CHECK', 'COLLATE', 'COLUMN', 'CONSTRAINT', 'CREATE', 'CREDENTIALS', 'CURRENT_DATE', 'CURRENT_TIME', 'CURRENT_TIMESTAMP', 'CURRENT_USER', 'CURRENT_USER_ID', 'DEFAULT', 'DEFERRABLE', 'DEFLATE', 'DEFRAG', 'DELTA', 'DELTA32K', 'DESC', 'DISABLE', 'DISTINCT', 'DO', 'ELSE', 'EMPTYASNULL', 'ENABLE', 'ENCODE', 'ENCRYPT', 'ENCRYPTION', 'END', 'EXPLICIT', 'FALSE', 'FOR', 'FOREIGN', 'FREEZE', 'FULL', 'GLOBALDICT256', 'GLOBALDICT64K', 'GRANT', 'GZIP', 'IDENTITY', 'IGNORE', 'ILIKE', 'INITIALLY', 'INTO', 'LEADING', 'LOCALTIME', 'LOCALTIMESTAMP', 'LUN', 'LUNS', 'LZO', 'LZOP', 'MINUS', 'MOSTLY13', 'MOSTLY32', 'MOSTLY8', 'NATURAL', 'NEW', 'NULLS', 'OFF', 'OFFLINE', 'OFFSET', 'OLD', 'ON', 'ONLY', 'OPEN', 'ORDER', 'OVERLAPS', 'PARALLEL', 'PARTITION', 'PERCENT', 'PERMISSIONS', 'PLACING', 'PRIMARY', 'RAW', 'READRATIO', 'RECOVER', 'REFERENCES', 'REJECTLOG', 'RESORT', 'RESTORE', 'SESSION_USER', 'SIMILAR', 'SYSDATE', 'SYSTEM', 'TABLE', 'TAG', 'TDES', 'TEXT255', 'TEXT32K', 'THEN', 'TIMESTAMP', 'TO', 'TOP', 'TRAILING', 'TRUE', 'TRUNCATECOLUMNS', 'UNIQUE', 'USER', 'USING', 'VERBOSE', 'WALLET', 'WHEN', 'WITH', 'WITHOUT', 'PREDICATE', 'COLUMNS', 'COMPROWS', 'COMPRESSION', 'COPY', 'FORMAT', 'DELIMITER', 'FIXEDWIDTH', 'AVRO', 'JSON', 'ENCRYPTED', 'BZIP2', 'GZIP', 'LZOP', 'PARQUET', 'ORC', 'ACCEPTANYDATE', 'ACCEPTINVCHARS', 'BLANKSASNULL', 'DATEFORMAT', 'EMPTYASNULL', 'ENCODING', 'ESCAPE', 'EXPLICIT_IDS', 'FILLRECORD', 'IGNOREBLANKLINES', 'IGNOREHEADER', 'NULL AS', 'REMOVEQUOTES', 'ROUNDEC', 'TIMEFORMAT', 'TRIMBLANKS', 'TRUNCATECOLUMNS', 'COMPROWS', 'COMPUPDATE', 'MAXERROR', 'NOLOAD', 'STATUPDATE', 'MANIFEST', 'REGION', 'IAM_ROLE', 'MASTER_SYMMETRIC_KEY', 'SSH', 'ACCEPTANYDATE', 'ACCEPTINVCHARS', 'ACCESS_KEY_ID', 'SECRET_ACCESS_KEY', 'AVRO', 'BLANKSASNULL', 'BZIP2', 'COMPROWS', 'COMPUPDATE', 'CREDENTIALS', 'DATEFORMAT', 'DELIMITER', 'EMPTYASNULL', 'ENCODING', 'ENCRYPTED', 'ESCAPE', 'EXPLICIT_IDS', 'FILLRECORD', 'FIXEDWIDTH', 'FORMAT', 'IAM_ROLE', 'GZIP', 'IGNOREBLANKLINES', 'IGNOREHEADER', 'JSON', 'LZOP', 'MANIFEST', 'MASTER_SYMMETRIC_KEY', 'MAXERROR', 'NOLOAD', 'NULL AS', 'READRATIO', 'REGION', 'REMOVEQUOTES', 'ROUNDEC', 'SSH', 'STATUPDATE', 'TIMEFORMAT', 'SESSION_TOKEN', 'TRIMBLANKS', 'TRUNCATECOLUMNS', 'EXTERNAL', 'DATA CATALOG', 'HIVE METASTORE', 'CATALOG_ROLE', 'VACUUM', 'COPY', 'UNLOAD', 'EVEN', 'ALL'];
var reservedTopLevelWords = ['ADD', 'AFTER', 'ALTER COLUMN', 'ALTER TABLE', 'DELETE FROM', 'EXCEPT', 'FROM', 'GROUP BY', 'HAVING', 'INSERT INTO', 'INSERT', 'INTERSECT', 'TOP', 'LIMIT', 'MODIFY', 'ORDER BY', 'SELECT', 'SET CURRENT SCHEMA', 'SET SCHEMA', 'SET', 'UNION ALL', 'UNION', 'UPDATE', 'VALUES', 'WHERE', 'VACUUM', 'COPY', 'UNLOAD', 'ANALYZE', 'ANALYSE', 'DISTKEY', 'SORTKEY', 'COMPOUND', 'INTERLEAVED', 'FORMAT', 'DELIMITER', 'FIXEDWIDTH', 'AVRO', 'JSON', 'ENCRYPTED', 'BZIP2', 'GZIP', 'LZOP', 'PARQUET', 'ORC', 'ACCEPTANYDATE', 'ACCEPTINVCHARS', 'BLANKSASNULL', 'DATEFORMAT', 'EMPTYASNULL', 'ENCODING', 'ESCAPE', 'EXPLICIT_IDS', 'FILLRECORD', 'IGNOREBLANKLINES', 'IGNOREHEADER', 'NULL AS', 'REMOVEQUOTES', 'ROUNDEC', 'TIMEFORMAT', 'TRIMBLANKS', 'TRUNCATECOLUMNS', 'COMPROWS', 'COMPUPDATE', 'MAXERROR', 'NOLOAD', 'STATUPDATE', 'MANIFEST', 'REGION', 'IAM_ROLE', 'MASTER_SYMMETRIC_KEY', 'SSH', 'ACCEPTANYDATE', 'ACCEPTINVCHARS', 'ACCESS_KEY_ID', 'SECRET_ACCESS_KEY', 'AVRO', 'BLANKSASNULL', 'BZIP2', 'COMPROWS', 'COMPUPDATE', 'CREDENTIALS', 'DATEFORMAT', 'DELIMITER', 'EMPTYASNULL', 'ENCODING', 'ENCRYPTED', 'ESCAPE', 'EXPLICIT_IDS', 'FILLRECORD', 'FIXEDWIDTH', 'FORMAT', 'IAM_ROLE', 'GZIP', 'IGNOREBLANKLINES', 'IGNOREHEADER', 'JSON', 'LZOP', 'MANIFEST', 'MASTER_SYMMETRIC_KEY', 'MAXERROR', 'NOLOAD', 'NULL AS', 'READRATIO', 'REGION', 'REMOVEQUOTES', 'ROUNDEC', 'SSH', 'STATUPDATE', 'TIMEFORMAT', 'SESSION_TOKEN', 'TRIMBLANKS', 'TRUNCATECOLUMNS', 'EXTERNAL', 'DATA CATALOG', 'HIVE METASTORE', 'CATALOG_ROLE'];
var reservedTopLevelWordsNoIndent = [];
var reservedNewlineWords = ['AND', 'ELSE', 'OR', 'OUTER APPLY', 'WHEN', 'VACUUM', 'COPY', 'UNLOAD', 'ANALYZE', 'ANALYSE', 'DISTKEY', 'SORTKEY', 'COMPOUND', 'INTERLEAVED', // joins
'JOIN', 'INNER JOIN', 'LEFT JOIN', 'LEFT OUTER JOIN', 'RIGHT JOIN', 'RIGHT OUTER JOIN', 'FULL JOIN', 'FULL OUTER JOIN', 'CROSS JOIN', 'NATURAL JOIN'];

var RedshiftFormatter = /*#__PURE__*/function (_Formatter) {
  _inherits(RedshiftFormatter, _Formatter);

  var _super = _createSuper(RedshiftFormatter);

  function RedshiftFormatter() {
    _classCallCheck(this, RedshiftFormatter);

    return _super.apply(this, arguments);
  }

  _createClass(RedshiftFormatter, [{
    key: "tokenizer",
    value: function tokenizer() {
      return new _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__["default"]({
        reservedWords: reservedWords,
        reservedTopLevelWords: reservedTopLevelWords,
        reservedNewlineWords: reservedNewlineWords,
        reservedTopLevelWordsNoIndent: reservedTopLevelWordsNoIndent,
        stringTypes: ["\"\"", "''", '``'],
        openParens: ['('],
        closeParens: [')'],
        indexedPlaceholderTypes: ['?'],
        namedPlaceholderTypes: ['@', '#', '$'],
        lineCommentTypes: ['--'],
        operators: ['|/', '||/', '<<', '>>', '!=', '||']
      });
    }
  }]);

  return RedshiftFormatter;
}(_core_Formatter__WEBPACK_IMPORTED_MODULE_0__["default"]);



/***/ }),

/***/ "./src/languages/SparkSqlFormatter.js":
/*!********************************************!*\
  !*** ./src/languages/SparkSqlFormatter.js ***!
  \********************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return SparkSqlFormatter; });
/* harmony import */ var _core_Formatter__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../core/Formatter */ "./src/core/Formatter.js");
/* harmony import */ var _core_token__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../core/token */ "./src/core/token.js");
/* harmony import */ var _core_Tokenizer__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../core/Tokenizer */ "./src/core/Tokenizer.js");
/* harmony import */ var _core_tokenTypes__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../core/tokenTypes */ "./src/core/tokenTypes.js");
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _createSuper(Derived) { var hasNativeReflectConstruct = _isNativeReflectConstruct(); return function _createSuperInternal() { var Super = _getPrototypeOf(Derived), result; if (hasNativeReflectConstruct) { var NewTarget = _getPrototypeOf(this).constructor; result = Reflect.construct(Super, arguments, NewTarget); } else { result = Super.apply(this, arguments); } return _possibleConstructorReturn(this, result); }; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _isNativeReflectConstruct() { if (typeof Reflect === "undefined" || !Reflect.construct) return false; if (Reflect.construct.sham) return false; if (typeof Proxy === "function") return true; try { Date.prototype.toString.call(Reflect.construct(Date, [], function () {})); return true; } catch (e) { return false; } }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }





var reservedWords = ['ALL', 'ALTER', 'ANALYSE', 'ANALYZE', 'ARRAY_ZIP', 'ARRAY', 'AS', 'ASC', 'AVG', 'BETWEEN', 'CASCADE', 'CASE', 'CAST', 'COALESCE', 'COLLECT_LIST', 'COLLECT_SET', 'COLUMN', 'COLUMNS', 'COMMENT', 'CONSTRAINT', 'CONTAINS', 'CONVERT', 'COUNT', 'CUME_DIST', 'CURRENT ROW', 'CURRENT_DATE', 'CURRENT_TIMESTAMP', 'DATABASE', 'DATABASES', 'DATE_ADD', 'DATE_SUB', 'DATE_TRUNC', 'DAY_HOUR', 'DAY_MINUTE', 'DAY_SECOND', 'DAY', 'DAYS', 'DECODE', 'DEFAULT', 'DELETE', 'DENSE_RANK', 'DESC', 'DESCRIBE', 'DISTINCT', 'DISTINCTROW', 'DIV', 'DROP', 'ELSE', 'ENCODE', 'END', 'EXISTS', 'EXPLAIN', 'EXPLODE_OUTER', 'EXPLODE', 'FILTER', 'FIRST_VALUE', 'FIRST', 'FIXED', 'FLATTEN', 'FOLLOWING', 'FROM_UNIXTIME', 'FULL', 'GREATEST', 'GROUP_CONCAT', 'HOUR_MINUTE', 'HOUR_SECOND', 'HOUR', 'HOURS', 'IF', 'IFNULL', 'IN', 'INSERT', 'INTERVAL', 'INTO', 'IS', 'LAG', 'LAST_VALUE', 'LAST', 'LEAD', 'LEADING', 'LEAST', 'LEVEL', 'LIKE', 'MAX', 'MERGE', 'MIN', 'MINUTE_SECOND', 'MINUTE', 'MONTH', 'NATURAL', 'NOT', 'NOW()', 'NTILE', 'NULL', 'NULLIF', 'OFFSET', 'ON DELETE', 'ON UPDATE', 'ON', 'ONLY', 'OPTIMIZE', 'OVER', 'PERCENT_RANK', 'PRECEDING', 'RANGE', 'RANK', 'REGEXP', 'RENAME', 'RLIKE', 'ROW', 'ROWS', 'SECOND', 'SEPARATOR', 'SEQUENCE', 'SIZE', 'STRING', 'STRUCT', 'SUM', 'TABLE', 'TABLES', 'TEMPORARY', 'THEN', 'TO_DATE', 'TO_JSON', 'TO', 'TRAILING', 'TRANSFORM', 'TRUE', 'TRUNCATE', 'TYPE', 'TYPES', 'UNBOUNDED', 'UNIQUE', 'UNIX_TIMESTAMP', 'UNLOCK', 'UNSIGNED', 'USING', 'VARIABLES', 'VIEW', 'WHEN', 'WITH', 'YEAR_MONTH'];
var reservedTopLevelWords = ['ADD', 'AFTER', 'ALTER COLUMN', 'ALTER DATABASE', 'ALTER SCHEMA', 'ALTER TABLE', 'CLUSTER BY', 'CLUSTERED BY', 'DELETE FROM', 'DISTRIBUTE BY', 'FROM', 'GROUP BY', 'HAVING', 'INSERT INTO', 'INSERT', 'LIMIT', 'OPTIONS', 'ORDER BY', 'PARTITION BY', 'PARTITIONED BY', 'RANGE', 'ROWS', 'SELECT', 'SET CURRENT SCHEMA', 'SET SCHEMA', 'SET', 'TBLPROPERTIES', 'UPDATE', 'USING', 'VALUES', 'WHERE', 'WINDOW'];
var reservedTopLevelWordsNoIndent = ['EXCEPT ALL', 'EXCEPT', 'INTERSECT ALL', 'INTERSECT', 'UNION ALL', 'UNION'];
var reservedNewlineWords = ['AND', 'CREATE OR', 'CREATE', 'ELSE', 'LATERAL VIEW', 'OR', 'OUTER APPLY', 'WHEN', 'XOR', // joins
'JOIN', 'INNER JOIN', 'LEFT JOIN', 'LEFT OUTER JOIN', 'RIGHT JOIN', 'RIGHT OUTER JOIN', 'FULL JOIN', 'FULL OUTER JOIN', 'CROSS JOIN', 'NATURAL JOIN', // non-standard-joins
'ANTI JOIN', 'SEMI JOIN', 'LEFT ANTI JOIN', 'LEFT SEMI JOIN', 'RIGHT OUTER JOIN', 'RIGHT SEMI JOIN', 'NATURAL ANTI JOIN', 'NATURAL FULL OUTER JOIN', 'NATURAL INNER JOIN', 'NATURAL LEFT ANTI JOIN', 'NATURAL LEFT OUTER JOIN', 'NATURAL LEFT SEMI JOIN', 'NATURAL OUTER JOIN', 'NATURAL RIGHT OUTER JOIN', 'NATURAL RIGHT SEMI JOIN', 'NATURAL SEMI JOIN'];

var SparkSqlFormatter = /*#__PURE__*/function (_Formatter) {
  _inherits(SparkSqlFormatter, _Formatter);

  var _super = _createSuper(SparkSqlFormatter);

  function SparkSqlFormatter() {
    _classCallCheck(this, SparkSqlFormatter);

    return _super.apply(this, arguments);
  }

  _createClass(SparkSqlFormatter, [{
    key: "tokenizer",
    value: function tokenizer() {
      return new _core_Tokenizer__WEBPACK_IMPORTED_MODULE_2__["default"]({
        reservedWords: reservedWords,
        reservedTopLevelWords: reservedTopLevelWords,
        reservedNewlineWords: reservedNewlineWords,
        reservedTopLevelWordsNoIndent: reservedTopLevelWordsNoIndent,
        stringTypes: ["\"\"", "''", '``', '{}'],
        openParens: ['(', 'CASE'],
        closeParens: [')', 'END'],
        indexedPlaceholderTypes: ['?'],
        namedPlaceholderTypes: ['$'],
        lineCommentTypes: ['--'],
        operators: ['!=', '<=>', '&&', '||', '==']
      });
    }
  }, {
    key: "tokenOverride",
    value: function tokenOverride(token) {
      // Fix cases where names are ambiguously keywords or functions
      if (Object(_core_token__WEBPACK_IMPORTED_MODULE_1__["isWindow"])(token)) {
        var aheadToken = this.tokenLookAhead();

        if (aheadToken && aheadToken.type === _core_tokenTypes__WEBPACK_IMPORTED_MODULE_3__["default"].OPEN_PAREN) {
          // This is a function call, treat it as a reserved word
          return {
            type: _core_tokenTypes__WEBPACK_IMPORTED_MODULE_3__["default"].RESERVED,
            value: token.value
          };
        }
      } // Fix cases where names are ambiguously keywords or properties


      if (Object(_core_token__WEBPACK_IMPORTED_MODULE_1__["isEnd"])(token)) {
        var backToken = this.tokenLookBehind();

        if (backToken && backToken.type === _core_tokenTypes__WEBPACK_IMPORTED_MODULE_3__["default"].OPERATOR && backToken.value === '.') {
          // This is window().end (or similar) not CASE ... END
          return {
            type: _core_tokenTypes__WEBPACK_IMPORTED_MODULE_3__["default"].WORD,
            value: token.value
          };
        }
      }

      return token;
    }
  }]);

  return SparkSqlFormatter;
}(_core_Formatter__WEBPACK_IMPORTED_MODULE_0__["default"]);



/***/ }),

/***/ "./src/languages/StandardSqlFormatter.js":
/*!***********************************************!*\
  !*** ./src/languages/StandardSqlFormatter.js ***!
  \***********************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return StandardSqlFormatter; });
/* harmony import */ var _core_Formatter__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../core/Formatter */ "./src/core/Formatter.js");
/* harmony import */ var _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../core/Tokenizer */ "./src/core/Tokenizer.js");
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _createSuper(Derived) { var hasNativeReflectConstruct = _isNativeReflectConstruct(); return function _createSuperInternal() { var Super = _getPrototypeOf(Derived), result; if (hasNativeReflectConstruct) { var NewTarget = _getPrototypeOf(this).constructor; result = Reflect.construct(Super, arguments, NewTarget); } else { result = Super.apply(this, arguments); } return _possibleConstructorReturn(this, result); }; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _isNativeReflectConstruct() { if (typeof Reflect === "undefined" || !Reflect.construct) return false; if (Reflect.construct.sham) return false; if (typeof Proxy === "function") return true; try { Date.prototype.toString.call(Reflect.construct(Date, [], function () {})); return true; } catch (e) { return false; } }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }


 // https://jakewheat.github.io/sql-overview/sql-2008-foundation-grammar.html#reserved-word

var reservedWords = ['ABS', 'ALL', 'ALLOCATE', 'ALTER', 'AND', 'ANY', 'ARE', 'ARRAY', 'AS', 'ASENSITIVE', 'ASYMMETRIC', 'AT', 'ATOMIC', 'AUTHORIZATION', 'AVG', 'BEGIN', 'BETWEEN', 'BIGINT', 'BINARY', 'BLOB', 'BOOLEAN', 'BOTH', 'BY', 'CALL', 'CALLED', 'CARDINALITY', 'CASCADED', 'CASE', 'CAST', 'CEIL', 'CEILING', 'CHAR', 'CHAR_LENGTH', 'CHARACTER', 'CHARACTER_LENGTH', 'CHECK', 'CLOB', 'CLOSE', 'COALESCE', 'COLLATE', 'COLLECT', 'COLUMN', 'COMMIT', 'CONDITION', 'CONNECT', 'CONSTRAINT', 'CONVERT', 'CORR', 'CORRESPONDING', 'COUNT', 'COVAR_POP', 'COVAR_SAMP', 'CREATE', 'CROSS', 'CUBE', 'CUME_DIST', 'CURRENT', 'CURRENT_CATALOG', 'CURRENT_DATE', 'CURRENT_DEFAULT_TRANSFORM_GROUP', 'CURRENT_PATH', 'CURRENT_ROLE', 'CURRENT_SCHEMA', 'CURRENT_TIME', 'CURRENT_TIMESTAMP', 'CURRENT_TRANSFORM_GROUP_FOR_TYPE', 'CURRENT_USER', 'CURSOR', 'CYCLE', 'DATE', 'DAY', 'DEALLOCATE', 'DEC', 'DECIMAL', 'DECLARE', 'DEFAULT', 'DELETE', 'DENSE_RANK', 'DEREF', 'DESCRIBE', 'DETERMINISTIC', 'DISCONNECT', 'DISTINCT', 'DOUBLE', 'DROP', 'DYNAMIC', 'EACH', 'ELEMENT', 'ELSE', 'END', 'END-EXEC', 'ESCAPE', 'EVERY', 'EXCEPT', 'EXEC', 'EXECUTE', 'EXISTS', 'EXP', 'EXTERNAL', 'EXTRACT', 'FALSE', 'FETCH', 'FILTER', 'FLOAT', 'FLOOR', 'FOR', 'FOREIGN', 'FREE', 'FROM', 'FULL', 'FUNCTION', 'FUSION', 'GET', 'GLOBAL', 'GRANT', 'GROUP', 'GROUPING', 'HAVING', 'HOLD', 'HOUR', 'IDENTITY', 'IN', 'INDICATOR', 'INNER', 'INOUT', 'INSENSITIVE', 'INSERT', 'INT', 'INTEGER', 'INTERSECT', 'INTERSECTION', 'INTERVAL', 'INTO', 'IS', 'JOIN', 'LANGUAGE', 'LARGE', 'LATERAL', 'LEADING', 'LEFT', 'LIKE', 'LIKE_REGEX', 'LN', 'LOCAL', 'LOCALTIME', 'LOCALTIMESTAMP', 'LOWER', 'MATCH', 'MAX', 'MEMBER', 'MERGE', 'METHOD', 'MIN', 'MINUTE', 'MOD', 'MODIFIES', 'MODULE', 'MONTH', 'MULTISET', 'NATIONAL', 'NATURAL', 'NCHAR', 'NCLOB', 'NEW', 'NO', 'NONE', 'NORMALIZE', 'NOT', 'NULL', 'NULLIF', 'NUMERIC', 'OCTET_LENGTH', 'OCCURRENCES_REGEX', 'OF', 'OLD', 'ON', 'ONLY', 'OPEN', 'OR', 'ORDER', 'OUT', 'OUTER', 'OVER', 'OVERLAPS', 'OVERLAY', 'PARAMETER', 'PARTITION', 'PERCENT_RANK', 'PERCENTILE_CONT', 'PERCENTILE_DISC', 'POSITION', 'POSITION_REGEX', 'POWER', 'PRECISION', 'PREPARE', 'PRIMARY', 'PROCEDURE', 'RANGE', 'RANK', 'READS', 'REAL', 'RECURSIVE', 'REF', 'REFERENCES', 'REFERENCING', 'REGR_AVGX', 'REGR_AVGY', 'REGR_COUNT', 'REGR_INTERCEPT', 'REGR_R2', 'REGR_SLOPE', 'REGR_SXX', 'REGR_SXY', 'REGR_SYY', 'RELEASE', 'RESULT', 'RETURN', 'RETURNS', 'REVOKE', 'RIGHT', 'ROLLBACK', 'ROLLUP', 'ROW', 'ROW_NUMBER', 'ROWS', 'SAVEPOINT', 'SCOPE', 'SCROLL', 'SEARCH', 'SECOND', 'SELECT', 'SENSITIVE', 'SESSION_USER', 'SET', 'SIMILAR', 'SMALLINT', 'SOME', 'SPECIFIC', 'SPECIFICTYPE', 'SQL', 'SQLEXCEPTION', 'SQLSTATE', 'SQLWARNING', 'SQRT', 'START', 'STATIC', 'STDDEV_POP', 'STDDEV_SAMP', 'SUBMULTISET', 'SUBSTRING', 'SUBSTRING_REGEX', 'SUM', 'SYMMETRIC', 'SYSTEM', 'SYSTEM_USER', 'TABLE', 'TABLESAMPLE', 'THEN', 'TIME', 'TIMESTAMP', 'TIMEZONE_HOUR', 'TIMEZONE_MINUTE', 'TO', 'TRAILING', 'TRANSLATE', 'TRANSLATE_REGEX', 'TRANSLATION', 'TREAT', 'TRIGGER', 'TRIM', 'TRUE', 'UESCAPE', 'UNION', 'UNIQUE', 'UNKNOWN', 'UNNEST', 'UPDATE', 'UPPER', 'USER', 'USING', 'VALUE', 'VALUES', 'VAR_POP', 'VAR_SAMP', 'VARBINARY', 'VARCHAR', 'VARYING', 'WHEN', 'WHENEVER', 'WHERE', 'WIDTH_BUCKET', 'WINDOW', 'WITH', 'WITHIN', 'WITHOUT', 'YEAR'];
var reservedTopLevelWords = ['ADD', 'ALTER COLUMN', 'ALTER TABLE', 'CASE', 'DELETE FROM', 'END', 'FETCH FIRST', 'FETCH NEXT', 'FETCH PRIOR', 'FETCH LAST', 'FETCH ABSOLUTE', 'FETCH RELATIVE', 'FROM', 'GROUP BY', 'HAVING', 'INSERT INTO', 'LIMIT', 'ORDER BY', 'SELECT', 'SET SCHEMA', 'SET', 'UPDATE', 'VALUES', 'WHERE'];
var reservedTopLevelWordsNoIndent = ['INTERSECT', 'INTERSECT ALL', 'INTERSECT DISTINCT', 'UNION', 'UNION ALL', 'UNION DISTINCT', 'EXCEPT', 'EXCEPT ALL', 'EXCEPT DISTINCT'];
var reservedNewlineWords = ['AND', 'ELSE', 'OR', 'WHEN', // joins
'JOIN', 'INNER JOIN', 'LEFT JOIN', 'LEFT OUTER JOIN', 'RIGHT JOIN', 'RIGHT OUTER JOIN', 'FULL JOIN', 'FULL OUTER JOIN', 'CROSS JOIN', 'NATURAL JOIN'];

var StandardSqlFormatter = /*#__PURE__*/function (_Formatter) {
  _inherits(StandardSqlFormatter, _Formatter);

  var _super = _createSuper(StandardSqlFormatter);

  function StandardSqlFormatter() {
    _classCallCheck(this, StandardSqlFormatter);

    return _super.apply(this, arguments);
  }

  _createClass(StandardSqlFormatter, [{
    key: "tokenizer",
    value: function tokenizer() {
      return new _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__["default"]({
        reservedWords: reservedWords,
        reservedTopLevelWords: reservedTopLevelWords,
        reservedNewlineWords: reservedNewlineWords,
        reservedTopLevelWordsNoIndent: reservedTopLevelWordsNoIndent,
        stringTypes: ["\"\"", "''"],
        openParens: ['(', 'CASE'],
        closeParens: [')', 'END'],
        indexedPlaceholderTypes: ['?'],
        namedPlaceholderTypes: [],
        lineCommentTypes: ['--']
      });
    }
  }]);

  return StandardSqlFormatter;
}(_core_Formatter__WEBPACK_IMPORTED_MODULE_0__["default"]);



/***/ }),

/***/ "./src/languages/TSqlFormatter.js":
/*!****************************************!*\
  !*** ./src/languages/TSqlFormatter.js ***!
  \****************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return TSqlFormatter; });
/* harmony import */ var _core_Formatter__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../core/Formatter */ "./src/core/Formatter.js");
/* harmony import */ var _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../core/Tokenizer */ "./src/core/Tokenizer.js");
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _createSuper(Derived) { var hasNativeReflectConstruct = _isNativeReflectConstruct(); return function _createSuperInternal() { var Super = _getPrototypeOf(Derived), result; if (hasNativeReflectConstruct) { var NewTarget = _getPrototypeOf(this).constructor; result = Reflect.construct(Super, arguments, NewTarget); } else { result = Super.apply(this, arguments); } return _possibleConstructorReturn(this, result); }; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _isNativeReflectConstruct() { if (typeof Reflect === "undefined" || !Reflect.construct) return false; if (Reflect.construct.sham) return false; if (typeof Proxy === "function") return true; try { Date.prototype.toString.call(Reflect.construct(Date, [], function () {})); return true; } catch (e) { return false; } }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }



var reservedWords = ['ADD', 'EXTERNAL', 'PROCEDURE', 'ALL', 'FETCH', 'PUBLIC', 'ALTER', 'FILE', 'RAISERROR', 'AND', 'FILLFACTOR', 'READ', 'ANY', 'FOR', 'READTEXT', 'AS', 'FOREIGN', 'RECONFIGURE', 'ASC', 'FREETEXT', 'REFERENCES', 'AUTHORIZATION', 'FREETEXTTABLE', 'REPLICATION', 'BACKUP', 'FROM', 'RESTORE', 'BEGIN', 'FULL', 'RESTRICT', 'BETWEEN', 'FUNCTION', 'RETURN', 'BREAK', 'GOTO', 'REVERT', 'BROWSE', 'GRANT', 'REVOKE', 'BULK', 'GROUP', 'RIGHT', 'BY', 'HAVING', 'ROLLBACK', 'CASCADE', 'HOLDLOCK', 'ROWCOUNT', 'CASE', 'IDENTITY', 'ROWGUIDCOL', 'CHECK', 'IDENTITY_INSERT', 'RULE', 'CHECKPOINT', 'IDENTITYCOL', 'SAVE', 'CLOSE', 'IF', 'SCHEMA', 'CLUSTERED', 'IN', 'SECURITYAUDIT', 'COALESCE', 'INDEX', 'SELECT', 'COLLATE', 'INNER', 'SEMANTICKEYPHRASETABLE', 'COLUMN', 'INSERT', 'SEMANTICSIMILARITYDETAILSTABLE', 'COMMIT', 'INTERSECT', 'SEMANTICSIMILARITYTABLE', 'COMPUTE', 'INTO', 'SESSION_USER', 'CONSTRAINT', 'IS', 'SET', 'CONTAINS', 'JOIN', 'SETUSER', 'CONTAINSTABLE', 'KEY', 'SHUTDOWN', 'CONTINUE', 'KILL', 'SOME', 'CONVERT', 'LEFT', 'STATISTICS', 'CREATE', 'LIKE', 'SYSTEM_USER', 'CROSS', 'LINENO', 'TABLE', 'CURRENT', 'LOAD', 'TABLESAMPLE', 'CURRENT_DATE', 'MERGE', 'TEXTSIZE', 'CURRENT_TIME', 'NATIONAL', 'THEN', 'CURRENT_TIMESTAMP', 'NOCHECK', 'TO', 'CURRENT_USER', 'NONCLUSTERED', 'TOP', 'CURSOR', 'NOT', 'TRAN', 'DATABASE', 'NULL', 'TRANSACTION', 'DBCC', 'NULLIF', 'TRIGGER', 'DEALLOCATE', 'OF', 'TRUNCATE', 'DECLARE', 'OFF', 'TRY_CONVERT', 'DEFAULT', 'OFFSETS', 'TSEQUAL', 'DELETE', 'ON', 'UNION', 'DENY', 'OPEN', 'UNIQUE', 'DESC', 'OPENDATASOURCE', 'UNPIVOT', 'DISK', 'OPENQUERY', 'UPDATE', 'DISTINCT', 'OPENROWSET', 'UPDATETEXT', 'DISTRIBUTED', 'OPENXML', 'USE', 'DOUBLE', 'OPTION', 'USER', 'DROP', 'OR', 'VALUES', 'DUMP', 'ORDER', 'VARYING', 'ELSE', 'OUTER', 'VIEW', 'END', 'OVER', 'WAITFOR', 'ERRLVL', 'PERCENT', 'WHEN', 'ESCAPE', 'PIVOT', 'WHERE', 'EXCEPT', 'PLAN', 'WHILE', 'EXEC', 'PRECISION', 'WITH', 'EXECUTE', 'PRIMARY', 'WITHIN GROUP', 'EXISTS', 'PRINT', 'WRITETEXT', 'EXIT', 'PROC'];
var reservedTopLevelWords = ['ADD', 'ALTER COLUMN', 'ALTER TABLE', 'CASE', 'DELETE FROM', 'END', 'EXCEPT', 'FROM', 'GROUP BY', 'HAVING', 'INSERT INTO', 'INSERT', 'LIMIT', 'ORDER BY', 'SELECT', 'SET CURRENT SCHEMA', 'SET SCHEMA', 'SET', 'UPDATE', 'VALUES', 'WHERE'];
var reservedTopLevelWordsNoIndent = ['INTERSECT', 'INTERSECT ALL', 'MINUS', 'UNION', 'UNION ALL'];
var reservedNewlineWords = ['AND', 'ELSE', 'OR', 'WHEN', // joins
'JOIN', 'INNER JOIN', 'LEFT JOIN', 'LEFT OUTER JOIN', 'RIGHT JOIN', 'RIGHT OUTER JOIN', 'FULL JOIN', 'FULL OUTER JOIN', 'CROSS JOIN'];

var TSqlFormatter = /*#__PURE__*/function (_Formatter) {
  _inherits(TSqlFormatter, _Formatter);

  var _super = _createSuper(TSqlFormatter);

  function TSqlFormatter() {
    _classCallCheck(this, TSqlFormatter);

    return _super.apply(this, arguments);
  }

  _createClass(TSqlFormatter, [{
    key: "tokenizer",
    value: function tokenizer() {
      return new _core_Tokenizer__WEBPACK_IMPORTED_MODULE_1__["default"]({
        reservedWords: reservedWords,
        reservedTopLevelWords: reservedTopLevelWords,
        reservedNewlineWords: reservedNewlineWords,
        reservedTopLevelWordsNoIndent: reservedTopLevelWordsNoIndent,
        stringTypes: ["\"\"", "N''", "''", '[]'],
        openParens: ['(', 'CASE'],
        closeParens: [')', 'END'],
        indexedPlaceholderTypes: [],
        namedPlaceholderTypes: ['@'],
        lineCommentTypes: ['--'],
        specialWordChars: ['#', '@'],
        operators: ['>=', '<=', '<>', '!=', '!<', '!>', '+=', '-=', '*=', '/=', '%=', '|=', '&=', '^=', '::'] // TODO: Support for money constants

      });
    }
  }]);

  return TSqlFormatter;
}(_core_Formatter__WEBPACK_IMPORTED_MODULE_0__["default"]);



/***/ }),

/***/ "./src/sqlFormatter.js":
/*!*****************************!*\
  !*** ./src/sqlFormatter.js ***!
  \*****************************/
/*! exports provided: format, supportedDialects, isSupportDialects, getTokens */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "format", function() { return format; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "supportedDialects", function() { return supportedDialects; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isSupportDialects", function() { return isSupportDialects; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "getTokens", function() { return getTokens; });
/* harmony import */ var _languages_Db2Formatter__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./languages/Db2Formatter */ "./src/languages/Db2Formatter.js");
/* harmony import */ var _languages_MariaDbFormatter__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./languages/MariaDbFormatter */ "./src/languages/MariaDbFormatter.js");
/* harmony import */ var _languages_MySqlFormatter__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./languages/MySqlFormatter */ "./src/languages/MySqlFormatter.js");
/* harmony import */ var _languages_N1qlFormatter__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./languages/N1qlFormatter */ "./src/languages/N1qlFormatter.js");
/* harmony import */ var _languages_PlSqlFormatter__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./languages/PlSqlFormatter */ "./src/languages/PlSqlFormatter.js");
/* harmony import */ var _languages_PostgreSqlFormatter__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./languages/PostgreSqlFormatter */ "./src/languages/PostgreSqlFormatter.js");
/* harmony import */ var _languages_RedshiftFormatter__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ./languages/RedshiftFormatter */ "./src/languages/RedshiftFormatter.js");
/* harmony import */ var _languages_SparkSqlFormatter__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ./languages/SparkSqlFormatter */ "./src/languages/SparkSqlFormatter.js");
/* harmony import */ var _languages_StandardSqlFormatter__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! ./languages/StandardSqlFormatter */ "./src/languages/StandardSqlFormatter.js");
/* harmony import */ var _languages_TSqlFormatter__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! ./languages/TSqlFormatter */ "./src/languages/TSqlFormatter.js");
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }











var formatters = {
  db2: _languages_Db2Formatter__WEBPACK_IMPORTED_MODULE_0__["default"],
  mariadb: _languages_MariaDbFormatter__WEBPACK_IMPORTED_MODULE_1__["default"],
  mysql: _languages_MySqlFormatter__WEBPACK_IMPORTED_MODULE_2__["default"],
  n1ql: _languages_N1qlFormatter__WEBPACK_IMPORTED_MODULE_3__["default"],
  plsql: _languages_PlSqlFormatter__WEBPACK_IMPORTED_MODULE_4__["default"],
  postgresql: _languages_PostgreSqlFormatter__WEBPACK_IMPORTED_MODULE_5__["default"],
  redshift: _languages_RedshiftFormatter__WEBPACK_IMPORTED_MODULE_6__["default"],
  spark: _languages_SparkSqlFormatter__WEBPACK_IMPORTED_MODULE_7__["default"],
  sql: _languages_StandardSqlFormatter__WEBPACK_IMPORTED_MODULE_8__["default"],
  tsql: _languages_TSqlFormatter__WEBPACK_IMPORTED_MODULE_9__["default"]
};
/**
 * Format whitespace in a query to make it easier to read.
 *
 * @param {String} query
 * @param {Object} cfg
 *  @param {String} cfg.language Query language, default is Standard SQL
 *  @param {String} cfg.indent Characters used for indentation, default is "  " (2 spaces)
 *  @param {Boolean} cfg.uppercase Converts keywords to uppercase
 *  @param {Integer} cfg.linesBetweenQueries How many line breaks between queries
 *  @param {Object} cfg.params Collection of params for placeholder replacement
 * @return {String}
 */

var format = function format(query) {
  var cfg = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

  if (typeof query !== 'string') {
    throw new Error('Invalid query argument. Extected string, instead got ' + _typeof(query));
  }

  var Formatter = _languages_StandardSqlFormatter__WEBPACK_IMPORTED_MODULE_8__["default"];

  if (cfg.language !== undefined) {
    Formatter = formatters[cfg.language];
  }

  if (Formatter === undefined) {
    throw Error("Unsupported SQL dialect: ".concat(cfg.language));
  }

  return new Formatter(cfg).format(query);
};
var supportedDialects = Object.keys(formatters);
/**
 * Check Support language
 *
 *  @param {String} language Query language
 * @return {Boolean}
 */

var isSupportDialects = function isSupportDialects(language) {
  if (language === undefined) {
    throw Error("Unsupported SQL dialect: ".concat(language));
  }

  language == language.toLowerCase();

  for (var key in formatters) {
    if (key == language) {
      return true;
    }
  }

  return false;
};
var getTokens = function getTokens(query) {
  var cfg = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

  if (typeof query !== 'string') {
    throw new Error('Invalid query argument. Extected string, instead got ' + _typeof(query));
  }

  var Formatter = _languages_StandardSqlFormatter__WEBPACK_IMPORTED_MODULE_8__["default"];

  if (cfg.language !== undefined) {
    Formatter = formatters[cfg.language];
  }

  if (Formatter === undefined) {
    throw Error("Unsupported SQL dialect: ".concat(cfg.language));
  }

  return new Formatter(cfg).tokenizer().tokenize(query);
};

/***/ }),

/***/ "./src/utils.js":
/*!**********************!*\
  !*** ./src/utils.js ***!
  \**********************/
/*! exports provided: trimSpacesEnd, last, isEmpty, escapeRegExp, sortByLengthDesc, isArray, isFunction, isObject, isDate, objectMerge */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "trimSpacesEnd", function() { return trimSpacesEnd; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "last", function() { return last; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isEmpty", function() { return isEmpty; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "escapeRegExp", function() { return escapeRegExp; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "sortByLengthDesc", function() { return sortByLengthDesc; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isArray", function() { return isArray; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isFunction", function() { return isFunction; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isObject", function() { return isObject; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isDate", function() { return isDate; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "objectMerge", function() { return objectMerge; });
function _typeof(obj) { "@babel/helpers - typeof"; if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

// Only removes spaces, not newlines
var trimSpacesEnd = function trimSpacesEnd(str) {
  return str.replace(/[\t ]+$/, '');
}; // Last element from array

var last = function last(arr) {
  return arr[arr.length - 1];
}; // True array is empty, or it's not an array at all

var isEmpty = function isEmpty(arr) {
  return !Array.isArray(arr) || arr.length === 0;
}; // Escapes regex special chars

var escapeRegExp = function escapeRegExp(string) {
  return string.replace(/[\$\(-\+\.\?\[-\^\{-\}]/g, '\\$&');
}; // Sorts strings by length, so that longer ones are first
// Also sorts alphabetically after sorting by length.

var sortByLengthDesc = function sortByLengthDesc(strings) {
  return strings.sort(function (a, b) {
    return b.length - a.length || a.localeCompare(b);
  });
};
var isArray = function isArray(obj) {
  if (Array.isArray) {
    return Array.isArray(obj);
  } else {
    return Object.prototype.toString.call(obj) === '[object Array]';
  }
};
var isFunction = function isFunction(obj) {
  return typeof obj === 'function';
};
var isObject = function isObject(obj) {
  if (isArray(obj)) {
    return false;
  } else if (isFunction(obj)) {
    return false;
  }

  return _typeof(obj) === 'object';
};
var isDate = function isDate(obj) {
  if (obj instanceof Date) return true;

  if (isObject(obj)) {
    return typeof obj.toDateString === 'function' && typeof obj.getDate === 'function' && typeof obj.setDate === 'function';
  }

  return false;
};
var objectMerge = function objectMerge() {
  var reval = arguments.length <= 0 ? undefined : arguments[0];

  if (_typeof(reval) !== 'object' || reval === null) {
    return reval;
  }

  var i = 1;

  if (Object.keys(reval).length > 0) {
    i = 0;
    reval = isArray(reval) ? [] : {};
  }

  var argLen = arguments.length;

  for (; i < argLen; i++) {
    cloneDeep(reval, i < 0 || arguments.length <= i ? undefined : arguments[i]);
  }

  return reval;
};

function cloneDeep(dst, src) {
  if (isObject(src)) {
    return cloneObjectDeep(dst, src);
  } else if (isArray(src)) {
    return cloneArrayDeep(dst, src);
  } else {
    if (isDate(src)) {
      return new src.constructor(src);
    } else {
      return src;
    }
  }
}

function cloneObjectDeep(dst, src) {
  if (typeof src === 'function') {
    return src;
  }

  for (var key in src) {
    if (!src.hasOwnProperty(key)) {
      continue;
    }

    var val = src[key];

    if (val === undefined) {
      continue;
    }

    if (_typeof(val) !== 'object' || val === null) {
      dst[key] = val;
    } else if (_typeof(dst[key]) !== 'object' || dst[key] === null) {
      dst[key] = cloneDeep(isArray(val) ? [] : {}, val);
    } else {
      cloneDeep(dst[key], val);
    }
  }

  return dst;
}

function cloneArrayDeep(dst, src) {
  var isObj = isObject(dst);

  for (var i = 0; i < src.length; i++) {
    var val = src[i];
    var newVal;

    if (val == null) {
      newVal = val;
    } else {
      newVal = cloneDeep(isArray(val) ? [] : {}, val);
    }

    if (isObj) {
      dst[i] = newVal;
    } else {
      var addFlag = true;

      for (var j = 0, l = dst.length; j < l; j++) {
        if (dst[j] == newVal) {
          addFlag = false;
          break;
        }
      }

      if (addFlag) {
        dst.push(newVal);
      }
    }
  }

  return dst;
}

/***/ })

/******/ });
});
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly9zcWxGb3JtYXR0ZXIvd2VicGFjay91bml2ZXJzYWxNb2R1bGVEZWZpbml0aW9uIiwid2VicGFjazovL3NxbEZvcm1hdHRlci93ZWJwYWNrL2Jvb3RzdHJhcCIsIndlYnBhY2s6Ly9zcWxGb3JtYXR0ZXIvLi9zcmMvY29yZS9Gb3JtYXR0ZXIuanMiLCJ3ZWJwYWNrOi8vc3FsRm9ybWF0dGVyLy4vc3JjL2NvcmUvSW5kZW50YXRpb24uanMiLCJ3ZWJwYWNrOi8vc3FsRm9ybWF0dGVyLy4vc3JjL2NvcmUvSW5saW5lQmxvY2suanMiLCJ3ZWJwYWNrOi8vc3FsRm9ybWF0dGVyLy4vc3JjL2NvcmUvUGFyYW1zLmpzIiwid2VicGFjazovL3NxbEZvcm1hdHRlci8uL3NyYy9jb3JlL1Rva2VuaXplci5qcyIsIndlYnBhY2s6Ly9zcWxGb3JtYXR0ZXIvLi9zcmMvY29yZS9yZWdleEZhY3RvcnkuanMiLCJ3ZWJwYWNrOi8vc3FsRm9ybWF0dGVyLy4vc3JjL2NvcmUvdG9rZW4uanMiLCJ3ZWJwYWNrOi8vc3FsRm9ybWF0dGVyLy4vc3JjL2NvcmUvdG9rZW5UeXBlcy5qcyIsIndlYnBhY2s6Ly9zcWxGb3JtYXR0ZXIvLi9zcmMvbGFuZ3VhZ2VzL0RiMkZvcm1hdHRlci5qcyIsIndlYnBhY2s6Ly9zcWxGb3JtYXR0ZXIvLi9zcmMvbGFuZ3VhZ2VzL01hcmlhRGJGb3JtYXR0ZXIuanMiLCJ3ZWJwYWNrOi8vc3FsRm9ybWF0dGVyLy4vc3JjL2xhbmd1YWdlcy9NeVNxbEZvcm1hdHRlci5qcyIsIndlYnBhY2s6Ly9zcWxGb3JtYXR0ZXIvLi9zcmMvbGFuZ3VhZ2VzL04xcWxGb3JtYXR0ZXIuanMiLCJ3ZWJwYWNrOi8vc3FsRm9ybWF0dGVyLy4vc3JjL2xhbmd1YWdlcy9QbFNxbEZvcm1hdHRlci5qcyIsIndlYnBhY2s6Ly9zcWxGb3JtYXR0ZXIvLi9zcmMvbGFuZ3VhZ2VzL1Bvc3RncmVTcWxGb3JtYXR0ZXIuanMiLCJ3ZWJwYWNrOi8vc3FsRm9ybWF0dGVyLy4vc3JjL2xhbmd1YWdlcy9SZWRzaGlmdEZvcm1hdHRlci5qcyIsIndlYnBhY2s6Ly9zcWxGb3JtYXR0ZXIvLi9zcmMvbGFuZ3VhZ2VzL1NwYXJrU3FsRm9ybWF0dGVyLmpzIiwid2VicGFjazovL3NxbEZvcm1hdHRlci8uL3NyYy9sYW5ndWFnZXMvU3RhbmRhcmRTcWxGb3JtYXR0ZXIuanMiLCJ3ZWJwYWNrOi8vc3FsRm9ybWF0dGVyLy4vc3JjL2xhbmd1YWdlcy9UU3FsRm9ybWF0dGVyLmpzIiwid2VicGFjazovL3NxbEZvcm1hdHRlci8uL3NyYy9zcWxGb3JtYXR0ZXIuanMiLCJ3ZWJwYWNrOi8vc3FsRm9ybWF0dGVyLy4vc3JjL3V0aWxzLmpzIl0sIm5hbWVzIjpbIkZvcm1hdHRlciIsImNmZyIsImluZGVudGF0aW9uIiwiSW5kZW50YXRpb24iLCJpbmRlbnQiLCJpbmxpbmVCbG9jayIsIklubGluZUJsb2NrIiwicGFyYW1zIiwiUGFyYW1zIiwicHJldmlvdXNSZXNlcnZlZFRva2VuIiwidG9rZW5zIiwiaW5kZXgiLCJFcnJvciIsInRva2VuIiwicXVlcnkiLCJ0b2tlbml6ZXIiLCJ0b2tlbml6ZSIsImZvcm1hdHRlZFF1ZXJ5IiwiZ2V0Rm9ybWF0dGVkUXVlcnlGcm9tVG9rZW5zIiwidHJpbSIsImZvckVhY2giLCJ0b2tlbk92ZXJyaWRlIiwidHlwZSIsInRva2VuVHlwZXMiLCJMSU5FX0NPTU1FTlQiLCJmb3JtYXRMaW5lQ29tbWVudCIsIkJMT0NLX0NPTU1FTlQiLCJmb3JtYXRCbG9ja0NvbW1lbnQiLCJSRVNFUlZFRF9UT1BfTEVWRUwiLCJmb3JtYXRUb3BMZXZlbFJlc2VydmVkV29yZCIsIlJFU0VSVkVEX1RPUF9MRVZFTF9OT19JTkRFTlQiLCJmb3JtYXRUb3BMZXZlbFJlc2VydmVkV29yZE5vSW5kZW50IiwiUkVTRVJWRURfTkVXTElORSIsImZvcm1hdE5ld2xpbmVSZXNlcnZlZFdvcmQiLCJSRVNFUlZFRCIsImZvcm1hdFdpdGhTcGFjZXMiLCJPUEVOX1BBUkVOIiwiZm9ybWF0T3BlbmluZ1BhcmVudGhlc2VzIiwiQ0xPU0VfUEFSRU4iLCJmb3JtYXRDbG9zaW5nUGFyZW50aGVzZXMiLCJQTEFDRUhPTERFUiIsImZvcm1hdFBsYWNlaG9sZGVyIiwidmFsdWUiLCJmb3JtYXRDb21tYSIsImZvcm1hdFdpdGhTcGFjZUFmdGVyIiwiZm9ybWF0V2l0aG91dFNwYWNlcyIsImZvcm1hdFF1ZXJ5U2VwYXJhdG9yIiwid2hpdGVzcGFjZUJlZm9yZSIsIm1hdGNoIiwicmVwbGFjZSIsImdldEluZGVudCIsInNob3ciLCJhZGROZXdsaW5lIiwiaW5kZW50Q29tbWVudCIsImNvbW1lbnQiLCJkZWNyZWFzZVRvcExldmVsIiwiZXF1YWxpemVXaGl0ZXNwYWNlIiwiaW5jcmVhc2VUb3BMZXZlbCIsImlzQW5kIiwiaXNCZXR3ZWVuIiwidG9rZW5Mb29rQmVoaW5kIiwic3RyaW5nIiwicHJlc2VydmVXaGl0ZXNwYWNlRm9yIiwiT1BFUkFUT1IiLCJsZW5ndGgiLCJ0cmltU3BhY2VzRW5kIiwiYmVnaW5JZlBvc3NpYmxlIiwiaXNBY3RpdmUiLCJpbmNyZWFzZUJsb2NrTGV2ZWwiLCJlbmQiLCJkZWNyZWFzZUJsb2NrTGV2ZWwiLCJnZXQiLCJpc0xpbWl0IiwicmVzZXRJbmRlbnRhdGlvbiIsInJlcGVhdCIsImxpbmVzQmV0d2VlblF1ZXJpZXMiLCJ1cHBlcmNhc2UiLCJ0b1VwcGVyQ2FzZSIsImVuZHNXaXRoIiwibiIsIklOREVOVF9UWVBFX1RPUF9MRVZFTCIsIklOREVOVF9UWVBFX0JMT0NLX0xFVkVMIiwiaW5kZW50VHlwZXMiLCJwdXNoIiwibGFzdCIsInBvcCIsIklOTElORV9NQVhfTEVOR1RIIiwibGV2ZWwiLCJpc0lubGluZUJsb2NrIiwiaSIsImlzRm9yYmlkZGVuVG9rZW4iLCJDT01NRU5UIiwia2V5IiwiVG9rZW5pemVyIiwib2JqZWN0TWVyZ2UiLCJzdHJpbmdUeXBlcyIsIm9wZW5QYXJlbnMiLCJjbG9zZVBhcmVucyIsImluZGV4ZWRQbGFjZWhvbGRlclR5cGVzIiwibmFtZWRQbGFjZWhvbGRlclR5cGVzIiwibGluZUNvbW1lbnRUeXBlcyIsIldISVRFU1BBQ0VfUkVHRVgiLCJOVU1CRVJfUkVHRVgiLCJPUEVSQVRPUl9SRUdFWCIsInJlZ2V4RmFjdG9yeSIsIm9wZXJhdG9ycyIsIkJMT0NLX0NPTU1FTlRfUkVHRVgiLCJMSU5FX0NPTU1FTlRfUkVHRVgiLCJSRVNFUlZFRF9UT1BfTEVWRUxfUkVHRVgiLCJyZXNlcnZlZFRvcExldmVsV29yZHMiLCJSRVNFUlZFRF9UT1BfTEVWRUxfTk9fSU5ERU5UX1JFR0VYIiwicmVzZXJ2ZWRUb3BMZXZlbFdvcmRzTm9JbmRlbnQiLCJSRVNFUlZFRF9ORVdMSU5FX1JFR0VYIiwicmVzZXJ2ZWROZXdsaW5lV29yZHMiLCJSRVNFUlZFRF9QTEFJTl9SRUdFWCIsInJlc2VydmVkV29yZHMiLCJXT1JEX1JFR0VYIiwic3BlY2lhbFdvcmRDaGFycyIsIlNUUklOR19SRUdFWCIsIk9QRU5fUEFSRU5fUkVHRVgiLCJDTE9TRV9QQVJFTl9SRUdFWCIsIklOREVYRURfUExBQ0VIT0xERVJfUkVHRVgiLCJJREVOVF9OQU1FRF9QTEFDRUhPTERFUl9SRUdFWCIsIlNUUklOR19OQU1FRF9QTEFDRUhPTERFUl9SRUdFWCIsImlucHV0IiwiZ2V0V2hpdGVzcGFjZSIsInN1YnN0cmluZyIsImdldE5leHRUb2tlbiIsIm1hdGNoZXMiLCJwcmV2aW91c1Rva2VuIiwiZ2V0Q29tbWVudFRva2VuIiwiZ2V0U3RyaW5nVG9rZW4iLCJnZXRPcGVuUGFyZW5Ub2tlbiIsImdldENsb3NlUGFyZW5Ub2tlbiIsImdldFBsYWNlaG9sZGVyVG9rZW4iLCJnZXROdW1iZXJUb2tlbiIsImdldFJlc2VydmVkV29yZFRva2VuIiwiZ2V0V29yZFRva2VuIiwiZ2V0T3BlcmF0b3JUb2tlbiIsImdldExpbmVDb21tZW50VG9rZW4iLCJnZXRCbG9ja0NvbW1lbnRUb2tlbiIsImdldFRva2VuT25GaXJzdE1hdGNoIiwicmVnZXgiLCJTVFJJTkciLCJnZXRJZGVudE5hbWVkUGxhY2Vob2xkZXJUb2tlbiIsImdldFN0cmluZ05hbWVkUGxhY2Vob2xkZXJUb2tlbiIsImdldEluZGV4ZWRQbGFjZWhvbGRlclRva2VuIiwiZ2V0UGxhY2Vob2xkZXJUb2tlbldpdGhLZXkiLCJwYXJzZUtleSIsInYiLCJzbGljZSIsImdldEVzY2FwZWRQbGFjZWhvbGRlcktleSIsInF1b3RlQ2hhciIsIlJlZ0V4cCIsImVzY2FwZVJlZ0V4cCIsIk5VTUJFUiIsInVuZGVmaW5lZCIsImdldFRvcExldmVsUmVzZXJ2ZWRUb2tlbiIsImdldE5ld2xpbmVSZXNlcnZlZFRva2VuIiwiZ2V0VG9wTGV2ZWxSZXNlcnZlZFRva2VuTm9JbmRlbnQiLCJnZXRQbGFpblJlc2VydmVkVG9rZW4iLCJXT1JEIiwiY3JlYXRlT3BlcmF0b3JSZWdleCIsIm11bHRpTGV0dGVyT3BlcmF0b3JzIiwic29ydEJ5TGVuZ3RoRGVzYyIsIm1hcCIsImpvaW4iLCJjcmVhdGVMaW5lQ29tbWVudFJlZ2V4IiwiYyIsImNyZWF0ZVJlc2VydmVkV29yZFJlZ2V4IiwicmVzZXJ2ZWRXb3Jkc1BhdHRlcm4iLCJjcmVhdGVXb3JkUmVnZXgiLCJzcGVjaWFsQ2hhcnMiLCJjcmVhdGVTdHJpbmdSZWdleCIsImNyZWF0ZVN0cmluZ1BhdHRlcm4iLCJwYXR0ZXJucyIsIiQkIiwidCIsImNyZWF0ZVBhcmVuUmVnZXgiLCJwYXJlbnMiLCJlc2NhcGVQYXJlbiIsInBhcmVuIiwiY3JlYXRlUGxhY2Vob2xkZXJSZWdleCIsInR5cGVzIiwicGF0dGVybiIsImlzRW1wdHkiLCJ0eXBlc1JlZ2V4IiwiaXNUb2tlbiIsInRlc3QiLCJpc1NldCIsImlzQnkiLCJpc1dpbmRvdyIsImlzRW5kIiwiRGIyRm9ybWF0dGVyIiwiTWFyaWFEYkZvcm1hdHRlciIsIk15U3FsRm9ybWF0dGVyIiwiTjFxbEZvcm1hdHRlciIsIlBsU3FsRm9ybWF0dGVyIiwiUG9zdGdyZVNxbEZvcm1hdHRlciIsIlJlZHNoaWZ0Rm9ybWF0dGVyIiwiU3BhcmtTcWxGb3JtYXR0ZXIiLCJhaGVhZFRva2VuIiwidG9rZW5Mb29rQWhlYWQiLCJiYWNrVG9rZW4iLCJTdGFuZGFyZFNxbEZvcm1hdHRlciIsIlRTcWxGb3JtYXR0ZXIiLCJmb3JtYXR0ZXJzIiwiZGIyIiwibWFyaWFkYiIsIm15c3FsIiwibjFxbCIsInBsc3FsIiwicG9zdGdyZXNxbCIsInJlZHNoaWZ0Iiwic3BhcmsiLCJzcWwiLCJ0c3FsIiwiZm9ybWF0IiwibGFuZ3VhZ2UiLCJzdXBwb3J0ZWREaWFsZWN0cyIsIk9iamVjdCIsImtleXMiLCJpc1N1cHBvcnREaWFsZWN0cyIsInRvTG93ZXJDYXNlIiwiZ2V0VG9rZW5zIiwic3RyIiwiYXJyIiwiQXJyYXkiLCJpc0FycmF5Iiwic3RyaW5ncyIsInNvcnQiLCJhIiwiYiIsImxvY2FsZUNvbXBhcmUiLCJvYmoiLCJwcm90b3R5cGUiLCJ0b1N0cmluZyIsImNhbGwiLCJpc0Z1bmN0aW9uIiwiaXNPYmplY3QiLCJpc0RhdGUiLCJEYXRlIiwidG9EYXRlU3RyaW5nIiwiZ2V0RGF0ZSIsInNldERhdGUiLCJyZXZhbCIsImFyZ0xlbiIsImNsb25lRGVlcCIsImRzdCIsInNyYyIsImNsb25lT2JqZWN0RGVlcCIsImNsb25lQXJyYXlEZWVwIiwiY29uc3RydWN0b3IiLCJoYXNPd25Qcm9wZXJ0eSIsInZhbCIsImlzT2JqIiwibmV3VmFsIiwiYWRkRmxhZyIsImoiLCJsIl0sIm1hcHBpbmdzIjoiQUFBQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxDQUFDO0FBQ0QsTztRQ1ZBO1FBQ0E7O1FBRUE7UUFDQTs7UUFFQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7UUFDQTs7UUFFQTtRQUNBOztRQUVBO1FBQ0E7O1FBRUE7UUFDQTtRQUNBOzs7UUFHQTtRQUNBOztRQUVBO1FBQ0E7O1FBRUE7UUFDQTtRQUNBO1FBQ0EsMENBQTBDLGdDQUFnQztRQUMxRTtRQUNBOztRQUVBO1FBQ0E7UUFDQTtRQUNBLHdEQUF3RCxrQkFBa0I7UUFDMUU7UUFDQSxpREFBaUQs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LENBQUMsR0FBR1gsR0FBRyxDQUFDeEwsTUFBdEIsRUFBOEJrTSxDQUFDLEdBQUNDLENBQWhDLEVBQW1DRCxDQUFDLEVBQXBDLEVBQXVDO0FBQ25DLFlBQUdWLEdBQUcsQ0FBQ1UsQ0FBRCxDQUFILElBQVVGLE1BQWIsRUFBb0I7QUFDbEJDLGlCQUFPLEdBQUcsS0FBVjtBQUNBO0FBQ0Q7QUFDSjs7QUFDRCxVQUFHQSxPQUFILEVBQVc7QUFDVFQsV0FBRyxDQUFDckssSUFBSixDQUFTNkssTUFBVDtBQUNEO0FBQ0o7QUFDRDs7QUFDRCxTQUFPUixHQUFQO0FBQ0EsQyIsImZpbGUiOiJzcWwtZm9ybWF0dGVyLmpzIiwic291cmNlc0NvbnRlbnQiOlsiKGZ1bmN0aW9uIHdlYnBhY2tVbml2ZXJzYWxNb2R1bGVEZWZpbml0aW9uKHJvb3QsIGZhY3RvcnkpIHtcblx0aWYodHlwZW9mIGV4cG9ydHMgPT09ICdvYmplY3QnICYmIHR5cGVvZiBtb2R1bGUgPT09ICdvYmplY3QnKVxuXHRcdG1vZHVsZS5leHBvcnRzID0gZmFjdG9yeSgpO1xuXHRlbHNlIGlmKHR5cGVvZiBkZWZpbmUgPT09ICdmdW5jdGlvbicgJiYgZGVmaW5lLmFtZClcblx0XHRkZWZpbmUoW10sIGZhY3RvcnkpO1xuXHRlbHNlIGlmKHR5cGVvZiBleHBvcnRzID09PSAnb2JqZWN0Jylcblx0XHRleHBvcnRzW1wic3FsRm9ybWF0dGVyXCJdID0gZmFjdG9yeSgpO1xuXHRlbHNlXG5cdFx0cm9vdFtcInNxbEZvcm1hdHRlclwiXSA9IGZhY3RvcnkoKTtcbn0pKHdpbmRvdywgZnVuY3Rpb24oKSB7XG5yZXR1cm4gIiwiIFx0Ly8gVGhlIG1vZHVsZSBjYWNoZVxuIFx0dmFyIGluc3RhbGxlZE1vZHVsZXMgPSB7fTtcblxuIFx0Ly8gVGhlIHJlcXVpcmUgZnVuY3Rpb25cbiBcdGZ1bmN0aW9uIF9fd2VicGFja19yZXF1aXJlX18obW9kdWxlSWQpIHtcblxuIFx0XHQvLyBDaGVjayBpZiBtb2R1bGUgaXMgaW4gY2FjaGVcbiBcdFx0aWYoaW5zdGFsbGVkTW9kdWxlc1ttb2R1bGVJZF0pIHtcbiBcdFx0XHRyZXR1cm4gaW5zdGFsbGVkTW9kdWxlc1ttb2R1bGVJZF0uZXhwb3J0cztcbiBcdFx0fVxuIFx0XHQvLyBDcmVhdGUgYSBuZXcgbW9kdWxlIChhbmQgcHV0IGl0IGludG8gdGhlIGNhY2hlKVxuIFx0XHR2YXIgbW9kdWxlID0gaW5zdGFsbGVkTW9kdWxlc1ttb2R1bGVJZF0gPSB7XG4gXHRcdFx0aTogbW9kdWxlSWQsXG4gXHRcdFx0bDogZmFsc2UsXG4gXHRcdFx0ZXhwb3J0czoge31cbiBcdFx0fTtcblxuIFx0XHQvLyBFeGVjdXRlIHRoZSBtb2R1bGUgZnVuY3Rpb25cbiBcdFx0bW9kdWxlc1ttb2R1bGVJZF0uY2FsbChtb2R1bGUuZXhwb3J0cywgbW9kdWxlLCBtb2R1bGUuZXhwb3J0cywgX193ZWJwYWNrX3JlcXVpcmVfXyk7XG5cbiBcdFx0Ly8gRmxhZyB0aGUgbW9kdWxlIGFzIGxvYWRlZFxuIFx0XHRtb2R1bGUubCA9IHRydWU7XG5cbiBcdFx0Ly8gUmV0dXJuIHRoZSBleHBvcnRzIG9mIHRoZSBtb2R1bGVcbiBcdFx0cmV0dXJuIG1vZHVsZS5leHBvcnRzO1xuIFx0fVxuXG5cbiBcdC8vIGV4cG9zZSB0aGUgbW9kdWxlcyBvYmplY3QgKF9fd2VicGFja19tb2R1bGVzX18pXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLm0gPSBtb2R1bGVzO1xuXG4gXHQvLyBleHBvc2UgdGhlIG1vZHVsZSBjYWNoZVxuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5jID0gaW5zdGFsbGVkTW9kdWxlcztcblxuIFx0Ly8gZGVmaW5lIGdldHRlciBmdW5jdGlvbiBmb3IgaGFybW9ueSBleHBvcnRzXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLmQgPSBmdW5jdGlvbihleHBvcnRzLCBuYW1lLCBnZXR0ZXIpIHtcbiBcdFx0aWYoIV9fd2VicGFja19yZXF1aXJlX18ubyhleHBvcnRzLCBuYW1lKSkge1xuIFx0XHRcdE9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBuYW1lLCB7IGVudW1lcmFibGU6IHRydWUsIGdldDogZ2V0dGVyIH0pO1xuIFx0XHR9XG4gXHR9O1xuXG4gXHQvLyBkZWZpbmUgX19lc01vZHVsZSBvbiBleHBvcnRzXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLnIgPSBmdW5jdGlvbihleHBvcnRzKSB7XG4gXHRcdGlmKHR5cGVvZiBTeW1ib2wgIT09ICd1bmRlZmluZWQnICYmIFN5bWJvbC50b1N0cmluZ1RhZykge1xuIFx0XHRcdE9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBTeW1ib2wudG9TdHJpbmdUYWcsIHsgdmFsdWU6ICdNb2R1bGUnIH0pO1xuIFx0XHR9XG4gXHRcdE9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCAnX19lc01vZHVsZScsIHsgdmFsdWU6IHRydWUgfSk7XG4gXHR9O1xuXG4gXHQvLyBjcmVhdGUgYSBmYWtlIG5hbWVzcGFjZSBvYmplY3RcbiBcdC8vIG1vZGUgJiAxOiB2YWx1ZSBpcyBhIG1vZHVsZSBpZCwgcmVxdWlyZSBpdFxuIFx0Ly8gbW9kZSAmIDI6IG1lcmdlIGFsbCBwcm9wZXJ0aWVzIG9mIHZhbHVlIGludG8gdGhlIG5zXG4gXHQvLyBtb2RlICYgNDogcmV0dXJuIHZhbHVlIHdoZW4gYWxyZWFkeSBucyBvYmplY3RcbiBcdC8vIG1vZGUgJiA4fDE6IGJlaGF2ZSBsaWtlIHJlcXVpcmVcbiBcdF9fd2VicGFja19yZXF1aXJlX18udCA9IGZ1bmN0aW9uKHZhbHVlLCBtb2RlKSB7XG4gXHRcdGlmKG1vZGUgJiAxKSB2YWx1ZSA9IF9fd2VicGFja19yZXF1aXJlX18odmFsdWUpO1xuIFx0XHRpZihtb2RlICYgOCkgcmV0dXJuIHZhbHVlO1xuIFx0XHRpZigobW9kZSAmIDQpICYmIHR5cGVvZiB2YWx1ZSA9PT0gJ29iamVjdCcgJiYgdmFsdWUgJiYgdmFsdWUuX19lc01vZHVsZSkgcmV0dXJuIHZhbHVlO1xuIFx0XHR2YXIgbnMgPSBPYmplY3QuY3JlYXRlKG51bGwpO1xuIFx0XHRfX3dlYnBhY2tfcmVxdWlyZV9fLnIobnMpO1xuIFx0XHRPYmplY3QuZGVmaW5lUHJvcGVydHkobnMsICdkZWZhdWx0JywgeyBlbnVtZXJhYmxlOiB0cnVlLCB2YWx1ZTogdmFsdWUgfSk7XG4gXHRcdGlmKG1vZGUgJiAyICYmIHR5cGVvZiB2YWx1ZSAhPSAnc3RyaW5nJykgZm9yKHZhciBrZXkgaW4gdmFsdWUpIF9fd2VicGFja19yZXF1aXJlX18uZChucywga2V5LCBmdW5jdGlvbihrZXkpIHsgcmV0dXJuIHZhbHVlW2tleV07IH0uYmluZChudWxsLCBrZXkpKTtcbiBcdFx0cmV0dXJuIG5zO1xuIFx0fTtcblxuIFx0Ly8gZ2V0RGVmYXVsdEV4cG9ydCBmdW5jdGlvbiBmb3IgY29tcGF0aWJpbGl0eSB3aXRoIG5vbi1oYXJtb255IG1vZHVsZXNcbiBcdF9fd2VicGFja19yZXF1aXJlX18ubiA9IGZ1bmN0aW9uKG1vZHVsZSkge1xuIFx0XHR2YXIgZ2V0dGVyID0gbW9kdWxlICYmIG1vZHVsZS5fX2VzTW9kdWxlID9cbiBcdFx0XHRmdW5jdGlvbiBnZXREZWZhdWx0KCkgeyByZXR1cm4gbW9kdWxlWydkZWZhdWx0J107IH0gOlxuIFx0XHRcdGZ1bmN0aW9uIGdldE1vZHVsZUV4cG9ydHMoKSB7IHJldHVybiBtb2R1bGU7IH07XG4gXHRcdF9fd2VicGFja19yZXF1aXJlX18uZChnZXR0ZXIsICdhJywgZ2V0dGVyKTtcbiBcdFx0cmV0dXJuIGdldHRlcjtcbiBcdH07XG5cbiBcdC8vIE9iamVjdC5wcm90b3R5cGUuaGFzT3duUHJvcGVydHkuY2FsbFxuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5vID0gZnVuY3Rpb24ob2JqZWN0LCBwcm9wZXJ0eSkgeyByZXR1cm4gT2JqZWN0LnByb3RvdHlwZS5oYXNPd25Qcm9wZXJ0eS5jYWxsKG9iamVjdCwgcHJvcGVydHkpOyB9O1xuXG4gXHQvLyBfX3dlYnBhY2tfcHVibGljX3BhdGhfX1xuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5wID0gXCJcIjtcblxuXG4gXHQvLyBMb2FkIGVudHJ5IG1vZHVsZSBhbmQgcmV0dXJuIGV4cG9ydHNcbiBcdHJldHVybiBfX3dlYnBhY2tfcmVxdWlyZV9fKF9fd2VicGFja19yZXF1aXJlX18ucyA9IFwiLi9zcmMvc3FsRm9ybWF0dGVyLmpzXCIpO1xuIiwiaW1wb3J0IHRva2VuVHlwZXMgZnJvbSAnLi90b2tlblR5cGVzJztcbmltcG9ydCBJbmRlbnRhdGlvbiBmcm9tICcuL0luZGVudGF0aW9uJztcbmltcG9ydCBJbmxpbmVCbG9jayBmcm9tICcuL0lubGluZUJsb2NrJztcbmltcG9ydCBQYXJhbXMgZnJvbSAnLi9QYXJhbXMnO1xuaW1wb3J0IHsgdHJpbVNwYWNlc0VuZCB9IGZyb20gJy4uL3V0aWxzJztcbmltcG9ydCB7IGlzQW5kLCBpc0JldHdlZW4sIGlzTGltaXQgfSBmcm9tICcuL3Rva2VuJztcblxuZXhwb3J0IGRlZmF1bHQgY2xhc3MgRm9ybWF0dGVyIHtcbiAgLyoqXG4gICAqIEBwYXJhbSB7T2JqZWN0fSBjZmdcbiAgICogIEBwYXJhbSB7U3RyaW5nfSBjZmcubGFuZ3VhZ2VcbiAgICogIEBwYXJhbSB7U3RyaW5nfSBjZmcuaW5kZW50XG4gICAqICBAcGFyYW0ge0Jvb2xlYW59IGNmZy51cHBlcmNhc2VcbiAgICogIEBwYXJhbSB7SW50ZWdlcn0gY2ZnLmxpbmVzQmV0d2VlblF1ZXJpZXNcbiAgICogIEBwYXJhbSB7T2JqZWN0fSBjZmcucGFyYW1zXG4gICAqL1xuICBjb25zdHJ1Y3RvcihjZmcpIHtcbiAgICB0aGlzLmNmZyA9IGNmZztcbiAgICB0aGlzLmluZGVudGF0aW9uID0gbmV3IEluZGVudGF0aW9uKHRoaXMuY2ZnLmluZGVudCk7XG4gICAgdGhpcy5pbmxpbmVCbG9jayA9IG5ldyBJbmxpbmVCbG9jaygpO1xuICAgIHRoaXMucGFyYW1zID0gbmV3IFBhcmFtcyh0aGlzLmNmZy5wYXJhbXMpO1xuICAgIHRoaXMucHJldmlvdXNSZXNlcnZlZFRva2VuID0ge307XG4gICAgdGhpcy50b2tlbnMgPSBbXTtcbiAgICB0aGlzLmluZGV4ID0gMDtcbiAgfVxuXG4gIC8qKlxuICAgKiBTUUwgVG9rZW5pemVyIGZvciB0aGlzIGZvcm1hdHRlciwgcHJvdmlkZWQgYnkgc3ViY2xhc3Nlcy5cbiAgICovXG4gIHRva2VuaXplcigpIHtcbiAgICB0aHJvdyBuZXcgRXJyb3IoJ3Rva2VuaXplcigpIG5vdCBpbXBsZW1lbnRlZCBieSBzdWJjbGFzcycpO1xuICB9XG5cbiAgLyoqXG4gICAqIFJlcHJvY2VzcyBhbmQgbW9kaWZ5IGEgdG9rZW4gYmFzZWQgb24gcGFyc2VkIGNvbnRleHQuXG4gICAqXG4gICAqIEBwYXJhbSB7T2JqZWN0fSB0b2tlbiBUaGUgdG9rZW4gdG8gbW9kaWZ5XG4gICAqICBAcGFyYW0ge1N0cmluZ30gdG9rZW4udHlwZVxuICAgKiAgQHBhcmFtIHtTdHJpbmd9IHRva2VuLnZhbHVlXG4gICAqIEByZXR1cm4ge09iamVjdH0gbmV3IHRva2VuIG9yIHRoZSBvcmlnaW5hbFxuICAgKiAgQHJldHVybiB7U3RyaW5nfSB0b2tlbi50eXBlXG4gICAqICBAcmV0dXJuIHtTdHJpbmd9IHRva2VuLnZhbHVlXG4gICAqL1xuICB0b2tlbk92ZXJyaWRlKHRva2VuKSB7XG4gICAgLy8gc3ViY2xhc3NlcyBjYW4gb3ZlcnJpZGUgdGhpcyB0byBtb2RpZnkgdG9rZW5zIGR1cmluZyBmb3JtYXR0aW5nXG4gICAgcmV0dXJuIHRva2VuO1xuICB9XG5cbiAgLyoqXG4gICAqIEZvcm1hdHMgd2hpdGVzcGFjZSBpbiBhIFNRTCBzdHJpbmcgdG8gbWFrZSBpdCBlYXNpZXIgdG8gcmVhZC5cbiAgICpcbiAgICogQHBhcmFtIHtTdHJpbmd9IHF1ZXJ5IFRoZSBTUUwgcXVlcnkgc3RyaW5nXG4gICAqIEByZXR1cm4ge1N0cmluZ30gZm9ybWF0dGVkIHF1ZXJ5XG4gICAqL1xuICBmb3JtYXQocXVlcnkpIHtcbiAgICB0aGlzLnRva2VucyA9IHRoaXMudG9rZW5pemVyKCkudG9rZW5pemUocXVlcnkpO1xuICAgIGNvbnN0IGZvcm1hdHRlZFF1ZXJ5ID0gdGhpcy5nZXRGb3JtYXR0ZWRRdWVyeUZyb21Ub2tlbnMoKTtcblxuICAgIHJldHVybiBmb3JtYXR0ZWRRdWVyeS50cmltKCk7XG4gIH1cblxuICBnZXRGb3JtYXR0ZWRRdWVyeUZyb21Ub2tlbnMoKSB7XG4gICAgbGV0IGZvcm1hdHRlZFF1ZXJ5ID0gJyc7XG5cbiAgICB0aGlzLnRva2Vucy5mb3JFYWNoKCh0b2tlbiwgaW5kZXgpID0+IHtcbiAgICAgIHRoaXMuaW5kZXggPSBpbmRleDtcblxuICAgICAgdG9rZW4gPSB0aGlzLnRva2VuT3ZlcnJpZGUodG9rZW4pO1xuXG4gICAgICBpZiAodG9rZW4udHlwZSA9PT0gdG9rZW5UeXBlcy5MSU5FX0NPTU1FTlQpIHtcbiAgICAgICAgZm9ybWF0dGVkUXVlcnkgPSB0aGlzLmZvcm1hdExpbmVDb21tZW50KHRva2VuLCBmb3JtYXR0ZWRRdWVyeSk7XG4gICAgICB9IGVsc2UgaWYgKHRva2VuLnR5cGUgPT09IHRva2VuVHlwZXMuQkxPQ0tfQ09NTUVOVCkge1xuICAgICAgICBmb3JtYXR0ZWRRdWVyeSA9IHRoaXMuZm9ybWF0QmxvY2tDb21tZW50KHRva2VuLCBmb3JtYXR0ZWRRdWVyeSk7XG4gICAgICB9IGVsc2UgaWYgKHRva2VuLnR5cGUgPT09IHRva2VuVHlwZXMuUkVTRVJWRURfVE9QX0xFVkVMKSB7XG4gICAgICAgIGZvcm1hdHRlZFF1ZXJ5ID0gdGhpcy5mb3JtYXRUb3BMZXZlbFJlc2VydmVkV29yZCh0b2tlbiwgZm9ybWF0dGVkUXVlcnkpO1xuICAgICAgICB0aGlzLnByZXZpb3VzUmVzZXJ2ZWRUb2tlbiA9IHRva2VuO1xuICAgICAgfSBlbHNlIGlmICh0b2tlbi50eXBlID09PSB0b2tlblR5cGVzLlJFU0VSVkVEX1RPUF9MRVZFTF9OT19JTkRFTlQpIHtcbiAgICAgICAgZm9ybWF0dGVkUXVlcnkgPSB0aGlzLmZvcm1hdFRvcExldmVsUmVzZXJ2ZWRXb3JkTm9JbmRlbnQodG9rZW4sIGZvcm1hdHRlZFF1ZXJ5KTtcbiAgICAgICAgdGhpcy5wcmV2aW91c1Jlc2VydmVkVG9rZW4gPSB0b2tlbjtcbiAgICAgIH0gZWxzZSBpZiAodG9rZW4udHlwZSA9PT0gdG9rZW5UeXBlcy5SRVNFUlZFRF9ORVdMSU5FKSB7XG4gICAgICAgIGZvcm1hdHRlZFF1ZXJ5ID0gdGhpcy5mb3JtYXROZXdsaW5lUmVzZXJ2ZWRXb3JkKHRva2VuLCBmb3JtYXR0ZWRRdWVyeSk7XG4gICAgICAgIHRoaXMucHJldmlvdXNSZXNlcnZlZFRva2VuID0gdG9rZW47XG4gICAgICB9IGVsc2UgaWYgKHRva2VuLnR5cGUgPT09IHRva2VuVHlwZXMuUkVTRVJWRUQpIHtcbiAgICAgICAgZm9ybWF0dGVkUXVlcnkgPSB0aGlzLmZvcm1hdFdpdGhTcGFjZXModG9rZW4sIGZvcm1hdHRlZFF1ZXJ5KTtcbiAgICAgICAgdGhpcy5wcmV2aW91c1Jlc2VydmVkVG9rZW4gPSB0b2tlbjtcbiAgICAgIH0gZWxzZSBpZiAodG9rZW4udHlwZSA9PT0gdG9rZW5UeXBlcy5PUEVOX1BBUkVOKSB7XG4gICAgICAgIGZvcm1hdHRlZFF1ZXJ5ID0gdGhpcy5mb3JtYXRPcGVuaW5nUGFyZW50aGVzZXModG9rZW4sIGZvcm1hdHRlZFF1ZXJ5KTtcbiAgICAgIH0gZWxzZSBpZiAodG9rZW4udHlwZSA9PT0gdG9rZW5UeXBlcy5DTE9TRV9QQVJFTikge1xuICAgICAgICBmb3JtYXR0ZWRRdWVyeSA9IHRoaXMuZm9ybWF0Q2xvc2luZ1BhcmVudGhlc2VzKHRva2VuLCBmb3JtYXR0ZWRRdWVyeSk7XG4gICAgICB9IGVsc2UgaWYgKHRva2VuLnR5cGUgPT09IHRva2VuVHlwZXMuUExBQ0VIT0xERVIpIHtcbiAgICAgICAgZm9ybWF0dGVkUXVlcnkgPSB0aGlzLmZvcm1hdFBsYWNlaG9sZGVyKHRva2VuLCBmb3JtYXR0ZWRRdWVyeSk7XG4gICAgICB9IGVsc2UgaWYgKHRva2VuLnZhbHVlID09PSAnLCcpIHtcbiAgICAgICAgZm9ybWF0dGVkUXVlcnkgPSB0aGlzLmZvcm1hdENvbW1hKHRva2VuLCBmb3JtYXR0ZWRRdWVyeSk7XG4gICAgICB9IGVsc2UgaWYgKHRva2VuLnZhbHVlID09PSAnOicpIHtcbiAgICAgICAgZm9ybWF0dGVkUXVlcnkgPSB0aGlzLmZvcm1hdFdpdGhTcGFjZUFmdGVyKHRva2VuLCBmb3JtYXR0ZWRRdWVyeSk7XG4gICAgICB9IGVsc2UgaWYgKHRva2VuLnZhbHVlID09PSAnLicpIHtcbiAgICAgICAgZm9ybWF0dGVkUXVlcnkgPSB0aGlzLmZvcm1hdFdpdGhvdXRTcGFjZXModG9rZW4sIGZvcm1hdHRlZFF1ZXJ5KTtcbiAgICAgIH0gZWxzZSBpZiAodG9rZW4udmFsdWUgPT09ICc7Jykge1xuICAgICAgICBmb3JtYXR0ZWRRdWVyeSA9IHRoaXMuZm9ybWF0UXVlcnlTZXBhcmF0b3IodG9rZW4sIGZvcm1hdHRlZFF1ZXJ5KTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIGZvcm1hdHRlZFF1ZXJ5ID0gdGhpcy5mb3JtYXRXaXRoU3BhY2VzKHRva2VuLCBmb3JtYXR0ZWRRdWVyeSk7XG4gICAgICB9XG4gICAgfSk7XG4gICAgcmV0dXJuIGZvcm1hdHRlZFF1ZXJ5O1xuICB9XG5cbiAgZm9ybWF0TGluZUNvbW1lbnQodG9rZW4sIHF1ZXJ5KSB7XG4gICAgLy8gYWRkIGNvbW1lbnQgaW5kZW50XG4gICAgaWYodG9rZW4udHlwZSA9PT10b2tlblR5cGVzLkxJTkVfQ09NTUVOVCAgJiYgdG9rZW4ud2hpdGVzcGFjZUJlZm9yZS5tYXRjaCgvXFxuLykpe1xuICAgICAgdmFsdWUgPSAnXFxuJyt2YWx1ZTsgICBcbiAgICAgIHF1ZXJ5ID0gcXVlcnkucmVwbGFjZSgvXFxzKyQvZyxcIlwiKTtcbiAgICAgIHZhciB2YWx1ZSA9IHRoaXMuaW5kZW50YXRpb24uZ2V0SW5kZW50KCkgK3RoaXMuc2hvdyh0b2tlbik7XG4gICAgICByZXR1cm4gdGhpcy5hZGROZXdsaW5lKHF1ZXJ5ICsgJ1xcbicrdmFsdWUpO1xuICAgIH1lbHNle1xuICAgICAgcmV0dXJuIHRoaXMuYWRkTmV3bGluZShxdWVyeSArIHRoaXMuc2hvdyh0b2tlbikpO1xuICAgIH1cbiAgfVxuXG4gIGZvcm1hdEJsb2NrQ29tbWVudCh0b2tlbiwgcXVlcnkpIHtcbiAgICByZXR1cm4gdGhpcy5hZGROZXdsaW5lKHRoaXMuYWRkTmV3bGluZShxdWVyeSkgKyB0aGlzLmluZGVudENvbW1lbnQodG9rZW4udmFsdWUpKTtcbiAgfVxuXG4gIGluZGVudENvbW1lbnQoY29tbWVudCkge1xuICAgIHJldHVybiBjb21tZW50LnJlcGxhY2UoL1xcblsgXFx0XSovZ3UsICdcXG4nICsgdGhpcy5pbmRlbnRhdGlvbi5nZXRJbmRlbnQoKSArICcgJyk7XG4gIH1cblxuICBmb3JtYXRUb3BMZXZlbFJlc2VydmVkV29yZE5vSW5kZW50KHRva2VuLCBxdWVyeSkge1xuICAgIHRoaXMuaW5kZW50YXRpb24uZGVjcmVhc2VUb3BMZXZlbCgpO1xuICAgIHF1ZXJ5ID0gdGhpcy5hZGROZXdsaW5lKHF1ZXJ5KSArIHRoaXMuZXF1YWxpemVXaGl0ZXNwYWNlKHRoaXMuc2hvdyh0b2tlbikpO1xuICAgIHJldHVybiB0aGlzLmFkZE5ld2xpbmUocXVlcnkpO1xuICB9XG5cbiAgZm9ybWF0VG9wTGV2ZWxSZXNlcnZlZFdvcmQodG9rZW4sIHF1ZXJ5KSB7XG4gICAgdGhpcy5pbmRlbnRhdGlvbi5kZWNyZWFzZVRvcExldmVsKCk7XG5cbiAgICBxdWVyeSA9IHRoaXMuYWRkTmV3bGluZShxdWVyeSk7XG5cbiAgICB0aGlzLmluZGVudGF0aW9uLmluY3JlYXNlVG9wTGV2ZWwoKTtcblxuICAgIHF1ZXJ5ICs9IHRoaXMuZXF1YWxpemVXaGl0ZXNwYWNlKHRoaXMuc2hvdyh0b2tlbikpO1xuICAgIHJldHVybiB0aGlzLmFkZE5ld2xpbmUocXVlcnkpO1xuICB9XG5cbiAgZm9ybWF0TmV3bGluZVJlc2VydmVkV29yZCh0b2tlbiwgcXVlcnkpIHtcbiAgICBpZiAoaXNBbmQodG9rZW4pICYmIGlzQmV0d2Vlbih0aGlzLnRva2VuTG9va0JlaGluZCgyKSkpIHtcbiAgICAgIHJldHVybiB0aGlzLmZvcm1hdFdpdGhTcGFjZXModG9rZW4sIHF1ZXJ5KTtcbiAgICB9XG4gICAgcmV0dXJuIHRoaXMuYWRkTmV3bGluZShxdWVyeSkgKyB0aGlzLmVxdWFsaXplV2hpdGVzcGFjZSh0aGlzLnNob3codG9rZW4pKSArICcgJztcbiAgfVxuXG4gIC8vIFJlcGxhY2UgYW55IHNlcXVlbmNlIG9mIHdoaXRlc3BhY2UgY2hhcmFjdGVycyB3aXRoIHNpbmdsZSBzcGFjZVxuICBlcXVhbGl6ZVdoaXRlc3BhY2Uoc3RyaW5nKSB7XG4gICAgcmV0dXJuIHN0cmluZy5yZXBsYWNlKC9cXHMrL2d1LCAnICcpO1xuICB9XG5cbiAgLy8gT3BlbmluZyBwYXJlbnRoZXNlcyBpbmNyZWFzZSB0aGUgYmxvY2sgaW5kZW50IGxldmVsIGFuZCBzdGFydCBhIG5ldyBsaW5lXG4gIGZvcm1hdE9wZW5pbmdQYXJlbnRoZXNlcyh0b2tlbiwgcXVlcnkpIHtcbiAgICAvLyBUYWtlIG91dCB0aGUgcHJlY2VkaW5nIHNwYWNlIHVubGVzcyB0aGVyZSB3YXMgd2hpdGVzcGFjZSB0aGVyZSBpbiB0aGUgb3JpZ2luYWwgcXVlcnlcbiAgICAvLyBvciBhbm90aGVyIG9wZW5pbmcgcGFyZW5zIG9yIGxpbmUgY29tbWVudFxuICAgIGNvbnN0IHByZXNlcnZlV2hpdGVzcGFjZUZvciA9IHtcbiAgICAgIFt0b2tlblR5cGVzLk9QRU5fUEFSRU5dOiB0cnVlLFxuICAgICAgW3Rva2VuVHlwZXMuTElORV9DT01NRU5UXTogdHJ1ZSxcbiAgICAgIFt0b2tlblR5cGVzLk9QRVJBVE9SXTogdHJ1ZSxcbiAgICB9O1xuICAgIGlmIChcbiAgICAgIHRva2VuLndoaXRlc3BhY2VCZWZvcmUubGVuZ3RoID09PSAwICYmXG4gICAgICAhcHJlc2VydmVXaGl0ZXNwYWNlRm9yW3RoaXMudG9rZW5Mb29rQmVoaW5kKCk/LnR5cGVdXG4gICAgKSB7XG4gICAgICBxdWVyeSA9IHRyaW1TcGFjZXNFbmQocXVlcnkpO1xuICAgIH1cbiAgICBxdWVyeSArPSB0aGlzLnNob3codG9rZW4pO1xuXG4gICAgdGhpcy5pbmxpbmVCbG9jay5iZWdpbklmUG9zc2libGUodGhpcy50b2tlbnMsIHRoaXMuaW5kZXgpO1xuXG4gICAgaWYgKCF0aGlzLmlubGluZUJsb2NrLmlzQWN0aXZlKCkpIHtcbiAgICAgIHRoaXMuaW5kZW50YXRpb24uaW5jcmVhc2VCbG9ja0xldmVsKCk7XG4gICAgICBxdWVyeSA9IHRoaXMuYWRkTmV3bGluZShxdWVyeSk7XG4gICAgfVxuICAgIHJldHVybiBxdWVyeTtcbiAgfVxuXG4gIC8vIENsb3NpbmcgcGFyZW50aGVzZXMgZGVjcmVhc2UgdGhlIGJsb2NrIGluZGVudCBsZXZlbFxuICBmb3JtYXRDbG9zaW5nUGFyZW50aGVzZXModG9rZW4sIHF1ZXJ5KSB7XG4gICAgaWYgKHRoaXMuaW5saW5lQmxvY2suaXNBY3RpdmUoKSkge1xuICAgICAgdGhpcy5pbmxpbmVCbG9jay5lbmQoKTtcbiAgICAgIHJldHVybiB0aGlzLmZvcm1hdFdpdGhTcGFjZUFmdGVyKHRva2VuLCBxdWVyeSk7XG4gICAgfSBlbHNlIHtcbiAgICAgIHRoaXMuaW5kZW50YXRpb24uZGVjcmVhc2VCbG9ja0xldmVsKCk7XG4gICAgICByZXR1cm4gdGhpcy5mb3JtYXRXaXRoU3BhY2VzKHRva2VuLCB0aGlzLmFkZE5ld2xpbmUocXVlcnkpKTtcbiAgICB9XG4gIH1cblxuICBmb3JtYXRQbGFjZWhvbGRlcih0b2tlbiwgcXVlcnkpIHtcbiAgICByZXR1cm4gcXVlcnkgKyB0aGlzLnBhcmFtcy5nZXQodG9rZW4pICsgJyAnO1xuICB9XG5cbiAgLy8gQ29tbWFzIHN0YXJ0IGEgbmV3IGxpbmUgKHVubGVzcyB3aXRoaW4gaW5saW5lIHBhcmVudGhlc2VzIG9yIFNRTCBcIkxJTUlUXCIgY2xhdXNlKVxuICBmb3JtYXRDb21tYSh0b2tlbiwgcXVlcnkpIHtcbiAgICBxdWVyeSA9IHRyaW1TcGFjZXNFbmQocXVlcnkpICsgdGhpcy5zaG93KHRva2VuKSArICcgJztcblxuICAgIGlmICh0aGlzLmlubGluZUJsb2NrLmlzQWN0aXZlKCkpIHtcbiAgICAgIHJldHVybiBxdWVyeTtcbiAgICB9IGVsc2UgaWYgKGlzTGltaXQodGhpcy5wcmV2aW91c1Jlc2VydmVkVG9rZW4pKSB7XG4gICAgICByZXR1cm4gcXVlcnk7XG4gICAgfSBlbHNlIHtcbiAgICAgIHJldHVybiB0aGlzLmFkZE5ld2xpbmUocXVlcnkpO1xuICAgIH1cbiAgfVxuXG4gIGZvcm1hdFdpdGhTcGFjZUFmdGVyKHRva2VuLCBxdWVyeSkge1xuICAgIHJldHVybiB0cmltU3BhY2VzRW5kKHF1ZXJ5KSArIHRoaXMuc2hvdyh0b2tlbikgKyAnICc7XG4gIH1cblxuICBmb3JtYXRXaXRob3V0U3BhY2VzKHRva2VuLCBxdWVyeSkge1xuICAgIHJldHVybiB0cmltU3BhY2VzRW5kKHF1ZXJ5KSArIHRoaXMuc2hvdyh0b2tlbik7XG4gIH1cblxuICBmb3JtYXRXaXRoU3BhY2VzKHRva2VuLCBxdWVyeSkge1xuICAgIHJldHVybiBxdWVyeSArIHRoaXMuc2hvdyh0b2tlbikgKyAnICc7XG4gIH1cblxuICBmb3JtYXRRdWVyeVNlcGFyYXRvcih0b2tlbiwgcXVlcnkpIHtcbiAgICB0aGlzLmluZGVudGF0aW9uLnJlc2V0SW5kZW50YXRpb24oKTtcbiAgICByZXR1cm4gdHJpbVNwYWNlc0VuZChxdWVyeSkgKyB0aGlzLnNob3codG9rZW4pICsgJ1xcbicucmVwZWF0KHRoaXMuY2ZnLmxpbmVzQmV0d2VlblF1ZXJpZXMgfHwgMSk7XG4gIH1cblxuICAvLyBDb252ZXJ0cyB0b2tlbiB0byBzdHJpbmcgKHVwcGVyY2FzaW5nIGl0IGlmIG5lZWRlZClcbiAgc2hvdyh7IHR5cGUsIHZhbHVlIH0pIHtcbiAgICBcblxuICAgIGlmIChcbiAgICAgIHRoaXMuY2ZnLnVwcGVyY2FzZSAmJlxuICAgICAgKHR5cGUgPT09IHRva2VuVHlwZXMuUkVTRVJWRUQgfHxcbiAgICAgICAgdHlwZSA9PT0gdG9rZW5UeXBlcy5SRVNFUlZFRF9UT1BfTEVWRUwgfHxcbiAgICAgICAgdHlwZSA9PT0gdG9rZW5UeXBlcy5SRVNFUlZFRF9UT1BfTEVWRUxfTk9fSU5ERU5UIHx8XG4gICAgICAgIHR5cGUgPT09IHRva2VuVHlwZXMuUkVTRVJWRURfTkVXTElORSB8fFxuICAgICAgICB0eXBlID09PSB0b2tlblR5cGVzLk9QRU5fUEFSRU4gfHxcbiAgICAgICAgdHlwZSA9PT0gdG9rZW5UeXBlcy5DTE9TRV9QQVJFTilcbiAgICApIHtcbiAgICAgIHJldHVybiB2YWx1ZS50b1VwcGVyQ2FzZSgpO1xuICAgIH0gZWxzZSB7XG4gICAgICByZXR1cm4gdmFsdWU7XG4gICAgfVxuICB9XG5cbiAgYWRkTmV3bGluZShxdWVyeSkge1xuICAgIHF1ZXJ5ID0gdHJpbVNwYWNlc0VuZChxdWVyeSk7XG4gICAgaWYgKCFxdWVyeS5lbmRzV2l0aCgnXFxuJykpIHtcbiAgICAgIHF1ZXJ5ICs9ICdcXG4nO1xuICAgIH1cbiAgICByZXR1cm4gcXVlcnkgKyB0aGlzLmluZGVudGF0aW9uLmdldEluZGVudCgpO1xuICB9XG5cbiAgdG9rZW5Mb29rQmVoaW5kKG4gPSAxKSB7XG4gICAgcmV0dXJuIHRoaXMudG9rZW5zW3RoaXMuaW5kZXggLSBuXTtcbiAgfVxuXG4gIHRva2VuTG9va0FoZWFkKG4gPSAxKSB7XG4gICAgcmV0dXJuIHRoaXMudG9rZW5zW3RoaXMuaW5kZXggKyBuXTtcbiAgfVxufVxuIiwiaW1wb3J0IHsgbGFzdCB9IGZyb20gJy4uL3V0aWxzJztcblxuY29uc3QgSU5ERU5UX1RZUEVfVE9QX0xFVkVMID0gJ3RvcC1sZXZlbCc7XG5jb25zdCBJTkRFTlRfVFlQRV9CTE9DS19MRVZFTCA9ICdibG9jay1sZXZlbCc7XG5cbi8qKlxuICogTWFuYWdlcyBpbmRlbnRhdGlvbiBsZXZlbHMuXG4gKlxuICogVGhlcmUgYXJlIHR3byB0eXBlcyBvZiBpbmRlbnRhdGlvbiBsZXZlbHM6XG4gKlxuICogLSBCTE9DS19MRVZFTCA6IGluY3JlYXNlZCBieSBvcGVuLXBhcmVudGhlc2lzXG4gKiAtIFRPUF9MRVZFTCA6IGluY3JlYXNlZCBieSBSRVNFUlZFRF9UT1BfTEVWRUwgd29yZHNcbiAqL1xuZXhwb3J0IGRlZmF1bHQgY2xhc3MgSW5kZW50YXRpb24ge1xuICAvKipcbiAgICogQHBhcmFtIHtTdHJpbmd9IGluZGVudCBJbmRlbnQgdmFsdWUsIGRlZmF1bHQgaXMgXCIgIFwiICgyIHNwYWNlcylcbiAgICovXG4gIGNvbnN0cnVjdG9yKGluZGVudCkge1xuICAgIHRoaXMuaW5kZW50ID0gaW5kZW50IHx8ICcgICc7XG4gICAgdGhpcy5pbmRlbnRUeXBlcyA9IFtdO1xuICB9XG5cbiAgLyoqXG4gICAqIFJldHVybnMgY3VycmVudCBpbmRlbnRhdGlvbiBzdHJpbmcuXG4gICAqIEByZXR1cm4ge1N0cmluZ31cbiAgICovXG4gIGdldEluZGVudCgpIHtcbiAgICByZXR1cm4gdGhpcy5pbmRlbnQucmVwZWF0KHRoaXMuaW5kZW50VHlwZXMubGVuZ3RoKTtcbiAgfVxuXG4gIC8qKlxuICAgKiBJbmNyZWFzZXMgaW5kZW50YXRpb24gYnkgb25lIHRvcC1sZXZlbCBpbmRlbnQuXG4gICAqL1xuICBpbmNyZWFzZVRvcExldmVsKCkge1xuICAgIHRoaXMuaW5kZW50VHlwZXMucHVzaChJTkRFTlRfVFlQRV9UT1BfTEVWRUwpO1xuICB9XG5cbiAgLyoqXG4gICAqIEluY3JlYXNlcyBpbmRlbnRhdGlvbiBieSBvbmUgYmxvY2stbGV2ZWwgaW5kZW50LlxuICAgKi9cbiAgaW5jcmVhc2VCbG9ja0xldmVsKCkge1xuICAgIHRoaXMuaW5kZW50VHlwZXMucHVzaChJTkRFTlRfVFlQRV9CTE9DS19MRVZFTCk7XG4gIH1cblxuICAvKipcbiAgICogRGVjcmVhc2VzIGluZGVudGF0aW9uIGJ5IG9uZSB0b3AtbGV2ZWwgaW5kZW50LlxuICAgKiBEb2VzIG5vdGhpbmcgd2hlbiB0aGUgcHJldmlvdXMgaW5kZW50IGlzIG5vdCB0b3AtbGV2ZWwuXG4gICAqL1xuICBkZWNyZWFzZVRvcExldmVsKCkge1xuICAgIGlmICh0aGlzLmluZGVudFR5cGVzLmxlbmd0aCA+IDAgJiYgbGFzdCh0aGlzLmluZGVudFR5cGVzKSA9PT0gSU5ERU5UX1RZUEVfVE9QX0xFVkVMKSB7XG4gICAgICB0aGlzLmluZGVudFR5cGVzLnBvcCgpO1xuICAgIH1cbiAgfVxuXG4gIC8qKlxuICAgKiBEZWNyZWFzZXMgaW5kZW50YXRpb24gYnkgb25lIGJsb2NrLWxldmVsIGluZGVudC5cbiAgICogSWYgdGhlcmUgYXJlIHRvcC1sZXZlbCBpbmRlbnRzIHdpdGhpbiB0aGUgYmxvY2stbGV2ZWwgaW5kZW50LFxuICAgKiB0aHJvd3MgYXdheSB0aGVzZSBhcyB3ZWxsLlxuICAgKi9cbiAgZGVjcmVhc2VCbG9ja0xldmVsKCkge1xuICAgIHdoaWxlICh0aGlzLmluZGVudFR5cGVzLmxlbmd0aCA+IDApIHtcbiAgICAgIGNvbnN0IHR5cGUgPSB0aGlzLmluZGVudFR5cGVzLnBvcCgpO1xuICAgICAgaWYgKHR5cGUgIT09IElOREVOVF9UWVBFX1RPUF9MRVZFTCkge1xuICAgICAgICBicmVhaztcbiAgICAgIH1cbiAgICB9XG4gIH1cblxuICByZXNldEluZGVudGF0aW9uKCkge1xuICAgIHRoaXMuaW5kZW50VHlwZXMgPSBbXTtcbiAgfVxufVxuIiwiaW1wb3J0IHRva2VuVHlwZXMgZnJvbSAnLi90b2tlblR5cGVzJztcblxuY29uc3QgSU5MSU5FX01BWF9MRU5HVEggPSA1MDtcblxuLyoqXG4gKiBCb29ra2VlcGVyIGZvciBpbmxpbmUgYmxvY2tzLlxuICpcbiAqIElubGluZSBibG9ja3MgYXJlIHBhcmVudGhpemVkIGV4cHJlc3Npb25zIHRoYXQgYXJlIHNob3J0ZXIgdGhhbiBJTkxJTkVfTUFYX0xFTkdUSC5cbiAqIFRoZXNlIGJsb2NrcyBhcmUgZm9ybWF0dGVkIG9uIGEgc2luZ2xlIGxpbmUsIHVubGlrZSBsb25nZXIgcGFyZW50aGl6ZWRcbiAqIGV4cHJlc3Npb25zIHdoZXJlIG9wZW4tcGFyZW50aGVzaXMgY2F1c2VzIG5ld2xpbmUgYW5kIGluY3JlYXNlIG9mIGluZGVudGF0aW9uLlxuICovXG5leHBvcnQgZGVmYXVsdCBjbGFzcyBJbmxpbmVCbG9jayB7XG4gIGNvbnN0cnVjdG9yKCkge1xuICAgIHRoaXMubGV2ZWwgPSAwO1xuICB9XG5cbiAgLyoqXG4gICAqIEJlZ2lucyBpbmxpbmUgYmxvY2sgd2hlbiBsb29rYWhlYWQgdGhyb3VnaCB1cGNvbWluZyB0b2tlbnMgZGV0ZXJtaW5lc1xuICAgKiB0aGF0IHRoZSBibG9jayB3b3VsZCBiZSBzbWFsbGVyIHRoYW4gSU5MSU5FX01BWF9MRU5HVEguXG4gICAqIEBwYXJhbSAge09iamVjdFtdfSB0b2tlbnMgQXJyYXkgb2YgYWxsIHRva2Vuc1xuICAgKiBAcGFyYW0gIHtOdW1iZXJ9IGluZGV4IEN1cnJlbnQgdG9rZW4gcG9zaXRpb25cbiAgICovXG4gIGJlZ2luSWZQb3NzaWJsZSh0b2tlbnMsIGluZGV4KSB7XG4gICAgaWYgKHRoaXMubGV2ZWwgPT09IDAgJiYgdGhpcy5pc0lubGluZUJsb2NrKHRva2VucywgaW5kZXgpKSB7XG4gICAgICB0aGlzLmxldmVsID0gMTtcbiAgICB9IGVsc2UgaWYgKHRoaXMubGV2ZWwgPiAwKSB7XG4gICAgICB0aGlzLmxldmVsKys7XG4gICAgfSBlbHNlIHtcbiAgICAgIHRoaXMubGV2ZWwgPSAwO1xuICAgIH1cbiAgfVxuXG4gIC8qKlxuICAgKiBGaW5pc2hlcyBjdXJyZW50IGlubGluZSBibG9jay5cbiAgICogVGhlcmUgbWlnaHQgYmUgc2V2ZXJhbCBuZXN0ZWQgb25lcy5cbiAgICovXG4gIGVuZCgpIHtcbiAgICB0aGlzLmxldmVsLS07XG4gIH1cblxuICAvKipcbiAgICogVHJ1ZSB3aGVuIGluc2lkZSBhbiBpbmxpbmUgYmxvY2tcbiAgICogQHJldHVybiB7Qm9vbGVhbn1cbiAgICovXG4gIGlzQWN0aXZlKCkge1xuICAgIHJldHVybiB0aGlzLmxldmVsID4gMDtcbiAgfVxuXG4gIC8vIENoZWNrIGlmIHRoaXMgc2hvdWxkIGJlIGFuIGlubGluZSBwYXJlbnRoZXNlcyBibG9ja1xuICAvLyBFeGFtcGxlcyBhcmUgXCJOT1coKVwiLCBcIkNPVU5UKCopXCIsIFwiaW50KDEwKVwiLCBrZXkoYHNvbWVjb2x1bW5gKSwgREVDSU1BTCg3LDIpXG4gIGlzSW5saW5lQmxvY2sodG9rZW5zLCBpbmRleCkge1xuICAgIGxldCBsZW5ndGggPSAwO1xuICAgIGxldCBsZXZlbCA9IDA7XG5cbiAgICBmb3IgKGxldCBpID0gaW5kZXg7IGkgPCB0b2tlbnMubGVuZ3RoOyBpKyspIHtcbiAgICAgIGNvbnN0IHRva2VuID0gdG9rZW5zW2ldO1xuICAgICAgbGVuZ3RoICs9IHRva2VuLnZhbHVlLmxlbmd0aDtcblxuICAgICAgLy8gT3ZlcnJhbiBtYXggbGVuZ3RoXG4gICAgICBpZiAobGVuZ3RoID4gSU5MSU5FX01BWF9MRU5HVEgpIHtcbiAgICAgICAgcmV0dXJuIGZhbHNlO1xuICAgICAgfVxuXG4gICAgICBpZiAodG9rZW4udHlwZSA9PT0gdG9rZW5UeXBlcy5PUEVOX1BBUkVOKSB7XG4gICAgICAgIGxldmVsKys7XG4gICAgICB9IGVsc2UgaWYgKHRva2VuLnR5cGUgPT09IHRva2VuVHlwZXMuQ0xPU0VfUEFSRU4pIHtcbiAgICAgICAgbGV2ZWwtLTtcbiAgICAgICAgaWYgKGxldmVsID09PSAwKSB7XG4gICAgICAgICAgcmV0dXJuIHRydWU7XG4gICAgICAgIH1cbiAgICAgIH1cblxuICAgICAgaWYgKHRoaXMuaXNGb3JiaWRkZW5Ub2tlbih0b2tlbikpIHtcbiAgICAgICAgcmV0dXJuIGZhbHNlO1xuICAgICAgfVxuICAgIH1cbiAgICByZXR1cm4gZmFsc2U7XG4gIH1cblxuICAvLyBSZXNlcnZlZCB3b3JkcyB0aGF0IGNhdXNlIG5ld2xpbmVzLCBjb21tZW50cyBhbmQgc2VtaWNvbG9uc1xuICAvLyBhcmUgbm90IGFsbG93ZWQgaW5zaWRlIGlubGluZSBwYXJlbnRoZXNlcyBibG9ja1xuICBpc0ZvcmJpZGRlblRva2VuKHsgdHlwZSwgdmFsdWUgfSkge1xuICAgIHJldHVybiAoXG4gICAgICB0eXBlID09PSB0b2tlblR5cGVzLlJFU0VSVkVEX1RPUF9MRVZFTCB8fFxuICAgICAgdHlwZSA9PT0gdG9rZW5UeXBlcy5SRVNFUlZFRF9ORVdMSU5FIHx8XG4gICAgICB0eXBlID09PSB0b2tlblR5cGVzLkNPTU1FTlQgfHxcbiAgICAgIHR5cGUgPT09IHRva2VuVHlwZXMuQkxPQ0tfQ09NTUVOVCB8fFxuICAgICAgdmFsdWUgPT09ICc7J1xuICAgICk7XG4gIH1cbn1cbiIsIi8qKlxuICogSGFuZGxlcyBwbGFjZWhvbGRlciByZXBsYWNlbWVudCB3aXRoIGdpdmVuIHBhcmFtcy5cbiAqL1xuZXhwb3J0IGRlZmF1bHQgY2xhc3MgUGFyYW1zIHtcbiAgLyoqXG4gICAqIEBwYXJhbSB7T2JqZWN0fSBwYXJhbXNcbiAgICovXG4gIGNvbnN0cnVjdG9yKHBhcmFtcykge1xuICAgIHRoaXMucGFyYW1zID0gcGFyYW1zO1xuICAgIHRoaXMuaW5kZXggPSAwO1xuICB9XG5cbiAgLyoqXG4gICAqIFJldHVybnMgcGFyYW0gdmFsdWUgdGhhdCBtYXRjaGVzIGdpdmVuIHBsYWNlaG9sZGVyIHdpdGggcGFyYW0ga2V5LlxuICAgKiBAcGFyYW0ge09iamVjdH0gdG9rZW5cbiAgICogICBAcGFyYW0ge1N0cmluZ30gdG9rZW4ua2V5IFBsYWNlaG9sZGVyIGtleVxuICAgKiAgIEBwYXJhbSB7U3RyaW5nfSB0b2tlbi52YWx1ZSBQbGFjZWhvbGRlciB2YWx1ZVxuICAgKiBAcmV0dXJuIHtTdHJpbmd9IHBhcmFtIG9yIHRva2VuLnZhbHVlIHdoZW4gcGFyYW1zIGFyZSBtaXNzaW5nXG4gICAqL1xuICBnZXQoeyBrZXksIHZhbHVlIH0pIHtcbiAgICBpZiAoIXRoaXMucGFyYW1zKSB7XG4gICAgICByZXR1cm4gdmFsdWU7XG4gICAgfVxuICAgIGlmIChrZXkpIHtcbiAgICAgIHJldHVybiB0aGlzLnBhcmFtc1trZXldO1xuICAgIH1cbiAgICByZXR1cm4gdGhpcy5wYXJhbXNbdGhpcy5pbmRleCsrXTtcbiAgfVxufVxuIiwiaW1wb3J0IHRva2VuVHlwZXMgZnJvbSAnLi90b2tlblR5cGVzJztcbmltcG9ydCAqIGFzIHJlZ2V4RmFjdG9yeSBmcm9tICcuL3JlZ2V4RmFjdG9yeSc7XG5pbXBvcnQgeyBlc2NhcGVSZWdFeHAsb2JqZWN0TWVyZ2UgfSBmcm9tICcuLi91dGlscyc7XG5cbmV4cG9ydCBkZWZhdWx0IGNsYXNzIFRva2VuaXplciB7XG4gIC8qKlxuICAgKiBAcGFyYW0ge09iamVjdH0gY2ZnXG4gICAqICBAcGFyYW0ge1N0cmluZ1tdfSBjZmcucmVzZXJ2ZWRXb3JkcyBSZXNlcnZlZCB3b3JkcyBpbiBTUUxcbiAgICogIEBwYXJhbSB7U3RyaW5nW119IGNmZy5yZXNlcnZlZFRvcExldmVsV29yZHMgV29yZHMgdGhhdCBhcmUgc2V0IHRvIG5ldyBsaW5lIHNlcGFyYXRlbHlcbiAgICogIEBwYXJhbSB7U3RyaW5nW119IGNmZy5yZXNlcnZlZE5ld2xpbmVXb3JkcyBXb3JkcyB0aGF0IGFyZSBzZXQgdG8gbmV3bGluZVxuICAgKiAgQHBhcmFtIHtTdHJpbmdbXX0gY2ZnLnJlc2VydmVkVG9wTGV2ZWxXb3Jkc05vSW5kZW50IFdvcmRzIHRoYXQgYXJlIHRvcCBsZXZlbCBidXQgaGF2ZSBubyBpbmRlbnRhdGlvblxuICAgKiAgQHBhcmFtIHtTdHJpbmdbXX0gY2ZnLnN0cmluZ1R5cGVzIFN0cmluZyB0eXBlcyB0byBlbmFibGU6IFwiXCIsICcnLCBgYCwgW10sIE4nJ1xuICAgKiAgQHBhcmFtIHtTdHJpbmdbXX0gY2ZnLm9wZW5QYXJlbnMgT3BlbmluZyBwYXJlbnRoZXNlcyB0byBlbmFibGUsIGxpa2UgKCwgW1xuICAgKiAgQHBhcmFtIHtTdHJpbmdbXX0gY2ZnLmNsb3NlUGFyZW5zIENsb3NpbmcgcGFyZW50aGVzZXMgdG8gZW5hYmxlLCBsaWtlICksIF1cbiAgICogIEBwYXJhbSB7U3RyaW5nW119IGNmZy5pbmRleGVkUGxhY2Vob2xkZXJUeXBlcyBQcmVmaXhlcyBmb3IgaW5kZXhlZCBwbGFjZWhvbGRlcnMsIGxpa2UgP1xuICAgKiAgQHBhcmFtIHtTdHJpbmdbXX0gY2ZnLm5hbWVkUGxhY2Vob2xkZXJUeXBlcyBQcmVmaXhlcyBmb3IgbmFtZWQgcGxhY2Vob2xkZXJzLCBsaWtlIEAgYW5kIDpcbiAgICogIEBwYXJhbSB7U3RyaW5nW119IGNmZy5saW5lQ29tbWVudFR5cGVzIExpbmUgY29tbWVudHMgdG8gZW5hYmxlLCBsaWtlICMgYW5kIC0tXG4gICAqICBAcGFyYW0ge1N0cmluZ1tdfSBjZmcuc3BlY2lhbFdvcmRDaGFycyBTcGVjaWFsIGNoYXJzIHRoYXQgY2FuIGJlIGZvdW5kIGluc2lkZSBvZiB3b3JkcywgbGlrZSBAIGFuZCAjXG4gICAqICBAcGFyYW0ge1N0cmluZ1tdfSBbY2ZnLm9wZXJhdG9yXSBBZGRpdGlvbmFsIG9wZXJhdG9ycyB0byByZWNvZ25pemVcbiAgICovXG4gIGNvbnN0cnVjdG9yKGNmZykge1xuXG4gICAgY2ZnID0gb2JqZWN0TWVyZ2Uoe1xuICAgICAgc3RyaW5nVHlwZXM6IFtgXCJcImAsIFwiJydcIixcIm15YmF0aXNcIl0sXG4gICAgICBvcGVuUGFyZW5zOiBbJygnLCAnQ0FTRSddLFxuICAgICAgY2xvc2VQYXJlbnM6IFsnKScsICdFTkQnXSxcbiAgICAgIGluZGV4ZWRQbGFjZWhvbGRlclR5cGVzOiBbJz8nXSxcbiAgICAgIG5hbWVkUGxhY2Vob2xkZXJUeXBlczogW10sXG4gICAgICBsaW5lQ29tbWVudFR5cGVzOiBbJy0tJ10sXG4gICAgfSAsIGNmZyk7XG5cbiAgICB0aGlzLldISVRFU1BBQ0VfUkVHRVggPSAvXihcXHMrKS91O1xuICAgIHRoaXMuTlVNQkVSX1JFR0VYID0gL14oKC1cXHMqKT9bMC05XSsoXFwuWzAtOV0rKT8oW2VFXS0/WzAtOV0rKFxcLlswLTldKyk/KT98MHhbMC05YS1mQS1GXSt8MGJbMDFdKylcXGIvdTtcblxuICAgIHRoaXMuT1BFUkFUT1JfUkVHRVggPSByZWdleEZhY3RvcnkuY3JlYXRlT3BlcmF0b3JSZWdleChbXG4gICAgICAnPD4nLFxuICAgICAgJzw9JyxcbiAgICAgICc+PScsXG4gICAgICAuLi4oY2ZnLm9wZXJhdG9ycyB8fCBbXSksXG4gICAgXSk7XG5cbiAgICB0aGlzLkJMT0NLX0NPTU1FTlRfUkVHRVggPSAvXihcXC9cXCpbXl0qPyg/OlxcKlxcL3wkKSkvdTtcbiAgICB0aGlzLkxJTkVfQ09NTUVOVF9SRUdFWCA9IHJlZ2V4RmFjdG9yeS5jcmVhdGVMaW5lQ29tbWVudFJlZ2V4KGNmZy5saW5lQ29tbWVudFR5cGVzKTtcblxuICAgIHRoaXMuUkVTRVJWRURfVE9QX0xFVkVMX1JFR0VYID0gcmVnZXhGYWN0b3J5LmNyZWF0ZVJlc2VydmVkV29yZFJlZ2V4KGNmZy5yZXNlcnZlZFRvcExldmVsV29yZHMpO1xuICAgIHRoaXMuUkVTRVJWRURfVE9QX0xFVkVMX05PX0lOREVOVF9SRUdFWCA9IHJlZ2V4RmFjdG9yeS5jcmVhdGVSZXNlcnZlZFdvcmRSZWdleChcbiAgICAgIGNmZy5yZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudFxuICAgICk7XG4gICAgdGhpcy5SRVNFUlZFRF9ORVdMSU5FX1JFR0VYID0gcmVnZXhGYWN0b3J5LmNyZWF0ZVJlc2VydmVkV29yZFJlZ2V4KGNmZy5yZXNlcnZlZE5ld2xpbmVXb3Jkcyk7XG4gICAgdGhpcy5SRVNFUlZFRF9QTEFJTl9SRUdFWCA9IHJlZ2V4RmFjdG9yeS5jcmVhdGVSZXNlcnZlZFdvcmRSZWdleChjZmcucmVzZXJ2ZWRXb3Jkcyk7XG5cbiAgICB0aGlzLldPUkRfUkVHRVggPSByZWdleEZhY3RvcnkuY3JlYXRlV29yZFJlZ2V4KGNmZy5zcGVjaWFsV29yZENoYXJzKTtcbiAgICB0aGlzLlNUUklOR19SRUdFWCA9IHJlZ2V4RmFjdG9yeS5jcmVhdGVTdHJpbmdSZWdleChjZmcuc3RyaW5nVHlwZXMpO1xuXG4gICAgdGhpcy5PUEVOX1BBUkVOX1JFR0VYID0gcmVnZXhGYWN0b3J5LmNyZWF0ZVBhcmVuUmVnZXgoY2ZnLm9wZW5QYXJlbnMpO1xuICAgIHRoaXMuQ0xPU0VfUEFSRU5fUkVHRVggPSByZWdleEZhY3RvcnkuY3JlYXRlUGFyZW5SZWdleChjZmcuY2xvc2VQYXJlbnMpO1xuXG4gICAgdGhpcy5JTkRFWEVEX1BMQUNFSE9MREVSX1JFR0VYID0gcmVnZXhGYWN0b3J5LmNyZWF0ZVBsYWNlaG9sZGVyUmVnZXgoXG4gICAgICBjZmcuaW5kZXhlZFBsYWNlaG9sZGVyVHlwZXMsXG4gICAgICAnWzAtOV0qJ1xuICAgICk7XG4gICAgdGhpcy5JREVOVF9OQU1FRF9QTEFDRUhPTERFUl9SRUdFWCA9IHJlZ2V4RmFjdG9yeS5jcmVhdGVQbGFjZWhvbGRlclJlZ2V4KFxuICAgICAgY2ZnLm5hbWVkUGxhY2Vob2xkZXJUeXBlcyxcbiAgICAgICdbYS16QS1aMC05Ll8kXSsnXG4gICAgKTtcbiAgICB0aGlzLlNUUklOR19OQU1FRF9QTEFDRUhPTERFUl9SRUdFWCA9IHJlZ2V4RmFjdG9yeS5jcmVhdGVQbGFjZWhvbGRlclJlZ2V4KFxuICAgICAgY2ZnLm5hbWVkUGxhY2Vob2xkZXJUeXBlcyxcbiAgICAgIHJlZ2V4RmFjdG9yeS5jcmVhdGVTdHJpbmdQYXR0ZXJuKGNmZy5zdHJpbmdUeXBlcylcbiAgICApO1xuICB9XG5cbiAgLyoqXG4gICAqIFRha2VzIGEgU1FMIHN0cmluZyBhbmQgYnJlYWtzIGl0IGludG8gdG9rZW5zLlxuICAgKiBFYWNoIHRva2VuIGlzIGFuIG9iamVjdCB3aXRoIHR5cGUgYW5kIHZhbHVlLlxuICAgKlxuICAgKiBAcGFyYW0ge1N0cmluZ30gaW5wdXQgVGhlIFNRTCBzdHJpbmdcbiAgICogQHJldHVybiB7T2JqZWN0W119IHRva2VucyBBbiBhcnJheSBvZiB0b2tlbnMuXG4gICAqICBAcmV0dXJuIHtTdHJpbmd9IHRva2VuLnR5cGVcbiAgICogIEByZXR1cm4ge1N0cmluZ30gdG9rZW4udmFsdWVcbiAgICogIEByZXR1cm4ge1N0cmluZ30gdG9rZW4ud2hpdGVzcGFjZUJlZm9yZSBQcmVjZWRpbmcgd2hpdGVzcGFjZVxuICAgKi9cbiAgdG9rZW5pemUoaW5wdXQpIHtcbiAgICBjb25zdCB0b2tlbnMgPSBbXTtcbiAgICBsZXQgdG9rZW47XG5cbiAgICAvLyBLZWVwIHByb2Nlc3NpbmcgdGhlIHN0cmluZyB1bnRpbCBpdCBpcyBlbXB0eVxuICAgIHdoaWxlIChpbnB1dC5sZW5ndGgpIHtcbiAgICAgIC8vIGdyYWIgYW55IHByZWNlZGluZyB3aGl0ZXNwYWNlXG4gICAgICBjb25zdCB3aGl0ZXNwYWNlQmVmb3JlID0gdGhpcy5nZXRXaGl0ZXNwYWNlKGlucHV0KTtcbiAgICAgIGlucHV0ID0gaW5wdXQuc3Vic3RyaW5nKHdoaXRlc3BhY2VCZWZvcmUubGVuZ3RoKTtcblxuICAgICAgaWYgKGlucHV0Lmxlbmd0aCkge1xuICAgICAgICAvLyBHZXQgdGhlIG5leHQgdG9rZW4gYW5kIHRoZSB0b2tlbiB0eXBlXG4gICAgICAgIHRva2VuID0gdGhpcy5nZXROZXh0VG9rZW4oaW5wdXQsIHRva2VuKTtcbiAgICAgICAgLy8gQWR2YW5jZSB0aGUgc3RyaW5nXG4gICAgICAgIGlucHV0ID0gaW5wdXQuc3Vic3RyaW5nKHRva2VuLnZhbHVlLmxlbmd0aCk7XG5cbiAgICAgICAgdG9rZW5zLnB1c2goeyAuLi50b2tlbiwgd2hpdGVzcGFjZUJlZm9yZSB9KTtcbiAgICAgIH1cbiAgICB9XG4gICAgcmV0dXJuIHRva2VucztcbiAgfVxuXG4gIGdldFdoaXRlc3BhY2UoaW5wdXQpIHtcbiAgICBjb25zdCBtYXRjaGVzID0gaW5wdXQubWF0Y2godGhpcy5XSElURVNQQUNFX1JFR0VYKTtcbiAgICByZXR1cm4gbWF0Y2hlcyA/IG1hdGNoZXNbMV0gOiAnJztcbiAgfVxuXG4gIGdldE5leHRUb2tlbihpbnB1dCwgcHJldmlvdXNUb2tlbikge1xuICAgIHJldHVybiAoXG4gICAgICB0aGlzLmdldENvbW1lbnRUb2tlbihpbnB1dCkgfHxcbiAgICAgIHRoaXMuZ2V0U3RyaW5nVG9rZW4oaW5wdXQpIHx8XG4gICAgICB0aGlzLmdldE9wZW5QYXJlblRva2VuKGlucHV0KSB8fFxuICAgICAgdGhpcy5nZXRDbG9zZVBhcmVuVG9rZW4oaW5wdXQpIHx8XG4gICAgICB0aGlzLmdldFBsYWNlaG9sZGVyVG9rZW4oaW5wdXQpIHx8XG4gICAgICB0aGlzLmdldE51bWJlclRva2VuKGlucHV0KSB8fFxuICAgICAgdGhpcy5nZXRSZXNlcnZlZFdvcmRUb2tlbihpbnB1dCwgcHJldmlvdXNUb2tlbikgfHxcbiAgICAgIHRoaXMuZ2V0V29yZFRva2VuKGlucHV0KSB8fFxuICAgICAgdGhpcy5nZXRPcGVyYXRvclRva2VuKGlucHV0KVxuICAgICk7XG4gIH1cblxuICBnZXRDb21tZW50VG9rZW4oaW5wdXQpIHtcbiAgICByZXR1cm4gdGhpcy5nZXRMaW5lQ29tbWVudFRva2VuKGlucHV0KSB8fCB0aGlzLmdldEJsb2NrQ29tbWVudFRva2VuKGlucHV0KTtcbiAgfVxuXG4gIGdldExpbmVDb21tZW50VG9rZW4oaW5wdXQpIHtcbiAgICByZXR1cm4gdGhpcy5nZXRUb2tlbk9uRmlyc3RNYXRjaCh7XG4gICAgICBpbnB1dCxcbiAgICAgIHR5cGU6IHRva2VuVHlwZXMuTElORV9DT01NRU5ULFxuICAgICAgcmVnZXg6IHRoaXMuTElORV9DT01NRU5UX1JFR0VYLFxuICAgIH0pO1xuICB9XG5cbiAgZ2V0QmxvY2tDb21tZW50VG9rZW4oaW5wdXQpIHtcbiAgICByZXR1cm4gdGhpcy5nZXRUb2tlbk9uRmlyc3RNYXRjaCh7XG4gICAgICBpbnB1dCxcbiAgICAgIHR5cGU6IHRva2VuVHlwZXMuQkxPQ0tfQ09NTUVOVCxcbiAgICAgIHJlZ2V4OiB0aGlzLkJMT0NLX0NPTU1FTlRfUkVHRVgsXG4gICAgfSk7XG4gIH1cblxuICBnZXRTdHJpbmdUb2tlbihpbnB1dCkge1xuICAgIHJldHVybiB0aGlzLmdldFRva2VuT25GaXJzdE1hdGNoKHtcbiAgICAgIGlucHV0LFxuICAgICAgdHlwZTogdG9rZW5UeXBlcy5TVFJJTkcsXG4gICAgICByZWdleDogdGhpcy5TVFJJTkdfUkVHRVgsXG4gICAgfSk7XG4gIH1cblxuICBnZXRPcGVuUGFyZW5Ub2tlbihpbnB1dCkge1xuICAgIHJldHVybiB0aGlzLmdldFRva2VuT25GaXJzdE1hdGNoKHtcbiAgICAgIGlucHV0LFxuICAgICAgdHlwZTogdG9rZW5UeXBlcy5PUEVOX1BBUkVOLFxuICAgICAgcmVnZXg6IHRoaXMuT1BFTl9QQVJFTl9SRUdFWCxcbiAgICB9KTtcbiAgfVxuXG4gIGdldENsb3NlUGFyZW5Ub2tlbihpbnB1dCkge1xuICAgIHJldHVybiB0aGlzLmdldFRva2VuT25GaXJzdE1hdGNoKHtcbiAgICAgIGlucHV0LFxuICAgICAgdHlwZTogdG9rZW5UeXBlcy5DTE9TRV9QQVJFTixcbiAgICAgIHJlZ2V4OiB0aGlzLkNMT1NFX1BBUkVOX1JFR0VYLFxuICAgIH0pO1xuICB9XG5cbiAgZ2V0UGxhY2Vob2xkZXJUb2tlbihpbnB1dCkge1xuICAgIHJldHVybiAoXG4gICAgICB0aGlzLmdldElkZW50TmFtZWRQbGFjZWhvbGRlclRva2VuKGlucHV0KSB8fFxuICAgICAgdGhpcy5nZXRTdHJpbmdOYW1lZFBsYWNlaG9sZGVyVG9rZW4oaW5wdXQpIHx8XG4gICAgICB0aGlzLmdldEluZGV4ZWRQbGFjZWhvbGRlclRva2VuKGlucHV0KVxuICAgICk7XG4gIH1cblxuICBnZXRJZGVudE5hbWVkUGxhY2Vob2xkZXJUb2tlbihpbnB1dCkge1xuICAgIHJldHVybiB0aGlzLmdldFBsYWNlaG9sZGVyVG9rZW5XaXRoS2V5KHtcbiAgICAgIGlucHV0LFxuICAgICAgcmVnZXg6IHRoaXMuSURFTlRfTkFNRURfUExBQ0VIT0xERVJfUkVHRVgsXG4gICAgICBwYXJzZUtleTogKHYpID0+IHYuc2xpY2UoMSksXG4gICAgfSk7XG4gIH1cblxuICBnZXRTdHJpbmdOYW1lZFBsYWNlaG9sZGVyVG9rZW4oaW5wdXQpIHtcbiAgICByZXR1cm4gdGhpcy5nZXRQbGFjZWhvbGRlclRva2VuV2l0aEtleSh7XG4gICAgICBpbnB1dCxcbiAgICAgIHJlZ2V4OiB0aGlzLlNUUklOR19OQU1FRF9QTEFDRUhPTERFUl9SRUdFWCxcbiAgICAgIHBhcnNlS2V5OiAodikgPT5cbiAgICAgICAgdGhpcy5nZXRFc2NhcGVkUGxhY2Vob2xkZXJLZXkoeyBrZXk6IHYuc2xpY2UoMiwgLTEpLCBxdW90ZUNoYXI6IHYuc2xpY2UoLTEpIH0pLFxuICAgIH0pO1xuICB9XG5cbiAgZ2V0SW5kZXhlZFBsYWNlaG9sZGVyVG9rZW4oaW5wdXQpIHtcbiAgICByZXR1cm4gdGhpcy5nZXRQbGFjZWhvbGRlclRva2VuV2l0aEtleSh7XG4gICAgICBpbnB1dCxcbiAgICAgIHJlZ2V4OiB0aGlzLklOREVYRURfUExBQ0VIT0xERVJfUkVHRVgsXG4gICAgICBwYXJzZUtleTogKHYpID0+IHYuc2xpY2UoMSksXG4gICAgfSk7XG4gIH1cblxuICBnZXRQbGFjZWhvbGRlclRva2VuV2l0aEtleSh7IGlucHV0LCByZWdleCwgcGFyc2VLZXkgfSkge1xuICAgIGNvbnN0IHRva2VuID0gdGhpcy5nZXRUb2tlbk9uRmlyc3RNYXRjaCh7IGlucHV0LCByZWdleCwgdHlwZTogdG9rZW5UeXBlcy5QTEFDRUhPTERFUiB9KTtcbiAgICBpZiAodG9rZW4pIHtcbiAgICAgIHRva2VuLmtleSA9IHBhcnNlS2V5KHRva2VuLnZhbHVlKTtcbiAgICB9XG4gICAgcmV0dXJuIHRva2VuO1xuICB9XG5cbiAgZ2V0RXNjYXBlZFBsYWNlaG9sZGVyS2V5KHsga2V5LCBxdW90ZUNoYXIgfSkge1xuICAgIHJldHVybiBrZXkucmVwbGFjZShuZXcgUmVnRXhwKGVzY2FwZVJlZ0V4cCgnXFxcXCcgKyBxdW90ZUNoYXIpLCAnZ3UnKSwgcXVvdGVDaGFyKTtcbiAgfVxuXG4gIC8vIERlY2ltYWwsIGJpbmFyeSwgb3IgaGV4IG51bWJlcnNcbiAgZ2V0TnVtYmVyVG9rZW4oaW5wdXQpIHtcbiAgICByZXR1cm4gdGhpcy5nZXRUb2tlbk9uRmlyc3RNYXRjaCh7XG4gICAgICBpbnB1dCxcbiAgICAgIHR5cGU6IHRva2VuVHlwZXMuTlVNQkVSLFxuICAgICAgcmVnZXg6IHRoaXMuTlVNQkVSX1JFR0VYLFxuICAgIH0pO1xuICB9XG5cbiAgLy8gUHVuY3R1YXRpb24gYW5kIHN5bWJvbHNcbiAgZ2V0T3BlcmF0b3JUb2tlbihpbnB1dCkge1xuICAgIHJldHVybiB0aGlzLmdldFRva2VuT25GaXJzdE1hdGNoKHtcbiAgICAgIGlucHV0LFxuICAgICAgdHlwZTogdG9rZW5UeXBlcy5PUEVSQVRPUixcbiAgICAgIHJlZ2V4OiB0aGlzLk9QRVJBVE9SX1JFR0VYLFxuICAgIH0pO1xuICB9XG5cbiAgZ2V0UmVzZXJ2ZWRXb3JkVG9rZW4oaW5wdXQsIHByZXZpb3VzVG9rZW4pIHtcbiAgICAvLyBBIHJlc2VydmVkIHdvcmQgY2Fubm90IGJlIHByZWNlZGVkIGJ5IGEgXCIuXCJcbiAgICAvLyB0aGlzIG1ha2VzIGl0IHNvIGluIFwibXl0YWJsZS5mcm9tXCIsIFwiZnJvbVwiIGlzIG5vdCBjb25zaWRlcmVkIGEgcmVzZXJ2ZWQgd29yZFxuICAgIGlmIChwcmV2aW91c1Rva2VuICYmIHByZXZpb3VzVG9rZW4udmFsdWUgJiYgcHJldmlvdXNUb2tlbi52YWx1ZSA9PT0gJy4nKSB7XG4gICAgICByZXR1cm4gdW5kZWZpbmVkO1xuICAgIH1cbiAgICByZXR1cm4gKFxuICAgICAgdGhpcy5nZXRUb3BMZXZlbFJlc2VydmVkVG9rZW4oaW5wdXQpIHx8XG4gICAgICB0aGlzLmdldE5ld2xpbmVSZXNlcnZlZFRva2VuKGlucHV0KSB8fFxuICAgICAgdGhpcy5nZXRUb3BMZXZlbFJlc2VydmVkVG9rZW5Ob0luZGVudChpbnB1dCkgfHxcbiAgICAgIHRoaXMuZ2V0UGxhaW5SZXNlcnZlZFRva2VuKGlucHV0KVxuICAgICk7XG4gIH1cblxuICBnZXRUb3BMZXZlbFJlc2VydmVkVG9rZW4oaW5wdXQpIHtcbiAgICByZXR1cm4gdGhpcy5nZXRUb2tlbk9uRmlyc3RNYXRjaCh7XG4gICAgICBpbnB1dCxcbiAgICAgIHR5cGU6IHRva2VuVHlwZXMuUkVTRVJWRURfVE9QX0xFVkVMLFxuICAgICAgcmVnZXg6IHRoaXMuUkVTRVJWRURfVE9QX0xFVkVMX1JFR0VYLFxuICAgIH0pO1xuICB9XG5cbiAgZ2V0TmV3bGluZVJlc2VydmVkVG9rZW4oaW5wdXQpIHtcbiAgICByZXR1cm4gdGhpcy5nZXRUb2tlbk9uRmlyc3RNYXRjaCh7XG4gICAgICBpbnB1dCxcbiAgICAgIHR5cGU6IHRva2VuVHlwZXMuUkVTRVJWRURfTkVXTElORSxcbiAgICAgIHJlZ2V4OiB0aGlzLlJFU0VSVkVEX05FV0xJTkVfUkVHRVgsXG4gICAgfSk7XG4gIH1cblxuICBnZXRUb3BMZXZlbFJlc2VydmVkVG9rZW5Ob0luZGVudChpbnB1dCkge1xuICAgIHJldHVybiB0aGlzLmdldFRva2VuT25GaXJzdE1hdGNoKHtcbiAgICAgIGlucHV0LFxuICAgICAgdHlwZTogdG9rZW5UeXBlcy5SRVNFUlZFRF9UT1BfTEVWRUxfTk9fSU5ERU5ULFxuICAgICAgcmVnZXg6IHRoaXMuUkVTRVJWRURfVE9QX0xFVkVMX05PX0lOREVOVF9SRUdFWCxcbiAgICB9KTtcbiAgfVxuXG4gIGdldFBsYWluUmVzZXJ2ZWRUb2tlbihpbnB1dCkge1xuICAgIHJldHVybiB0aGlzLmdldFRva2VuT25GaXJzdE1hdGNoKHtcbiAgICAgIGlucHV0LFxuICAgICAgdHlwZTogdG9rZW5UeXBlcy5SRVNFUlZFRCxcbiAgICAgIHJlZ2V4OiB0aGlzLlJFU0VSVkVEX1BMQUlOX1JFR0VYLFxuICAgIH0pO1xuICB9XG5cbiAgZ2V0V29yZFRva2VuKGlucHV0KSB7XG4gICAgcmV0dXJuIHRoaXMuZ2V0VG9rZW5PbkZpcnN0TWF0Y2goe1xuICAgICAgaW5wdXQsXG4gICAgICB0eXBlOiB0b2tlblR5cGVzLldPUkQsXG4gICAgICByZWdleDogdGhpcy5XT1JEX1JFR0VYLFxuICAgIH0pO1xuICB9XG5cbiAgZ2V0VG9rZW5PbkZpcnN0TWF0Y2goeyBpbnB1dCwgdHlwZSwgcmVnZXggfSkge1xuICAgIGNvbnN0IG1hdGNoZXMgPSBpbnB1dC5tYXRjaChyZWdleCk7XG5cbiAgICByZXR1cm4gbWF0Y2hlcyA/IHsgdHlwZSwgdmFsdWU6IG1hdGNoZXNbMV0gfSA6IHVuZGVmaW5lZDtcbiAgfVxufVxuIiwiaW1wb3J0IHsgZXNjYXBlUmVnRXhwLCBpc0VtcHR5LCBzb3J0QnlMZW5ndGhEZXNjIH0gZnJvbSAnLi4vdXRpbHMnO1xuXG5leHBvcnQgZnVuY3Rpb24gY3JlYXRlT3BlcmF0b3JSZWdleChtdWx0aUxldHRlck9wZXJhdG9ycykge1xuICByZXR1cm4gbmV3IFJlZ0V4cChcbiAgICBgXigke3NvcnRCeUxlbmd0aERlc2MobXVsdGlMZXR0ZXJPcGVyYXRvcnMpLm1hcChlc2NhcGVSZWdFeHApLmpvaW4oJ3wnKX18LilgLFxuICAgICd1J1xuICApO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gY3JlYXRlTGluZUNvbW1lbnRSZWdleChsaW5lQ29tbWVudFR5cGVzKSB7XG4gIHJldHVybiBuZXcgUmVnRXhwKFxuICAgIGBeKCg/OiR7bGluZUNvbW1lbnRUeXBlcy5tYXAoKGMpID0+IGVzY2FwZVJlZ0V4cChjKSkuam9pbignfCcpfSkuKj8pKD86XFxyXFxufFxccnxcXG58JClgLFxuICAgICd1J1xuICApO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gY3JlYXRlUmVzZXJ2ZWRXb3JkUmVnZXgocmVzZXJ2ZWRXb3Jkcykge1xuICBpZiAocmVzZXJ2ZWRXb3Jkcy5sZW5ndGggPT09IDApIHtcbiAgICByZXR1cm4gbmV3IFJlZ0V4cChgXlxcYiRgLCAndScpO1xuICB9XG4gIGNvbnN0IHJlc2VydmVkV29yZHNQYXR0ZXJuID0gc29ydEJ5TGVuZ3RoRGVzYyhyZXNlcnZlZFdvcmRzKS5qb2luKCd8JykucmVwbGFjZSgvIC9ndSwgJ1xcXFxzKycpO1xuICByZXR1cm4gbmV3IFJlZ0V4cChgXigke3Jlc2VydmVkV29yZHNQYXR0ZXJufSlcXFxcYmAsICdpdScpO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gY3JlYXRlV29yZFJlZ2V4KHNwZWNpYWxDaGFycyA9IFtdKSB7XG4gIHJldHVybiBuZXcgUmVnRXhwKFxuICAgIGBeKFtcXFxccHtBbHBoYWJldGljfVxcXFxwe01hcmt9XFxcXHB7RGVjaW1hbF9OdW1iZXJ9XFxcXHB7Q29ubmVjdG9yX1B1bmN0dWF0aW9ufVxcXFxwe0pvaW5fQ29udHJvbH0ke3NwZWNpYWxDaGFycy5qb2luKFxuICAgICAgJydcbiAgICApfV0rKWAsXG4gICAgJ3UnXG4gICk7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBjcmVhdGVTdHJpbmdSZWdleChzdHJpbmdUeXBlcykge1xuICByZXR1cm4gbmV3IFJlZ0V4cCgnXignICsgY3JlYXRlU3RyaW5nUGF0dGVybihzdHJpbmdUeXBlcykgKyAnKScsICd1Jyk7XG59XG5cbi8vIFRoaXMgZW5hYmxlcyB0aGUgZm9sbG93aW5nIHN0cmluZyBwYXR0ZXJuczpcbi8vIDEuIGJhY2t0aWNrIHF1b3RlZCBzdHJpbmcgdXNpbmcgYGAgdG8gZXNjYXBlXG4vLyAyLiBzcXVhcmUgYnJhY2tldCBxdW90ZWQgc3RyaW5nIChTUUwgU2VydmVyKSB1c2luZyBdXSB0byBlc2NhcGVcbi8vIDMuIGRvdWJsZSBxdW90ZWQgc3RyaW5nIHVzaW5nIFwiXCIgb3IgXFxcIiB0byBlc2NhcGVcbi8vIDQuIHNpbmdsZSBxdW90ZWQgc3RyaW5nIHVzaW5nICcnIG9yIFxcJyB0byBlc2NhcGVcbi8vIDUuIG5hdGlvbmFsIGNoYXJhY3RlciBxdW90ZWQgc3RyaW5nIHVzaW5nIE4nJyBvciBOXFwnIHRvIGVzY2FwZVxuLy8gNi4gVW5pY29kZSBzaW5nbGUtcXVvdGVkIHN0cmluZyB1c2luZyBcXCcgdG8gZXNjYXBlXG4vLyA3LiBVbmljb2RlIGRvdWJsZS1xdW90ZWQgc3RyaW5nIHVzaW5nIFxcXCIgdG8gZXNjYXBlXG4vLyA4LiBQb3N0Z3JlU1FMIGRvbGxhci1xdW90ZWQgc3RyaW5nc1xuLy8gOS4gbXliYXRpcyBwYXJhbWV0ZXIgc3RyaW5nXG5leHBvcnQgZnVuY3Rpb24gY3JlYXRlU3RyaW5nUGF0dGVybihzdHJpbmdUeXBlcykge1xuICBjb25zdCBwYXR0ZXJucyA9IHtcbiAgICAnYGAnOiAnKChgW15gXSooJHxgKSkrKScsXG4gICAgJ3t9JzogJygoXFxcXHtbXlxcXFx9XSooJHxcXFxcfSkpKyknLFxuICAgICdbXSc6ICcoKFxcXFxbW15cXFxcXV0qKCR8XFxcXF0pKShcXFxcXVteXFxcXF1dKigkfFxcXFxdKSkqKScsXG4gICAgJ1wiXCInOiAnKChcIlteXCJcXFxcXFxcXF0qKD86XFxcXFxcXFwuW15cIlxcXFxcXFxcXSopKihcInwkKSkrKScsXG4gICAgXCInJ1wiOiBcIigoJ1teJ1xcXFxcXFxcXSooPzpcXFxcXFxcXC5bXidcXFxcXFxcXF0qKSooJ3wkKSkrKVwiLFxuICAgIFwiTicnXCI6IFwiKChOJ1teJ1xcXFxcXFxcXSooPzpcXFxcXFxcXC5bXidcXFxcXFxcXF0qKSooJ3wkKSkrKVwiLFxuICAgIFwiVSYnJ1wiOiBcIigoVSYnW14nXFxcXFxcXFxdKig/OlxcXFxcXFxcLlteJ1xcXFxcXFxcXSopKignfCQpKSspXCIsXG4gICAgJ1UmXCJcIic6ICcoKFUmXCJbXlwiXFxcXFxcXFxdKig/OlxcXFxcXFxcLlteXCJcXFxcXFxcXF0qKSooXCJ8JCkpKyknLFxuICAgICQkOiAnKCg/PHRhZz5cXFxcJFxcXFx3KlxcXFwkKVtcXFxcc1xcXFxTXSo/KD86XFxcXGs8dGFnPnwkKSknLFxuICAgICdteWJhdGlzJzogJ1sjfCRdXFxcXHsuKz9cXFxcfScsICAvLyBhZGQgbXliYXRpcyBwYXJhbWV0ZXIgXG4gIH07XG5cbiAgcmV0dXJuIHN0cmluZ1R5cGVzLm1hcCgodCkgPT4gcGF0dGVybnNbdF0pLmpvaW4oJ3wnKTtcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIGNyZWF0ZVBhcmVuUmVnZXgocGFyZW5zKSB7XG4gIHJldHVybiBuZXcgUmVnRXhwKCdeKCcgKyBwYXJlbnMubWFwKGVzY2FwZVBhcmVuKS5qb2luKCd8JykgKyAnKScsICdpdScpO1xufVxuXG5mdW5jdGlvbiBlc2NhcGVQYXJlbihwYXJlbikge1xuICBpZiAocGFyZW4ubGVuZ3RoID09PSAxKSB7XG4gICAgLy8gQSBzaW5nbGUgcHVuY3R1YXRpb24gY2hhcmFjdGVyXG4gICAgcmV0dXJuIGVzY2FwZVJlZ0V4cChwYXJlbik7XG4gIH0gZWxzZSB7XG4gICAgLy8gbG9uZ2VyIHdvcmRcbiAgICByZXR1cm4gJ1xcXFxiJyArIHBhcmVuICsgJ1xcXFxiJztcbiAgfVxufVxuXG5leHBvcnQgZnVuY3Rpb24gY3JlYXRlUGxhY2Vob2xkZXJSZWdleCh0eXBlcywgcGF0dGVybikge1xuICBpZiAoaXNFbXB0eSh0eXBlcykpIHtcbiAgICByZXR1cm4gZmFsc2U7XG4gIH1cbiAgY29uc3QgdHlwZXNSZWdleCA9IHR5cGVzLm1hcChlc2NhcGVSZWdFeHApLmpvaW4oJ3wnKTtcblxuICByZXR1cm4gbmV3IFJlZ0V4cChgXigoPzoke3R5cGVzUmVnZXh9KSg/OiR7cGF0dGVybn0pKWAsICd1Jyk7XG59XG4iLCJpbXBvcnQgdG9rZW5UeXBlcyBmcm9tICcuL3Rva2VuVHlwZXMnO1xuXG5jb25zdCBpc1Rva2VuID0gKHR5cGUsIHJlZ2V4KSA9PiAodG9rZW4pID0+IHRva2VuPy50eXBlID09PSB0eXBlICYmIHJlZ2V4LnRlc3QodG9rZW4/LnZhbHVlKTtcblxuZXhwb3J0IGNvbnN0IGlzQW5kID0gaXNUb2tlbih0b2tlblR5cGVzLlJFU0VSVkVEX05FV0xJTkUsIC9eQU5EJC9pdSk7XG5cbmV4cG9ydCBjb25zdCBpc0JldHdlZW4gPSBpc1Rva2VuKHRva2VuVHlwZXMuUkVTRVJWRUQsIC9eQkVUV0VFTiQvaXUpO1xuXG5leHBvcnQgY29uc3QgaXNMaW1pdCA9IGlzVG9rZW4odG9rZW5UeXBlcy5SRVNFUlZFRF9UT1BfTEVWRUwsIC9eTElNSVQkL2l1KTtcblxuZXhwb3J0IGNvbnN0IGlzU2V0ID0gaXNUb2tlbih0b2tlblR5cGVzLlJFU0VSVkVEX1RPUF9MRVZFTCwgL15TRVQkL2l1KTtcblxuZXhwb3J0IGNvbnN0IGlzQnkgPSBpc1Rva2VuKHRva2VuVHlwZXMuUkVTRVJWRUQsIC9eQlkkL2l1KTtcblxuZXhwb3J0IGNvbnN0IGlzV2luZG93ID0gaXNUb2tlbih0b2tlblR5cGVzLlJFU0VSVkVEX1RPUF9MRVZFTCwgL15XSU5ET1ckL2l1KTtcblxuZXhwb3J0IGNvbnN0IGlzRW5kID0gaXNUb2tlbih0b2tlblR5cGVzLkNMT1NFX1BBUkVOLCAvXkVORCQvaXUpO1xuIiwiLyoqXG4gKiBDb25zdGFudHMgZm9yIHRva2VuIHR5cGVzXG4gKi9cbmV4cG9ydCBkZWZhdWx0IHtcbiAgV09SRDogJ3dvcmQnLFxuICBTVFJJTkc6ICdzdHJpbmcnLFxuICBSRVNFUlZFRDogJ3Jlc2VydmVkJyxcbiAgUkVTRVJWRURfVE9QX0xFVkVMOiAncmVzZXJ2ZWQtdG9wLWxldmVsJyxcbiAgUkVTRVJWRURfVE9QX0xFVkVMX05PX0lOREVOVDogJ3Jlc2VydmVkLXRvcC1sZXZlbC1uby1pbmRlbnQnLFxuICBSRVNFUlZFRF9ORVdMSU5FOiAncmVzZXJ2ZWQtbmV3bGluZScsXG4gIE9QRVJBVE9SOiAnb3BlcmF0b3InLFxuICBPUEVOX1BBUkVOOiAnb3Blbi1wYXJlbicsXG4gIENMT1NFX1BBUkVOOiAnY2xvc2UtcGFyZW4nLFxuICBMSU5FX0NPTU1FTlQ6ICdsaW5lLWNvbW1lbnQnLFxuICBCTE9DS19DT01NRU5UOiAnYmxvY2stY29tbWVudCcsXG4gIE5VTUJFUjogJ251bWJlcicsXG4gIFBMQUNFSE9MREVSOiAncGxhY2Vob2xkZXInLFxufTtcbiIsImltcG9ydCBGb3JtYXR0ZXIgZnJvbSAnLi4vY29yZS9Gb3JtYXR0ZXInO1xuaW1wb3J0IFRva2VuaXplciBmcm9tICcuLi9jb3JlL1Rva2VuaXplcic7XG5cbmNvbnN0IHJlc2VydmVkV29yZHMgPSBbXG4gICdBQlMnLFxuICAnQUNUSVZBVEUnLFxuICAnQUxJQVMnLFxuICAnQUxMJyxcbiAgJ0FMTE9DQVRFJyxcbiAgJ0FMTE9XJyxcbiAgJ0FMVEVSJyxcbiAgJ0FOWScsXG4gICdBUkUnLFxuICAnQVJSQVknLFxuICAnQVMnLFxuICAnQVNDJyxcbiAgJ0FTRU5TSVRJVkUnLFxuICAnQVNTT0NJQVRFJyxcbiAgJ0FTVVRJTUUnLFxuICAnQVNZTU1FVFJJQycsXG4gICdBVCcsXG4gICdBVE9NSUMnLFxuICAnQVRUUklCVVRFUycsXG4gICdBVURJVCcsXG4gICdBVVRIT1JJWkFUSU9OJyxcbiAgJ0FVWCcsXG4gICdBVVhJTElBUlknLFxuICAnQVZHJyxcbiAgJ0JFRk9SRScsXG4gICdCRUdJTicsXG4gICdCRVRXRUVOJyxcbiAgJ0JJR0lOVCcsXG4gICdCSU5BUlknLFxuICAnQkxPQicsXG4gICdCT09MRUFOJyxcbiAgJ0JPVEgnLFxuICAnQlVGRkVSUE9PTCcsXG4gICdCWScsXG4gICdDQUNIRScsXG4gICdDQUxMJyxcbiAgJ0NBTExFRCcsXG4gICdDQVBUVVJFJyxcbiAgJ0NBUkRJTkFMSVRZJyxcbiAgJ0NBU0NBREVEJyxcbiAgJ0NBU0UnLFxuICAnQ0FTVCcsXG4gICdDQ1NJRCcsXG4gICdDRUlMJyxcbiAgJ0NFSUxJTkcnLFxuICAnQ0hBUicsXG4gICdDSEFSQUNURVInLFxuICAnQ0hBUkFDVEVSX0xFTkdUSCcsXG4gICdDSEFSX0xFTkdUSCcsXG4gICdDSEVDSycsXG4gICdDTE9CJyxcbiAgJ0NMT05FJyxcbiAgJ0NMT1NFJyxcbiAgJ0NMVVNURVInLFxuICAnQ09BTEVTQ0UnLFxuICAnQ09MTEFURScsXG4gICdDT0xMRUNUJyxcbiAgJ0NPTExFQ1RJT04nLFxuICAnQ09MTElEJyxcbiAgJ0NPTFVNTicsXG4gICdDT01NRU5UJyxcbiAgJ0NPTU1JVCcsXG4gICdDT05DQVQnLFxuICAnQ09ORElUSU9OJyxcbiAgJ0NPTk5FQ1QnLFxuICAnQ09OTkVDVElPTicsXG4gICdDT05TVFJBSU5UJyxcbiAgJ0NPTlRBSU5TJyxcbiAgJ0NPTlRJTlVFJyxcbiAgJ0NPTlZFUlQnLFxuICAnQ09SUicsXG4gICdDT1JSRVNQT05ESU5HJyxcbiAgJ0NPVU5UJyxcbiAgJ0NPVU5UX0JJRycsXG4gICdDT1ZBUl9QT1AnLFxuICAnQ09WQVJfU0FNUCcsXG4gICdDUkVBVEUnLFxuICAnQ1JPU1MnLFxuICAnQ1VCRScsXG4gICdDVU1FX0RJU1QnLFxuICAnQ1VSUkVOVCcsXG4gICdDVVJSRU5UX0RBVEUnLFxuICAnQ1VSUkVOVF9ERUZBVUxUX1RSQU5TRk9STV9HUk9VUCcsXG4gICdDVVJSRU5UX0xDX0NUWVBFJyxcbiAgJ0NVUlJFTlRfUEFUSCcsXG4gICdDVVJSRU5UX1JPTEUnLFxuICAnQ1VSUkVOVF9TQ0hFTUEnLFxuICAnQ1VSUkVOVF9TRVJWRVInLFxuICAnQ1VSUkVOVF9USU1FJyxcbiAgJ0NVUlJFTlRfVElNRVNUQU1QJyxcbiAgJ0NVUlJFTlRfVElNRVpPTkUnLFxuICAnQ1VSUkVOVF9UUkFOU0ZPUk1fR1JPVVBfRk9SX1RZUEUnLFxuICAnQ1VSUkVOVF9VU0VSJyxcbiAgJ0NVUlNPUicsXG4gICdDWUNMRScsXG4gICdEQVRBJyxcbiAgJ0RBVEFCQVNFJyxcbiAgJ0RBVEFQQVJUSVRJT05OQU1FJyxcbiAgJ0RBVEFQQVJUSVRJT05OVU0nLFxuICAnREFURScsXG4gICdEQVknLFxuICAnREFZUycsXG4gICdEQjJHRU5FUkFMJyxcbiAgJ0RCMkdFTlJMJyxcbiAgJ0RCMlNRTCcsXG4gICdEQklORk8nLFxuICAnREJQQVJUSVRJT05OQU1FJyxcbiAgJ0RCUEFSVElUSU9OTlVNJyxcbiAgJ0RFQUxMT0NBVEUnLFxuICAnREVDJyxcbiAgJ0RFQ0lNQUwnLFxuICAnREVDTEFSRScsXG4gICdERUZBVUxUJyxcbiAgJ0RFRkFVTFRTJyxcbiAgJ0RFRklOSVRJT04nLFxuICAnREVMRVRFJyxcbiAgJ0RFTlNFUkFOSycsXG4gICdERU5TRV9SQU5LJyxcbiAgJ0RFUkVGJyxcbiAgJ0RFU0NSSUJFJyxcbiAgJ0RFU0NSSVBUT1InLFxuICAnREVURVJNSU5JU1RJQycsXG4gICdESUFHTk9TVElDUycsXG4gICdESVNBQkxFJyxcbiAgJ0RJU0FMTE9XJyxcbiAgJ0RJU0NPTk5FQ1QnLFxuICAnRElTVElOQ1QnLFxuICAnRE8nLFxuICAnRE9DVU1FTlQnLFxuICAnRE9VQkxFJyxcbiAgJ0RST1AnLFxuICAnRFNTSVpFJyxcbiAgJ0RZTkFNSUMnLFxuICAnRUFDSCcsXG4gICdFRElUUFJPQycsXG4gICdFTEVNRU5UJyxcbiAgJ0VMU0UnLFxuICAnRUxTRUlGJyxcbiAgJ0VOQUJMRScsXG4gICdFTkNPRElORycsXG4gICdFTkNSWVBUSU9OJyxcbiAgJ0VORCcsXG4gICdFTkQtRVhFQycsXG4gICdFTkRJTkcnLFxuICAnRVJBU0UnLFxuICAnRVNDQVBFJyxcbiAgJ0VWRVJZJyxcbiAgJ0VYQ0VQVElPTicsXG4gICdFWENMVURJTkcnLFxuICAnRVhDTFVTSVZFJyxcbiAgJ0VYRUMnLFxuICAnRVhFQ1VURScsXG4gICdFWElTVFMnLFxuICAnRVhJVCcsXG4gICdFWFAnLFxuICAnRVhQTEFJTicsXG4gICdFWFRFTkRFRCcsXG4gICdFWFRFUk5BTCcsXG4gICdFWFRSQUNUJyxcbiAgJ0ZBTFNFJyxcbiAgJ0ZFTkNFRCcsXG4gICdGRVRDSCcsXG4gICdGSUVMRFBST0MnLFxuICAnRklMRScsXG4gICdGSUxURVInLFxuICAnRklOQUwnLFxuICAnRklSU1QnLFxuICAnRkxPQVQnLFxuICAnRkxPT1InLFxuICAnRk9SJyxcbiAgJ0ZPUkVJR04nLFxuICAnRlJFRScsXG4gICdGVUxMJyxcbiAgJ0ZVTkNUSU9OJyxcbiAgJ0ZVU0lPTicsXG4gICdHRU5FUkFMJyxcbiAgJ0dFTkVSQVRFRCcsXG4gICdHRVQnLFxuICAnR0xPQkFMJyxcbiAgJ0dPVE8nLFxuICAnR1JBTlQnLFxuICAnR1JBUEhJQycsXG4gICdHUk9VUCcsXG4gICdHUk9VUElORycsXG4gICdIQU5ETEVSJyxcbiAgJ0hBU0gnLFxuICAnSEFTSEVEX1ZBTFVFJyxcbiAgJ0hJTlQnLFxuICAnSE9MRCcsXG4gICdIT1VSJyxcbiAgJ0hPVVJTJyxcbiAgJ0lERU5USVRZJyxcbiAgJ0lGJyxcbiAgJ0lNTUVESUFURScsXG4gICdJTicsXG4gICdJTkNMVURJTkcnLFxuICAnSU5DTFVTSVZFJyxcbiAgJ0lOQ1JFTUVOVCcsXG4gICdJTkRFWCcsXG4gICdJTkRJQ0FUT1InLFxuICAnSU5ESUNBVE9SUycsXG4gICdJTkYnLFxuICAnSU5GSU5JVFknLFxuICAnSU5IRVJJVCcsXG4gICdJTk5FUicsXG4gICdJTk9VVCcsXG4gICdJTlNFTlNJVElWRScsXG4gICdJTlNFUlQnLFxuICAnSU5UJyxcbiAgJ0lOVEVHRVInLFxuICAnSU5URUdSSVRZJyxcbiAgJ0lOVEVSU0VDVElPTicsXG4gICdJTlRFUlZBTCcsXG4gICdJTlRPJyxcbiAgJ0lTJyxcbiAgJ0lTT0JJRCcsXG4gICdJU09MQVRJT04nLFxuICAnSVRFUkFURScsXG4gICdKQVInLFxuICAnSkFWQScsXG4gICdLRUVQJyxcbiAgJ0tFWScsXG4gICdMQUJFTCcsXG4gICdMQU5HVUFHRScsXG4gICdMQVJHRScsXG4gICdMQVRFUkFMJyxcbiAgJ0xDX0NUWVBFJyxcbiAgJ0xFQURJTkcnLFxuICAnTEVBVkUnLFxuICAnTEVGVCcsXG4gICdMSUtFJyxcbiAgJ0xJTktUWVBFJyxcbiAgJ0xOJyxcbiAgJ0xPQ0FMJyxcbiAgJ0xPQ0FMREFURScsXG4gICdMT0NBTEUnLFxuICAnTE9DQUxUSU1FJyxcbiAgJ0xPQ0FMVElNRVNUQU1QJyxcbiAgJ0xPQ0FUT1InLFxuICAnTE9DQVRPUlMnLFxuICAnTE9DSycsXG4gICdMT0NLTUFYJyxcbiAgJ0xPQ0tTSVpFJyxcbiAgJ0xPTkcnLFxuICAnTE9PUCcsXG4gICdMT1dFUicsXG4gICdNQUlOVEFJTkVEJyxcbiAgJ01BVENIJyxcbiAgJ01BVEVSSUFMSVpFRCcsXG4gICdNQVgnLFxuICAnTUFYVkFMVUUnLFxuICAnTUVNQkVSJyxcbiAgJ01FUkdFJyxcbiAgJ01FVEhPRCcsXG4gICdNSUNST1NFQ09ORCcsXG4gICdNSUNST1NFQ09ORFMnLFxuICAnTUlOJyxcbiAgJ01JTlVURScsXG4gICdNSU5VVEVTJyxcbiAgJ01JTlZBTFVFJyxcbiAgJ01PRCcsXG4gICdNT0RFJyxcbiAgJ01PRElGSUVTJyxcbiAgJ01PRFVMRScsXG4gICdNT05USCcsXG4gICdNT05USFMnLFxuICAnTVVMVElTRVQnLFxuICAnTkFOJyxcbiAgJ05BVElPTkFMJyxcbiAgJ05BVFVSQUwnLFxuICAnTkNIQVInLFxuICAnTkNMT0InLFxuICAnTkVXJyxcbiAgJ05FV19UQUJMRScsXG4gICdORVhUVkFMJyxcbiAgJ05PJyxcbiAgJ05PQ0FDSEUnLFxuICAnTk9DWUNMRScsXG4gICdOT0RFTkFNRScsXG4gICdOT0RFTlVNQkVSJyxcbiAgJ05PTUFYVkFMVUUnLFxuICAnTk9NSU5WQUxVRScsXG4gICdOT05FJyxcbiAgJ05PT1JERVInLFxuICAnTk9STUFMSVpFJyxcbiAgJ05PUk1BTElaRUQnLFxuICAnTk9UJyxcbiAgJ05VTEwnLFxuICAnTlVMTElGJyxcbiAgJ05VTExTJyxcbiAgJ05VTUVSSUMnLFxuICAnTlVNUEFSVFMnLFxuICAnT0JJRCcsXG4gICdPQ1RFVF9MRU5HVEgnLFxuICAnT0YnLFxuICAnT0ZGU0VUJyxcbiAgJ09MRCcsXG4gICdPTERfVEFCTEUnLFxuICAnT04nLFxuICAnT05MWScsXG4gICdPUEVOJyxcbiAgJ09QVElNSVpBVElPTicsXG4gICdPUFRJTUlaRScsXG4gICdPUFRJT04nLFxuICAnT1JERVInLFxuICAnT1VUJyxcbiAgJ09VVEVSJyxcbiAgJ09WRVInLFxuICAnT1ZFUkxBUFMnLFxuICAnT1ZFUkxBWScsXG4gICdPVkVSUklESU5HJyxcbiAgJ1BBQ0tBR0UnLFxuICAnUEFEREVEJyxcbiAgJ1BBR0VTSVpFJyxcbiAgJ1BBUkFNRVRFUicsXG4gICdQQVJUJyxcbiAgJ1BBUlRJVElPTicsXG4gICdQQVJUSVRJT05FRCcsXG4gICdQQVJUSVRJT05JTkcnLFxuICAnUEFSVElUSU9OUycsXG4gICdQQVNTV09SRCcsXG4gICdQQVRIJyxcbiAgJ1BFUkNFTlRJTEVfQ09OVCcsXG4gICdQRVJDRU5USUxFX0RJU0MnLFxuICAnUEVSQ0VOVF9SQU5LJyxcbiAgJ1BJRUNFU0laRScsXG4gICdQTEFOJyxcbiAgJ1BPU0lUSU9OJyxcbiAgJ1BPV0VSJyxcbiAgJ1BSRUNJU0lPTicsXG4gICdQUkVQQVJFJyxcbiAgJ1BSRVZWQUwnLFxuICAnUFJJTUFSWScsXG4gICdQUklRVFknLFxuICAnUFJJVklMRUdFUycsXG4gICdQUk9DRURVUkUnLFxuICAnUFJPR1JBTScsXG4gICdQU0lEJyxcbiAgJ1BVQkxJQycsXG4gICdRVUVSWScsXG4gICdRVUVSWU5PJyxcbiAgJ1JBTkdFJyxcbiAgJ1JBTksnLFxuICAnUkVBRCcsXG4gICdSRUFEUycsXG4gICdSRUFMJyxcbiAgJ1JFQ09WRVJZJyxcbiAgJ1JFQ1VSU0lWRScsXG4gICdSRUYnLFxuICAnUkVGRVJFTkNFUycsXG4gICdSRUZFUkVOQ0lORycsXG4gICdSRUZSRVNIJyxcbiAgJ1JFR1JfQVZHWCcsXG4gICdSRUdSX0FWR1knLFxuICAnUkVHUl9DT1VOVCcsXG4gICdSRUdSX0lOVEVSQ0VQVCcsXG4gICdSRUdSX1IyJyxcbiAgJ1JFR1JfU0xPUEUnLFxuICAnUkVHUl9TWFgnLFxuICAnUkVHUl9TWFknLFxuICAnUkVHUl9TWVknLFxuICAnUkVMRUFTRScsXG4gICdSRU5BTUUnLFxuICAnUkVQRUFUJyxcbiAgJ1JFU0VUJyxcbiAgJ1JFU0lHTkFMJyxcbiAgJ1JFU1RBUlQnLFxuICAnUkVTVFJJQ1QnLFxuICAnUkVTVUxUJyxcbiAgJ1JFU1VMVF9TRVRfTE9DQVRPUicsXG4gICdSRVRVUk4nLFxuICAnUkVUVVJOUycsXG4gICdSRVZPS0UnLFxuICAnUklHSFQnLFxuICAnUk9MRScsXG4gICdST0xMQkFDSycsXG4gICdST0xMVVAnLFxuICAnUk9VTkRfQ0VJTElORycsXG4gICdST1VORF9ET1dOJyxcbiAgJ1JPVU5EX0ZMT09SJyxcbiAgJ1JPVU5EX0hBTEZfRE9XTicsXG4gICdST1VORF9IQUxGX0VWRU4nLFxuICAnUk9VTkRfSEFMRl9VUCcsXG4gICdST1VORF9VUCcsXG4gICdST1VUSU5FJyxcbiAgJ1JPVycsXG4gICdST1dOVU1CRVInLFxuICAnUk9XUycsXG4gICdST1dTRVQnLFxuICAnUk9XX05VTUJFUicsXG4gICdSUk4nLFxuICAnUlVOJyxcbiAgJ1NBVkVQT0lOVCcsXG4gICdTQ0hFTUEnLFxuICAnU0NPUEUnLFxuICAnU0NSQVRDSFBBRCcsXG4gICdTQ1JPTEwnLFxuICAnU0VBUkNIJyxcbiAgJ1NFQ09ORCcsXG4gICdTRUNPTkRTJyxcbiAgJ1NFQ1FUWScsXG4gICdTRUNVUklUWScsXG4gICdTRU5TSVRJVkUnLFxuICAnU0VRVUVOQ0UnLFxuICAnU0VTU0lPTicsXG4gICdTRVNTSU9OX1VTRVInLFxuICAnU0lHTkFMJyxcbiAgJ1NJTUlMQVInLFxuICAnU0lNUExFJyxcbiAgJ1NNQUxMSU5UJyxcbiAgJ1NOQU4nLFxuICAnU09NRScsXG4gICdTT1VSQ0UnLFxuICAnU1BFQ0lGSUMnLFxuICAnU1BFQ0lGSUNUWVBFJyxcbiAgJ1NRTCcsXG4gICdTUUxFWENFUFRJT04nLFxuICAnU1FMSUQnLFxuICAnU1FMU1RBVEUnLFxuICAnU1FMV0FSTklORycsXG4gICdTUVJUJyxcbiAgJ1NUQUNLRUQnLFxuICAnU1RBTkRBUkQnLFxuICAnU1RBUlQnLFxuICAnU1RBUlRJTkcnLFxuICAnU1RBVEVNRU5UJyxcbiAgJ1NUQVRJQycsXG4gICdTVEFUTUVOVCcsXG4gICdTVEFZJyxcbiAgJ1NURERFVl9QT1AnLFxuICAnU1REREVWX1NBTVAnLFxuICAnU1RPR1JPVVAnLFxuICAnU1RPUkVTJyxcbiAgJ1NUWUxFJyxcbiAgJ1NVQk1VTFRJU0VUJyxcbiAgJ1NVQlNUUklORycsXG4gICdTVU0nLFxuICAnU1VNTUFSWScsXG4gICdTWU1NRVRSSUMnLFxuICAnU1lOT05ZTScsXG4gICdTWVNGVU4nLFxuICAnU1lTSUJNJyxcbiAgJ1NZU1BST0MnLFxuICAnU1lTVEVNJyxcbiAgJ1NZU1RFTV9VU0VSJyxcbiAgJ1RBQkxFJyxcbiAgJ1RBQkxFU0FNUExFJyxcbiAgJ1RBQkxFU1BBQ0UnLFxuICAnVEhFTicsXG4gICdUSU1FJyxcbiAgJ1RJTUVTVEFNUCcsXG4gICdUSU1FWk9ORV9IT1VSJyxcbiAgJ1RJTUVaT05FX01JTlVURScsXG4gICdUTycsXG4gICdUUkFJTElORycsXG4gICdUUkFOU0FDVElPTicsXG4gICdUUkFOU0xBVEUnLFxuICAnVFJBTlNMQVRJT04nLFxuICAnVFJFQVQnLFxuICAnVFJJR0dFUicsXG4gICdUUklNJyxcbiAgJ1RSVUUnLFxuICAnVFJVTkNBVEUnLFxuICAnVFlQRScsXG4gICdVRVNDQVBFJyxcbiAgJ1VORE8nLFxuICAnVU5JUVVFJyxcbiAgJ1VOS05PV04nLFxuICAnVU5ORVNUJyxcbiAgJ1VOVElMJyxcbiAgJ1VQUEVSJyxcbiAgJ1VTQUdFJyxcbiAgJ1VTRVInLFxuICAnVVNJTkcnLFxuICAnVkFMSURQUk9DJyxcbiAgJ1ZBTFVFJyxcbiAgJ1ZBUkNIQVInLFxuICAnVkFSSUFCTEUnLFxuICAnVkFSSUFOVCcsXG4gICdWQVJZSU5HJyxcbiAgJ1ZBUl9QT1AnLFxuICAnVkFSX1NBTVAnLFxuICAnVkNBVCcsXG4gICdWRVJTSU9OJyxcbiAgJ1ZJRVcnLFxuICAnVk9MQVRJTEUnLFxuICAnVk9MVU1FUycsXG4gICdXSEVOJyxcbiAgJ1dIRU5FVkVSJyxcbiAgJ1dISUxFJyxcbiAgJ1dJRFRIX0JVQ0tFVCcsXG4gICdXSU5ET1cnLFxuICAnV0lUSCcsXG4gICdXSVRISU4nLFxuICAnV0lUSE9VVCcsXG4gICdXTE0nLFxuICAnV1JJVEUnLFxuICAnWE1MRUxFTUVOVCcsXG4gICdYTUxFWElTVFMnLFxuICAnWE1MTkFNRVNQQUNFUycsXG4gICdZRUFSJyxcbiAgJ1lFQVJTJyxcbl07XG5cbmNvbnN0IHJlc2VydmVkVG9wTGV2ZWxXb3JkcyA9IFtcbiAgJ0FERCcsXG4gICdBRlRFUicsXG4gICdBTFRFUiBDT0xVTU4nLFxuICAnQUxURVIgVEFCTEUnLFxuICAnREVMRVRFIEZST00nLFxuICAnRVhDRVBUJyxcbiAgJ0ZFVENIIEZJUlNUJyxcbiAgJ0ZST00nLFxuICAnR1JPVVAgQlknLFxuICAnR08nLFxuICAnSEFWSU5HJyxcbiAgJ0lOU0VSVCBJTlRPJyxcbiAgJ0lOVEVSU0VDVCcsXG4gICdMSU1JVCcsXG4gICdPUkRFUiBCWScsXG4gICdTRUxFQ1QnLFxuICAnU0VUIENVUlJFTlQgU0NIRU1BJyxcbiAgJ1NFVCBTQ0hFTUEnLFxuICAnU0VUJyxcbiAgJ1VQREFURScsXG4gICdWQUxVRVMnLFxuICAnV0hFUkUnLFxuXTtcblxuY29uc3QgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzTm9JbmRlbnQgPSBbJ0lOVEVSU0VDVCcsICdJTlRFUlNFQ1QgQUxMJywgJ01JTlVTJywgJ1VOSU9OJywgJ1VOSU9OIEFMTCddO1xuXG5jb25zdCByZXNlcnZlZE5ld2xpbmVXb3JkcyA9IFtcbiAgJ0FORCcsXG4gICdPUicsXG4gIC8vIGpvaW5zXG4gICdKT0lOJyxcbiAgJ0lOTkVSIEpPSU4nLFxuICAnTEVGVCBKT0lOJyxcbiAgJ0xFRlQgT1VURVIgSk9JTicsXG4gICdSSUdIVCBKT0lOJyxcbiAgJ1JJR0hUIE9VVEVSIEpPSU4nLFxuICAnRlVMTCBKT0lOJyxcbiAgJ0ZVTEwgT1VURVIgSk9JTicsXG4gICdDUk9TUyBKT0lOJyxcbiAgJ05BVFVSQUwgSk9JTicsXG5dO1xuXG4vLyBGb3IgcmVmZXJlbmNlOiBodHRwczovL3d3dy5pYm0uY29tL3N1cHBvcnQva25vd2xlZGdlY2VudGVyL2VuL3Nzd19pYm1faV83Mi9kYjIvcmJhZnppbnRyby5odG1cbmV4cG9ydCBkZWZhdWx0IGNsYXNzIERiMkZvcm1hdHRlciBleHRlbmRzIEZvcm1hdHRlciB7XG4gIHRva2VuaXplcigpIHtcbiAgICByZXR1cm4gbmV3IFRva2VuaXplcih7XG4gICAgICByZXNlcnZlZFdvcmRzLFxuICAgICAgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzLFxuICAgICAgcmVzZXJ2ZWROZXdsaW5lV29yZHMsXG4gICAgICByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCxcbiAgICAgIHN0cmluZ1R5cGVzOiBbYFwiXCJgLCBcIicnXCIsICdgYCcsICdbXSddLFxuICAgICAgb3BlblBhcmVuczogWycoJ10sXG4gICAgICBjbG9zZVBhcmVuczogWycpJ10sXG4gICAgICBpbmRleGVkUGxhY2Vob2xkZXJUeXBlczogWyc/J10sXG4gICAgICBuYW1lZFBsYWNlaG9sZGVyVHlwZXM6IFsnOiddLFxuICAgICAgbGluZUNvbW1lbnRUeXBlczogWyctLSddLFxuICAgICAgc3BlY2lhbFdvcmRDaGFyczogWycjJywgJ0AnXSxcbiAgICAgIG9wZXJhdG9yczogWycqKicsICchPScsICchPicsICchPicsICd8fCddLFxuICAgIH0pO1xuICB9XG59XG4iLCJpbXBvcnQgRm9ybWF0dGVyIGZyb20gJy4uL2NvcmUvRm9ybWF0dGVyJztcbmltcG9ydCBUb2tlbml6ZXIgZnJvbSAnLi4vY29yZS9Ub2tlbml6ZXInO1xuXG5jb25zdCByZXNlcnZlZFdvcmRzID0gW1xuICAnQUNDRVNTSUJMRScsXG4gICdBREQnLFxuICAnQUxMJyxcbiAgJ0FMVEVSJyxcbiAgJ0FOQUxZWkUnLFxuICAnQU5EJyxcbiAgJ0FTJyxcbiAgJ0FTQycsXG4gICdBU0VOU0lUSVZFJyxcbiAgJ0JFRk9SRScsXG4gICdCRVRXRUVOJyxcbiAgJ0JJR0lOVCcsXG4gICdCSU5BUlknLFxuICAnQkxPQicsXG4gICdCT1RIJyxcbiAgJ0JZJyxcbiAgJ0NBTEwnLFxuICAnQ0FTQ0FERScsXG4gICdDQVNFJyxcbiAgJ0NIQU5HRScsXG4gICdDSEFSJyxcbiAgJ0NIQVJBQ1RFUicsXG4gICdDSEVDSycsXG4gICdDT0xMQVRFJyxcbiAgJ0NPTFVNTicsXG4gICdDT05ESVRJT04nLFxuICAnQ09OU1RSQUlOVCcsXG4gICdDT05USU5VRScsXG4gICdDT05WRVJUJyxcbiAgJ0NSRUFURScsXG4gICdDUk9TUycsXG4gICdDVVJSRU5UX0RBVEUnLFxuICAnQ1VSUkVOVF9ST0xFJyxcbiAgJ0NVUlJFTlRfVElNRScsXG4gICdDVVJSRU5UX1RJTUVTVEFNUCcsXG4gICdDVVJSRU5UX1VTRVInLFxuICAnQ1VSU09SJyxcbiAgJ0RBVEFCQVNFJyxcbiAgJ0RBVEFCQVNFUycsXG4gICdEQVlfSE9VUicsXG4gICdEQVlfTUlDUk9TRUNPTkQnLFxuICAnREFZX01JTlVURScsXG4gICdEQVlfU0VDT05EJyxcbiAgJ0RFQycsXG4gICdERUNJTUFMJyxcbiAgJ0RFQ0xBUkUnLFxuICAnREVGQVVMVCcsXG4gICdERUxBWUVEJyxcbiAgJ0RFTEVURScsXG4gICdERVNDJyxcbiAgJ0RFU0NSSUJFJyxcbiAgJ0RFVEVSTUlOSVNUSUMnLFxuICAnRElTVElOQ1QnLFxuICAnRElTVElOQ1RST1cnLFxuICAnRElWJyxcbiAgJ0RPX0RPTUFJTl9JRFMnLFxuICAnRE9VQkxFJyxcbiAgJ0RST1AnLFxuICAnRFVBTCcsXG4gICdFQUNIJyxcbiAgJ0VMU0UnLFxuICAnRUxTRUlGJyxcbiAgJ0VOQ0xPU0VEJyxcbiAgJ0VTQ0FQRUQnLFxuICAnRVhDRVBUJyxcbiAgJ0VYSVNUUycsXG4gICdFWElUJyxcbiAgJ0VYUExBSU4nLFxuICAnRkFMU0UnLFxuICAnRkVUQ0gnLFxuICAnRkxPQVQnLFxuICAnRkxPQVQ0JyxcbiAgJ0ZMT0FUOCcsXG4gICdGT1InLFxuICAnRk9SQ0UnLFxuICAnRk9SRUlHTicsXG4gICdGUk9NJyxcbiAgJ0ZVTExURVhUJyxcbiAgJ0dFTkVSQUwnLFxuICAnR1JBTlQnLFxuICAnR1JPVVAnLFxuICAnSEFWSU5HJyxcbiAgJ0hJR0hfUFJJT1JJVFknLFxuICAnSE9VUl9NSUNST1NFQ09ORCcsXG4gICdIT1VSX01JTlVURScsXG4gICdIT1VSX1NFQ09ORCcsXG4gICdJRicsXG4gICdJR05PUkUnLFxuICAnSUdOT1JFX0RPTUFJTl9JRFMnLFxuICAnSUdOT1JFX1NFUlZFUl9JRFMnLFxuICAnSU4nLFxuICAnSU5ERVgnLFxuICAnSU5GSUxFJyxcbiAgJ0lOTkVSJyxcbiAgJ0lOT1VUJyxcbiAgJ0lOU0VOU0lUSVZFJyxcbiAgJ0lOU0VSVCcsXG4gICdJTlQnLFxuICAnSU5UMScsXG4gICdJTlQyJyxcbiAgJ0lOVDMnLFxuICAnSU5UNCcsXG4gICdJTlQ4JyxcbiAgJ0lOVEVHRVInLFxuICAnSU5URVJTRUNUJyxcbiAgJ0lOVEVSVkFMJyxcbiAgJ0lOVE8nLFxuICAnSVMnLFxuICAnSVRFUkFURScsXG4gICdKT0lOJyxcbiAgJ0tFWScsXG4gICdLRVlTJyxcbiAgJ0tJTEwnLFxuICAnTEVBRElORycsXG4gICdMRUFWRScsXG4gICdMRUZUJyxcbiAgJ0xJS0UnLFxuICAnTElNSVQnLFxuICAnTElORUFSJyxcbiAgJ0xJTkVTJyxcbiAgJ0xPQUQnLFxuICAnTE9DQUxUSU1FJyxcbiAgJ0xPQ0FMVElNRVNUQU1QJyxcbiAgJ0xPQ0snLFxuICAnTE9ORycsXG4gICdMT05HQkxPQicsXG4gICdMT05HVEVYVCcsXG4gICdMT09QJyxcbiAgJ0xPV19QUklPUklUWScsXG4gICdNQVNURVJfSEVBUlRCRUFUX1BFUklPRCcsXG4gICdNQVNURVJfU1NMX1ZFUklGWV9TRVJWRVJfQ0VSVCcsXG4gICdNQVRDSCcsXG4gICdNQVhWQUxVRScsXG4gICdNRURJVU1CTE9CJyxcbiAgJ01FRElVTUlOVCcsXG4gICdNRURJVU1URVhUJyxcbiAgJ01JRERMRUlOVCcsXG4gICdNSU5VVEVfTUlDUk9TRUNPTkQnLFxuICAnTUlOVVRFX1NFQ09ORCcsXG4gICdNT0QnLFxuICAnTU9ESUZJRVMnLFxuICAnTkFUVVJBTCcsXG4gICdOT1QnLFxuICAnTk9fV1JJVEVfVE9fQklOTE9HJyxcbiAgJ05VTEwnLFxuICAnTlVNRVJJQycsXG4gICdPTicsXG4gICdPUFRJTUlaRScsXG4gICdPUFRJT04nLFxuICAnT1BUSU9OQUxMWScsXG4gICdPUicsXG4gICdPUkRFUicsXG4gICdPVVQnLFxuICAnT1VURVInLFxuICAnT1VURklMRScsXG4gICdPVkVSJyxcbiAgJ1BBR0VfQ0hFQ0tTVU0nLFxuICAnUEFSU0VfVkNPTF9FWFBSJyxcbiAgJ1BBUlRJVElPTicsXG4gICdQT1NJVElPTicsXG4gICdQUkVDSVNJT04nLFxuICAnUFJJTUFSWScsXG4gICdQUk9DRURVUkUnLFxuICAnUFVSR0UnLFxuICAnUkFOR0UnLFxuICAnUkVBRCcsXG4gICdSRUFEUycsXG4gICdSRUFEX1dSSVRFJyxcbiAgJ1JFQUwnLFxuICAnUkVDVVJTSVZFJyxcbiAgJ1JFRl9TWVNURU1fSUQnLFxuICAnUkVGRVJFTkNFUycsXG4gICdSRUdFWFAnLFxuICAnUkVMRUFTRScsXG4gICdSRU5BTUUnLFxuICAnUkVQRUFUJyxcbiAgJ1JFUExBQ0UnLFxuICAnUkVRVUlSRScsXG4gICdSRVNJR05BTCcsXG4gICdSRVNUUklDVCcsXG4gICdSRVRVUk4nLFxuICAnUkVUVVJOSU5HJyxcbiAgJ1JFVk9LRScsXG4gICdSSUdIVCcsXG4gICdSTElLRScsXG4gICdST1dTJyxcbiAgJ1NDSEVNQScsXG4gICdTQ0hFTUFTJyxcbiAgJ1NFQ09ORF9NSUNST1NFQ09ORCcsXG4gICdTRUxFQ1QnLFxuICAnU0VOU0lUSVZFJyxcbiAgJ1NFUEFSQVRPUicsXG4gICdTRVQnLFxuICAnU0hPVycsXG4gICdTSUdOQUwnLFxuICAnU0xPVycsXG4gICdTTUFMTElOVCcsXG4gICdTUEFUSUFMJyxcbiAgJ1NQRUNJRklDJyxcbiAgJ1NRTCcsXG4gICdTUUxFWENFUFRJT04nLFxuICAnU1FMU1RBVEUnLFxuICAnU1FMV0FSTklORycsXG4gICdTUUxfQklHX1JFU1VMVCcsXG4gICdTUUxfQ0FMQ19GT1VORF9ST1dTJyxcbiAgJ1NRTF9TTUFMTF9SRVNVTFQnLFxuICAnU1NMJyxcbiAgJ1NUQVJUSU5HJyxcbiAgJ1NUQVRTX0FVVE9fUkVDQUxDJyxcbiAgJ1NUQVRTX1BFUlNJU1RFTlQnLFxuICAnU1RBVFNfU0FNUExFX1BBR0VTJyxcbiAgJ1NUUkFJR0hUX0pPSU4nLFxuICAnVEFCTEUnLFxuICAnVEVSTUlOQVRFRCcsXG4gICdUSEVOJyxcbiAgJ1RJTllCTE9CJyxcbiAgJ1RJTllJTlQnLFxuICAnVElOWVRFWFQnLFxuICAnVE8nLFxuICAnVFJBSUxJTkcnLFxuICAnVFJJR0dFUicsXG4gICdUUlVFJyxcbiAgJ1VORE8nLFxuICAnVU5JT04nLFxuICAnVU5JUVVFJyxcbiAgJ1VOTE9DSycsXG4gICdVTlNJR05FRCcsXG4gICdVUERBVEUnLFxuICAnVVNBR0UnLFxuICAnVVNFJyxcbiAgJ1VTSU5HJyxcbiAgJ1VUQ19EQVRFJyxcbiAgJ1VUQ19USU1FJyxcbiAgJ1VUQ19USU1FU1RBTVAnLFxuICAnVkFMVUVTJyxcbiAgJ1ZBUkJJTkFSWScsXG4gICdWQVJDSEFSJyxcbiAgJ1ZBUkNIQVJBQ1RFUicsXG4gICdWQVJZSU5HJyxcbiAgJ1dIRU4nLFxuICAnV0hFUkUnLFxuICAnV0hJTEUnLFxuICAnV0lORE9XJyxcbiAgJ1dJVEgnLFxuICAnV1JJVEUnLFxuICAnWE9SJyxcbiAgJ1lFQVJfTU9OVEgnLFxuICAnWkVST0ZJTEwnLFxuXTtcblxuY29uc3QgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzID0gW1xuICAnQUREJyxcbiAgJ0FMVEVSIENPTFVNTicsXG4gICdBTFRFUiBUQUJMRScsXG4gICdERUxFVEUgRlJPTScsXG4gICdFWENFUFQnLFxuICAnRlJPTScsXG4gICdHUk9VUCBCWScsXG4gICdIQVZJTkcnLFxuICAnSU5TRVJUIElOVE8nLFxuICAnSU5TRVJUJyxcbiAgJ0xJTUlUJyxcbiAgJ09SREVSIEJZJyxcbiAgJ1NFTEVDVCcsXG4gICdTRVQnLFxuICAnVVBEQVRFJyxcbiAgJ1ZBTFVFUycsXG4gICdXSEVSRScsXG5dO1xuXG5jb25zdCByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCA9IFsnSU5URVJTRUNUJywgJ0lOVEVSU0VDVCBBTEwnLCAnVU5JT04nLCAnVU5JT04gQUxMJ107XG5cbmNvbnN0IHJlc2VydmVkTmV3bGluZVdvcmRzID0gW1xuICAnQU5EJyxcbiAgJ0VMU0UnLFxuICAnT1InLFxuICAnV0hFTicsXG4gIC8vIGpvaW5zXG4gICdKT0lOJyxcbiAgJ0lOTkVSIEpPSU4nLFxuICAnTEVGVCBKT0lOJyxcbiAgJ0xFRlQgT1VURVIgSk9JTicsXG4gICdSSUdIVCBKT0lOJyxcbiAgJ1JJR0hUIE9VVEVSIEpPSU4nLFxuICAnQ1JPU1MgSk9JTicsXG4gICdOQVRVUkFMIEpPSU4nLFxuICAvLyBub24tc3RhbmRhcmQgam9pbnNcbiAgJ1NUUkFJR0hUX0pPSU4nLFxuICAnTkFUVVJBTCBMRUZUIEpPSU4nLFxuICAnTkFUVVJBTCBMRUZUIE9VVEVSIEpPSU4nLFxuICAnTkFUVVJBTCBSSUdIVCBKT0lOJyxcbiAgJ05BVFVSQUwgUklHSFQgT1VURVIgSk9JTicsXG5dO1xuXG4vLyBGb3IgcmVmZXJlbmNlOiBodHRwczovL21hcmlhZGIuY29tL2tiL2VuL3NxbC1zdGF0ZW1lbnRzLXN0cnVjdHVyZS9cbmV4cG9ydCBkZWZhdWx0IGNsYXNzIE1hcmlhRGJGb3JtYXR0ZXIgZXh0ZW5kcyBGb3JtYXR0ZXIge1xuICB0b2tlbml6ZXIoKSB7XG4gICAgcmV0dXJuIG5ldyBUb2tlbml6ZXIoe1xuICAgICAgcmVzZXJ2ZWRXb3JkcyxcbiAgICAgIHJlc2VydmVkVG9wTGV2ZWxXb3JkcyxcbiAgICAgIHJlc2VydmVkTmV3bGluZVdvcmRzLFxuICAgICAgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzTm9JbmRlbnQsXG4gICAgICBzdHJpbmdUeXBlczogWydgYCcsIFwiJydcIiwgJ1wiXCInXSxcbiAgICAgIG9wZW5QYXJlbnM6IFsnKCcsICdDQVNFJ10sXG4gICAgICBjbG9zZVBhcmVuczogWycpJywgJ0VORCddLFxuICAgICAgaW5kZXhlZFBsYWNlaG9sZGVyVHlwZXM6IFsnPyddLFxuICAgICAgbmFtZWRQbGFjZWhvbGRlclR5cGVzOiBbXSxcbiAgICAgIGxpbmVDb21tZW50VHlwZXM6IFsnLS0nLCAnIyddLFxuICAgICAgc3BlY2lhbFdvcmRDaGFyczogWydAJ10sXG4gICAgICBvcGVyYXRvcnM6IFsnOj0nLCAnPDwnLCAnPj4nLCAnIT0nLCAnPD4nLCAnPD0+JywgJyYmJywgJ3x8J10sXG4gICAgfSk7XG4gIH1cbn1cbiIsImltcG9ydCBGb3JtYXR0ZXIgZnJvbSAnLi4vY29yZS9Gb3JtYXR0ZXInO1xuaW1wb3J0IFRva2VuaXplciBmcm9tICcuLi9jb3JlL1Rva2VuaXplcic7XG5cbmNvbnN0IHJlc2VydmVkV29yZHMgPSBbXG4gICdBQ0NFU1NJQkxFJyxcbiAgJ0FERCcsXG4gICdBTEwnLFxuICAnQUxURVInLFxuICAnQU5BTFlaRScsXG4gICdBTkQnLFxuICAnQVMnLFxuICAnQVNDJyxcbiAgJ0FTRU5TSVRJVkUnLFxuICAnQkVGT1JFJyxcbiAgJ0JFVFdFRU4nLFxuICAnQklHSU5UJyxcbiAgJ0JJTkFSWScsXG4gICdCTE9CJyxcbiAgJ0JPVEgnLFxuICAnQlknLFxuICAnQ0FMTCcsXG4gICdDQVNDQURFJyxcbiAgJ0NBU0UnLFxuICAnQ0hBTkdFJyxcbiAgJ0NIQVInLFxuICAnQ0hBUkFDVEVSJyxcbiAgJ0NIRUNLJyxcbiAgJ0NPTExBVEUnLFxuICAnQ09MVU1OJyxcbiAgJ0NPTkRJVElPTicsXG4gICdDT05TVFJBSU5UJyxcbiAgJ0NPTlRJTlVFJyxcbiAgJ0NPTlZFUlQnLFxuICAnQ1JFQVRFJyxcbiAgJ0NST1NTJyxcbiAgJ0NVQkUnLFxuICAnQ1VNRV9ESVNUJyxcbiAgJ0NVUlJFTlRfREFURScsXG4gICdDVVJSRU5UX1RJTUUnLFxuICAnQ1VSUkVOVF9USU1FU1RBTVAnLFxuICAnQ1VSUkVOVF9VU0VSJyxcbiAgJ0NVUlNPUicsXG4gICdEQVRBQkFTRScsXG4gICdEQVRBQkFTRVMnLFxuICAnREFZX0hPVVInLFxuICAnREFZX01JQ1JPU0VDT05EJyxcbiAgJ0RBWV9NSU5VVEUnLFxuICAnREFZX1NFQ09ORCcsXG4gICdERUMnLFxuICAnREVDSU1BTCcsXG4gICdERUNMQVJFJyxcbiAgJ0RFRkFVTFQnLFxuICAnREVMQVlFRCcsXG4gICdERUxFVEUnLFxuICAnREVOU0VfUkFOSycsXG4gICdERVNDJyxcbiAgJ0RFU0NSSUJFJyxcbiAgJ0RFVEVSTUlOSVNUSUMnLFxuICAnRElTVElOQ1QnLFxuICAnRElTVElOQ1RST1cnLFxuICAnRElWJyxcbiAgJ0RPVUJMRScsXG4gICdEUk9QJyxcbiAgJ0RVQUwnLFxuICAnRUFDSCcsXG4gICdFTFNFJyxcbiAgJ0VMU0VJRicsXG4gICdFTVBUWScsXG4gICdFTkNMT1NFRCcsXG4gICdFU0NBUEVEJyxcbiAgJ0VYQ0VQVCcsXG4gICdFWElTVFMnLFxuICAnRVhJVCcsXG4gICdFWFBMQUlOJyxcbiAgJ0ZBTFNFJyxcbiAgJ0ZFVENIJyxcbiAgJ0ZJUlNUX1ZBTFVFJyxcbiAgJ0ZMT0FUJyxcbiAgJ0ZMT0FUNCcsXG4gICdGTE9BVDgnLFxuICAnRk9SJyxcbiAgJ0ZPUkNFJyxcbiAgJ0ZPUkVJR04nLFxuICAnRlJPTScsXG4gICdGVUxMVEVYVCcsXG4gICdGVU5DVElPTicsXG4gICdHRU5FUkFURUQnLFxuICAnR0VUJyxcbiAgJ0dSQU5UJyxcbiAgJ0dST1VQJyxcbiAgJ0dST1VQSU5HJyxcbiAgJ0dST1VQUycsXG4gICdIQVZJTkcnLFxuICAnSElHSF9QUklPUklUWScsXG4gICdIT1VSX01JQ1JPU0VDT05EJyxcbiAgJ0hPVVJfTUlOVVRFJyxcbiAgJ0hPVVJfU0VDT05EJyxcbiAgJ0lGJyxcbiAgJ0lHTk9SRScsXG4gICdJTicsXG4gICdJTkRFWCcsXG4gICdJTkZJTEUnLFxuICAnSU5ORVInLFxuICAnSU5PVVQnLFxuICAnSU5TRU5TSVRJVkUnLFxuICAnSU5TRVJUJyxcbiAgJ0lOVCcsXG4gICdJTlQxJyxcbiAgJ0lOVDInLFxuICAnSU5UMycsXG4gICdJTlQ0JyxcbiAgJ0lOVDgnLFxuICAnSU5URUdFUicsXG4gICdJTlRFUlZBTCcsXG4gICdJTlRPJyxcbiAgJ0lPX0FGVEVSX0dUSURTJyxcbiAgJ0lPX0JFRk9SRV9HVElEUycsXG4gICdJUycsXG4gICdJVEVSQVRFJyxcbiAgJ0pPSU4nLFxuICAnSlNPTl9UQUJMRScsXG4gICdLRVknLFxuICAnS0VZUycsXG4gICdLSUxMJyxcbiAgJ0xBRycsXG4gICdMQVNUX1ZBTFVFJyxcbiAgJ0xBVEVSQUwnLFxuICAnTEVBRCcsXG4gICdMRUFESU5HJyxcbiAgJ0xFQVZFJyxcbiAgJ0xFRlQnLFxuICAnTElLRScsXG4gICdMSU1JVCcsXG4gICdMSU5FQVInLFxuICAnTElORVMnLFxuICAnTE9BRCcsXG4gICdMT0NBTFRJTUUnLFxuICAnTE9DQUxUSU1FU1RBTVAnLFxuICAnTE9DSycsXG4gICdMT05HJyxcbiAgJ0xPTkdCTE9CJyxcbiAgJ0xPTkdURVhUJyxcbiAgJ0xPT1AnLFxuICAnTE9XX1BSSU9SSVRZJyxcbiAgJ01BU1RFUl9CSU5EJyxcbiAgJ01BU1RFUl9TU0xfVkVSSUZZX1NFUlZFUl9DRVJUJyxcbiAgJ01BVENIJyxcbiAgJ01BWFZBTFVFJyxcbiAgJ01FRElVTUJMT0InLFxuICAnTUVESVVNSU5UJyxcbiAgJ01FRElVTVRFWFQnLFxuICAnTUlERExFSU5UJyxcbiAgJ01JTlVURV9NSUNST1NFQ09ORCcsXG4gICdNSU5VVEVfU0VDT05EJyxcbiAgJ01PRCcsXG4gICdNT0RJRklFUycsXG4gICdOQVRVUkFMJyxcbiAgJ05PVCcsXG4gICdOT19XUklURV9UT19CSU5MT0cnLFxuICAnTlRIX1ZBTFVFJyxcbiAgJ05USUxFJyxcbiAgJ05VTEwnLFxuICAnTlVNRVJJQycsXG4gICdPRicsXG4gICdPTicsXG4gICdPUFRJTUlaRScsXG4gICdPUFRJTUlaRVJfQ09TVFMnLFxuICAnT1BUSU9OJyxcbiAgJ09QVElPTkFMTFknLFxuICAnT1InLFxuICAnT1JERVInLFxuICAnT1VUJyxcbiAgJ09VVEVSJyxcbiAgJ09VVEZJTEUnLFxuICAnT1ZFUicsXG4gICdQQVJUSVRJT04nLFxuICAnUEVSQ0VOVF9SQU5LJyxcbiAgJ1BSRUNJU0lPTicsXG4gICdQUklNQVJZJyxcbiAgJ1BST0NFRFVSRScsXG4gICdQVVJHRScsXG4gICdSQU5HRScsXG4gICdSQU5LJyxcbiAgJ1JFQUQnLFxuICAnUkVBRFMnLFxuICAnUkVBRF9XUklURScsXG4gICdSRUFMJyxcbiAgJ1JFQ1VSU0lWRScsXG4gICdSRUZFUkVOQ0VTJyxcbiAgJ1JFR0VYUCcsXG4gICdSRUxFQVNFJyxcbiAgJ1JFTkFNRScsXG4gICdSRVBFQVQnLFxuICAnUkVQTEFDRScsXG4gICdSRVFVSVJFJyxcbiAgJ1JFU0lHTkFMJyxcbiAgJ1JFU1RSSUNUJyxcbiAgJ1JFVFVSTicsXG4gICdSRVZPS0UnLFxuICAnUklHSFQnLFxuICAnUkxJS0UnLFxuICAnUk9XJyxcbiAgJ1JPV1MnLFxuICAnUk9XX05VTUJFUicsXG4gICdTQ0hFTUEnLFxuICAnU0NIRU1BUycsXG4gICdTRUNPTkRfTUlDUk9TRUNPTkQnLFxuICAnU0VMRUNUJyxcbiAgJ1NFTlNJVElWRScsXG4gICdTRVBBUkFUT1InLFxuICAnU0VUJyxcbiAgJ1NIT1cnLFxuICAnU0lHTkFMJyxcbiAgJ1NNQUxMSU5UJyxcbiAgJ1NQQVRJQUwnLFxuICAnU1BFQ0lGSUMnLFxuICAnU1FMJyxcbiAgJ1NRTEVYQ0VQVElPTicsXG4gICdTUUxTVEFURScsXG4gICdTUUxXQVJOSU5HJyxcbiAgJ1NRTF9CSUdfUkVTVUxUJyxcbiAgJ1NRTF9DQUxDX0ZPVU5EX1JPV1MnLFxuICAnU1FMX1NNQUxMX1JFU1VMVCcsXG4gICdTU0wnLFxuICAnU1RBUlRJTkcnLFxuICAnU1RPUkVEJyxcbiAgJ1NUUkFJR0hUX0pPSU4nLFxuICAnU1lTVEVNJyxcbiAgJ1RBQkxFJyxcbiAgJ1RFUk1JTkFURUQnLFxuICAnVEhFTicsXG4gICdUSU5ZQkxPQicsXG4gICdUSU5ZSU5UJyxcbiAgJ1RJTllURVhUJyxcbiAgJ1RPJyxcbiAgJ1RSQUlMSU5HJyxcbiAgJ1RSSUdHRVInLFxuICAnVFJVRScsXG4gICdVTkRPJyxcbiAgJ1VOSU9OJyxcbiAgJ1VOSVFVRScsXG4gICdVTkxPQ0snLFxuICAnVU5TSUdORUQnLFxuICAnVVBEQVRFJyxcbiAgJ1VTQUdFJyxcbiAgJ1VTRScsXG4gICdVU0lORycsXG4gICdVVENfREFURScsXG4gICdVVENfVElNRScsXG4gICdVVENfVElNRVNUQU1QJyxcbiAgJ1ZBTFVFUycsXG4gICdWQVJCSU5BUlknLFxuICAnVkFSQ0hBUicsXG4gICdWQVJDSEFSQUNURVInLFxuICAnVkFSWUlORycsXG4gICdWSVJUVUFMJyxcbiAgJ1dIRU4nLFxuICAnV0hFUkUnLFxuICAnV0hJTEUnLFxuICAnV0lORE9XJyxcbiAgJ1dJVEgnLFxuICAnV1JJVEUnLFxuICAnWE9SJyxcbiAgJ1lFQVJfTU9OVEgnLFxuICAnWkVST0ZJTEwnLFxuXTtcblxuY29uc3QgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzID0gW1xuICAnQUREJyxcbiAgJ0FMVEVSIENPTFVNTicsXG4gICdBTFRFUiBUQUJMRScsXG4gICdERUxFVEUgRlJPTScsXG4gICdFWENFUFQnLFxuICAnRlJPTScsXG4gICdHUk9VUCBCWScsXG4gICdIQVZJTkcnLFxuICAnSU5TRVJUIElOVE8nLFxuICAnSU5TRVJUJyxcbiAgJ0xJTUlUJyxcbiAgJ09SREVSIEJZJyxcbiAgJ1NFTEVDVCcsXG4gICdTRVQnLFxuICAnVVBEQVRFJyxcbiAgJ1ZBTFVFUycsXG4gICdXSEVSRScsXG5dO1xuXG5jb25zdCByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCA9IFsnSU5URVJTRUNUJywgJ0lOVEVSU0VDVCBBTEwnLCAnVU5JT04nLCAnVU5JT04gQUxMJ107XG5cbmNvbnN0IHJlc2VydmVkTmV3bGluZVdvcmRzID0gW1xuICAnQU5EJyxcbiAgJ0VMU0UnLFxuICAnT1InLFxuICAnV0hFTicsXG4gIC8vIGpvaW5zXG4gICdKT0lOJyxcbiAgJ0lOTkVSIEpPSU4nLFxuICAnTEVGVCBKT0lOJyxcbiAgJ0xFRlQgT1VURVIgSk9JTicsXG4gICdSSUdIVCBKT0lOJyxcbiAgJ1JJR0hUIE9VVEVSIEpPSU4nLFxuICAnQ1JPU1MgSk9JTicsXG4gICdOQVRVUkFMIEpPSU4nLFxuICAvLyBub24tc3RhbmRhcmQgam9pbnNcbiAgJ1NUUkFJR0hUX0pPSU4nLFxuICAnTkFUVVJBTCBMRUZUIEpPSU4nLFxuICAnTkFUVVJBTCBMRUZUIE9VVEVSIEpPSU4nLFxuICAnTkFUVVJBTCBSSUdIVCBKT0lOJyxcbiAgJ05BVFVSQUwgUklHSFQgT1VURVIgSk9JTicsXG5dO1xuXG5leHBvcnQgZGVmYXVsdCBjbGFzcyBNeVNxbEZvcm1hdHRlciBleHRlbmRzIEZvcm1hdHRlciB7XG4gIHRva2VuaXplcigpIHtcbiAgICByZXR1cm4gbmV3IFRva2VuaXplcih7XG4gICAgICByZXNlcnZlZFdvcmRzLFxuICAgICAgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzLFxuICAgICAgcmVzZXJ2ZWROZXdsaW5lV29yZHMsXG4gICAgICByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCxcbiAgICAgIHN0cmluZ1R5cGVzOiBbJ2BgJywgXCInJ1wiLCAnXCJcIiddLFxuICAgICAgb3BlblBhcmVuczogWycoJywgJ0NBU0UnXSxcbiAgICAgIGNsb3NlUGFyZW5zOiBbJyknLCAnRU5EJ10sXG4gICAgICBpbmRleGVkUGxhY2Vob2xkZXJUeXBlczogWyc/J10sXG4gICAgICBuYW1lZFBsYWNlaG9sZGVyVHlwZXM6IFtdLFxuICAgICAgbGluZUNvbW1lbnRUeXBlczogWyctLScsICcjJ10sXG4gICAgICBzcGVjaWFsV29yZENoYXJzOiBbJ0AnXSxcbiAgICAgIG9wZXJhdG9yczogWyc6PScsICc8PCcsICc+PicsICchPScsICc8PicsICc8PT4nLCAnJiYnLCAnfHwnLCAnLT4nLCAnLT4+J10sXG4gICAgfSk7XG4gIH1cbn1cbiIsImltcG9ydCBGb3JtYXR0ZXIgZnJvbSAnLi4vY29yZS9Gb3JtYXR0ZXInO1xuaW1wb3J0IFRva2VuaXplciBmcm9tICcuLi9jb3JlL1Rva2VuaXplcic7XG5cbmNvbnN0IHJlc2VydmVkV29yZHMgPSBbXG4gICdBTEwnLFxuICAnQUxURVInLFxuICAnQU5BTFlaRScsXG4gICdBTkQnLFxuICAnQU5ZJyxcbiAgJ0FSUkFZJyxcbiAgJ0FTJyxcbiAgJ0FTQycsXG4gICdCRUdJTicsXG4gICdCRVRXRUVOJyxcbiAgJ0JJTkFSWScsXG4gICdCT09MRUFOJyxcbiAgJ0JSRUFLJyxcbiAgJ0JVQ0tFVCcsXG4gICdCVUlMRCcsXG4gICdCWScsXG4gICdDQUxMJyxcbiAgJ0NBU0UnLFxuICAnQ0FTVCcsXG4gICdDTFVTVEVSJyxcbiAgJ0NPTExBVEUnLFxuICAnQ09MTEVDVElPTicsXG4gICdDT01NSVQnLFxuICAnQ09OTkVDVCcsXG4gICdDT05USU5VRScsXG4gICdDT1JSRUxBVEUnLFxuICAnQ09WRVInLFxuICAnQ1JFQVRFJyxcbiAgJ0RBVEFCQVNFJyxcbiAgJ0RBVEFTRVQnLFxuICAnREFUQVNUT1JFJyxcbiAgJ0RFQ0xBUkUnLFxuICAnREVDUkVNRU5UJyxcbiAgJ0RFTEVURScsXG4gICdERVJJVkVEJyxcbiAgJ0RFU0MnLFxuICAnREVTQ1JJQkUnLFxuICAnRElTVElOQ1QnLFxuICAnRE8nLFxuICAnRFJPUCcsXG4gICdFQUNIJyxcbiAgJ0VMRU1FTlQnLFxuICAnRUxTRScsXG4gICdFTkQnLFxuICAnRVZFUlknLFxuICAnRVhDRVBUJyxcbiAgJ0VYQ0xVREUnLFxuICAnRVhFQ1VURScsXG4gICdFWElTVFMnLFxuICAnRVhQTEFJTicsXG4gICdGQUxTRScsXG4gICdGRVRDSCcsXG4gICdGSVJTVCcsXG4gICdGTEFUVEVOJyxcbiAgJ0ZPUicsXG4gICdGT1JDRScsXG4gICdGUk9NJyxcbiAgJ0ZVTkNUSU9OJyxcbiAgJ0dSQU5UJyxcbiAgJ0dST1VQJyxcbiAgJ0dTSScsXG4gICdIQVZJTkcnLFxuICAnSUYnLFxuICAnSUdOT1JFJyxcbiAgJ0lMSUtFJyxcbiAgJ0lOJyxcbiAgJ0lOQ0xVREUnLFxuICAnSU5DUkVNRU5UJyxcbiAgJ0lOREVYJyxcbiAgJ0lORkVSJyxcbiAgJ0lOTElORScsXG4gICdJTk5FUicsXG4gICdJTlNFUlQnLFxuICAnSU5URVJTRUNUJyxcbiAgJ0lOVE8nLFxuICAnSVMnLFxuICAnSk9JTicsXG4gICdLRVknLFxuICAnS0VZUycsXG4gICdLRVlTUEFDRScsXG4gICdLTk9XTicsXG4gICdMQVNUJyxcbiAgJ0xFRlQnLFxuICAnTEVUJyxcbiAgJ0xFVFRJTkcnLFxuICAnTElLRScsXG4gICdMSU1JVCcsXG4gICdMU00nLFxuICAnTUFQJyxcbiAgJ01BUFBJTkcnLFxuICAnTUFUQ0hFRCcsXG4gICdNQVRFUklBTElaRUQnLFxuICAnTUVSR0UnLFxuICAnTUlTU0lORycsXG4gICdOQU1FU1BBQ0UnLFxuICAnTkVTVCcsXG4gICdOT1QnLFxuICAnTlVMTCcsXG4gICdOVU1CRVInLFxuICAnT0JKRUNUJyxcbiAgJ09GRlNFVCcsXG4gICdPTicsXG4gICdPUFRJT04nLFxuICAnT1InLFxuICAnT1JERVInLFxuICAnT1VURVInLFxuICAnT1ZFUicsXG4gICdQQVJTRScsXG4gICdQQVJUSVRJT04nLFxuICAnUEFTU1dPUkQnLFxuICAnUEFUSCcsXG4gICdQT09MJyxcbiAgJ1BSRVBBUkUnLFxuICAnUFJJTUFSWScsXG4gICdQUklWQVRFJyxcbiAgJ1BSSVZJTEVHRScsXG4gICdQUk9DRURVUkUnLFxuICAnUFVCTElDJyxcbiAgJ1JBVycsXG4gICdSRUFMTScsXG4gICdSRURVQ0UnLFxuICAnUkVOQU1FJyxcbiAgJ1JFVFVSTicsXG4gICdSRVRVUk5JTkcnLFxuICAnUkVWT0tFJyxcbiAgJ1JJR0hUJyxcbiAgJ1JPTEUnLFxuICAnUk9MTEJBQ0snLFxuICAnU0FUSVNGSUVTJyxcbiAgJ1NDSEVNQScsXG4gICdTRUxFQ1QnLFxuICAnU0VMRicsXG4gICdTRU1JJyxcbiAgJ1NFVCcsXG4gICdTSE9XJyxcbiAgJ1NPTUUnLFxuICAnU1RBUlQnLFxuICAnU1RBVElTVElDUycsXG4gICdTVFJJTkcnLFxuICAnU1lTVEVNJyxcbiAgJ1RIRU4nLFxuICAnVE8nLFxuICAnVFJBTlNBQ1RJT04nLFxuICAnVFJJR0dFUicsXG4gICdUUlVFJyxcbiAgJ1RSVU5DQVRFJyxcbiAgJ1VOREVSJyxcbiAgJ1VOSU9OJyxcbiAgJ1VOSVFVRScsXG4gICdVTktOT1dOJyxcbiAgJ1VOTkVTVCcsXG4gICdVTlNFVCcsXG4gICdVUERBVEUnLFxuICAnVVBTRVJUJyxcbiAgJ1VTRScsXG4gICdVU0VSJyxcbiAgJ1VTSU5HJyxcbiAgJ1ZBTElEQVRFJyxcbiAgJ1ZBTFVFJyxcbiAgJ1ZBTFVFRCcsXG4gICdWQUxVRVMnLFxuICAnVklBJyxcbiAgJ1ZJRVcnLFxuICAnV0hFTicsXG4gICdXSEVSRScsXG4gICdXSElMRScsXG4gICdXSVRIJyxcbiAgJ1dJVEhJTicsXG4gICdXT1JLJyxcbiAgJ1hPUicsXG5dO1xuXG5jb25zdCByZXNlcnZlZFRvcExldmVsV29yZHMgPSBbXG4gICdERUxFVEUgRlJPTScsXG4gICdFWENFUFQgQUxMJyxcbiAgJ0VYQ0VQVCcsXG4gICdFWFBMQUlOIERFTEVURSBGUk9NJyxcbiAgJ0VYUExBSU4gVVBEQVRFJyxcbiAgJ0VYUExBSU4gVVBTRVJUJyxcbiAgJ0ZST00nLFxuICAnR1JPVVAgQlknLFxuICAnSEFWSU5HJyxcbiAgJ0lORkVSJyxcbiAgJ0lOU0VSVCBJTlRPJyxcbiAgJ0xFVCcsXG4gICdMSU1JVCcsXG4gICdNRVJHRScsXG4gICdORVNUJyxcbiAgJ09SREVSIEJZJyxcbiAgJ1BSRVBBUkUnLFxuICAnU0VMRUNUJyxcbiAgJ1NFVCBDVVJSRU5UIFNDSEVNQScsXG4gICdTRVQgU0NIRU1BJyxcbiAgJ1NFVCcsXG4gICdVTk5FU1QnLFxuICAnVVBEQVRFJyxcbiAgJ1VQU0VSVCcsXG4gICdVU0UgS0VZUycsXG4gICdWQUxVRVMnLFxuICAnV0hFUkUnLFxuXTtcblxuY29uc3QgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzTm9JbmRlbnQgPSBbJ0lOVEVSU0VDVCcsICdJTlRFUlNFQ1QgQUxMJywgJ01JTlVTJywgJ1VOSU9OJywgJ1VOSU9OIEFMTCddO1xuXG5jb25zdCByZXNlcnZlZE5ld2xpbmVXb3JkcyA9IFtcbiAgJ0FORCcsXG4gICdPUicsXG4gICdYT1InLFxuICAvLyBqb2luc1xuICAnSk9JTicsXG4gICdJTk5FUiBKT0lOJyxcbiAgJ0xFRlQgSk9JTicsXG4gICdMRUZUIE9VVEVSIEpPSU4nLFxuICAnUklHSFQgSk9JTicsXG4gICdSSUdIVCBPVVRFUiBKT0lOJyxcbl07XG5cbi8vIEZvciByZWZlcmVuY2U6IGh0dHA6Ly9kb2NzLmNvdWNoYmFzZS5jb20uczMtd2Vic2l0ZS11cy13ZXN0LTEuYW1hem9uYXdzLmNvbS9zZXJ2ZXIvNi4wL24xcWwvbjFxbC1sYW5ndWFnZS1yZWZlcmVuY2UvaW5kZXguaHRtbFxuZXhwb3J0IGRlZmF1bHQgY2xhc3MgTjFxbEZvcm1hdHRlciBleHRlbmRzIEZvcm1hdHRlciB7XG4gIHRva2VuaXplcigpIHtcbiAgICByZXR1cm4gbmV3IFRva2VuaXplcih7XG4gICAgICByZXNlcnZlZFdvcmRzLFxuICAgICAgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzLFxuICAgICAgcmVzZXJ2ZWROZXdsaW5lV29yZHMsXG4gICAgICByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCxcbiAgICAgIHN0cmluZ1R5cGVzOiBbYFwiXCJgLCBcIicnXCIsICdgYCddLFxuICAgICAgb3BlblBhcmVuczogWycoJywgJ1snLCAneyddLFxuICAgICAgY2xvc2VQYXJlbnM6IFsnKScsICddJywgJ30nXSxcbiAgICAgIG5hbWVkUGxhY2Vob2xkZXJUeXBlczogWyckJ10sXG4gICAgICBsaW5lQ29tbWVudFR5cGVzOiBbJyMnLCAnLS0nXSxcbiAgICAgIG9wZXJhdG9yczogWyc9PScsICchPSddLFxuICAgIH0pO1xuICB9XG59XG4iLCJpbXBvcnQgRm9ybWF0dGVyIGZyb20gJy4uL2NvcmUvRm9ybWF0dGVyJztcbmltcG9ydCB7IGlzQnksIGlzU2V0IH0gZnJvbSAnLi4vY29yZS90b2tlbic7XG5pbXBvcnQgVG9rZW5pemVyIGZyb20gJy4uL2NvcmUvVG9rZW5pemVyJztcbmltcG9ydCB0b2tlblR5cGVzIGZyb20gJy4uL2NvcmUvdG9rZW5UeXBlcyc7XG5cbmNvbnN0IHJlc2VydmVkV29yZHMgPSBbXG4gICdBJyxcbiAgJ0FDQ0VTU0lCTEUnLFxuICAnQUdFTlQnLFxuICAnQUdHUkVHQVRFJyxcbiAgJ0FMTCcsXG4gICdBTFRFUicsXG4gICdBTlknLFxuICAnQVJSQVknLFxuICAnQVMnLFxuICAnQVNDJyxcbiAgJ0FUJyxcbiAgJ0FUVFJJQlVURScsXG4gICdBVVRISUQnLFxuICAnQVZHJyxcbiAgJ0JFVFdFRU4nLFxuICAnQkZJTEVfQkFTRScsXG4gICdCSU5BUllfSU5URUdFUicsXG4gICdCSU5BUlknLFxuICAnQkxPQl9CQVNFJyxcbiAgJ0JMT0NLJyxcbiAgJ0JPRFknLFxuICAnQk9PTEVBTicsXG4gICdCT1RIJyxcbiAgJ0JPVU5EJyxcbiAgJ0JSRUFEVEgnLFxuICAnQlVMSycsXG4gICdCWScsXG4gICdCWVRFJyxcbiAgJ0MnLFxuICAnQ0FMTCcsXG4gICdDQUxMSU5HJyxcbiAgJ0NBU0NBREUnLFxuICAnQ0FTRScsXG4gICdDSEFSX0JBU0UnLFxuICAnQ0hBUicsXG4gICdDSEFSQUNURVInLFxuICAnQ0hBUlNFVCcsXG4gICdDSEFSU0VURk9STScsXG4gICdDSEFSU0VUSUQnLFxuICAnQ0hFQ0snLFxuICAnQ0xPQl9CQVNFJyxcbiAgJ0NMT05FJyxcbiAgJ0NMT1NFJyxcbiAgJ0NMVVNURVInLFxuICAnQ0xVU1RFUlMnLFxuICAnQ09BTEVTQ0UnLFxuICAnQ09MQVVUSCcsXG4gICdDT0xMRUNUJyxcbiAgJ0NPTFVNTlMnLFxuICAnQ09NTUVOVCcsXG4gICdDT01NSVQnLFxuICAnQ09NTUlUVEVEJyxcbiAgJ0NPTVBJTEVEJyxcbiAgJ0NPTVBSRVNTJyxcbiAgJ0NPTk5FQ1QnLFxuICAnQ09OU1RBTlQnLFxuICAnQ09OU1RSVUNUT1InLFxuICAnQ09OVEVYVCcsXG4gICdDT05USU5VRScsXG4gICdDT05WRVJUJyxcbiAgJ0NPVU5UJyxcbiAgJ0NSQVNIJyxcbiAgJ0NSRUFURScsXG4gICdDUkVERU5USUFMJyxcbiAgJ0NVUlJFTlQnLFxuICAnQ1VSUlZBTCcsXG4gICdDVVJTT1InLFxuICAnQ1VTVE9NREFUVU0nLFxuICAnREFOR0xJTkcnLFxuICAnREFUQScsXG4gICdEQVRFX0JBU0UnLFxuICAnREFURScsXG4gICdEQVknLFxuICAnREVDSU1BTCcsXG4gICdERUZBVUxUJyxcbiAgJ0RFRklORScsXG4gICdERUxFVEUnLFxuICAnREVQVEgnLFxuICAnREVTQycsXG4gICdERVRFUk1JTklTVElDJyxcbiAgJ0RJUkVDVE9SWScsXG4gICdESVNUSU5DVCcsXG4gICdETycsXG4gICdET1VCTEUnLFxuICAnRFJPUCcsXG4gICdEVVJBVElPTicsXG4gICdFTEVNRU5UJyxcbiAgJ0VMU0lGJyxcbiAgJ0VNUFRZJyxcbiAgJ0VORCcsXG4gICdFU0NBUEUnLFxuICAnRVhDRVBUSU9OUycsXG4gICdFWENMVVNJVkUnLFxuICAnRVhFQ1VURScsXG4gICdFWElTVFMnLFxuICAnRVhJVCcsXG4gICdFWFRFTkRTJyxcbiAgJ0VYVEVSTkFMJyxcbiAgJ0VYVFJBQ1QnLFxuICAnRkFMU0UnLFxuICAnRkVUQ0gnLFxuICAnRklOQUwnLFxuICAnRklSU1QnLFxuICAnRklYRUQnLFxuICAnRkxPQVQnLFxuICAnRk9SJyxcbiAgJ0ZPUkFMTCcsXG4gICdGT1JDRScsXG4gICdGUk9NJyxcbiAgJ0ZVTkNUSU9OJyxcbiAgJ0dFTkVSQUwnLFxuICAnR09UTycsXG4gICdHUkFOVCcsXG4gICdHUk9VUCcsXG4gICdIQVNIJyxcbiAgJ0hFQVAnLFxuICAnSElEREVOJyxcbiAgJ0hPVVInLFxuICAnSURFTlRJRklFRCcsXG4gICdJRicsXG4gICdJTU1FRElBVEUnLFxuICAnSU4nLFxuICAnSU5DTFVESU5HJyxcbiAgJ0lOREVYJyxcbiAgJ0lOREVYRVMnLFxuICAnSU5ESUNBVE9SJyxcbiAgJ0lORElDRVMnLFxuICAnSU5GSU5JVEUnLFxuICAnSU5TVEFOVElBQkxFJyxcbiAgJ0lOVCcsXG4gICdJTlRFR0VSJyxcbiAgJ0lOVEVSRkFDRScsXG4gICdJTlRFUlZBTCcsXG4gICdJTlRPJyxcbiAgJ0lOVkFMSURBVEUnLFxuICAnSVMnLFxuICAnSVNPTEFUSU9OJyxcbiAgJ0pBVkEnLFxuICAnTEFOR1VBR0UnLFxuICAnTEFSR0UnLFxuICAnTEVBRElORycsXG4gICdMRU5HVEgnLFxuICAnTEVWRUwnLFxuICAnTElCUkFSWScsXG4gICdMSUtFJyxcbiAgJ0xJS0UyJyxcbiAgJ0xJS0U0JyxcbiAgJ0xJS0VDJyxcbiAgJ0xJTUlURUQnLFxuICAnTE9DQUwnLFxuICAnTE9DSycsXG4gICdMT05HJyxcbiAgJ01BUCcsXG4gICdNQVgnLFxuICAnTUFYTEVOJyxcbiAgJ01FTUJFUicsXG4gICdNRVJHRScsXG4gICdNSU4nLFxuICAnTUlOVVRFJyxcbiAgJ01MU0xBQkVMJyxcbiAgJ01PRCcsXG4gICdNT0RFJyxcbiAgJ01PTlRIJyxcbiAgJ01VTFRJU0VUJyxcbiAgJ05BTUUnLFxuICAnTkFOJyxcbiAgJ05BVElPTkFMJyxcbiAgJ05BVElWRScsXG4gICdOQVRVUkFMJyxcbiAgJ05BVFVSQUxOJyxcbiAgJ05DSEFSJyxcbiAgJ05FVycsXG4gICdORVhUVkFMJyxcbiAgJ05PQ09NUFJFU1MnLFxuICAnTk9DT1BZJyxcbiAgJ05PVCcsXG4gICdOT1dBSVQnLFxuICAnTlVMTCcsXG4gICdOVUxMSUYnLFxuICAnTlVNQkVSX0JBU0UnLFxuICAnTlVNQkVSJyxcbiAgJ09CSkVDVCcsXG4gICdPQ0lDT0xMJyxcbiAgJ09DSURBVEUnLFxuICAnT0NJREFURVRJTUUnLFxuICAnT0NJRFVSQVRJT04nLFxuICAnT0NJSU5URVJWQUwnLFxuICAnT0NJTE9CTE9DQVRPUicsXG4gICdPQ0lOVU1CRVInLFxuICAnT0NJUkFXJyxcbiAgJ09DSVJFRicsXG4gICdPQ0lSRUZDVVJTT1InLFxuICAnT0NJUk9XSUQnLFxuICAnT0NJU1RSSU5HJyxcbiAgJ09DSVRZUEUnLFxuICAnT0YnLFxuICAnT0xEJyxcbiAgJ09OJyxcbiAgJ09OTFknLFxuICAnT1BBUVVFJyxcbiAgJ09QRU4nLFxuICAnT1BFUkFUT1InLFxuICAnT1BUSU9OJyxcbiAgJ09SQUNMRScsXG4gICdPUkFEQVRBJyxcbiAgJ09SREVSJyxcbiAgJ09SR0FOSVpBVElPTicsXG4gICdPUkxBTlknLFxuICAnT1JMVkFSWScsXG4gICdPVEhFUlMnLFxuICAnT1VUJyxcbiAgJ09WRVJMQVBTJyxcbiAgJ09WRVJSSURJTkcnLFxuICAnUEFDS0FHRScsXG4gICdQQVJBTExFTF9FTkFCTEUnLFxuICAnUEFSQU1FVEVSJyxcbiAgJ1BBUkFNRVRFUlMnLFxuICAnUEFSRU5UJyxcbiAgJ1BBUlRJVElPTicsXG4gICdQQVNDQUwnLFxuICAnUENURlJFRScsXG4gICdQSVBFJyxcbiAgJ1BJUEVMSU5FRCcsXG4gICdQTFNfSU5URUdFUicsXG4gICdQTFVHR0FCTEUnLFxuICAnUE9TSVRJVkUnLFxuICAnUE9TSVRJVkVOJyxcbiAgJ1BSQUdNQScsXG4gICdQUkVDSVNJT04nLFxuICAnUFJJT1InLFxuICAnUFJJVkFURScsXG4gICdQUk9DRURVUkUnLFxuICAnUFVCTElDJyxcbiAgJ1JBSVNFJyxcbiAgJ1JBTkdFJyxcbiAgJ1JBVycsXG4gICdSRUFEJyxcbiAgJ1JFQUwnLFxuICAnUkVDT1JEJyxcbiAgJ1JFRicsXG4gICdSRUZFUkVOQ0UnLFxuICAnUkVMRUFTRScsXG4gICdSRUxJRVNfT04nLFxuICAnUkVNJyxcbiAgJ1JFTUFJTkRFUicsXG4gICdSRU5BTUUnLFxuICAnUkVTT1VSQ0UnLFxuICAnUkVTVUxUX0NBQ0hFJyxcbiAgJ1JFU1VMVCcsXG4gICdSRVRVUk4nLFxuICAnUkVUVVJOSU5HJyxcbiAgJ1JFVkVSU0UnLFxuICAnUkVWT0tFJyxcbiAgJ1JPTExCQUNLJyxcbiAgJ1JPVycsXG4gICdST1dJRCcsXG4gICdST1dOVU0nLFxuICAnUk9XVFlQRScsXG4gICdTQU1QTEUnLFxuICAnU0FWRScsXG4gICdTQVZFUE9JTlQnLFxuICAnU0IxJyxcbiAgJ1NCMicsXG4gICdTQjQnLFxuICAnU0VBUkNIJyxcbiAgJ1NFQ09ORCcsXG4gICdTRUdNRU5UJyxcbiAgJ1NFTEYnLFxuICAnU0VQQVJBVEUnLFxuICAnU0VRVUVOQ0UnLFxuICAnU0VSSUFMSVpBQkxFJyxcbiAgJ1NIQVJFJyxcbiAgJ1NIT1JUJyxcbiAgJ1NJWkVfVCcsXG4gICdTSVpFJyxcbiAgJ1NNQUxMSU5UJyxcbiAgJ1NPTUUnLFxuICAnU1BBQ0UnLFxuICAnU1BBUlNFJyxcbiAgJ1NRTCcsXG4gICdTUUxDT0RFJyxcbiAgJ1NRTERBVEEnLFxuICAnU1FMRVJSTScsXG4gICdTUUxOQU1FJyxcbiAgJ1NRTFNUQVRFJyxcbiAgJ1NUQU5EQVJEJyxcbiAgJ1NUQVJUJyxcbiAgJ1NUQVRJQycsXG4gICdTVERERVYnLFxuICAnU1RPUkVEJyxcbiAgJ1NUUklORycsXG4gICdTVFJVQ1QnLFxuICAnU1RZTEUnLFxuICAnU1VCTVVMVElTRVQnLFxuICAnU1VCUEFSVElUSU9OJyxcbiAgJ1NVQlNUSVRVVEFCTEUnLFxuICAnU1VCVFlQRScsXG4gICdTVUNDRVNTRlVMJyxcbiAgJ1NVTScsXG4gICdTWU5PTllNJyxcbiAgJ1NZU0RBVEUnLFxuICAnVEFCQVVUSCcsXG4gICdUQUJMRScsXG4gICdURE8nLFxuICAnVEhFJyxcbiAgJ1RIRU4nLFxuICAnVElNRScsXG4gICdUSU1FU1RBTVAnLFxuICAnVElNRVpPTkVfQUJCUicsXG4gICdUSU1FWk9ORV9IT1VSJyxcbiAgJ1RJTUVaT05FX01JTlVURScsXG4gICdUSU1FWk9ORV9SRUdJT04nLFxuICAnVE8nLFxuICAnVFJBSUxJTkcnLFxuICAnVFJBTlNBQ1RJT04nLFxuICAnVFJBTlNBQ1RJT05BTCcsXG4gICdUUklHR0VSJyxcbiAgJ1RSVUUnLFxuICAnVFJVU1RFRCcsXG4gICdUWVBFJyxcbiAgJ1VCMScsXG4gICdVQjInLFxuICAnVUI0JyxcbiAgJ1VJRCcsXG4gICdVTkRFUicsXG4gICdVTklRVUUnLFxuICAnVU5QTFVHJyxcbiAgJ1VOU0lHTkVEJyxcbiAgJ1VOVFJVU1RFRCcsXG4gICdVU0UnLFxuICAnVVNFUicsXG4gICdVU0lORycsXG4gICdWQUxJREFURScsXG4gICdWQUxJU1QnLFxuICAnVkFMVUUnLFxuICAnVkFSQ0hBUicsXG4gICdWQVJDSEFSMicsXG4gICdWQVJJQUJMRScsXG4gICdWQVJJQU5DRScsXG4gICdWQVJSQVknLFxuICAnVkFSWUlORycsXG4gICdWSUVXJyxcbiAgJ1ZJRVdTJyxcbiAgJ1ZPSUQnLFxuICAnV0hFTkVWRVInLFxuICAnV0hJTEUnLFxuICAnV0lUSCcsXG4gICdXT1JLJyxcbiAgJ1dSQVBQRUQnLFxuICAnV1JJVEUnLFxuICAnWUVBUicsXG4gICdaT05FJyxcbl07XG5cbmNvbnN0IHJlc2VydmVkVG9wTGV2ZWxXb3JkcyA9IFtcbiAgJ0FERCcsXG4gICdBTFRFUiBDT0xVTU4nLFxuICAnQUxURVIgVEFCTEUnLFxuICAnQkVHSU4nLFxuICAnQ09OTkVDVCBCWScsXG4gICdERUNMQVJFJyxcbiAgJ0RFTEVURSBGUk9NJyxcbiAgJ0RFTEVURScsXG4gICdFTkQnLFxuICAnRVhDRVBUJyxcbiAgJ0VYQ0VQVElPTicsXG4gICdGRVRDSCBGSVJTVCcsXG4gICdGUk9NJyxcbiAgJ0dST1VQIEJZJyxcbiAgJ0hBVklORycsXG4gICdJTlNFUlQgSU5UTycsXG4gICdJTlNFUlQnLFxuICAnTElNSVQnLFxuICAnTE9PUCcsXG4gICdNT0RJRlknLFxuICAnT1JERVIgQlknLFxuICAnU0VMRUNUJyxcbiAgJ1NFVCBDVVJSRU5UIFNDSEVNQScsXG4gICdTRVQgU0NIRU1BJyxcbiAgJ1NFVCcsXG4gICdTVEFSVCBXSVRIJyxcbiAgJ1VQREFURScsXG4gICdWQUxVRVMnLFxuICAnV0hFUkUnLFxuXTtcblxuY29uc3QgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzTm9JbmRlbnQgPSBbJ0lOVEVSU0VDVCcsICdJTlRFUlNFQ1QgQUxMJywgJ01JTlVTJywgJ1VOSU9OJywgJ1VOSU9OIEFMTCddO1xuXG5jb25zdCByZXNlcnZlZE5ld2xpbmVXb3JkcyA9IFtcbiAgJ0FORCcsXG4gICdDUk9TUyBBUFBMWScsXG4gICdFTFNFJyxcbiAgJ0VORCcsXG4gICdPUicsXG4gICdPVVRFUiBBUFBMWScsXG4gICdXSEVOJyxcbiAgJ1hPUicsXG4gIC8vIGpvaW5zXG4gICdKT0lOJyxcbiAgJ0lOTkVSIEpPSU4nLFxuICAnTEVGVCBKT0lOJyxcbiAgJ0xFRlQgT1VURVIgSk9JTicsXG4gICdSSUdIVCBKT0lOJyxcbiAgJ1JJR0hUIE9VVEVSIEpPSU4nLFxuICAnRlVMTCBKT0lOJyxcbiAgJ0ZVTEwgT1VURVIgSk9JTicsXG4gICdDUk9TUyBKT0lOJyxcbiAgJ05BVFVSQUwgSk9JTicsXG5dO1xuXG5leHBvcnQgZGVmYXVsdCBjbGFzcyBQbFNxbEZvcm1hdHRlciBleHRlbmRzIEZvcm1hdHRlciB7XG4gIHRva2VuaXplcigpIHtcbiAgICByZXR1cm4gbmV3IFRva2VuaXplcih7XG4gICAgICByZXNlcnZlZFdvcmRzLFxuICAgICAgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzLFxuICAgICAgcmVzZXJ2ZWROZXdsaW5lV29yZHMsXG4gICAgICByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCxcbiAgICAgIHN0cmluZ1R5cGVzOiBbYFwiXCJgLCBcIk4nJ1wiLCBcIicnXCIsICdgYCddLFxuICAgICAgb3BlblBhcmVuczogWycoJywgJ0NBU0UnXSxcbiAgICAgIGNsb3NlUGFyZW5zOiBbJyknLCAnRU5EJ10sXG4gICAgICBpbmRleGVkUGxhY2Vob2xkZXJUeXBlczogWyc/J10sXG4gICAgICBuYW1lZFBsYWNlaG9sZGVyVHlwZXM6IFsnOiddLFxuICAgICAgbGluZUNvbW1lbnRUeXBlczogWyctLSddLFxuICAgICAgc3BlY2lhbFdvcmRDaGFyczogWydfJywgJyQnLCAnIycsICcuJywgJ0AnXSxcbiAgICAgIG9wZXJhdG9yczogWyd8fCcsICcqKicsICchPScsICc6PSddLFxuICAgIH0pO1xuICB9XG5cbiAgdG9rZW5PdmVycmlkZSh0b2tlbikge1xuICAgIGlmIChpc1NldCh0b2tlbikgJiYgaXNCeSh0aGlzLnByZXZpb3VzUmVzZXJ2ZWRUb2tlbikpIHtcbiAgICAgIHJldHVybiB7IHR5cGU6IHRva2VuVHlwZXMuUkVTRVJWRUQsIHZhbHVlOiB0b2tlbi52YWx1ZSB9O1xuICAgIH1cbiAgICByZXR1cm4gdG9rZW47XG4gIH1cbn1cbiIsImltcG9ydCBGb3JtYXR0ZXIgZnJvbSAnLi4vY29yZS9Gb3JtYXR0ZXInO1xuaW1wb3J0IFRva2VuaXplciBmcm9tICcuLi9jb3JlL1Rva2VuaXplcic7XG5cbmNvbnN0IHJlc2VydmVkV29yZHMgPSBbXG4gICdBQk9SVCcsXG4gICdBQlNPTFVURScsXG4gICdBQ0NFU1MnLFxuICAnQUNUSU9OJyxcbiAgJ0FERCcsXG4gICdBRE1JTicsXG4gICdBRlRFUicsXG4gICdBR0dSRUdBVEUnLFxuICAnQUxMJyxcbiAgJ0FMU08nLFxuICAnQUxURVInLFxuICAnQUxXQVlTJyxcbiAgJ0FOQUxZU0UnLFxuICAnQU5BTFlaRScsXG4gICdBTkQnLFxuICAnQU5ZJyxcbiAgJ0FSUkFZJyxcbiAgJ0FTJyxcbiAgJ0FTQycsXG4gICdBU1NFUlRJT04nLFxuICAnQVNTSUdOTUVOVCcsXG4gICdBU1lNTUVUUklDJyxcbiAgJ0FUJyxcbiAgJ0FUVEFDSCcsXG4gICdBVFRSSUJVVEUnLFxuICAnQVVUSE9SSVpBVElPTicsXG4gICdCQUNLV0FSRCcsXG4gICdCRUZPUkUnLFxuICAnQkVHSU4nLFxuICAnQkVUV0VFTicsXG4gICdCSUdJTlQnLFxuICAnQklOQVJZJyxcbiAgJ0JJVCcsXG4gICdCT09MRUFOJyxcbiAgJ0JPVEgnLFxuICAnQlknLFxuICAnQ0FDSEUnLFxuICAnQ0FMTCcsXG4gICdDQUxMRUQnLFxuICAnQ0FTQ0FERScsXG4gICdDQVNDQURFRCcsXG4gICdDQVNFJyxcbiAgJ0NBU1QnLFxuICAnQ0FUQUxPRycsXG4gICdDSEFJTicsXG4gICdDSEFSJyxcbiAgJ0NIQVJBQ1RFUicsXG4gICdDSEFSQUNURVJJU1RJQ1MnLFxuICAnQ0hFQ0snLFxuICAnQ0hFQ0tQT0lOVCcsXG4gICdDTEFTUycsXG4gICdDTE9TRScsXG4gICdDTFVTVEVSJyxcbiAgJ0NPQUxFU0NFJyxcbiAgJ0NPTExBVEUnLFxuICAnQ09MTEFUSU9OJyxcbiAgJ0NPTFVNTicsXG4gICdDT0xVTU5TJyxcbiAgJ0NPTU1FTlQnLFxuICAnQ09NTUVOVFMnLFxuICAnQ09NTUlUJyxcbiAgJ0NPTU1JVFRFRCcsXG4gICdDT05DVVJSRU5UTFknLFxuICAnQ09ORklHVVJBVElPTicsXG4gICdDT05GTElDVCcsXG4gICdDT05ORUNUSU9OJyxcbiAgJ0NPTlNUUkFJTlQnLFxuICAnQ09OU1RSQUlOVFMnLFxuICAnQ09OVEVOVCcsXG4gICdDT05USU5VRScsXG4gICdDT05WRVJTSU9OJyxcbiAgJ0NPUFknLFxuICAnQ09TVCcsXG4gICdDUkVBVEUnLFxuICAnQ1JPU1MnLFxuICAnQ1NWJyxcbiAgJ0NVQkUnLFxuICAnQ1VSUkVOVCcsXG4gICdDVVJSRU5UX0NBVEFMT0cnLFxuICAnQ1VSUkVOVF9EQVRFJyxcbiAgJ0NVUlJFTlRfUk9MRScsXG4gICdDVVJSRU5UX1NDSEVNQScsXG4gICdDVVJSRU5UX1RJTUUnLFxuICAnQ1VSUkVOVF9USU1FU1RBTVAnLFxuICAnQ1VSUkVOVF9VU0VSJyxcbiAgJ0NVUlNPUicsXG4gICdDWUNMRScsXG4gICdEQVRBJyxcbiAgJ0RBVEFCQVNFJyxcbiAgJ0RBWScsXG4gICdERUFMTE9DQVRFJyxcbiAgJ0RFQycsXG4gICdERUNJTUFMJyxcbiAgJ0RFQ0xBUkUnLFxuICAnREVGQVVMVCcsXG4gICdERUZBVUxUUycsXG4gICdERUZFUlJBQkxFJyxcbiAgJ0RFRkVSUkVEJyxcbiAgJ0RFRklORVInLFxuICAnREVMRVRFJyxcbiAgJ0RFTElNSVRFUicsXG4gICdERUxJTUlURVJTJyxcbiAgJ0RFUEVORFMnLFxuICAnREVTQycsXG4gICdERVRBQ0gnLFxuICAnRElDVElPTkFSWScsXG4gICdESVNBQkxFJyxcbiAgJ0RJU0NBUkQnLFxuICAnRElTVElOQ1QnLFxuICAnRE8nLFxuICAnRE9DVU1FTlQnLFxuICAnRE9NQUlOJyxcbiAgJ0RPVUJMRScsXG4gICdEUk9QJyxcbiAgJ0VBQ0gnLFxuICAnRUxTRScsXG4gICdFTkFCTEUnLFxuICAnRU5DT0RJTkcnLFxuICAnRU5DUllQVEVEJyxcbiAgJ0VORCcsXG4gICdFTlVNJyxcbiAgJ0VTQ0FQRScsXG4gICdFVkVOVCcsXG4gICdFWENFUFQnLFxuICAnRVhDTFVERScsXG4gICdFWENMVURJTkcnLFxuICAnRVhDTFVTSVZFJyxcbiAgJ0VYRUNVVEUnLFxuICAnRVhJU1RTJyxcbiAgJ0VYUExBSU4nLFxuICAnRVhQUkVTU0lPTicsXG4gICdFWFRFTlNJT04nLFxuICAnRVhURVJOQUwnLFxuICAnRVhUUkFDVCcsXG4gICdGQUxTRScsXG4gICdGQU1JTFknLFxuICAnRkVUQ0gnLFxuICAnRklMVEVSJyxcbiAgJ0ZJUlNUJyxcbiAgJ0ZMT0FUJyxcbiAgJ0ZPTExPV0lORycsXG4gICdGT1InLFxuICAnRk9SQ0UnLFxuICAnRk9SRUlHTicsXG4gICdGT1JXQVJEJyxcbiAgJ0ZSRUVaRScsXG4gICdGUk9NJyxcbiAgJ0ZVTEwnLFxuICAnRlVOQ1RJT04nLFxuICAnRlVOQ1RJT05TJyxcbiAgJ0dFTkVSQVRFRCcsXG4gICdHTE9CQUwnLFxuICAnR1JBTlQnLFxuICAnR1JBTlRFRCcsXG4gICdHUkVBVEVTVCcsXG4gICdHUk9VUCcsXG4gICdHUk9VUElORycsXG4gICdHUk9VUFMnLFxuICAnSEFORExFUicsXG4gICdIQVZJTkcnLFxuICAnSEVBREVSJyxcbiAgJ0hPTEQnLFxuICAnSE9VUicsXG4gICdJREVOVElUWScsXG4gICdJRicsXG4gICdJTElLRScsXG4gICdJTU1FRElBVEUnLFxuICAnSU1NVVRBQkxFJyxcbiAgJ0lNUExJQ0lUJyxcbiAgJ0lNUE9SVCcsXG4gICdJTicsXG4gICdJTkNMVURFJyxcbiAgJ0lOQ0xVRElORycsXG4gICdJTkNSRU1FTlQnLFxuICAnSU5ERVgnLFxuICAnSU5ERVhFUycsXG4gICdJTkhFUklUJyxcbiAgJ0lOSEVSSVRTJyxcbiAgJ0lOSVRJQUxMWScsXG4gICdJTkxJTkUnLFxuICAnSU5ORVInLFxuICAnSU5PVVQnLFxuICAnSU5QVVQnLFxuICAnSU5TRU5TSVRJVkUnLFxuICAnSU5TRVJUJyxcbiAgJ0lOU1RFQUQnLFxuICAnSU5UJyxcbiAgJ0lOVEVHRVInLFxuICAnSU5URVJTRUNUJyxcbiAgJ0lOVEVSVkFMJyxcbiAgJ0lOVE8nLFxuICAnSU5WT0tFUicsXG4gICdJUycsXG4gICdJU05VTEwnLFxuICAnSVNPTEFUSU9OJyxcbiAgJ0pPSU4nLFxuICAnS0VZJyxcbiAgJ0xBQkVMJyxcbiAgJ0xBTkdVQUdFJyxcbiAgJ0xBUkdFJyxcbiAgJ0xBU1QnLFxuICAnTEFURVJBTCcsXG4gICdMRUFESU5HJyxcbiAgJ0xFQUtQUk9PRicsXG4gICdMRUFTVCcsXG4gICdMRUZUJyxcbiAgJ0xFVkVMJyxcbiAgJ0xJS0UnLFxuICAnTElNSVQnLFxuICAnTElTVEVOJyxcbiAgJ0xPQUQnLFxuICAnTE9DQUwnLFxuICAnTE9DQUxUSU1FJyxcbiAgJ0xPQ0FMVElNRVNUQU1QJyxcbiAgJ0xPQ0FUSU9OJyxcbiAgJ0xPQ0snLFxuICAnTE9DS0VEJyxcbiAgJ0xPR0dFRCcsXG4gICdNQVBQSU5HJyxcbiAgJ01BVENIJyxcbiAgJ01BVEVSSUFMSVpFRCcsXG4gICdNQVhWQUxVRScsXG4gICdNRVRIT0QnLFxuICAnTUlOVVRFJyxcbiAgJ01JTlZBTFVFJyxcbiAgJ01PREUnLFxuICAnTU9OVEgnLFxuICAnTU9WRScsXG4gICdOQU1FJyxcbiAgJ05BTUVTJyxcbiAgJ05BVElPTkFMJyxcbiAgJ05BVFVSQUwnLFxuICAnTkNIQVInLFxuICAnTkVXJyxcbiAgJ05FWFQnLFxuICAnTkZDJyxcbiAgJ05GRCcsXG4gICdORktDJyxcbiAgJ05GS0QnLFxuICAnTk8nLFxuICAnTk9ORScsXG4gICdOT1JNQUxJWkUnLFxuICAnTk9STUFMSVpFRCcsXG4gICdOT1QnLFxuICAnTk9USElORycsXG4gICdOT1RJRlknLFxuICAnTk9UTlVMTCcsXG4gICdOT1dBSVQnLFxuICAnTlVMTCcsXG4gICdOVUxMSUYnLFxuICAnTlVMTFMnLFxuICAnTlVNRVJJQycsXG4gICdPQkpFQ1QnLFxuICAnT0YnLFxuICAnT0ZGJyxcbiAgJ09GRlNFVCcsXG4gICdPSURTJyxcbiAgJ09MRCcsXG4gICdPTicsXG4gICdPTkxZJyxcbiAgJ09QRVJBVE9SJyxcbiAgJ09QVElPTicsXG4gICdPUFRJT05TJyxcbiAgJ09SJyxcbiAgJ09SREVSJyxcbiAgJ09SRElOQUxJVFknLFxuICAnT1RIRVJTJyxcbiAgJ09VVCcsXG4gICdPVVRFUicsXG4gICdPVkVSJyxcbiAgJ09WRVJMQVBTJyxcbiAgJ09WRVJMQVknLFxuICAnT1ZFUlJJRElORycsXG4gICdPV05FRCcsXG4gICdPV05FUicsXG4gICdQQVJBTExFTCcsXG4gICdQQVJTRVInLFxuICAnUEFSVElBTCcsXG4gICdQQVJUSVRJT04nLFxuICAnUEFTU0lORycsXG4gICdQQVNTV09SRCcsXG4gICdQTEFDSU5HJyxcbiAgJ1BMQU5TJyxcbiAgJ1BPTElDWScsXG4gICdQT1NJVElPTicsXG4gICdQUkVDRURJTkcnLFxuICAnUFJFQ0lTSU9OJyxcbiAgJ1BSRVBBUkUnLFxuICAnUFJFUEFSRUQnLFxuICAnUFJFU0VSVkUnLFxuICAnUFJJTUFSWScsXG4gICdQUklPUicsXG4gICdQUklWSUxFR0VTJyxcbiAgJ1BST0NFRFVSQUwnLFxuICAnUFJPQ0VEVVJFJyxcbiAgJ1BST0NFRFVSRVMnLFxuICAnUFJPR1JBTScsXG4gICdQVUJMSUNBVElPTicsXG4gICdRVU9URScsXG4gICdSQU5HRScsXG4gICdSRUFEJyxcbiAgJ1JFQUwnLFxuICAnUkVBU1NJR04nLFxuICAnUkVDSEVDSycsXG4gICdSRUNVUlNJVkUnLFxuICAnUkVGJyxcbiAgJ1JFRkVSRU5DRVMnLFxuICAnUkVGRVJFTkNJTkcnLFxuICAnUkVGUkVTSCcsXG4gICdSRUlOREVYJyxcbiAgJ1JFTEFUSVZFJyxcbiAgJ1JFTEVBU0UnLFxuICAnUkVOQU1FJyxcbiAgJ1JFUEVBVEFCTEUnLFxuICAnUkVQTEFDRScsXG4gICdSRVBMSUNBJyxcbiAgJ1JFU0VUJyxcbiAgJ1JFU1RBUlQnLFxuICAnUkVTVFJJQ1QnLFxuICAnUkVUVVJOSU5HJyxcbiAgJ1JFVFVSTlMnLFxuICAnUkVWT0tFJyxcbiAgJ1JJR0hUJyxcbiAgJ1JPTEUnLFxuICAnUk9MTEJBQ0snLFxuICAnUk9MTFVQJyxcbiAgJ1JPVVRJTkUnLFxuICAnUk9VVElORVMnLFxuICAnUk9XJyxcbiAgJ1JPV1MnLFxuICAnUlVMRScsXG4gICdTQVZFUE9JTlQnLFxuICAnU0NIRU1BJyxcbiAgJ1NDSEVNQVMnLFxuICAnU0NST0xMJyxcbiAgJ1NFQVJDSCcsXG4gICdTRUNPTkQnLFxuICAnU0VDVVJJVFknLFxuICAnU0VMRUNUJyxcbiAgJ1NFUVVFTkNFJyxcbiAgJ1NFUVVFTkNFUycsXG4gICdTRVJJQUxJWkFCTEUnLFxuICAnU0VSVkVSJyxcbiAgJ1NFU1NJT04nLFxuICAnU0VTU0lPTl9VU0VSJyxcbiAgJ1NFVCcsXG4gICdTRVRPRicsXG4gICdTRVRTJyxcbiAgJ1NIQVJFJyxcbiAgJ1NIT1cnLFxuICAnU0lNSUxBUicsXG4gICdTSU1QTEUnLFxuICAnU0tJUCcsXG4gICdTTUFMTElOVCcsXG4gICdTTkFQU0hPVCcsXG4gICdTT01FJyxcbiAgJ1NRTCcsXG4gICdTVEFCTEUnLFxuICAnU1RBTkRBTE9ORScsXG4gICdTVEFSVCcsXG4gICdTVEFURU1FTlQnLFxuICAnU1RBVElTVElDUycsXG4gICdTVERJTicsXG4gICdTVERPVVQnLFxuICAnU1RPUkFHRScsXG4gICdTVE9SRUQnLFxuICAnU1RSSUNUJyxcbiAgJ1NUUklQJyxcbiAgJ1NVQlNDUklQVElPTicsXG4gICdTVUJTVFJJTkcnLFxuICAnU1VQUE9SVCcsXG4gICdTWU1NRVRSSUMnLFxuICAnU1lTSUQnLFxuICAnU1lTVEVNJyxcbiAgJ1RBQkxFJyxcbiAgJ1RBQkxFUycsXG4gICdUQUJMRVNBTVBMRScsXG4gICdUQUJMRVNQQUNFJyxcbiAgJ1RFTVAnLFxuICAnVEVNUExBVEUnLFxuICAnVEVNUE9SQVJZJyxcbiAgJ1RFWFQnLFxuICAnVEhFTicsXG4gICdUSUVTJyxcbiAgJ1RJTUUnLFxuICAnVElNRVNUQU1QJyxcbiAgJ1RPJyxcbiAgJ1RSQUlMSU5HJyxcbiAgJ1RSQU5TQUNUSU9OJyxcbiAgJ1RSQU5TRk9STScsXG4gICdUUkVBVCcsXG4gICdUUklHR0VSJyxcbiAgJ1RSSU0nLFxuICAnVFJVRScsXG4gICdUUlVOQ0FURScsXG4gICdUUlVTVEVEJyxcbiAgJ1RZUEUnLFxuICAnVFlQRVMnLFxuICAnVUVTQ0FQRScsXG4gICdVTkJPVU5ERUQnLFxuICAnVU5DT01NSVRURUQnLFxuICAnVU5FTkNSWVBURUQnLFxuICAnVU5JT04nLFxuICAnVU5JUVVFJyxcbiAgJ1VOS05PV04nLFxuICAnVU5MSVNURU4nLFxuICAnVU5MT0dHRUQnLFxuICAnVU5USUwnLFxuICAnVVBEQVRFJyxcbiAgJ1VTRVInLFxuICAnVVNJTkcnLFxuICAnVkFDVVVNJyxcbiAgJ1ZBTElEJyxcbiAgJ1ZBTElEQVRFJyxcbiAgJ1ZBTElEQVRPUicsXG4gICdWQUxVRScsXG4gICdWQUxVRVMnLFxuICAnVkFSQ0hBUicsXG4gICdWQVJJQURJQycsXG4gICdWQVJZSU5HJyxcbiAgJ1ZFUkJPU0UnLFxuICAnVkVSU0lPTicsXG4gICdWSUVXJyxcbiAgJ1ZJRVdTJyxcbiAgJ1ZPTEFUSUxFJyxcbiAgJ1dIRU4nLFxuICAnV0hFUkUnLFxuICAnV0hJVEVTUEFDRScsXG4gICdXSU5ET1cnLFxuICAnV0lUSCcsXG4gICdXSVRISU4nLFxuICAnV0lUSE9VVCcsXG4gICdXT1JLJyxcbiAgJ1dSQVBQRVInLFxuICAnV1JJVEUnLFxuICAnWE1MJyxcbiAgJ1hNTEFUVFJJQlVURVMnLFxuICAnWE1MQ09OQ0FUJyxcbiAgJ1hNTEVMRU1FTlQnLFxuICAnWE1MRVhJU1RTJyxcbiAgJ1hNTEZPUkVTVCcsXG4gICdYTUxOQU1FU1BBQ0VTJyxcbiAgJ1hNTFBBUlNFJyxcbiAgJ1hNTFBJJyxcbiAgJ1hNTFJPT1QnLFxuICAnWE1MU0VSSUFMSVpFJyxcbiAgJ1hNTFRBQkxFJyxcbiAgJ1lFQVInLFxuICAnWUVTJyxcbiAgJ1pPTkUnLFxuXTtcblxuY29uc3QgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzID0gW1xuICAnQUREJyxcbiAgJ0FGVEVSJyxcbiAgJ0FMVEVSIENPTFVNTicsXG4gICdBTFRFUiBUQUJMRScsXG4gICdDQVNFJyxcbiAgJ0RFTEVURSBGUk9NJyxcbiAgJ0VORCcsXG4gICdFWENFUFQnLFxuICAnRkVUQ0ggRklSU1QnLFxuICAnRlJPTScsXG4gICdHUk9VUCBCWScsXG4gICdIQVZJTkcnLFxuICAnSU5TRVJUIElOVE8nLFxuICAnSU5TRVJUJyxcbiAgJ0xJTUlUJyxcbiAgJ09SREVSIEJZJyxcbiAgJ1NFTEVDVCcsXG4gICdTRVQgQ1VSUkVOVCBTQ0hFTUEnLFxuICAnU0VUIFNDSEVNQScsXG4gICdTRVQnLFxuICAnVVBEQVRFJyxcbiAgJ1ZBTFVFUycsXG4gICdXSEVSRScsXG5dO1xuXG5jb25zdCByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCA9IFsnSU5URVJTRUNUJywgJ0lOVEVSU0VDVCBBTEwnLCAnVU5JT04nLCAnVU5JT04gQUxMJ107XG5cbmNvbnN0IHJlc2VydmVkTmV3bGluZVdvcmRzID0gW1xuICAnQU5EJyxcbiAgJ0VMU0UnLFxuICAnT1InLFxuICAnV0hFTicsXG4gIC8vIGpvaW5zXG4gICdKT0lOJyxcbiAgJ0lOTkVSIEpPSU4nLFxuICAnTEVGVCBKT0lOJyxcbiAgJ0xFRlQgT1VURVIgSk9JTicsXG4gICdSSUdIVCBKT0lOJyxcbiAgJ1JJR0hUIE9VVEVSIEpPSU4nLFxuICAnRlVMTCBKT0lOJyxcbiAgJ0ZVTEwgT1VURVIgSk9JTicsXG4gICdDUk9TUyBKT0lOJyxcbiAgJ05BVFVSQUwgSk9JTicsXG5dO1xuXG5leHBvcnQgZGVmYXVsdCBjbGFzcyBQb3N0Z3JlU3FsRm9ybWF0dGVyIGV4dGVuZHMgRm9ybWF0dGVyIHtcbiAgdG9rZW5pemVyKCkge1xuICAgIHJldHVybiBuZXcgVG9rZW5pemVyKHtcbiAgICAgIHJlc2VydmVkV29yZHMsXG4gICAgICByZXNlcnZlZFRvcExldmVsV29yZHMsXG4gICAgICByZXNlcnZlZE5ld2xpbmVXb3JkcyxcbiAgICAgIHJlc2VydmVkVG9wTGV2ZWxXb3Jkc05vSW5kZW50LFxuICAgICAgc3RyaW5nVHlwZXM6IFtgXCJcImAsIFwiJydcIiwgXCJVJicnXCIsICdVJlwiXCInLCAnJCQnXSxcbiAgICAgIG9wZW5QYXJlbnM6IFsnKCcsICdDQVNFJ10sXG4gICAgICBjbG9zZVBhcmVuczogWycpJywgJ0VORCddLFxuICAgICAgaW5kZXhlZFBsYWNlaG9sZGVyVHlwZXM6IFsnJCddLFxuICAgICAgbmFtZWRQbGFjZWhvbGRlclR5cGVzOiBbJzonXSxcbiAgICAgIGxpbmVDb21tZW50VHlwZXM6IFsnLS0nXSxcbiAgICAgIG9wZXJhdG9yczogW1xuICAgICAgICAnIT0nLFxuICAgICAgICAnPDwnLFxuICAgICAgICAnPj4nLFxuICAgICAgICAnfHwvJyxcbiAgICAgICAgJ3wvJyxcbiAgICAgICAgJzo6JyxcbiAgICAgICAgJy0+PicsXG4gICAgICAgICctPicsXG4gICAgICAgICd+fionLFxuICAgICAgICAnfn4nLFxuICAgICAgICAnIX5+KicsXG4gICAgICAgICchfn4nLFxuICAgICAgICAnfionLFxuICAgICAgICAnIX4qJyxcbiAgICAgICAgJyF+JyxcbiAgICAgICAgJyEhJyxcbiAgICAgIF0sXG4gICAgfSk7XG4gIH1cbn1cbiIsImltcG9ydCBGb3JtYXR0ZXIgZnJvbSAnLi4vY29yZS9Gb3JtYXR0ZXInO1xuaW1wb3J0IFRva2VuaXplciBmcm9tICcuLi9jb3JlL1Rva2VuaXplcic7XG5cbmNvbnN0IHJlc2VydmVkV29yZHMgPSBbXG4gICdBRVMxMjgnLFxuICAnQUVTMjU2JyxcbiAgJ0FMTE9XT1ZFUldSSVRFJyxcbiAgJ0FOQUxZU0UnLFxuICAnQVJSQVknLFxuICAnQVMnLFxuICAnQVNDJyxcbiAgJ0FVVEhPUklaQVRJT04nLFxuICAnQkFDS1VQJyxcbiAgJ0JJTkFSWScsXG4gICdCTEFOS1NBU05VTEwnLFxuICAnQk9USCcsXG4gICdCWVRFRElDVCcsXG4gICdCWklQMicsXG4gICdDQVNUJyxcbiAgJ0NIRUNLJyxcbiAgJ0NPTExBVEUnLFxuICAnQ09MVU1OJyxcbiAgJ0NPTlNUUkFJTlQnLFxuICAnQ1JFQVRFJyxcbiAgJ0NSRURFTlRJQUxTJyxcbiAgJ0NVUlJFTlRfREFURScsXG4gICdDVVJSRU5UX1RJTUUnLFxuICAnQ1VSUkVOVF9USU1FU1RBTVAnLFxuICAnQ1VSUkVOVF9VU0VSJyxcbiAgJ0NVUlJFTlRfVVNFUl9JRCcsXG4gICdERUZBVUxUJyxcbiAgJ0RFRkVSUkFCTEUnLFxuICAnREVGTEFURScsXG4gICdERUZSQUcnLFxuICAnREVMVEEnLFxuICAnREVMVEEzMksnLFxuICAnREVTQycsXG4gICdESVNBQkxFJyxcbiAgJ0RJU1RJTkNUJyxcbiAgJ0RPJyxcbiAgJ0VMU0UnLFxuICAnRU1QVFlBU05VTEwnLFxuICAnRU5BQkxFJyxcbiAgJ0VOQ09ERScsXG4gICdFTkNSWVBUJyxcbiAgJ0VOQ1JZUFRJT04nLFxuICAnRU5EJyxcbiAgJ0VYUExJQ0lUJyxcbiAgJ0ZBTFNFJyxcbiAgJ0ZPUicsXG4gICdGT1JFSUdOJyxcbiAgJ0ZSRUVaRScsXG4gICdGVUxMJyxcbiAgJ0dMT0JBTERJQ1QyNTYnLFxuICAnR0xPQkFMRElDVDY0SycsXG4gICdHUkFOVCcsXG4gICdHWklQJyxcbiAgJ0lERU5USVRZJyxcbiAgJ0lHTk9SRScsXG4gICdJTElLRScsXG4gICdJTklUSUFMTFknLFxuICAnSU5UTycsXG4gICdMRUFESU5HJyxcbiAgJ0xPQ0FMVElNRScsXG4gICdMT0NBTFRJTUVTVEFNUCcsXG4gICdMVU4nLFxuICAnTFVOUycsXG4gICdMWk8nLFxuICAnTFpPUCcsXG4gICdNSU5VUycsXG4gICdNT1NUTFkxMycsXG4gICdNT1NUTFkzMicsXG4gICdNT1NUTFk4JyxcbiAgJ05BVFVSQUwnLFxuICAnTkVXJyxcbiAgJ05VTExTJyxcbiAgJ09GRicsXG4gICdPRkZMSU5FJyxcbiAgJ09GRlNFVCcsXG4gICdPTEQnLFxuICAnT04nLFxuICAnT05MWScsXG4gICdPUEVOJyxcbiAgJ09SREVSJyxcbiAgJ09WRVJMQVBTJyxcbiAgJ1BBUkFMTEVMJyxcbiAgJ1BBUlRJVElPTicsXG4gICdQRVJDRU5UJyxcbiAgJ1BFUk1JU1NJT05TJyxcbiAgJ1BMQUNJTkcnLFxuICAnUFJJTUFSWScsXG4gICdSQVcnLFxuICAnUkVBRFJBVElPJyxcbiAgJ1JFQ09WRVInLFxuICAnUkVGRVJFTkNFUycsXG4gICdSRUpFQ1RMT0cnLFxuICAnUkVTT1JUJyxcbiAgJ1JFU1RPUkUnLFxuICAnU0VTU0lPTl9VU0VSJyxcbiAgJ1NJTUlMQVInLFxuICAnU1lTREFURScsXG4gICdTWVNURU0nLFxuICAnVEFCTEUnLFxuICAnVEFHJyxcbiAgJ1RERVMnLFxuICAnVEVYVDI1NScsXG4gICdURVhUMzJLJyxcbiAgJ1RIRU4nLFxuICAnVElNRVNUQU1QJyxcbiAgJ1RPJyxcbiAgJ1RPUCcsXG4gICdUUkFJTElORycsXG4gICdUUlVFJyxcbiAgJ1RSVU5DQVRFQ09MVU1OUycsXG4gICdVTklRVUUnLFxuICAnVVNFUicsXG4gICdVU0lORycsXG4gICdWRVJCT1NFJyxcbiAgJ1dBTExFVCcsXG4gICdXSEVOJyxcbiAgJ1dJVEgnLFxuICAnV0lUSE9VVCcsXG4gICdQUkVESUNBVEUnLFxuICAnQ09MVU1OUycsXG4gICdDT01QUk9XUycsXG4gICdDT01QUkVTU0lPTicsXG4gICdDT1BZJyxcbiAgJ0ZPUk1BVCcsXG4gICdERUxJTUlURVInLFxuICAnRklYRURXSURUSCcsXG4gICdBVlJPJyxcbiAgJ0pTT04nLFxuICAnRU5DUllQVEVEJyxcbiAgJ0JaSVAyJyxcbiAgJ0daSVAnLFxuICAnTFpPUCcsXG4gICdQQVJRVUVUJyxcbiAgJ09SQycsXG4gICdBQ0NFUFRBTllEQVRFJyxcbiAgJ0FDQ0VQVElOVkNIQVJTJyxcbiAgJ0JMQU5LU0FTTlVMTCcsXG4gICdEQVRFRk9STUFUJyxcbiAgJ0VNUFRZQVNOVUxMJyxcbiAgJ0VOQ09ESU5HJyxcbiAgJ0VTQ0FQRScsXG4gICdFWFBMSUNJVF9JRFMnLFxuICAnRklMTFJFQ09SRCcsXG4gICdJR05PUkVCTEFOS0xJTkVTJyxcbiAgJ0lHTk9SRUhFQURFUicsXG4gICdOVUxMIEFTJyxcbiAgJ1JFTU9WRVFVT1RFUycsXG4gICdST1VOREVDJyxcbiAgJ1RJTUVGT1JNQVQnLFxuICAnVFJJTUJMQU5LUycsXG4gICdUUlVOQ0FURUNPTFVNTlMnLFxuICAnQ09NUFJPV1MnLFxuICAnQ09NUFVQREFURScsXG4gICdNQVhFUlJPUicsXG4gICdOT0xPQUQnLFxuICAnU1RBVFVQREFURScsXG4gICdNQU5JRkVTVCcsXG4gICdSRUdJT04nLFxuICAnSUFNX1JPTEUnLFxuICAnTUFTVEVSX1NZTU1FVFJJQ19LRVknLFxuICAnU1NIJyxcbiAgJ0FDQ0VQVEFOWURBVEUnLFxuICAnQUNDRVBUSU5WQ0hBUlMnLFxuICAnQUNDRVNTX0tFWV9JRCcsXG4gICdTRUNSRVRfQUNDRVNTX0tFWScsXG4gICdBVlJPJyxcbiAgJ0JMQU5LU0FTTlVMTCcsXG4gICdCWklQMicsXG4gICdDT01QUk9XUycsXG4gICdDT01QVVBEQVRFJyxcbiAgJ0NSRURFTlRJQUxTJyxcbiAgJ0RBVEVGT1JNQVQnLFxuICAnREVMSU1JVEVSJyxcbiAgJ0VNUFRZQVNOVUxMJyxcbiAgJ0VOQ09ESU5HJyxcbiAgJ0VOQ1JZUFRFRCcsXG4gICdFU0NBUEUnLFxuICAnRVhQTElDSVRfSURTJyxcbiAgJ0ZJTExSRUNPUkQnLFxuICAnRklYRURXSURUSCcsXG4gICdGT1JNQVQnLFxuICAnSUFNX1JPTEUnLFxuICAnR1pJUCcsXG4gICdJR05PUkVCTEFOS0xJTkVTJyxcbiAgJ0lHTk9SRUhFQURFUicsXG4gICdKU09OJyxcbiAgJ0xaT1AnLFxuICAnTUFOSUZFU1QnLFxuICAnTUFTVEVSX1NZTU1FVFJJQ19LRVknLFxuICAnTUFYRVJST1InLFxuICAnTk9MT0FEJyxcbiAgJ05VTEwgQVMnLFxuICAnUkVBRFJBVElPJyxcbiAgJ1JFR0lPTicsXG4gICdSRU1PVkVRVU9URVMnLFxuICAnUk9VTkRFQycsXG4gICdTU0gnLFxuICAnU1RBVFVQREFURScsXG4gICdUSU1FRk9STUFUJyxcbiAgJ1NFU1NJT05fVE9LRU4nLFxuICAnVFJJTUJMQU5LUycsXG4gICdUUlVOQ0FURUNPTFVNTlMnLFxuICAnRVhURVJOQUwnLFxuICAnREFUQSBDQVRBTE9HJyxcbiAgJ0hJVkUgTUVUQVNUT1JFJyxcbiAgJ0NBVEFMT0dfUk9MRScsXG4gICdWQUNVVU0nLFxuICAnQ09QWScsXG4gICdVTkxPQUQnLFxuICAnRVZFTicsXG4gICdBTEwnLFxuXTtcblxuY29uc3QgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzID0gW1xuICAnQUREJyxcbiAgJ0FGVEVSJyxcbiAgJ0FMVEVSIENPTFVNTicsXG4gICdBTFRFUiBUQUJMRScsXG4gICdERUxFVEUgRlJPTScsXG4gICdFWENFUFQnLFxuICAnRlJPTScsXG4gICdHUk9VUCBCWScsXG4gICdIQVZJTkcnLFxuICAnSU5TRVJUIElOVE8nLFxuICAnSU5TRVJUJyxcbiAgJ0lOVEVSU0VDVCcsXG4gICdUT1AnLFxuICAnTElNSVQnLFxuICAnTU9ESUZZJyxcbiAgJ09SREVSIEJZJyxcbiAgJ1NFTEVDVCcsXG4gICdTRVQgQ1VSUkVOVCBTQ0hFTUEnLFxuICAnU0VUIFNDSEVNQScsXG4gICdTRVQnLFxuICAnVU5JT04gQUxMJyxcbiAgJ1VOSU9OJyxcbiAgJ1VQREFURScsXG4gICdWQUxVRVMnLFxuICAnV0hFUkUnLFxuICAnVkFDVVVNJyxcbiAgJ0NPUFknLFxuICAnVU5MT0FEJyxcbiAgJ0FOQUxZWkUnLFxuICAnQU5BTFlTRScsXG4gICdESVNUS0VZJyxcbiAgJ1NPUlRLRVknLFxuICAnQ09NUE9VTkQnLFxuICAnSU5URVJMRUFWRUQnLFxuICAnRk9STUFUJyxcbiAgJ0RFTElNSVRFUicsXG4gICdGSVhFRFdJRFRIJyxcbiAgJ0FWUk8nLFxuICAnSlNPTicsXG4gICdFTkNSWVBURUQnLFxuICAnQlpJUDInLFxuICAnR1pJUCcsXG4gICdMWk9QJyxcbiAgJ1BBUlFVRVQnLFxuICAnT1JDJyxcbiAgJ0FDQ0VQVEFOWURBVEUnLFxuICAnQUNDRVBUSU5WQ0hBUlMnLFxuICAnQkxBTktTQVNOVUxMJyxcbiAgJ0RBVEVGT1JNQVQnLFxuICAnRU1QVFlBU05VTEwnLFxuICAnRU5DT0RJTkcnLFxuICAnRVNDQVBFJyxcbiAgJ0VYUExJQ0lUX0lEUycsXG4gICdGSUxMUkVDT1JEJyxcbiAgJ0lHTk9SRUJMQU5LTElORVMnLFxuICAnSUdOT1JFSEVBREVSJyxcbiAgJ05VTEwgQVMnLFxuICAnUkVNT1ZFUVVPVEVTJyxcbiAgJ1JPVU5ERUMnLFxuICAnVElNRUZPUk1BVCcsXG4gICdUUklNQkxBTktTJyxcbiAgJ1RSVU5DQVRFQ09MVU1OUycsXG4gICdDT01QUk9XUycsXG4gICdDT01QVVBEQVRFJyxcbiAgJ01BWEVSUk9SJyxcbiAgJ05PTE9BRCcsXG4gICdTVEFUVVBEQVRFJyxcbiAgJ01BTklGRVNUJyxcbiAgJ1JFR0lPTicsXG4gICdJQU1fUk9MRScsXG4gICdNQVNURVJfU1lNTUVUUklDX0tFWScsXG4gICdTU0gnLFxuICAnQUNDRVBUQU5ZREFURScsXG4gICdBQ0NFUFRJTlZDSEFSUycsXG4gICdBQ0NFU1NfS0VZX0lEJyxcbiAgJ1NFQ1JFVF9BQ0NFU1NfS0VZJyxcbiAgJ0FWUk8nLFxuICAnQkxBTktTQVNOVUxMJyxcbiAgJ0JaSVAyJyxcbiAgJ0NPTVBST1dTJyxcbiAgJ0NPTVBVUERBVEUnLFxuICAnQ1JFREVOVElBTFMnLFxuICAnREFURUZPUk1BVCcsXG4gICdERUxJTUlURVInLFxuICAnRU1QVFlBU05VTEwnLFxuICAnRU5DT0RJTkcnLFxuICAnRU5DUllQVEVEJyxcbiAgJ0VTQ0FQRScsXG4gICdFWFBMSUNJVF9JRFMnLFxuICAnRklMTFJFQ09SRCcsXG4gICdGSVhFRFdJRFRIJyxcbiAgJ0ZPUk1BVCcsXG4gICdJQU1fUk9MRScsXG4gICdHWklQJyxcbiAgJ0lHTk9SRUJMQU5LTElORVMnLFxuICAnSUdOT1JFSEVBREVSJyxcbiAgJ0pTT04nLFxuICAnTFpPUCcsXG4gICdNQU5JRkVTVCcsXG4gICdNQVNURVJfU1lNTUVUUklDX0tFWScsXG4gICdNQVhFUlJPUicsXG4gICdOT0xPQUQnLFxuICAnTlVMTCBBUycsXG4gICdSRUFEUkFUSU8nLFxuICAnUkVHSU9OJyxcbiAgJ1JFTU9WRVFVT1RFUycsXG4gICdST1VOREVDJyxcbiAgJ1NTSCcsXG4gICdTVEFUVVBEQVRFJyxcbiAgJ1RJTUVGT1JNQVQnLFxuICAnU0VTU0lPTl9UT0tFTicsXG4gICdUUklNQkxBTktTJyxcbiAgJ1RSVU5DQVRFQ09MVU1OUycsXG4gICdFWFRFUk5BTCcsXG4gICdEQVRBIENBVEFMT0cnLFxuICAnSElWRSBNRVRBU1RPUkUnLFxuICAnQ0FUQUxPR19ST0xFJyxcbl07XG5cbmNvbnN0IHJlc2VydmVkVG9wTGV2ZWxXb3Jkc05vSW5kZW50ID0gW107XG5cbmNvbnN0IHJlc2VydmVkTmV3bGluZVdvcmRzID0gW1xuICAnQU5EJyxcbiAgJ0VMU0UnLFxuICAnT1InLFxuICAnT1VURVIgQVBQTFknLFxuICAnV0hFTicsXG4gICdWQUNVVU0nLFxuICAnQ09QWScsXG4gICdVTkxPQUQnLFxuICAnQU5BTFlaRScsXG4gICdBTkFMWVNFJyxcbiAgJ0RJU1RLRVknLFxuICAnU09SVEtFWScsXG4gICdDT01QT1VORCcsXG4gICdJTlRFUkxFQVZFRCcsXG4gIC8vIGpvaW5zXG4gICdKT0lOJyxcbiAgJ0lOTkVSIEpPSU4nLFxuICAnTEVGVCBKT0lOJyxcbiAgJ0xFRlQgT1VURVIgSk9JTicsXG4gICdSSUdIVCBKT0lOJyxcbiAgJ1JJR0hUIE9VVEVSIEpPSU4nLFxuICAnRlVMTCBKT0lOJyxcbiAgJ0ZVTEwgT1VURVIgSk9JTicsXG4gICdDUk9TUyBKT0lOJyxcbiAgJ05BVFVSQUwgSk9JTicsXG5dO1xuXG5leHBvcnQgZGVmYXVsdCBjbGFzcyBSZWRzaGlmdEZvcm1hdHRlciBleHRlbmRzIEZvcm1hdHRlciB7XG4gIHRva2VuaXplcigpIHtcbiAgICByZXR1cm4gbmV3IFRva2VuaXplcih7XG4gICAgICByZXNlcnZlZFdvcmRzLFxuICAgICAgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzLFxuICAgICAgcmVzZXJ2ZWROZXdsaW5lV29yZHMsXG4gICAgICByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCxcbiAgICAgIHN0cmluZ1R5cGVzOiBbYFwiXCJgLCBcIicnXCIsICdgYCddLFxuICAgICAgb3BlblBhcmVuczogWycoJ10sXG4gICAgICBjbG9zZVBhcmVuczogWycpJ10sXG4gICAgICBpbmRleGVkUGxhY2Vob2xkZXJUeXBlczogWyc/J10sXG4gICAgICBuYW1lZFBsYWNlaG9sZGVyVHlwZXM6IFsnQCcsICcjJywgJyQnXSxcbiAgICAgIGxpbmVDb21tZW50VHlwZXM6IFsnLS0nXSxcbiAgICAgIG9wZXJhdG9yczogWyd8LycsICd8fC8nLCAnPDwnLCAnPj4nLCAnIT0nLCAnfHwnXSxcbiAgICB9KTtcbiAgfVxufVxuIiwiaW1wb3J0IEZvcm1hdHRlciBmcm9tICcuLi9jb3JlL0Zvcm1hdHRlcic7XG5pbXBvcnQgeyBpc0VuZCwgaXNXaW5kb3cgfSBmcm9tICcuLi9jb3JlL3Rva2VuJztcbmltcG9ydCBUb2tlbml6ZXIgZnJvbSAnLi4vY29yZS9Ub2tlbml6ZXInO1xuaW1wb3J0IHRva2VuVHlwZXMgZnJvbSAnLi4vY29yZS90b2tlblR5cGVzJztcblxuY29uc3QgcmVzZXJ2ZWRXb3JkcyA9IFtcbiAgJ0FMTCcsXG4gICdBTFRFUicsXG4gICdBTkFMWVNFJyxcbiAgJ0FOQUxZWkUnLFxuICAnQVJSQVlfWklQJyxcbiAgJ0FSUkFZJyxcbiAgJ0FTJyxcbiAgJ0FTQycsXG4gICdBVkcnLFxuICAnQkVUV0VFTicsXG4gICdDQVNDQURFJyxcbiAgJ0NBU0UnLFxuICAnQ0FTVCcsXG4gICdDT0FMRVNDRScsXG4gICdDT0xMRUNUX0xJU1QnLFxuICAnQ09MTEVDVF9TRVQnLFxuICAnQ09MVU1OJyxcbiAgJ0NPTFVNTlMnLFxuICAnQ09NTUVOVCcsXG4gICdDT05TVFJBSU5UJyxcbiAgJ0NPTlRBSU5TJyxcbiAgJ0NPTlZFUlQnLFxuICAnQ09VTlQnLFxuICAnQ1VNRV9ESVNUJyxcbiAgJ0NVUlJFTlQgUk9XJyxcbiAgJ0NVUlJFTlRfREFURScsXG4gICdDVVJSRU5UX1RJTUVTVEFNUCcsXG4gICdEQVRBQkFTRScsXG4gICdEQVRBQkFTRVMnLFxuICAnREFURV9BREQnLFxuICAnREFURV9TVUInLFxuICAnREFURV9UUlVOQycsXG4gICdEQVlfSE9VUicsXG4gICdEQVlfTUlOVVRFJyxcbiAgJ0RBWV9TRUNPTkQnLFxuICAnREFZJyxcbiAgJ0RBWVMnLFxuICAnREVDT0RFJyxcbiAgJ0RFRkFVTFQnLFxuICAnREVMRVRFJyxcbiAgJ0RFTlNFX1JBTksnLFxuICAnREVTQycsXG4gICdERVNDUklCRScsXG4gICdESVNUSU5DVCcsXG4gICdESVNUSU5DVFJPVycsXG4gICdESVYnLFxuICAnRFJPUCcsXG4gICdFTFNFJyxcbiAgJ0VOQ09ERScsXG4gICdFTkQnLFxuICAnRVhJU1RTJyxcbiAgJ0VYUExBSU4nLFxuICAnRVhQTE9ERV9PVVRFUicsXG4gICdFWFBMT0RFJyxcbiAgJ0ZJTFRFUicsXG4gICdGSVJTVF9WQUxVRScsXG4gICdGSVJTVCcsXG4gICdGSVhFRCcsXG4gICdGTEFUVEVOJyxcbiAgJ0ZPTExPV0lORycsXG4gICdGUk9NX1VOSVhUSU1FJyxcbiAgJ0ZVTEwnLFxuICAnR1JFQVRFU1QnLFxuICAnR1JPVVBfQ09OQ0FUJyxcbiAgJ0hPVVJfTUlOVVRFJyxcbiAgJ0hPVVJfU0VDT05EJyxcbiAgJ0hPVVInLFxuICAnSE9VUlMnLFxuICAnSUYnLFxuICAnSUZOVUxMJyxcbiAgJ0lOJyxcbiAgJ0lOU0VSVCcsXG4gICdJTlRFUlZBTCcsXG4gICdJTlRPJyxcbiAgJ0lTJyxcbiAgJ0xBRycsXG4gICdMQVNUX1ZBTFVFJyxcbiAgJ0xBU1QnLFxuICAnTEVBRCcsXG4gICdMRUFESU5HJyxcbiAgJ0xFQVNUJyxcbiAgJ0xFVkVMJyxcbiAgJ0xJS0UnLFxuICAnTUFYJyxcbiAgJ01FUkdFJyxcbiAgJ01JTicsXG4gICdNSU5VVEVfU0VDT05EJyxcbiAgJ01JTlVURScsXG4gICdNT05USCcsXG4gICdOQVRVUkFMJyxcbiAgJ05PVCcsXG4gICdOT1coKScsXG4gICdOVElMRScsXG4gICdOVUxMJyxcbiAgJ05VTExJRicsXG4gICdPRkZTRVQnLFxuICAnT04gREVMRVRFJyxcbiAgJ09OIFVQREFURScsXG4gICdPTicsXG4gICdPTkxZJyxcbiAgJ09QVElNSVpFJyxcbiAgJ09WRVInLFxuICAnUEVSQ0VOVF9SQU5LJyxcbiAgJ1BSRUNFRElORycsXG4gICdSQU5HRScsXG4gICdSQU5LJyxcbiAgJ1JFR0VYUCcsXG4gICdSRU5BTUUnLFxuICAnUkxJS0UnLFxuICAnUk9XJyxcbiAgJ1JPV1MnLFxuICAnU0VDT05EJyxcbiAgJ1NFUEFSQVRPUicsXG4gICdTRVFVRU5DRScsXG4gICdTSVpFJyxcbiAgJ1NUUklORycsXG4gICdTVFJVQ1QnLFxuICAnU1VNJyxcbiAgJ1RBQkxFJyxcbiAgJ1RBQkxFUycsXG4gICdURU1QT1JBUlknLFxuICAnVEhFTicsXG4gICdUT19EQVRFJyxcbiAgJ1RPX0pTT04nLFxuICAnVE8nLFxuICAnVFJBSUxJTkcnLFxuICAnVFJBTlNGT1JNJyxcbiAgJ1RSVUUnLFxuICAnVFJVTkNBVEUnLFxuICAnVFlQRScsXG4gICdUWVBFUycsXG4gICdVTkJPVU5ERUQnLFxuICAnVU5JUVVFJyxcbiAgJ1VOSVhfVElNRVNUQU1QJyxcbiAgJ1VOTE9DSycsXG4gICdVTlNJR05FRCcsXG4gICdVU0lORycsXG4gICdWQVJJQUJMRVMnLFxuICAnVklFVycsXG4gICdXSEVOJyxcbiAgJ1dJVEgnLFxuICAnWUVBUl9NT05USCcsXG5dO1xuXG5jb25zdCByZXNlcnZlZFRvcExldmVsV29yZHMgPSBbXG4gICdBREQnLFxuICAnQUZURVInLFxuICAnQUxURVIgQ09MVU1OJyxcbiAgJ0FMVEVSIERBVEFCQVNFJyxcbiAgJ0FMVEVSIFNDSEVNQScsXG4gICdBTFRFUiBUQUJMRScsXG4gICdDTFVTVEVSIEJZJyxcbiAgJ0NMVVNURVJFRCBCWScsXG4gICdERUxFVEUgRlJPTScsXG4gICdESVNUUklCVVRFIEJZJyxcbiAgJ0ZST00nLFxuICAnR1JPVVAgQlknLFxuICAnSEFWSU5HJyxcbiAgJ0lOU0VSVCBJTlRPJyxcbiAgJ0lOU0VSVCcsXG4gICdMSU1JVCcsXG4gICdPUFRJT05TJyxcbiAgJ09SREVSIEJZJyxcbiAgJ1BBUlRJVElPTiBCWScsXG4gICdQQVJUSVRJT05FRCBCWScsXG4gICdSQU5HRScsXG4gICdST1dTJyxcbiAgJ1NFTEVDVCcsXG4gICdTRVQgQ1VSUkVOVCBTQ0hFTUEnLFxuICAnU0VUIFNDSEVNQScsXG4gICdTRVQnLFxuICAnVEJMUFJPUEVSVElFUycsXG4gICdVUERBVEUnLFxuICAnVVNJTkcnLFxuICAnVkFMVUVTJyxcbiAgJ1dIRVJFJyxcbiAgJ1dJTkRPVycsXG5dO1xuXG5jb25zdCByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCA9IFtcbiAgJ0VYQ0VQVCBBTEwnLFxuICAnRVhDRVBUJyxcbiAgJ0lOVEVSU0VDVCBBTEwnLFxuICAnSU5URVJTRUNUJyxcbiAgJ1VOSU9OIEFMTCcsXG4gICdVTklPTicsXG5dO1xuXG5jb25zdCByZXNlcnZlZE5ld2xpbmVXb3JkcyA9IFtcbiAgJ0FORCcsXG4gICdDUkVBVEUgT1InLFxuICAnQ1JFQVRFJyxcbiAgJ0VMU0UnLFxuICAnTEFURVJBTCBWSUVXJyxcbiAgJ09SJyxcbiAgJ09VVEVSIEFQUExZJyxcbiAgJ1dIRU4nLFxuICAnWE9SJyxcbiAgLy8gam9pbnNcbiAgJ0pPSU4nLFxuICAnSU5ORVIgSk9JTicsXG4gICdMRUZUIEpPSU4nLFxuICAnTEVGVCBPVVRFUiBKT0lOJyxcbiAgJ1JJR0hUIEpPSU4nLFxuICAnUklHSFQgT1VURVIgSk9JTicsXG4gICdGVUxMIEpPSU4nLFxuICAnRlVMTCBPVVRFUiBKT0lOJyxcbiAgJ0NST1NTIEpPSU4nLFxuICAnTkFUVVJBTCBKT0lOJyxcbiAgLy8gbm9uLXN0YW5kYXJkLWpvaW5zXG4gICdBTlRJIEpPSU4nLFxuICAnU0VNSSBKT0lOJyxcbiAgJ0xFRlQgQU5USSBKT0lOJyxcbiAgJ0xFRlQgU0VNSSBKT0lOJyxcbiAgJ1JJR0hUIE9VVEVSIEpPSU4nLFxuICAnUklHSFQgU0VNSSBKT0lOJyxcbiAgJ05BVFVSQUwgQU5USSBKT0lOJyxcbiAgJ05BVFVSQUwgRlVMTCBPVVRFUiBKT0lOJyxcbiAgJ05BVFVSQUwgSU5ORVIgSk9JTicsXG4gICdOQVRVUkFMIExFRlQgQU5USSBKT0lOJyxcbiAgJ05BVFVSQUwgTEVGVCBPVVRFUiBKT0lOJyxcbiAgJ05BVFVSQUwgTEVGVCBTRU1JIEpPSU4nLFxuICAnTkFUVVJBTCBPVVRFUiBKT0lOJyxcbiAgJ05BVFVSQUwgUklHSFQgT1VURVIgSk9JTicsXG4gICdOQVRVUkFMIFJJR0hUIFNFTUkgSk9JTicsXG4gICdOQVRVUkFMIFNFTUkgSk9JTicsXG5dO1xuXG5leHBvcnQgZGVmYXVsdCBjbGFzcyBTcGFya1NxbEZvcm1hdHRlciBleHRlbmRzIEZvcm1hdHRlciB7XG4gIHRva2VuaXplcigpIHtcbiAgICByZXR1cm4gbmV3IFRva2VuaXplcih7XG4gICAgICByZXNlcnZlZFdvcmRzLFxuICAgICAgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzLFxuICAgICAgcmVzZXJ2ZWROZXdsaW5lV29yZHMsXG4gICAgICByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCxcbiAgICAgIHN0cmluZ1R5cGVzOiBbYFwiXCJgLCBcIicnXCIsICdgYCcsICd7fSddLFxuICAgICAgb3BlblBhcmVuczogWycoJywgJ0NBU0UnXSxcbiAgICAgIGNsb3NlUGFyZW5zOiBbJyknLCAnRU5EJ10sXG4gICAgICBpbmRleGVkUGxhY2Vob2xkZXJUeXBlczogWyc/J10sXG4gICAgICBuYW1lZFBsYWNlaG9sZGVyVHlwZXM6IFsnJCddLFxuICAgICAgbGluZUNvbW1lbnRUeXBlczogWyctLSddLFxuICAgICAgb3BlcmF0b3JzOiBbJyE9JywgJzw9PicsICcmJicsICd8fCcsICc9PSddLFxuICAgIH0pO1xuICB9XG5cbiAgdG9rZW5PdmVycmlkZSh0b2tlbikge1xuICAgIC8vIEZpeCBjYXNlcyB3aGVyZSBuYW1lcyBhcmUgYW1iaWd1b3VzbHkga2V5d29yZHMgb3IgZnVuY3Rpb25zXG4gICAgaWYgKGlzV2luZG93KHRva2VuKSkge1xuICAgICAgY29uc3QgYWhlYWRUb2tlbiA9IHRoaXMudG9rZW5Mb29rQWhlYWQoKTtcbiAgICAgIGlmIChhaGVhZFRva2VuICYmIGFoZWFkVG9rZW4udHlwZSA9PT0gdG9rZW5UeXBlcy5PUEVOX1BBUkVOKSB7XG4gICAgICAgIC8vIFRoaXMgaXMgYSBmdW5jdGlvbiBjYWxsLCB0cmVhdCBpdCBhcyBhIHJlc2VydmVkIHdvcmRcbiAgICAgICAgcmV0dXJuIHsgdHlwZTogdG9rZW5UeXBlcy5SRVNFUlZFRCwgdmFsdWU6IHRva2VuLnZhbHVlIH07XG4gICAgICB9XG4gICAgfVxuXG4gICAgLy8gRml4IGNhc2VzIHdoZXJlIG5hbWVzIGFyZSBhbWJpZ3VvdXNseSBrZXl3b3JkcyBvciBwcm9wZXJ0aWVzXG4gICAgaWYgKGlzRW5kKHRva2VuKSkge1xuICAgICAgY29uc3QgYmFja1Rva2VuID0gdGhpcy50b2tlbkxvb2tCZWhpbmQoKTtcbiAgICAgIGlmIChiYWNrVG9rZW4gJiYgYmFja1Rva2VuLnR5cGUgPT09IHRva2VuVHlwZXMuT1BFUkFUT1IgJiYgYmFja1Rva2VuLnZhbHVlID09PSAnLicpIHtcbiAgICAgICAgLy8gVGhpcyBpcyB3aW5kb3coKS5lbmQgKG9yIHNpbWlsYXIpIG5vdCBDQVNFIC4uLiBFTkRcbiAgICAgICAgcmV0dXJuIHsgdHlwZTogdG9rZW5UeXBlcy5XT1JELCB2YWx1ZTogdG9rZW4udmFsdWUgfTtcbiAgICAgIH1cbiAgICB9XG5cbiAgICByZXR1cm4gdG9rZW47XG4gIH1cbn1cbiIsImltcG9ydCBGb3JtYXR0ZXIgZnJvbSAnLi4vY29yZS9Gb3JtYXR0ZXInO1xuaW1wb3J0IFRva2VuaXplciBmcm9tICcuLi9jb3JlL1Rva2VuaXplcic7XG5cbi8vIGh0dHBzOi8vamFrZXdoZWF0LmdpdGh1Yi5pby9zcWwtb3ZlcnZpZXcvc3FsLTIwMDgtZm91bmRhdGlvbi1ncmFtbWFyLmh0bWwjcmVzZXJ2ZWQtd29yZFxuY29uc3QgcmVzZXJ2ZWRXb3JkcyA9IFtcbiAgJ0FCUycsXG4gICdBTEwnLFxuICAnQUxMT0NBVEUnLFxuICAnQUxURVInLFxuICAnQU5EJyxcbiAgJ0FOWScsXG4gICdBUkUnLFxuICAnQVJSQVknLFxuICAnQVMnLFxuICAnQVNFTlNJVElWRScsXG4gICdBU1lNTUVUUklDJyxcbiAgJ0FUJyxcbiAgJ0FUT01JQycsXG4gICdBVVRIT1JJWkFUSU9OJyxcbiAgJ0FWRycsXG4gICdCRUdJTicsXG4gICdCRVRXRUVOJyxcbiAgJ0JJR0lOVCcsXG4gICdCSU5BUlknLFxuICAnQkxPQicsXG4gICdCT09MRUFOJyxcbiAgJ0JPVEgnLFxuICAnQlknLFxuICAnQ0FMTCcsXG4gICdDQUxMRUQnLFxuICAnQ0FSRElOQUxJVFknLFxuICAnQ0FTQ0FERUQnLFxuICAnQ0FTRScsXG4gICdDQVNUJyxcbiAgJ0NFSUwnLFxuICAnQ0VJTElORycsXG4gICdDSEFSJyxcbiAgJ0NIQVJfTEVOR1RIJyxcbiAgJ0NIQVJBQ1RFUicsXG4gICdDSEFSQUNURVJfTEVOR1RIJyxcbiAgJ0NIRUNLJyxcbiAgJ0NMT0InLFxuICAnQ0xPU0UnLFxuICAnQ09BTEVTQ0UnLFxuICAnQ09MTEFURScsXG4gICdDT0xMRUNUJyxcbiAgJ0NPTFVNTicsXG4gICdDT01NSVQnLFxuICAnQ09ORElUSU9OJyxcbiAgJ0NPTk5FQ1QnLFxuICAnQ09OU1RSQUlOVCcsXG4gICdDT05WRVJUJyxcbiAgJ0NPUlInLFxuICAnQ09SUkVTUE9ORElORycsXG4gICdDT1VOVCcsXG4gICdDT1ZBUl9QT1AnLFxuICAnQ09WQVJfU0FNUCcsXG4gICdDUkVBVEUnLFxuICAnQ1JPU1MnLFxuICAnQ1VCRScsXG4gICdDVU1FX0RJU1QnLFxuICAnQ1VSUkVOVCcsXG4gICdDVVJSRU5UX0NBVEFMT0cnLFxuICAnQ1VSUkVOVF9EQVRFJyxcbiAgJ0NVUlJFTlRfREVGQVVMVF9UUkFOU0ZPUk1fR1JPVVAnLFxuICAnQ1VSUkVOVF9QQVRIJyxcbiAgJ0NVUlJFTlRfUk9MRScsXG4gICdDVVJSRU5UX1NDSEVNQScsXG4gICdDVVJSRU5UX1RJTUUnLFxuICAnQ1VSUkVOVF9USU1FU1RBTVAnLFxuICAnQ1VSUkVOVF9UUkFOU0ZPUk1fR1JPVVBfRk9SX1RZUEUnLFxuICAnQ1VSUkVOVF9VU0VSJyxcbiAgJ0NVUlNPUicsXG4gICdDWUNMRScsXG4gICdEQVRFJyxcbiAgJ0RBWScsXG4gICdERUFMTE9DQVRFJyxcbiAgJ0RFQycsXG4gICdERUNJTUFMJyxcbiAgJ0RFQ0xBUkUnLFxuICAnREVGQVVMVCcsXG4gICdERUxFVEUnLFxuICAnREVOU0VfUkFOSycsXG4gICdERVJFRicsXG4gICdERVNDUklCRScsXG4gICdERVRFUk1JTklTVElDJyxcbiAgJ0RJU0NPTk5FQ1QnLFxuICAnRElTVElOQ1QnLFxuICAnRE9VQkxFJyxcbiAgJ0RST1AnLFxuICAnRFlOQU1JQycsXG4gICdFQUNIJyxcbiAgJ0VMRU1FTlQnLFxuICAnRUxTRScsXG4gICdFTkQnLFxuICAnRU5ELUVYRUMnLFxuICAnRVNDQVBFJyxcbiAgJ0VWRVJZJyxcbiAgJ0VYQ0VQVCcsXG4gICdFWEVDJyxcbiAgJ0VYRUNVVEUnLFxuICAnRVhJU1RTJyxcbiAgJ0VYUCcsXG4gICdFWFRFUk5BTCcsXG4gICdFWFRSQUNUJyxcbiAgJ0ZBTFNFJyxcbiAgJ0ZFVENIJyxcbiAgJ0ZJTFRFUicsXG4gICdGTE9BVCcsXG4gICdGTE9PUicsXG4gICdGT1InLFxuICAnRk9SRUlHTicsXG4gICdGUkVFJyxcbiAgJ0ZST00nLFxuICAnRlVMTCcsXG4gICdGVU5DVElPTicsXG4gICdGVVNJT04nLFxuICAnR0VUJyxcbiAgJ0dMT0JBTCcsXG4gICdHUkFOVCcsXG4gICdHUk9VUCcsXG4gICdHUk9VUElORycsXG4gICdIQVZJTkcnLFxuICAnSE9MRCcsXG4gICdIT1VSJyxcbiAgJ0lERU5USVRZJyxcbiAgJ0lOJyxcbiAgJ0lORElDQVRPUicsXG4gICdJTk5FUicsXG4gICdJTk9VVCcsXG4gICdJTlNFTlNJVElWRScsXG4gICdJTlNFUlQnLFxuICAnSU5UJyxcbiAgJ0lOVEVHRVInLFxuICAnSU5URVJTRUNUJyxcbiAgJ0lOVEVSU0VDVElPTicsXG4gICdJTlRFUlZBTCcsXG4gICdJTlRPJyxcbiAgJ0lTJyxcbiAgJ0pPSU4nLFxuICAnTEFOR1VBR0UnLFxuICAnTEFSR0UnLFxuICAnTEFURVJBTCcsXG4gICdMRUFESU5HJyxcbiAgJ0xFRlQnLFxuICAnTElLRScsXG4gICdMSUtFX1JFR0VYJyxcbiAgJ0xOJyxcbiAgJ0xPQ0FMJyxcbiAgJ0xPQ0FMVElNRScsXG4gICdMT0NBTFRJTUVTVEFNUCcsXG4gICdMT1dFUicsXG4gICdNQVRDSCcsXG4gICdNQVgnLFxuICAnTUVNQkVSJyxcbiAgJ01FUkdFJyxcbiAgJ01FVEhPRCcsXG4gICdNSU4nLFxuICAnTUlOVVRFJyxcbiAgJ01PRCcsXG4gICdNT0RJRklFUycsXG4gICdNT0RVTEUnLFxuICAnTU9OVEgnLFxuICAnTVVMVElTRVQnLFxuICAnTkFUSU9OQUwnLFxuICAnTkFUVVJBTCcsXG4gICdOQ0hBUicsXG4gICdOQ0xPQicsXG4gICdORVcnLFxuICAnTk8nLFxuICAnTk9ORScsXG4gICdOT1JNQUxJWkUnLFxuICAnTk9UJyxcbiAgJ05VTEwnLFxuICAnTlVMTElGJyxcbiAgJ05VTUVSSUMnLFxuICAnT0NURVRfTEVOR1RIJyxcbiAgJ09DQ1VSUkVOQ0VTX1JFR0VYJyxcbiAgJ09GJyxcbiAgJ09MRCcsXG4gICdPTicsXG4gICdPTkxZJyxcbiAgJ09QRU4nLFxuICAnT1InLFxuICAnT1JERVInLFxuICAnT1VUJyxcbiAgJ09VVEVSJyxcbiAgJ09WRVInLFxuICAnT1ZFUkxBUFMnLFxuICAnT1ZFUkxBWScsXG4gICdQQVJBTUVURVInLFxuICAnUEFSVElUSU9OJyxcbiAgJ1BFUkNFTlRfUkFOSycsXG4gICdQRVJDRU5USUxFX0NPTlQnLFxuICAnUEVSQ0VOVElMRV9ESVNDJyxcbiAgJ1BPU0lUSU9OJyxcbiAgJ1BPU0lUSU9OX1JFR0VYJyxcbiAgJ1BPV0VSJyxcbiAgJ1BSRUNJU0lPTicsXG4gICdQUkVQQVJFJyxcbiAgJ1BSSU1BUlknLFxuICAnUFJPQ0VEVVJFJyxcbiAgJ1JBTkdFJyxcbiAgJ1JBTksnLFxuICAnUkVBRFMnLFxuICAnUkVBTCcsXG4gICdSRUNVUlNJVkUnLFxuICAnUkVGJyxcbiAgJ1JFRkVSRU5DRVMnLFxuICAnUkVGRVJFTkNJTkcnLFxuICAnUkVHUl9BVkdYJyxcbiAgJ1JFR1JfQVZHWScsXG4gICdSRUdSX0NPVU5UJyxcbiAgJ1JFR1JfSU5URVJDRVBUJyxcbiAgJ1JFR1JfUjInLFxuICAnUkVHUl9TTE9QRScsXG4gICdSRUdSX1NYWCcsXG4gICdSRUdSX1NYWScsXG4gICdSRUdSX1NZWScsXG4gICdSRUxFQVNFJyxcbiAgJ1JFU1VMVCcsXG4gICdSRVRVUk4nLFxuICAnUkVUVVJOUycsXG4gICdSRVZPS0UnLFxuICAnUklHSFQnLFxuICAnUk9MTEJBQ0snLFxuICAnUk9MTFVQJyxcbiAgJ1JPVycsXG4gICdST1dfTlVNQkVSJyxcbiAgJ1JPV1MnLFxuICAnU0FWRVBPSU5UJyxcbiAgJ1NDT1BFJyxcbiAgJ1NDUk9MTCcsXG4gICdTRUFSQ0gnLFxuICAnU0VDT05EJyxcbiAgJ1NFTEVDVCcsXG4gICdTRU5TSVRJVkUnLFxuICAnU0VTU0lPTl9VU0VSJyxcbiAgJ1NFVCcsXG4gICdTSU1JTEFSJyxcbiAgJ1NNQUxMSU5UJyxcbiAgJ1NPTUUnLFxuICAnU1BFQ0lGSUMnLFxuICAnU1BFQ0lGSUNUWVBFJyxcbiAgJ1NRTCcsXG4gICdTUUxFWENFUFRJT04nLFxuICAnU1FMU1RBVEUnLFxuICAnU1FMV0FSTklORycsXG4gICdTUVJUJyxcbiAgJ1NUQVJUJyxcbiAgJ1NUQVRJQycsXG4gICdTVERERVZfUE9QJyxcbiAgJ1NURERFVl9TQU1QJyxcbiAgJ1NVQk1VTFRJU0VUJyxcbiAgJ1NVQlNUUklORycsXG4gICdTVUJTVFJJTkdfUkVHRVgnLFxuICAnU1VNJyxcbiAgJ1NZTU1FVFJJQycsXG4gICdTWVNURU0nLFxuICAnU1lTVEVNX1VTRVInLFxuICAnVEFCTEUnLFxuICAnVEFCTEVTQU1QTEUnLFxuICAnVEhFTicsXG4gICdUSU1FJyxcbiAgJ1RJTUVTVEFNUCcsXG4gICdUSU1FWk9ORV9IT1VSJyxcbiAgJ1RJTUVaT05FX01JTlVURScsXG4gICdUTycsXG4gICdUUkFJTElORycsXG4gICdUUkFOU0xBVEUnLFxuICAnVFJBTlNMQVRFX1JFR0VYJyxcbiAgJ1RSQU5TTEFUSU9OJyxcbiAgJ1RSRUFUJyxcbiAgJ1RSSUdHRVInLFxuICAnVFJJTScsXG4gICdUUlVFJyxcbiAgJ1VFU0NBUEUnLFxuICAnVU5JT04nLFxuICAnVU5JUVVFJyxcbiAgJ1VOS05PV04nLFxuICAnVU5ORVNUJyxcbiAgJ1VQREFURScsXG4gICdVUFBFUicsXG4gICdVU0VSJyxcbiAgJ1VTSU5HJyxcbiAgJ1ZBTFVFJyxcbiAgJ1ZBTFVFUycsXG4gICdWQVJfUE9QJyxcbiAgJ1ZBUl9TQU1QJyxcbiAgJ1ZBUkJJTkFSWScsXG4gICdWQVJDSEFSJyxcbiAgJ1ZBUllJTkcnLFxuICAnV0hFTicsXG4gICdXSEVORVZFUicsXG4gICdXSEVSRScsXG4gICdXSURUSF9CVUNLRVQnLFxuICAnV0lORE9XJyxcbiAgJ1dJVEgnLFxuICAnV0lUSElOJyxcbiAgJ1dJVEhPVVQnLFxuICAnWUVBUicsXG5dO1xuXG5jb25zdCByZXNlcnZlZFRvcExldmVsV29yZHMgPSBbXG4gICdBREQnLFxuICAnQUxURVIgQ09MVU1OJyxcbiAgJ0FMVEVSIFRBQkxFJyxcbiAgJ0NBU0UnLFxuICAnREVMRVRFIEZST00nLFxuICAnRU5EJyxcbiAgJ0ZFVENIIEZJUlNUJyxcbiAgJ0ZFVENIIE5FWFQnLFxuICAnRkVUQ0ggUFJJT1InLFxuICAnRkVUQ0ggTEFTVCcsXG4gICdGRVRDSCBBQlNPTFVURScsXG4gICdGRVRDSCBSRUxBVElWRScsXG4gICdGUk9NJyxcbiAgJ0dST1VQIEJZJyxcbiAgJ0hBVklORycsXG4gICdJTlNFUlQgSU5UTycsXG4gICdMSU1JVCcsXG4gICdPUkRFUiBCWScsXG4gICdTRUxFQ1QnLFxuICAnU0VUIFNDSEVNQScsXG4gICdTRVQnLFxuICAnVVBEQVRFJyxcbiAgJ1ZBTFVFUycsXG4gICdXSEVSRScsXG5dO1xuXG5jb25zdCByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCA9IFtcbiAgJ0lOVEVSU0VDVCcsXG4gICdJTlRFUlNFQ1QgQUxMJyxcbiAgJ0lOVEVSU0VDVCBESVNUSU5DVCcsXG4gICdVTklPTicsXG4gICdVTklPTiBBTEwnLFxuICAnVU5JT04gRElTVElOQ1QnLFxuICAnRVhDRVBUJyxcbiAgJ0VYQ0VQVCBBTEwnLFxuICAnRVhDRVBUIERJU1RJTkNUJyxcbl07XG5cbmNvbnN0IHJlc2VydmVkTmV3bGluZVdvcmRzID0gW1xuICAnQU5EJyxcbiAgJ0VMU0UnLFxuICAnT1InLFxuICAnV0hFTicsXG4gIC8vIGpvaW5zXG4gICdKT0lOJyxcbiAgJ0lOTkVSIEpPSU4nLFxuICAnTEVGVCBKT0lOJyxcbiAgJ0xFRlQgT1VURVIgSk9JTicsXG4gICdSSUdIVCBKT0lOJyxcbiAgJ1JJR0hUIE9VVEVSIEpPSU4nLFxuICAnRlVMTCBKT0lOJyxcbiAgJ0ZVTEwgT1VURVIgSk9JTicsXG4gICdDUk9TUyBKT0lOJyxcbiAgJ05BVFVSQUwgSk9JTicsXG5dO1xuXG5leHBvcnQgZGVmYXVsdCBjbGFzcyBTdGFuZGFyZFNxbEZvcm1hdHRlciBleHRlbmRzIEZvcm1hdHRlciB7XG4gIHRva2VuaXplcigpIHtcbiAgICByZXR1cm4gbmV3IFRva2VuaXplcih7XG4gICAgICByZXNlcnZlZFdvcmRzLFxuICAgICAgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzLFxuICAgICAgcmVzZXJ2ZWROZXdsaW5lV29yZHMsXG4gICAgICByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCxcbiAgICAgIHN0cmluZ1R5cGVzOiBbYFwiXCJgLCBcIicnXCJdLFxuICAgICAgb3BlblBhcmVuczogWycoJywgJ0NBU0UnXSxcbiAgICAgIGNsb3NlUGFyZW5zOiBbJyknLCAnRU5EJ10sXG4gICAgICBpbmRleGVkUGxhY2Vob2xkZXJUeXBlczogWyc/J10sXG4gICAgICBuYW1lZFBsYWNlaG9sZGVyVHlwZXM6IFtdLFxuICAgICAgbGluZUNvbW1lbnRUeXBlczogWyctLSddLFxuICAgIH0pO1xuICB9XG59XG4iLCJpbXBvcnQgRm9ybWF0dGVyIGZyb20gJy4uL2NvcmUvRm9ybWF0dGVyJztcbmltcG9ydCBUb2tlbml6ZXIgZnJvbSAnLi4vY29yZS9Ub2tlbml6ZXInO1xuXG5jb25zdCByZXNlcnZlZFdvcmRzID0gW1xuICAnQUREJyxcbiAgJ0VYVEVSTkFMJyxcbiAgJ1BST0NFRFVSRScsXG4gICdBTEwnLFxuICAnRkVUQ0gnLFxuICAnUFVCTElDJyxcbiAgJ0FMVEVSJyxcbiAgJ0ZJTEUnLFxuICAnUkFJU0VSUk9SJyxcbiAgJ0FORCcsXG4gICdGSUxMRkFDVE9SJyxcbiAgJ1JFQUQnLFxuICAnQU5ZJyxcbiAgJ0ZPUicsXG4gICdSRUFEVEVYVCcsXG4gICdBUycsXG4gICdGT1JFSUdOJyxcbiAgJ1JFQ09ORklHVVJFJyxcbiAgJ0FTQycsXG4gICdGUkVFVEVYVCcsXG4gICdSRUZFUkVOQ0VTJyxcbiAgJ0FVVEhPUklaQVRJT04nLFxuICAnRlJFRVRFWFRUQUJMRScsXG4gICdSRVBMSUNBVElPTicsXG4gICdCQUNLVVAnLFxuICAnRlJPTScsXG4gICdSRVNUT1JFJyxcbiAgJ0JFR0lOJyxcbiAgJ0ZVTEwnLFxuICAnUkVTVFJJQ1QnLFxuICAnQkVUV0VFTicsXG4gICdGVU5DVElPTicsXG4gICdSRVRVUk4nLFxuICAnQlJFQUsnLFxuICAnR09UTycsXG4gICdSRVZFUlQnLFxuICAnQlJPV1NFJyxcbiAgJ0dSQU5UJyxcbiAgJ1JFVk9LRScsXG4gICdCVUxLJyxcbiAgJ0dST1VQJyxcbiAgJ1JJR0hUJyxcbiAgJ0JZJyxcbiAgJ0hBVklORycsXG4gICdST0xMQkFDSycsXG4gICdDQVNDQURFJyxcbiAgJ0hPTERMT0NLJyxcbiAgJ1JPV0NPVU5UJyxcbiAgJ0NBU0UnLFxuICAnSURFTlRJVFknLFxuICAnUk9XR1VJRENPTCcsXG4gICdDSEVDSycsXG4gICdJREVOVElUWV9JTlNFUlQnLFxuICAnUlVMRScsXG4gICdDSEVDS1BPSU5UJyxcbiAgJ0lERU5USVRZQ09MJyxcbiAgJ1NBVkUnLFxuICAnQ0xPU0UnLFxuICAnSUYnLFxuICAnU0NIRU1BJyxcbiAgJ0NMVVNURVJFRCcsXG4gICdJTicsXG4gICdTRUNVUklUWUFVRElUJyxcbiAgJ0NPQUxFU0NFJyxcbiAgJ0lOREVYJyxcbiAgJ1NFTEVDVCcsXG4gICdDT0xMQVRFJyxcbiAgJ0lOTkVSJyxcbiAgJ1NFTUFOVElDS0VZUEhSQVNFVEFCTEUnLFxuICAnQ09MVU1OJyxcbiAgJ0lOU0VSVCcsXG4gICdTRU1BTlRJQ1NJTUlMQVJJVFlERVRBSUxTVEFCTEUnLFxuICAnQ09NTUlUJyxcbiAgJ0lOVEVSU0VDVCcsXG4gICdTRU1BTlRJQ1NJTUlMQVJJVFlUQUJMRScsXG4gICdDT01QVVRFJyxcbiAgJ0lOVE8nLFxuICAnU0VTU0lPTl9VU0VSJyxcbiAgJ0NPTlNUUkFJTlQnLFxuICAnSVMnLFxuICAnU0VUJyxcbiAgJ0NPTlRBSU5TJyxcbiAgJ0pPSU4nLFxuICAnU0VUVVNFUicsXG4gICdDT05UQUlOU1RBQkxFJyxcbiAgJ0tFWScsXG4gICdTSFVURE9XTicsXG4gICdDT05USU5VRScsXG4gICdLSUxMJyxcbiAgJ1NPTUUnLFxuICAnQ09OVkVSVCcsXG4gICdMRUZUJyxcbiAgJ1NUQVRJU1RJQ1MnLFxuICAnQ1JFQVRFJyxcbiAgJ0xJS0UnLFxuICAnU1lTVEVNX1VTRVInLFxuICAnQ1JPU1MnLFxuICAnTElORU5PJyxcbiAgJ1RBQkxFJyxcbiAgJ0NVUlJFTlQnLFxuICAnTE9BRCcsXG4gICdUQUJMRVNBTVBMRScsXG4gICdDVVJSRU5UX0RBVEUnLFxuICAnTUVSR0UnLFxuICAnVEVYVFNJWkUnLFxuICAnQ1VSUkVOVF9USU1FJyxcbiAgJ05BVElPTkFMJyxcbiAgJ1RIRU4nLFxuICAnQ1VSUkVOVF9USU1FU1RBTVAnLFxuICAnTk9DSEVDSycsXG4gICdUTycsXG4gICdDVVJSRU5UX1VTRVInLFxuICAnTk9OQ0xVU1RFUkVEJyxcbiAgJ1RPUCcsXG4gICdDVVJTT1InLFxuICAnTk9UJyxcbiAgJ1RSQU4nLFxuICAnREFUQUJBU0UnLFxuICAnTlVMTCcsXG4gICdUUkFOU0FDVElPTicsXG4gICdEQkNDJyxcbiAgJ05VTExJRicsXG4gICdUUklHR0VSJyxcbiAgJ0RFQUxMT0NBVEUnLFxuICAnT0YnLFxuICAnVFJVTkNBVEUnLFxuICAnREVDTEFSRScsXG4gICdPRkYnLFxuICAnVFJZX0NPTlZFUlQnLFxuICAnREVGQVVMVCcsXG4gICdPRkZTRVRTJyxcbiAgJ1RTRVFVQUwnLFxuICAnREVMRVRFJyxcbiAgJ09OJyxcbiAgJ1VOSU9OJyxcbiAgJ0RFTlknLFxuICAnT1BFTicsXG4gICdVTklRVUUnLFxuICAnREVTQycsXG4gICdPUEVOREFUQVNPVVJDRScsXG4gICdVTlBJVk9UJyxcbiAgJ0RJU0snLFxuICAnT1BFTlFVRVJZJyxcbiAgJ1VQREFURScsXG4gICdESVNUSU5DVCcsXG4gICdPUEVOUk9XU0VUJyxcbiAgJ1VQREFURVRFWFQnLFxuICAnRElTVFJJQlVURUQnLFxuICAnT1BFTlhNTCcsXG4gICdVU0UnLFxuICAnRE9VQkxFJyxcbiAgJ09QVElPTicsXG4gICdVU0VSJyxcbiAgJ0RST1AnLFxuICAnT1InLFxuICAnVkFMVUVTJyxcbiAgJ0RVTVAnLFxuICAnT1JERVInLFxuICAnVkFSWUlORycsXG4gICdFTFNFJyxcbiAgJ09VVEVSJyxcbiAgJ1ZJRVcnLFxuICAnRU5EJyxcbiAgJ09WRVInLFxuICAnV0FJVEZPUicsXG4gICdFUlJMVkwnLFxuICAnUEVSQ0VOVCcsXG4gICdXSEVOJyxcbiAgJ0VTQ0FQRScsXG4gICdQSVZPVCcsXG4gICdXSEVSRScsXG4gICdFWENFUFQnLFxuICAnUExBTicsXG4gICdXSElMRScsXG4gICdFWEVDJyxcbiAgJ1BSRUNJU0lPTicsXG4gICdXSVRIJyxcbiAgJ0VYRUNVVEUnLFxuICAnUFJJTUFSWScsXG4gICdXSVRISU4gR1JPVVAnLFxuICAnRVhJU1RTJyxcbiAgJ1BSSU5UJyxcbiAgJ1dSSVRFVEVYVCcsXG4gICdFWElUJyxcbiAgJ1BST0MnLFxuXTtcblxuY29uc3QgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzID0gW1xuICAnQUREJyxcbiAgJ0FMVEVSIENPTFVNTicsXG4gICdBTFRFUiBUQUJMRScsXG4gICdDQVNFJyxcbiAgJ0RFTEVURSBGUk9NJyxcbiAgJ0VORCcsXG4gICdFWENFUFQnLFxuICAnRlJPTScsXG4gICdHUk9VUCBCWScsXG4gICdIQVZJTkcnLFxuICAnSU5TRVJUIElOVE8nLFxuICAnSU5TRVJUJyxcbiAgJ0xJTUlUJyxcbiAgJ09SREVSIEJZJyxcbiAgJ1NFTEVDVCcsXG4gICdTRVQgQ1VSUkVOVCBTQ0hFTUEnLFxuICAnU0VUIFNDSEVNQScsXG4gICdTRVQnLFxuICAnVVBEQVRFJyxcbiAgJ1ZBTFVFUycsXG4gICdXSEVSRScsXG5dO1xuXG5jb25zdCByZXNlcnZlZFRvcExldmVsV29yZHNOb0luZGVudCA9IFsnSU5URVJTRUNUJywgJ0lOVEVSU0VDVCBBTEwnLCAnTUlOVVMnLCAnVU5JT04nLCAnVU5JT04gQUxMJ107XG5cbmNvbnN0IHJlc2VydmVkTmV3bGluZVdvcmRzID0gW1xuICAnQU5EJyxcbiAgJ0VMU0UnLFxuICAnT1InLFxuICAnV0hFTicsXG4gIC8vIGpvaW5zXG4gICdKT0lOJyxcbiAgJ0lOTkVSIEpPSU4nLFxuICAnTEVGVCBKT0lOJyxcbiAgJ0xFRlQgT1VURVIgSk9JTicsXG4gICdSSUdIVCBKT0lOJyxcbiAgJ1JJR0hUIE9VVEVSIEpPSU4nLFxuICAnRlVMTCBKT0lOJyxcbiAgJ0ZVTEwgT1VURVIgSk9JTicsXG4gICdDUk9TUyBKT0lOJyxcbl07XG5cbmV4cG9ydCBkZWZhdWx0IGNsYXNzIFRTcWxGb3JtYXR0ZXIgZXh0ZW5kcyBGb3JtYXR0ZXIge1xuICB0b2tlbml6ZXIoKSB7XG4gICAgcmV0dXJuIG5ldyBUb2tlbml6ZXIoe1xuICAgICAgcmVzZXJ2ZWRXb3JkcyxcbiAgICAgIHJlc2VydmVkVG9wTGV2ZWxXb3JkcyxcbiAgICAgIHJlc2VydmVkTmV3bGluZVdvcmRzLFxuICAgICAgcmVzZXJ2ZWRUb3BMZXZlbFdvcmRzTm9JbmRlbnQsXG4gICAgICBzdHJpbmdUeXBlczogW2BcIlwiYCwgXCJOJydcIiwgXCInJ1wiLCAnW10nXSxcbiAgICAgIG9wZW5QYXJlbnM6IFsnKCcsICdDQVNFJ10sXG4gICAgICBjbG9zZVBhcmVuczogWycpJywgJ0VORCddLFxuICAgICAgaW5kZXhlZFBsYWNlaG9sZGVyVHlwZXM6IFtdLFxuICAgICAgbmFtZWRQbGFjZWhvbGRlclR5cGVzOiBbJ0AnXSxcbiAgICAgIGxpbmVDb21tZW50VHlwZXM6IFsnLS0nXSxcbiAgICAgIHNwZWNpYWxXb3JkQ2hhcnM6IFsnIycsICdAJ10sXG4gICAgICBvcGVyYXRvcnM6IFtcbiAgICAgICAgJz49JyxcbiAgICAgICAgJzw9JyxcbiAgICAgICAgJzw+JyxcbiAgICAgICAgJyE9JyxcbiAgICAgICAgJyE8JyxcbiAgICAgICAgJyE+JyxcbiAgICAgICAgJys9JyxcbiAgICAgICAgJy09JyxcbiAgICAgICAgJyo9JyxcbiAgICAgICAgJy89JyxcbiAgICAgICAgJyU9JyxcbiAgICAgICAgJ3w9JyxcbiAgICAgICAgJyY9JyxcbiAgICAgICAgJ149JyxcbiAgICAgICAgJzo6JyxcbiAgICAgIF0sXG4gICAgICAvLyBUT0RPOiBTdXBwb3J0IGZvciBtb25leSBjb25zdGFudHNcbiAgICB9KTtcbiAgfVxufVxuIiwiaW1wb3J0IERiMkZvcm1hdHRlciBmcm9tICcuL2xhbmd1YWdlcy9EYjJGb3JtYXR0ZXInO1xuaW1wb3J0IE1hcmlhRGJGb3JtYXR0ZXIgZnJvbSAnLi9sYW5ndWFnZXMvTWFyaWFEYkZvcm1hdHRlcic7XG5pbXBvcnQgTXlTcWxGb3JtYXR0ZXIgZnJvbSAnLi9sYW5ndWFnZXMvTXlTcWxGb3JtYXR0ZXInO1xuaW1wb3J0IE4xcWxGb3JtYXR0ZXIgZnJvbSAnLi9sYW5ndWFnZXMvTjFxbEZvcm1hdHRlcic7XG5pbXBvcnQgUGxTcWxGb3JtYXR0ZXIgZnJvbSAnLi9sYW5ndWFnZXMvUGxTcWxGb3JtYXR0ZXInO1xuaW1wb3J0IFBvc3RncmVTcWxGb3JtYXR0ZXIgZnJvbSAnLi9sYW5ndWFnZXMvUG9zdGdyZVNxbEZvcm1hdHRlcic7XG5pbXBvcnQgUmVkc2hpZnRGb3JtYXR0ZXIgZnJvbSAnLi9sYW5ndWFnZXMvUmVkc2hpZnRGb3JtYXR0ZXInO1xuaW1wb3J0IFNwYXJrU3FsRm9ybWF0dGVyIGZyb20gJy4vbGFuZ3VhZ2VzL1NwYXJrU3FsRm9ybWF0dGVyJztcbmltcG9ydCBTdGFuZGFyZFNxbEZvcm1hdHRlciBmcm9tICcuL2xhbmd1YWdlcy9TdGFuZGFyZFNxbEZvcm1hdHRlcic7XG5pbXBvcnQgVFNxbEZvcm1hdHRlciBmcm9tICcuL2xhbmd1YWdlcy9UU3FsRm9ybWF0dGVyJztcblxuY29uc3QgZm9ybWF0dGVycyA9IHtcbiAgZGIyOiBEYjJGb3JtYXR0ZXIsXG4gIG1hcmlhZGI6IE1hcmlhRGJGb3JtYXR0ZXIsXG4gIG15c3FsOiBNeVNxbEZvcm1hdHRlcixcbiAgbjFxbDogTjFxbEZvcm1hdHRlcixcbiAgcGxzcWw6IFBsU3FsRm9ybWF0dGVyLFxuICBwb3N0Z3Jlc3FsOiBQb3N0Z3JlU3FsRm9ybWF0dGVyLFxuICByZWRzaGlmdDogUmVkc2hpZnRGb3JtYXR0ZXIsXG4gIHNwYXJrOiBTcGFya1NxbEZvcm1hdHRlcixcbiAgc3FsOiBTdGFuZGFyZFNxbEZvcm1hdHRlcixcbiAgdHNxbDogVFNxbEZvcm1hdHRlcixcbn07XG5cbi8qKlxuICogRm9ybWF0IHdoaXRlc3BhY2UgaW4gYSBxdWVyeSB0byBtYWtlIGl0IGVhc2llciB0byByZWFkLlxuICpcbiAqIEBwYXJhbSB7U3RyaW5nfSBxdWVyeVxuICogQHBhcmFtIHtPYmplY3R9IGNmZ1xuICogIEBwYXJhbSB7U3RyaW5nfSBjZmcubGFuZ3VhZ2UgUXVlcnkgbGFuZ3VhZ2UsIGRlZmF1bHQgaXMgU3RhbmRhcmQgU1FMXG4gKiAgQHBhcmFtIHtTdHJpbmd9IGNmZy5pbmRlbnQgQ2hhcmFjdGVycyB1c2VkIGZvciBpbmRlbnRhdGlvbiwgZGVmYXVsdCBpcyBcIiAgXCIgKDIgc3BhY2VzKVxuICogIEBwYXJhbSB7Qm9vbGVhbn0gY2ZnLnVwcGVyY2FzZSBDb252ZXJ0cyBrZXl3b3JkcyB0byB1cHBlcmNhc2VcbiAqICBAcGFyYW0ge0ludGVnZXJ9IGNmZy5saW5lc0JldHdlZW5RdWVyaWVzIEhvdyBtYW55IGxpbmUgYnJlYWtzIGJldHdlZW4gcXVlcmllc1xuICogIEBwYXJhbSB7T2JqZWN0fSBjZmcucGFyYW1zIENvbGxlY3Rpb24gb2YgcGFyYW1zIGZvciBwbGFjZWhvbGRlciByZXBsYWNlbWVudFxuICogQHJldHVybiB7U3RyaW5nfVxuICovXG5leHBvcnQgY29uc3QgZm9ybWF0ID0gKHF1ZXJ5LCBjZmcgPSB7fSkgPT4ge1xuICBpZiAodHlwZW9mIHF1ZXJ5ICE9PSAnc3RyaW5nJykge1xuICAgIHRocm93IG5ldyBFcnJvcignSW52YWxpZCBxdWVyeSBhcmd1bWVudC4gRXh0ZWN0ZWQgc3RyaW5nLCBpbnN0ZWFkIGdvdCAnICsgdHlwZW9mIHF1ZXJ5KTtcbiAgfVxuXG4gIGxldCBGb3JtYXR0ZXIgPSBTdGFuZGFyZFNxbEZvcm1hdHRlcjtcbiAgaWYgKGNmZy5sYW5ndWFnZSAhPT0gdW5kZWZpbmVkKSB7XG4gICAgRm9ybWF0dGVyID0gZm9ybWF0dGVyc1tjZmcubGFuZ3VhZ2VdO1xuICB9XG4gIGlmIChGb3JtYXR0ZXIgPT09IHVuZGVmaW5lZCkge1xuICAgIHRocm93IEVycm9yKGBVbnN1cHBvcnRlZCBTUUwgZGlhbGVjdDogJHtjZmcubGFuZ3VhZ2V9YCk7XG4gIH1cbiAgcmV0dXJuIG5ldyBGb3JtYXR0ZXIoY2ZnKS5mb3JtYXQocXVlcnkpO1xufTtcblxuZXhwb3J0IGNvbnN0IHN1cHBvcnRlZERpYWxlY3RzID0gT2JqZWN0LmtleXMoZm9ybWF0dGVycyk7XG5cbi8qKlxuICogQ2hlY2sgU3VwcG9ydCBsYW5ndWFnZVxuICpcbiAqICBAcGFyYW0ge1N0cmluZ30gbGFuZ3VhZ2UgUXVlcnkgbGFuZ3VhZ2VcbiAqIEByZXR1cm4ge0Jvb2xlYW59XG4gKi9cbmV4cG9ydCBjb25zdCBpc1N1cHBvcnREaWFsZWN0cyA9IChsYW5ndWFnZSkgPT57XG4gIGlmIChsYW5ndWFnZSA9PT0gdW5kZWZpbmVkKSB7XG4gICAgdGhyb3cgRXJyb3IoYFVuc3VwcG9ydGVkIFNRTCBkaWFsZWN0OiAke2xhbmd1YWdlfWApO1xuICB9XG5cbiAgbGFuZ3VhZ2UgPT0gbGFuZ3VhZ2UudG9Mb3dlckNhc2UoKTtcblxuICBmb3IodmFyIGtleSBpbiBmb3JtYXR0ZXJzKXtcbiAgICBpZihrZXkgPT0gbGFuZ3VhZ2Upe1xuICAgICAgcmV0dXJuIHRydWU7IFxuICAgIH1cbiAgfVxuICByZXR1cm4gZmFsc2U7IFxufVxuXG5leHBvcnQgY29uc3QgZ2V0VG9rZW5zID0gKHF1ZXJ5LCBjZmcgPSB7fSkgPT4ge1xuICBpZiAodHlwZW9mIHF1ZXJ5ICE9PSAnc3RyaW5nJykge1xuICAgIHRocm93IG5ldyBFcnJvcignSW52YWxpZCBxdWVyeSBhcmd1bWVudC4gRXh0ZWN0ZWQgc3RyaW5nLCBpbnN0ZWFkIGdvdCAnICsgdHlwZW9mIHF1ZXJ5KTtcbiAgfVxuXG4gIGxldCBGb3JtYXR0ZXIgPSBTdGFuZGFyZFNxbEZvcm1hdHRlcjtcbiAgaWYgKGNmZy5sYW5ndWFnZSAhPT0gdW5kZWZpbmVkKSB7XG4gICAgRm9ybWF0dGVyID0gZm9ybWF0dGVyc1tjZmcubGFuZ3VhZ2VdO1xuICB9XG4gIGlmIChGb3JtYXR0ZXIgPT09IHVuZGVmaW5lZCkge1xuICAgIHRocm93IEVycm9yKGBVbnN1cHBvcnRlZCBTUUwgZGlhbGVjdDogJHtjZmcubGFuZ3VhZ2V9YCk7XG4gIH1cbiAgcmV0dXJuIG5ldyBGb3JtYXR0ZXIoY2ZnKS50b2tlbml6ZXIoKS50b2tlbml6ZShxdWVyeSk7XG59O1xuIiwiLy8gT25seSByZW1vdmVzIHNwYWNlcywgbm90IG5ld2xpbmVzXG5leHBvcnQgY29uc3QgdHJpbVNwYWNlc0VuZCA9IChzdHIpID0+IHN0ci5yZXBsYWNlKC9bIFxcdF0rJC91LCAnJyk7XG5cbi8vIExhc3QgZWxlbWVudCBmcm9tIGFycmF5XG5leHBvcnQgY29uc3QgbGFzdCA9IChhcnIpID0+IGFyclthcnIubGVuZ3RoIC0gMV07XG5cbi8vIFRydWUgYXJyYXkgaXMgZW1wdHksIG9yIGl0J3Mgbm90IGFuIGFycmF5IGF0IGFsbFxuZXhwb3J0IGNvbnN0IGlzRW1wdHkgPSAoYXJyKSA9PiAhQXJyYXkuaXNBcnJheShhcnIpIHx8IGFyci5sZW5ndGggPT09IDA7XG5cbi8vIEVzY2FwZXMgcmVnZXggc3BlY2lhbCBjaGFyc1xuZXhwb3J0IGNvbnN0IGVzY2FwZVJlZ0V4cCA9IChzdHJpbmcpID0+IHN0cmluZy5yZXBsYWNlKC9bLiorP14ke30oKXxbXFxdXFxcXF0vZ3UsICdcXFxcJCYnKTtcblxuLy8gU29ydHMgc3RyaW5ncyBieSBsZW5ndGgsIHNvIHRoYXQgbG9uZ2VyIG9uZXMgYXJlIGZpcnN0XG4vLyBBbHNvIHNvcnRzIGFscGhhYmV0aWNhbGx5IGFmdGVyIHNvcnRpbmcgYnkgbGVuZ3RoLlxuZXhwb3J0IGNvbnN0IHNvcnRCeUxlbmd0aERlc2MgPSAoc3RyaW5ncykgPT5cbiAgc3RyaW5ncy5zb3J0KChhLCBiKSA9PiB7XG4gICAgcmV0dXJuIGIubGVuZ3RoIC0gYS5sZW5ndGggfHwgYS5sb2NhbGVDb21wYXJlKGIpO1xuICB9KTtcblxuZXhwb3J0IGNvbnN0IGlzQXJyYXkgPSAob2JqKSA9PiB7XG4gIGlmKEFycmF5LmlzQXJyYXkpe1xuXHRcdHJldHVybiBBcnJheS5pc0FycmF5KG9iailcblx0fWVsc2V7XG5cdFx0cmV0dXJuIE9iamVjdC5wcm90b3R5cGUudG9TdHJpbmcuY2FsbChvYmopID09PSAnW29iamVjdCBBcnJheV0nO1xuXHR9XG59XG5cbmV4cG9ydCBjb25zdCBpc0Z1bmN0aW9uID0gKG9iaikgPT4gdHlwZW9mIG9iaj09PSdmdW5jdGlvbic7XG5cbmV4cG9ydCBjb25zdCBpc09iamVjdD0gKG9iaikgPT4ge1xuICBpZihpc0FycmF5KG9iaikpe1xuICAgIHJldHVybiBmYWxzZTtcbiAgfWVsc2UgaWYoaXNGdW5jdGlvbihvYmopKXtcbiAgICByZXR1cm4gZmFsc2U7XG4gIH1cblxuICByZXR1cm4gdHlwZW9mIG9iaj09PSdvYmplY3QnO1xufVxuXG5leHBvcnQgY29uc3QgaXNEYXRlID0gKG9iaikgPT4ge1xuXHRpZiAob2JqIGluc3RhbmNlb2YgRGF0ZSkgcmV0dXJuIHRydWU7XG5cblx0aWYoaXNPYmplY3Qob2JqKSl7XG5cdFx0cmV0dXJuIHR5cGVvZiBvYmoudG9EYXRlU3RyaW5nID09PSAnZnVuY3Rpb24nXG5cdFx0ICAgICYmIHR5cGVvZiBvYmouZ2V0RGF0ZSA9PT0gJ2Z1bmN0aW9uJ1xuXHRcdCAgICAmJiB0eXBlb2Ygb2JqLnNldERhdGUgPT09ICdmdW5jdGlvbic7XG5cdH1cblxuXHRyZXR1cm4gIGZhbHNlO1xufVxuXG5leHBvcnQgY29uc3Qgb2JqZWN0TWVyZ2UgPSguLi5hcmcpID0+IHtcbiAgdmFyIHJldmFsID0gYXJnWzBdO1xuICBpZiAodHlwZW9mIHJldmFsICE9PSAnb2JqZWN0JyB8fCByZXZhbCA9PT0gbnVsbCkge1x0cmV0dXJuIHJldmFsO31cbiAgdmFyIGkgPSAxO1xuICBpZihPYmplY3Qua2V5cyhyZXZhbCkubGVuZ3RoID4gMCl7XG4gICAgaSA9IDA7XG4gICAgcmV2YWwgPSBpc0FycmF5KHJldmFsKSA/IFtdIDp7fTtcbiAgfVxuICB2YXIgYXJnTGVuID0gYXJnLmxlbmd0aDtcbiAgZm9yICg7IGkgPCBhcmdMZW47IGkrKykge1xuICAgIGNsb25lRGVlcChyZXZhbCwgYXJnW2ldKTtcbiAgfVxuICByZXR1cm4gcmV2YWw7XG59XG5cbmZ1bmN0aW9uIGNsb25lRGVlcCAoZHN0LCBzcmMpe1xuXHRpZihpc09iamVjdChzcmMpKXtcblx0XHRyZXR1cm4gY2xvbmVPYmplY3REZWVwKGRzdCwgc3JjKTtcblx0fWVsc2UgaWYoaXNBcnJheShzcmMpKXtcblx0XHRyZXR1cm4gY2xvbmVBcnJheURlZXAoZHN0LCBzcmMpO1xuXHR9ZWxzZXtcblx0XHRpZiAoaXNEYXRlKHNyYykpe1xuXHRcdFx0cmV0dXJuIG5ldyBzcmMuY29uc3RydWN0b3Ioc3JjKTtcblx0XHR9ZWxzZXtcblx0XHRcdHJldHVybiBzcmM7XG5cdFx0fVxuXHR9XG59XG5cbmZ1bmN0aW9uIGNsb25lT2JqZWN0RGVlcChkc3QsIHNyYykge1xuXHRpZiAodHlwZW9mIHNyYyA9PT0gJ2Z1bmN0aW9uJykge1xuXHRcdHJldHVybiBzcmM7XG5cdH1cblxuXHRmb3IgKGxldCBrZXkgaW4gc3JjKSB7XG5cblx0XHRpZighc3JjLmhhc093blByb3BlcnR5KGtleSkpIHtjb250aW51ZTt9XG5cblx0XHR2YXIgdmFsID0gc3JjW2tleV07XG5cblx0XHRpZiAodmFsPT09IHVuZGVmaW5lZCkge2NvbnRpbnVlO31cblxuXHRcdGlmICggdHlwZW9mIHZhbCAhPT0gJ29iamVjdCcgfHwgdmFsPT09IG51bGwpIHtcblx0XHRcdGRzdFtrZXldICA9IHZhbDtcblx0XHR9IGVsc2UgaWYgKHR5cGVvZiBkc3Rba2V5XSAhPT0gJ29iamVjdCcgfHwgZHN0W2tleV0gPT09IG51bGwpIHtcblx0XHRcdGRzdFtrZXldID0gY2xvbmVEZWVwKGlzQXJyYXkodmFsKSA/IFtdIDoge30sIHZhbCk7XG5cdFx0fSBlbHNlIHtcblx0XHRcdGNsb25lRGVlcChkc3Rba2V5XSAsIHZhbCk7XG5cdFx0fVxuXHR9XG5cdHJldHVybiBkc3Q7XG59XG5cbmZ1bmN0aW9uIGNsb25lQXJyYXlEZWVwKGRzdCwgc3JjKSB7XG5cdHZhciBpc09iaiA9IGlzT2JqZWN0KGRzdCk7XG5cblx0Zm9yICh2YXIgaSA9IDA7IGkgPCBzcmMubGVuZ3RoOyBpKyspIHtcblx0XHR2YXIgdmFsID0gc3JjW2ldO1xuXHRcdHZhciBuZXdWYWw7XG5cblx0XHRpZih2YWwgPT0gbnVsbCl7XG5cdFx0XHRuZXdWYWwgPSB2YWw7XG5cdFx0fWVsc2V7XG5cdFx0XHRuZXdWYWw9IGNsb25lRGVlcChpc0FycmF5KHZhbCkgPyBbXSA6IHt9LCB2YWwpO1xuXHRcdH1cblxuXHRcdGlmKGlzT2JqKXtcblx0XHRcdGRzdFtpXSA9IG5ld1ZhbDtcblx0XHR9ZWxzZXtcblx0XHRcdHZhciBhZGRGbGFnID10cnVlOyBcbiAgICAgIGZvcih2YXIgaiA9IDAsbCA9IGRzdC5sZW5ndGg7IGo8bDsgaisrKXtcbiAgICAgICAgICBpZihkc3Rbal0gPT0gbmV3VmFsKXtcbiAgICAgICAgICAgIGFkZEZsYWcgPSBmYWxzZTsgXG4gICAgICAgICAgICBicmVhazsgXG4gICAgICAgICAgfVxuICAgICAgfVxuICAgICAgaWYoYWRkRmxhZyl7XG4gICAgICAgIGRzdC5wdXNoKG5ld1ZhbCk7XG4gICAgICB9XG5cdFx0fVxuXHR9XG5cdHJldHVybiBkc3Q7XG59Il0sInNvdXJjZVJvb3QiOiIifQ==