function login() {
	var loginForm = document.getElementById("loginForm");
	if (loginForm.username_fcs.value == "") {
		var message = document.getElementById("message");
		message.innerHTML = "<font color='red'>???????????????????</font>";
		return false;
	}
	if (loginForm.password_fcs.value == "") {
		var message = document.getElementById("message");
		message.innerHTML = "<font color='red'>????????????????</font>";
		return false;
	}
	
	var submitForm = document.getElementById("submitForm");
	submitForm.username_fcs.value = loginForm.username_fcs.value;
	submitForm.response_fcs.value = hex_md5(loginForm.challenge_fcs.value+loginForm.password_fcs.value);
	submitForm.remember_fcs.value = loginForm.remember_fcs.checked;
	submitForm.submit();
}

function detectEnter(evt)
{
	//press enter?
	if(evt.keyCode == 13)
	{
		login();
	}
}