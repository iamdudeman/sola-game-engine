onmessage = function (event) {
    console.log("worker received", event.data);
}


postMessage("hi");
