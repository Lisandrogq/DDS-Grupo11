const googleBtn = document.querySelector("#google-login-btn");
const githubBtn = document.querySelector("#github-login-btn");

const handleOAuthLogin = async (provider, token) => {
	const formData = new FormData();
	formData.append("provider", provider);
	formData.append("token", token);

	const response = await fetch("/user/login/", {
		method: "POST",
		headers: {
			Accept: "application/json",
		},
		body: formData,
	});

	if (response.redirected) {
		const allowedDomains = ["https://fridgebridge.simplecharity.com"];

		const url = new URL(redirectedUrl);
		const domain = url.hostname;

		if (allowedDomains.includes(domain)) {
			window.location.href = redirectedUrl;
		} else {
			alert("Redirecting to an untrusted site is not allowed.");
		}
	}
};

const startApp = () => {
	// google listener
	window.onGoogleLibraryLoad = () => {
		google.accounts.id.initialize({
			client_id: "933613917422-bb9a9ptfqof8saqbc5ub1gr5mtpoohh2.apps.googleusercontent.com",
			callback: (googleUser) => handleOAuthLogin("Google", googleUser.credential),
		});
		const parent = document.querySelector("#google-btn-wrapper");
		google.accounts.id.renderButton(parent, {
			type: "icon",
		});
		const googleLoginWrapperButton = parent.querySelector("div[role=button]");
		googleBtn.onclick = () => googleLoginWrapperButton.click();
	};

	// github listener
	githubBtn.onclick = () => {
		const githubAuthUrl = `https://github.com/login/oauth/authorize?scope=user:email&client_id=Ov23lio6HofL2ZF56U32&redirect_uri=${encodeURIComponent(
			"https://fridgebridge.simplecharity.com/user/login/github"
		)}`;
		window.location.href = githubAuthUrl;
	};
};

startApp();
