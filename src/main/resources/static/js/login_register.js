const signUpSwitch = document.getElementById('signUp');
const signInSwitch = document.getElementById('signIn');
const container = document.getElementById('container');
const signUpBtn = document.getElementById('sign-up-btn');
const passwordRegister = document.getElementById('register-password');
const usernameRegister = document.getElementById('register-username');
const emailRegister = document.getElementById('register-email');
const registerErrorMsg = document.getElementById('register-error-message')
const baseUrl = "/"

signUpSwitch.addEventListener('click', () => {
	container.classList.add("right-panel-active");
});

signInSwitch.addEventListener('click', () => {
	container.classList.remove("right-panel-active");
});

signUpBtn.addEventListener('click', signUp)

function signUp(){
	$.ajax({
		url: baseUrl + "api/register",
		method: "post",
		async: true,
		contentType: 'application/json',
		data: JSON.stringify({
			"username": usernameRegister.value,
			"email": emailRegister.value,
			"password": passwordRegister.value,
			"imgUrl": ""
		}),
		success: (response) => {
			if(response.status){
				registerErrorMsg.textContent = ""
				window.location.href = baseUrl + "login"	
			}
		},
		error: (jqXHR) => {
			parseErrorResponse(jqXHR)
		}
	})
}

function parseErrorResponse(jqXHR){
	var responseText = jqXHR.responseText
	var responseObject = {}
	if(JSON.parse(responseText)){
		responseObject = JSON.parse(responseText)
		registerErrorMsg.textContent = responseObject.message
	}else
		registerErrorMsg.textContent = "Something went wrong, try again"
}