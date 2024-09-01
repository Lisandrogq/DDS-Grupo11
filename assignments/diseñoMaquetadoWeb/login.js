function actualizarConNombre() {
	let mail = document.getElementById("contrasena").value;
	let contrasena= document.getElementById("contrasena").value;
    if(mail==""||contrasena=="")
    alert("Completar todos los campos");

    if(mail == "admin"||contrasena=="admin"||contrasena=="1234") 
    alert("La contraseña ingresada no está permitida");
    
}