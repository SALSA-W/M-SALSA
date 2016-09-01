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

const DynamicListItemId: string = "newitem";
const DynamicListBtnAdd:string = "btnAdd";

// http://codereview.stackexchange.com/questions/85116/simple-todo-list
function initToDoList() {

	var textBox = $("#" + DynamicListItemId);
	var list = $("#dynamicList"); 
    
    // Attach evet to button
	$("#" + DynamicListBtnAdd).click(function() { addItem() });
    
    function createSpacerSpanLink() {
		var deleteLink = document.createElement('span');
        // Use JQuery to avoid property problems
        $(deleteLink).text(" ");
		return deleteLink;
	}

	function createDeleteLink() {

		var deleteLink = document.createElement('a');
		deleteLink.setAttribute("href", "#");
		deleteLink.setAttribute("class", "delete-link");
		deleteLink.innerHTML = "x";
		deleteLink.addEventListener("click", removeItem);
		return deleteLink;
	}

	function addItem() {

		var listElement = document.createElement('li');
        // Use JQuery to avoid property problems
		$(listElement).text(textBox.val());
        listElement.appendChild(createSpacerSpanLink());
		listElement.appendChild(createDeleteLink());

		list.append(listElement);

		textBox.val("");
	}

	function removeItem() {
		var parent = this.parentNode.parentNode;
		var child = this.parentNode

		parent.removeChild(child);
	}
}

initToDoList();