(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory();
	else if(typeof define === 'function' && define.amd)
		define([], factory);
	else if(typeof exports === 'object')
		exports["VARSQLUtils"] = factory();
	else
		root["VARSQLUtils"] = factory();
})(self, () => {
return /******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "./src/constants.ts":
/*!**************************!*\
  !*** ./src/constants.ts ***!
  \**************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {



var __importDefault = this && this.__importDefault || function (mod) {
  return mod && mod.__esModule ? mod : {
    "default": mod
  };
};

Object.defineProperty(exports, "__esModule", ({
  value: true
}));
exports.DEFAULT_END_SPLITTER = exports.CREATE_END_CHECK_KEYWORD = exports.DEFAULT_TEXT_CHECK_TOKEN = exports.TAB_CHAR = exports.LINE_CHAR = void 0;

var CreateChecker_1 = __importDefault(__webpack_require__(/*! ./core/create/CreateChecker */ "./src/core/create/CreateChecker.ts"));

var CreateFunctionChecker_1 = __importDefault(__webpack_require__(/*! ./core/create/CreateFunctionChecker */ "./src/core/create/CreateFunctionChecker.ts"));

var CreateTriggerCheckerr_1 = __importDefault(__webpack_require__(/*! ./core/create/CreateTriggerCheckerr */ "./src/core/create/CreateTriggerCheckerr.ts"));

var CreateProcedureChecker_1 = __importDefault(__webpack_require__(/*! ./core/create/CreateProcedureChecker */ "./src/core/create/CreateProcedureChecker.ts"));

var DefaultChecker_1 = __importDefault(__webpack_require__(/*! ./core/DefaultChecker */ "./src/core/DefaultChecker.ts")); // line char


exports.LINE_CHAR = '\n'; // tab char

exports.TAB_CHAR = '\t';
exports.DEFAULT_TEXT_CHECK_TOKEN = {
  "'": {
    keepWord: true,
    checkEnd: function checkEnd(beforeCh1, c1, c2) {
      return beforeCh1 != "\\" && c1 == "'";
    }
  },
  '"': {
    keepWord: true,
    checkEnd: function checkEnd(beforeCh1, c1, c2) {
      return beforeCh1 != "\\" && c1 == '"';
    }
  },
  "--": {
    keepWord: true,
    endToken: exports.LINE_CHAR
  },
  "/*": {
    keepWord: true,
    endToken: "*/"
  },
  "#{": {
    keepWord: true,
    endToken: "}"
  },
  "${": {
    keepWord: true,
    endToken: "}"
  }
};
exports.CREATE_END_CHECK_KEYWORD = {
  'if': {
    end: 'if'
  },
  'loop': {
    end: 'loop'
  },
  'case': {
    end: ''
  }
};
exports.DEFAULT_END_SPLITTER = {
  'default': new DefaultChecker_1["default"](),
  'create': new CreateChecker_1["default"](),
  'create_function': new CreateFunctionChecker_1["default"](exports.CREATE_END_CHECK_KEYWORD),
  'create_trigger': new CreateTriggerCheckerr_1["default"](exports.CREATE_END_CHECK_KEYWORD),
  'create_procedure': new CreateProcedureChecker_1["default"](exports.CREATE_END_CHECK_KEYWORD)
};

/***/ }),

/***/ "./src/core/DefaultChecker.ts":
/*!************************************!*\
  !*** ./src/core/DefaultChecker.ts ***!
  \************************************/
/***/ ((__unused_webpack_module, exports, __webpack_require__) => {



function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); Object.defineProperty(Constructor, "prototype", { writable: false }); return Constructor; }

Object.defineProperty(exports, "__esModule", ({
  value: true
}));

var constants_1 = __webpack_require__(/*! ../constants */ "./src/constants.ts"); // create function check token


var DefaultChecker = /*#__PURE__*/function () {
  function DefaultChecker() {
    _classCallCheck(this, DefaultChecker);

    this.lineCount = 0;
  }

  _createClass(DefaultChecker, [{
    key: "init",
    value: function init() {
      this.lineCount = 0;
    }
  }, {
    key: "checkEnd",
    value: function checkEnd(word, beforeWord, c1, beforeCh1, i, sql) {
      if (/\s/.test(c1)) {
        if (this.lineCount > 0 && c1 == constants_1.LINE_CHAR) {
          this.lineCount += 1;
        } else if (c1 == constants_1.LINE_CHAR) {
          this.lineCount = 1;
        }

        return this.lineCount > 2 ? true : false; // 공백 2줄 이상이면 분리
      }

      this.lineCount = 0;

      if (beforeCh1 == constants_1.LINE_CHAR && beforeCh1 == c1) {
        return true;
      }

      return c1 == ";";
    }
  }]);

  return DefaultChecker;
}();

