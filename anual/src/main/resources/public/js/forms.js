function validar() {
    const password = document.getElementById('password').value;
    const confirmedPassword = document.getElementById('confirmedPassword').value;
    const errorMessage = document.getElementById('error-message');

    if (password !== confirmedPassword) {
        errorMessage.textContent = "Passwords do not match. Please try again.";
        errorMessage.style.display = "block";
    } else {
        errorMessage.style.display = "none";
        document.querySelector('form').submit();
    }
}