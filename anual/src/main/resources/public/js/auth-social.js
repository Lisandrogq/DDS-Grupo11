const googleBtn = document.querySelector("#google-connect-btn");
const githubBtn = document.querySelector("#github-connect-btn");

googleBtn.onclick = () => {};

var googleUser = {};
const startApp = () => {
    gapi.load("auth2", function () {
        auth2 = gapi.auth2.init({
            client_id: "933613917422-bb9a9ptfqof8saqbc5ub1gr5mtpoohh2.apps.googleusercontent.com",
            cookiepolicy: "single_host_origin",
        });
        attachSignin(document.getElementById("#google-auth-btn"));
    });
};

const attachSignin = (element) => {
    auth2.attachClickHandler(
        element,
        {},
        async (googleUser) => {
            const id_token = googleUser.getAuthResponse().id_token;
            const req = await fetch("/user/login/provider", {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ id_token, provider: "google" }),
            });
            const res = await req.json();

            if (res?.status != 200) {
                alert(`Could not login: ${res?.message}`);
            }
        },
        (error) => {
            alert(`Could not login: ${error}`);
        }
    );
};

startApp();