exports["default"] = DefaultChecker;

/***/ }),

/***/ "./src/core/EndCheckTokenInfo.ts":
/*!***************************************!*\
  !*** ./src/core/EndCheckTokenInfo.ts ***!
  \***************************************/
/***/ ((__unused_webpack_module, exports) => {



function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); Object.defineProperty(Constructor, "prototype", { writable: false }); return Constructor; }

Object.defineProperty(exports, "__esModule", ({
  value: true
}));
exports.EndCheckTokenInfo = void 0;

var EndCheckTokenInfo = /*#__PURE__*/function () {
  function EndCheckTokenInfo(tokens) {
    _classCallCheck(this, EndCheckTokenInfo);

    this.tokens = {};
    this.tokens = tokens;
  }

  _createClass(EndCheckTokenInfo, [{
    key: "put",
    value: function put(key, value) {
      this.tokens[key] = value;
      return this;
    }
  }, {
    key: "hasKey",
    value: function hasKey(key) {
      return this.tokens.hasOwnProperty(key);
    }
  }, {
    key: "get",
    value: function get(key) {
      var endCheck = this.tokens[key];
      endCheck.init();
      return endCheck;
    }
  }]);

  return EndCheckTokenInfo;
}();

exports.EndCheckTokenInfo = EndCheckTokenInfo;

/***/ }),

/***/ "./src/core/Splitter.ts":
/*!******************************!*\
  !*** ./src/core/Splitter.ts ***!
  \******************************/
/***/ ((__unused_webpack_module, exports, __webpack_require__) => {



function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); Object.defineProperty(Constructor, "prototype", { writable: false }); return Constructor; }

Object.defineProperty(exports, "__esModule", ({
  value: true
}));

var constants_1 = __webpack_require__(/*! ../constants */ "./src/constants.ts");

