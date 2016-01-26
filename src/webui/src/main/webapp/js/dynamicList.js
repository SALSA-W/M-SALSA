// http://codereview.stackexchange.com/questions/85116/simple-todo-list
function initToDoList() {

	var textBox = document.getElementById('newitem');
	var list = document.getElementById('dynamicList');
	var addButton = document.getElementById('btnAdd');

	addButton.addEventListener("click", addItem);

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
		listElement.innerText = textBox.value;
		listElement.appendChild(createDeleteLink());

		list.appendChild(listElement);

		textBox.value = "";
	}

	function removeItem() {

		var parent = this.parentNode.parentNode;
		var child = this.parentNode

		parent.removeChild(child);

	}
}

initToDoList();