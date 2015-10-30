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

// http://stackoverflow.com/a/30538844
$(document).ready(function() {
	// http://stackoverflow.com/a/30538844
    $('#salsa-parameters-form').parsley().subscribe('parsley:form:validate', function (formInstance) {
        // If any of these fields are valid
        if ($("textarea[name=inputText]").parsley().isValid() || 
            $("input[name=inputFile]").parsley().isValid()) 
        {
            // Remove the error message
            $('.invalid-form-error-message').html('');

            // Remove the required validation from all of them, so the form gets submitted
            // We already validated each field, so one is filled.
            // Also, destroy parsley's object
            $("textarea[name=inputText]").removeAttr('required').parsley().destroy();
            $("input[name=inputFile]").removeAttr('required').parsley().destroy();

            return;
        }

        // If none is valid, add the validation to them all
        $("textarea[name=inputText]").attr('required', 'required').parsley();
        $("input[name=inputFile]").attr('required', 'required').parsley();

        // stop form submission
        formInstance.submitEvent.preventDefault();

        // and display a gentle message
        $('#validation-errors').show();
        $('#validation-errors-message').html("An input text or a file is required to perfrom alignment");
        return;
    });
});