var Splitter = /*#__PURE__*/function () {
  function Splitter(cfg) {
    _classCallCheck(this, Splitter);

    this.cfg = cfg;
    var checker = this.checker();
    this.textChecker = checker.textChecker;
    this.endChecker = checker.endChecker;
  }

  _createClass(Splitter, [{
    key: "checker",
    value: function checker() {
      throw new Error('checker() not implemented by subclass');
    }
  }, {
    key: "split",
    value: function split(sql, findLine, findCharPos) {
      findLine = typeof findLine == 'undefined' ? -1 : findLine;
      findCharPos = typeof findCharPos == 'undefined' ? -1 : findCharPos;
      var statementList = [];
      var statement = [];
      var g_idx = 0;
      var sqlLen = sql.length;
      var lineIdx = 0;
      var startLine = 0;
      var startCharPos = 0;
      var c1 = '',
          c2 = '';
      var beforeCh1;
      var orginCh;
      var startCommand = false;
      var command = '';
      var word = '';
      var beforeWord = '';
      var endCheckerInter = null;
      var textCheckerInter = null;
      var currentEndTokenKey = '';
      var lineStartCharIdx = 0;
      var overflowFlag = false; // 문자 단위로 읽기

      for (var i = 0; i < sqlLen; i++) {
        beforeCh1 = c1; // 이전 값 넣기.

        orginCh = sql.charAt(i);
        c1 = orginCh.toLowerCase();
        c2 = c1 + (i + 1 < sqlLen ? sql.charAt(i + 1) : '0');
        c2 = c2.toLowerCase();

        if (c1 == constants_1.LINE_CHAR) {
          lineStartCharIdx = i;
          ++lineIdx;
        }

        if (c1 == beforeCh1) {
          g_idx++;

          if (g_idx > 1000) {
            console.log('split error');
            break;
          }
        } else {
          g_idx = 0;
        }

        if (textCheckerInter == null) {
          if (this.textChecker.hasKey(c2) || this.textChecker.hasKey(c1)) {
            textCheckerInter = this.textChecker.get(c2) || this.textChecker.get(c1);
            if (textCheckerInter.keepWord !== false) statement.push(orginCh);
            continue;
          }
        } else {
          var check = false;

          if (textCheckerInter.checkEnd && textCheckerInter.checkEnd(beforeCh1, c1, c2)) {
            check = true;
          } else if (textCheckerInter.endToken == c2 || textCheckerInter.endToken == c1) {
            check = true;
          }

          if (check) {
            if (textCheckerInter.keepWord === false) {
              i += textCheckerInter.endToken ? textCheckerInter.endToken.length - 1 : 0;
            } else {
              statement.push(orginCh);
            }

            textCheckerInter = null;
          } else {
            if (textCheckerInter.keepWord !== false) statement.push(orginCh);
          }

          continue;
        }

        statement.push(orginCh);

        if (/[(),]/.test(c1)) {
          continue;
        } // 공백체크. ( 체크 


        if (/[\s(]/.test(c1)) {
          if (startCommand) startCommand = false;
          beforeWord = word;
          word = '';
        } else {
          if (command == '') {
            if (/[;/!@#$%^&()+=?\-]/.test(c1)) {
              // command 시작 문자가 특수 문자면 command로 처리 안함. 
              continue;
            }

            startLine = lineIdx;
            startCharPos = i - lineStartCharIdx;
            startCommand = true;
          }

          if (startCommand) {
            command += c1;
          }

          word += c1;
        }

        if (startCommand === false && endCheckerInter == null && command != '') {
          currentEndTokenKey = command;

          if (this.endChecker.hasKey(command)) {
            endCheckerInter = this.endChecker.get(command);
          } else {
            endCheckerInter = this.endChecker.get('default');
          }
        } else {
          if (this.endChecker.hasKey(currentEndTokenKey + '_' + word)) {
            currentEndTokenKey = currentEndTokenKey + '_' + word;
            endCheckerInter = this.endChecker.get(currentEndTokenKey);
          }
        }

        if (!overflowFlag && findLine != -1 && lineIdx >= findLine && findCharPos <= i - lineStartCharIdx) {
          //console.log('['+command+']', endCheckerInter, 'asdf : ',lineIdx, findLine , findCharPos , i-lineStartCharIdx)
          overflowFlag = true;

          if (command.trim() == '' && statementList.length > 0) {
            return [statementList[statementList.length - 1]];
          }
        }

        if (endCheckerInter && endCheckerInter.checkEnd(word, beforeWord, c1, beforeCh1, i, sql) || i + 1 == sqlLen) {
          var sqlSplitInfo = {
            command: command,
            startLine: startLine,
            startCharPos: startCharPos,
            endLine: lineIdx,
            endCharPos: i - lineStartCharIdx,
            statement: statement.join('')
          };

          if (overflowFlag) {
            return [sqlSplitInfo];
          }

          if (findLine != -1) {
            if (startLine <= findLine && findLine <= lineIdx) {
              // 라인이 다를경우
              if (startLine == lineIdx && findLine == lineIdx) {
                if (startCharPos <= findCharPos && findCharPos <= sqlSplitInfo.endCharPos) {
                  return [sqlSplitInfo];
                }
              } else {
                return [sqlSplitInfo];
              }
            }
          } else {
            statementList.push(sqlSplitInfo);
          }

          command = '';
          word == '';
          statement = [];
          endCheckerInter = null;
          startCommand = false;
        }
      }

      return statementList;
    }
  }]);

  return Splitter;
}();

exports["default"] = Splitter;

/***/ }),

/***/ "./src/core/TextCheckTokenInfo.ts":
/*!****************************************!*\
  !*** ./src/core/TextCheckTokenInfo.ts ***!
  \****************************************/
/***/ ((__unused_webpack_module, exports) => {



function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); Object.defineProperty(Constructor, "prototype", { writable: false }); return Constructor; }

Object.defineProperty(exports, "__esModule", ({
  value: true
}));
exports.TextCheckTokenInfo = void 0;

var TextCheckTokenInfo = /*#__PURE__*/function () {
  function TextCheckTokenInfo(tokens) {
    _classCallCheck(this, TextCheckTokenInfo);

    this.tokens = tokens;
  }

  _createClass(TextCheckTokenInfo, [{
    key: "put",
    value: function put(key, value) {
      this.tokens[key] = value;
      return this;
    }
  }, {
    key: "hasKey",
    value: function hasKey(key) {
      return this.tokens.hasOwnProperty(key);
    }
  }, {
    key: "get",
    value: function get(key) {
      return this.tokens[key];
    }
  }]);

  return TextCheckTokenInfo;
}();

exports.TextCheckTokenInfo = TextCheckTokenInfo;

