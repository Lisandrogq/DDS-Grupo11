let elementoBtnEliminar = document.getElementById("btnEliminarInput");
let elementoBtnCrear = document.getElementById("btnCrearInput");

//Agrega evento click al elemento Botón Eliminar
elementoBtnEliminar.addEventListener("click", function () {
	eliminarInput();
});

//Agrega evento click al elemento Botón Crear
elementoBtnCrear.addEventListener("click", function () {
	agregarInput();
});

function validar() {
	let mail = document.getElementById("mail").value;
	let contrasena = document.getElementById("contrasena").value;
	let contrasenaConfirmada = document.getElementById("contrasenaConfirmada").value;

	if (mail == "" || contrasena == "") alert("Completar todos los campos");

	if (mail == "admin" || contrasena == "admin" || contrasena == "1234")
		alert("La contraseña ingresada no está permitida");

	if (contrasena != contrasenaConfirmada) alert("Las contraseñas no coinciden");
}

function eliminarInput() {
	var inputs = document.getElementById("input-placeholder").querySelectorAll(".input");
	inputs[inputs.length - 1].remove();
}

function agregarInput() {
	let div = document.createElement("div");
	div.classList.add("input");
	div.innerHTML =
		' <input type="text" id="meal" name="meal" required placeholder="ID of meal to distribute..."class="col-12 inputs">';
	document.getElementById("input-placeholder").appendChild(div);
}
