onmessage = function (event) {
  console.log("worker received", event.data);
  var data = event.data;
  var payload = event.payload;

  switch (data.type) {
    case "start": {
      main();
      break;
    }
    case "resize": {
      // todo
      break
    }
    case "keyboard": {
      // todo
      break;
    }
    case "mouse": {
      // todo
      break;
    }
  }
}



function main() {
  postMessage({
    type: "initKeyboard",
    payload: {
      eventName: "keydown",
    }
  });

  postMessage({
    type: "initMouse",
    payload: {
      eventName: "mousedown"
    }
  });
}