/***/ }),

/***/ "./src/core/create/CreateChecker.ts":
/*!******************************************!*\
  !*** ./src/core/create/CreateChecker.ts ***!
  \******************************************/
/***/ ((__unused_webpack_module, exports) => {



function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); Object.defineProperty(Constructor, "prototype", { writable: false }); return Constructor; }

Object.defineProperty(exports, "__esModule", ({
  value: true
})); // create check token

var CreateChecker = /*#__PURE__*/function () {
  function CreateChecker() {
    _classCallCheck(this, CreateChecker);

    this.idx = 0;
  }

  _createClass(CreateChecker, [{
    key: "init",
    value: function init() {
      this.idx = 0;
    }
  }, {
    key: "checkEnd",
    value: function checkEnd(word, beforeWord, c1, beforeCh1, i, sql) {
      console.log(++this.idx);
      return c1 == '/' || c1 == ';';
    }
  }]);

  return CreateChecker;
}();

exports["default"] = CreateChecker;

/***/ }),

/***/ "./src/core/create/CreateFunctionChecker.ts":
/*!**************************************************!*\
  !*** ./src/core/create/CreateFunctionChecker.ts ***!
  \**************************************************/
/***/ ((__unused_webpack_module, exports) => {



function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); Object.defineProperty(Constructor, "prototype", { writable: false }); return Constructor; }

Object.defineProperty(exports, "__esModule", ({
  value: true
})); // create function check token

var CreateFunctionChecker = /*#__PURE__*/function () {
  function CreateFunctionChecker(endCheckKeywords) {
    _classCallCheck(this, CreateFunctionChecker);

    this.begin = false;
    this.endFlag = false;
    this.tokenStack = [];
    this.endCheckKeywords = endCheckKeywords;
  }

  _createClass(CreateFunctionChecker, [{
    key: "init",
    value: function init() {
      this.begin = false;
      this.endFlag = false;
      this.tokenStack = [];
    }
  }, {
    key: "checkEnd",
    value: function checkEnd(word, beforeWord, c1, beforeCh1, i, sql) {
      if ('begin' == word) {
        this.begin = true;
      } else if (this.endCheckKeywords.hasOwnProperty(word)) {
        if ('end' == beforeWord) {
          this.tokenStack.pop();
        } else {
          this.tokenStack.push(this.endCheckKeywords[word]);
        }
      } else if ('end' == word) {
        var popItem = this.tokenStack[this.tokenStack.length - 1];

        if (popItem && popItem.end == '') {
          this.tokenStack.pop();
        }

        this.endFlag = this.tokenStack.length == 0;
      }

      if (this.begin && this.endFlag && c1 == ';') {
        return true;
      }

      return false;
    }
  }]);

  return CreateFunctionChecker;
}();

exports["default"] = CreateFunctionChecker;

/***/ }),

/***/ "./src/core/create/CreateProcedureChecker.ts":
/*!***************************************************!*\
  !*** ./src/core/create/CreateProcedureChecker.ts ***!
  \***************************************************/
/***/ ((__unused_webpack_module, exports) => {



function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); Object.defineProperty(Constructor, "prototype", { writable: false }); return Constructor; }

Object.defineProperty(exports, "__esModule", ({
  value: true
})); // create function check token

var CreateProcedureChecker = /*#__PURE__*/function () {
  function CreateProcedureChecker(endCheckKeywords) {
    _classCallCheck(this, CreateProcedureChecker);

    this.begin = false;
    this.endFlag = false;
    this.tokenStack = [];
    this.endCheckKeywords = endCheckKeywords;
  }

  _createClass(CreateProcedureChecker, [{
    key: "init",
    value: function init() {
      this.begin = false;
      this.endFlag = false;
      this.tokenStack = [];
    }
  }, {
    key: "checkEnd",
    value: function checkEnd(word, beforeWord, c1, beforeCh1, i, sql) {
      if ('begin' == word) {
        this.begin = true;
      } else if (this.endCheckKeywords.hasOwnProperty(word)) {
        if ('end' == beforeWord) {
          this.tokenStack.pop();
        } else {
          this.tokenStack.push(this.endCheckKeywords[word]);
        }
      } else if ('end' == word) {
        var popItem = this.tokenStack[this.tokenStack.length - 1];

        if (popItem && popItem.end == '') {
          this.tokenStack.pop();
        }

        this.endFlag = this.tokenStack.length == 0;
      }

      if (this.begin && this.endFlag && c1 == ';') {
        return true;
      }

      return false;
    }
  }]);

  return CreateProcedureChecker;
}();

