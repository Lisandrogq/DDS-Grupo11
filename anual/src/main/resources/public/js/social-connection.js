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
        attachSignin(document.getElementById("#google-connect-btn"));
    });
};

const attachSignin = (element) => {
    auth2.attachClickHandler(
        element,
        {},
        (googleUser) => {
            // TODO send request to server to add social account with token
            document.getElementById("name").innerText =
                "Signed in: " + googleUser.getBasicProfile().getName();
        },
        (error) => {}
    );
};

startApp();
