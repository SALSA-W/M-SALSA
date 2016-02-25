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

$(document).ready(
		function() {
			poll(7, 500, getUrlParameter('id'));
		});

function poll(maxDelayIncrement, delayMilliseconds, processId) {
	$.ajax({
		url : 'AlignmentChecker?id=' + processId,
		type : "GET",
		statusCode : {
			200 : function(responseText) {
				var redirectUrl = 'AlignmentResultServlet?id=' + getUrlParameter('id');
				window.location.href = redirectUrl;
			},

			202 : function() {
				// http://jsfiddle.net/pajtai/pLka0ow9/
				var exponentialBackoffDelay = delayMilliseconds;
				if (maxDelayIncrement > 0) {
					// Exponential backoff
					exponentialBackoffDelay = delayMilliseconds * 1.5;
				}

				// Polling:
				// http://stackoverflow.com/questions/6835835/jquery-simple-polling-example
				setTimeout(function() {
					poll(--maxDelayIncrement, exponentialBackoffDelay, processId)
				}, exponentialBackoffDelay);
			},
		},
		error : function() {
			// TODO - manage errors
			alert("Processes doesn't exists");
		},
		timeout : 2000,
	});
}

// http://stackoverflow.com/a/21903119
function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) 
        {
            return sParameterName[1];
        }
    }
}  