exports["default"] = CreateProcedureChecker;

/***/ }),

/***/ "./src/core/create/CreateTriggerCheckerr.ts":
/*!**************************************************!*\
  !*** ./src/core/create/CreateTriggerCheckerr.ts ***!
  \**************************************************/
/***/ ((__unused_webpack_module, exports) => {



function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); Object.defineProperty(Constructor, "prototype", { writable: false }); return Constructor; }

Object.defineProperty(exports, "__esModule", ({
  value: true
})); // create function check token

var CreateTriggerCheckerr = /*#__PURE__*/function () {
  function CreateTriggerCheckerr(endCheckKeywords) {
    _classCallCheck(this, CreateTriggerCheckerr);

    this.begin = false;
    this.endFlag = false;
    this.tokenStack = [];
    this.endCheckKeywords = endCheckKeywords;
  }

  _createClass(CreateTriggerCheckerr, [{
    key: "init",
    value: function init() {
      this.begin = false;
      this.endFlag = false;
      this.tokenStack = [];
    }
  }, {
    key: "checkEnd",
    value: function checkEnd(word, beforeWord, c1, beforeCh1, i, sql) {
      if ('begin' == word) {
        this.begin = true;
      } else if (this.endCheckKeywords.hasOwnProperty(word)) {
        if ('end' == beforeWord) {
          this.tokenStack.pop();
        } else {
          this.tokenStack.push(this.endCheckKeywords[word]);
        }
      } else if ('end' == word) {
        var popItem = this.tokenStack[this.tokenStack.length - 1];

        if (popItem && popItem.end == '') {
          this.tokenStack.pop();
        }

        this.endFlag = this.tokenStack.length == 0;
      }

      if (this.begin && this.endFlag && c1 == ';') {
        return true;
      }

      return false;
    }
  }]);

  return CreateTriggerCheckerr;
}();

exports["default"] = CreateTriggerCheckerr;

/***/ }),

/***/ "./src/db/standard.splitter.ts":
/*!*************************************!*\
  !*** ./src/db/standard.splitter.ts ***!
  \*************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {



function _typeof(obj) { "@babel/helpers - typeof"; return _typeof = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (obj) { return typeof obj; } : function (obj) { return obj && "function" == typeof Symbol && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }, _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); Object.defineProperty(Constructor, "prototype", { writable: false }); return Constructor; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); Object.defineProperty(subClass, "prototype", { writable: false }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf ? Object.setPrototypeOf.bind() : function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _createSuper(Derived) { var hasNativeReflectConstruct = _isNativeReflectConstruct(); return function _createSuperInternal() { var Super = _getPrototypeOf(Derived), result; if (hasNativeReflectConstruct) { var NewTarget = _getPrototypeOf(this).constructor; result = Reflect.construct(Super, arguments, NewTarget); } else { result = Super.apply(this, arguments); } return _possibleConstructorReturn(this, result); }; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } else if (call !== void 0) { throw new TypeError("Derived constructors may only return object or undefined"); } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _isNativeReflectConstruct() { if (typeof Reflect === "undefined" || !Reflect.construct) return false; if (Reflect.construct.sham) return false; if (typeof Proxy === "function") return true; try { Boolean.prototype.valueOf.call(Reflect.construct(Boolean, [], function () {})); return true; } catch (e) { return false; } }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf.bind() : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

var __importDefault = this && this.__importDefault || function (mod) {
  return mod && mod.__esModule ? mod : {
    "default": mod
  };
};

Object.defineProperty(exports, "__esModule", ({
  value: true
}));

var constants_1 = __webpack_require__(/*! ../constants */ "./src/constants.ts");

var Splitter_1 = __importDefault(__webpack_require__(/*! ../core/Splitter */ "./src/core/Splitter.ts"));

var TextCheckTokenInfo_1 = __webpack_require__(/*! ../core/TextCheckTokenInfo */ "./src/core/TextCheckTokenInfo.ts");

var EndCheckTokenInfo_1 = __webpack_require__(/*! ../core/EndCheckTokenInfo */ "./src/core/EndCheckTokenInfo.ts");

