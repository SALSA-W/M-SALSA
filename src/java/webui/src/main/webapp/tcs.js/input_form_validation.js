/**
 * Copyright 2016 Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/// <reference path="../typings/main.d.ts" />
"use strict";
var AminoAcidsSequenceContent = [
    "A",
    "C",
    "D",
    "E",
    "F",
    "G",
    "H",
    "I",
    "K",
    "L",
    "M",
    "N",
    "P",
    "Q",
    "R",
    "S",
    "T",
    "V",
    "W",
    "Y",
    // not possible two differentiate two closely related amino acids
    "B",
    "Z",
];
var NewLine = "\n";
var EmptyString = "";
var WhiteSpace = " ";
var HTMLNewLine = "<br>";
var SequenceStart = ">";
var SequenceInputTextId = "manualInputSequence";
var ValidationErrorsMessageId = "validation-errors-message";
var ValidationErrorsId = "validation-errors";
var ProteinValidator = (function () {
    function ProteinValidator() {
    }
    ProteinValidator.Validate = function (input) {
        var sequenceName;
        var start;
        var line = 1;
        var errors = [];
        var htmlContent = EmptyString;
        if (input[0] != SequenceStart) {
            errors.push("Missing &gt at descprion beginning. Unable to manage first line.");
            sequenceName = "--- no name ---";
        }
        else {
            // Get first line as description
            var nameLegth = input.indexOf(NewLine);
            sequenceName = input.substring(1, nameLegth);
            // Sequence start from next line
            start = nameLegth + 1;
            line++;
            htmlContent += "&gt" + sequenceName + HTMLNewLine;
        }
        for (var i = start, len = input.length; i < len; i++) {
            if (input[i] === SequenceStart) {
                // Search next sequence name and save it
                var nameLegth = input.indexOf(NewLine, i);
                sequenceName = input.substring(i + 1, nameLegth);
                // Sequence content start from next line
                start = nameLegth + 1;
                // Continue validation from start position
                i = start;
                line++;
                htmlContent += "&gt" + sequenceName + HTMLNewLine;
            }
            if (input[i] === NewLine) {
                line++;
                htmlContent += HTMLNewLine;
            }
            else if (input[i] != WhiteSpace && AminoAcidsSequenceContent.indexOf(input[i]) === -1) {
                // Not found
                var error = "Invalid character '" + input[i] + "'";
                error += " in sequence '" + sequenceName;
                error += "' at line " + line;
                errors.push(error);
                // Use bootstrap style
                htmlContent += '<strong class="bg-info">' + input[i] + '</strong>';
            }
            else {
                htmlContent += input[i];
            }
        }
        if (errors.length == 0) {
            return null;
        }
        var result = "The errors are:";
        errors.forEach(function (error) { return result += "<p>" + error + "</p>"; });
        result += "<p>" + htmlContent + "</p>";
        return result;
    };
    return ProteinValidator;
}());
function validateSequenceInputText() {
    var inputSequences = $("#" + SequenceInputTextId).val();
    // Avoid to manage empty strings
    if (inputSequences != null &&
        inputSequences.trim() != EmptyString) {
        // Reset error
        $("#" + ValidationErrorsMessageId).html(null);
        var htmlValidationResults = ProteinValidator.Validate(inputSequences);
        if (htmlValidationResults != null) {
            // Set error content and show
            $("#" + ValidationErrorsMessageId).html(htmlValidationResults);
            $("#" + ValidationErrorsId).show();
            return false;
        }
        $("#" + ValidationErrorsId).hide();
    }
    return true;
}
$("#" + SequenceInputTextId).bind('input propertychange', function () {
    validateSequenceInputText();
});
//# sourceMappingURL=input_form_validation.js.map