/**
 * Copyright 2015 Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
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
/// <reference path="input_form_validation.ts"/>
"use strict";
// http://stackoverflow.com/a/3261380
function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}
var InputFileId = "inputFile";
// http://stackoverflow.com/a/30538844
$(document).ready(function () {
    // http://stackoverflow.com/a/30538844
    $('#salsa-parameters-form').parsley().subscribe('parsley:form:validate', function (formInstance) {
        // If any of these fields are valid
        if (($("textarea[name=" + SequenceInputTextId + "]").parsley().isValid() && validateSequenceInputText() === true) ||
            $("input[name=" + InputFileId + "]").parsley().isValid() ||
            $('#dynamicList').children('li').length > 2) {
            // Remove the error message
            $('.invalid-form-error-message').html('');
            // Remove the required validation from all of them, so the form gets submitted
            // We already validated each field, so one is filled.
            // Also, destroy parsley's object
            $("textarea[name=" + SequenceInputTextId + "]").removeAttr('required').parsley().destroy();
            $("input[name=" + InputFileId + "]").removeAttr('required').parsley().destroy();
            $("input[name=" + DynamicListItemId + "]").removeAttr('required').parsley();
            return;
        }
        // If none is valid, add the validation to them all
        $("textarea[name=" + SequenceInputTextId + "]").attr('required', 'required').parsley();
        $("input[name=" + InputFileId + "]").attr('required', 'required').parsley();
        $("input[name=" + DynamicListItemId + "]").attr('required', 'required').parsley();
        // stop form submission
        formInstance.submitEvent.preventDefault();
        // and display a gentle message
        $('#validation-errors').show();
        $('#validation-errors-message').html("An input text or at least 3 UniProt Ids or a file is required to perfrom alignment");
        return;
    });
    $.listen('parsley:field:success', function (ParsleyField) {
        // Show the title to add to email if e-mail is correct
        if (ParsleyField.$element.attr('name') === 'recipientEmail') {
            if (isBlank($("#recipientEmail").val()) === false) {
                $("div#userJobTitleForm").removeClass('hidden').addClass('show');
            }
        }
    });
    $.listen('parsley:field:error', function (ParsleyField) {
        // Hide the title to add to email if e-mail isn't correct
        if (ParsleyField.$element.attr('name') === 'recipientEmail') {
            $("div#userJobTitleForm").removeClass('show').addClass('hidden');
        }
    });
});
function SetUniProtIdsValues() {
    if ($('#dynamicList').children('li').length > 0) {
        // Compose value to pass to servlet
        var uniProtIdsValueArray = Array();
        $('#dynamicList li').each(function () {
            uniProtIdsValueArray.push($(this).text().slice(0, -1));
        });
        $('<input>').attr({
            type: 'hidden',
            id: 'uniProtIds',
            name: 'uniProtIds',
            value: uniProtIdsValueArray.join(',')
        }).appendTo("#salsa-parameters-form");
    }
}
;
function submitSalsaParametersForm() {
    // Set correct values for hidden input
    SetUniProtIdsValues();
}
$("#" + DynamicListItemId).bind('input propertychange', function () {
    // Force validation
    var buttonDisabled = false;
    var dynamicListItem = $("input[name=" + DynamicListItemId + "]").parsley();
    // Check regex  
    if (dynamicListItem.isValid() === false) {
        dynamicListItem.validate();
        buttonDisabled = true;
    }
    else {
        // Show loading during validation
        $("#" + ValidatingModalItemId).modal();
        asyncUniProtValidation(function (success) {
            $("#" + DynamicListBtnAdd).prop("disabled", success == false);
            // Hide loading during validation
            $("#" + ValidatingModalItemId).modal('hide');
        });
    }
    $("#" + DynamicListBtnAdd).prop("disabled", buttonDisabled);
});
var ErrorInvalidIdLabel = "invalidIdResource";
var ValidatingModalItemId = "validatingModal";
function asyncUniProtValidation(callback) {
    // Generate request based on inserted value
    $.ajax({
        url: "http://www.uniprot.org/uniprot/" + $("#" + DynamicListItemId).val() + ".fasta",
        type: "HEAD",
        statusCode: {
            200: function () {
                // Remove errors
                var dynamicListItem = $("input[name=" + DynamicListItemId + "]").parsley();
                window.ParsleyUI.removeError(dynamicListItem, ErrorInvalidIdLabel);
                dynamicListItem.validate();
                callback(true);
            }
        },
        error: function () {
            // Add errors
            var dynamicListItem = $("input[name=" + DynamicListItemId + "]").parsley();
            // Clear previous errors
            var errors = window.ParsleyUI.getErrorsMessages(dynamicListItem);
            var oldError;
            for (var _i = 0, errors_1 = errors; _i < errors_1.length; _i++) {
                oldError = errors_1[_i];
                window.ParsleyUI.removeError(dynamicListItem, name);
            }
            // Add new errors
            window.ParsleyUI.addError(dynamicListItem, ErrorInvalidIdLabel, "The input value doesn't exists inside UniProt");
            callback(false);
        },
        timeout: 2000
    });
}
//# sourceMappingURL=index.js.map