var StandardSplitter = /*#__PURE__*/function (_Splitter_1$default) {
  _inherits(StandardSplitter, _Splitter_1$default);

  var _super = _createSuper(StandardSplitter);

  function StandardSplitter() {
    _classCallCheck(this, StandardSplitter);

    return _super.apply(this, arguments);
  }

  _createClass(StandardSplitter, [{
    key: "checker",
    value: function checker() {
      return {
        textChecker: new TextCheckTokenInfo_1.TextCheckTokenInfo(constants_1.DEFAULT_TEXT_CHECK_TOKEN),
        endChecker: new EndCheckTokenInfo_1.EndCheckTokenInfo(constants_1.DEFAULT_END_SPLITTER)
      };
    }
  }]);

  return StandardSplitter;
}(Splitter_1["default"]);

exports["default"] = StandardSplitter;

/***/ }),

/***/ "./src/index.ts":
/*!**********************!*\
  !*** ./src/index.ts ***!
  \**********************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {



var __createBinding = this && this.__createBinding || (Object.create ? function (o, m, k, k2) {
  if (k2 === undefined) k2 = k;
  var desc = Object.getOwnPropertyDescriptor(m, k);

  if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
    desc = {
      enumerable: true,
      get: function get() {
        return m[k];
      }
    };
  }

  Object.defineProperty(o, k2, desc);
} : function (o, m, k, k2) {
  if (k2 === undefined) k2 = k;
  o[k2] = m[k];
});

var __exportStar = this && this.__exportStar || function (m, exports) {
  for (var p in m) {
    if (p !== "default" && !Object.prototype.hasOwnProperty.call(exports, p)) __createBinding(exports, m, p);
  }
};

Object.defineProperty(exports, "__esModule", ({
  value: true
}));

__exportStar(__webpack_require__(/*! ./sqlSplitter */ "./src/sqlSplitter.ts"), exports);

__exportStar(__webpack_require__(/*! ./splitOptions */ "./src/splitOptions.ts"), exports);

/***/ }),

/***/ "./src/splitOptions.ts":
/*!*****************************!*\
  !*** ./src/splitOptions.ts ***!
  \*****************************/
/***/ ((__unused_webpack_module, exports) => {



Object.defineProperty(exports, "__esModule", ({
  value: true
}));

/***/ }),

/***/ "./src/sqlSplitter.ts":
/*!****************************!*\
  !*** ./src/sqlSplitter.ts ***!
  \****************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {



function _typeof(obj) { "@babel/helpers - typeof"; return _typeof = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (obj) { return typeof obj; } : function (obj) { return obj && "function" == typeof Symbol && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }, _typeof(obj); }

var __importDefault = this && this.__importDefault || function (mod) {
  return mod && mod.__esModule ? mod : {
    "default": mod
  };
};

Object.defineProperty(exports, "__esModule", ({
  value: true
}));
exports.split = void 0;

var standard_splitter_1 = __importDefault(__webpack_require__(/*! ./db/standard.splitter */ "./src/db/standard.splitter.ts"));

var splitters = {
  standard: standard_splitter_1["default"]
}; // default option

var defaultOptions = {
  language: "standard",
  keepComment: true,
  findLine: -1,
  findCharPos: -1
}; // split function

var split = function split(sql) {
  var cfg = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

  if (typeof sql !== "string") {
    throw new Error("Invalid sql argument. Expected string, instead got " + _typeof(sql));
  }

  var options = Object.assign(Object.assign({}, defaultOptions), cfg);
  var Splitter = splitters[options.language];
  return new Splitter(options).split(sql, cfg.findLine, cfg.findCharPos);
};

exports.split = split;

/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The module cache
/******/ 	var __webpack_module_cache__ = {};
/******/ 	
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/ 		// Check if module is in cache
/******/ 		var cachedModule = __webpack_module_cache__[moduleId];
/******/ 		if (cachedModule !== undefined) {
/******/ 			return cachedModule.exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = __webpack_module_cache__[moduleId] = {
/******/ 			// no module.id needed
/******/ 			// no module.loaded needed
/******/ 			exports: {}
/******/ 		};
/******/ 	
/******/ 		// Execute the module function
/******/ 		__webpack_modules__[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/ 	
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/ 	
/************************************************************************/
/******/ 	
/******/ 	// startup
/******/ 	// Load entry module and return exports
/******/ 	// This entry module is referenced by other modules so it can't be inlined
/******/ 	var __webpack_exports__ = __webpack_require__("./src/index.ts");
/******/ 	
/******/ 	return __webpack_exports__;
/******/ })()
;
});
//# sourceMappingURL=varsql.db.util.js.map