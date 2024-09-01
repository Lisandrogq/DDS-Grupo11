function validar() {
	let mail = document.getElementById("mail").value;
	let contrasena= document.getElementById("contrasena").value;
    let contrasenaConfirmada = document.getElementById("contrasenaConfirmada").value;

    if(mail==""||contrasena=="")
    alert("Completar todos los campos");

    if(mail == "admin"||contrasena=="admin"||contrasena=="1234") 
    alert("La contraseña ingresada no está permitida");
    
    if(contrasena!=contrasenaConfirmada)
    alert("Las contraseñas no coinciden");
}